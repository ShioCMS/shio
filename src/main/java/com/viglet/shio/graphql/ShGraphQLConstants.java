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
package com.viglet.shio.graphql;

import static graphql.schema.GraphQLEnumType.newEnum;
import static graphql.schema.GraphqlTypeComparatorRegistry.BY_NAME_REGISTRY;

import graphql.schema.GraphQLEnumType;

/**
 * GraphQL Constants.
 *
 * @author Alexandre Oliveira
 * @since 0.3.7
 */
public final class ShGraphQLConstants {

	private ShGraphQLConstants() {
        // restrict instantiation
	}
	
	public final static String QUERY_TYPE = "Query";

	public final static String SEARCH = "_search";
	public final static String AND = "AND";
	public final static String OR = "OR";
	public final static String NOT = "NOT";
	public final static String ID = "id";
	public final static String TITLE = "_title";
	public final static String DESCRIPTION = "_description";
	public final static String FURL = "_furl";
	public final static String MODIFIER = "_modifier";
	public final static String PUBLISHER = "_publisher";
	public final static String FOLDER = "_folder";
	public final static String SITE = "_site";
	public final static String CREATED_AT = "_createdAt";
	public final static String UPDATED_AT = "_updatedAt";
	public final static String PUBLISHED_AT = "_publishedAt";

	public final static String CONDITION_EQUAL = "equal";
	public final static String CONDITION_NOT = "not";
	public final static String CONDITION_IN = "in";
	public final static String CONDITION_NOT_IN = "not_in";
	public final static String CONDITION_CONTAINS = "contains";
	public final static String CONDITION_NOT_CONTAINS = "not_contains";
	public final static String CONDITION_STARTS_WITH = "starts_with";
	public final static String CONDITION_NOT_STARTS_WITH = "not_starts_with";
	public final static String CONDITION_ENDS_WITH = "ends_with";
	public final static String CONDITION_NOT_ENDS_WITH = "not_ends_with";

	public final static String CONDITION_LT = "lt";
	public final static String CONDITION_LTE = "lte";
	public final static String CONDITION_GT = "gt";
	public final static String CONDITION_GTE = "gte";

	public final static String STAGE_ARG = "stage";
	public final static String STAGE_ARG_TITLE = "Stage";
	public final static String LOCALES_ARG = "locales";
	public final static String LOCALES_ARG_TITLE = "Locale";
	public final static String SITES_ARG = "sites";
	public final static String SITES_ARG_TITLE = "Site";
	public final static String WHERE_ARG = "where";

	public final static String WHERE_UNIQUE_INPUT = "WhereUniqueInput";
	public final static String WHERE_INPUT = "WhereInput";
	public final static String CONDITION_SEPARATOR = "_";

	public final static String FIELD_TYPE_GRAPHQL_ID = "GraphQLID";
	public final static String FIELD_TYPE_GRAPHQL_STRING = "GraphQLString";
	public final static String FIELD_TYPE_GRAPHQL_DATE_TIME = "DateTime";
	
	public final static GraphQLEnumType stageEnum = newEnum().name(STAGE_ARG_TITLE).description("Stage system enumeration")
			.value("PUBLISHED", 10, "System Published Stage.").value("DRAFT", 20, "System Draft Stage")
			.comparatorRegistry(BY_NAME_REGISTRY).build();

	public final static GraphQLEnumType localeEnum = newEnum().name(LOCALES_ARG_TITLE).description("Locale system enumeration")
			.value("en", "EN", "System Locale.").comparatorRegistry(BY_NAME_REGISTRY).build();
	
	public static GraphQLEnumType siteEnum;

}
