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

package com.viglet.shiohara.provider.otmm;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.viglet.shiohara.provider.ShProvider;
import com.viglet.shiohara.provider.ShProviderBreadcrumbItem;
import com.viglet.shiohara.provider.ShProviderFolder;
import com.viglet.shiohara.provider.ShProviderPost;
import com.viglet.shiohara.provider.otmm.bean.assets.ShOTMMAssetBean;
import com.viglet.shiohara.provider.otmm.bean.assets.ShOTMMAssetDetailBean;
import com.viglet.shiohara.provider.otmm.bean.assets.ShOTMMAssetsBean;
import com.viglet.shiohara.provider.otmm.bean.folders.ShOTMMFolderBean;
import com.viglet.shiohara.provider.otmm.bean.folders.ShOTMMFoldersBean;
import com.viglet.shiohara.provider.otmm.bean.sessions.ShOTMMSessionsBean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

public class ShOTMMProvider implements ShProvider {

	private static final Log logger = LogFactory.getLog(ShOTMMProvider.class);
	private static final String PROVIDER_NAME = "OTMM";
	private static final String URL = "URL";
	private static final String USERNAME = "USERNAME";
	private static final String PASSWORD = "PASSWORD";

	private ObjectMapper objectMapper = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

	private CookieStore httpCookieStore = new BasicCookieStore();

