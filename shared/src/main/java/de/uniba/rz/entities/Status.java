package de.uniba.rz.entities;

import java.io.Serializable;

/**
 * Enumeration to describe the Status of a {@link Ticket} or
 * {@link TransferTicket}.
 * 
 * Possible Values:
 * <ul>
 * <li>{@code NEW}</li>
 * <li>{@code ACCEPTED}</li>
 * <li>{@code REJECTED}</li>
 * <li>{@code CLOSED}</li>
 * </ul>
 * 
 */
public enum Status implements Serializable {
	NEW, ACCEPTED, REJECTED, CLOSED
}
