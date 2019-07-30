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

package com.viglet.shiohara.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.utils.ShPostUtils;

@Component
public class ShQueryComponent {

	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShPostUtils shPostUtils;

	public List<Map<String, ShPostAttr>> findByFolderName(String folderId, String postTypeName) {

		ShPostType shPostType = shPostTypeRepository.findByName(postTypeName);
		ShFolder shFolder = shFolderRepository.findById(folderId).orElse(null);
		List<ShPost> shPostList = shPostRepository.findByShFolderAndShPostTypeOrderByPositionAsc(shFolder, shPostType);

		List<Map<String, ShPostAttr>> shPosts = new ArrayList<Map<String, ShPostAttr>>();
		for (ShPost shPost : shPostList) {
			Map<String, ShPostAttr> shPostObject = shPostUtils.postToMap(shPost);
			shPosts.add(shPostObject);
		}

		return shPosts;
	}
	
	public List<Map<String, ShPostAttr>> findByPostTypeName(String postTypeName) {

		ShPostType shPostType = shPostTypeRepository.findByName(postTypeName);
		List<ShPost> shPostList = shPostRepository.findByShPostType(shPostType);

		List<Map<String, ShPostAttr>> shPosts = new ArrayList<Map<String, ShPostAttr>>();
		for (ShPost shPost : shPostList) {
			Map<String, ShPostAttr> shPostObject = shPostUtils.postToMap(shPost);
			shPosts.add(shPostObject);
		}

		return shPosts;
	}
	public List<Map<String, ShPostAttr>> findByPostTypeNameIn(String postTypeName, String postAttrName, Set<String> arrayValue) {
		
		ShPostType shPostType = shPostTypeRepository.findByName(postTypeName);
		Set<ShPostAttr> shPostAttrs = shPostAttrRepository.findByArrayValueIn(arrayValue);
		
		List<ShPostAttr> shPostAttrList = new ArrayList<ShPostAttr>();
		shPostAttrList.addAll(shPostAttrs);
		
		Set<ShPost> shPostList  = shPostRepository.findByShPostTypeAndShPostAttrsIn(shPostType, shPostAttrList);
		
		List<Map<String, ShPostAttr>> shPosts = new ArrayList<Map<String, ShPostAttr>>();
		for (ShPost shPost : shPostList) {
			Map<String, ShPostAttr> shPostObject = shPostUtils.postToMap(shPost);
			shPosts.add(shPostObject);
		}

		return shPosts;
	}
}
