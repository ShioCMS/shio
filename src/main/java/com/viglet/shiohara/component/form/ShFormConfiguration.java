package com.viglet.shiohara.component.form;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;

public class ShFormConfiguration {

	private HttpMethod method;
	private UUID pageLayout;
	private boolean createPost;
	private UUID folder;

	public ShFormConfiguration(JSONObject setting) {
		super();
		this.setMethod(HttpMethod.valueOf(setting.getString("method")));
		this.setPageLayout(UUID.fromString(setting.getString("pageLayout")));
		this.setCreatePost(setting.getInt("createPost") == 1 ? true : false);
		this.setFolder(
				StringUtils.isNotBlank(setting.getString("folder")) ? UUID.fromString(setting.getString("folder"))
						: null);
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public UUID getPageLayout() {
		return pageLayout;
	}

	public void setPageLayout(UUID pageLayout) {
		this.pageLayout = pageLayout;
	}

	public boolean isCreatePost() {
		return createPost;
	}

	public void setCreatePost(boolean createPost) {
		this.createPost = createPost;
	}

	public UUID getFolder() {
		return folder;
	}

	public void setFolder(UUID folder) {
		this.folder = folder;
	}

}
