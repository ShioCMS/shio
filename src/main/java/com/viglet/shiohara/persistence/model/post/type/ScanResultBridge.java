package com.viglet.shiohara.persistence.model.post.type;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class ScanResultBridge implements TwoWayFieldBridge {

	@Override
	public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object get(String name, Document document) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String objectToString(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
