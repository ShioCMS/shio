/*
 * Copyright (C) 2016-2019 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

package com.viglet.shiohara.turing;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.post.type.ShSystemPostTypeAttr;
import com.viglet.shiohara.utils.ShObjectUtils;
import com.viglet.shiohara.utils.ShPostTypeUtils;
import com.viglet.shiohara.utils.ShUtils;
import com.viglet.shiohara.utils.stage.ShStageFolderUtils;
import com.viglet.shiohara.utils.stage.ShStageObjectUtils;
import com.viglet.shiohara.utils.stage.ShStagePostUtils;
import com.viglet.shiohara.widget.ShSystemWidget;
import com.viglet.turing.api.sn.job.TurSNJobAction;
import com.viglet.turing.api.sn.job.TurSNJobItem;
import com.viglet.turing.api.sn.job.TurSNJobItems;

@Component
public class ShTuringIntegration {
	static final Logger logger = LogManager.getLogger(ShTuringIntegration.class.getName());
	private String encoding = "UTF-8";
	private String turingServer = "http://localhost:2700";
	private ShSite shSite = null;
	private boolean showOutput = true;
	private final boolean turingEnabled = true;

	@Autowired
	private ShStagePostUtils shStagePostUtils;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShUtils shUtils;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostTypeUtils shPostTypeUtils;
	@Autowired
	private ShObjectUtils shObjectUtils;
	@Autowired
	private ShStageObjectUtils shStageObjectUtils;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShStageFolderUtils shStageFolderUtils;

	public void indexObject(ShObject shObject) {

		if (turingEnabled) {
			shSite = shObjectUtils.getSite(shObject);
			TurSNJobItems turSNJobItems = new TurSNJobItems();
			if (StringUtils.isNotBlank(shSite.getSearchablePostTypes())) {
				JSONObject searchablePostTypes = new JSONObject(shSite.getSearchablePostTypes());
				String objectName = null;
				String objectTypeName = null;

				if (shObject instanceof ShPost) {
					ShPost shPost = (ShPost) shObject;
					objectName = shPost.getTitle();

					objectTypeName = shPost.getShPostType().getName();
					if (objectTypeName.equals(ShSystemPostType.FOLDER_INDEX)) {
						objectTypeName = ShObjectType.FOLDER;
						shObject = shPost.getShFolder();
					}
				} else if (shObject instanceof ShFolder) {

					objectTypeName = ShObjectType.FOLDER;
				}
				logger.info(String.format("Preparing to index %s...", objectName));

				if (objectTypeName != null && searchablePostTypes.has(objectTypeName)) {
					boolean isSearchable = searchablePostTypes.getBoolean(objectTypeName);
					if (isSearchable) {
						TurSNJobItem turSNJobItem = this.toTurSNJobItem(shObject);
						if (turSNJobItem != null) {
							turSNJobItem.setTurSNJobAction(TurSNJobAction.CREATE);
							turSNJobItems.add(turSNJobItem);
						}
					}

					if (turSNJobItems != null) {
						this.sendServer(turSNJobItems);
						logger.info(String.format("Sent to index queue: %s", objectName));
					}
				}
			}
		}
	}

	public void deindexObject(ShObject shObject) {
		if (turingEnabled) {
			shSite = shObjectUtils.getSite(shObject);
			TurSNJobItems turSNJobItems = new TurSNJobItems();

			if (StringUtils.isNotBlank(shSite.getSearchablePostTypes())) {
				JSONObject searchablePostTypes = new JSONObject(shSite.getSearchablePostTypes());
				String objectTypeName = null;
				if (shObject instanceof ShPost) {
					objectTypeName = ((ShPost) shObject).getShPostType().getName();
				} else if (shObject instanceof ShFolder) {
					objectTypeName = "FOLDER";
					ShFolder shFolder = (ShFolder) shObject;
					this.deindexInBatch(shPostRepository.findByShFolder(shFolder));
					for (ShFolder shFolderChild : shFolderRepository.findByParentFolder(shFolder)) {
						this.deindexObject(shFolderChild);
					}
				}

				if (searchablePostTypes.has(objectTypeName)) {
					boolean isSearchable = searchablePostTypes.getBoolean(objectTypeName);
					if (isSearchable) {
						TurSNJobItem turSNJobItem = new TurSNJobItem();
						Map<String, Object> attributes = new HashMap<String, Object>();
						attributes.put("id", shObject.getId());
						turSNJobItem.setAttributes(attributes);
						turSNJobItem.setTurSNJobAction(TurSNJobAction.DELETE);
						turSNJobItems.add(turSNJobItem);
					}

					if (turSNJobItems != null) {
						this.sendServer(turSNJobItems);
					}
				}
			}
		}
	}

	private void deindexInBatch(Iterable<ShPost> shPosts) {
		for (ShPost shPost : shPosts) {
			this.deindexObject(shPost);
		}
	}

	private TurSNJobItem toTurSNJobItem(ShObject shObject) {
		try {
			TurSNJobItem turSNJobItem = new TurSNJobItem();
			Map<String, Object> attributes = new HashMap<String, Object>();

			addGenericAttributes(shObject, attributes);

			if (shObject instanceof ShPost)
				addPost(shObject, attributes);
			else if (shObject instanceof ShFolder)
				addFolder(shObject, attributes);

			// If URL is null, is a invisible page
			if (attributes.get("url") != null) {
				turSNJobItem.setAttributes(attributes);
				return turSNJobItem;
			} else
				return null;
		} catch (Exception e) {
			logger.error(e);
		}

		return null;
	}

	private void addPost(ShObject shObject, Map<String, Object> attributes) {
		ShPost shPost = (ShPost) shObject;

		ShPostType shPostType = shPostTypeRepository.findById(shPost.getShPostType().getId()).orElse(null);

		if (shPost != null && shPostType != null) {
			addDefaultAttributes(attributes, shPost);
			addAssociationAttributes(attributes, shPost, shPostType);
			addAdditionalAttributes(attributes, shPost, shPostType);
		}
	}

	private void addGenericAttributes(ShObject shObject, Map<String, Object> attributes) {
		attributes.put("id", shObject.getId());
		attributes.put("publication_date", formatDateToTuring(shObject.getDate()));
		attributes.put("url", shStageObjectUtils.generateObjectLink(shObject));
	}

	private String formatDateToTuring(Date date) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		df.setTimeZone(tz);
		return df.format(date);
	}

	private void addFolder(ShObject shObject, Map<String, Object> attributes) {
		ShFolder shFolder = (ShFolder) shObject;
		attributes.put("title", shFolder.getName());
		ShPost shFolderIndexPost = shStageFolderUtils.getFolderIndex(shFolder);
		if (shFolderIndexPost != null) {
			Map<String, ShPostAttr> shFolderIndexPostMap = shStagePostUtils.postToMap(shFolderIndexPost);
			if (shFolderIndexPostMap.containsKey(ShSystemPostTypeAttr.DESCRIPTION)) {
				attributes.put("abstract", shFolderIndexPostMap.get(ShSystemPostTypeAttr.DESCRIPTION).getStrValue());
			}
		}
	}

	private boolean isAdditionalField(ShPostTypeAttr shPostTypeAttr) {
		if (shUtils.isJSONValid(shPostTypeAttr.getWidgetSettings())) {
			JSONObject settings = new JSONObject(shPostTypeAttr.getWidgetSettings());
			if (settings.has(ShTuringSearchSettings.SEARCH)) {
				JSONObject searchSettings = settings.getJSONObject(ShTuringSearchSettings.SEARCH);
				if (searchSettings.has(ShTuringSearchSettings.IS_ADDITIONAL_FIELD)
						&& (searchSettings.getInt(ShTuringSearchSettings.IS_ADDITIONAL_FIELD) == 1)) {
					return true;
				}
			}
		}
		return false;

	}

	private boolean isSamePostTypeField(ShPostTypeAttr shPostTypeAttr) {
		if (shUtils.isJSONValid(shPostTypeAttr.getWidgetSettings())) {
			JSONObject settings = new JSONObject(shPostTypeAttr.getWidgetSettings());
			if (settings.has(ShTuringSearchSettings.SEARCH)) {
				JSONObject searchSettings = settings.getJSONObject(ShTuringSearchSettings.SEARCH);
				if (searchSettings.has(ShTuringSearchSettings.IS_SAME_POSTTYPE_FIELD)
						&& (searchSettings.getInt(ShTuringSearchSettings.IS_SAME_POSTTYPE_FIELD) == 1)) {
					return true;
				}
			}
		}
		return false;

	}

	private boolean hasCustomFieldName(ShPostTypeAttr shPostTypeAttr) {
		if (shUtils.isJSONValid(shPostTypeAttr.getWidgetSettings())) {
			JSONObject settings = new JSONObject(shPostTypeAttr.getWidgetSettings());
			if (settings.has(ShTuringSearchSettings.SEARCH)) {
				JSONObject searchSettings = settings.getJSONObject(ShTuringSearchSettings.SEARCH);
				if (searchSettings.has(ShTuringSearchSettings.CUSTOM_FIELD_NAME)
						&& StringUtils.isNotBlank(searchSettings.getString(ShTuringSearchSettings.CUSTOM_FIELD_NAME))) {
					return true;
				}
			}
		}
		return false;

	}

	private String getCustomFieldName(ShPostTypeAttr shPostTypeAttr) {
		if (shUtils.isJSONValid(shPostTypeAttr.getWidgetSettings())) {
			JSONObject settings = new JSONObject(shPostTypeAttr.getWidgetSettings());
			if (settings.has(ShTuringSearchSettings.SEARCH)) {
				JSONObject searchSettings = settings.getJSONObject(ShTuringSearchSettings.SEARCH);
				if (searchSettings.has(ShTuringSearchSettings.CUSTOM_FIELD_NAME)
						&& StringUtils.isNotBlank(searchSettings.getString(ShTuringSearchSettings.CUSTOM_FIELD_NAME))) {
					return searchSettings.getString(ShTuringSearchSettings.CUSTOM_FIELD_NAME);
				}
			}
		}
		return null;

	}

	private boolean hasAssociation(ShPostTypeAttr shPostTypeAttr) {
		if (shUtils.isJSONValid(shPostTypeAttr.getWidgetSettings())) {
			JSONObject settings = new JSONObject(shPostTypeAttr.getWidgetSettings());
			if (settings.has(ShTuringSearchSettings.SEARCH)) {
				JSONObject searchSettings = settings.getJSONObject(ShTuringSearchSettings.SEARCH);
				if (searchSettings.has(ShTuringSearchSettings.ASSOCIATION)
						&& StringUtils.isNotBlank(searchSettings.getString(ShTuringSearchSettings.ASSOCIATION))) {
					return true;
				}
			}
		}
		return false;

	}

	private String getAssociation(ShPostTypeAttr shPostTypeAttr) {
		if (shUtils.isJSONValid(shPostTypeAttr.getWidgetSettings())) {
			JSONObject settings = new JSONObject(shPostTypeAttr.getWidgetSettings());
			if (settings.has(ShTuringSearchSettings.SEARCH)) {
				JSONObject searchSettings = settings.getJSONObject(ShTuringSearchSettings.SEARCH);
				if (searchSettings.has(ShTuringSearchSettings.ASSOCIATION)
						&& StringUtils.isNotBlank(searchSettings.getString(ShTuringSearchSettings.ASSOCIATION))) {
					return searchSettings.getString(ShTuringSearchSettings.ASSOCIATION);
				}
			}
		}
		return null;

	}

	private void addAdditionalAttributes(Map<String, Object> attributes, ShPost shPost, ShPostType shPostType) {
		Map<String, ShPostTypeAttr> shPostTypeMap = shPostTypeUtils.toMap(shPostType);
		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			ShPostTypeAttr shPostTypeAttr = shPostTypeMap.get(shPostAttr.getShPostTypeAttr().getName());
			if (isAdditionalField(shPostTypeAttr)) {
				String attributeName = shPostAttr.getShPostTypeAttr().getName().toLowerCase();
				if (!isSamePostTypeField(shPostTypeAttr) && hasCustomFieldName(shPostTypeAttr))
					attributeName = getCustomFieldName(shPostTypeAttr);
				if (shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.MULTI_SELECT)) {
					Set<String> multiValue = new HashSet<>();
					for (String multiSelectId : shPostAttr.getArrayValue()) {
						ShPost shPostMultiSelect = shPostRepository.findById(multiSelectId).orElse(null);
						if (shPostMultiSelect != null)
							multiValue.add(shPostMultiSelect.getTitle());
					}
					attributes.put(attributeName, multiValue);
				} else {
					attributes.put(attributeName, shPostAttr.getStrValue());
				}
			}
		}
	}

	private void addAssociationAttributes(Map<String, Object> attributes, ShPost shPost, ShPostType shPostType) {
		Map<String, ShPostAttr> shPostMap = shStagePostUtils.postToMap(shPost);
		for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {
			if (hasAssociation(shPostTypeAttr)) {
				if (shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.FILE)) {
					String shPostFileId = shPostMap.get(shPostTypeAttr.getName()).getStrValue();
					if (StringUtils.isNotBlank(shPostFileId)) {
						ShPost shFilePost = shPostRepository.findById(shPostFileId).orElse(null);
						attributes.put(getAssociation(shPostTypeAttr), shStagePostUtils.generatePostLink(shFilePost));
					}
				} else if (shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.DATE)) {
					attributes.put(getAssociation(shPostTypeAttr),
							formatDateToTuring(shPostMap.get(shPostTypeAttr.getName()).getDateValue()));
				} else
					attributes.put(getAssociation(shPostTypeAttr),
							shPostMap.get(shPostTypeAttr.getName()).getStrValue());
			}
		}
	}

	private void addDefaultAttributes(Map<String, Object> attributes, ShPost shPost) {
		attributes.put("type", shPost.getShPostType().getTitle());
		attributes.put("title", shPost.getTitle());
		attributes.put("abstract", shPost.getSummary());

		if (shPost.getShPostType().getName().equals(ShSystemPostType.FILE)) {
			attributes.put("image", shStagePostUtils.generatePostLink(shPost));
		}
	}

	private void sendServer(TurSNJobItems turSNJobItems) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonResult = mapper.writeValueAsString(turSNJobItems);
			Charset utf8Charset = Charset.forName("UTF-8");
			Charset customCharset = Charset.forName(encoding);

			ByteBuffer inputBuffer = ByteBuffer.wrap(jsonResult.toString().getBytes());

			// decode UTF-8
			CharBuffer data = utf8Charset.decode(inputBuffer);

			// encode
			ByteBuffer outputBuffer = customCharset.encode(data);

			byte[] outputData;

			outputData = new String(outputBuffer.array()).getBytes("UTF-8");

			String jsonUTF8 = new String(outputData);

			CloseableHttpClient client = HttpClients.createDefault();
			String serviceAPI = "%s/api/sn/%s/import";

			HttpPost httpPost = new HttpPost(String.format(serviceAPI, turingServer, shSite.getName()));
			if (showOutput) {
				System.out.println(jsonUTF8);
			}
			StringEntity entity = new StringEntity(new String(jsonUTF8), "UTF-8");
			httpPost.setEntity(entity);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setHeader("Accept-Encoding", "UTF-8");

			@SuppressWarnings("unused")
			CloseableHttpResponse response = client.execute(httpPost);

			client.close();
		} catch (IOException e) {
			logger.error("sendServer: ", e);
		}

	}
}
