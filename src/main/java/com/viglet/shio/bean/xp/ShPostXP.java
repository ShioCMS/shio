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
package com.viglet.shio.bean.xp;

import com.viglet.shio.persistence.model.post.ShPost;

/**
 * @author Alexandre Oliveira
 */
public class ShPostXP {

	private ShPost shPost;
	
	private boolean allowPublish;

	public ShPost getShPost() {
		return shPost;
	}

	public void setShPost(ShPost shPost) {
		this.shPost = shPost;
	}

	public boolean isAllowPublish() {
		return allowPublish;
	}

	public void setAllowPublish(boolean allowPublish) {
		this.allowPublish = allowPublish;
	} 
	
	
}
