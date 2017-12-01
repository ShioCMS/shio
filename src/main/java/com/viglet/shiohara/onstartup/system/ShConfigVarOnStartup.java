package com.viglet.shiohara.onstartup.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.system.ShConfigVar;
import com.viglet.shiohara.persistence.repository.system.ShConfigVarRepository;

@Component
public class ShConfigVarOnStartup {

	@Autowired
	ShConfigVarRepository shConfigVarRepository;

	public void createDefaultRows() {

		final String FIRST_TIME = "FIRST_TIME";

		ShConfigVar shConfigVar = new ShConfigVar();

		if (shConfigVarRepository.findById(FIRST_TIME) == null) {

			shConfigVar.setId(FIRST_TIME);
			shConfigVar.setPath("/system");
			shConfigVar.setValue("true");
			shConfigVarRepository.save(shConfigVar);
		}
	}

}