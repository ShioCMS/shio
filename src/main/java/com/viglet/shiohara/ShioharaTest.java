package com.viglet.shiohara;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/test")
@Api(value = "/api/v2/test", tags = "Heartbeat", description = "Heartbeat")
public class ShioharaTest {

	@GetMapping
	public void test2() {
		
	}
}
