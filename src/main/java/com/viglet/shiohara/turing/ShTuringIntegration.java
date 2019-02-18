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
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.viglet.shiohara.utils.ShFolderUtils;
import com.viglet.shiohara.utils.ShObjectUtils;
import com.viglet.shiohara.utils.ShPostTypeUtils;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.utils.ShUtils;
import com.viglet.shiohara.widget.ShSystemWidget;
import com.viglet.turing.api.sn.job.TurSNJobAction;
import com.viglet.turing.api.sn.job.TurSNJobItem;
import com.viglet.turing.api.sn.job.TurSNJobItems;

@Component
public class ShTuringIntegration {
	private String encoding = "UTF-8";
	private String turingServer = "http://localhost:2700";
	private ShSite shSite = null;
	private boolean showOutput = false;
	private final boolean turingEnabled = true;

	@Autowired
	private ShPostUtils shPostUtils;
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
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShFolderUtils shFolderUtils;

	public void indexObject(ShObject shObject) throws ClientProtocolException, IOException {
		if (turingEnabled) {
			shSite = shObjectUtils.getSite(shObject);
			TurSNJobItems turSNJobItems = new TurSNJobItems();
			if (StringUtils.isNotBlank(shSite.getSearchablePostTypes())) {
				JSONObject searchablePostTypes = new JSONObject(shSite.getSearchablePostTypes());
				String objectName = null;

				if (shObject instanceof ShPost) {
					ShPost shPost = (ShPost) shObject;
					objectName = shPost.getShPostType().getName();
					if (objectName.equals(ShSystemPostType.FOLDER_INDEX)) {
						objectName = "FOLDER";
						shObject = shPost.getShFolder();
					}
				} else if (shObject instanceof ShFolder) {
					objectName = "FOLDER";
				}

				boolean visiblePage = shObjectUtils.isVisiblePage(shObject);

				if (visiblePage && objectName != null && searchablePostTypes.has(objectName)) {
					boolean isSearchable = searchablePostTypes.getBoolean(objectName);
					if (isSearchable) {
						TurSNJobItem turSNJobItem = this.toTurSNJobItem(shObject);
						turSNJobItem.setTurSNJobAction(TurSNJobAction.CREATE);
						turSNJobItems.add(turSNJobItem);
					}

					if (turSNJobItems != null) {
						this.sendServer(turSNJobItems);
					}
				}
			}
		}
	}

