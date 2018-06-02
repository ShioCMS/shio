package com.viglet.shiohara.api.folder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.utils.ShUtils;


@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShFolderAPITest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	private String newSiteId = "edcb4055-f3c5-47db-a124-1a805e00273b";

	private String newFolderId = "4c5ec489-e64b-45c1-9404-39fa44151566";

	private Principal mockPrincipal;

	@Autowired
	private ShSiteRepository shSiteRepository;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockPrincipal = Mockito.mock(Principal.class);
		Mockito.when(mockPrincipal.getName()).thenReturn("admin");
	}

	@Test
	public void shFolderList() throws Exception {
		mockMvc.perform(get("/api/v2/folder")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));
	}
	
	@Test
	public void shFolderStructure() throws Exception {
		mockMvc.perform(get("/api/v2/folder/model")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));
	}
	
	@Test
	public void stage01ShFolderAdd() throws Exception {
		ShSite shSite = new ShSite();
		shSite.setId(newSiteId);
		shSite.setDescription("Test Site");
		shSite.setName("Test");
		shSite.setUrl("http://example.com");

		String requestBody = ShUtils.asJsonString(shSite);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v2/site").principal(mockPrincipal)
				.accept(MediaType.APPLICATION_JSON).content(requestBody).contentType("application/json;charset=UTF-8")
				.header("X-Requested-With", "XMLHttpRequest");

		mockMvc.perform(requestBuilder).andExpect(status().isOk());

		ShFolder shFolder = new ShFolder();
		shFolder.setId(newFolderId);
		shFolder.setName("Unit Test");
		shFolder.setPosition(2);
		shFolder.setRootFolder((byte) 1);
		shFolder.setShSite(shSite);

		String folderRequestBody = ShUtils.asJsonString(shFolder);

		RequestBuilder folderRequestBuilder = MockMvcRequestBuilders.post("/api/v2/folder").principal(mockPrincipal)
				.accept(MediaType.APPLICATION_JSON).content(folderRequestBody)
				.contentType("application/json;charset=UTF-8").header("X-Requested-With", "XMLHttpRequest");

		mockMvc.perform(folderRequestBuilder).andExpect(status().isOk());
	}

	@Test
	public void stage02ShFolderGet() throws Exception {
		mockMvc.perform(get("/api/v2/folder/" + newFolderId)).andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"));
	}
	
	@Test
	public void stage03ShFolderPath() throws Exception {
		mockMvc.perform(get("/api/v2/folder/" + newFolderId + "/path")).andExpect(status().isOk())
		.andExpect(content().contentType("application/json;charset=UTF-8"));
	}
	
	@Test
	public void stage04ShFolderUpdate() throws Exception {
		ShSite shSite = shSiteRepository.findById(newSiteId).get();

		ShFolder shFolder = new ShFolder();
		shFolder.setId(newFolderId);
		shFolder.setName("Unit Test2");
		shFolder.setPosition(2);
		shFolder.setRootFolder((byte) 1);
		shFolder.setShSite(shSite);

		String folderRequestBody = ShUtils.asJsonString(shFolder);

		RequestBuilder folderRequestBuilder = MockMvcRequestBuilders.put("/api/v2/folder/" + newFolderId)
				.principal(mockPrincipal).accept(MediaType.APPLICATION_JSON).content(folderRequestBody)
				.contentType("application/json;charset=UTF-8").header("X-Requested-With", "XMLHttpRequest");

		mockMvc.perform(folderRequestBuilder).andExpect(status().isOk());
	}

	@Test
	public void stage05ShFolderAddFromParentObjectSite() throws Exception{

		ShFolder shFolder = new ShFolder();
		shFolder.setName("Unit Test By Object Site");

		String folderRequestBody = ShUtils.asJsonString(shFolder);

		RequestBuilder folderRequestBuilder = MockMvcRequestBuilders.post("/api/v2/folder/object/" + newSiteId)
				.principal(mockPrincipal).accept(MediaType.APPLICATION_JSON).content(folderRequestBody)
				.contentType("application/json;charset=UTF-8").header("X-Requested-With", "XMLHttpRequest");

		mockMvc.perform(folderRequestBuilder).andExpect(status().isOk());
	}

	@Test
	public void stage06ShFolderAddFromParentObjectFolder() throws Exception{

		ShFolder shFolder = new ShFolder();
		shFolder.setName("Unit Test By Object Folder");

		String folderRequestBody = ShUtils.asJsonString(shFolder);

		RequestBuilder folderRequestBuilder = MockMvcRequestBuilders.post("/api/v2/folder/object/" + newFolderId)
				.principal(mockPrincipal).accept(MediaType.APPLICATION_JSON).content(folderRequestBody)
				.contentType("application/json;charset=UTF-8").header("X-Requested-With", "XMLHttpRequest");

		mockMvc.perform(folderRequestBuilder).andExpect(status().isOk());
	}

	@Test
	public void stage07ShFolderDelete() throws Exception {
		mockMvc.perform(delete("/api/v2/folder/" + newFolderId)).andExpect(status().isOk());
	}	
}