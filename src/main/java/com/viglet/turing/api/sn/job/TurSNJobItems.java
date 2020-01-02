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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alexandre Oliveira
 */
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
