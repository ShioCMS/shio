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
package com.viglet.turing.api.sn.job;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Alexandre Oliveira
 */
public class TurSNJobItem implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private TurSNJobAction turSNJobAction;
	
	private Map<String, Object> attributes;

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public TurSNJobAction getTurSNJobAction() {
		return turSNJobAction;
	}

	public void setTurSNJobAction(TurSNJobAction turSNJobAction) {
		this.turSNJobAction = turSNJobAction;
	}


}
