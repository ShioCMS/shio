package com.viglet.shiohara.spring.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShContext {
	@GetMapping("/welcome")
	public String welcome() {
		return "/welcome/index";
	}
	
	@GetMapping("/403")
    public String error403() {
        return "/template/error/403";
    }
	
	@GetMapping("/content")
	public String content() {
		return "/content/index";
	}

}
