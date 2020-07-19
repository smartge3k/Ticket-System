package de.uniba.rz.restful;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.logging.Logger;
import java.util.stream.Collectors;

import de.uniba.rz.app.TicketManagementBackend;
import de.uniba.rz.entities.DTO;
import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketException;
import de.uniba.rz.entities.Type;

public class restTicketbackendManagement implements TicketManagementBackend{
	private static final Logger logger = Logger.getLogger("Ticket Service");
	Client client = ClientBuilder.newClient(new ClientConfig().register(Logger.class));
	HashMap<Integer, Ticket> localTicketStore = new HashMap<>();
	AtomicInteger nextId;

	public restTicketbackendManagement() {
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
		Gson json = new GsonBuilder().serializeNulls().create();
		String packet = json.toJson(StoreticketDTO, DTO.class);
		WebTarget webTarget = client.target("http://localhost:9999/tickets").path("saveTicket");
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(packet, (MediaType.APPLICATION_JSON)));
		String Response = response.readEntity(String.class);
		System.out.println("Response is: " + Response);
		return ticket;
	}

	@Override
	public List<Ticket> getAllTickets() throws TicketException {
		// Client client = ClientBuilder.newClient( new ClientConfig().register(
		// Gson.class) );
		WebTarget webTarget = client.target("http://localhost:9999/tickets").path("getallTickets");
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		String reply = response.readEntity(String.class);
		Gson json = new GsonBuilder().serializeNulls().create();
		DTO replyDTO = new DTO();
		replyDTO = json.fromJson(reply, DTO.class);
		return replyDTO.getTickets();
	}

	@Override
	public Ticket getTicketById(int id) throws TicketException {
		WebTarget webTarget = client.target("http://localhost:9999/tickets").path("getTicketbyId")
		      .path(Integer.toString(id));
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		String ticket = response.readEntity(String.class);
		Gson json = new GsonBuilder().serializeNulls().create();
		DTO ticketbyid = json.fromJson(new String(ticket).trim(), DTO.class);
		return ticketbyid.getTicket();
	}

	private Ticket getTicketByIdInteral(int id) throws TicketException {
		if (!localTicketStore.containsKey(id)) {
			throw new TicketException("Ticket ID is unknown");
		}
		return localTicketStore.get(id);
	}

	@Override
	public Ticket acceptTicket(int id) throws TicketException {
		WebTarget webTarget = client.target("http://localhost:9999/tickets").path("updateTicketToAccept")
		      .path(Integer.toString(id));
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		String ticket = response.readEntity(String.class);
		return getTicketById(id);
	}

	@Override
	public Ticket rejectTicket(int id) throws TicketException {
		WebTarget webTarget = client.target("http://localhost:9999/tickets").path("updateTicketToReject")
		      .path(Integer.toString(id));
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		String ticket = response.readEntity(String.class);
		return getTicketById(id);
	}

	@Override
	public Ticket closeTicket(int id) throws TicketException {
		WebTarget webTarget = client.target("http://localhost:9999/tickets").path("updateTicketToClose")
		      .path(Integer.toString(id));
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		String ticket = response.readEntity(String.class);
		return getTicketById(id);
	}

	@Override
	public List<Ticket> getTicketsByName(String name) throws TicketException {
		WebTarget webTarget = client.target("http://localhost:9999/tickets").path("SearchTicketByName").path(name);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		String reply = response.readEntity(String.class);
		Gson json = new GsonBuilder().serializeNulls().create();
		DTO replyDTO = new DTO();
		replyDTO = json.fromJson(reply, DTO.class);
		return replyDTO.getTickets();
	}

	@Override
	public List<Ticket> getTicketsByNameAndType(String name, Type type) throws TicketException {
		return TicketManagementBackend.super.getTicketsByName(name);
	}
}
