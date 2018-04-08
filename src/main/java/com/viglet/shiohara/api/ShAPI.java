package com.viglet.shiohara.api;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2")
@Api(value="/", tags="Heartbeat", description="Heartbeat")
public class ShAPI {

	@Autowired
	private ShAPIBean shAPIBean;

	@GetMapping
	public ShAPIBean shApiInfo() throws JSONException {

		shAPIBean.setProduct("Viglet Shiohara");

		return shAPIBean;
	}
}
