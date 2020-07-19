package de.uniba.rz.backend.gRPC;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.uniba.rz.backend.UDPSenderServer;
import de.uniba.rz.entities.DTO;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.io.rpc.TicketRequest;
import de.uniba.rz.io.rpc.TicketResponse;
import de.uniba.rz.io.rpc.TicketServiceGrpc;
import io.grpc.stub.StreamObserver;

class serverrequestImpl extends TicketServiceGrpc.TicketServiceImplBase{
	gRPCticketStoreImplementation ticketStore = new gRPCticketStoreImplementation();

	@Override
	public void serverrequest(TicketRequest request, StreamObserver<TicketResponse> responseObserver) {
		String clientdata = new String(request.getRequestbyclient());
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
			//Write the Response
			TicketResponse response = TicketResponse.newBuilder()
			      .setResponsebyserver("The Size is : " + ticketStore.getAllTickets().size()).build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		} else if (clientRequest.getplay() == 2) { //2 is to get allTickets From Server
			String clientdata2 = new String(request.getRequestbyclient());
			DTO AllTicketRequest = new DTO();
			Gson json2 = new GsonBuilder().serializeNulls().create();
			AllTicketRequest.setTickets(ticketStore.getAllTickets());
			String allticketsreturn = json2.toJson(AllTicketRequest, DTO.class);
			TicketResponse response = TicketResponse.newBuilder().setResponsebyserver(allticketsreturn).build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		} else if (clientRequest.getplay() == 3) { //to get the ticket from the id
			String clientdata3 = new String(request.getRequestbyclient());
			clientRequest = json.fromJson(clientdata3.trim(), DTO.class);
			List<Ticket> alltickets = ticketStore.getAllTickets();
			Ticket a = alltickets.get((clientRequest.getTicketId()) - 1);
			DTO ticketbyidrequest = new DTO();
			ticketbyidrequest.setTicket(a);
			Gson json3 = new GsonBuilder().serializeNulls().create();
			String idticketresponse = json3.toJson(ticketbyidrequest, DTO.class);
			TicketResponse response = TicketResponse.newBuilder().setResponsebyserver(idticketresponse).build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
			System.out.println("Response for get Ticket By id has been Sent");
		} else if (clientRequest.getplay() == 4) { //4 to make Status Accepted
			String clientdata3 = new String(request.getRequestbyclient());
			DTO ticketbyidrequest = new DTO();
			clientRequest = json.fromJson(clientdata3.trim(), DTO.class);
			List<Ticket> alltickets = ticketStore.getAllTickets();
			Ticket a = alltickets.get((clientRequest.getTicketId()) - 1);
			a.setStatus(Status.ACCEPTED);
			ticketbyidrequest.setTicket(a);
			Gson json3 = new GsonBuilder().serializeNulls().create();
			String idticketresponse = json3.toJson(ticketbyidrequest, DTO.class);
			ticketStore.getAllTickets().set((clientRequest.getTicketId()) - 1, a);
			TicketResponse response = TicketResponse.newBuilder().setResponsebyserver(idticketresponse).build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
			//alltickets.get(clientRequest.getTicketId()-1).setStatus(Status.ACCEPTED);
			System.out.println("Ticket Status changed to Accepted");
		} else if (clientRequest.getplay() == 5) {//to make Status Closed
			String clientdata3 = new String(request.getRequestbyclient());
			DTO ticketbyidrequest = new DTO();
			clientRequest = json.fromJson(clientdata3.trim(), DTO.class);
			List<Ticket> alltickets = ticketStore.getAllTickets();
			Ticket a = alltickets.get((clientRequest.getTicketId()) - 1);
			a.setStatus(Status.CLOSED);
			ticketStore.getAllTickets().set((clientRequest.getTicketId()) - 1, a);
			ticketbyidrequest.setTicket(a);
			Gson json3 = new GsonBuilder().serializeNulls().create();
			String idticketresponse = json3.toJson(ticketbyidrequest, DTO.class);
			TicketResponse response = TicketResponse.newBuilder().setResponsebyserver(idticketresponse).build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
			//alltickets.get(clientRequest.getTicketId()-1).setStatus(Status.ACCEPTED);
			System.out.println("Ticket Status changed to Closed");
		} else if (clientRequest.getplay() == 6) { //To make Status Rejected
			String clientdata3 = new String(request.getRequestbyclient());
			DTO ticketbyidrequest = new DTO();
			clientRequest = json.fromJson(clientdata3.trim(), DTO.class);
			List<Ticket> alltickets = ticketStore.getAllTickets();
			Ticket a = alltickets.get((clientRequest.getTicketId()) - 1);
			a.setStatus(Status.REJECTED);
			ticketStore.getAllTickets().set((clientRequest.getTicketId()) - 1, a);
			ticketbyidrequest.setTicket(a);
			Gson json3 = new GsonBuilder().serializeNulls().create();
			String idticketresponse = json3.toJson(ticketbyidrequest, DTO.class);
			TicketResponse response = TicketResponse.newBuilder().setResponsebyserver(idticketresponse).build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
			//alltickets.get(clientRequest.getTicketId()-1).setStatus(Status.ACCEPTED);
			System.out.println("Ticket Status changed to Rejected");
		}
	}
}