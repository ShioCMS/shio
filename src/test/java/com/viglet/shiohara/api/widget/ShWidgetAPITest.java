package com.viglet.shiohara.api.widget;

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

import com.viglet.shiohara.persistence.model.widget.ShWidget;
import com.viglet.shiohara.utils.ShUtils;
import com.viglet.shiohara.widget.ShSystemWidget;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShWidgetAPITest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	private Principal mockPrincipal;

	private String newWidgetId = "9be0d9a6-1b98-43af-9e19-00bf71a05908";

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockPrincipal = Mockito.mock(Principal.class);
		Mockito.when(mockPrincipal.getName()).thenReturn("admin");
	}

	@Test
	public void shWidgetList() throws Exception {
		mockMvc.perform(get("/api/v2/widget")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));

	}

	@Test
	public void stage01ShWidgetAdd() throws Exception {
		ShWidget shWidget = new ShWidget();

		shWidget.setId(newWidgetId);
		shWidget.setName(ShSystemWidget.TEXT);
		shWidget.setDescription("Test Widget");
		shWidget.setClassName("com.viglet.shiohara.widget.ShTestWidget");
		shWidget.setImplementationCode("template/widget/test.html");
		shWidget.setType("TEXT,TEXTAREA");

		String requestBody = ShUtils.asJsonString(shWidget);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v2/widget").principal(mockPrincipal)
				.accept(MediaType.APPLICATION_JSON).content(requestBody).contentType("application/json;charset=UTF-8");

		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	public void stage02ShWidgetUpdate() throws Exception {
		ShWidget shWidget = new ShWidget();

		shWidget.setId(newWidgetId);
		shWidget.setName(ShSystemWidget.TEXT);
		shWidget.setDescription("Changed Test Widget");
		shWidget.setClassName("com.viglet.shiohara.widget.ShTestWidget");
		shWidget.setImplementationCode("template/widget/test.html");
		shWidget.setType("TEXT,TEXTAREA");

		String requestBody = ShUtils.asJsonString(shWidget);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v2/widget/" + newWidgetId)
				.principal(mockPrincipal).accept(MediaType.APPLICATION_JSON).content(requestBody)
				.contentType("application/json;charset=UTF-8");

		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	public void stage03ShWidgetEdit() throws Exception {
		mockMvc.perform(get("/api/v2/widget/" + newWidgetId)).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));
	}
	
	@Test
	public void stage04ShWidgetDelete() throws Exception {
		mockMvc.perform(delete("/api/v2/widget/" + newWidgetId)).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));
	}
}