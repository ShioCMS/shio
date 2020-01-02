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
package com.viglet.shiohara.auth.otds.provider;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.viglet.shiohara.auth.otds.provider.bean.ShOTDSAuthenticationResponseBean;
import com.viglet.shiohara.auth.otds.provider.bean.ShOTDSCredencialBean;
import com.viglet.shiohara.auth.otds.provider.bean.ShOTDSTicketRequestBean;
import com.viglet.shiohara.auth.otds.provider.bean.ShOTDSUserResponseBean;
import com.viglet.shiohara.auth.otds.provider.bean.ShOTDSValuesBean;
import com.viglet.shiohara.persistence.model.auth.ShUser;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShOTDSService {
	@Value("${shiohara.auth.otds.enabled}")
	boolean isOTDS;
	@Value("${shiohara.auth.otds.host}")
	private String otdsHost;
	@Value("${shiohara.auth.otds.port}")
	private int otdsPort;
	@Value("${shiohara.auth.otds.resource-id}")
	private String otdsResourceId;
	@Value("${shiohara.auth.otds.secret-key}")
	private String otdsSecretKey;
	@Value("${shiohara.auth.otds.partition-name}")
	private String otdsPartitionName;
	@Value("${shiohara.auth.otds.domain}")
	private String otdsDomain;
	@Value("${shiohara.auth.otds.membership-filter-attribute}")
	private String otdsMembershipFilterAttribute;
	@Value("${shiohara.auth.otds.username-atribute}")
	private String otdsUsernameAttribute;
	@Value("${shiohara.auth.otds.user.filter}")
	private String otdsUserFilter;
	@Value("${shiohara.auth.otds.user.scope}")
	private String otdsUserScope;
	@Value("${shiohara.auth.otds.user.dn}")
	private String otdsUserDN;
	@Value("${shiohara.auth.otds.group.filter}")
	private String otdsGroupFilter;
	@Value("${shiohara.auth.otds.group.scope}")
	private String otdsGroupScope;
	@Value("${shiohara.auth.otds.group.dn}")
	private String otdsGroupDN;

	private static final String OTDS_TICKET = "OTDSTicket";
	private HttpClient httpClient = HttpClientBuilder.create().build();

	private ObjectMapper objectMapper = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	private ResponseHandler<String> responseHandler = new BasicResponseHandler();

	private static final Log logger = LogFactory.getLog(ShOTDSService.class);

	private String otdsTicket = null;

	public boolean isAuthorizedUser(String username, String password, boolean requestSsoTicket) {

		ShOTDSCredencialBean shOTDSCredencialBean = new ShOTDSCredencialBean();
		shOTDSCredencialBean.setUserName(username);
		shOTDSCredencialBean.setPassword(password);

		try {
			String jsonString = objectMapper.writeValueAsString(shOTDSCredencialBean);

			StringEntity entity = new StringEntity(jsonString);

			HttpPost httpPost = new HttpPost(
					String.format("http://%s:%d/otdsws/rest/authentication/credentials", otdsHost, otdsPort));
			httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			httpPost.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
			httpPost.setEntity(entity);

			HttpResponse response = httpClient.execute(httpPost);
			String responseString = responseHandler.handleResponse(response);
			ShOTDSAuthenticationResponseBean authenticationResponse = objectMapper.readValue(responseString,
					ShOTDSAuthenticationResponseBean.class);

			if (response.getStatusLine().getStatusCode() == 200
					&& this.validateTicket(authenticationResponse.getTicket())) {

				otdsTicket = authenticationResponse.getTicket();
				return true;
			}
		} catch (HttpResponseException e) {
			logger.error("authenticate HttpResponseException.");
		} catch (UnsupportedOperationException e) {
			logger.error("authenticate UnsupportedOperationException: ", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("authenticate IOException: ", e);
		}
		return false;
	}

	public boolean validateTicket(String ticket) {

		ShOTDSTicketRequestBean shOTDSTicketRequestBean = new ShOTDSTicketRequestBean();
		shOTDSTicketRequestBean.setSourceResourceId(otdsResourceId);
		shOTDSTicketRequestBean.setSecureSecret(otdsSecretKey);
		shOTDSTicketRequestBean.setTicket(ticket);

		try {
			String jsonString = objectMapper.writeValueAsString(shOTDSTicketRequestBean);

			StringEntity entity = new StringEntity(jsonString);

			HttpPost httpPost = new HttpPost(
					String.format("http://%s:%d/otdsws/rest/authentication/resource/validation", otdsHost, otdsPort));
			httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			httpPost.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
			httpPost.setEntity(entity);

			HttpResponse response = httpClient.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == 200) {
				return true;
			}
		} catch (HttpResponseException e) {
			logger.error("validateTicket HttpResponseException.");
		} catch (UnsupportedOperationException e) {
			logger.error("validateTicket UnsupportedOperationException: ", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("validateTicket IOException: ", e);
		}
		return false;
	}

	public ShOTDSUserResponseBean getUser(String userName) {

		ShOTDSUserResponseBean userResponse = new ShOTDSUserResponseBean();

		HttpGet httpGet = new HttpGet(String.format("http://%s:%d/otdsws/rest/users/%s", otdsHost, otdsPort, userName));
		httpGet.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
		httpGet.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
		httpGet.setHeader(OTDS_TICKET, otdsTicket);
		try {
			HttpResponse response = httpClient.execute(httpGet);

			String responseString = responseHandler.handleResponse(response);
			userResponse = objectMapper.readValue(responseString, ShOTDSUserResponseBean.class);

		} catch (HttpResponseException e) {
			logger.error("getUser HttpResponseException.");
		} catch (UnsupportedOperationException e) {
			logger.error("getUser UnsupportedOperationException: ", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("getUser IOException: ", e);
		}
		return userResponse;
	}
	
	
	public ShUser getCurrentUser() {
		ShUser shUser = new ShUser();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			shUser.setUsername(currentUserName);

			ShOTDSUserResponseBean userResponse = this.getUser(currentUserName);
			if (userResponse != null) {
				for (ShOTDSValuesBean value : userResponse.getValues()) {
					if (value.getName().equals("givenName")) {
						shUser.setFirstName((String) value.getValues()[0]);

					}
					if (value.getName().equals("sn")) {
						shUser.setLastName((String) value.getValues()[0]);

					}
				}
			}
		}
		return shUser;
	}
}
