package de.uniba.rz.app;

import java.util.List;

import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketException;
import de.uniba.rz.entities.Type;

/**
 * Defines the actions the GUI is calling to search for {@link Ticket}s
 */
public interface TicketSearchBackend{
	/**
	 * Method to search for a Ticket based on the provided information
	 * 
	 * @param name The name of the ticket to be searched
	 * @return a list of {@link Ticket} that fulfill the requested attributes
	 * @throws TicketException if something failed
	 */
	default List<Ticket> getTicketsByName(String name) throws TicketException {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Method to search for a Ticket based on the provided information
	 * 
	 * @param name The name of the ticket to be searched
	 * @param type The {@link Type} of the ticket to be searched
	 * @return a list of {@link Ticket} that fulfill the requested attributes
	 * @throws TicketException if something failed
	 */
	default List<Ticket> getTicketsByNameAndType(String name, Type type) throws TicketException {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}