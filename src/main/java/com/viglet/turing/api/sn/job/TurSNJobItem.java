package com.viglet.turing.api.sn.job;

import java.io.Serializable;
import java.util.Map;

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
