package de.uniba.rz.app;

import java.util.List;

import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketException;
import de.uniba.rz.entities.Type;

/**
 * Defines the actions the GUI is calling to create, list and modify
 * {@link Ticket}s
 */
public interface TicketManagementBackend extends Shutdown,TicketSearchBackend{
	/**
	 * Method to create a new Ticket based on the provided information
	 * 
	 * @param reporter    the name of the reporter
	 * @param topic       the topic of the ticket
	 * @param description a textual description of the problem
	 * @param type        the {@link Type} of the ticket to be created
	 * @param priority    the {@link Priority} of the problem
	 * @return a {@link Ticket} representation of the newly created ticket
	 * @throws TicketException if the creation failed
	 */
	Ticket createNewTicket(String reporter, String topic, String description, Type type, Priority priority)
	      throws TicketException;

	/**
	 * Returns a list of {@link Ticket}s currently available in the system.
	 * 
	 * @return the list of {@link Ticket}s
	 * @throws TicketException if technical problems occur
	 */
	List<Ticket> getAllTickets() throws TicketException;

	/**
	 * Returns a single {@link Ticket} with the given {@code id}
	 * 
	 * @param id the Id of the ticket to be accepted
	 * @return a {@link Ticket} representation of the ticket
	 * @throws TicketException thrown if the ticket with the {@code id} is unknown
	 */
	Ticket getTicketById(int id) throws TicketException;

	/**
	 * Method to accept a Ticket, i.e., changing the {@link Status} to
	 * {@code Status.ACCEPTED}
	 * 
	 * Throws an exception if this status change is not possible (i.e., the current
	 * status is not {@code Status.NEW}) or if the {@code id} refers to a
	 * {@link Ticket} that does not exist.
	 * 
	 * @param id the Id of the ticket to be accepted
	 * @return a {@link Ticket} representation of the modified ticket
	 * @throws TicketException thrown if the status change is not allowed or the
	 *                         ticket with the {@code id} is unknown
	 */
	Ticket acceptTicket(int id) throws TicketException;

	/**
	 * Method to reject a Ticket, i.e., changing the {@link Status} to
	 * {@code Status.REJECTED}
	 * 
	 * Throws an exception if this status change is not possible (i.e., the current
	 * status is not {@code Status.NEW}) or if the {@code id} refers to a
	 * {@link Ticket} that does not exist.
	 * 
	 * @param id the Id of the ticket to be rejected
	 * @return a {@link Ticket} representation of the modified ticket
	 * @throws TicketException thrown if the status change is not allowed or the
	 *                         ticket with the {@code id} is unknown
	 */
	Ticket rejectTicket(int id) throws TicketException;

	/**
	 * Method to close a Ticket, i.e., changing the {@link Status} to
	 * {@code Status.CLOSED}
	 * 
	 * Throws an exception if this status change is not possible (i.e., the current
	 * status is not {@code Status.ACCEPTED}) or if the {@code id} refers to a
	 * {@link Ticket} that does not exist.
	 * 
	 * @param id the Id of the ticket to be accepted
	 * @return a {@link Ticket} representation of the modified ticket
	 * @throws TicketException thrown if the status change is not allowed or the
	 *                         ticket with the {@code id} is unknown
	 */
	Ticket closeTicket(int id) throws TicketException;
}
