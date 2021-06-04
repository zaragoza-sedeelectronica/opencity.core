package org.sede.core.exception;


public class NoVisibleException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public NoVisibleException(String errorMessage) {
        super(errorMessage);
    }
}
