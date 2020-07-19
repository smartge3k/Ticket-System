package de.uniba.rz.backend.gRPC;

import java.io.IOException;

import de.uniba.rz.io.rpc.TicketRequest;
import de.uniba.rz.io.rpc.TicketResponse;
import de.uniba.rz.io.rpc.TicketServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class gRPCServer{
	private final int port;
	private final Server server;

	/**
	 * During the construction of our server, the implemented service must be
	 * specified. There is also a possibility to
	 * expose more than one service.
	 */
	public gRPCServer(int p) {
		this.port = p;
		this.server = ServerBuilder.forPort(port).addService(new serverrequestImpl()).build();
	}

	/**
	 * Starts the server and adds a shutdown hock to orderly shutdown the server.
	 *
	 * @throws IOException
	 */
	public void start() throws IOException {
		server.start();
		System.out.println("Server started and listened on port " + this.port);
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				// Use stderr here since the logger may have been reset by its JVM shutdown hook.
				System.err.println("*** shutting down gRPC server since JVM is shutting down");
				gRPCServer.this.stop();
				System.err.println("*** server shut down");
			}
		});
	}

	/**
	 * Method to stop the server, if a server is present.
	 */
	public void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	/**
	 * Blocking method until the shutdown hock terminates the server.
	 *
	 * @throws InterruptedException
	 */
	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}
	/**
	 * Custom class for the implementation of the base service, which is an abstract
	 * class. The method must be
	 * overridden since the default implementation has no implemented logic.
	 */
}