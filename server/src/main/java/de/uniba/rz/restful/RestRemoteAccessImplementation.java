package de.uniba.rz.restful;

import de.uniba.rz.backend.RemoteAccess;
import de.uniba.rz.backend.TicketStore;

public class RestRemoteAccessImplementation implements RemoteAccess{
	JaxRsServer server = new JaxRsServer();

	@Override
	public void run() {
		server.run();
	}

	@Override
	public void prepareStartup(TicketStore ticketStore) {
	}

	@Override
	public void shutdown() {
	}
}
