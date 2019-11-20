package com.viglet.shiohara.api.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.api.folder.ShFolderList;
import com.viglet.shiohara.bean.ShFolderTinyBean;
import com.viglet.shiohara.bean.ShPostTinyBean;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.provider.otcs.ShOTCSProvider;
import com.viglet.shiohara.provider.otcs.bean.result.ShOTCSResultsBean;
import com.viglet.shiohara.provider.otcs.bean.result.ShOTCSResultsDataBean;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/provider")
@Api(tags = "Provider", description = "Provider API")
public class ShProviderAPI {
	private static final Log logger = LogFactory.getLog(ShProviderAPI.class);
	@Autowired
	private ShOTCSProvider shOTCSProvider;

	@GetMapping("/{id}/download")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public StreamingResponseBody shProviderDownloadItem(@PathVariable int id) {
		shOTCSProvider.init("http://localhost/OTCS/cs.exe", "admin", "admin");

		InputStream is = shOTCSProvider.getDownload(id);

		return new StreamingResponseBody() {
			@Override
			public void writeTo(java.io.OutputStream output) throws IOException {

				try {
					IOUtils.copy(is, output);
				} catch (IOException ex) {
					logger.error("shProviderDownloadItemIOException", ex);
				} catch (Exception e) {
					logger.error("shProviderDownloadItemtException", e);
				}
			}
		};
	}

	@GetMapping("/{id}/list")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderList shObjectListItem(@PathVariable int id) {
		shOTCSProvider.init("http://localhost/OTCS/cs.exe", "admin", "admin");

		ShFolderList shFolderList = new ShFolderList();

		Set<ShFolderTinyBean> shFolderTinyBeans = new HashSet<>();
		List<ShPostTinyBean> shPostTinyBeans = new ArrayList<>();
		for (ShOTCSResultsBean results : shOTCSProvider.getFolder(id).getResults()) {

			if (results.getData().getProperties().getType_name().equals("Folder")) {
				String resultId = Integer.toString(results.getData().getProperties().getId());
				String resultName = results.getData().getProperties().getName();
				int resultPosition = 0;
				Date resultDate = results.getData().getProperties().getCreate_date();

				ShFolderTinyBean shFolderTinyBean = new ShFolderTinyBean(resultId, resultName, resultPosition,
						resultDate);
				shFolderTinyBeans.add(shFolderTinyBean);
			} else {

				String postId = Integer.toString(results.getData().getProperties().getId());

				String postTitle = results.getData().getProperties().getName();

				String postSummary = null;

				int postPosition = 0;

				Date postDate = results.getData().getProperties().getCreate_date();

				String shPostTypeId = null;

				String shPostTypeName = results.getData().getProperties().getType_name();

				String shPostTypeTitle = results.getData().getProperties().getType_name();

				String objectType = "Post";

				String publishStatus = null;

				boolean published = true;

				ShPostTinyBean shPostTinyBean = new ShPostTinyBean(postId, postTitle, postSummary, postPosition,
						postDate, shPostTypeId, shPostTypeName, shPostTypeTitle, objectType, publishStatus, published);

				shPostTinyBeans.add(shPostTinyBean);

			}
		}

		ShOTCSResultsDataBean currentObject = shOTCSProvider.getObject(id).getResults().getData();

		ArrayList<ShFolder> breadcrumb = new ArrayList<>();

		ShFolder shFolder = new ShFolder();
		shFolder.setName(currentObject.getProperties().getName());
		shFolder.setId(Integer.toString(currentObject.getProperties().getId()));
		shFolder.setDate(currentObject.getProperties().getCreate_date());

		int parentId = currentObject.getProperties().getParent_id();

		this.getParentFolder(parentId, breadcrumb);

		breadcrumb.add(shFolder);

		ShSite shSite = new ShSite();
		shSite.setName("OTCS");
		shSite.setId("2000");

		shFolderList.setShFolders(shFolderTinyBeans);
		shFolderList.setShPosts(shPostTinyBeans);
		shFolderList.setFolderPath("/");
		shFolderList.setBreadcrumb(breadcrumb);
		shFolderList.setShSite(shSite);

		return shFolderList;
	}

	private void getParentFolder(int parentId, ArrayList<ShFolder> breadcrumb) {
		if (parentId > 0) {
			ShOTCSResultsDataBean parentObject = shOTCSProvider.getObject(parentId).getResults().getData();

			ShFolder shParentFolder = new ShFolder();
			shParentFolder.setName(parentObject.getProperties().getName());
			shParentFolder.setId(Integer.toString(parentObject.getProperties().getId()));
			shParentFolder.setDate(parentObject.getProperties().getCreate_date());
			this.getParentFolder(parentObject.getProperties().getParent_id(), breadcrumb);
			breadcrumb.add(shParentFolder);
		}
	}

}
