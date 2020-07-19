package de.uniba.rz.backend;

public class UnknownTicketException extends Exception{
	private static final long serialVersionUID = -4619078398004213294L;

	public UnknownTicketException() {
	}

	public UnknownTicketException(String message) {
		super(message);
	}

	public UnknownTicketException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnknownTicketException(Throwable cause) {
		super(cause);
	}

	public UnknownTicketException(String message, Throwable cause, boolean enableSuppression,
	      boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
