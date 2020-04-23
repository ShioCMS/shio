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
import graphql.schema.DataFetcher;
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
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLNonNull.nonNull;
import static graphql.schema.GraphQLObjectType.newObject;
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
public class GraphQLProvider {

	@Autowired
	private ShPostTypeRepository shPostTypeRepository;

	@Autowired
	private ShObjectRepository shObjectRepository;

	@Autowired
	private ShPostRepository shPostRepository;

	@Autowired
	private ShPostUtils shPostUtils;

	private final static String QUERY_TYPE = "QueryType";

	private final static String ID = "id";

	private GraphQL graphQL;

	private GraphQLSchema loadSchema() {
		Builder queryTypeBuilder = newObject().name(QUERY_TYPE);
		graphql.schema.GraphQLCodeRegistry.Builder codeRegistryBuilder = newCodeRegistry();
		for (ShPostType shPostType : shPostTypeRepository.findAll()) {

			String postTypeName = getPostTypeName(shPostType);

			Builder builder = newObject().name(postTypeName).description(shPostType.getDescription());

			GraphQLObjectType graphQLObjectType = this.postTypeFields(shPostType, builder);

			this.allPosts(queryTypeBuilder, codeRegistryBuilder, shPostType, graphQLObjectType);

			this.postById(queryTypeBuilder, codeRegistryBuilder, shPostType, graphQLObjectType);

		}

		GraphQLObjectType queryType = queryTypeBuilder.comparatorRegistry(BY_NAME_REGISTRY).build();

		return GraphQLSchema.newSchema().query(queryType).codeRegistry(codeRegistryBuilder.build()).build();

	}

	private String getPostTypeName(ShPostType shPostType) {
		String postTypeName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,
				shPostType.getName().toLowerCase().replaceAll("-", "_"));
		return postTypeName;
	}

	private void postById(Builder queryTypeBuilder, graphql.schema.GraphQLCodeRegistry.Builder codeRegistryBuilder,
			ShPostType shPostType, GraphQLObjectType graphQLObjectType) {

		String fieldName = String.format("%sById", getPostTypeName(shPostType));

		queryTypeBuilder.field(newFieldDefinition().name(fieldName).type(graphQLObjectType)
				.argument(newArgument().name(ID).description(ID).type(nonNull(GraphQLString))));

		codeRegistryBuilder.dataFetcher(coordinates(QUERY_TYPE, fieldName), getPostTypeByIdDataFetcher(shPostType));
	}

	private void allPosts(Builder queryTypeBuilder, graphql.schema.GraphQLCodeRegistry.Builder codeRegistryBuilder,
			ShPostType shPostType, GraphQLObjectType graphQLObjectType) {

		String fieldName = String.format("%sAll", getPostTypeName(shPostType));

		queryTypeBuilder.field(newFieldDefinition().name(fieldName).type(list(graphQLObjectType)));

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
			List<ShPost> shPosts = shPostRepository.findByShPostType(shPostType);
			List<Map<String, String>> posts = new ArrayList<>();
			for (ShPost shPost : shPosts) {
				Map<String, String> postAttrs = shPostUtils.postAttrGraphQL(shPost);
				posts.add(postAttrs);
			}
			return posts;
		};
	}

	private DataFetcher<Map<String, String>> getPostTypeByIdDataFetcher(ShPostType shPostType) {
		return dataFetchingEnvironment -> {
			String objectId = dataFetchingEnvironment.getArgument(ID);
			ShObject shObject = shObjectRepository.findById(objectId).orElse(null);
			Map<String, String> postAttrs = shPostUtils.postAttrGraphQL((ShPost) shObject);

			return postAttrs;
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
