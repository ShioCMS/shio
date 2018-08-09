package com.viglet.shiohara.persistence.document.customer;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ShCustomer {

    @Id
    public String id;

    public String firstName;
    public String lastName;

    public ShCustomer() {}

    public ShCustomer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%s, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

}