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
package com.viglet.shio.provider.exchange.otcs.bean.result;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Alexandre Oliveira
 * 
 * @since 0.3.6
 */
public class ShOTCSRMIConsDataBean {

	@JsonProperty("class_id")
	private int classId;

	private int official;

	@JsonProperty("show_classify")
	private boolean showClassify;

	@JsonProperty("show_hold")
	private boolean showHold;

	@JsonProperty("show_hold_tab")
	private boolean showHoldTab;

	@JsonProperty("show_official")
	private boolean showOfficial;

	@JsonProperty("show_xref_tab")
	private boolean showXrefTab;

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public int getOfficial() {
		return official;
	}

	public void setOfficial(int official) {
		this.official = official;
	}

	public boolean isShowClassify() {
		return showClassify;
	}

	public void setShowClassify(boolean showClassify) {
		this.showClassify = showClassify;
	}

	public boolean isShowHold() {
		return showHold;
	}

	public void setShowHold(boolean showHold) {
		this.showHold = showHold;
	}

	public boolean isShowHoldTab() {
		return showHoldTab;
	}

	public void setShowHoldTab(boolean showHoldTab) {
		this.showHoldTab = showHoldTab;
	}

	public boolean isShowOfficial() {
		return showOfficial;
	}

	public void setShowOfficial(boolean showOfficial) {
		this.showOfficial = showOfficial;
	}

	public boolean isShowXrefTab() {
		return showXrefTab;
	}

	public void setShowXrefTab(boolean showXrefTab) {
		this.showXrefTab = showXrefTab;
	}

}
