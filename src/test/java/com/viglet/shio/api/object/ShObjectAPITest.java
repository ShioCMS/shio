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

package com.viglet.shio.api.object;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.site.ShSite;
import com.viglet.shio.persistence.repository.folder.ShFolderRepository;
import com.viglet.shio.persistence.repository.site.ShSiteRepository;
import com.viglet.shio.utils.ShUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShObjectAPITest {

	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShFolderRepository shFolderRepository;
	
	private MockMvc mockMvc;

	private String siteObjectId = "c5bdee96-6feb-4894-9daf-3aab6cdd5087";

	private String folderObjectId = "fdbff43b-cc7e-40f1-80a1-062bb08ae5d0";

	private String postObjectId = "7887cd98-a593-4e3c-a7d9-eabf41103d03";

	private Principal mockPrincipal;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockPrincipal = Mockito.mock(Principal.class);
		Mockito.when(mockPrincipal.getName()).thenReturn("admin");
	}

	// shObjectMoveTo
	
	@Test
	public void shObjectMoveToFolder() throws Exception {

		ShSite shSite = shSiteRepository.findById(siteObjectId).get();

		String newFolderId = UUID.randomUUID().toString();
		ShFolder shFolder = new ShFolder();
		shFolder.setId(newFolderId);
		shFolder.setName("Unit Test Move to Folder");
		shFolder.setPosition(2);
		shFolder.setRootFolder((byte) 1);
		shFolder.setShSite(shSite);
		String folderRequestBody = ShUtils.asJsonString(shFolder);

		RequestBuilder folderRequestBuilder = MockMvcRequestBuilders.post("/api/v2/folder").principal(mockPrincipal)
				.accept(MediaType.APPLICATION_JSON).content(folderRequestBody)
				.contentType("application/json");
		
		mockMvc.perform(folderRequestBuilder).andExpect(status().isOk());
		
		List<String> objectIds = new ArrayList<String>();
		
		objectIds.add(newFolderId);
		
		String objectIdsRequestBody = ShUtils.asJsonString(objectIds);
		
		RequestBuilder moveRequestBuilder = MockMvcRequestBuilders.put("/api/v2/object/moveto/" + folderObjectId)
				.principal(mockPrincipal).accept(MediaType.APPLICATION_JSON).content(objectIdsRequestBody)
				.contentType("application/json");

		mockMvc.perform(moveRequestBuilder).andExpect(status().isOk());
	}

	@Test
	public void shObjectMoveToSite() throws Exception {

		ShFolder shParentFolder = shFolderRepository.findById(folderObjectId).get();

		String newFolderId = UUID.randomUUID().toString();
		ShFolder shFolder = new ShFolder();
		shFolder.setId(newFolderId);
		shFolder.setName("Unit Test Move to Site");
		shFolder.setPosition(2);
		shFolder.setRootFolder((byte) 0);
		shFolder.setParentFolder(shParentFolder);
		String folderRequestBody = ShUtils.asJsonString(shFolder);

		RequestBuilder folderRequestBuilder = MockMvcRequestBuilders.post("/api/v2/folder").principal(mockPrincipal)
				.accept(MediaType.APPLICATION_JSON).content(folderRequestBody)
				.contentType("application/json");
		
		mockMvc.perform(folderRequestBuilder).andExpect(status().isOk());
		
		List<String> objectIds = new ArrayList<String>();
		
		objectIds.add(newFolderId);
		
		String objectIdsRequestBody = ShUtils.asJsonString(objectIds);
		
		RequestBuilder moveRequestBuilder = MockMvcRequestBuilders.put("/api/v2/object/moveto/" + siteObjectId)
				.principal(mockPrincipal).accept(MediaType.APPLICATION_JSON).content(objectIdsRequestBody)
				.contentType("application/json");

		mockMvc.perform(moveRequestBuilder).andExpect(status().isOk());
	}
	
	// shObjectCopyTo
	
		@Test
		public void shObjectCopyToFolder() throws Exception {

			ShSite shSite = shSiteRepository.findById(siteObjectId).get();

			String newFolderId = UUID.randomUUID().toString();
			ShFolder shFolder = new ShFolder();
			shFolder.setId(newFolderId);
			shFolder.setName("Unit Test Copy to Folder");
			shFolder.setPosition(2);
			shFolder.setRootFolder((byte) 1);
			shFolder.setShSite(shSite);
			String folderRequestBody = ShUtils.asJsonString(shFolder);

			RequestBuilder folderRequestBuilder = MockMvcRequestBuilders.post("/api/v2/folder").principal(mockPrincipal)
					.accept(MediaType.APPLICATION_JSON).content(folderRequestBody)
					.contentType("application/json");
			
			mockMvc.perform(folderRequestBuilder).andExpect(status().isOk());
			
			List<String> objectIds = new ArrayList<String>();
			
			objectIds.add(newFolderId);
			
			String objectIdsRequestBody = ShUtils.asJsonString(objectIds);
			
			RequestBuilder moveRequestBuilder = MockMvcRequestBuilders.put("/api/v2/object/copyto/" + folderObjectId)
					.principal(mockPrincipal).accept(MediaType.APPLICATION_JSON).content(objectIdsRequestBody)
					.contentType("application/json");

			mockMvc.perform(moveRequestBuilder).andExpect(status().isOk());
		}

		@Test
		public void shObjectCopyToSite() throws Exception {

			ShFolder shParentFolder = shFolderRepository.findById(folderObjectId).get();

			String newFolderId = UUID.randomUUID().toString();
			ShFolder shFolder = new ShFolder();
			shFolder.setId(newFolderId);
			shFolder.setName("Unit Test Copy to Site");
			shFolder.setPosition(2);
			shFolder.setRootFolder((byte) 0);
			shFolder.setParentFolder(shParentFolder);
			String folderRequestBody = ShUtils.asJsonString(shFolder);

			RequestBuilder folderRequestBuilder = MockMvcRequestBuilders.post("/api/v2/folder").principal(mockPrincipal)
					.accept(MediaType.APPLICATION_JSON).content(folderRequestBody)
					.contentType("application/json");
			
			mockMvc.perform(folderRequestBuilder).andExpect(status().isOk());
			
			List<String> objectIds = new ArrayList<String>();
			
			objectIds.add(newFolderId);
			
			String objectIdsRequestBody = ShUtils.asJsonString(objectIds);
			
			RequestBuilder moveRequestBuilder = MockMvcRequestBuilders.put("/api/v2/object/copyto/" + siteObjectId)
					.principal(mockPrincipal).accept(MediaType.APPLICATION_JSON).content(objectIdsRequestBody)
					.contentType("application/json");

			mockMvc.perform(moveRequestBuilder).andExpect(status().isOk());
		}
		
	// shObjectPreview

	@Test
	public void shObjectPreviewSite() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + siteObjectId + "/preview")).andExpect(status().is3xxRedirection());

	}

	@Test
	public void shObjectPreviewFolder() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + folderObjectId + "/preview")).andExpect(status().is3xxRedirection());

	}

	@Test
	public void shObjectPreviewPost() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + postObjectId + "/preview")).andExpect(status().is3xxRedirection());

	}

	// shObjectListItem

	@Test
	public void shObjectListItemSite() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + siteObjectId + "/list")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));

	}

	@Test
	public void shObjectListItemFolder() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + folderObjectId + "/list")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));

	}

	// shObjectListItem

	@Test
	public void shFolderListByPostTypeSite() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + siteObjectId + "/list/PT-TEXT")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));

	}

	@Test
	public void shFolderListByPostTypeFolder() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + folderObjectId + "/list/PT-TEXT")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));

	}

	// shObjectPath

	@Test
	public void shObjectPathSite() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + siteObjectId + "/path")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));

	}

	@Test
	public void shObjectPathFolder() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + folderObjectId + "/path")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));

	}

	@Test
	public void shObjectPathPost() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + postObjectId + "/path")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));

	}

}