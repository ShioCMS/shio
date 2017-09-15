package com.viglet.shiohara.listener.onstartup.system;

import com.viglet.shiohara.persistence.model.system.ShLocale;
import com.viglet.shiohara.persistence.service.system.ShLocaleService;

public class ShLocaleOnStartup {

	public static void createDefaultRows() {

		ShLocaleService shLocaleService = new ShLocaleService();

		if (shLocaleService.listAll().isEmpty()) {
			
			shLocaleService.save(new ShLocale("ar", "العربية - Arabic", "العربية - Árabe"));
			shLocaleService.save(new ShLocale("bn", "বাংলা - Bengali", "বাংলা - Bengali"));
			shLocaleService.save(new ShLocale("ca", "Català - Catalan (beta)", "Català - Catalão (beta)"));
			shLocaleService.save(new ShLocale("cs", "Čeština - Czech", "Čeština - Tcheco"));
			shLocaleService.save(new ShLocale("da", "Dansk - Danish", "Dansk - Dinamarquês"));
			shLocaleService.save(new ShLocale("de", "Deutsch - German", "Deutsch - Alemão"));
			shLocaleService.save(new ShLocale("el", "Ελληνικά - Greek", "Ελληνικά - Grego"));
			shLocaleService.save(new ShLocale("en", "English", "English - Inglês"));
			shLocaleService
					.save(new ShLocale("en-gb", "English UK - British English", "English UK - Inglês britânico"));
			shLocaleService.save(new ShLocale("es", "Español - Spanish", "Español - Espanhol"));
			shLocaleService.save(new ShLocale("eu", "Euskara - Basque", "Euskara - Basco"));
			shLocaleService.save(new ShLocale("fa", "فارسی - Persian", "فارسی - Persa"));
			shLocaleService.save(new ShLocale("fi", "Suomi - Finnish", "Suomi - Finlandês"));
			shLocaleService.save(new ShLocale("fil", "Filipino", "Filipino"));
			shLocaleService.save(new ShLocale("fr", "Français - French", "Français - Francês"));
			shLocaleService.save(new ShLocale("ga", "Gaeilge - Irish", "Gaeilge - Irlandês"));
			shLocaleService.save(new ShLocale("gl", "Galego - Galician", "Galego"));
			shLocaleService.save(new ShLocale("gu", "ગુજરાતી - Gujarati", "ગુજરાતી - Guzerate"));
			shLocaleService.save(new ShLocale("he", "עִבְרִית - Hebrew", "עִבְרִית - Hebraico"));
			shLocaleService.save(new ShLocale("hi", "िन्दी - Hindi", "िन्दी - Híndi"));
			shLocaleService.save(new ShLocale("hu", "Magyar - Hungarian", "Magyar - Húngaro"));
			shLocaleService.save(new ShLocale("id", "Bahasa Indonesia - Indonesian", "Bahasa Indonesia - Indonésio"));
			shLocaleService.save(new ShLocale("it", "Italiano - Italian", "Italiano"));
			shLocaleService.save(new ShLocale("ja", "日本語 - Japanese", "日本語 - Japonês"));
			shLocaleService.save(new ShLocale("kn", "ಕನ್ನಡ - Kannada", "ಕನ್ನಡ - Canarês"));
			shLocaleService.save(new ShLocale("ko", "한국어- Korean", "한국어- Coreano"));
			shLocaleService.save(new ShLocale("mr", "मराठी - Marathi", "मराठी - Marata"));
			shLocaleService.save(new ShLocale("msa", "Bahasa Melayu - Malay", "Bahasa Melayu - Malaio"));
			shLocaleService.save(new ShLocale("nl", "Nederlands - Dutch", "Nederlands - Holandês"));
			shLocaleService.save(new ShLocale("no", "Norsk - Norwegian", "Norsk - Norueguês"));
			shLocaleService.save(new ShLocale("pl", "Polski - Polish", "Polski - Polonês"));
			shLocaleService.save(new ShLocale("pt-pt", "Português - Portuguese (Portugal)", "Português (Portugal)"));
			shLocaleService.save(new ShLocale("pt-br", "Português - Portuguese (Brazil)", "Português (Brasil)"));
			shLocaleService.save(new ShLocale("ro", "Română - Romanian", "Română - Romeno"));
			shLocaleService.save(new ShLocale("ru", "Русский - Russian", "Русский - Russo"));
			shLocaleService.save(new ShLocale("sv", "Svenska - Swedish", "Svenska - Sueco"));
			shLocaleService.save(new ShLocale("ta", "தமிழ் - Tamil", "தமிழ் - Tâmil"));
			shLocaleService.save(new ShLocale("th", "ภาษาไทย - Thai", "ภาษาไทย - Tailandês"));
			shLocaleService.save(new ShLocale("tr", "Türkçe - Shkish", "Türkçe - Shco"));
			shLocaleService
					.save(new ShLocale("uk", "Українська мова - Ukrainian", "Українська мова - Ucraniano"));
			shLocaleService.save(new ShLocale("ur", "اردو - Urdu", "اردو - Urdu"));
			shLocaleService.save(new ShLocale("vi", "Tiếng Việt - Vietnamese", "Tiếng Việt - Vietnamita"));
			shLocaleService.save(new ShLocale("xx-lc", "LOLCATZ - Lolcat", "LOLCATZ - Lolcat"));
			shLocaleService
					.save(new ShLocale("zh-cn", "简体中文 - Simplified Chinese", "简体中文 - Chinês simplificado"));
			shLocaleService
			.save(new ShLocale("zh-tw", "简体中文 - Traditional Chinese", "繁體中文 - Chinês tradicional"));
		}
	}

}