package com.viglet.shiohara.bean.security;

public class ShSecurityBean {

	private ShConsoleSecurityBean console;
	
	private ShPageSecurityBean page;

	public ShConsoleSecurityBean getConsole() {
		return console;
	}

	public void setConsole(ShConsoleSecurityBean console) {
		this.console = console;
	}

	public ShPageSecurityBean getPage() {
		return page;
	}

	public void setPage(ShPageSecurityBean page) {
		this.page = page;
	}

}
