package de.uniba.rz.backend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.uniba.rz.entities.DTO;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;

public class UDPPacketHandler implements Runnable{
	private DatagramPacket clientpacket;
	private DatagramPacket replypacket;
	UDPTicketStoreImplementation ticketStore;
	private InetAddress clientIp;
	private int port;

	public UDPPacketHandler(DatagramPacket packet, UDPTicketStoreImplementation store) {
		this.clientpacket = packet;
		this.ticketStore = store;
		this.clientIp = packet.getAddress();
		this.port = packet.getPort();
	}

	@Override
	public void run() {
		String clientdata = new String(clientpacket.getData());
		Gson json = new GsonBuilder().serializeNulls().create();
		DTO clientRequest = new DTO();
		clientRequest = json.fromJson(clientdata.trim(), DTO.class);
		if (clientRequest.getplay() == 1) { //1 is to save ticket
			//This will handle the request for the ticket saving process
			ticketStore.storeNewTicket(
			      clientRequest.getTicket().getReporter(), clientRequest.getTicket().getTopic(),
			      clientRequest.getTicket().getDescription(), clientRequest.getTicket().getType(),
			      clientRequest.getTicket().getPriority()
			);
			try {
				UDPSenderServer.send(
				      new DatagramPacket(
				            clientpacket.getData(), clientpacket.getData().length, clientpacket.getAddress(),
				            clientpacket.getPort()
				      )
				);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		} else if (clientRequest.getplay() == 2) {
			String clientdata2 = new String(clientpacket.getData());
			DTO AllTicketRequest = new DTO();
			Gson json2 = new GsonBuilder().serializeNulls().create();
			AllTicketRequest.setTickets(ticketStore.getAllTickets());
			String allticketsreturn = json2.toJson(AllTicketRequest, DTO.class);
			try {
				UDPSenderServer.send(
				      new DatagramPacket(
				            allticketsreturn.getBytes(), allticketsreturn.getBytes().length, clientpacket.getAddress(),
				            clientpacket.getPort()
				      )
				);
				System.out.println("Reply Sent for GetAllTickets");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (clientRequest.getplay() == 3) { //to get the ticket from the id
			String clientdata3 = new String(clientpacket.getData());
			DTO ticketbyidrequest = new DTO();
			clientRequest = json.fromJson(clientdata3.trim(), DTO.class);
			List<Ticket> alltickets = ticketStore.getAllTickets();
			Ticket a = alltickets.get((clientRequest.getTicketId()) - 1);
			ticketbyidrequest.setTicket(a);
			System.out.println("ID at serverside for our ticket Request: " + clientRequest.getTicketId());
			Gson json3 = new GsonBuilder().serializeNulls().create();
			String idticketresponse = json3.toJson(ticketbyidrequest, DTO.class);
			try {
				UDPSenderServer.send(
				      new DatagramPacket(
				            idticketresponse.getBytes(), idticketresponse.getBytes().length, clientpacket.getAddress(),
				            clientpacket.getPort()
				      )
				);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Response for get Ticket By id has been Sent");
		} else if (clientRequest.getplay() == 4) { //4 to make Status Accepted
			String clientdata3 = new String(clientpacket.getData());
			DTO ticketbyidrequest = new DTO();
			clientRequest = json.fromJson(clientdata3.trim(), DTO.class);
			List<Ticket> alltickets = ticketStore.getAllTickets();
			Ticket a = alltickets.get((clientRequest.getTicketId()) - 1);
			a.setStatus(Status.ACCEPTED);
			//alltickets.get(clientRequest.getTicketId()-1).setStatus(Status.ACCEPTED);
			ticketStore.getAllTickets().set((clientRequest.getTicketId()) - 1, a);
			System.out.println("Ticket Status changed to Accepted");
		} else if (clientRequest.getplay() == 5) {//to make Status Closed
			String clientdata3 = new String(clientpacket.getData());
			DTO ticketbyidrequest = new DTO();
			clientRequest = json.fromJson(clientdata3.trim(), DTO.class);
			List<Ticket> alltickets = ticketStore.getAllTickets();
			Ticket a = alltickets.get((clientRequest.getTicketId()) - 1);
			a.setStatus(Status.CLOSED);
			//alltickets.get(clientRequest.getTicketId()-1).setStatus(Status.ACCEPTED);
			ticketStore.getAllTickets().set((clientRequest.getTicketId()) - 1, a);
			System.out.println("Ticket Status changed to Accepted");
		} else if (clientRequest.getplay() == 6) { //To make Status Rejected
			String clientdata3 = new String(clientpacket.getData());
			DTO ticketbyidrequest = new DTO();
			clientRequest = json.fromJson(clientdata3.trim(), DTO.class);
			List<Ticket> alltickets = ticketStore.getAllTickets();
			Ticket a = alltickets.get((clientRequest.getTicketId()) - 1);
			a.setStatus(Status.REJECTED);
			//alltickets.get(clientRequest.getTicketId()-1).setStatus(Status.ACCEPTED);
			ticketStore.getAllTickets().set((clientRequest.getTicketId()) - 1, a);
			System.out.println("Ticket Status changed to Accepted");
		}
	}
}
