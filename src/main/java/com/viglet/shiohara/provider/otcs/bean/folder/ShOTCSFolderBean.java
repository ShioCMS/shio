package com.viglet.shiohara.provider.otcs.bean.folder;

import java.util.List;

import com.viglet.shiohara.provider.otcs.bean.collection.ShOTCSCollectionBean;
import com.viglet.shiohara.provider.otcs.bean.link.ShOTCSLinksBean;
import com.viglet.shiohara.provider.otcs.bean.result.ShOTCSResultsBean;

public class ShOTCSFolderBean {

	private ShOTCSCollectionBean collection;

	private ShOTCSLinksBean links;

	private List<ShOTCSResultsBean> results;

	public ShOTCSCollectionBean getCollection() {
		return collection;
	}

	public void setCollection(ShOTCSCollectionBean collection) {
		this.collection = collection;
	}

	public ShOTCSLinksBean getLinks() {
		return links;
	}

	public void setLinks(ShOTCSLinksBean links) {
		this.links = links;
	}

	public List<ShOTCSResultsBean> getResults() {
		return results;
	}

	public void setResults(List<ShOTCSResultsBean> results) {
		this.results = results;
	}

	
}
