package de.uniba.rz.backend;

import java.util.ArrayList;
import java.util.List;

import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.Type;

public class UDPTicketStoreImplementation implements TicketStore{
	private int nextTicketId = 0;
	private List<Ticket> ticketList = new ArrayList<>();

	@Override
	public Ticket storeNewTicket(String reporter, String topic, String description, Type type, Priority priority) {
		System.out.println("Creating new Ticket from Reporter: " + reporter + " with the topic \"" + topic + "\"");
		nextTicketId = nextTicketId + 1;
		Ticket newTicket = new Ticket(nextTicketId, reporter, topic, description, type, priority);
		ticketList.add(newTicket);
		System.out.println("Ticket id at saving Server Side" + nextTicketId);
		return newTicket;
	}

	@Override
	public void updateTicketStatus(int ticketId, Status newStatus) throws UnknownTicketException, IllegalStateException {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Ticket> getAllTickets() {
		// TODO Auto-generated method stub
		return ticketList;
	}
}
