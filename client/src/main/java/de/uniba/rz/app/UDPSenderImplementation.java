package de.uniba.rz.app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPSenderImplementation{
	private DatagramSocket socket;

	public UDPSenderImplementation() {
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public UDPSenderImplementation(DatagramSocket serverSocket) {
		this.socket = serverSocket;
	}

	public void send(DatagramPacket packet) {
		try {
			socket.send(packet);
			System.out.println("Packet Sent from Client");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
