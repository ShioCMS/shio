package com.viglet.shiohara.onstartup.widget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.widget.ShWidget;
import com.viglet.shiohara.persistence.repository.widget.ShWidgetRepository;
import com.viglet.shiohara.widget.ShSystemWidget;

@Component
public class ShWidgetOnStartup {

	@Autowired
	private ShWidgetRepository shWidgetRepository;

	public void createDefaultRows() {

		if (shWidgetRepository.findAll().isEmpty()) {

			ShWidget shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.HIDDEN);
			shWidget.setDescription("Hidden Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShHiddenWidget");
			shWidget.setImplementationCode("template/widget/hidden/hidden.html");
			shWidget.setSettingPath("template/widget/hidden/setting/hidden-setting.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);
			
			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.TEXT);
			shWidget.setDescription("Text Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShTextWidget");
			shWidget.setImplementationCode("template/widget/text.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);

			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.TEXT_AREA);
			shWidget.setDescription("Text Area Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShTextAreaWidget");
			shWidget.setImplementationCode("template/widget/textarea.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);

			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.FILE);
			shWidget.setDescription("File Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShFileWidget");
			shWidget.setImplementationCode("template/widget/file.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);

			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.ACE_JS);
			shWidget.setDescription("Ace Editor Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShAceEditorWidget");
			shWidget.setImplementationCode("template/widget/ace-editor-js.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);

			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.ACE_HTML);
			shWidget.setDescription("Ace Editor Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShAceEditorWidget");
			shWidget.setImplementationCode("template/widget/ace-editor-html.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);

			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.HTML_EDITOR);
			shWidget.setDescription("HTML Editor Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShHTMLWidget");
			shWidget.setImplementationCode("template/widget/html-editor.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);

			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.CONTENT_SELECT);
			shWidget.setDescription("Content Select Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShContentSelectWidget");
			shWidget.setImplementationCode("template/widget/content-select.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);

			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.RELATOR);
			shWidget.setDescription("Relator Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShRelatorWidget");
			shWidget.setImplementationCode("template/widget/relator/relator-list.html");
			shWidget.setSettingPath("template/widget/relator/setting/relator-setting.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);
			
			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.COMBO_BOX);
			shWidget.setDescription("Combo Box Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShComboBoxWidget");
			shWidget.setImplementationCode("template/widget/combo-box/combo-box.html");
			shWidget.setSettingPath("template/widget/combo-box/setting/combo-box-setting.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);
			
			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.RECAPTCHA);
			shWidget.setDescription("reCAPTCHA Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShReCaptchaWidget");
			shWidget.setImplementationCode("template/widget/recaptcha/recaptcha.html");
			shWidget.setSettingPath("template/widget/recaptcha/setting/recaptcha-setting.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);
			
			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.PAYMENT_DEFINITION);
			shWidget.setDescription("Payment Type Definition Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ecommerce.ShPaymentTypeDefinitionWidget");
			shWidget.setImplementationCode("template/widget/payment/type/definition/payment-type-definition.html");
			shWidget.setSettingPath("template/widget/payment/type/definition/setting/payment-type-definition-setting.html");
			shWidget.setType("JSON");

			shWidgetRepository.save(shWidget);
			
			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.PAYMENT);
			shWidget.setDescription("Payment Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ecommerce.ShPaymentWidget");
			shWidget.setImplementationCode("template/widget/payment/payment.html");
			shWidget.setSettingPath("template/widget/payment/setting/payment-setting.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);
		}

	}
}
