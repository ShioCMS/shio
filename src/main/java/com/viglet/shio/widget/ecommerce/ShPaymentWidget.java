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
package com.viglet.shio.widget.ecommerce;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.viglet.shio.ecommerce.ShEcomProductBean;
import com.viglet.shio.ecommerce.payment.ShPaymentSlip;
import com.viglet.shio.persistence.model.ecommerce.ShEcomPaymentTypeDefinition;
import com.viglet.shio.persistence.model.object.ShObject;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shio.persistence.repository.ecommerce.ShEcomPaymentTypeDefinitionRepository;
import com.viglet.shio.persistence.repository.post.ShPostRepository;
import com.viglet.shio.sites.ShSitesContextURL;
import com.viglet.shio.sites.utils.ShSitesPostUtils;
import com.viglet.shio.widget.ShWidgetImplementation;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShPaymentWidget implements ShWidgetImplementation {
	@Autowired
	private SpringTemplateEngine templateEngine;
	@Autowired
	private ShPaymentSlip shPaymentSlip;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShSitesPostUtils shSitesPostUtils;
	@Autowired
	private ShEcomPaymentTypeDefinitionRepository shEcomPaymentTypeDefinitionRepository;

	@Override
	public String render(ShPostTypeAttr shPostTypeAttr, ShObject shObject) {
		JSONObject settings = new JSONObject(shPostTypeAttr.getWidgetSettings());
		JSONArray paymentTypesJSON = settings.getJSONArray("paymentTypes");
		Set<JSONObject> paymentTypes = new HashSet<JSONObject>();

		for (int i = 0; i < paymentTypesJSON.length(); i++) {
			JSONObject paymentType = paymentTypesJSON.getJSONObject(i);
			paymentTypes.add(paymentType);
			String paymentTypeId = paymentType.getString("id");
			ShPost shPaymentTypePost = shPostRepository.findById(paymentTypeId).orElse(null);
			Map<String, ShPostAttr> shPaymentTypePostMap = shSitesPostUtils.postToMap(shPaymentTypePost);

			ShPostAttr shPaymentTypeDefinitionPostAttr = shPaymentTypePostMap.get("PAYMENT_TYPE_DEFINITION");
			JSONObject ptdJSON = new JSONObject(shPaymentTypeDefinitionPostAttr.getStrValue());

			ShEcomPaymentTypeDefinition shEcomPaymentTypeDefinition = shEcomPaymentTypeDefinitionRepository
					.findById(ptdJSON.getString("id")).get();
			paymentType.put("formPath", shEcomPaymentTypeDefinition.getFormPath());
		}

		// Product BEGIN
		JSONObject product = settings.getJSONObject("product");
		
		ShPost shProductPost = null;
		
		if (shObject instanceof ShPost) {
			shProductPost = (ShPost) shObject;
		} else {
			shProductPost = shPostRepository.findById(product.getString("post")).get();
		}
		
		Map<String, ShPostAttr> shProductPostMap = shSitesPostUtils.postToMap(shProductPost);

		ShEcomProductBean shProduct = new ShEcomProductBean();
		shProduct.setName(shProductPostMap.get(product.getString("name")).getStrValue());
		shProduct.setDescription(shProductPostMap.get(product.getString("description")).getStrValue());
		shProduct.setValue(Double.valueOf(shProductPostMap.get(product.getString("value")).getStrValue()));

		/// Product END

		final Context ctx = new Context();
		ctx.setVariable("shPostTypeAttr", shPostTypeAttr);
		ctx.setVariable("shProduct", shProduct);
		ctx.setVariable("paymentTypes", paymentTypes);
		return templateEngine.process("widget/payment/payment-widget", ctx);
	}

	@Override
	public boolean validateForm(HttpServletRequest request, ShPostTypeAttr shPostTypeAttr) {

		/*
		 * ShEcomPaymentTypeDefinition shEcomPaymentTypeDefinition =
		 * shEcomPaymentTypeDefinitionRepository
		 * .findByName(ShSystemEcomPaymentTypeDefinition.PAYMENTSLIP); ShEcomOrder
		 * shEcomOrder = new ShEcomOrder(); shEcomOrder.setName("Client");
		 * shEcomOrder.setDescription("Pagamento"); shEcomOrder.setProduct("Product");
		 * shEcomOrder.setValue(25.00);
		 * shEcomOrder.setShEcomPaymentType(shEcomPaymentType);
		 * 
		 * shEcomOrderRepository.save(shEcomOrder);
		 */

		return true;
	}

	@Override
	public void postRender(ShPost shPost, ShSitesContextURL shSitesContextURL) throws IOException {
		shPaymentSlip.payment(shPost, shSitesContextURL);
	}
}
