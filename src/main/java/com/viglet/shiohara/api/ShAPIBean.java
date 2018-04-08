package com.viglet.shiohara.api;

import org.springframework.stereotype.Component;

@Component

public class ShAPIBean {

	private String product;

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
	
}
