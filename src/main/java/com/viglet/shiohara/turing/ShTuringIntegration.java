package com.viglet.shiohara.turing;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

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
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.utils.ShPostUtils;

@Component
public class ShTuringIntegration {
	private int chunk = 100;
	public String encoding = "UTF-8";
	private String turingServer = "http://localhost:2700";
	private String site = "1";
	public boolean showOutput = true;
	
	@Autowired
	private ShPostUtils shPostUtils;
	public void preparePost(ShPost shPost) throws ClientProtocolException, IOException {
		JSONArray shPosts = new JSONArray();
		JSONObject shPostJson = this.toJSON(shPost);
		shPosts.put(shPostJson);
		this.sendServer(shPosts, 1);
		
	}
	
	public JSONObject toJSON(ShPost shPost) {
		JSONObject shPostItemAttrs = new JSONObject();
	
		shPostItemAttrs.put("id", shPost.getId());
		shPostItemAttrs.put("type", shPost.getShPostType().getTitle());
		shPostItemAttrs.put("title", shPost.getTitle());
		shPostItemAttrs.put("text", shPost.getSummary());
		shPostItemAttrs.put("url", "http://localhost:2710" + shPostUtils.generatePostLink(shPost));
		if (shPost.getShPostType().getName().equals(ShSystemPostType.FILE)) {
			shPostItemAttrs.put("image","http://localhost:2710" + shPostUtils.generatePostLink(shPost));			
		}
		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			if (shPostAttr.getShPostTypeAttr().getName() != null) {
				shPostItemAttrs.put("sh_" + shPostAttr.getShPostTypeAttr().getName().toLowerCase(), shPostAttr.getStrValue());
			}
		}
		return shPostItemAttrs;
	}
	public void sendServer(JSONArray jsonResult, int chunkTotal) throws ClientProtocolException, IOException {
		int initial = 1;
		if (chunkTotal > chunk) {
			initial = chunkTotal - chunk;
		}

		Charset utf8Charset = Charset.forName("UTF-8");
		Charset customCharset = Charset.forName(encoding);

		ByteBuffer inputBuffer = ByteBuffer.wrap(jsonResult.toString().getBytes());

		// decode UTF-8
		CharBuffer data = utf8Charset.decode(inputBuffer);

		// encode
		ByteBuffer outputBuffer = customCharset.encode(data);

		byte[] outputData = new String(outputBuffer.array()).getBytes("UTF-8");
		String jsonUTF8 = new String(outputData);

		System.out.print("Importing " + initial + " to " + chunkTotal + " items\n");
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

		CloseableHttpResponse response = client.execute(httpPost);
		// System.out.println(response.toString());
		client.close();
	}
}
