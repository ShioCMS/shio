package com.viglet.shio.graphql;

import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class GraphQLDataFetchers {

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
}
