/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.viglet.shiohara.exchange.site;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viglet.shiohara.exchange.ShExchange;
import com.viglet.shiohara.exchange.ShFileExchange;
import com.viglet.shiohara.exchange.ShSiteExchange;
import com.viglet.shiohara.exchange.folder.ShFolderExport;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.utils.ShUtils;

@Component
public class ShSiteExport {
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShUtils shUtils;
	@Autowired
	private ShFolderExport shFolderExport;

	public StreamingResponseBody exportObject(@PathVariable String id, HttpServletResponse response) throws Exception {
		String folderName = UUID.randomUUID().toString();
		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			File tmpDir = new File(userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
			if (!tmpDir.exists()) {
				tmpDir.mkdirs();
			}

			ShExchange shExchange = new ShExchange();
			ShSite shSite = shSiteRepository.findById(id).get();

			List<ShFolder> rootFolders = shFolderRepository.findByShSiteAndRootFolder(shSite, (byte) 1);

			List<String> rootFoldersUUID = new ArrayList<String>();
			for (ShFolder shFolder : rootFolders) {
				rootFoldersUUID.add(shFolder.getId());
			}

			ShSiteExchange shSiteExchange = new ShSiteExchange();
			shSiteExchange.setId(shSite.getId());
			shSiteExchange.setName(shSite.getName());
			shSiteExchange.setUrl(shSite.getUrl());
			shSiteExchange.setDescription(shSite.getDescription());
			shSiteExchange.setPostTypeLayout(shSite.getPostTypeLayout());
			shSiteExchange.setSearchablePostTypes(shSite.getSearchablePostTypes());
			shSiteExchange.setDate(shSite.getDate());
			shSiteExchange.setRootFolders(rootFoldersUUID);
			shSiteExchange.setOwner(shSite.getOwner());
			shSiteExchange.setFurl(shSite.getFurl());

			List<ShSiteExchange> shSiteExchanges = new ArrayList<ShSiteExchange>();
			shSiteExchanges.add(shSiteExchange);
			shExchange.setSites(shSiteExchanges);

			ShExchange shExchangeFolder = shFolderExport.shFolderExchangeIterate(rootFolders);

			shExchange.setFolders(shExchangeFolder.getFolders());
			shExchange.setPosts(shExchangeFolder.getPosts());
			shExchange.setPostTypes(shExchangeFolder.getPostTypes());

			File exportDir = new File(tmpDir.getAbsolutePath().concat(File.separator + folderName));
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}

			for (ShFileExchange fileExchange : shExchangeFolder.getFiles()) {
				FileUtils.copyFile(fileExchange.getFile(),
						new File(exportDir.getAbsolutePath().concat(File.separator + fileExchange.getId())));
			}
			// Object to JSON in file
			ObjectMapper mapper = new ObjectMapper();
			mapper.writerWithDefaultPrettyPrinter().writeValue(
					new File(exportDir.getAbsolutePath().concat(File.separator + "export.json")), shExchange);

			File zipFile = new File(tmpDir.getAbsolutePath().concat(File.separator + folderName + ".zip"));

			shUtils.addFilesToZip(exportDir, zipFile);

			String strDate = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
			String zipFileName = shSite.getName() + "_" + strDate + ".zip";

			response.addHeader("Content-disposition", "attachment;filename=" + zipFileName);
			response.setContentType("application/octet-stream");
			response.setStatus(HttpServletResponse.SC_OK);

			return new StreamingResponseBody() {
				@Override
				public void writeTo(java.io.OutputStream output) throws IOException {

					try {
						java.nio.file.Path path = Paths.get(zipFile.getAbsolutePath());
						byte[] data = Files.readAllBytes(path);
						output.write(data);
						output.flush();

						FileUtils.deleteDirectory(exportDir);
						FileUtils.deleteQuietly(zipFile);

					} catch (IOException ex) {
						ex.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		} else {
			return null;
		}

	}
}
