package com.viglet.shiohara.provider.otcs;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.viglet.shiohara.provider.otcs.bean.folder.ShOTCSFolderBean;
import com.viglet.shiohara.provider.otcs.bean.object.ShOTCSObjectBean;
import com.viglet.shiohara.provider.otcs.bean.ticket.ShOTCSTicketBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

@Component
public class ShOTCSProvider {

	private static final Log logger = LogFactory.getLog(ShOTCSProvider.class);

	private final static String OTCS_TICKET = "OTCSTicket";

	private ObjectMapper objectMapper = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

	private HttpClient httpClient = HttpClientBuilder.create().build();

	private ResponseHandler<String> responseHandler = new BasicResponseHandler();

	private String baseURL = null;

	private String username = null;

	private String password = null;

	public void init(String baseURL, String username, String password) {
		this.baseURL = baseURL;
		this.username = username;
		this.password = password;
	}

	public ShOTCSFolderBean getRootFolder() {

		return this.getFolder(2000);
	}

	public ShOTCSFolderBean getFolder(int id) {
		ShOTCSTicketBean sOTCSTicketBean = this.otcsAuth();

		ShOTCSFolderBean shOTCSFolderBean = null;
		try {
			HttpGet httpGet = new HttpGet(String.format("%s/api/v2/nodes/%d/nodes", this.baseURL, id));
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

		return shOTCSFolderBean;
	}

	public ShOTCSObjectBean getObject(int id) {
		ShOTCSTicketBean sOTCSTicketBean = this.otcsAuth();

		ShOTCSObjectBean shOTCSObjetBean = null;
		try {
			HttpGet httpGet = new HttpGet(String.format("%s/api/v2/nodes/%d", this.baseURL, id));
			httpGet.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			httpGet.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
			httpGet.setHeader(OTCS_TICKET, sOTCSTicketBean.getTicket());
			HttpResponse response = httpClient.execute(httpGet);
			shOTCSObjetBean = objectMapper.readValue(responseHandler.handleResponse(response), ShOTCSObjectBean.class);

		} catch (UnsupportedOperationException e) {
			logger.error("rootFolder UnsupportedOperationException: ", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("rootFolder IOException: ", e);
		}

		return shOTCSObjetBean;
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

	public InputStream getDownload(int id) {
		ShOTCSTicketBean sOTCSTicketBean = this.otcsAuth();
		InputStream inputStream = null;
		try {
			HttpGet httpGet = new HttpGet(String.format("%s/api/v2/nodes/%d/content", this.baseURL, id));
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
}
