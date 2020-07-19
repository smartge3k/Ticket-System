package de.uniba.rz.app.gRPC;

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
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketException;
import de.uniba.rz.entities.Type;

public class gRPCticketManagmentBackend implements TicketManagementBackend{
	gRPCClient client;
	HashMap<Integer, Ticket> localTicketStore = new HashMap<>();
	AtomicInteger nextId;

	public gRPCticketManagmentBackend(String host, int port) {
		this.client = new gRPCClient(host, port);
		nextId = new AtomicInteger(1);
	}

	@Override
	public void triggerShutdown() {
		// TODO Auto-generated method stub
	}

	@Override
	public Ticket createNewTicket(String reporter, String topic, String description, Type type, Priority priority)
	      throws TicketException {
		int id = nextId.getAndIncrement();
		Ticket ticket = new Ticket(id, reporter, topic, description, type, priority);
		localTicketStore.put(id, ticket);
		DTO StoreticketDTO = new DTO();
		StoreticketDTO.setTicket(ticket);
		StoreticketDTO.setplay(1);
		Gson json = new GsonBuilder().serializeNulls().create();
		String packet = json.toJson(StoreticketDTO, DTO.class);
		String resp = client.requestToServer(packet);
		System.out.println(resp);
		return ticket;
	}

	@Override
	public List<Ticket> getAllTickets() throws TicketException {
		DTO StoreticketDTO = new DTO();
		StoreticketDTO.setplay(2);
		Gson json = new GsonBuilder().serializeNulls().create();
		String packet = json.toJson(StoreticketDTO, DTO.class);
		String resp = client.requestToServer(packet);
		String responseString = new String(resp).trim();
		DTO replyDTO = json.fromJson(responseString, DTO.class);
		return replyDTO.getTickets();
	}

	@Override
	public Ticket getTicketById(int id) throws TicketException {
		System.out.println("ID at getTicketbyID " + id);
		return getTicketByIdInteral(id);
	}

	private Ticket getTicketByIdInteral(int id) throws TicketException {
		DTO StoreticketDTO = new DTO();
		StoreticketDTO.setplay(3);
		StoreticketDTO.setTicketId(id);
		Gson json = new GsonBuilder().serializeNulls().create();
		String packet = json.toJson(StoreticketDTO, DTO.class);
		String resp = client.requestToServer(packet);
		String responseString = new String(resp).trim();
		DTO replyDTO = json.fromJson(responseString, DTO.class);
		return replyDTO.getTicket();
	}

	@Override
	public Ticket acceptTicket(int id) throws TicketException {
		DTO StoreticketDTO = new DTO();
		StoreticketDTO.setplay(4);
		StoreticketDTO.setTicketId(id);
		Gson json = new GsonBuilder().serializeNulls().create();
		String packet = json.toJson(StoreticketDTO, DTO.class);
		String resp = client.requestToServer(packet);
		String responseString = new String(resp).trim();
		DTO replyDTO = json.fromJson(responseString, DTO.class);
		return replyDTO.getTicket();
	}

	@Override
	public Ticket rejectTicket(int id) throws TicketException {
		DTO StoreticketDTO = new DTO();
		StoreticketDTO.setplay(6);
		StoreticketDTO.setTicketId(id);
		Gson json = new GsonBuilder().serializeNulls().create();
		String packet = json.toJson(StoreticketDTO, DTO.class);
		String resp = client.requestToServer(packet);
		String responseString = new String(resp).trim();
		DTO replyDTO = json.fromJson(responseString, DTO.class);
		return replyDTO.getTicket();
	}

	@Override
	public Ticket closeTicket(int id) throws TicketException {
		DTO StoreticketDTO = new DTO();
		StoreticketDTO.setplay(5);
		StoreticketDTO.setTicketId(id);
		Gson json = new GsonBuilder().serializeNulls().create();
		String packet = json.toJson(StoreticketDTO, DTO.class);
		String resp = client.requestToServer(packet);
		String responseString = new String(resp).trim();
		DTO replyDTO = json.fromJson(responseString, DTO.class);
		return replyDTO.getTicket();
	}
}