	public void deindexObject(ShObject shObject) throws ClientProtocolException, IOException {
		if (turingEnabled) {
			shSite = shObjectUtils.getSite(shObject);
			TurSNJobItems turSNJobItems = new TurSNJobItems();

			if (StringUtils.isNotBlank(shSite.getSearchablePostTypes())) {
				JSONObject searchablePostTypes = new JSONObject(shSite.getSearchablePostTypes());
				String objectName = null;
				if (shObject instanceof ShPost) {
					objectName = ((ShPost) shObject).getShPostType().getName();
				} else if (shObject instanceof ShFolder) {
					objectName = "FOLDER";
					ShFolder shFolder = (ShFolder) shObject;
					this.deindexInBatch(shPostRepository.findByShFolder(shFolder));
					for (ShFolder shFolderChild : shFolderRepository.findByParentFolder(shFolder)) {
						this.deindexObject(shFolderChild);
					}
				}

				if (searchablePostTypes.has(objectName)) {
					boolean isSearchable = searchablePostTypes.getBoolean(objectName);
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

	private void deindexInBatch(Iterable<ShPost> shPosts) throws ClientProtocolException, IOException {
		for (ShPost shPost : shPosts) {
			this.deindexObject(shPost);
		}
	}

	private TurSNJobItem toTurSNJobItem(ShObject shObject) {
		try {
			TurSNJobItem turSNJobItem = new TurSNJobItem();
			Map<String, Object> attributes = new HashMap<String, Object>();

			attributes.put("id", shObject.getId());

			TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
			df.setTimeZone(tz);

			attributes.put("publication_date", df.format(shObject.getDate()));
			attributes.put("url", shObjectUtils.generateObjectLink(shObject));

			if (shObject instanceof ShPost) {
				ShPost shPost = (ShPost) shObject;
				Map<String, ShPostAttr> shPostMap = shPostUtils.postToMap(shPost);

				ShPostType shPostType = shPostTypeRepository.findById(shPost.getShPostType().getId()).orElse(null);
				Map<String, ShPostTypeAttr> shPostTypeMap = shPostTypeUtils.toMap(shPostType);

				attributes.put("type", shPost.getShPostType().getTitle());
				attributes.put("title", shPost.getTitle());
				attributes.put("abstract", shPost.getSummary());

				if (shPost.getShPostType().getName().equals(ShSystemPostType.FILE)) {
					attributes.put("image", shPostUtils.generatePostLink(shPost));
				}

				for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {
					if (shUtils.isJSONValid(shPostTypeAttr.getWidgetSettings())) {
						JSONObject settings = new JSONObject(shPostTypeAttr.getWidgetSettings());
						if (settings.has("search")) {
							JSONObject searchSettings = settings.getJSONObject("search");
							if (searchSettings.has("association")
									&& StringUtils.isNotBlank(searchSettings.getString("association"))) {
								if (shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.FILE)) {
									ShPost shFilePost = shPostRepository
											.findById(shPostMap.get(shPostTypeAttr.getName()).getStrValue())
											.orElse(null);
									attributes.put(searchSettings.getString("association"),
											shPostUtils.generatePostLink(shFilePost));
								} else {
									attributes.put(searchSettings.getString("association"),
											shPostMap.get(shPostTypeAttr.getName()).getStrValue());
								}
							}
						}
					}
				}

				for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
					ShPostTypeAttr shPostTypeAttr = shPostTypeMap.get(shPostAttr.getShPostTypeAttr().getName());
					if (shUtils.isJSONValid(shPostTypeAttr.getWidgetSettings())) {
						JSONObject settings = new JSONObject(shPostTypeAttr.getWidgetSettings());
						if (settings.has("search")) {
							JSONObject searchSettings = settings.getJSONObject("search");
							if (searchSettings.has("isAdditionalField")
									&& (searchSettings.getInt("isAdditionalField") == 1)) {
								if (searchSettings.has("isSamePostTypeField")
										&& (searchSettings.getInt("isSamePostTypeField") == 1)) {
									attributes.put("sh_" + shPostAttr.getShPostTypeAttr().getName().toLowerCase(),
											shPostAttr.getStrValue());
								} else {
									if (searchSettings.has("customFieldName")
											&& StringUtils.isNotBlank(searchSettings.getString("customFieldName"))) {
										attributes.put("sh_" + searchSettings.getString("customFieldName"),
												shPostAttr.getStrValue());
									}
								}
							}
						}
					}
				}
			} else if (shObject instanceof ShFolder) {
				ShFolder shFolder = (ShFolder) shObject;
				attributes.put("title", shFolder.getName());
				ShPost shFolderIndexPost = shFolderUtils.getFolderIndex(shFolder);
				if (shFolderIndexPost != null) {
					Map<String, ShPostAttr> shFolderIndexPostMap = shPostUtils.postToMap(shFolderIndexPost);
					if (shFolderIndexPostMap.containsKey(ShSystemPostTypeAttr.DESCRIPTION)) {
						attributes.put("abstract",
								shFolderIndexPostMap.get(ShSystemPostTypeAttr.DESCRIPTION).getStrValue());
					}
				}
			}

			turSNJobItem.setAttributes(attributes);

			return turSNJobItem;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private void sendServer(TurSNJobItems turSNJobItems) throws ClientProtocolException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonResult = mapper.writeValueAsString(turSNJobItems);
		Charset utf8Charset = Charset.forName("UTF-8");
		Charset customCharset = Charset.forName(encoding);

		ByteBuffer inputBuffer = ByteBuffer.wrap(jsonResult.toString().getBytes());

		// decode UTF-8
		CharBuffer data = utf8Charset.decode(inputBuffer);

		// encode
		ByteBuffer outputBuffer = customCharset.encode(data);

		byte[] outputData = new String(outputBuffer.array()).getBytes("UTF-8");
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

	}
}
