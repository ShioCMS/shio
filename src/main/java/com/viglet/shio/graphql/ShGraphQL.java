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

import com.google.common.base.CaseFormat;
import com.viglet.shio.persistence.model.object.ShObject;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shio.persistence.repository.object.ShObjectRepository;
import com.viglet.shio.persistence.repository.post.ShPostRepository;
import com.viglet.shio.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shio.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shio.persistence.service.post.ShPostAttrService;
import com.viglet.shio.utils.ShPostUtils;

import graphql.GraphQL;
import graphql.scalars.ExtendedScalars;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLObjectType.Builder;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLTypeReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static graphql.Scalars.GraphQLString;
import static graphql.Scalars.GraphQLID;
import static graphql.schema.GraphQLEnumType.newEnum;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLNonNull.nonNull;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static graphql.schema.GraphQLList.list;
import static graphql.schema.GraphQLCodeRegistry.newCodeRegistry;
import static graphql.schema.FieldCoordinates.coordinates;
import static graphql.schema.GraphqlTypeComparatorRegistry.BY_NAME_REGISTRY;

/**
 * GraphQL Provider.
 *
 * @author Alexandre Oliveira
 * @since 0.3.7
 */
@Component
public class ShGraphQL {

	@Autowired
	private ShPostTypeRepository shPostTypeRepository;

	@Autowired
	private ShObjectRepository shObjectRepository;

	@Autowired
	private ShPostRepository shPostRepository;

	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostUtils shPostUtils;

	@Autowired
	private ShPostAttrService shPostAttrService;

	private final static String QUERY_TYPE = "Query";

	private final static String SEARCH = "_search";
	private final static String AND = "AND";
	private final static String OR = "OR";
	private final static String NOT = "NOT";
	private final static String ID = "id";
	private final static String TITLE = "title";
	private final static String DESCRIPTION = "description";
	private final static String FURL = "furl";
	private final static String MODIFIER = "modifier";
	private final static String PUBLISHER = "publisher";
	private final static String FOLDER = "folder";

	private final static String CREATED_AT = "createdAt";
	private final static String UPDATED_AT = "updatedAt";
	private final static String PUBLISHED_AT = "publishedAt";

	private final static String STAGE_ARG = "stage";
	private final static String LOCALES_ARG = "locales";
	private final static String WHERE_ARG = "where";

	private GraphQL graphQL;

	public static GraphQLEnumType stageEnum = newEnum().name("Stage").description("Stage system enumeration")
			.value("PUBLISHED", 10, "System Published Stage.").value("DRAFT", 20, "System Draft Stage")
			.comparatorRegistry(BY_NAME_REGISTRY).build();

	public static GraphQLEnumType localeEnum = newEnum().name("Locale").description("Locale system enumeration")
			.value("en", "EN", "System Locale.").comparatorRegistry(BY_NAME_REGISTRY).build();

	private void createInputObjectField(String name, String type,
			graphql.schema.GraphQLInputObjectType.Builder builder) {

		GraphQLScalarType scalarType = null;

		if (type.equals("DateTime")) {
			scalarType = ExtendedScalars.DateTime;
			builder.field(newInputObjectField().name(name).description("All values that are equal to given value.")
					.type(scalarType))
					.field(newInputObjectField().name(String.format("%s_not", name))
							.description("All values that are not equal to given value.").type(scalarType))
					.field(newInputObjectField().name(String.format("%s_in", name))
							.description("All values that are not contained in given list.")
							.type(list(nonNull(scalarType))))
					.field(newInputObjectField().name(String.format("%s_lt", name))
							.description("All values less than the given value.").type(scalarType))
					.field(newInputObjectField().name(String.format("%s_lte", name))
							.description("All values less than or equal the given value.").type(scalarType))
					.field(newInputObjectField().name(String.format("%s_gt", name))
							.description("All values greater than the given value.").type(scalarType))
					.field(newInputObjectField().name(String.format("%s_gte", name))
							.description("All values greater than the given value.").type(scalarType));
		} else if (type.equals("GraphQLString") || type.equals("GraphQLID")) {
			if (type.equals("GraphQLString"))
				scalarType = GraphQLString;
			else if (type.equals("GraphQLID"))
				scalarType = GraphQLID;

			builder.field(newInputObjectField().name(name).description("All values that are equal to given value.")
					.type(scalarType))
					.field(newInputObjectField().name(String.format("%s_not", name))
							.description("All values that are not equal to given value.").type(scalarType))
					.field(newInputObjectField().name(String.format("%s_in", name))
							.description("All values that are contained in given list.")
							.type(list(nonNull(scalarType))))
					.field(newInputObjectField().name(String.format("%s_not_in", name))
							.description("All values that are not contained in given list.")
							.type(list(nonNull(scalarType))))
					.field(newInputObjectField().name(String.format("%s_contains", name))
							.description("All values containing the given string.").type(scalarType))
					.field(newInputObjectField().name(String.format("%s_not_contains", name))
							.description("All values not containing the given string.").type(scalarType))
					.field(newInputObjectField().name(String.format("%s_starts_with", name))
							.description("All values starting with the given string.").type(scalarType))
					.field(newInputObjectField().name(String.format("%s_not_starts_with", name))
							.description("All values not starting with the given string.").type(scalarType))
					.field(newInputObjectField().name(String.format("%s_ends_with", name))
							.description("All values ending with the given string.").type(scalarType))
					.field(newInputObjectField().name(String.format("%s_not_ends_with", name))
							.description("All values not ending with the given string").type(scalarType));
		}

	}

