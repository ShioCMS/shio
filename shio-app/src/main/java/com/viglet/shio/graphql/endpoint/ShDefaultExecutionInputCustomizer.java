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

import graphql.ExecutionInput;
import graphql.spring.web.servlet.ExecutionInputCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.concurrent.CompletableFuture;

@Component
public class ShDefaultExecutionInputCustomizer implements ExecutionInputCustomizer {

	@Override
	public CompletableFuture<ExecutionInput> customizeExecutionInput(ExecutionInput executionInput,
			WebRequest webRequest) {
		return CompletableFuture.completedFuture(executionInput);
	}
}