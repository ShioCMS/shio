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

package com.viglet.shiohara.provider.otcs;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import com.viglet.shiohara.provider.otcs.bean.folder.ShOTCSFolderBean;
import com.viglet.shiohara.provider.otcs.bean.object.ShOTCSObjectBean;
import com.viglet.shiohara.provider.otcs.bean.result.ShOTCSResultsBean;
import com.viglet.shiohara.provider.otcs.bean.ticket.ShOTCSTicketBean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

public class ShOTCSProvider implements ShProvider {

	private static final Log logger = LogFactory.getLog(ShOTCSProvider.class);
	private static final String OTCS_TICKET = "OTCSTicket";
	private static final String ROOT_FOLDER_ID = "2000";
	private static final String PROVIDER_NAME = "OTCS";
	private static final String URL = "URL";
	private static final String USERNAME = "USERNAME";
	private static final String PASSWORD = "PASSWORD";

	private ObjectMapper objectMapper = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

	private HttpClient httpClient = HttpClientBuilder.create().build();

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

		return this.getFolder(ROOT_FOLDER_ID);
	}

	public ShProviderFolder getFolder(String id) {
		ShOTCSTicketBean sOTCSTicketBean = this.otcsAuth();

		ShOTCSFolderBean shOTCSFolderBean = null;
		try {
			HttpGet httpGet = new HttpGet(String.format("%s/api/v2/nodes/%s/nodes", this.baseURL, id));
			httpGet.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			httpGet.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
			httpGet.setHeader(OTCS_TICKET, sOTCSTicketBean.getTicket());
			HttpResponse response = httpClient.execute(httpGet);
			shOTCSFolderBean = objectMapper.readValue(responseHandler.handleResponse(response), ShOTCSFolderBean.class);

		} catch (UnsupportedOperationException e) {
			logger.error("rootFolder UnsupportedOperationException: ", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("rootFolder IOException: ", e);
		}

		ShProviderPost shProviderPost = this.getObject(id, true);
		ShProviderFolder shProviderFolder = new ShProviderFolder();

		shProviderFolder.setId(id);
		shProviderFolder.setName(shProviderPost.getTitle());
		shProviderFolder.setBreadcrumb(this.getBreadcrumb(id));
		shProviderFolder.setProviderName(PROVIDER_NAME);
		shProviderFolder.setParentId(shProviderPost.getParentId());

		for (ShOTCSResultsBean results : shOTCSFolderBean.getResults()) {

			if (results.getData().getProperties().getType_name().equals("Folder")) {

				String resultId = Integer.toString(results.getData().getProperties().getId());

				String resultName = results.getData().getProperties().getName();

				Date resultDate = results.getData().getProperties().getCreate_date();

				ShProviderFolder shProviderFolderChild = new ShProviderFolder();
				shProviderFolderChild.setId(resultId);
				shProviderFolderChild.setName(resultName);
				shProviderFolderChild.setDate(resultDate);

				shProviderFolder.getFolders().add(shProviderFolderChild);
			} else {

				String postId = Integer.toString(results.getData().getProperties().getId());

				String postTitle = results.getData().getProperties().getName();

				Date postDate = results.getData().getProperties().getCreate_date();

				String postType = results.getData().getProperties().getType_name();

				ShProviderPost shProviderPostChild = new ShProviderPost();

				shProviderPostChild.setId(postId);
				shProviderPostChild.setTitle(postTitle);
				shProviderPostChild.setDate(postDate);
				shProviderPostChild.setType(postType);

				shProviderFolder.getPosts().add(shProviderPostChild);

			}
		}

		return shProviderFolder;
	}

	public ShProviderPost getObject(String id, boolean isFolder) {
		ShOTCSTicketBean sOTCSTicketBean = this.otcsAuth();

		ShOTCSObjectBean shOTCSObjetBean = null;
		try {
			HttpGet httpGet = new HttpGet(String.format("%s/api/v2/nodes/%s", this.baseURL, id));
			httpGet.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			httpGet.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
			httpGet.setHeader(OTCS_TICKET, sOTCSTicketBean.getTicket());
			HttpResponse response = httpClient.execute(httpGet);
			shOTCSObjetBean = objectMapper.readValue(responseHandler.handleResponse(response), ShOTCSObjectBean.class);

		} catch (UnsupportedOperationException e) {
			logger.error("getObject UnsupportedOperationException: ", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("getObject IOException: ", e);
		}

		ShProviderPost shProviderPost = new ShProviderPost();
		shProviderPost.setId(id);
		shProviderPost.setTitle(shOTCSObjetBean.getResults().getData().getProperties().getName());
		shProviderPost
				.setParentId(Integer.toString(shOTCSObjetBean.getResults().getData().getProperties().getParent_id()));
		return shProviderPost;
	}

	private ShOTCSTicketBean otcsAuth() {
		List<NameValuePair> form = new ArrayList<>();
		form.add(new BasicNameValuePair("username", this.username));
		form.add(new BasicNameValuePair("password", this.password));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

		HttpPost httpPost = new HttpPost(String.format("%s/api/v1/auth", this.baseURL));
		httpPost.setEntity(entity);
		httpPost.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());

		ShOTCSTicketBean sOTCSTicketBean = null;

		try {
			HttpResponse response = httpClient.execute(httpPost);

			sOTCSTicketBean = objectMapper.readValue(responseHandler.handleResponse(response), ShOTCSTicketBean.class);

		} catch (UnsupportedOperationException e) {
			logger.error("rootFolder UnsupportedOperationException: ", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("rootFolder IOException: ", e);
		}
		return sOTCSTicketBean;
	}

	public InputStream getDownload(String id) {
		ShOTCSTicketBean sOTCSTicketBean = this.otcsAuth();
		InputStream inputStream = null;
		try {
			HttpGet httpGet = new HttpGet(String.format("%s/api/v2/nodes/%s/content", this.baseURL, id));
			httpGet.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			httpGet.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
			httpGet.setHeader(OTCS_TICKET, sOTCSTicketBean.getTicket());
			HttpResponse response = httpClient.execute(httpGet);
			inputStream = response.getEntity().getContent();
		} catch (UnsupportedOperationException e) {
			logger.error("rootFolder UnsupportedOperationException: ", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("rootFolder IOException: ", e);
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
			ShProviderPost shProviderPost = this.getObject(id, true);

			ShProviderBreadcrumbItem shProviderBreadcrumbItem = new ShProviderBreadcrumbItem();
			shProviderBreadcrumbItem.setId(shProviderPost.getId());
			shProviderBreadcrumbItem.setTitle(shProviderPost.getTitle());

			this.getParentBreadcrumbItem(shProviderPost.getParentId(), breadcrumb);
			breadcrumb.add(shProviderBreadcrumbItem);
		}
	}
}
