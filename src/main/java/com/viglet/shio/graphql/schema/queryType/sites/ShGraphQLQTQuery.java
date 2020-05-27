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
package com.viglet.shio.graphql.schema.queryType.sites;

import static graphql.Scalars.GraphQLID;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.FieldCoordinates.coordinates;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLNonNull.nonNull;
import static graphql.schema.GraphQLList.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.viglet.shio.api.ShJsonView;
import com.viglet.shio.graphql.ShGraphQLConstants;
import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.sites.component.ShQueryComponent;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLObjectType.Builder;

/**
 * GraphQL Navigation Query Type.
 *
 * @author Alexandre Oliveira
 * @since 0.3.7
 */
@Component
public class ShGraphQLQTQuery {

	@Autowired
	private ShQueryComponent shQueryComponent;
	public final static String POST_TYPE_NAME = "postTypeName";
	public final static String FOLDER_ID = "folderId";
	public final static String POST_ATTR_NAME = "postAttrName";
	public final static String ARRAY_VALUE = "arrayValue";
	private static final String QUERY_TYPE_NAME = "shQuery";

	public void createQueryType(Builder queryTypeBuilder,
			graphql.schema.GraphQLCodeRegistry.Builder codeRegistryBuilder, GraphQLObjectType graphQLObjectType) {

		this.createArguments(queryTypeBuilder, graphQLObjectType);

		codeRegistryBuilder.dataFetcher(coordinates(ShGraphQLConstants.QUERY_TYPE, QUERY_TYPE_NAME),
				this.getDataFetcher());
	}

	private void createArguments(Builder queryTypeBuilder, GraphQLObjectType graphQLObjectType) {
		queryTypeBuilder.field(newFieldDefinition().name(QUERY_TYPE_NAME).type(list(graphQLObjectType))
				.argument(newArgument().name(POST_TYPE_NAME).description("Post Type Name").type(nonNull(GraphQLString)))
				.argument(newArgument().name(FOLDER_ID).description("Folder Id").type(GraphQLID))
				.argument(newArgument().name(POST_ATTR_NAME).description("Post Attribute Name").type(GraphQLString))
				.argument(newArgument().name(ARRAY_VALUE).description("Array Value").type(list(GraphQLString))));

	}

	private DataFetcher<List<Map<String, Object>>> getDataFetcher() {
		return dataFetchingEnvironment -> {
			List<Map<String, Object>> results = new ArrayList<>();
			String postTypeName = dataFetchingEnvironment.getArgument(POST_TYPE_NAME);
			String folderId = dataFetchingEnvironment.getArgument(FOLDER_ID);
			String postAttrName = dataFetchingEnvironment.getArgument(POST_ATTR_NAME);
			List<String> arrayValue = dataFetchingEnvironment.getArgument(ARRAY_VALUE);
			if (folderId == null && postAttrName == null && arrayValue == null) {
				for (Map<String, ShPostAttr> post : shQueryComponent.findByPostTypeName(postTypeName)) {
					Map<String, Object> result = postToGraphQL(post);
					results.add(result);
				}
			} else {
				if (folderId != null) {
					for (Map<String, ShPostAttr> post : shQueryComponent.findByFolderName(folderId, postTypeName)) {
						Map<String, Object> result = postToGraphQL(post);
						results.add(result);
					}
				} else if (postAttrName != null && arrayValue != null) {
					for (Map<String, ShPostAttr> post : shQueryComponent.findByPostTypeNameIn(postTypeName,
							postAttrName, Sets.newHashSet(arrayValue))) {
						Map<String, Object> result = postToGraphQL(post);
						results.add(result);
					}
				}
			}
			return results;
		};
	}

	private Map<String, Object> postToGraphQL(Map<String, ShPostAttr> post)
			throws JsonProcessingException, JsonMappingException {
		Map<String, Object> result = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writerWithView(ShJsonView.ShJsonViewObject.class).writeValueAsString(post);
		Map<String, ShPostAttr> tinyPost = mapper.readValue(jsonInString, new TypeReference<Map<String, ShPostAttr>>() {
		});
		tinyPost.remove("__type__");
		result.put("post", tinyPost);
		return result;
	}
}
