package com.viglet.shiohara.api.user;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.viglet.shiohara.persistence.model.user.ShUser;
import com.viglet.shiohara.persistence.repository.user.ShUserRepository;
import com.viglet.shiohara.utils.ShUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShUserAPITest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	private String newUsername = "test";

	private Principal mockPrincipal;

	@Autowired
	private ShUserRepository shUserRepository;
	 
	@Mock SecurityContext mockSecurityContext;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockPrincipal = Mockito.mock(Principal.class);
		Mockito.when(mockPrincipal.getName()).thenReturn("admin");
	}

	@Test
	public void shUserList() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v2/user").principal(mockPrincipal)
				.accept(MediaType.APPLICATION_JSON).contentType("application/json;charset=UTF-8");
		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));
	}

	//@Test
	public void shUserCurrent() throws Exception {	
	        Authentication authentication = mock(Authentication.class);
	        SecurityContext securityContext = mock(SecurityContext.class);
	        when(securityContext.getAuthentication()).thenReturn(authentication);
	        SecurityContextHolder.setContext(securityContext);
	        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(mockPrincipal);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v2/user/current").principal(mockPrincipal)
				.accept(MediaType.APPLICATION_JSON).contentType("application/json;charset=UTF-8");

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));
	}

	@Test
	public void shUserStructure() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v2/user/model").principal(mockPrincipal)
				.accept(MediaType.APPLICATION_JSON).contentType("application/json;charset=UTF-8");

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));
	}

	@Test
	public void stage01ShUserAdd() throws Exception {
		ShUser shUser = new ShUser();
		shUser.setUsername("test");
		shUser.setEmail("test@test.com");
		shUser.setConfirmEmail("test@test.com");
		shUser.setEnabled(1);
		shUser.setFirstName("Test");
		shUser.setLastName("Test");
		shUser.setPassword("test");

		String requestBody = ShUtils.asJsonString(shUser);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v2/user").principal(mockPrincipal)
				.accept(MediaType.APPLICATION_JSON).content(requestBody).contentType("application/json;charset=UTF-8");

		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	public void stage02ShUserGet() throws Exception {
		mockMvc.perform(get("/api/v2/user/" + newUsername)).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));
	}

	@Test
	public void stage03ShUserUpdate() throws Exception {
		ShUser shUser = shUserRepository.findByUsername(newUsername);
		shUser.setFirstName("Test2");

		String userRequestBody = ShUtils.asJsonString(shUser);

		RequestBuilder userRequestBuilder = MockMvcRequestBuilders.put("/api/v2/user/" + newUsername)
				.principal(mockPrincipal).accept(MediaType.APPLICATION_JSON).content(userRequestBody)
				.contentType("application/json;charset=UTF-8");

		mockMvc.perform(userRequestBuilder).andExpect(status().isOk());
	}

	@Test
	public void stage04ShUserDelete() throws Exception {
		mockMvc.perform(delete("/api/v2/user/" + newUsername)).andExpect(status().isOk());
	}
}