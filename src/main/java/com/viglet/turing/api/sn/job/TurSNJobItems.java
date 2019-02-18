package com.viglet.turing.api.sn.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TurSNJobItems implements Iterable<TurSNJobItem>, Serializable {
	private static final long serialVersionUID = 1L;
	private List<TurSNJobItem> turSNJobItems = new ArrayList<TurSNJobItem>();

	@Override
	public Iterator<TurSNJobItem> iterator() {
		return turSNJobItems.iterator();
	}

	public List<TurSNJobItem> getTuringDocuments() {
		return turSNJobItems;
	}

	public void setTuringDocuments(List<TurSNJobItem> turSNJobItems) {
		this.turSNJobItems = turSNJobItems;
	}

	public boolean add(TurSNJobItem turSNJobItem) {
		return turSNJobItems.add(turSNJobItem);
	}

	public boolean remove(TurSNJobItem turSNJobItem) {
		return turSNJobItems.remove(turSNJobItem);
	}
}
