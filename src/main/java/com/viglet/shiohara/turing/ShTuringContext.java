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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/__tur/sn/{siteName}")
public class ShTuringContext {
	@GetMapping ("/search")
	public ResponseEntity<Object> turSNSiteSearchSelect(@PathVariable String siteName,
			@RequestParam(required = false, name = "q") String q,
			@RequestParam(required = false, name = "p") String strCurrentPage,
			@RequestParam(required = false, name = "fq[]") List<String> fq,
			@RequestParam(required = false, name = "sort") String sort, HttpServletRequest request)
			throws JSONException, URISyntaxException, ClientProtocolException, IOException {

		URIBuilder turingURL = new URIBuilder("http://localhost:2700/api/sn/" + siteName + "/search")
				.addParameter("q", q).addParameter("p", strCurrentPage).addParameter("sort", sort);

		if (fq != null) {
			for (String fqItem : fq) {
				turingURL.addParameter("fq[]", fqItem);
			}
		}

		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(turingURL.build().toString());
		
		HttpResponse response = client.execute(httpGet);
		
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
		final HttpHeaders httpHeaders= new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<Object> responseEntity = new ResponseEntity<Object>(result.toString(), httpHeaders, HttpStatus.OK);	
		return responseEntity;
	}
	
	@GetMapping ("/ac")
	public ResponseEntity<Object> turSNSiteAutoComplete(@PathVariable String siteName,
			@RequestParam(required = false, name = "q") String q, HttpServletRequest request)
			throws JSONException, URISyntaxException, ClientProtocolException, IOException {

		URIBuilder turingURL = new URIBuilder("http://localhost:2700/api/sn/" + siteName + "/ac")
				.addParameter("q", q);

		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(turingURL.build().toString());
		
		HttpResponse response = client.execute(httpGet);
		
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
		final HttpHeaders httpHeaders= new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<Object> responseEntity = new ResponseEntity<Object>(result.toString(), httpHeaders, HttpStatus.OK);	
		return responseEntity;
	}
}
