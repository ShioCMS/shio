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
package com.viglet.shio.graphql.schema.queryType;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.FieldCoordinates.coordinates;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static graphql.schema.GraphQLList.list;
import static graphql.schema.GraphQLNonNull.nonNull;
import static graphql.schema.GraphqlTypeComparatorRegistry.BY_NAME_REGISTRY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shio.graphql.schema.ShGraphQLConstants;
import com.viglet.shio.graphql.schema.ShGraphQLInputObjectField;
import com.viglet.shio.graphql.schema.ShGraphQLUtils;
import com.viglet.shio.persistence.model.object.ShObject;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shio.persistence.repository.object.ShObjectRepository;
import com.viglet.shio.persistence.repository.post.ShPostRepository;
import com.viglet.shio.utils.ShPostUtils;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLTypeReference;
import graphql.schema.GraphQLObjectType.Builder;

/**
 * GraphQL Query Type Plural.
 *
 * @author Alexandre Oliveira
 * @since 0.3.7
 */
@Component
public class ShGraphQLQTPlural {

	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShGraphQLUtils shGraphQLUtils;
	@Autowired
	private ShGraphQLInputObjectField shGraphQLInputObjectField;

	private String getPostTypeNamePlural(ShPostType shPostType) {
		return shGraphQLUtils.normalizedPostType(shPostType.getNamePlural());
	}

	public void createQueryTypePlural(Builder queryTypeBuilder,
			graphql.schema.GraphQLCodeRegistry.Builder codeRegistryBuilder, ShPostType shPostType,
			GraphQLObjectType graphQLObjectType) {
		String postTypeNamePlural = this.getPostTypeNamePlural(shPostType);

		GraphQLInputObjectType.Builder postTypeWhereInputBuilder = newInputObject()
				.name(shPostType.getName().concat(ShGraphQLConstants.WHERE_INPUT)).description("Identifies documents");

		this.whereFieldsPlural(shPostType, postTypeWhereInputBuilder);

		GraphQLInputObjectType postTypeWhereInput = postTypeWhereInputBuilder.comparatorRegistry(BY_NAME_REGISTRY)
				.build();

		queryTypeBuilder.field(newFieldDefinition().name(postTypeNamePlural)
				.type(nonNull(list(nonNull(graphQLObjectType))))
				.argument(newArgument().name(ShGraphQLConstants.STAGE_ARG)
						.description("A required enumeration indicating the current content Stage (defaults to DRAFT)")
						.type(nonNull(ShGraphQLConstants.stageEnum)).defaultValue(20))
				.argument(newArgument().name(ShGraphQLConstants.LOCALES_ARG)
						.description("A required array of one or more locales, defaults to the project's default.")
						.type(nonNull(list(ShGraphQLConstants.localeEnum))).defaultValue("EN"))
				.argument(newArgument().name(ShGraphQLConstants.WHERE_ARG)
						.description("An optional object type to filter the content based on a nested set of criteria.")
						.type(postTypeWhereInput)));

		codeRegistryBuilder.dataFetcher(coordinates(ShGraphQLConstants.QUERY_TYPE, postTypeNamePlural),
				this.getPostTypeAllDataFetcherPlural(shPostType));
	}

