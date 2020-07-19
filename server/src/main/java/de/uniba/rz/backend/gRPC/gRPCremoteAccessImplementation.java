package de.uniba.rz.backend.gRPC;

import java.io.IOException;

import de.uniba.rz.backend.RemoteAccess;
import de.uniba.rz.backend.TicketStore;

public class gRPCremoteAccessImplementation implements RemoteAccess{
	gRPCServer server;

	public gRPCremoteAccessImplementation(int port) {
		this.server = new gRPCServer(port);
	}

	@Override
	public void run() {
		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void prepareStartup(TicketStore ticketStore) {
	}

	@Override
	public void shutdown() {
		server.stop();
	}
}
