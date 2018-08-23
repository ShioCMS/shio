package com.viglet.shiohara.api.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.document.customer.ShCustomer;
import com.viglet.shiohara.persistence.repository.customer.ShCustomerRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v2/customer")
@Api(tags = "Customer", description = "Customer API")
public class ShCustomerAPI {

	@Autowired
	private ShCustomerRepository repository;

	@ApiOperation(value = "Customer list")
	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShCustomer> shCustomerList() throws Exception {
		repository.deleteAll();

		// save a couple of customers
		repository.save(new ShCustomer("Alice", "Smith"));
		repository.save(new ShCustomer("Bob", "Smith"));

		return repository.findByLastName("Smith");
	}
}
