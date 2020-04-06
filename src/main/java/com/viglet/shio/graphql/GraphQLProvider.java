package com.viglet.shio.graphql;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableMap;
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shio.persistence.repository.post.type.ShPostTypeRepository;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLObjectType.Builder;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static graphql.Scalars.GraphQLString;
import static graphql.Scalars.GraphQLID;
import static graphql.Scalars.GraphQLInt;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLNonNull.nonNull;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphqlTypeComparatorRegistry.BY_NAME_REGISTRY;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {
	@Autowired
	private GraphQLDataFetchers graphQLDataFetchers;

	@Autowired
	private ShPostTypeRepository shPostTypeRepository;

	private GraphQL graphQL;

	private GraphQLSchema loadSchema() {
		List<Map<String, String>> users = Arrays.asList(
				ImmutableMap.of("id", "sid-1", "name", "Siddharatha dhumale 1", "age", "1", "addressId", "address-1"),
				ImmutableMap.of("id", "sid-1", "name", "Siddharatha dhumale 2", "age", "2", "addressId", "address-2"),
				ImmutableMap.of("id", "sid-3", "name", "Siddharatha dhumale 3", "age", "3", "addressId", "address-3"));

		List<Map<String, String>> address = Arrays.asList(
				ImmutableMap.of("id", "address-1", "houseName", "Siddharatha Dhumale House Name 1", "country",
						"Australia"),
				ImmutableMap.of("id", "address-2", "houseName", "Siddharatha Dhumale House Name 2", "country", "UK"),
				ImmutableMap.of("id", "address-3", "houseName", "Siddharatha Dhumale House Name 3", "country",
						"India"));

		GraphQLObjectType userType = newObject().name("User")
				.description("A humanoid creature in the Star Wars universe.")
				.field(newFieldDefinition().name("id").description("The id of the human.").type(nonNull(GraphQLID)))
				.field(newFieldDefinition().name("name").description("The name of the human.").type(GraphQLString))
				.field(newFieldDefinition().name("age")
						.description("The friends of the human, or an empty list if they have none.").type(GraphQLInt))
				.field(newFieldDefinition().name("address").description("Which movies they appear in.")
						.type(GraphQLString))
				.comparatorRegistry(BY_NAME_REGISTRY).build();

		GraphQLObjectType addressType = newObject().name("Address")
				.description("A humanoid creature in the Star Wars universe.")
				.field(newFieldDefinition().name("id").description("The id of the human.").type(nonNull(GraphQLID)))
				.field(newFieldDefinition().name("houseName").description("The name of the human.").type(GraphQLString))
				.field(newFieldDefinition().name("country")
						.description("The friends of the human, or an empty list if they have none.")
						.type(GraphQLString))
				.comparatorRegistry(BY_NAME_REGISTRY).build();

		Builder queryTypeBuilder = newObject().name("QueryType")
				.field(newFieldDefinition().name("userById").type(userType)
						.argument(newArgument().name("id").description("id of the human").type(nonNull(GraphQLString)))
						.dataFetcher(getUserByIdDataFetcher(users)));

		for (ShPostType shPostType : shPostTypeRepository.findAll()) {
			
			String postTypeName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,
					shPostType.getName().toLowerCase().replaceAll("-", "_"));
			Builder builder = newObject().name(postTypeName).description(shPostType.getDescription());

			for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {
				String postTypeAttrName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,
						shPostTypeAttr.getName().toLowerCase().replaceAll("-", "_"));
				builder.field(newFieldDefinition().name(postTypeAttrName).description(shPostTypeAttr.getDescription())
						.type(GraphQLString));
			}

			GraphQLObjectType graphQLObjectType = builder.comparatorRegistry(BY_NAME_REGISTRY).build();
			queryTypeBuilder.field(newFieldDefinition().name(postTypeName + "ById").type(graphQLObjectType)
					.argument(newArgument().name("id").description("id").type(nonNull(GraphQLString)))
					.dataFetcher(getPostTypeByIdDataFetcher(users)));
		}

		GraphQLObjectType queryType = queryTypeBuilder.comparatorRegistry(BY_NAME_REGISTRY).build();

		return GraphQLSchema.newSchema().query(queryType).build();

	}

	DataFetcher getUserByIdDataFetcher(List<Map<String, String>> users) {
		return dataFetchingEnvironment -> {
			String userId = dataFetchingEnvironment.getArgument("id");
			return users.stream().filter(user -> user.get("id").equals(userId)).findFirst().orElse(null);
		};
	}

	DataFetcher getPostTypeByIdDataFetcher(List<Map<String, String>> users) {
		return dataFetchingEnvironment -> {
			String userId = dataFetchingEnvironment.getArgument("id");
			return users.stream().filter(user -> user.get("id").equals(userId)).findFirst().orElse(null);
		};
	}

	DataFetcher getUserDataFetcher(List<Map<String, String>> address) {
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
		GraphQLSchema graphQLSchema = this.loadSchema();
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
