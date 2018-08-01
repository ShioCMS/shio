package com.viglet.shiohara.onstartup.ecommerce;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.ecommerce.ShEcomPaymentType;
import com.viglet.shiohara.persistence.repository.ecommerce.ShEcomPaymentTypeRepository;

@Component
public class ShEcomPaymentTypeOnStartup {
	@Autowired
	private ShEcomPaymentTypeRepository shEcomPaymentTypeRepository;

	public void createDefaultRows() {

		if (shEcomPaymentTypeRepository.findAll().isEmpty()) {
			ShEcomPaymentType shEcomPaymentType = new ShEcomPaymentType();
			shEcomPaymentType.setName("Rede Card");
			shEcomPaymentType.setDate(new Date());
			shEcomPaymentType.setDescription("Rede Card Payment");
			shEcomPaymentType.setClassName("com.viglet.shiohara.ecommece.payment.ShRedeCardPayment");
			shEcomPaymentType.setSettingPath("template/ecommerce/payment/rede-card/setting/rede-card-setting.html");
			shEcomPaymentTypeRepository.save(shEcomPaymentType);

			shEcomPaymentType = new ShEcomPaymentType();
			shEcomPaymentType.setName("Boletos");
			shEcomPaymentType.setDate(new Date());
			shEcomPaymentType.setDescription("Boletos Payment");
			shEcomPaymentType.setClassName("com.viglet.shiohara.ecommece.payment.ShBoletoPayment");
			shEcomPaymentType.setSettingPath("template/ecommerce/payment/boleto/setting/boleto-setting.html");
			shEcomPaymentTypeRepository.save(shEcomPaymentType);

			shEcomPaymentType = new ShEcomPaymentType();
			shEcomPaymentType.setName("Shopline");
			shEcomPaymentType.setDate(new Date());
			shEcomPaymentType.setDescription("Shopline Payment");
			shEcomPaymentType.setClassName("com.viglet.shiohara.ecommece.payment.ShShoplinePayment");
			shEcomPaymentType.setSettingPath("template/ecommerce/payment/shopline/setting/shopline-setting.html");
			shEcomPaymentTypeRepository.save(shEcomPaymentType);

		}
	}

}
