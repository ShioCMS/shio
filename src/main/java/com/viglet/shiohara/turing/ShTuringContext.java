package com.viglet.shiohara.turing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
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
@RequestMapping("/__tur/sn/{siteName}/search")
public class ShTuringContext {
	@GetMapping
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
}
