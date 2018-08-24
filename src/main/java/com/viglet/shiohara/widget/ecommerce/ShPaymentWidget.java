package com.viglet.shiohara.widget.ecommerce;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.viglet.shiohara.ecommerce.payment.ShPaymentSlip;
import com.viglet.shiohara.persistence.model.ecommerce.ShEcomPaymentTypeDefinition;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.repository.ecommerce.ShEcomPaymentTypeDefinitionRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.widget.ShWidgetImplementation;

@Component
public class ShPaymentWidget implements ShWidgetImplementation {
	@Autowired
	private SpringTemplateEngine templateEngine;
	@Autowired
	private ShPaymentSlip shPaymentSlip;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShEcomPaymentTypeDefinitionRepository shEcomPaymentTypeDefinitionRepository;

	@Override
	public String render(ShPostTypeAttr shPostTypeAttr) {
		JSONObject settings = new JSONObject(shPostTypeAttr.getWidgetSettings());
		JSONArray paymentTypesJSON = settings.getJSONArray("paymentTypes");
		Set<JSONObject> paymentTypes = new HashSet<JSONObject>();

		for (int i = 0; i < paymentTypesJSON.length(); i++) {
			JSONObject paymentType = paymentTypesJSON.getJSONObject(i);
			paymentTypes.add(paymentType);
			String paymentTypeId = paymentType.getString("id");
			ShPost shPaymentTypePost = shPostRepository.findById(paymentTypeId).get();
			Map<String, ShPostAttr> shPaymentTypePostMap = shPostUtils.postToMap(shPaymentTypePost);
			
			ShPostAttr shPaymentTypeDefinitionPostAttr = shPaymentTypePostMap.get("PAYMENT_TYPE_DEFINITION");
			JSONObject ptdJSON = new JSONObject(shPaymentTypeDefinitionPostAttr.getStrValue());
			
			ShEcomPaymentTypeDefinition shEcomPaymentTypeDefinition = shEcomPaymentTypeDefinitionRepository
					.findById(ptdJSON.getString("id")).get();
			paymentType.put("formPath", shEcomPaymentTypeDefinition.getFormPath());
		}

		final Context ctx = new Context();
		ctx.setVariable("shPostTypeAttr", shPostTypeAttr);
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

	public void postRender(ShPost shPost, HttpServletRequest request, HttpServletResponse response) throws IOException {
		shPaymentSlip.payment(shPost, response);
	}
}
