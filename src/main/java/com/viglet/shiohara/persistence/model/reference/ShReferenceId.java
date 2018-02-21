package com.viglet.shiohara.persistence.model.reference;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;

@Embeddable
public class ShReferenceId implements Serializable {

	private static final long serialVersionUID = 1L;
	private UUID fromId;
	private UUID toId;
	
	public UUID getFromId() {
		return fromId;
	}

	public void setFromId(UUID fromId) {
		this.fromId = fromId;
	}

	public UUID getToId() {
		return toId;
	}

	public void setToId(UUID toId) {
		this.toId = toId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fromId == null) ? 0 : fromId.hashCode());
		result = prime * result + ((toId == null) ? 0 : toId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShReferenceId other = (ShReferenceId) obj;
		if (fromId == null) {
			if (other.fromId != null)
				return false;
		} else if (!fromId.equals(other.fromId))
			return false;
		if (toId == null) {
			if (other.toId != null)
				return false;
		} else if (!toId.equals(other.toId))
			return false;
		return true;
	}
	
	
}
