package com.viglet.shiohara.provider.exchange.otcs.bean.object;

import com.viglet.shiohara.provider.exchange.otcs.bean.link.ShOTCSLinksBean;
import com.viglet.shiohara.provider.exchange.otcs.bean.result.ShOTCSResultsBean;

public class ShOTCSObjectBean {

	private ShOTCSLinksBean links;

	private ShOTCSResultsBean results;

	public ShOTCSLinksBean getLinks() {
		return links;
	}

	public void setLinks(ShOTCSLinksBean links) {
		this.links = links;
	}

	public ShOTCSResultsBean getResults() {
		return results;
	}

	public void setResults(ShOTCSResultsBean results) {
		this.results = results;
	}
	
}
