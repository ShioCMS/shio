package com.viglet.shiohara.persistence.repository.customer;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.viglet.shiohara.persistence.document.customer.ShCustomer;

public interface ShCustomerRepository extends MongoRepository<ShCustomer, String> {

    public ShCustomer findByFirstName(String firstName);
    public List<ShCustomer> findByLastName(String lastName);

}