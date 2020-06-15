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
package com.viglet.shio.exchange.post;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.viglet.shio.exchange.ShPostExchange;
import com.viglet.shio.exchange.ShRelatorItemExchange;
import com.viglet.shio.exchange.ShRelatorItemExchanges;
import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.persistence.model.post.relator.ShRelatorItem;
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shio.persistence.repository.folder.ShFolderRepository;
import com.viglet.shio.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shio.persistence.repository.post.ShPostRepository;
import com.viglet.shio.persistence.repository.post.relator.ShRelatorItemRepository;
import com.viglet.shio.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shio.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shio.post.type.ShSystemPostType;
import com.viglet.shio.post.type.ShSystemPostTypeAttr;
import com.viglet.shio.turing.ShTuringIntegration;
import com.viglet.shio.url.ShURLFormatter;
import com.viglet.shio.utils.ShFolderUtils;
import com.viglet.shio.utils.ShPostUtils;
import com.viglet.shio.utils.ShStaticFileUtils;
import com.viglet.shio.widget.ShSystemWidget;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShPostImport {
	static final Logger logger = LogManager.getLogger(ShPostImport.class.getName());
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShRelatorItemRepository shRelatorItemRepository;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShURLFormatter shURLFormatter;
	@Autowired
	private ShTuringIntegration shTuringIntegration;

	private boolean turingEnabled = true;

	public ShPost getShPost(ShPostExchange shPostExchange) throws ClientProtocolException, IOException {
		ShPost shPost = new ShPost();
		shPost.setId(shPostExchange.getId());
		shPost.setDate(shPostExchange.getDate());
		if (shPostExchange.getPosition() > 0) {
			shPost.setPosition(shPostExchange.getPosition());
		}
		shPost.setShFolder(shFolderRepository.findById(shPostExchange.getFolder()).orElse(null));
		shPost.setShPostType(shPostTypeRepository.findByName(shPostExchange.getPostType()));
		shPost.setOwner(shPostExchange.getOwner());

		for (Entry<String, Object> shPostField : shPostExchange.getFields().entrySet()) {
			ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPost.getShPostType(),
					shPostField.getKey());
			if (shPostTypeAttr.getIsTitle() == (byte) 1) {
				shPost.setTitle(StringUtils.abbreviate((String) shPostField.getValue(), 255));
			} else if (shPostTypeAttr.getIsSummary() == (byte) 1) {
				shPost.setSummary(StringUtils.abbreviate((String) shPostField.getValue(), 255));
			}
		}

		if (shPostExchange.getFurl() != null) {
			shPost.setFurl(shPostExchange.getFurl());
		} else {
			shPost.setFurl(shURLFormatter.format(shPost.getTitle()));
		}

		this.getShPostAttrs(shPostExchange, shPost, shPostExchange.getFields(), null);

		return shPost;
	}

	private void getShPostAttrs(ShPostExchange shPostExchange, ShPost shPost, Map<String, Object> shPostFields,
			ShRelatorItem shParentRelatorItem) throws ClientProtocolException, IOException {
		for (Entry<String, Object> shPostField : shPostFields.entrySet()) {
			ShPostType shPostType = shPostTypeRepository.findByName(shPostExchange.getPostType());

			ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostType,
					shPostField.getKey());
			// Relator: the PostType is null
			if (shPostTypeAttr == null) {
				shPostTypeAttr = shPostTypeAttrRepository.findByShParentPostTypeAttrAndName(
						shParentRelatorItem.getShParentPostAttr().getShPostTypeAttr(), shPostField.getKey());
			}

			if ((shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.FILE)
					|| shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.CONTENT_SELECT))
					&& shPostField.getValue() != null && !shPostType.getName().equals(ShSystemPostType.FILE)) {
				try {
					String shReferencedPostUUID = (String) shPostField.getValue();
					if (!shPostRepository.findById(shReferencedPostUUID).isPresent()) {
						// Referenced Post not found
					}
				} catch (IllegalArgumentException iae) {
					logger.error("getShPostAttrs", iae);
				}
			}
			if (shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.RELATOR)) {
				postAttrRelator(shPostExchange, shPost, shParentRelatorItem, shPostField, shPostTypeAttr);
			} else {
				postAttrNonRelator(shPost, shParentRelatorItem, shPostField, shPostTypeAttr);
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	private void postAttrNonRelator(ShPost shPost, ShRelatorItem shParentRelatorItem, Entry<String, Object> shPostField,
			ShPostTypeAttr shPostTypeAttr) {
		ShPostAttr shPostAttr = new ShPostAttr();
		if (shPostField.getValue() instanceof ArrayList)
			shPostAttr.setArrayValue((new HashSet<String>((ArrayList<String>) shPostField.getValue())));
		else if (shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.DATE)) {
			if (shPostField.getValue() != null) {
				try {
					shPostAttr.setDateValue(
							new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'").parse((String) shPostField.getValue()));
				} catch (ParseException e) {
					logger.error("createShPostAttrs Error:", e);
				}
			}
		} else {
			shPostAttr.setStrValue((String) shPostField.getValue());
		}

		if (shParentRelatorItem != null) {
			shPostAttr.setShPost(null);
			shPostAttr.setShParentRelatorItem(shParentRelatorItem);
			if (shPostTypeAttr.getIsTitle() == 1) {
				shParentRelatorItem.setTitle("Parent" + shPostAttr.getStrValue());
			}
			if (shPostTypeAttr.getIsSummary() == 1) {
				shParentRelatorItem.setSummary(shPostAttr.getStrValue());
			}
		} else {
			shPostAttr.setShPost(shPost);
			shPost.addShPostAttr(shPostAttr);
		}

		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setType(1);
	}

	@SuppressWarnings({ "unchecked" })
	private void postAttrRelator(ShPostExchange shPostExchange, ShPost shPost, ShRelatorItem shParentRelatorItem,
			Entry<String, Object> shPostField, ShPostTypeAttr shPostTypeAttr)
			throws JsonProcessingException, JsonMappingException, ClientProtocolException, IOException {
		LinkedHashMap<String, Object> relatorFields = (LinkedHashMap<String, Object>) shPostField.getValue();

		ShPostAttr shPostAttr = new ShPostAttr();

		if (shParentRelatorItem != null) {
			shPostAttr.setShPost(null);
			shPostAttr.setShParentRelatorItem(shParentRelatorItem);
		} else {
			shPostAttr.setShPost(shPost);
			shPost.addShPostAttr(shPostAttr);
		}

		shPostAttr.setId((String) relatorFields.get("id"));
		shPostAttr.setStrValue((String) relatorFields.get("name"));
		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setType(1);

		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String subPostsJson = ow.writeValueAsString(relatorFields.get("shSubPosts"));
		ShRelatorItemExchanges subPosts = mapper.readValue(subPostsJson, ShRelatorItemExchanges.class);

		for (ShRelatorItemExchange shSubPost : subPosts) {
			ShRelatorItem shRelatorItem = new ShRelatorItem();
			shRelatorItem.setOrdinal(shSubPost.getPosition());
			shRelatorItem.setShParentPostAttr(shPostAttr);

			this.getShPostAttrs(shPostExchange, shPost, shSubPost.getFields(), shRelatorItem);

		}
	}

	public ShPost createShPost(ShPostExchange shPostExchange, File extractFolder, String username,
			Map<String, Object> shObjects, boolean isCloned) {
		ShPost shPost = null;
		if (shPostRepository.findById(shPostExchange.getId()).isPresent()) {
			shPost = shPostRepository.findById(shPostExchange.getId()).orElse(null);
		} else {
			shPost = new ShPost();
			shPost.setId(shPostExchange.getId());
			shPost.setDate(isCloned ? new Date() : shPostExchange.getDate());
			if (shPostExchange.getPosition() > 0)
				shPost.setPosition(shPostExchange.getPosition());

			ShFolder shFolder = shFolderRepository.findById(shPostExchange.getFolder()).orElse(null);
			shPost.setShFolder(shFolder);
			shPost.setShPostType(shPostTypeRepository.findByName(shPostExchange.getPostType()));
			shPost.setShSite(shFolderUtils.getSite(shFolder));
			if (shPostExchange.getOwner() != null)
				shPost.setOwner(shPostExchange.getOwner());
			else
				shPost.setOwner(username);

			detectPostAttrs(shPostExchange, extractFolder, shPost);

			if (shPostExchange.getFurl() != null) {
				shPost.setFurl(shPostExchange.getFurl());
			} else {
				shPost.setFurl(shURLFormatter.format(shPost.getTitle()));
			}

			shPostRepository.saveAndFlush(shPost);

			this.createShPostAttrs(shPostExchange, shPost, shPostExchange.getFields(), null, extractFolder, username,
					shObjects, isCloned);

			for (ShPostAttr shPostAttr : shPostAttrRepository.findByShPost(shPost)) {
				shPostUtils.updateRelatorInfo(shPostAttr, shPost);
			}

			if (turingEnabled)
				shTuringIntegration.indexObject(shPost);
		}

		if (shPost != null)
			logger.info(String.format("........ %s Post (%s)", shPost.getTitle(), shPost.getId()));
		return shPost;
	}

	private void detectPostAttrs(ShPostExchange shPostExchange, File extractFolder, ShPost shPost) {
		for (Entry<String, Object> shPostField : shPostExchange.getFields().entrySet()) {
			ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPost.getShPostType(),
					shPostField.getKey());
			if (shPostTypeAttr.getIsTitle() == (byte) 1) {
				shPost.setTitle(StringUtils.abbreviate((String) shPostField.getValue(), 255));
			} else if (shPostTypeAttr.getIsSummary() == (byte) 1) {
				shPost.setSummary(StringUtils.abbreviate((String) shPostField.getValue(), 255));
			}
			if (shPostTypeAttr.getName().equals(ShSystemPostTypeAttr.FILE)
					&& shPostExchange.getPostType().equals(ShSystemPostType.FILE)) {
				String fileName = (String) shPostField.getValue();
				File directoryPath = shStaticFileUtils.dirPath(shPost.getShFolder());
				File fileSource = new File(
						extractFolder.getAbsolutePath().concat(File.separator + shPostExchange.getId()));
				File fileDest = new File(directoryPath.getAbsolutePath().concat(File.separator + fileName));
				try {
					FileUtils.copyFile(fileSource, fileDest);
				} catch (IOException e) {
					logger.error("createShPostException", e);
				}
			}
		}
	}

	private void createShPostAttrs(ShPostExchange shPostExchange, ShPost shPost, Map<String, Object> shPostFields,
			ShRelatorItem shParentRelatorItem, File extractFolder, String username, Map<String, Object> shObjects,
			boolean isCloned) {
		for (Entry<String, Object> shPostField : shPostFields.entrySet()) {
			ShPostType shPostType = shPostTypeRepository.findByName(shPostExchange.getPostType());

			ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostType,
					shPostField.getKey());
			// Relator: the PostType is null
			if (shPostTypeAttr == null) {
				shPostTypeAttr = shPostTypeAttrRepository.findByShParentPostTypeAttrAndName(
						shParentRelatorItem.getShParentPostAttr().getShPostTypeAttr(), shPostField.getKey());
			}

			if ((shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.FILE)
					|| shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.CONTENT_SELECT))
					&& shPostField.getValue() != null && !shPostType.getName().equals(ShSystemPostType.FILE)) {
				try {
					String shReferencedPostUUID = (String) shPostField.getValue();
					// So the referenced Post not exists, need create first
					if (!shPostRepository.findById(shReferencedPostUUID).isPresent()
							&& shObjects.get(shReferencedPostUUID) instanceof ShPostExchange) {
						ShPostExchange shReferencedPostExchange = (ShPostExchange) shObjects.get(shReferencedPostUUID);
						this.createShPost(shReferencedPostExchange, extractFolder, username, shObjects, isCloned);
					}

				} catch (IllegalArgumentException iae) {
					logger.error("createShPostAttrs", iae);
				}
			}
			if (shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.RELATOR)) {

				detectPostAttrRelator(shPostExchange, shPost, shParentRelatorItem, extractFolder, username, shObjects,
						isCloned, shPostField, shPostTypeAttr);
			} else {
				detectPostAttrNonRelator(shPost, shParentRelatorItem, shPostField, shPostTypeAttr);
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	private void detectPostAttrNonRelator(ShPost shPost, ShRelatorItem shParentRelatorItem,
			Entry<String, Object> shPostField, ShPostTypeAttr shPostTypeAttr) {
		ShPostAttr shPostAttr = new ShPostAttr();
		if (shPostField.getValue() instanceof ArrayList)
			shPostAttr.setArrayValue((new HashSet<String>((ArrayList<String>) shPostField.getValue())));
		else if (shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.DATE)) {
			if (shPostField.getValue() != null) {
				try {
					shPostAttr.setDateValue(
							new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'").parse((String) shPostField.getValue()));
				} catch (ParseException e) {
					logger.error("createShPostAttrs Error:", e);
				}
			}
		} else {
			shPostAttr.setStrValue((String) shPostField.getValue());
		}

		if (shParentRelatorItem != null) {
			shPostAttr.setShPost(null);
			shPostAttr.setShParentRelatorItem(shParentRelatorItem);
			if (shPostTypeAttr.getIsTitle() == 1) {
				shParentRelatorItem.setTitle("Parent" + shPostAttr.getStrValue());
			}
			if (shPostTypeAttr.getIsSummary() == 1) {
				shParentRelatorItem.setSummary(shPostAttr.getStrValue());
			}
		} else {
			shPostAttr.setShPost(shPost);
		}

		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setType(1);
		shPostAttrRepository.save(shPostAttr);

		shPostUtils.referencedObject(shPostAttr, shPost);
		shPostAttrRepository.save(shPostAttr);
	}

	@SuppressWarnings({ "unchecked" })
	private void detectPostAttrRelator(ShPostExchange shPostExchange, ShPost shPost, ShRelatorItem shParentRelatorItem,
			File extractFolder, String username, Map<String, Object> shObjects, boolean isCloned,
			Entry<String, Object> shPostField, ShPostTypeAttr shPostTypeAttr) {
		LinkedHashMap<String, Object> relatorFields = (LinkedHashMap<String, Object>) shPostField.getValue();

		ShPostAttr shPostAttr = new ShPostAttr();

		if (shParentRelatorItem != null) {
			shPostAttr.setShPost(null);
			shPostAttr.setShParentRelatorItem(shParentRelatorItem);
		} else {
			shPostAttr.setShPost(shPost);
		}

		shPostAttr.setId((String) relatorFields.get("id"));
		shPostAttr.setStrValue((String) relatorFields.get("name"));
		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setType(1);

		shPostAttrRepository.save(shPostAttr);

		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		ShRelatorItemExchanges subPosts = null;
		try {
			String subPostsJson = ow.writeValueAsString(relatorFields.get("shSubPosts"));
			subPosts = mapper.readValue(subPostsJson, ShRelatorItemExchanges.class);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		if (subPosts != null) {
			subPosts.forEach(shSubPost -> {
				ShRelatorItem shRelatorItem = new ShRelatorItem();
				shRelatorItem.setOrdinal(shSubPost.getPosition());
				shRelatorItem.setShParentPostAttr(shPostAttr);

				shRelatorItemRepository.save(shRelatorItem);
				this.createShPostAttrs(shPostExchange, shPost, shSubPost.getFields(), shRelatorItem, extractFolder,
						username, shObjects, isCloned);
			});
		}
	}
}
