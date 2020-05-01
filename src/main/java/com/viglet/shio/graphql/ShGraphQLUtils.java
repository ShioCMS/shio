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

import static graphql.Scalars.GraphQLID;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphqlTypeComparatorRegistry.BY_NAME_REGISTRY;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.CaseFormat;
import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shio.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shio.persistence.service.post.ShPostAttrService;
import com.viglet.shio.utils.ShPostUtils;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLObjectType.Builder;

/**
 * GraphQL Utils.
 *
 * @author Alexandre Oliveira
 * @since 0.3.7
 */
@Component
public class ShGraphQLUtils {

	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShPostAttrService shPostAttrService;

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

	public void createInputObjectField(GraphQLInputObjectType.Builder builder, String name, GraphQLInputType type,
			String description) {
		builder.field(newInputObjectField().name(name).description(description).type(type));
	}

	public void createInputObjectField(GraphQLInputObjectType.Builder builder, String name, String type,
			String description) {

		GraphQLScalarType scalarType = null;

		if (type.equals(ShGraphQLConstants.FIELD_TYPE_GRAPHQL_DATE_TIME)) {
			scalarType = ExtendedScalars.DateTime;
			this.createInputObjectFieldCondition(builder, name, null, scalarType, description);
			this.createInputObjectFieldCondition(builder, name, ShGraphQLConstants.CONDITION_NOT, scalarType,
					"All values that are not equal to given value.");
			this.createInputObjectFieldCondition(builder, name, ShGraphQLConstants.CONDITION_IN, scalarType,
					"All values that are not contained in given list.");
			this.createInputObjectFieldCondition(builder, name, ShGraphQLConstants.CONDITION_LT, scalarType,
					"All values less than the given value.");
			this.createInputObjectFieldCondition(builder, name, ShGraphQLConstants.CONDITION_LTE, scalarType,
					"All values less than or equal the given value.");
			this.createInputObjectFieldCondition(builder, name, ShGraphQLConstants.CONDITION_GT, scalarType,
					"All values greater than the given value.");
			this.createInputObjectFieldCondition(builder, name, ShGraphQLConstants.CONDITION_GTE, scalarType,
					"All values greater than the given value.");
		} else if (type.equals(ShGraphQLConstants.FIELD_TYPE_GRAPHQL_STRING)
				|| type.equals(ShGraphQLConstants.FIELD_TYPE_GRAPHQL_ID)) {
			if (type.equals(ShGraphQLConstants.FIELD_TYPE_GRAPHQL_STRING))
				scalarType = GraphQLString;
			else if (type.equals(ShGraphQLConstants.FIELD_TYPE_GRAPHQL_ID))
				scalarType = GraphQLID;

			this.createInputObjectFieldCondition(builder, name, null, scalarType, description);
			this.createInputObjectFieldCondition(builder, name, ShGraphQLConstants.CONDITION_NOT, scalarType,
					"All values that are not equal to given value.");
			this.createInputObjectFieldCondition(builder, name, ShGraphQLConstants.CONDITION_IN, scalarType,
					"All values that are contained in given list.");
			this.createInputObjectFieldCondition(builder, name, ShGraphQLConstants.CONDITION_NOT_IN, scalarType,
					"All values that are not contained in given list.");
			this.createInputObjectFieldCondition(builder, name, ShGraphQLConstants.CONDITION_CONTAINS, scalarType,
					"All values containing the given string.");
			this.createInputObjectFieldCondition(builder, name, ShGraphQLConstants.CONDITION_NOT_CONTAINS, scalarType,
					"All values not containing the given string.");
			this.createInputObjectFieldCondition(builder, name, ShGraphQLConstants.CONDITION_STARTS_WITH, scalarType,
					"All values starting with the given string.");
			this.createInputObjectFieldCondition(builder, name, ShGraphQLConstants.CONDITION_NOT_STARTS_WITH,
					scalarType, "All values not starting with the given string.");
			this.createInputObjectFieldCondition(builder, name, ShGraphQLConstants.CONDITION_ENDS_WITH, scalarType,
					"All values ending with the given string.");
			this.createInputObjectFieldCondition(builder, name, ShGraphQLConstants.CONDITION_NOT_ENDS_WITH, scalarType,
					"All values not ending with the given string");
		}
	}

	public void createInputObjectFieldCondition(GraphQLInputObjectType.Builder builder, String name, String condition,
			GraphQLScalarType scalarType, String description) {
		builder.field(newInputObjectField()
				.name(condition == null ? name
						: String.format("%s%s%s", name, ShGraphQLConstants.CONDITION_SEPARATOR, condition))
				.description(description).type(scalarType));
	}

	public void fieldWhereCondition(ShPostType shPostType, List<Map<String, String>> posts,
			Entry<String, Object> whereArgItem, String field, String action) {
		ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostType,
				field.toUpperCase());
		List<ShPostAttr> shPostAttrs = shPostAttrService.findByShPostTypeAttrAndValueAndCondition(shPostTypeAttr,
				whereArgItem.getValue().toString(), action);
		for (ShPostAttr shPostAttr : shPostAttrs) {
			Map<String, String> postAttrsDefault = shPostUtils.postAttrGraphQL(shPostAttr.getShPost());
			posts.add(postAttrsDefault);
		}
	}

	public GraphQLObjectType createObjectType(ShPostType shPostType) {
		Builder builderPlural = newObject().name(shPostType.getName()).description(shPostType.getDescription());

		this.createObjectTypeFields(shPostType, builderPlural);

		return builderPlural.comparatorRegistry(BY_NAME_REGISTRY).build();
	}

	private void createObjectTypeFields(ShPostType shPostType, Builder builder) {
		builder.field(newFieldDefinition().name(ShGraphQLConstants.ID).description("Identifier").type(GraphQLID));
		builder.field(newFieldDefinition().name(ShGraphQLConstants.TITLE).description("Title").type(GraphQLString));
		builder.field(newFieldDefinition().name(ShGraphQLConstants.DESCRIPTION).description("Description")
				.type(GraphQLString));
		builder.field(
				newFieldDefinition().name(ShGraphQLConstants.FURL).description("Friendly URL").type(GraphQLString));
		builder.field(
				newFieldDefinition().name(ShGraphQLConstants.MODIFIER).description("Modifier").type(GraphQLString));
		builder.field(
				newFieldDefinition().name(ShGraphQLConstants.PUBLISHER).description("Publisher").type(GraphQLString));
		builder.field(
				newFieldDefinition().name(ShGraphQLConstants.FOLDER).description("Folder Name").type(GraphQLString));

		for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {
			String postTypeAttrName = this.normalizedField(shPostTypeAttr.getName());
			builder.field(newFieldDefinition().name(postTypeAttrName).description(shPostTypeAttr.getDescription())
					.type(GraphQLString));
		}
	}
}
