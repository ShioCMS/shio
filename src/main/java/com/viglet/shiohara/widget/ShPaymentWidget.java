package com.viglet.shiohara.widget;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.viglet.shiohara.onstartup.ecommerce.ShSystemEcomPaymentType;
import com.viglet.shiohara.persistence.model.ecommerce.ShEcomOrder;
import com.viglet.shiohara.persistence.model.ecommerce.ShEcomPaymentType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.repository.ecommerce.ShEcomOrderRepository;
import com.viglet.shiohara.persistence.repository.ecommerce.ShEcomPaymentTypeRepository;

@Component
public class ShPaymentWidget implements ShWidgetImplementation {
	@Autowired
	private SpringTemplateEngine templateEngine;
	@Autowired
	private ShEcomPaymentTypeRepository shEcomPaymentTypeRepository;
	@Autowired
	private ShEcomOrderRepository shEcomOrderRepository;
	
	public String render(ShPostTypeAttr shPostTypeAttr) {
		final Context ctx = new Context();
		ctx.setVariable("shPostTypeAttr", shPostTypeAttr);
		return templateEngine.process("widget/payment/payment-widget", ctx);
	}

	@Override
	public boolean validateForm(HttpServletRequest request, ShPostTypeAttr shPostTypeAttr) {
		
		ShEcomPaymentType shEcomPaymentType = shEcomPaymentTypeRepository.findByName(ShSystemEcomPaymentType.BOLETOS);		
		ShEcomOrder shEcomOrder = new ShEcomOrder();
		shEcomOrder.setName("Client");
		shEcomOrder.setDescription("Pagamento");
		shEcomOrder.setProduct("Product");
		shEcomOrder.setValue(25.00);
		shEcomOrder.setShEcomPaymentType(shEcomPaymentType);
		
		shEcomOrderRepository.save(shEcomOrder);
		return true;
	}
}