	private GraphQLSchema loadSchema() {
		Builder queryTypeBuilder = newObject().name(QUERY_TYPE);
		graphql.schema.GraphQLCodeRegistry.Builder codeRegistryBuilder = newCodeRegistry();
		for (ShPostType shPostType : shPostTypeRepository.findAll()) {

			String postTypeName = getPostTypeName(shPostType);

			Builder builder = newObject().name(postTypeName).description(shPostType.getDescription());

			graphql.schema.GraphQLInputObjectType.Builder postTypeWhereInputBuilder = newInputObject()
					.name(String.format("%sWhereInput", postTypeName)).description("Identifies documents");

			GraphQLObjectType graphQLObjectType = this.postTypeFields(shPostType, builder, postTypeWhereInputBuilder);

			GraphQLInputObjectType postTypeWhereInput = postTypeWhereInputBuilder.comparatorRegistry(BY_NAME_REGISTRY)
					.build();
			this.allPosts(queryTypeBuilder, codeRegistryBuilder, shPostType, graphQLObjectType, postTypeWhereInput);

		}

		GraphQLObjectType queryType = queryTypeBuilder.comparatorRegistry(BY_NAME_REGISTRY).build();

		return GraphQLSchema.newSchema().query(queryType).codeRegistry(codeRegistryBuilder.build()).build();

	}

