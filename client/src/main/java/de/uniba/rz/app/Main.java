package de.uniba.rz.app;

import de.uniba.rz.app.AmpqImplementation.AmpqTicketMangamentBackend;
import de.uniba.rz.app.gRPC.gRPCticketManagmentBackend;
import de.uniba.rz.restful.restTicketbackendManagement;
import de.uniba.rz.ui.swing.MainFrame;
import de.uniba.rz.ui.swing.SwingMainController;
import de.uniba.rz.ui.swing.SwingMainModel;

public class Main{

	public static void main(String[] args) {
		TicketManagementBackend backendToUse = evaluateArgs(args);
		SwingMainController control = new SwingMainController(backendToUse);
		SwingMainModel model = new SwingMainModel(backendToUse);
		MainFrame mf = new MainFrame(control, model);
		control.setMainFrame(mf);
		control.setSwingMainModel(model);
		control.start();
	}

	private static TicketManagementBackend evaluateArgs(String[] args) {
		switch (args[0]) {
		case "udp":
			System.out.println("Using UDP backend implemenation.");
			return new UDPTicketManagementBackend();
		case "ampq":
			System.out.println("Using Ampq backend implemenation.");
			return new AmpqTicketMangamentBackend(args[1], args[2]);
		case "grpc":
			System.out.println("Using gRPC backend implemenation.");
			int port = Integer.parseInt(args[2]);
			return new gRPCticketManagmentBackend(args[1], port);
		case "rest":
			System.out.println("Using Rest backend implemenation.");
			return new restTicketbackendManagement();
		default:
			System.out.println("Unknown Argument. Using UDP backend implemenation.");
			return new UDPTicketManagementBackend();
		}
	}
}
