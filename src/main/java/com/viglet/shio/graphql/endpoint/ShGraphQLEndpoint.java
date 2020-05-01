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
package com.viglet.shio.graphql.endpoint;

import graphql.ExecutionResult;
import graphql.spring.web.servlet.ExecutionResultHandler;
import graphql.spring.web.servlet.GraphQLInvocation;
import graphql.spring.web.servlet.GraphQLInvocationData;
import graphql.spring.web.servlet.JsonSerializer;
import graphql.spring.web.servlet.components.GraphQLRequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class ShGraphQLEndpoint {

    @Autowired
    GraphQLInvocation graphQLInvocation;

    @Autowired
    ExecutionResultHandler executionResultHandler;

    @Autowired
    JsonSerializer jsonSerializer;

    @RequestMapping(value = "graphql",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object graphqlPOST(
            @RequestHeader(value = HttpHeaders.CONTENT_TYPE, required = false) String contentType,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "operationName", required = false) String operationName,
            @RequestParam(value = "variables", required = false) String variablesJson,
            @RequestBody(required = false) String body,
            WebRequest webRequest) throws IOException {

        if (body == null) {
            body = "";
        }

        // https://graphql.org/learn/serving-over-http/#post-request
        //
        // A standard GraphQL POST request should use the application/json content type,
        // and include a JSON-encoded body of the following form:
        //
        // {
        //   "query": "...",
        //   "operationName": "...",
        //   "variables": { "myVariable": "someValue", ... }
        // }

        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            GraphQLRequestBody request = jsonSerializer.deserialize(body, GraphQLRequestBody.class);
            if (request.getQuery() == null) {
                request.setQuery("");
            }
            return executeRequest(request.getQuery(), request.getOperationName(), request.getVariables(), webRequest);
        }

        // In addition to the above, we recommend supporting two additional cases:
        //
        // * If the "query" query string parameter is present (as in the GET example above),
        //   it should be parsed and handled in the same way as the HTTP GET case.

        if (query != null) {
            return executeRequest(query, operationName, convertVariablesJson(variablesJson), webRequest);
        }

        // * If the "application/graphql" Content-Type header is present,
        //   treat the HTTP POST body contents as the GraphQL query string.

        if ("application/graphql".equals(contentType)) {
            return executeRequest(body, null, null, webRequest);
        }

        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Could not process GraphQL request");
    }

    @RequestMapping(value = "graphql",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object graphqlGET(
            @RequestParam("query") String query,
            @RequestParam(value = "operationName", required = false) String operationName,
            @RequestParam(value = "variables", required = false) String variablesJson,
            WebRequest webRequest) {

        // https://graphql.org/learn/serving-over-http/#get-request
        //
        // When receiving an HTTP GET request, the GraphQL query should be specified in the "query" query string.
        // For example, if we wanted to execute the following GraphQL query:
        //
        // {
        //   me {
        //     name
        //   }
        // }
        //
        // This request could be sent via an HTTP GET like so:
        //
        // http://myapi/graphql?query={me{name}}
        //
        // Query variables can be sent as a JSON-encoded string in an additional query parameter called "variables".
        // If the query contains several named operations,
        // an "operationName" query parameter can be used to control which one should be executed.

        return executeRequest(query, operationName, convertVariablesJson(variablesJson), webRequest);
    }

    private Map<String, Object> convertVariablesJson(String jsonMap) {
        if (jsonMap == null) {
            return Collections.emptyMap();
        }
        return jsonSerializer.deserialize(jsonMap, Map.class);
    }

    private Object executeRequest(
            String query,
            String operationName,
            Map<String, Object> variables,
            WebRequest webRequest) {
        GraphQLInvocationData invocationData = new GraphQLInvocationData(query, operationName, variables);
        CompletableFuture<ExecutionResult> executionResult = graphQLInvocation.invoke(invocationData, webRequest);
        return executionResultHandler.handleExecutionResult(executionResult);
    }

}