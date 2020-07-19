package de.uniba.rz.backend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPRemoteAccessImplementation implements RemoteAccess{
	private boolean isActive = false;
	UDPSenderServer udpSender;
	UDPPacketHandler handler;
	DatagramSocket socket = null;
	UDPTicketStoreImplementation store = new UDPTicketStoreImplementation();

	public UDPRemoteAccessImplementation(int port) {
		try {
			this.socket = new DatagramSocket(port);
			this.udpSender = new UDPSenderServer(port, socket);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (isActive) {
			byte[] receivedData = new byte[1024];
			DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
			try {
				socket.receive(receivedPacket);
				System.out.println("Recieved the Packet");
				//				isActive = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.handler = new UDPPacketHandler(receivedPacket, store);
			new Thread(this.handler).start();
		}
	}

	@Override
	public void prepareStartup(TicketStore ticketStore) {
		isActive = true;
	}

	@Override
	public void shutdown() {
		//this will close the server
		this.isActive = false;
		System.out.println("Server Stopped.");
	}
}
