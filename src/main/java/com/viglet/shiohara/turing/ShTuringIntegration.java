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

package com.viglet.shiohara.turing;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

@Component
public class ShTuringIntegration {
	private String encoding = "UTF-8";
	private String turingServer = "http://localhost:2700";
	private String site = "1";
	private boolean showOutput = false;
	private final int INDEX_TYPE = 1;
	private final int DEINDEX_TYPE = 2;
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
			ShSite shSite = shObjectUtils.getSite(shObject);
			if (StringUtils.isNotBlank(shSite.getSearchablePostTypes())) {
				JSONObject searchablePostTypes = new JSONObject(shSite.getSearchablePostTypes());
				JSONArray shObjects = new JSONArray();
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
						JSONObject shObjectJson = this.toJSON(shObject);
						shObjects.put(shObjectJson);
					}

					if (shObjects != null && shObjects.length() > 0) {
						this.sendServer(shObjects, 1, INDEX_TYPE);
					}
				}
			}
		}
	}

	public void deindexObject(ShObject shObject) throws ClientProtocolException, IOException {
		if (turingEnabled) {
			ShSite shSite = shObjectUtils.getSite(shObject);
			if (StringUtils.isNotBlank(shSite.getSearchablePostTypes())) {
				JSONObject searchablePostTypes = new JSONObject(shSite.getSearchablePostTypes());
				JSONArray shObjects = new JSONArray();
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
						JSONObject shObjectId = new JSONObject();
						shObjectId.put("id", shObject.getId());
						shObjects.put(shObjectId);
					}

					if (shObjects != null && shObjects.length() > 0) {
						this.sendServer(shObjects, 1, DEINDEX_TYPE);
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

	private JSONObject toJSON(ShObject shObject) {
		JSONObject shObjectJSON = new JSONObject();
		shObjectJSON.put("id", shObject.getId());

		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		df.setTimeZone(tz);

		shObjectJSON.put("publication_date", df.format(shObject.getDate()));
		shObjectJSON.put("url", shObjectUtils.generateObjectLink(shObject));

		if (shObject instanceof ShPost) {
			ShPost shPost = (ShPost) shObject;
			Map<String, ShPostAttr> shPostMap = shPostUtils.postToMap(shPost);

			ShPostType shPostType = shPostTypeRepository.findById(shPost.getShPostType().getId()).get();
			Map<String, ShPostTypeAttr> shPostTypeMap = shPostTypeUtils.toMap(shPostType);

			shObjectJSON.put("type", shPost.getShPostType().getTitle());
			shObjectJSON.put("title", shPost.getTitle());
			shObjectJSON.put("abstract", shPost.getSummary());
			
			if (shPost.getShPostType().getName().equals(ShSystemPostType.FILE)) {
				shObjectJSON.put("image", shPostUtils.generatePostLink(shPost));
			}

			for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {
				if (StringUtils.isNotBlank(shPostTypeAttr.getWidgetSettings())
						&& shUtils.isJSONValid(shPostTypeAttr.getWidgetSettings())) {
					JSONObject settings = new JSONObject(shPostTypeAttr.getWidgetSettings());
					if (settings.has("search")) {
						JSONObject searchSettings = settings.getJSONObject("search");
						if (searchSettings.has("association")
								&& StringUtils.isNotBlank(searchSettings.getString("association"))) {
							if (shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.FILE)) {
								ShPost shFilePost = shPostRepository
										.findById(shPostMap.get(shPostTypeAttr.getName()).getStrValue()).get();
								shObjectJSON.put(searchSettings.getString("association"),
										shPostUtils.generatePostLink(shFilePost));
							} else {
								shObjectJSON.put(searchSettings.getString("association"),
										shPostMap.get(shPostTypeAttr.getName()).getStrValue());
							}
						}
					}
				}
			}

			for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
				ShPostTypeAttr shPostTypeAttr = shPostTypeMap.get(shPostAttr.getShPostTypeAttr().getName());
				JSONObject settings = new JSONObject(shPostTypeAttr.getWidgetSettings());
				if (settings.has("search")) {
					JSONObject searchSettings = settings.getJSONObject("search");
					if (searchSettings.has("isAdditionalField") && (searchSettings.getInt("isAdditionalField") == 1)) {
						if (searchSettings.has("isSamePostTypeField")
								&& (searchSettings.getInt("isSamePostTypeField") == 1)) {
							shObjectJSON.put("sh_" + shPostAttr.getShPostTypeAttr().getName().toLowerCase(),
									shPostAttr.getStrValue());
						} else {
							if (searchSettings.has("customFieldName")
									&& StringUtils.isNotBlank(searchSettings.getString("customFieldName"))) {
								shObjectJSON.put("sh_" + searchSettings.getString("customFieldName"),
										shPostAttr.getStrValue());
							}
						}
					}
				}
			}
		} else if (shObject instanceof ShFolder) {
			ShFolder shFolder = (ShFolder) shObject;
			shObjectJSON.put("title", shFolder.getName());
			ShPost shFolderIndexPost = shFolderUtils.getFolderIndex(shFolder);
			if (shFolderIndexPost != null) {
				Map<String, ShPostAttr> shFolderIndexPostMap = shPostUtils.postToMap(shFolderIndexPost);	
				if (shFolderIndexPostMap.containsKey(ShSystemPostTypeAttr.DESCRIPTION)) {
					shObjectJSON.put("abstract", shFolderIndexPostMap.get(ShSystemPostTypeAttr.DESCRIPTION).getStrValue());
				}
			}
		}

		return shObjectJSON;
	}

	private void sendServer(JSONArray jsonResult, int chunkTotal, int type)
			throws ClientProtocolException, IOException {

		Charset utf8Charset = Charset.forName("UTF-8");
		Charset customCharset = Charset.forName(encoding);

		ByteBuffer inputBuffer = ByteBuffer.wrap(jsonResult.toString().getBytes());

		// decode UTF-8
		CharBuffer data = utf8Charset.decode(inputBuffer);

		// encode
		ByteBuffer outputBuffer = customCharset.encode(data);

		byte[] outputData = new String(outputBuffer.array()).getBytes("UTF-8");
		String jsonUTF8 = new String(outputData);

		// System.out.print("Importing " + initial + " to " + chunkTotal + " items\n");
		CloseableHttpClient client = HttpClients.createDefault();
		String serviceAPI = null;
		switch (type) {
		case INDEX_TYPE:
			serviceAPI = "%s/api/sn/%s/import";
			break;
		case DEINDEX_TYPE:
			serviceAPI = "%s/api/sn/%s/deindex";
			break;
		default:
			break;

		}
		if (serviceAPI != null) {
			HttpPost httpPost = new HttpPost(String.format(serviceAPI, turingServer, site));
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
}