	private String getPostTypeName(ShPostType shPostType) {
		String postTypeName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,
				shPostType.getName().toLowerCase().replaceAll("-", "_"));
		return postTypeName;
	}

	private void allPosts(Builder queryTypeBuilder, graphql.schema.GraphQLCodeRegistry.Builder codeRegistryBuilder,
			ShPostType shPostType, GraphQLObjectType graphQLObjectType, GraphQLInputObjectType postTypeWhereInput) {

		String fieldName = String.format("%s", getPostTypeName(shPostType));

		queryTypeBuilder.field(newFieldDefinition().name(fieldName).type(list(graphQLObjectType))
				.argument(newArgument().name(STAGE_ARG)
						.description("A required enumeration indicating the current content Stage (defaults to DRAFT)")
						.type(nonNull(stageEnum)).defaultValue(20))
				.argument(newArgument().name(LOCALES_ARG)
						.description("A required array of one or more locales, defaults to the project's default.")
						.type(nonNull(list(localeEnum))).defaultValue("EN"))
				.argument(newArgument().name(WHERE_ARG)
						.description("An optional object type to filter the content based on a nested set of criteria.")
						.type(postTypeWhereInput)));

		codeRegistryBuilder.dataFetcher(coordinates(QUERY_TYPE, fieldName), getPostTypeAllDataFetcher(shPostType));
	}

	private GraphQLObjectType postTypeFields(ShPostType shPostType, Builder builder,
			graphql.schema.GraphQLInputObjectType.Builder postTypeWhereInputBuilder) {

		String whereInputName = String.format("%sWhereInput", getPostTypeName(shPostType));

		builder.field(newFieldDefinition().name(ID).description("Object Id").type(GraphQLID));
		builder.field(newFieldDefinition().name(TITLE).description("Object Text").type(GraphQLString));
		builder.field(newFieldDefinition().name(DESCRIPTION).description("Object Description").type(GraphQLString));
		builder.field(newFieldDefinition().name(FURL).description("Object Description").type(GraphQLString));
		builder.field(newFieldDefinition().name(MODIFIER).description("Object Description").type(GraphQLString));
		builder.field(newFieldDefinition().name(PUBLISHER).description("Object Description").type(GraphQLString));
		builder.field(newFieldDefinition().name(FOLDER).description("Object Description").type(GraphQLString));

		postTypeWhereInputBuilder
				.field(newInputObjectField().name(SEARCH).description("Contains search across all appropriate fields.")
						.type(GraphQLString))
				.field(newInputObjectField().name(AND).description("Logical AND on all given filters.")
						.type(GraphQLTypeReference.typeRef(whereInputName)))
				.field(newInputObjectField().name(OR).description("Logical OR on all given filters.")
						.type(GraphQLTypeReference.typeRef(whereInputName)))
				.field(newInputObjectField().name(NOT).description("Logical NOT on all given filters combined by AND.")
						.type(GraphQLTypeReference.typeRef(whereInputName)))
				.field(newInputObjectField().name(ID).description("All values that are equal to given value.")
						.type(GraphQLID));
		this.createInputObjectField(ID, "GraphQLID", postTypeWhereInputBuilder);
		this.createInputObjectField(TITLE, "GraphQLString", postTypeWhereInputBuilder);
		this.createInputObjectField(DESCRIPTION, "GraphQLString", postTypeWhereInputBuilder);
		this.createInputObjectField(FURL, "GraphQLString", postTypeWhereInputBuilder);
		this.createInputObjectField(MODIFIER, "GraphQLString", postTypeWhereInputBuilder);
		this.createInputObjectField(PUBLISHER, "GraphQLString", postTypeWhereInputBuilder);
		this.createInputObjectField(FOLDER, "GraphQLString", postTypeWhereInputBuilder);
		this.createInputObjectField(CREATED_AT, "DateTime", postTypeWhereInputBuilder);
		this.createInputObjectField(UPDATED_AT, "DateTime", postTypeWhereInputBuilder);
		this.createInputObjectField(PUBLISHED_AT, "DateTime", postTypeWhereInputBuilder);

		for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {
			String postTypeAttrName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,
					shPostTypeAttr.getName().toLowerCase().replaceAll("-", "_"));
			builder.field(newFieldDefinition().name(postTypeAttrName).description(shPostTypeAttr.getDescription())
					.type(GraphQLString));

			this.createInputObjectField(postTypeAttrName, "GraphQLString", postTypeWhereInputBuilder);

		}

		return builder.comparatorRegistry(BY_NAME_REGISTRY).build();
	}

	private DataFetcher<List<Map<String, String>>> getPostTypeAllDataFetcher(ShPostType shPostType) {
		return dataFetchingEnvironment -> {
			List<Map<String, String>> posts = new ArrayList<>();

			Map<String, Object> whereMap = dataFetchingEnvironment.getArgument(WHERE_ARG);

			if (whereMap != null) {
				for (Entry<String, Object> whereArgItem : whereMap.entrySet()) {
					String arg = whereArgItem.getKey();
					if (arg.equals(SEARCH)) {

					} else if (arg.equals(SEARCH)) {

					} else if (arg.equals(AND)) {

					} else if (arg.equals(OR)) {

					} else if (arg.equals(NOT)) {

					} else if (arg.equals(ID)) {
						String objectId = whereMap.get(ID).toString();
						ShObject shObject = shObjectRepository.findById(objectId).orElse(null);
						Map<String, String> postAttrs = shPostUtils.postAttrGraphQL((ShPost) shObject);
						posts.add(postAttrs);
					} else if (arg.equals(TITLE)) {
						List<ShPost> shPosts = shPostRepository.findByTitle(whereMap.get(TITLE).toString());
						for (ShPost shPost : shPosts) {
							Map<String, String> postAttrs = shPostUtils.postAttrGraphQL(shPost);
							posts.add(postAttrs);
						}
					} else if (arg.equals(DESCRIPTION)) {
						List<ShPost> shPosts = shPostRepository.findBySummary(whereMap.get(DESCRIPTION).toString());
						for (ShPost shPost : shPosts) {
							Map<String, String> postAttrs = shPostUtils.postAttrGraphQL(shPost);
							posts.add(postAttrs);
						}
					} else if (arg.equals(FURL)) {
						List<ShPost> shPosts = shPostRepository.findByFurl(whereMap.get(FURL).toString());
						for (ShPost shPost : shPosts) {
							Map<String, String> postAttrs = shPostUtils.postAttrGraphQL(shPost);
							posts.add(postAttrs);
						}
					} else if (arg.equals(MODIFIER)) {
						List<ShPost> shPosts = shPostRepository.findByModifier(whereMap.get(MODIFIER).toString());
						for (ShPost shPost : shPosts) {
							Map<String, String> postAttrs = shPostUtils.postAttrGraphQL(shPost);
							posts.add(postAttrs);
						}
					} else if (arg.equals(PUBLISHER)) {
						List<ShPost> shPosts = shPostRepository.findByPublisher(whereMap.get(PUBLISHER).toString());
						for (ShPost shPost : shPosts) {
							Map<String, String> postAttrs = shPostUtils.postAttrGraphQL(shPost);
							posts.add(postAttrs);
						}
					} else if (arg.equals(FOLDER)) {
						List<ShPost> shPosts = shPostRepository.findByShFolder_Name(whereMap.get(FOLDER).toString());
						for (ShPost shPost : shPosts) {
							Map<String, String> postAttrs = shPostUtils.postAttrGraphQL(shPost);
							posts.add(postAttrs);
						}
					} else {
						if (arg.contains("_")) {
							String field = arg.split("_")[0];
							String action = arg.replaceFirst(field, "");
						} else { // It is a field
							ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostType,
									arg.toUpperCase());
							List<ShPostAttr> shPostAttrs = shPostAttrService
									.findByShPostTypeAttrAndValue(shPostTypeAttr, whereArgItem.getValue().toString());
							for (ShPostAttr shPostAttr : shPostAttrs) {
								Map<String, String> postAttrsDefault = shPostUtils
										.postAttrGraphQL(shPostAttr.getShPost());
								posts.add(postAttrsDefault);
							}
						}
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

	@PostConstruct
	public void init() throws IOException {
		GraphQLSchema graphQLSchema = this.loadSchema();
		this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
	}

	@Bean
	public GraphQL graphQL() {
		return graphQL;
	}

}
