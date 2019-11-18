package com.viglet.shiohara.spreedsheet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.widget.ShSystemWidget;

@Component
public class ShSpreadsheet {
	static final Logger logger = LogManager.getLogger(ShSpreadsheet.class.getName());
	static final int MIN_COL_WIDTH = 20 << 8;
	static final int MAX_COL_WIDTH = 100 << 8;

	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostUtils shPostUtils;

	public void generate(ShFolder shFolder, HttpServletResponse response) {

		try (Workbook workbook = new XSSFWorkbook()) {
			Map<String, List<ShPost>> shPostsByTypeMap = shPostMap(shFolder);
			for (Entry<String, List<ShPost>> entryPost : shPostsByTypeMap.entrySet()) {
				List<ShPost> shPostsEntry = entryPost.getValue();

				if (shPostsEntry != null && !shPostsEntry.isEmpty()) {
					XSSFSheet sheet = this.tableDefinition(workbook, entryPost.getKey(), shPostsEntry);
					this.tableRows(workbook, sheet, shPostsEntry);
					this.autoSize(sheet, shPostsEntry.get(0).getShPostAttrs().size());
				}
			}

			download(workbook, shFolder.getFurl(), response);
		} catch (IOException e) {
			logger.error("Spreadsheet error: ", e);
		}

	}

	private Map<String, List<ShPost>> shPostMap(ShFolder shFolder) {
		List<ShPost> shPosts = shPostRepository.findByShFolderOrderByShPostType(shFolder);

		Map<String, List<ShPost>> shPostsByTypeMap = new HashMap<>();

		String postTypeName = null;
		List<ShPost> shPostsByType = null;
		for (ShPost shPost : shPosts) {
			shPostUtils.syncWithPostType(shPost);
			if (postTypeName == null) {
				postTypeName = shPost.getShPostType().getTitle();
				shPostsByType = new ArrayList<>();
			} else if (!postTypeName.equals(shPost.getShPostType().getTitle())) {
				shPostsByTypeMap.put(postTypeName, new ArrayList<>(shPostsByType));
				shPostsByType = new ArrayList<>();
				postTypeName = shPost.getShPostType().getTitle();
			}
			shPostsByType.add(shPost);

		}
		shPostsByTypeMap.put(postTypeName, new ArrayList<>(shPostsByType));
		return shPostsByTypeMap;
	}

