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

	private String siteObjectId = "1b610b31-7e7a-403e-ab09-986a8f11711a";

	private String folderObjectId = "f0e8030e-8077-44d3-83cb-86fcb3711f3b";

	private String postObjectId = "a814fe3f-8293-425f-8c4c-3a094fd8b0dc";

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