package com.viglet.shiohara.turing;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Map;

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

import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.utils.ShPostTypeUtils;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.utils.ShUtils;
import com.viglet.shiohara.widget.ShSystemWidget;

@Component
public class ShTuringIntegration {
	public String encoding = "UTF-8";
	private String turingServer = "http://localhost:2700";
	private String site = "1";
	public boolean showOutput = false;

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

	public void preparePost(ShPost shPost) throws ClientProtocolException, IOException {
		ShSite shSite = shPostUtils.getSite(shPost);
		if (StringUtils.isNotBlank(shSite.getSearchablePostTypes())) {
			JSONObject searchablePostTypes = new JSONObject(shSite.getSearchablePostTypes());
			if (searchablePostTypes.has(shPost.getShPostType().getName())) {
				boolean isSearchable = searchablePostTypes.getBoolean(shPost.getShPostType().getName());
				if (isSearchable) {
					JSONArray shPosts = new JSONArray();
					JSONObject shPostJson = this.toJSON(shPost);
					shPosts.put(shPostJson);
					this.sendServer(shPosts, 1);
				}
			}

		}
	}

	public JSONObject toJSON(ShPost shPost) {
		Map<String, ShPostAttr> shPostMap = shPostUtils.postToMap(shPost);
		JSONObject shPostItemAttrs = new JSONObject();
		ShPostType shPostType = shPostTypeRepository.findById(shPost.getShPostType().getId()).get();
		Map<String, ShPostTypeAttr> shPostTypeMap = shPostTypeUtils.toMap(shPostType);
		shPostItemAttrs.put("id", shPost.getId());
		shPostItemAttrs.put("type", shPost.getShPostType().getTitle());
		shPostItemAttrs.put("title", shPost.getTitle());
		shPostItemAttrs.put("text", shPost.getSummary());
		shPostItemAttrs.put("url", shPostUtils.generatePostLink(shPost));

		if (shPost.getShPostType().getName().equals(ShSystemPostType.FILE)) {
			shPostItemAttrs.put("image", shPostUtils.generatePostLink(shPost));
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
							shPostItemAttrs.put(searchSettings.getString("association"),
									shPostUtils.generatePostLink(shFilePost));
						} else {
							shPostItemAttrs.put(searchSettings.getString("association"),
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
						shPostItemAttrs.put("sh_" + shPostAttr.getShPostTypeAttr().getName().toLowerCase(),
								shPostAttr.getStrValue());
					} else {
						if (searchSettings.has("customFieldName")
								&& StringUtils.isNotBlank(searchSettings.getString("customFieldName"))) {
							shPostItemAttrs.put("sh_" + searchSettings.getString("customFieldName"),
									shPostAttr.getStrValue());
						}
					}
				}
			}
		}
		return shPostItemAttrs;
	}

	public void sendServer(JSONArray jsonResult, int chunkTotal) throws ClientProtocolException, IOException {

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
		HttpPost httpPost = new HttpPost(String.format("%s/api/sn/%s/import", turingServer, site));
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
