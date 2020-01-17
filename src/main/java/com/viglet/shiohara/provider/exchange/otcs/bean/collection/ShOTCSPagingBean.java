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
package com.viglet.shiohara.provider.exchange.otcs.bean.collection;

import com.viglet.shiohara.provider.exchange.otcs.bean.link.ShOTCSLinksBean;

/**
 * @author Alexandre Oliveira
 * 
 * @since 0.3.6
 */
public class ShOTCSPagingBean {

	private int limit;
	
	private ShOTCSLinksBean links;
	
	private int page;
	
	private int page_total;
	
	private int range_max;
	
	private int range_min;
	
	private int total_count;

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public ShOTCSLinksBean getLinks() {
		return links;
	}

	public void setLinks(ShOTCSLinksBean links) {
		this.links = links;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPage_total() {
		return page_total;
	}

	public void setPage_total(int page_total) {
		this.page_total = page_total;
	}

	public int getRange_max() {
		return range_max;
	}

	public void setRange_max(int range_max) {
		this.range_max = range_max;
	}

	public int getRange_min() {
		return range_min;
	}

	public void setRange_min(int range_min) {
		this.range_min = range_min;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	
	
}