	private void whereFieldsPlural(ShPostType shPostType, GraphQLInputObjectType.Builder postTypeWhereInputBuilder) {

		String whereInputName = shPostType.getName().concat(ShGraphQLConstants.WHERE_INPUT);

		GraphQLTypeReference whereInputRef = GraphQLTypeReference.typeRef(whereInputName);

		shGraphQLInputObjectField.createInputObjectField(postTypeWhereInputBuilder, ShGraphQLConstants.SEARCH,
				GraphQLString, "Contains search across all appropriate fields.");
		shGraphQLInputObjectField.createInputObjectField(postTypeWhereInputBuilder, ShGraphQLConstants.AND,
				whereInputRef, "Logical AND on all given filters.");
		shGraphQLInputObjectField.createInputObjectField(postTypeWhereInputBuilder, ShGraphQLConstants.OR,
				whereInputRef, "Logical OR on all given filters.");
		shGraphQLInputObjectField.createInputObjectField(postTypeWhereInputBuilder, ShGraphQLConstants.NOT,
				whereInputRef, "Logical NOT on all given filters combined by AND.");

		shGraphQLInputObjectField.createInputObjectField(postTypeWhereInputBuilder, ShGraphQLConstants.ID,
				ShGraphQLConstants.FIELD_TYPE_GRAPHQL_ID, "Identifier");
		shGraphQLInputObjectField.createInputObjectField(postTypeWhereInputBuilder, ShGraphQLConstants.TITLE,
				ShGraphQLConstants.FIELD_TYPE_GRAPHQL_STRING, "Title");
		shGraphQLInputObjectField.createInputObjectField(postTypeWhereInputBuilder, ShGraphQLConstants.DESCRIPTION,
				ShGraphQLConstants.FIELD_TYPE_GRAPHQL_STRING, "Description");
		shGraphQLInputObjectField.createInputObjectField(postTypeWhereInputBuilder, ShGraphQLConstants.FURL,
				ShGraphQLConstants.FIELD_TYPE_GRAPHQL_STRING, "Friendly URL");
		shGraphQLInputObjectField.createInputObjectField(postTypeWhereInputBuilder, ShGraphQLConstants.MODIFIER,
				ShGraphQLConstants.FIELD_TYPE_GRAPHQL_STRING, "Modifier");
		shGraphQLInputObjectField.createInputObjectField(postTypeWhereInputBuilder, ShGraphQLConstants.PUBLISHER,
				ShGraphQLConstants.FIELD_TYPE_GRAPHQL_STRING, "Publisher");
		shGraphQLInputObjectField.createInputObjectField(postTypeWhereInputBuilder, ShGraphQLConstants.FOLDER,
				ShGraphQLConstants.FIELD_TYPE_GRAPHQL_STRING, "Folder Name");
		shGraphQLInputObjectField.createInputObjectField(postTypeWhereInputBuilder, ShGraphQLConstants.CREATED_AT,
				ShGraphQLConstants.FIELD_TYPE_GRAPHQL_DATE_TIME, "Created Date");
		shGraphQLInputObjectField.createInputObjectField(postTypeWhereInputBuilder, ShGraphQLConstants.UPDATED_AT,
				ShGraphQLConstants.FIELD_TYPE_GRAPHQL_DATE_TIME, "Updated Date");
		shGraphQLInputObjectField.createInputObjectField(postTypeWhereInputBuilder, ShGraphQLConstants.PUBLISHED_AT,
				ShGraphQLConstants.FIELD_TYPE_GRAPHQL_DATE_TIME, "Published Date");

		for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {

			String postTypeAttrName = shGraphQLUtils.normalizedField(shPostTypeAttr.getName());

			shGraphQLInputObjectField.createInputObjectField(postTypeWhereInputBuilder, postTypeAttrName,
					ShGraphQLConstants.FIELD_TYPE_GRAPHQL_STRING, shPostTypeAttr.getDescription());

		}
	}

