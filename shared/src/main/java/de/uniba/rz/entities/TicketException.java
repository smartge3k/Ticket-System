package de.uniba.rz.entities;

/**
 * Exception to be thrown if a problem occurs during ticket handling.
 * 
 */
public class TicketException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Empty Exception.
	 */
	public TicketException() {
		super();
	}

	/**
	 * Exception with description.
	 * 
	 * @param message
	 *            the descriptive message
	 */
	public TicketException(String message) {
		super(message);
	}

	/**
	 * Exception with description and cause
	 * 
	 * @param message
	 *            the descriptive message
	 * @param cause
	 *            wrapped Throwable cause
	 */
	public TicketException(String message, Throwable cause) {
		super(message, cause);
	}
}
