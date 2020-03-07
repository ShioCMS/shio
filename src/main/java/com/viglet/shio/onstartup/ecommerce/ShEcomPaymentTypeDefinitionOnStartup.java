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
package com.viglet.shio.onstartup.ecommerce;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shio.persistence.model.ecommerce.ShEcomPaymentTypeDefinition;
import com.viglet.shio.persistence.repository.ecommerce.ShEcomPaymentTypeDefinitionRepository;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShEcomPaymentTypeDefinitionOnStartup {
	@Autowired
	private ShEcomPaymentTypeDefinitionRepository shEcomPaymentTypeDefinitionRepository;

	public void createDefaultRows() {

		if (shEcomPaymentTypeDefinitionRepository.findAll().isEmpty()) {
			ShEcomPaymentTypeDefinition shEcomPaymentTypeDefinition = new ShEcomPaymentTypeDefinition();
			shEcomPaymentTypeDefinition.setId("983707eb-c84d-4432-9a7c-72101ec2f3a2");
			shEcomPaymentTypeDefinition.setName(ShSystemEcomPaymentTypeDefinition.REDECARD);
			shEcomPaymentTypeDefinition.setDate(new Date());
			shEcomPaymentTypeDefinition.setDescription("Rede Card Payment");
			shEcomPaymentTypeDefinition.setClassName("com.viglet.shio.ecommerce.payment.ShRedeCardPayment");
			shEcomPaymentTypeDefinition
					.setSettingPath("template/ecommerce/payment/rede-card/setting/rede-card-setting.html");
			shEcomPaymentTypeDefinition.setFormPath("widget/payment/rede-card/rede-card-form");

			shEcomPaymentTypeDefinitionRepository.save(shEcomPaymentTypeDefinition);

			shEcomPaymentTypeDefinition = new ShEcomPaymentTypeDefinition();
			shEcomPaymentTypeDefinition.setId("9e3ce17e-7fe8-44a8-a9a0-a302202b6ead");
			shEcomPaymentTypeDefinition.setName(ShSystemEcomPaymentTypeDefinition.PAYMENTSLIP);
			shEcomPaymentTypeDefinition.setDate(new Date());
			shEcomPaymentTypeDefinition.setDescription("Payment Slip");
			shEcomPaymentTypeDefinition.setClassName("com.viglet.shio.ecommerce.payment.ShPaymentSlip");
			shEcomPaymentTypeDefinition
					.setSettingPath("template/ecommerce/payment/payment-slip/setting/payment-slip-setting.html");
			shEcomPaymentTypeDefinition.setFormPath("widget/payment/payment-slip/payment-slip-form");

			shEcomPaymentTypeDefinitionRepository.save(shEcomPaymentTypeDefinition);

			shEcomPaymentTypeDefinition = new ShEcomPaymentTypeDefinition();
			shEcomPaymentTypeDefinition.setId("a0a81e69-04f9-4fe6-a313-2c643e467a5e");
			shEcomPaymentTypeDefinition.setName(ShSystemEcomPaymentTypeDefinition.SHOPLINE);
			shEcomPaymentTypeDefinition.setDate(new Date());
			shEcomPaymentTypeDefinition.setDescription("Shopline Payment");
			shEcomPaymentTypeDefinition.setClassName("com.viglet.shio.ecommerce.payment.ShShoplinePayment");
			shEcomPaymentTypeDefinition
					.setSettingPath("template/ecommerce/payment/shopline/setting/shopline-setting.html");
			shEcomPaymentTypeDefinition.setFormPath("widget/payment/shopline/shopline-form");

			shEcomPaymentTypeDefinitionRepository.save(shEcomPaymentTypeDefinition);

		}
	}

}
