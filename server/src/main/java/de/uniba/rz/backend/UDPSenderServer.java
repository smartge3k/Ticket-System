package de.uniba.rz.backend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPSenderServer{
	private static DatagramSocket socket;
	private int port;

	public UDPSenderServer(int serverport, DatagramSocket serversocket) {
		this.socket = serversocket;
		this.port = serverport;
	}

	public static void send(DatagramPacket packet) throws IOException {
		socket.send(packet);
		System.out.println("Response to the Request has been sent!");
	}
}
