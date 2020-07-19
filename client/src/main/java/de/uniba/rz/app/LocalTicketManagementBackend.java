package de.uniba.rz.app;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketException;
import de.uniba.rz.entities.Type;

public class LocalTicketManagementBackend implements TicketManagementBackend{
	HashMap<Integer, Ticket> localTicketStore = new HashMap<>();
	AtomicInteger nextId;

	public LocalTicketManagementBackend() {
		nextId = new AtomicInteger(1);
	}

	@Override
	public void triggerShutdown() {

	}

	@Override
	public Ticket createNewTicket(String reporter, String topic, String description, Type type, Priority priority) {
		Ticket newTicket = new Ticket(nextId.getAndIncrement(), reporter, topic, description, type, priority);
		return (Ticket) newTicket.clone();
	}

	@Override
	public List<Ticket> getAllTickets() throws TicketException {
		return localTicketStore.entrySet().stream().map(entry -> (Ticket) entry.getValue().clone())
		      .collect(Collectors.toList());
	}

	@Override
	public Ticket getTicketById(int id) throws TicketException {
		if (!localTicketStore.containsKey(id)) {
			throw new TicketException("Ticket ID is unknown");
		}
		return (Ticket) getTicketByIdInteral(id).clone();
	}

	private Ticket getTicketByIdInteral(int id) throws TicketException {
		if (!localTicketStore.containsKey(id)) {
			throw new TicketException("Ticket ID is unknown");
		}
		return localTicketStore.get(id);
	}

	@Override
	public Ticket acceptTicket(int id) throws TicketException {
		Ticket ticketToModify = getTicketByIdInteral(id);
		if (ticketToModify.getStatus() != Status.NEW) {
			throw new TicketException("Can not accept Ticket as it is currently in status " + ticketToModify.getStatus());
		}
		ticketToModify.setStatus(Status.ACCEPTED);
		return (Ticket) ticketToModify.clone();
	}

	@Override
	public Ticket rejectTicket(int id) throws TicketException {
		Ticket ticketToModify = getTicketByIdInteral(id);
		if (ticketToModify.getStatus() != Status.NEW) {
			throw new TicketException("Can not reject Ticket as it is currently in status " + ticketToModify.getStatus());
		}
		ticketToModify.setStatus(Status.REJECTED);
		return (Ticket) ticketToModify.clone();
	}

	@Override
	public Ticket closeTicket(int id) throws TicketException {
		Ticket ticketToModify = getTicketByIdInteral(id);
		if (ticketToModify.getStatus() != Status.ACCEPTED) {
			throw new TicketException("Can not close Ticket as it is currently in status " + ticketToModify.getStatus());
		}
		ticketToModify.setStatus(Status.CLOSED);
		return (Ticket) ticketToModify.clone();
	}
}
