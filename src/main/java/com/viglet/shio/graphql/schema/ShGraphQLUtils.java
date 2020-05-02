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
package com.viglet.shio.graphql.schema;

import org.springframework.stereotype.Component;

import com.google.common.base.CaseFormat;

/**
 * GraphQL Utils.
 *
 * @author Alexandre Oliveira
 * @since 0.3.7
 */
@Component
public class ShGraphQLUtils {

	public String normalizedField(String object) {
		String objectName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,
				object.toLowerCase().replaceAll("-", "_"));
		return objectName;

	}

	public String normalizedPostType(String object) {
		char c[] = object.replaceAll("-", "_").toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		return new String(c);

	}

	

}
