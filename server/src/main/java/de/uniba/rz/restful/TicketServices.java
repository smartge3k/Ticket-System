package de.uniba.rz.restful;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;

public class TicketServices{
	public static final TicketServices instance = new TicketServices();
	RestTicketStoreImplementation ticketStore;

	public TicketServices() {
		ticketStore = new RestTicketStoreImplementation();
	}

	public void saveTicket(Ticket ticket) {
		ticketStore.storeNewTicket(
		      ticket.getReporter(), ticket.getTopic(), ticket.getDescription(), ticket.getType(), ticket.getPriority()
		);
	}

	public List<Ticket> getAlltickets() {
		return ticketStore.getAllTickets();
	}

	public Ticket getTicketbyID(int id) {
		List<Ticket> alltickets = ticketStore.getAllTickets();
		Ticket a = alltickets.get((id - 1));
		return a;
	}

	public void updateTicketToAccept(int ticketId) {
		List<Ticket> alltickets = ticketStore.getAllTickets();
		Ticket a = alltickets.get((ticketId - 1));
		a.setStatus(Status.ACCEPTED);
	}

	public void updateTicketToReject(int ticketId) {
		List<Ticket> alltickets = ticketStore.getAllTickets();
		Ticket a = alltickets.get((ticketId - 1));
		a.setStatus(Status.REJECTED);
	}

	public void updateTicketToClose(int ticketId) {
		List<Ticket> alltickets = ticketStore.getAllTickets();
		Ticket a = alltickets.get((ticketId - 1));
		a.setStatus(Status.CLOSED);
	}

	public List<Ticket> SearchTicket(String name) {
		List<Ticket> listtosend = new ArrayList<Ticket>();
		List<Ticket> alltickets = ticketStore.getAllTickets();
		for (Ticket n : alltickets) {
			if (n.getReporter().equals(name) || n.getDescription().equals(name) || n.getTopic().equals(name)) {
				listtosend.add(n);
			}
		}
		Collections.sort(listtosend, (a1, a2) -> a1.getPriority().compareTo(a2.getPriority()));
		return listtosend;
	}
}
