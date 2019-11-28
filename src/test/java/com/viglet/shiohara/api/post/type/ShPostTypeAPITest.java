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

package com.viglet.shiohara.api.post.type;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.Calendar;
import java.util.HashSet;

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

import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.widget.ShWidget;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.widget.ShWidgetRepository;
import com.viglet.shiohara.post.type.ShSystemPostTypeAttr;
import com.viglet.shiohara.utils.ShUtils;
import com.viglet.shiohara.widget.ShSystemWidget;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShPostTypeAPITest {

	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private ShWidgetRepository shWidgetRepository;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;

	private MockMvc mockMvc;

	private Principal mockPrincipal;

	private String newPostTypeId = "f0a9cc2f-f283-4e05-a7c6-758bb3b81b76";

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockPrincipal = Mockito.mock(Principal.class);
		Mockito.when(mockPrincipal.getName()).thenReturn("admin");
	}

	@Test
	public void shPostTypeList() throws Exception {
		mockMvc.perform(get("/api/v2/post/type")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));

	}

	@Test
	public void shPostTypeStructure() throws Exception {
		mockMvc.perform(get("/api/v2/post/type/model")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));

	}

	@Test
	public void stage01ShPostTypeAdd() throws Exception {

		ShWidget shWidgetText = shWidgetRepository.findByName(ShSystemWidget.TEXT);

		ShPostType shPostType = new ShPostType();
		shPostType.setId(newPostTypeId);
		shPostType.setName("PT-TEST");
		shPostType.setTitle("Test");
		shPostType.setDate(Calendar.getInstance().getTime());
		shPostType.setDescription("Test Post Type");
		shPostType.setSystem((byte) 0);

		ShPostTypeAttr shPostTypeAttr = new ShPostTypeAttr();
		shPostTypeAttr.setName(ShSystemPostTypeAttr.TEXT);
		shPostTypeAttr.setLabel("Test");
		shPostTypeAttr.setDescription("Test");
		shPostTypeAttr.setIsSummary((byte) 0);
		shPostTypeAttr.setIsTitle((byte) 1);
		shPostTypeAttr.setOrdinal(1);
		shPostTypeAttr.setRequired((byte) 1);
		shPostTypeAttr.setShWidget(shWidgetText);

		shPostType.getShPostTypeAttrs().add(shPostTypeAttr);

		String postTypeRequestBody = ShUtils.asJsonStringAndView(shPostType, ShJsonView.ShJsonViewPostType.class);

		RequestBuilder folderRequestBuilder = MockMvcRequestBuilders.post("/api/v2/post/type").principal(mockPrincipal)
				.accept(MediaType.APPLICATION_JSON).content(postTypeRequestBody)
				.contentType("application/json");

		mockMvc.perform(folderRequestBuilder).andExpect(status().isOk());
	}

	@Test
	public void stage02ShPostTypeGet() throws Exception {
		mockMvc.perform(get("/api/v2/post/type/" + newPostTypeId)).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));
	}

	@Test
	public void stage03ShPostTypePostStructure() throws Exception {
		mockMvc.perform(get("/api/v2/post/type/" + newPostTypeId + "/post/model")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));
	}

	@Test
	public void stage04ShPostTypeByNamePostStructure() throws Exception {
		mockMvc.perform(get("/api/v2/post/type/name/PT-TEST/post/model")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));
	}

	@Test
	public void stage05ShPostTypeAttrAdd() throws Exception {
		ShWidget shWidgetTextArea = shWidgetRepository.findByName(ShSystemWidget.TEXT_AREA);

		ShPostTypeAttr shPostTypeAttr = new ShPostTypeAttr();
		shPostTypeAttr.setName(ShSystemPostTypeAttr.DESCRIPTION);
		shPostTypeAttr.setLabel("Test Area");
		shPostTypeAttr.setDescription("Test");
		shPostTypeAttr.setIsSummary((byte) 0);
		shPostTypeAttr.setIsTitle((byte) 1);
		shPostTypeAttr.setOrdinal(1);
		shPostTypeAttr.setRequired((byte) 1);
		shPostTypeAttr.setShWidget(shWidgetTextArea);

		String postTypeAttrRequestBody = ShUtils.asJsonStringAndView(shPostTypeAttr,
				ShJsonView.ShJsonViewPostType.class);

		RequestBuilder folderRequestBuilder = MockMvcRequestBuilders
				.post("/api/v2/post/type/" + newPostTypeId + "/attr").principal(mockPrincipal)
				.accept(MediaType.APPLICATION_JSON).content(postTypeAttrRequestBody)
				.contentType("application/json");

		mockMvc.perform(folderRequestBuilder).andExpect(status().isOk());
	}

	@Test
	public void stage06ShPostTypeUpdate() throws Exception {
		ShPostType shPostType = shPostTypeRepository.findById(newPostTypeId).get();

		shPostType.setName("PT-TEST");
		shPostType.setTitle("Test was Changed");
		shPostType.setDate(Calendar.getInstance().getTime());
		shPostType.setDescription("Test Post Type was Changed");
		shPostType.setSystem((byte) 0);
		shPostType.setShPostTypeAttrs(new HashSet<ShPostTypeAttr>());
		String postTypeRequestBody = ShUtils.asJsonStringAndView(shPostType, ShJsonView.ShJsonViewPostType.class);

		RequestBuilder folderRequestBuilder = MockMvcRequestBuilders.put("/api/v2/post/type/" + newPostTypeId)
				.principal(mockPrincipal).accept(MediaType.APPLICATION_JSON).content(postTypeRequestBody)
				.contentType("application/json");

		mockMvc.perform(folderRequestBuilder).andExpect(status().isOk());
	}

	@Test
	public void stage07ShPostTypeDelete() throws Exception {
		mockMvc.perform(delete("/api/v2/post/type/" + newPostTypeId)).andExpect(status().isOk());
	}
}