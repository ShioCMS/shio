package com.viglet.shiohara.api.object;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShObjectAPITest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	private String siteObjectId = "c5bdee96-6feb-4894-9daf-3aab6cdd5087";

	private String folderObjectId = "fdbff43b-cc7e-40f1-80a1-062bb08ae5d0";

	private String postObjectId = "7887cd98-a593-4e3c-a7d9-eabf41103d03";

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
				.andExpect(content().contentType("application/json;charset=UTF-8"));

	}

	@Test
	public void shObjectListItemFolder() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + folderObjectId + "/list")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));

	}


	// shObjectListItem

	@Test
	public void shFolderListByPostTypeSite() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + siteObjectId + "/list/PT-TEXT")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));

	}

	@Test
	public void shFolderListByPostTypeFolder() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + folderObjectId + "/list/PT-TEXT")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));

	}

	// shObjectPath

	@Test
	public void shObjectPathSite() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + siteObjectId + "/path")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));

	}

	@Test
	public void shObjectPathFolder() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + folderObjectId + "/path")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));

	}

	@Test
	public void shObjectPathPost() throws Exception {
		mockMvc.perform(get("/api/v2/object/" + postObjectId + "/path")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));

	}

}