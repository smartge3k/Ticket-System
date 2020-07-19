package de.uniba.rz.restful;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.uniba.rz.backend.TicketStore;
import de.uniba.rz.entities.DTO;
import de.uniba.rz.entities.Ticket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Path("tickets")
public class TicketResources{
	private static final Logger logger = Logger.getLogger("Ticket Service");

	@GET
	@Path("/getTicketbyId/{ticketId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getTicketbyId(@PathParam("ticketId") final int ticketId) {
		Ticket ticket = TicketServices.instance.getTicketbyID(ticketId);
		DTO ticketbyidrequest = new DTO();
		ticketbyidrequest.setTicket(ticket);
		Gson json3 = new GsonBuilder().serializeNulls().create();
		String idticketresponse = json3.toJson(ticketbyidrequest, DTO.class);
		return idticketresponse;
	}

	@GET
	@Path("/getallTickets")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getTickets() {
		List<Ticket> ticketlist = TicketServices.instance.getAlltickets();
		DTO AllTicketRequest = new DTO();
		Gson json2 = new GsonBuilder().serializeNulls().create();
		AllTicketRequest.setTickets(ticketlist);
		String allticketsreturn = json2.toJson(AllTicketRequest, DTO.class);
		return allticketsreturn;
	}

	@POST
	@Path("/saveTicket")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String saveTicket(String request) throws URISyntaxException {
		Gson json = new GsonBuilder().serializeNulls().create();
		DTO clientRequest = new DTO();
		clientRequest = json.fromJson(request.trim(), DTO.class);
		TicketServices.instance.saveTicket(clientRequest.getTicket());
		return "Ticket Has Been Saved ";
	}

	@GET
	@Path("/updateTicketToAccept/{ticketId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String updateTicketToAccept(@PathParam("ticketId") final int ticketId) {
		TicketServices.instance.updateTicketToAccept(ticketId);
		return "Ticket has been Updated!";
	}

	@GET
	@Path("/updateTicketToReject/{ticketId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String updateTicketToReject(@PathParam("ticketId") final int ticketId) {
		TicketServices.instance.updateTicketToReject(ticketId);
		return "Ticket has been Updated!";
	}

	@GET
	@Path("/updateTicketToClose/{ticketId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String updateTicketToClose(@PathParam("ticketId") final int ticketId) {
		TicketServices.instance.updateTicketToClose(ticketId);
		return "Ticket has been Updated!";
	}

	@GET
	@Path("/SearchTicketByName/{name}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String SearchTicketByName(@PathParam("name") final String name) {
		List<Ticket> searchedTicktes = TicketServices.instance.SearchTicket(name);
		DTO AllTicketRequest = new DTO();
		Gson json2 = new GsonBuilder().serializeNulls().create();
		AllTicketRequest.setTickets(searchedTicktes);
		String allticketsreturn = json2.toJson(AllTicketRequest, DTO.class);
		return allticketsreturn;
	}
}
