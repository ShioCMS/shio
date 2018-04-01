package com.viglet.shiohara.api;


import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@RequestMapping("/api/v2")
public class ShAPI {

	@Autowired
	ShAPIBean shAPIBean;

	@RequestMapping(method = RequestMethod.GET)
	public ShAPIBean info() throws JSONException {

		shAPIBean.setProduct("Viglet Shiohara");

		return shAPIBean;
	}
}
