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
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shio.persistence.repository.object.ShObjectRepository;
import com.viglet.shio.persistence.repository.post.ShPostRepository;
import com.viglet.shio.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shio.utils.ShPostUtils;

import graphql.GraphQL;
import graphql.scalars.ExtendedScalars;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLObjectType.Builder;
import graphql.schema.GraphQLSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	private ShPostUtils shPostUtils;

	private final static String QUERY_TYPE = "Query";

	private final static String ID = "id";
	private final static String STAGE_ARG = "stage";
	private final static String LOCALES_ARG = "locales";
	private final static String WHERE_ARG = "where";

	private GraphQL graphQL;

	public static GraphQLEnumType stageEnum = newEnum().name("Stage").description("Stage system enumeration")
			.value("PUBLISHED", 10, "System Published Stage.").value("DRAFT", 20, "System Draft Stage")
			.comparatorRegistry(BY_NAME_REGISTRY).build();

	public static GraphQLEnumType localeEnum = newEnum().name("Locale").description("Locale system enumeration")
			.value("en", "EN", "System Locale.").comparatorRegistry(BY_NAME_REGISTRY).build();

	public static GraphQLInputObjectType postTypeWhereInput = newInputObject().name("PostTypeWhereInput")
			.description("Locale system enumeration")
			.field(newInputObjectField().name("_search").description("Contains search across all appropriate fields.")
					.type(GraphQLString))
			.field(newInputObjectField().name("AND").description("Logical AND on all given filters.")
					.type(GraphQLString))
			.field(newInputObjectField().name("OR").description("Logical OR on all given filters.").type(GraphQLString))
			.field(newInputObjectField().name("NOT").description("Logical NOT on all given filters combined by AND.")
					.type(GraphQLString))
			.field(newInputObjectField().name("id").description("All values that are equal to given value.")
					.type(GraphQLID))
			.field(newInputObjectField().name("id_not").description("All values that are not equal to given value.")
					.type(GraphQLID))
			.field(newInputObjectField().name("id_in").description("All values that are contained in given list.")
					.type(list(nonNull(GraphQLID))))
			.field(newInputObjectField().name("id_not_in")
					.description("All values that are not contained in given list.").type(list(nonNull(GraphQLID))))
			.field(newInputObjectField().name("id_contains").description("All values containing the given string.")
					.type(GraphQLID))
			.field(newInputObjectField().name("id_not_contains")
					.description("All values not containing the given string.").type(GraphQLID))
			.field(newInputObjectField().name("id_starts_with")
					.description("All values starting with the given string.").type(GraphQLID))
			.field(newInputObjectField().name("id_not_starts_with")
					.description("All values not starting with the given string.").type(GraphQLID))
			.field(newInputObjectField().name("id_ends_with").description("All values ending with the given string.")
					.type(GraphQLID))
			.field(newInputObjectField().name("id_not_ends_with")
					.description("All values not ending with the given string").type(GraphQLID))
			.field(newInputObjectField().name("createdAt").description("All values that are equal to given value.")
					.type(ExtendedScalars.DateTime))
			.field(newInputObjectField().name("createdAt_in")
					.description("All values that are contained in given list.")
					.type(list(nonNull(ExtendedScalars.DateTime))))
			.field(newInputObjectField().name("createdAt_not_in")
					.description("All values that are not contained in given list.")
					.type(list(nonNull(ExtendedScalars.DateTime))))
			.field(newInputObjectField().name("createdAt_lt").description("All values less than the given value.")
					.type(ExtendedScalars.DateTime))
			.comparatorRegistry(BY_NAME_REGISTRY).build();

	private GraphQLSchema loadSchema() {
		Builder queryTypeBuilder = newObject().name(QUERY_TYPE);
		graphql.schema.GraphQLCodeRegistry.Builder codeRegistryBuilder = newCodeRegistry();
		for (ShPostType shPostType : shPostTypeRepository.findAll()) {

			String postTypeName = getPostTypeName(shPostType);

			Builder builder = newObject().name(postTypeName).description(shPostType.getDescription());

			GraphQLObjectType graphQLObjectType = this.postTypeFields(shPostType, builder);

			this.allPosts(queryTypeBuilder, codeRegistryBuilder, shPostType, graphQLObjectType);

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
			ShPostType shPostType, GraphQLObjectType graphQLObjectType) {

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

	private GraphQLObjectType postTypeFields(ShPostType shPostType, Builder builder) {
		builder.field(newFieldDefinition().name(ID).description("Id of Object").type(GraphQLString));

		for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {
			String postTypeAttrName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,
					shPostTypeAttr.getName().toLowerCase().replaceAll("-", "_"));
			builder.field(newFieldDefinition().name(postTypeAttrName).description(shPostTypeAttr.getDescription())
					.type(GraphQLString));
		}

		return builder.comparatorRegistry(BY_NAME_REGISTRY).build();
	}

	private DataFetcher<List<Map<String, String>>> getPostTypeAllDataFetcher(ShPostType shPostType) {
		return dataFetchingEnvironment -> {
			List<Map<String, String>> posts = new ArrayList<>();

			Map<String, Object> whereMap = dataFetchingEnvironment.getArgument(WHERE_ARG);
			if (whereMap != null && whereMap.containsKey(ID) && whereMap.get(ID) != null) {
				String objectId = whereMap.get(ID).toString();
				ShObject shObject = shObjectRepository.findById(objectId).orElse(null);
				Map<String, String> postAttrs = shPostUtils.postAttrGraphQL((ShPost) shObject);
				posts.add(postAttrs);
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
