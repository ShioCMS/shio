package com.viglet.shio.utils;

public class ShUtilsException extends Exception {

	private static final long serialVersionUID = 1L;

	public ShUtilsException() {
		super();
	}

	public ShUtilsException(String message) {
		super(message);
	}

	public ShUtilsException(String message, Throwable cause) {
		super(message, cause);
	}

	public ShUtilsException(Throwable cause) {
		super(cause);
	}
}