	private void download(Workbook workbook, String name, HttpServletResponse response) {

		String strDate = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
		String fileName = name + "_" + strDate + ".xlsx";

		response.addHeader("Content-disposition", "attachment;filename=" + fileName);
		response.setContentType("application/octet-stream");
		response.setStatus(HttpServletResponse.SC_OK);
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			workbook.write(out);
		} catch (IOException e1) {
			logger.error("Spreadsheet download error: ", e1);
		} finally {
			try {
				if (out != null)
					out.close();
				if (workbook != null)
					workbook.close();
			} catch (IOException e) {
				logger.error("Spreadsheet download error: ", e);
			}
		}
	}

	private void tableRows(Workbook workbook, XSSFSheet sheet, List<ShPost> shPostsEntry) {
		CreationHelper createHelper = workbook.getCreationHelper();

		int columnCount;
		int rowCount = 0;
		for (ShPost shPost : shPostsEntry) {

			rowCount++;
			columnCount = 0;
			CellStyle cellTextStyle = workbook.createCellStyle();
			cellTextStyle.setVerticalAlignment(VerticalAlignment.TOP);

			CellStyle cellTextAreaStyle = workbook.createCellStyle();
			cellTextAreaStyle.setWrapText(true);
			cellTextAreaStyle.setVerticalAlignment(VerticalAlignment.TOP);

			CellStyle cellDateStyle = workbook.createCellStyle();
			cellDateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
			cellDateStyle.setVerticalAlignment(VerticalAlignment.TOP);

			XSSFRow row = sheet.createRow(rowCount);
			Cell cellDate = row.createCell(0);
			cellDate.setCellValue((Date) shPost.getDate());
			cellDate.setCellStyle(cellDateStyle);

			for (ShPostAttr shPostAttr : shPostUtils.postAttrsSort(shPost.getShPostAttrs())) {
				columnCount++;
				String widgetName = shPostAttr.getShPostTypeAttr().getShWidget().getName();
				if (!widgetName.contains(ShSystemWidget.TAB)) {
					XSSFCell cell = row.createCell(columnCount);
					if (widgetName.equals(ShSystemWidget.DATE)) {
						cell.setCellValue((Date) shPostAttr.getDateValue());
						cell.setCellStyle(cellDateStyle);
					} else if (widgetName.equals(ShSystemWidget.CONTENT_SELECT)
							|| widgetName.equals(ShSystemWidget.FILE)) {
						String value = null;
						if (shPost.getShPostType().getName().equals(ShSystemPostType.FILE)) {
							value = shPostAttr.getStrValue();
						} else {
							ShObject shObject = shPostAttr.getReferenceObject();

							if (shObject instanceof ShFolder) {
								value = ((ShFolder) shObject).getName();
							} else if (shObject instanceof ShPost) {
								value = ((ShPost) shObject).getTitle();
							} else if (shObject instanceof ShSite) {
								value = ((ShSite) shObject).getName();
							}
						}
						cell.setCellValue((String) value);
						cell.setCellStyle(cellTextStyle);
					} else if (widgetName.equals(ShSystemWidget.TEXT_AREA)
							|| widgetName.equals(ShSystemWidget.HTML_EDITOR)) {
						cell.setCellValue((String) shPostAttr.getStrValue());
						cell.setCellStyle(cellTextAreaStyle);
					} else if (widgetName.equals(ShSystemWidget.MULTI_SELECT)) {
						List<String> msItems = new ArrayList<>();
						for (String id : shPostAttr.getArrayValue()) {
							Optional<ShPost> shPostMultSelect = shPostRepository.findById(id);
							if (shPostMultSelect.isPresent()) {
								msItems.add(shPostMultSelect.get().getTitle());
							}
						}
						cell.setCellValue((String) String.join(", ", msItems));
						cell.setCellStyle(cellTextAreaStyle);
					} else {
						cell.setCellValue((String) shPostAttr.getStrValue());
						cell.setCellStyle(cellTextStyle);
					}
				}

			}

		}

	}

	private XSSFSheet tableDefinition(Workbook workbook, String sheetName, List<ShPost> shPostsEntry) {
		if (shPostsEntry != null && ! shPostsEntry.isEmpty()) {
			XSSFSheet sheet = (XSSFSheet) workbook.createSheet(sheetName);

			int totalCols = shPostsEntry.get(0).getShPostAttrs().size();

			int columnCount = 0;

			XSSFRow row = sheet.createRow(0);

			Cell cellHeaderDate = row.createCell(columnCount);
			cellHeaderDate.setCellValue("Date");

			for (ShPostAttr shPostAttr : shPostUtils.postAttrsSort(shPostsEntry.get(0).getShPostAttrs())) {
				columnCount++;
				XSSFCell cell = row.createCell(columnCount);
				cell.setCellValue(shPostAttr.getShPostTypeAttr().getLabel());

			}
			sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, totalCols));

			return sheet;
		} else {
			return null;
		}
	}

	private int autoSize(XSSFSheet sheet, int maxCol) {
		for (int i = 0; i <= maxCol; i++) {
			sheet.autoSizeColumn(i);

			int cw = (int) (sheet.getColumnWidth(i) * 0.8);
			sheet.setColumnWidth(i, Math.max(Math.min(cw, MAX_COL_WIDTH), MIN_COL_WIDTH));
		}
		maxCol = 0;
		return maxCol;
	}

}
