/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

package com.viglet.shiohara.api.ecommerce;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.ecommerce.ShEcomOrder;
import com.viglet.shiohara.persistence.repository.ecommerce.ShEcomOrderRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v2/ecom/order")
@Api(tags = "Order", description = "Order API")
public class ShEcomOrderAPI {

	@Autowired
	private ShEcomOrderRepository shEcomOrderRepository;

	@ApiOperation(value = "Order List")
	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShEcomOrder> shEcomOrderList() throws Exception {
		return shEcomOrderRepository.findAll();
	}

	@ApiOperation(value = "Show a Order")
	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShEcomOrder ShEcomOrderGet(@PathVariable String id) throws Exception {
		if (shEcomOrderRepository.findById(id).isPresent()) {
			return shEcomOrderRepository.findById(id).get();
		} else {
			return null;
		}
	}

	@ApiOperation(value = "Update a Order")
	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShEcomOrder ShEcomOrderUpdate(@PathVariable String id, @RequestBody ShEcomOrder shEcomOrder)
			throws Exception {

		ShEcomOrder shEcomOrderEdit = shEcomOrderRepository.findById(id).orElse(null);

		shEcomOrderEdit.setDescription(shEcomOrder.getDescription());

		shEcomOrderRepository.saveAndFlush(shEcomOrderEdit);

		return shEcomOrderEdit;
	}

	@Transactional
	@ApiOperation(value = "Delete a Order")
	@DeleteMapping("/{id}")
	public boolean ShEcomOrderDelete(@PathVariable String id) throws Exception {
		shEcomOrderRepository.findById(id).ifPresent(new Consumer<ShEcomOrder>() {
			@Override
			public void accept(ShEcomOrder shEcomOrder) {
				shEcomOrderRepository.delete(shEcomOrder);
			}
		});
		return true;
	}

	@ApiOperation(value = "Create a Order")
	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShEcomOrder ShEcomOrderAdd(@RequestBody ShEcomOrder shEcomOrder) throws Exception {
		shEcomOrderRepository.save(shEcomOrder);

		return shEcomOrder;

	}
}
