/*
 * Copyright (C) 2016-2020 the original author or authors. 
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
package com.viglet.shio.exchange.folder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shio.exchange.ShExchange;
import com.viglet.shio.exchange.ShFileExchange;
import com.viglet.shio.exchange.ShFolderExchange;
import com.viglet.shio.exchange.ShPostExchange;
import com.viglet.shio.exchange.ShPostTypeExchange;
import com.viglet.shio.exchange.post.ShPostExport;
import com.viglet.shio.exchange.post.type.ShPostTypeExport;
import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.repository.folder.ShFolderRepository;
import com.viglet.shio.persistence.repository.post.ShPostRepository;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShFolderExport {
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostExport shPostExport;
	@Autowired
	private ShPostTypeExport shPostTypeExport;

	public ShExchange shFolderExchangeIterate(Set<ShFolder> shFolders) {
		ShExchange shExchange = new ShExchange();
		List<ShFolderExchange> shFolderExchanges = new ArrayList<>();
		List<ShPostExchange> shPostExchanges = new ArrayList<>();
		List<ShFileExchange> files = new ArrayList<>();
		Map<String, ShPostTypeExchange> shPostTypeExchanges = new HashMap<>();
		for (ShFolder shFolder : shFolders) {

			for (ShPost shPost : shPostRepository.findByShFolder(shFolder)) {
				ShPostExchange shPostExchange = exportShPost(files, shPostTypeExchanges, shPost);

				shPostExchanges.add(shPostExchange);
			}
			ShFolderExchange shFolderExchangeChild = new ShFolderExchange();
			shFolderExchangeChild.setId(shFolder.getId());
			shFolderExchangeChild.setDate(shFolder.getDate());
			shFolderExchangeChild.setName(shFolder.getName());
			shFolderExchangeChild.setOwner(shFolder.getOwner());
			shFolderExchangeChild.setFurl(shFolder.getFurl());
			shFolderExchangeChild.setPosition(shFolder.getPosition());

			if (shFolder.getParentFolder() != null) {
				shFolderExchangeChild.setParentFolder(shFolder.getParentFolder().getId());
			}
			shFolderExchanges.add(shFolderExchangeChild);
			ShExchange shExchangeChild = this.shFolderExchangeNested(shFolder);
			shFolderExchanges.addAll(shExchangeChild.getFolders());
			shPostExchanges.addAll(shExchangeChild.getPosts());

			for (ShPostTypeExchange shPostTypeExchange : shExchangeChild.getPostTypes()) {
				if (!shPostTypeExchanges.containsKey(shPostTypeExchange.getName())) {
					shPostTypeExchanges.put(shPostTypeExchange.getName(), shPostTypeExchange);
				}

			}
			files.addAll(shExchangeChild.getFiles());
		}
		shExchange.setFolders(shFolderExchanges);
		shExchange.setPosts(shPostExchanges);
		shExchange.setFiles(files);
		shExchange.setPostTypes(new ArrayList<>(shPostTypeExchanges.values()));
		return shExchange;
	}

	private ShPostExchange exportShPost(List<ShFileExchange> files, Map<String, ShPostTypeExchange> shPostTypeExchanges,
			ShPost shPost) {
		ShPostExchange shPostExchange = new ShPostExchange();
		shPostExchange.setId(shPost.getId());
		shPostExchange.setFolder(shPost.getShFolder().getId());
		shPostExchange.setDate(shPost.getDate());
		shPostExchange.setPostType(shPost.getShPostType().getName());
		shPostExchange.setOwner(shPost.getOwner());
		shPostExchange.setFurl(shPost.getFurl());
		shPostExchange.setPosition(shPost.getPosition());

		if (!shPostTypeExchanges.containsKey(shPost.getShPostType().getName())) {
			shPostTypeExchanges.put(shPost.getShPostType().getName(),
					shPostTypeExport.exportPostType(shPost.getShPostType()));
		}
		Map<String, Object> fields = new HashMap<>();

		shPostExport.shPostAttrExchangeIterate(shPost, fields, files);

		shPostExchange.setFields(fields);
		return shPostExchange;
	}

	public ShExchange shFolderExchangeNested(ShFolder shFolder) {
		Set<ShFolder> childFolders = shFolderRepository.findByParentFolder(shFolder);
		return this.shFolderExchangeIterate(childFolders);
	}
}
