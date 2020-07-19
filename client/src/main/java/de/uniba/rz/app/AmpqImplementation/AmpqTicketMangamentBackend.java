package de.uniba.rz.app.AmpqImplementation;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.uniba.rz.app.TicketManagementBackend;
import de.uniba.rz.entities.DTO;
import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketException;
import de.uniba.rz.entities.Type;

public class AmpqTicketMangamentBackend implements TicketManagementBackend{
	AmpqQueueSender sender; // new AmpqQueueSender("localhost","IdsAssignmentQueue");
	HashMap<Integer, Ticket> localTicketStore = new HashMap<>();
	AtomicInteger nextId;

	public AmpqTicketMangamentBackend(String host, String Queue) {
		this.sender = new AmpqQueueSender(host, Queue);
		nextId = new AtomicInteger(1);
	}

	@Override
	public void triggerShutdown() {
		// local implementation is in memory only - no need to close connections
		// and free resources
	}

	@Override
	public Ticket createNewTicket(String reporter, String topic, String description, Type type, Priority priority) {
		int id = nextId.getAndIncrement();
		Ticket ticket = new Ticket(id, reporter, topic, description, type, priority);
		localTicketStore.put(id, ticket);
		DTO StoreticketDTO = new DTO();
		StoreticketDTO.setTicket(ticket);
		StoreticketDTO.setPacketId(UUID.randomUUID());
		StoreticketDTO.setplay(1);
		Gson json = new GsonBuilder().serializeNulls().create();
		String packet = json.toJson(StoreticketDTO, DTO.class);
		DTO a = sender.sendMessage(packet);
		return a.getTicket();
	}

	@Override
	public List<Ticket> getAllTickets() throws TicketException {
		DTO StoreticketDTO = new DTO();
		StoreticketDTO.setPacketId(UUID.randomUUID());
		StoreticketDTO.setplay(2);
		Gson json = new GsonBuilder().serializeNulls().create();
		String packet = json.toJson(StoreticketDTO, DTO.class);
		DTO reply = sender.sendMessage(packet);
		return reply.getTickets();
		// return localTicketStore.entrySet().stream().map(entry -> (Ticket)
		// entry.getValue().clone()).collect(Collectors.toList());
	}

	@Override
	public Ticket getTicketById(int id) throws TicketException {
		System.out.println("ID at getTicketbyID " + id);
		return getTicketByIdInteral(id);
	}

	private Ticket getTicketByIdInteral(int id) throws TicketException {
		DTO StoreticketDTO = new DTO();
		StoreticketDTO.setPacketId(UUID.randomUUID());
		StoreticketDTO.setplay(3);
		StoreticketDTO.setTicketId(id);
		Gson json = new GsonBuilder().serializeNulls().create();
		String packet = json.toJson(StoreticketDTO, DTO.class);
		DTO reply = sender.sendMessage(packet);
		System.out
		      .println("The Ticket Details" + reply.getTicket().getDescription() + "  " + reply.getTicket().getTopic());
		return reply.getTicket();
		// return localTicketStore.get(id);
	}

	@Override
	public Ticket acceptTicket(int id) throws TicketException {
		DTO StoreticketDTO = new DTO();
		StoreticketDTO.setPacketId(UUID.randomUUID());
		StoreticketDTO.setplay(4);
		StoreticketDTO.setTicketId(id);
		Gson json = new GsonBuilder().serializeNulls().create();
		String packet = json.toJson(StoreticketDTO, DTO.class);
		DTO reply = sender.sendMessage(packet);
		return reply.getTicket();
	}

	@Override
	public Ticket rejectTicket(int id) throws TicketException {
		DTO StoreticketDTO = new DTO();
		StoreticketDTO.setPacketId(UUID.randomUUID());
		StoreticketDTO.setplay(6);
		StoreticketDTO.setTicketId(id);
		Gson json = new GsonBuilder().serializeNulls().create();
		String packet = json.toJson(StoreticketDTO, DTO.class);
		DTO reply = sender.sendMessage(packet);
		return reply.getTicket();
	}

	@Override
	public Ticket closeTicket(int id) throws TicketException {
		DTO StoreticketDTO = new DTO();
		StoreticketDTO.setPacketId(UUID.randomUUID());
		StoreticketDTO.setplay(5);
		StoreticketDTO.setTicketId(id);
		Gson json = new GsonBuilder().serializeNulls().create();
		String packet = json.toJson(StoreticketDTO, DTO.class);
		DTO reply = sender.sendMessage(packet);
		return reply.getTicket();
	}
}
