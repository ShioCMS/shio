package com.viglet.shiohara.persistence.repository.post;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.viglet.shiohara.persistence.model.post.ShPostDoc;

public interface ShPostDocRepository extends MongoRepository<ShPostDoc, String> {

	public Optional<ShPostDoc> findById(String id);

}