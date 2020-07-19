package de.uniba.rz.backend.AmpqImplementation;

import java.util.Scanner;

import de.uniba.rz.backend.RemoteAccess;
import de.uniba.rz.backend.TicketStore;

public class AmpqRemoteAccessImplementation implements RemoteAccess{
	QueueReceiver serverPull;
	QueueReceiver serverPush;

	public AmpqRemoteAccessImplementation(String host, String queue) {
		this.serverPull = new PullQueueReceiver(host, queue);
		this.serverPush = new PushQueueReceiver(host, queue);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		serverPull.startServer();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Hit Enter to stop the server.");
		scanner.nextLine();
		scanner.close();
		serverPull.stopServer();
		// Wait for server's termination
		try {
			serverPull.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void prepareStartup(TicketStore ticketStore) {
		// TODO Auto-generated method stub
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
	}
}
