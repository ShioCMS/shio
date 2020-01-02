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
package com.viglet.shiohara.widget.ecommerce;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.viglet.shiohara.ecommerce.payment.ShPaymentSlip;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.sites.ShSitesContextURL;
import com.viglet.shiohara.widget.ShWidgetImplementation;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShPaymentTypeDefinitionWidget implements ShWidgetImplementation {
	@Autowired
	private SpringTemplateEngine templateEngine;
	@Autowired
	private ShPaymentSlip shPaymentSlip;

	@Override
	public String render(ShPostTypeAttr shPostTypeAttr, ShObject shObject) {
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

	public void postRender(ShPost shPost, ShSitesContextURL shSitesContextURL) throws IOException {
		shPaymentSlip.payment(shPost, shSitesContextURL);
	}
}
