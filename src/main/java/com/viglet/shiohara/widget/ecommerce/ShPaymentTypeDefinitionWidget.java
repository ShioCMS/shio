package com.viglet.shiohara.widget.ecommerce;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.viglet.shiohara.ecommerce.payment.ShPaymentSlip;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.widget.ShWidgetImplementation;


@Component
public class ShPaymentTypeDefinitionWidget implements ShWidgetImplementation {
	@Autowired
	private SpringTemplateEngine templateEngine;
	@Autowired
	private ShPaymentSlip shPaymentSlip;

	@Override
	public String render(ShPostTypeAttr shPostTypeAttr) {
		final Context ctx = new Context();
		ctx.setVariable("shPostTypeAttr", shPostTypeAttr);
		return templateEngine.process("widget/payment/payment-widget", ctx);
	}

	@Override
	public boolean validateForm(HttpServletRequest request, ShPostTypeAttr shPostTypeAttr) {

		/*ShEcomPaymentTypeDefinition shEcomPaymentTypeDefinition = shEcomPaymentTypeDefinitionRepository
				.findByName(ShSystemEcomPaymentTypeDefinition.PAYMENTSLIP);
		ShEcomOrder shEcomOrder = new ShEcomOrder();
		shEcomOrder.setName("Client");
		shEcomOrder.setDescription("Pagamento");
		shEcomOrder.setProduct("Product");
		shEcomOrder.setValue(25.00);
		shEcomOrder.setShEcomPaymentType(shEcomPaymentType);

		shEcomOrderRepository.save(shEcomOrder);*/

		return true;
	}

	public void postRender(ShPost shPost, HttpServletRequest request, HttpServletResponse response) throws IOException {
		shPaymentSlip.payment(shPost, response);
	}
}