	private HttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore).build();

	private ResponseHandler<String> responseHandler = new BasicResponseHandler();

	private String baseURL = null;

	private String username = null;

	private String password = null;

	public void init(Map<String, String> variables) {
		this.baseURL = variables.get(URL);
		this.username = variables.get(USERNAME);
		this.password = variables.get(PASSWORD);
	}

	public ShProviderFolder getRootFolder() {

		this.otmmAuth();
		ShOTMMFoldersBean shOTMMFoldersBean = null;
		try {
			HttpGet httpGet = new HttpGet(String.format("%s/otmmapi/v5/folders/rootfolders", this.baseURL));
			httpGet.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			httpGet.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
			HttpResponse response = httpClient.execute(httpGet);

			shOTMMFoldersBean = objectMapper.readValue(responseHandler.handleResponse(response),
					ShOTMMFoldersBean.class);

		} catch (UnsupportedOperationException e) {
			logger.error("getRootFolder UnsupportedOperationException: ", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("getRootFolder IOException: ", e);
		}

		ShProviderFolder shProviderFolder = new ShProviderFolder();

		shProviderFolder.setId("_root");
		shProviderFolder.setName("OTMM Root");
		shProviderFolder.setBreadcrumb(null);
		shProviderFolder.setProviderName(PROVIDER_NAME);
		shProviderFolder.setParentId(null);

		for (ShOTMMFolderBean folder : shOTMMFoldersBean.getFoldersResource().getFolderList()) {
			String resultId = folder.getAssetId();

			String resultName = folder.getName();

			Date resultDate = new Date();

			ShProviderFolder shProviderFolderChild = new ShProviderFolder();
			shProviderFolderChild.setId(resultId);
			shProviderFolderChild.setName(resultName);
			shProviderFolderChild.setDate(resultDate);

			shProviderFolder.getFolders().add(shProviderFolderChild);

		}

		return shProviderFolder;
	}

	public ShProviderFolder getFolder(String id) {
		this.otmmAuth();
		ShProviderFolder shProviderFolder = new ShProviderFolder();

		shProviderFolder.setId(id);
		shProviderFolder.setName("OTMM Folder");
		shProviderFolder.setBreadcrumb(null);
		shProviderFolder.setProviderName(PROVIDER_NAME);
		shProviderFolder.setParentId(null);

		this.getOTMMFolders(id, shProviderFolder);

		this.getOTMMAssets(id, shProviderFolder);

		return shProviderFolder;
	}

	private void getOTMMFolders(String id, ShProviderFolder shProviderFolder) {
		ShOTMMFoldersBean shOTMMFoldersBean = null;
		try {
			HttpGet httpGet = new HttpGet(String.format("%s/otmmapi/v5/folders/%s/folders", this.baseURL, id));
			httpGet.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			httpGet.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
			HttpResponse response = httpClient.execute(httpGet);
			shOTMMFoldersBean = objectMapper.readValue(responseHandler.handleResponse(response),
					ShOTMMFoldersBean.class);

		} catch (UnsupportedOperationException e) {
			logger.error("getOTMMFolders UnsupportedOperationException: ", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("getOTMMFolders IOException: ", e);
		}

		for (ShOTMMFolderBean folder : shOTMMFoldersBean.getFoldersResource().getFolderList()) {
			String resultId = folder.getAssetId();

			String resultName = folder.getName();

			Date resultDate = new Date();

			ShProviderFolder shProviderFolderChild = new ShProviderFolder();
			shProviderFolderChild.setId(resultId);
			shProviderFolderChild.setName(resultName);
			shProviderFolderChild.setDate(resultDate);

			shProviderFolder.getFolders().add(shProviderFolderChild);

		}
	}

	private void getOTMMAssets(String id, ShProviderFolder shProviderFolder) {
		ShOTMMAssetsBean shOTMMAssetsBean = null;
		try {
			HttpGet httpGet = new HttpGet(String.format("%s/otmmapi/v5/folders/%s/assets", this.baseURL, id));
			httpGet.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			httpGet.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
			HttpResponse response = httpClient.execute(httpGet);
			shOTMMAssetsBean = objectMapper.readValue(responseHandler.handleResponse(response), ShOTMMAssetsBean.class);

		} catch (UnsupportedOperationException e) {
			logger.error("getOTMMAssets UnsupportedOperationException: ", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("getOTMMAssets IOException: ", e);
		}

		for (ShOTMMAssetBean asset : shOTMMAssetsBean.getAssetsResource().getAssetList()) {
			String postId = asset.getAssetId();

			String postTitle = asset.getName();

			Date postDate = new Date();

			String postType = asset.getContentType();

			ShProviderPost shProviderPostChild = new ShProviderPost();

			shProviderPostChild.setId(postId);
			shProviderPostChild.setTitle(postTitle);
			shProviderPostChild.setDate(postDate);
			shProviderPostChild.setType(postType);

			shProviderFolder.getPosts().add(shProviderPostChild);

		}
	}

	public ShProviderPost getObject(String id) {
		this.otmmAuth();

		ShOTMMAssetDetailBean shOTMMAssetDetailBean = null;

		try {
			HttpGet httpGet = new HttpGet(String.format("%s/otmmapi/v5/assets/%s", this.baseURL, id));
			httpGet.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			httpGet.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
			HttpResponse response = httpClient.execute(httpGet);
			shOTMMAssetDetailBean = objectMapper.readValue(responseHandler.handleResponse(response),
					ShOTMMAssetDetailBean.class);

		} catch (UnsupportedOperationException e) {
			logger.error("getObject UnsupportedOperationException: ", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("getObject IOException: ", e);
		}

		ShProviderPost shProviderPost = new ShProviderPost();
		shProviderPost.setId(id);
		shProviderPost.setTitle(shOTMMAssetDetailBean.getAssetResource().getAsset().getName());
		shProviderPost.setParentId(null);
		return shProviderPost;
	}

	private ShOTMMSessionsBean otmmAuth() {
		List<NameValuePair> form = new ArrayList<>();
		form.add(new BasicNameValuePair("username", this.username));
		form.add(new BasicNameValuePair("password", this.password));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

		HttpPost httpPost = new HttpPost(String.format("%s/otmmapi/v5/sessions", this.baseURL));
		httpPost.setEntity(entity);
		httpPost.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());

		ShOTMMSessionsBean shOTMMSessionsBean = null;

		try {
			HttpResponse response = httpClient.execute(httpPost);

			shOTMMSessionsBean = objectMapper.readValue(responseHandler.handleResponse(response),
					ShOTMMSessionsBean.class);

		} catch (UnsupportedOperationException e) {
			logger.error("otmmAuth UnsupportedOperationException: ", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("otmmAuth IOException: ", e);
		}
		return shOTMMSessionsBean;
	}

	public InputStream getDownload(String id) {
		this.otmmAuth();
		InputStream inputStream = null;

		try {
			HttpGet httpGet = new HttpGet(String.format("%s/otmmapi/v5/assets/%s/contents", this.baseURL, id));
			httpGet.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			httpGet.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
			HttpResponse response = httpClient.execute(httpGet);
			inputStream = response.getEntity().getContent();
		} catch (UnsupportedOperationException e) {
			logger.error("getDownload UnsupportedOperationException: ", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("getDownload IOException: ", e);
		}

		return inputStream;

	}

	private List<ShProviderBreadcrumbItem> getBreadcrumb(String id) {
		ArrayList<ShProviderBreadcrumbItem> breadcrumb = new ArrayList<>();

		this.getParentBreadcrumbItem(id, breadcrumb);

		return breadcrumb;
	}

	private void getParentBreadcrumbItem(String id, ArrayList<ShProviderBreadcrumbItem> breadcrumb) {
		if (!StringUtils.isBlank(id) && Integer.parseInt(id) > 0) {
			ShProviderPost shProviderPost = this.getObject(id);

			ShProviderBreadcrumbItem shProviderBreadcrumbItem = new ShProviderBreadcrumbItem();
			shProviderBreadcrumbItem.setId(shProviderPost.getId());
			shProviderBreadcrumbItem.setTitle(shProviderPost.getTitle());

			this.getParentBreadcrumbItem(shProviderPost.getParentId(), breadcrumb);
			breadcrumb.add(shProviderBreadcrumbItem);
		}
	}
}
