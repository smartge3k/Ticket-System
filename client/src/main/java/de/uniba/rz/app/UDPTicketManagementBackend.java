package de.uniba.rz.app;

import java.util.List;
import java.util.UUID;

import de.uniba.rz.app.Shutdown;
import de.uniba.rz.entities.DTO;
import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketException;
import de.uniba.rz.entities.Type;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UDPTicketManagementBackend implements TicketManagementBackend{
	HashMap<Integer, Ticket> localTicketStore = new HashMap<>();
	AtomicInteger nextId;
	private DatagramSocket socket;
	UDPSenderImplementation UDPSender;

	public UDPTicketManagementBackend() {
		this.nextId = new AtomicInteger(1);
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UDPSender = new UDPSenderImplementation(this.socket);
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
		System.out.println("Ticket id at saving Client Side" + id);
		DTO StoreticketDTO = new DTO();
		StoreticketDTO.setTicket(ticket);
		StoreticketDTO.setPacketId(UUID.randomUUID());
		StoreticketDTO.setplay(1);
		Gson json = new GsonBuilder().serializeNulls().create();
		String packet = json.toJson(StoreticketDTO, DTO.class);
		try {
			UDPSender.send(
			      new DatagramPacket(
			            packet.trim().getBytes(), packet.trim().getBytes().length, InetAddress.getLocalHost(), 8888
			      )
			); // Here Port Number
		} catch (IOException e) {
			e.getStackTrace();
		}
		DTO replyDTO = null;
		byte[] receiveData = new byte[1024];
		boolean gotResponse = false;
		while (!gotResponse) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				this.socket.receive(receivePacket);
				gotResponse = true;
			} catch (SocketTimeoutException e) {
			} catch (IOException e) {
			}
			String responseString = new String(receivePacket.getData()).trim();
			replyDTO = json.fromJson(responseString, DTO.class);
		}
		return replyDTO.getTicket();
	}

	@Override
	public List<Ticket> getAllTickets() throws TicketException {
		DTO StoreticketDTO = new DTO();
		StoreticketDTO.setPacketId(UUID.randomUUID());
		StoreticketDTO.setplay(2);
		Gson json = new GsonBuilder().serializeNulls().create();
		String packet = json.toJson(StoreticketDTO, DTO.class);
		try {
			UDPSender.send(
			      new DatagramPacket(
			            packet.trim().getBytes(), packet.trim().getBytes().length, InetAddress.getLocalHost(), 8888
			      )
			); // Here Port Number
		} catch (IOException e) {
			e.getStackTrace();
		}
		DTO replyDTO = new DTO();
		byte[] receiveData = new byte[1024];
		boolean gotResponse = false;
		while (!gotResponse) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				this.socket.receive(receivePacket);
				gotResponse = true;
			} catch (SocketTimeoutException e) {
			} catch (IOException e) {
			}
			String responseString = new String(receivePacket.getData()).trim();
			replyDTO = json.fromJson(responseString, DTO.class);
		}
		return replyDTO.getTickets();
	}

	@Override
	public Ticket getTicketById(int id) throws TicketException {
		System.out.println("ID at getTicketbyID " + id);
		return getTicketByIdInteral(id);
	}

	private Ticket getTicketByIdInteral(int id) throws TicketException {
		DTO request = new DTO();
		request.setplay(3); // 3 to get ticket
		request.setTicketId(id);
		Gson json = new GsonBuilder().serializeNulls().create();
		String requestString = json.toJson(request, DTO.class);
		try {
			UDPSender.send(
			      new DatagramPacket(
			            requestString.trim().getBytes(), requestString.trim().getBytes().length,
			            InetAddress.getLocalHost(), 8888
			      )
			);
		} catch (IOException e) {
		}
		byte[] receiveData = new byte[1024];
		boolean gotResponse = false;
		DTO response = new DTO();
		while (!gotResponse) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				this.socket.receive(receivePacket);
				gotResponse = true;
			} catch (SocketTimeoutException e) {
			} catch (IOException e) {
			}
			json = new GsonBuilder().serializeNulls().create();
			response = json.fromJson(new String(receivePacket.getData()).trim(), DTO.class);
		}
		return response.getTicket();
	}

	@Override
	public Ticket acceptTicket(int id) throws TicketException {
		DTO request = new DTO();
		request.setplay(4); // 4 to change status to accept
		request.setTicketId(id);
		Gson json = new GsonBuilder().serializeNulls().create();
		String requestString = json.toJson(request, DTO.class);
		try {
			UDPSender.send(
			      new DatagramPacket(
			            requestString.trim().getBytes(), requestString.trim().getBytes().length,
			            InetAddress.getLocalHost(), 8888
			      )
			);
		} catch (IOException e) {
		}
		Ticket ticketToModify = getTicketById(id);
		return ticketToModify;
	}

	@Override
	public Ticket rejectTicket(int id) throws TicketException {
		DTO request = new DTO();
		request.setplay(6); // 6 to change status to accept
		request.setTicketId(id);
		Gson json = new GsonBuilder().serializeNulls().create();
		String requestString = json.toJson(request, DTO.class);
		try {
			UDPSender.send(
			      new DatagramPacket(
			            requestString.trim().getBytes(), requestString.trim().getBytes().length,
			            InetAddress.getLocalHost(), 8888
			      )
			);
		} catch (IOException e) {
		}
		Ticket ticketToModify = getTicketById(id);
		return ticketToModify;
	}

	@Override
	public Ticket closeTicket(int id) throws TicketException {
		DTO request = new DTO();
		request.setplay(5); // 5 to change status to accept
		request.setTicketId(id);
		Gson json = new GsonBuilder().serializeNulls().create();
		String requestString = json.toJson(request, DTO.class);
		try {
			UDPSender.send(
			      new DatagramPacket(
			            requestString.trim().getBytes(), requestString.trim().getBytes().length,
			            InetAddress.getLocalHost(), 8888
			      )
			);
		} catch (IOException e) {
		}
		Ticket ticketToModify = getTicketById(id);
		return ticketToModify;
	}
}
