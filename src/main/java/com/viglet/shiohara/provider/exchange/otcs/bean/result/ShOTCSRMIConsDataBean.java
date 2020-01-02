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
package com.viglet.shiohara.provider.exchange.otcs.bean.result;

/**
 * @author Alexandre Oliveira
 * 
 * @since 0.3.6
 */
public class ShOTCSRMIConsDataBean {

	private int class_id;
	
	private int official;
	
	private boolean show_classify;
	
	private boolean show_hold;
	
	private boolean show_hold_tab;
	
	private boolean show_official;
	
	private boolean show_xref_tab;

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}

	public int getOfficial() {
		return official;
	}

	public void setOfficial(int official) {
		this.official = official;
	}

	public boolean isShow_classify() {
		return show_classify;
	}

	public void setShow_classify(boolean show_classify) {
		this.show_classify = show_classify;
	}

	public boolean isShow_hold() {
		return show_hold;
	}

	public void setShow_hold(boolean show_hold) {
		this.show_hold = show_hold;
	}

	public boolean isShow_hold_tab() {
		return show_hold_tab;
	}

	public void setShow_hold_tab(boolean show_hold_tab) {
		this.show_hold_tab = show_hold_tab;
	}

	public boolean isShow_official() {
		return show_official;
	}

	public void setShow_official(boolean show_official) {
		this.show_official = show_official;
	}

	public boolean isShow_xref_tab() {
		return show_xref_tab;
	}

	public void setShow_xref_tab(boolean show_xref_tab) {
		this.show_xref_tab = show_xref_tab;
	}
	
	
}
