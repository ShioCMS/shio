package com.viglet.shiohara.sites;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShSitesContextTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void sitesFullGeneric() throws Exception {
		mockMvc.perform(get("/sites/viglet/default/en-us")).andExpect(status().isOk());

	}

	@Test
	public void sitesFullGenericHeaders() throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("x-sh-site", "viglet");
		httpHeaders.add("x-sh-nocache", "1");
		mockMvc.perform(get("/sites").headers(httpHeaders)).andExpect(status().isOk());

	}
}