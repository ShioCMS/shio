package com.viglet.shio.graphql;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static graphql.Scalars.GraphQLString;
import static graphql.Scalars.GraphQLID;
import static graphql.Scalars.GraphQLInt;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLList.list;
import static graphql.schema.GraphQLNonNull.nonNull;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphqlTypeComparatorRegistry.BY_NAME_REGISTRY;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {
	@Autowired
	GraphQLDataFetchers graphQLDataFetchers;

	private GraphQL graphQL;

	public static GraphQLObjectType userType = newObject().name("User")
			.description("A humanoid creature in the Star Wars universe.")
			.field(newFieldDefinition().name("id").description("The id of the human.").type(nonNull(GraphQLID)))
			.field(newFieldDefinition().name("name").description("The name of the human.").type(GraphQLString))
			.field(newFieldDefinition().name("age")
					.description("The friends of the human, or an empty list if they have none.").type(GraphQLInt))
			.field(newFieldDefinition().name("address").description("Which movies they appear in.").type(GraphQLString))
			.comparatorRegistry(BY_NAME_REGISTRY).build();

	public static GraphQLObjectType addressType = newObject().name("Address")
			.description("A humanoid creature in the Star Wars universe.")
			.field(newFieldDefinition().name("id").description("The id of the human.").type(nonNull(GraphQLID)))
			.field(newFieldDefinition().name("houseName").description("The name of the human.").type(GraphQLString))
			.field(newFieldDefinition().name("country")
					.description("The friends of the human, or an empty list if they have none.").type(GraphQLString))
			.comparatorRegistry(BY_NAME_REGISTRY).build();

	public static GraphQLObjectType queryType = newObject().name("QueryType")
			.field(newFieldDefinition().name("userById").type(userType)
					.argument(newArgument().name("id").description("id of the human").type(nonNull(GraphQLString)))
					.dataFetcher(getUserByIdDataFetcher()))
			.comparatorRegistry(BY_NAME_REGISTRY).build();

	public static GraphQLSchema userSchema = GraphQLSchema.newSchema().query(queryType).build();

	private static List<Map<String, String>> users = Arrays.asList(
			ImmutableMap.of("id", "sid-1", "name", "Siddharatha dhumale 1", "age", "1", "addressId", "address-1"),
			ImmutableMap.of("id", "sid-1", "name", "Siddharatha dhumale 2", "age", "2", "addressId", "address-2"),
			ImmutableMap.of("id", "sid-3", "name", "Siddharatha dhumale 3", "age", "3", "addressId", "address-3"));

	private static List<Map<String, String>> address = Arrays.asList(
			ImmutableMap.of("id", "address-1", "houseName", "Siddharatha Dhumale House Name 1", "country", "Australia"),
			ImmutableMap.of("id", "address-2", "houseName", "Siddharatha Dhumale House Name 2", "country", "UK"),
			ImmutableMap.of("id", "address-3", "houseName", "Siddharatha Dhumale House Name 3", "country", "India"));

	public static DataFetcher getUserByIdDataFetcher() {
		return dataFetchingEnvironment -> {
			String userId = dataFetchingEnvironment.getArgument("id");
			return users.stream().filter(user -> user.get("id").equals(userId)).findFirst().orElse(null);
		};
	}

	public static DataFetcher getUserDataFetcher() {
		return dataFetchingEnvironment -> {
			Map<String, String> user = dataFetchingEnvironment.getSource();
			String addressId = user.get("addressId");
			return address.stream().filter(author -> author.get("id").equals(addressId)).findFirst().orElse(null);
		};
	}
	
	@PostConstruct
	public void init() throws IOException {
		// URL url = Resources.getResource("schema.graphqls");
		// String sdl = Resources.toString(url, Charsets.UTF_8);
		// GraphQLSchema graphQLSchema = buildSchema(sdl);
		GraphQLSchema graphQLSchema = GraphQLSchema.newSchema().query(queryType).build();
		this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
	}

	private GraphQLSchema buildSchema(String sdl) {
		TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
		RuntimeWiring runtimeWiring = buildWiring();
		SchemaGenerator schemaGenerator = new SchemaGenerator();
		return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
	}

	private RuntimeWiring buildWiring() {
		return RuntimeWiring.newRuntimeWiring()
				.type(newTypeWiring("Query").dataFetcher("userById", graphQLDataFetchers.getUserByIdDataFetcher()))
				.type(newTypeWiring("User").dataFetcher("address", graphQLDataFetchers.getUserDataFetcher())).build();
	}

	@Bean
	public GraphQL graphQL() {
		return graphQL;
	}

}
