package de.uniba.rz.ui.swing;

import java.util.List;
import java.util.Observable;

import de.uniba.rz.app.TicketManagementBackend;
import de.uniba.rz.app.TicketSearchBackend;
import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketException;
import de.uniba.rz.entities.Type;

public class SwingMainModel extends Observable{
	private TicketManagementBackend backend;

	public SwingMainModel(TicketManagementBackend backend) {
		this.backend = backend;
	}

	public List<Ticket> getAllTickets() throws TicketException {
		return backend.getAllTickets();
	}

	public void tmDataChanged() {
		setChanged();
		notifyObservers();
	}

	public Ticket getTicket(int id) throws TicketException {
		return backend.getTicketById(id);
	}

	public Ticket createNewTicket(String reporter, String topic, String description, Type type, Priority priority)
	      throws TicketException {
		Ticket newTicket = backend.createNewTicket(reporter, topic, description, type, priority);
		setChanged();
		notifyObservers();
		return newTicket;
	}

	public void rejectTicket(int id) throws TicketException {
		backend.rejectTicket(id);
		setChanged();
		notifyObservers();
	}

	public void acceptTicket(int id) throws TicketException {
		backend.acceptTicket(id);
		setChanged();
		notifyObservers();
	}

	public void closeTicket(int id) throws TicketException {
		backend.closeTicket(id);
		setChanged();
		notifyObservers();
	}

	public List<Ticket> searchTicket(String name, Type type) throws TicketException {
		try {
			if (type == null) {
				return backend.getTicketsByName(name);
			} else {
				return backend.getTicketsByNameAndType(name, type);
			}
		} catch (UnsupportedOperationException e) {
			throw new TicketException("No search service registered");
		}
	}
}
