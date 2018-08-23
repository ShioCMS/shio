package com.viglet.shiohara.ecommerce.payment;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.widget.ShSystemWidget;

import br.com.caelum.stella.boleto.Banco;
import br.com.caelum.stella.boleto.Beneficiario;
import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.Datas;
import br.com.caelum.stella.boleto.Endereco;
import br.com.caelum.stella.boleto.Pagador;
import br.com.caelum.stella.boleto.bancos.Itau;
import br.com.caelum.stella.boleto.transformer.GeradorDeBoleto;

@Component
public class ShPaymentSlip {
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShPostRepository shPostRepository;

	public void payment(ShPost shPost, HttpServletResponse response) throws IOException {
		Set<ShPostTypeAttr> shPostTypeAttrs = shPost.getShPostType().getShPostTypeAttrs();
		String paymentTypeId = null;
		JSONObject ptdSettings = null;
		JSONObject payer = null;
		for (ShPostTypeAttr shPostTypeAttr : shPostTypeAttrs) {
			if (shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.PAYMENT)) {
				JSONObject settings = new JSONObject(shPostTypeAttr.getWidgetSettings());
				payer = settings.getJSONObject("payer");
				JSONArray paymentTypes = settings.getJSONArray("paymentTypes");
				for (int i = 0; i < paymentTypes.length(); i++) {
					JSONObject paymentType = paymentTypes.getJSONObject(i);
					paymentTypeId = paymentType.getString("id");
				}
			}
		}

		if (paymentTypeId != null) {
			ShPost shPaymentTypePost = shPostRepository.findById(paymentTypeId).get();
			Map<String, ShPostAttr> shPaymentTypePostMap = shPostUtils.postToMap(shPaymentTypePost);
			ShPostAttr shPaymentTypeDefinitionPostAttr = shPaymentTypePostMap.get("PAYMENT_TYPE_DEFINITION");
			ptdSettings = new JSONObject(shPaymentTypeDefinitionPostAttr.getStrValue()).getJSONObject("widgetSettings");
		}
		if (ptdSettings != null) {

			Calendar today = Calendar.getInstance();
			Calendar firstPayment = Calendar.getInstance();
			firstPayment.add(Calendar.DATE, 7);

			Calendar secondPayment = Calendar.getInstance();
			secondPayment.add(Calendar.DATE, 7);
			secondPayment.add(Calendar.MONTH, 1);

			Datas datas = Datas.novasDatas().comDocumento(today).comProcessamento(today).comVencimento(firstPayment);

			Datas datas2nd = Datas.novasDatas().comDocumento(today).comProcessamento(today)
					.comVencimento(secondPayment);

			Endereco enderecoBeneficiario = Endereco.novoEndereco()
					.comLogradouro(ptdSettings.getString("address") + ", " + ptdSettings.getString("addressNumber"))
					.comBairro(ptdSettings.getString("district")).comCep(ptdSettings.getString("zipCode"))
					.comCidade(ptdSettings.getString("city")).comUf(ptdSettings.getString("state"));

			// Quem emite o boleto
			Beneficiario beneficiario = Beneficiario.novoBeneficiario()
					.comDocumento(ptdSettings.getString("documentId"))
					.comNomeBeneficiario(ptdSettings.getString("name")).comAgencia(ptdSettings.getString("agency"))
					.comDigitoAgencia(ptdSettings.getString("agencyCode"))
					.comCodigoBeneficiario(ptdSettings.getString("recepientCode"))
					.comDigitoCodigoBeneficiario(ptdSettings.getString("recepientCodeDigit"))
					.comNumeroConvenio(ptdSettings.getString("agreementNumber"))
					.comCarteira(ptdSettings.getString("wallet")).comEndereco(enderecoBeneficiario)
					.comNossoNumero(ptdSettings.getString("ourNumber"))
					.comDigitoNossoNumero(ptdSettings.getString("ourNumberDigit"));

			// Quem paga o boleto
			Map<String, ShPostAttr> shPostMap = shPostUtils.postToMap(shPost);

			Pagador pagador = Pagador.novoPagador().comNome(shPostMap.get(payer.getString("name")).getStrValue())
					.comDocumento(shPostMap.get(payer.getString("documentId")).getStrValue());

			Banco banco = new Itau();
			String[] instructions = ptdSettings.getString("instructions").split("\n");
			Boleto primeraParcela = Boleto.novoBoleto().comBanco(banco).comDatas(datas).comBeneficiario(beneficiario)
					.comPagador(pagador).comValorBoleto("12.50")
					.comNumeroDoDocumento(ptdSettings.getString("documentNumber")).comInstrucoes(instructions)
					.comLocaisDePagamento(ptdSettings.getString("local"));

			Boleto segundaParcela = Boleto.novoBoleto().comBanco(banco).comDatas(datas2nd).comBeneficiario(beneficiario)
					.comPagador(pagador).comValorBoleto("12.50")
					.comNumeroDoDocumento(ptdSettings.getString("documentNumber")).comInstrucoes(instructions)
					.comLocaisDePagamento(ptdSettings.getString("local"));

			Boleto[] boletos = { primeraParcela, segundaParcela };
			GeradorDeBoleto gerador = new GeradorDeBoleto(boletos);

			byte[] bPDF = gerador.geraPDF();

			MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
			response.setContentType(mimetypesFileTypeMap.getContentType("PaymentSlip.pdf"));
			response.setHeader("Content-disposition", "attachment; filename=PaymentSlip.pdf");
			response.getOutputStream().write(bPDF);
		}
	}
}
