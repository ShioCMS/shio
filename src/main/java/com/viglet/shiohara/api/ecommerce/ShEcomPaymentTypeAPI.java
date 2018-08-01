package com.viglet.shiohara.api.ecommerce;

import java.util.Date;
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
import com.viglet.shiohara.persistence.model.ecommerce.ShEcomPaymentType;
import com.viglet.shiohara.persistence.repository.ecommerce.ShEcomPaymentTypeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v2/ecom/payment/type")
@Api(tags = "Payment Type", description = "Payment Type API")
public class ShEcomPaymentTypeAPI {

	@Autowired
	private ShEcomPaymentTypeRepository shEcomPaymentTypeRepository;

	@ApiOperation(value = "Payment Type list")
	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShEcomPaymentType> shEcomPaymentTypeList() throws Exception {
		return shEcomPaymentTypeRepository.findAll();
	}

	@ApiOperation(value = "Show a Payment Type")
	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShEcomPaymentType ShEcomPaymentTypeGet(@PathVariable String id) throws Exception {
		return shEcomPaymentTypeRepository.findById(id).get();
	}

	@ApiOperation(value = "Update a Payment Type")
	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShEcomPaymentType ShEcomPaymentTypeUpdate(@PathVariable String id,
			@RequestBody ShEcomPaymentType shEcomPaymentType) throws Exception {

		ShEcomPaymentType shEcomPaymentTypeEdit = shEcomPaymentTypeRepository.findById(id).get();

		shEcomPaymentTypeEdit.setDate(new Date());
		shEcomPaymentTypeEdit.setClassName(shEcomPaymentType.getClassName());
		shEcomPaymentTypeEdit.setDescription(shEcomPaymentType.getDescription());
		shEcomPaymentTypeEdit.setName(shEcomPaymentType.getName());
		shEcomPaymentTypeEdit.setSettingPath(shEcomPaymentType.getSettingPath());

		shEcomPaymentTypeRepository.saveAndFlush(shEcomPaymentTypeEdit);

		return shEcomPaymentTypeEdit;
	}

	@Transactional
	@ApiOperation(value = "Delete a Payment Type")
	@DeleteMapping("/{id}")
	public boolean ShEcomPaymentTypeDelete(@PathVariable String id) throws Exception {
		shEcomPaymentTypeRepository.findById(id).ifPresent(new Consumer<ShEcomPaymentType>() {
			@Override
			public void accept(ShEcomPaymentType shEcomPaymentType) {
				shEcomPaymentTypeRepository.delete(shEcomPaymentType);
			}
		});
		return true;
	}

	@ApiOperation(value = "Create a Payment Type")
	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShEcomPaymentType ShEcomPaymentTypeAdd(@RequestBody ShEcomPaymentType shEcomPaymentType) throws Exception {
		shEcomPaymentType.setDate(new Date());
		shEcomPaymentTypeRepository.save(shEcomPaymentType);

		return shEcomPaymentType;

	}
}
