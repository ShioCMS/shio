package com.viglet.shiohara.provider.otcs.bean.link;

public class ShOTCSLinksDataBean {

	private ShOTCSLinksDataSelfBean self;
	
	private ShOTCSLinksDataSelfBean next;

	public ShOTCSLinksDataSelfBean getSelf() {
		return self;
	}

	public void setSelf(ShOTCSLinksDataSelfBean self) {
		this.self = self;
	}

	public ShOTCSLinksDataSelfBean getNext() {
		return next;
	}

	public void setNext(ShOTCSLinksDataSelfBean next) {
		this.next = next;
	}

}
