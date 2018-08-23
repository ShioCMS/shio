package com.viglet.shiohara.onstartup.ecommerce;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.ecommerce.ShEcomPaymentTypeDefinition;
import com.viglet.shiohara.persistence.repository.ecommerce.ShEcomPaymentTypeDefinitionRepository;

@Component
public class ShEcomPaymentTypeDefinitionOnStartup {
	@Autowired
	private ShEcomPaymentTypeDefinitionRepository shEcomPaymentTypeDefinitionRepository;

	public void createDefaultRows() {

		if (shEcomPaymentTypeDefinitionRepository.findAll().isEmpty()) {
			ShEcomPaymentTypeDefinition shEcomPaymentTypeDefinition = new ShEcomPaymentTypeDefinition();
			shEcomPaymentTypeDefinition.setName(ShSystemEcomPaymentTypeDefinition.REDECARD);
			shEcomPaymentTypeDefinition.setDate(new Date());
			shEcomPaymentTypeDefinition.setDescription("Rede Card Payment");
			shEcomPaymentTypeDefinition.setClassName("com.viglet.shiohara.ecommerce.payment.ShRedeCardPayment");
			shEcomPaymentTypeDefinition
					.setSettingPath("template/ecommerce/payment/rede-card/setting/rede-card-setting.html");
			shEcomPaymentTypeDefinitionRepository.save(shEcomPaymentTypeDefinition);

			shEcomPaymentTypeDefinition = new ShEcomPaymentTypeDefinition();
			shEcomPaymentTypeDefinition.setName(ShSystemEcomPaymentTypeDefinition.PAYMENTSLIP);
			shEcomPaymentTypeDefinition.setDate(new Date());
			shEcomPaymentTypeDefinition.setDescription("Payment Slip");
			shEcomPaymentTypeDefinition.setClassName("com.viglet.shiohara.ecommerce.payment.ShPaymentSlip");
			shEcomPaymentTypeDefinition
					.setSettingPath("template/ecommerce/payment/payment-slip/setting/payment-slip-setting.html");
			shEcomPaymentTypeDefinitionRepository.save(shEcomPaymentTypeDefinition);

			shEcomPaymentTypeDefinition = new ShEcomPaymentTypeDefinition();
			shEcomPaymentTypeDefinition.setName(ShSystemEcomPaymentTypeDefinition.SHOPLINE);
			shEcomPaymentTypeDefinition.setDate(new Date());
			shEcomPaymentTypeDefinition.setDescription("Shopline Payment");
			shEcomPaymentTypeDefinition.setClassName("com.viglet.shiohara.ecommerce.payment.ShShoplinePayment");
			shEcomPaymentTypeDefinition
					.setSettingPath("template/ecommerce/payment/shopline/setting/shopline-setting.html");
			shEcomPaymentTypeDefinitionRepository.save(shEcomPaymentTypeDefinition);

		}
	}

}