	private DataFetcher<List<Map<String, String>>> getPostTypeAllDataFetcherPlural(ShPostType shPostType) {
		return dataFetchingEnvironment -> {
			List<Map<String, String>> posts = new ArrayList<>();

			Map<String, Object> whereMap = dataFetchingEnvironment.getArgument(ShGraphQLConstants.WHERE_ARG);

			if (whereMap != null) {
				for (Entry<String, Object> whereArgItem : whereMap.entrySet()) {
					String arg = whereArgItem.getKey();
					if (arg.equals(ShGraphQLConstants.SEARCH)) {

					} else if (arg.equals(ShGraphQLConstants.SEARCH)) {

					} else if (arg.equals(ShGraphQLConstants.AND)) {

					} else if (arg.equals(ShGraphQLConstants.OR)) {

					} else if (arg.equals(ShGraphQLConstants.NOT)) {

					} else if (arg.equals(ShGraphQLConstants.ID)) {
						String objectId = whereMap.get(ShGraphQLConstants.ID).toString();
						ShObject shObject = shObjectRepository.findById(objectId).orElse(null);
						Map<String, String> postAttrs = shPostUtils.postAttrGraphQL((ShPost) shObject);
						posts.add(postAttrs);
					} else if (arg.equals(ShGraphQLConstants.TITLE)) {
						List<ShPost> shPosts = shPostRepository
								.findByTitle(whereMap.get(ShGraphQLConstants.TITLE).toString());
						for (ShPost shPost : shPosts) {
							Map<String, String> postAttrs = shPostUtils.postAttrGraphQL(shPost);
							posts.add(postAttrs);
						}
					} else if (arg.equals(ShGraphQLConstants.DESCRIPTION)) {
						List<ShPost> shPosts = shPostRepository
								.findBySummary(whereMap.get(ShGraphQLConstants.DESCRIPTION).toString());
						for (ShPost shPost : shPosts) {
							Map<String, String> postAttrs = shPostUtils.postAttrGraphQL(shPost);
							posts.add(postAttrs);
						}
					} else if (arg.equals(ShGraphQLConstants.FURL)) {
						List<ShPost> shPosts = shPostRepository
								.findByFurl(whereMap.get(ShGraphQLConstants.FURL).toString());
						for (ShPost shPost : shPosts) {
							Map<String, String> postAttrs = shPostUtils.postAttrGraphQL(shPost);
							posts.add(postAttrs);
						}
					} else if (arg.equals(ShGraphQLConstants.MODIFIER)) {
						List<ShPost> shPosts = shPostRepository
								.findByModifier(whereMap.get(ShGraphQLConstants.MODIFIER).toString());
						for (ShPost shPost : shPosts) {
							Map<String, String> postAttrs = shPostUtils.postAttrGraphQL(shPost);
							posts.add(postAttrs);
						}
					} else if (arg.equals(ShGraphQLConstants.PUBLISHER)) {
						List<ShPost> shPosts = shPostRepository
								.findByPublisher(whereMap.get(ShGraphQLConstants.PUBLISHER).toString());
						for (ShPost shPost : shPosts) {
							Map<String, String> postAttrs = shPostUtils.postAttrGraphQL(shPost);
							posts.add(postAttrs);
						}
					} else if (arg.equals(ShGraphQLConstants.FOLDER)) {
						List<ShPost> shPosts = shPostRepository
								.findByShFolder_Name(whereMap.get(ShGraphQLConstants.FOLDER).toString());
						for (ShPost shPost : shPosts) {
							Map<String, String> postAttrs = shPostUtils.postAttrGraphQL(shPost);
							posts.add(postAttrs);
						}
					} else {
						String field = arg;
						String action = ShGraphQLConstants.CONDITION_EQUAL;
						if (arg.contains(ShGraphQLConstants.CONDITION_SEPARATOR)) {
							field = arg.split(ShGraphQLConstants.CONDITION_SEPARATOR)[0];
							action = arg.replaceFirst(field.concat(ShGraphQLConstants.CONDITION_SEPARATOR), "");
						}
						shGraphQLInputObjectField.fieldWhereCondition(shPostType, posts, whereArgItem, field, action);
					}
				}
			} else {
				List<ShPost> shPosts = shPostRepository.findByShPostType(shPostType);
				for (ShPost shPost : shPosts) {
					Map<String, String> postAttrs = shPostUtils.postAttrGraphQL(shPost);
					posts.add(postAttrs);
				}
			}
			return posts;
		};
	}
}
