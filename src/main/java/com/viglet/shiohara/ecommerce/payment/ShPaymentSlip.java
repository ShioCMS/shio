/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.sites.ShSitesContextURL;
import com.viglet.shiohara.utils.stage.ShStagePostUtils;
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
	private ShStagePostUtils shStagePostUtils;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShObjectRepository shObjectRepository;
	
	public void payment(ShPost shPost, ShSitesContextURL shSitesContextURL) throws IOException {

		Set<ShPostTypeAttr> shPostTypeAttrs = shPost.getShPostType().getShPostTypeAttrs();
		String paymentTypeId = null;
		JSONObject ptdSettings = null;
		JSONObject product = null;
		JSONObject payer = null;
		ShPost shProductPost = null;
		for (ShPostTypeAttr shPostTypeAttr : shPostTypeAttrs) {
			if (shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.PAYMENT)) {
				JSONObject settings = new JSONObject(shPostTypeAttr.getWidgetSettings());
				product = settings.getJSONObject("product");
				ShObject shObject = shObjectRepository.findById(shSitesContextURL.getInfo().getObjectId()).orElse(null);
				if (shObject instanceof ShPost) {
					shProductPost = (ShPost) shObject;
				} else {
					shProductPost = shPostRepository.findById(product.getString("post")).orElse(null);
				}
				payer = settings.getJSONObject("payer");
				JSONArray paymentTypes = settings.getJSONArray("paymentTypes");
				for (int i = 0; i < paymentTypes.length(); i++) {
					JSONObject paymentType = paymentTypes.getJSONObject(i);
					paymentTypeId = paymentType.getString("id");
				}
			}
		}

		if (paymentTypeId != null) {
			ShPost shPaymentTypePost = shPostRepository.findById(paymentTypeId).orElse(null);
			Map<String, ShPostAttr> shPaymentTypePostMap = shStagePostUtils.postToMap(shPaymentTypePost);
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
			Map<String, ShPostAttr> shPostMap = shStagePostUtils.postToMap(shPost);
			Map<String, ShPostAttr> shProductPostMap = shStagePostUtils.postToMap(shProductPost);
			double paymentSlipValue = Double
					.parseDouble(shProductPostMap.get(product.getString("value")).getStrValue());

			Pagador pagador = Pagador.novoPagador().comNome(shPostMap.get(payer.getString("name")).getStrValue())
					.comDocumento(shPostMap.get(payer.getString("documentId")).getStrValue());

			Banco banco = new Itau();
			String[] instructions = ptdSettings.getString("instructions").split("\n");
			Boleto primeraParcela = Boleto.novoBoleto().comBanco(banco).comDatas(datas).comBeneficiario(beneficiario)
					.comPagador(pagador).comValorBoleto(paymentSlipValue)
					.comNumeroDoDocumento(ptdSettings.getString("documentNumber")).comInstrucoes(instructions)
					.comLocaisDePagamento(ptdSettings.getString("local"));

		/*	Boleto segundaParcela = Boleto.novoBoleto().comBanco(banco).comDatas(datas2nd).comBeneficiario(beneficiario)
					.comPagador(pagador).comValorBoleto(paymentSlipValue)
					.comNumeroDoDocumento(ptdSettings.getString("documentNumber")).comInstrucoes(instructions)
					.comLocaisDePagamento(ptdSettings.getString("local"));*/

			Boleto[] boletos = { primeraParcela };
			GeradorDeBoleto gerador = new GeradorDeBoleto(boletos);

			byte[] bPDF = gerador.geraPDF();

			MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
			HttpServletResponse response = shSitesContextURL.getResponse();
			response.setContentType(mimetypesFileTypeMap.getContentType("PaymentSlip.pdf"));
			response.setHeader("Content-disposition", "attachment; filename=PaymentSlip.pdf");
			response.getOutputStream().write(bPDF);
		}
	}
}
