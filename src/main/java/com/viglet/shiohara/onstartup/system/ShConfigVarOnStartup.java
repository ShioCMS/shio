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

package com.viglet.shiohara.onstartup.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.system.ShConfigVar;
import com.viglet.shiohara.persistence.repository.system.ShConfigVarRepository;

@Component
public class ShConfigVarOnStartup {

	@Autowired
	private ShConfigVarRepository shConfigVarRepository;

	public void createDefaultRows() {

		final String FIRST_TIME = "FIRST_TIME";

		ShConfigVar shConfigVar = new ShConfigVar();

		if (!shConfigVarRepository.findById(FIRST_TIME).isPresent()) {

			shConfigVar.setId(FIRST_TIME);
			shConfigVar.setPath("/system");
			shConfigVar.setValue("true");
			shConfigVarRepository.save(shConfigVar);

			shConfigVar = new ShConfigVar();
			shConfigVar.setId("EMAIL_HOST");
			shConfigVar.setPath("/email/host");
			shConfigVar.setValue("localhost");

			shConfigVarRepository.save(shConfigVar);
		}
	}

}
