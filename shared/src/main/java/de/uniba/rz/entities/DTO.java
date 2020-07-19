package de.uniba.rz.entities;

import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;

import java.util.List;
import java.util.UUID;
public class DTO {
	    private Ticket ticket;
	    private UUID packetId;
	    private int play;
	    private int ticketId;
	    private Status status;
	    private List<Ticket> tickets;

	    public int getplay() {
	        return play;
	    }

	    public void setplay(int Play) {
	        this.play = Play;
	    }

	    public UUID getPacketId() {
	        return packetId;
	    }

	    public void setPacketId(UUID packetId) {
	        this.packetId = packetId;
	    }

	    public Ticket getTicket() {
	        return ticket;
	    }

	    public void setTicket(Ticket ticket) {
	        this.ticket = ticket;
	    }
	    public List<Ticket> getTickets() {
	        return tickets;
	    }

	    public void setTickets(List<Ticket> tickets) {
	        this.tickets = tickets;
	    }
	    /**
	     * @return the ticketId
	     */
	    public int getTicketId() {
	        return ticketId;
	    }

	    /**
	     * @param ticketId the ticketId to set
	     */
	    public void setTicketId(int ticketId) {
	        this.ticketId = ticketId;
	    }

	    /**
	     * @return the newStatus
	     */
	    public Status getStatus() {
	        return status;
	    }

	    /**
	     * @param newStatus the newStatus to set
	     */
	    public void setStatus(Status Status) {
	        this.status = Status;
	    }

	    public String echo() {
	        return "packet Id: " + packetId + " ~~~~~ action: " + status;
	    }
}
