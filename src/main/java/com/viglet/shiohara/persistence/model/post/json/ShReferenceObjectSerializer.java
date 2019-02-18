package com.viglet.shiohara.persistence.model.post.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;

public class ShReferenceObjectSerializer extends JsonSerializer<ShObject> {

	@Override
	public void serialize(ShObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		
		if (value instanceof ShPost) {
			ShPost shPostMod = new ShPost();
			
			ShPost shPost = (ShPost) value;
			
		/*	shPost.setDate(date);
			shPost.setFurl(furl);
			shPost.setId(id);
			shPost.setModifiedDate(modifiedDate);
			shPost.setModifier(modifier);
			shPost.setObjectType(objectType);
			shPost.setOwner(owner);
			shPost.set
			shPostMod.setTitle("Alexandre");
		*/	
			for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
				shPostAttr.setShPost(null);
				shPostAttr.setShParentRelatorItem(null);
				shPostMod.addShPostAttr(shPostAttr);
			}
			gen.writeObject(shPostMod);
		} else {
			gen.writeObject(value);
		}

	}
}