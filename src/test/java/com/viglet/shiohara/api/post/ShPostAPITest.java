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

package com.viglet.shiohara.api.post;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.relator.ShRelatorItem;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.post.type.ShSystemPostTypeAttr;
import com.viglet.shiohara.utils.ShUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShPostAPITest {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostRepository shPostRepository;

	private MockMvc mockMvc;

	private Principal mockPrincipal;

	private String newPostId = "553923c7-fda4-4a91-9700-eb9a549bb522";

	private String sampleHomeFolderId = "fdbff43b-cc7e-40f1-80a1-062bb08ae5d0";

	@Before
	public void setup() {
		log.debug("PostAPITest Setup");
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockPrincipal = Mockito.mock(Principal.class);
		Mockito.when(mockPrincipal.getName()).thenReturn("admin");
	}

	@Test
	public void shPostList() throws Exception {
		mockMvc.perform(get("/api/v2/post")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));

	}

	@Test
	public void shPostAttrModel() throws Exception {
		mockMvc.perform(get("/api/v2/post/attr/model")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));

	}

	@Test
	public void stage01ShPostAdd() throws Exception {

		ShFolder shFolder = shFolderRepository.findById(sampleHomeFolderId).get();
		ShPostType shPostType = shPostTypeRepository.findByName(ShSystemPostType.ARTICLE);

		// Post
		ShPost shPost = new ShPost();
		shPost.setId(newPostId);
		shPost.setShFolder(shFolder);
		shPost.setShPostType(shPostType);

		// Title Field
		ShPostAttr shPostAttrTitle = new ShPostAttr();
		shPostAttrTitle.setStrValue("Test Title Value");
		shPostAttrTitle.setType(1);
		shPostAttrTitle.setShPostTypeAttr(
				shPostTypeAttrRepository.findByShPostTypeAndName(shPostType, ShSystemPostTypeAttr.TITLE));

		shPost.getShPostAttrs().add(shPostAttrTitle);

		// Description Field
		ShPostAttr shPostAttrDescription = new ShPostAttr();
		shPostAttrDescription.setStrValue("Test Description Value");
		shPostAttrDescription.setType(1);
		shPostAttrDescription.setShPostTypeAttr(
				shPostTypeAttrRepository.findByShPostTypeAndName(shPostType, ShSystemPostTypeAttr.DESCRIPTION));

		shPost.getShPostAttrs().add(shPostAttrDescription);

		// Text Relation
		ShRelatorItem shRelatorItem = new ShRelatorItem();

		ShPostAttr shPostAttrTextRelation = new ShPostAttr();
		shPostAttrTextRelation.setStrValue("Test Text Relation Value");
		shPostAttrTextRelation.setType(1);
		shPostAttrTextRelation.setShPostTypeAttr(shPostTypeAttrRepository.findByShParentPostTypeAttrAndName(
				shPostTypeAttrRepository.findByShPostTypeAndName(shPostType, ShSystemPostTypeAttr.RELATION),
				"TEXT_RELATION"));

		shRelatorItem.getShChildrenPostAttrs().add(shPostAttrTextRelation);

		// Relation
		ShPostAttr shPostAttrRelation = new ShPostAttr();
		shPostAttrRelation.getShChildrenRelatorItems().add(shRelatorItem);
		shPostAttrRelation.setType(1);
		shPostAttrRelation.setShPostTypeAttr(
				shPostTypeAttrRepository.findByShPostTypeAndName(shPostType, ShSystemPostTypeAttr.RELATION));

		shPost.getShPostAttrs().add(shPostAttrRelation);

		String postRequestBody = ShUtils.asJsonStringAndView(shPost, ShJsonView.ShJsonViewObject.class);

		RequestBuilder folderRequestBuilder = MockMvcRequestBuilders.post("/api/v2/post").principal(mockPrincipal)
				.accept(MediaType.APPLICATION_JSON).content(postRequestBody)
				.contentType("application/json;charset=UTF-8");

		mockMvc.perform(folderRequestBuilder).andExpect(status().isOk());
	}

	@Test
	public void stage02ShPostEdit() throws Exception {
		mockMvc.perform(get("/api/v2/post/" + newPostId)).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));
	}

	@Test
	public void stage03ShPostUpdate() throws Exception {

		ShPost shPost = shPostRepository.findById(newPostId).get();

		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			shPostAttr.setStrValue("Test Value was changed");
		}

		String postRequestBody = ShUtils.asJsonStringAndView(shPost, ShJsonView.ShJsonViewPost.class);

		RequestBuilder folderRequestBuilder = MockMvcRequestBuilders.put("/api/v2/post/" + newPostId)
				.principal(mockPrincipal).accept(MediaType.APPLICATION_JSON).content(postRequestBody)
				.contentType("application/json;charset=UTF-8");

		mockMvc.perform(folderRequestBuilder).andExpect(status().isOk());
	}

	@Test
	public void stage04ShPostDelete() throws Exception {

		RequestBuilder folderRequestBuilder = MockMvcRequestBuilders.delete("/api/v2/post/" + newPostId)
				.principal(mockPrincipal).accept(MediaType.APPLICATION_JSON)
				.contentType("application/json;charset=UTF-8");

		mockMvc.perform(folderRequestBuilder).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));
	}

}