package de.uniba.rz.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import de.uniba.rz.backend.AmpqImplementation.AmpqRemoteAccessImplementation;
import de.uniba.rz.backend.AmpqImplementation.AmpqTicketStoreImplementation;
import de.uniba.rz.backend.gRPC.gRPCremoteAccessImplementation;
import de.uniba.rz.backend.gRPC.gRPCticketStoreImplementation;
import de.uniba.rz.restful.RestRemoteAccessImplementation;
import de.uniba.rz.restful.RestTicketStoreImplementation;

public class TicketServerMain{
	public static void main(String[] args) throws IOException, NamingException {
		TicketStore ticketStore = null;
		List<RemoteAccess> remoteAccessImplementations = getAvailableRemoteAccessImplementations(args);
		// Starting remote access implementations:
		for (RemoteAccess implementation : remoteAccessImplementations) {
			switch (args[0]) {
			case "udp":
				System.out.println("Using UDP");
				ticketStore = new UDPTicketStoreImplementation();
				implementation.prepareStartup(ticketStore);
				new Thread(implementation).start();
				break;
			case "ampq":
				ticketStore = new AmpqTicketStoreImplementation();
				implementation.prepareStartup(ticketStore);
				new Thread(implementation).start();
				break;
			case "grpc":
				ticketStore = new gRPCticketStoreImplementation();
				implementation.prepareStartup(ticketStore);
				new Thread(implementation).start();
				break;
			case "rest":
				ticketStore = new RestTicketStoreImplementation();
				implementation.prepareStartup(ticketStore);
				new Thread(implementation).start();
				break;
			default:
				System.out.println("Unknown Argument Using UDP Implementation");
				ticketStore = new UDPTicketStoreImplementation();
				implementation.prepareStartup(ticketStore);
				new Thread(implementation).start();
			}
		}
		try (BufferedReader shutdownReader = new BufferedReader(new InputStreamReader(System.in))) {
			System.out.println("Press enter to shutdown system.");
			shutdownReader.readLine();
			System.out.println("Shutting down...");
			// Shuttung down all remote access implementations
			for (RemoteAccess implementation : remoteAccessImplementations) {
				implementation.shutdown();
			}
			System.out.println("completed. Bye!");
		}
	}

	private static List<RemoteAccess> getAvailableRemoteAccessImplementations(String[] args) {
		List<RemoteAccess> implementations = new ArrayList<>();
		switch (args[0]) {
		case "udp":
			System.out.println(" Using UDP Remote Access Implementation");
			implementations.add(new UDPRemoteAccessImplementation(Integer.parseInt(args[1])));
			break;
		case "ampq":
			System.out.println(" Using Ampq Remote Access Implementation");
			implementations.add(new AmpqRemoteAccessImplementation(args[1], args[2]));
			break;
		case "grpc":
			System.out.println(" Using gRPC Remote Access Implementation");
			implementations.add(new gRPCremoteAccessImplementation(Integer.parseInt(args[2])));
			break;
		case "rest":
			System.out.println(" Using REST Remote Access Implementation");
			implementations.add(new RestRemoteAccessImplementation());
			break;
		default:
			System.out.println("Unkown Argument, Using UDP Implementation");
			implementations.add(new UDPRemoteAccessImplementation(Integer.parseInt(args[1])));
		}
		return implementations;
	}
}
