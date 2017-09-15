package com.viglet.shiohara.listener.onstartup.system;

import com.viglet.shiohara.persistence.model.system.ShConfigVar;
import com.viglet.shiohara.persistence.service.system.ShConfigVarService;

public class ShConfigVarOnStartup {

	public static void createDefaultRows() {

		final String FIRST_TIME = "FIRST_TIME";
		ShConfigVarService shConfigVarService = new ShConfigVarService();
		ShConfigVar shConfigVar = new ShConfigVar();

		if (shConfigVarService.get(FIRST_TIME) == null) {

			shConfigVar.setId(FIRST_TIME);
			shConfigVar.setPath("/system");
			shConfigVar.setValue("true");
			shConfigVarService.save(shConfigVar);
		}
	}

}