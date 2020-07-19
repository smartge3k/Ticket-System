package de.uniba.rz.backend.AmpqImplementation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;

import de.uniba.rz.entities.Status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.GetResponse;

import de.uniba.rz.entities.DTO;
import de.uniba.rz.entities.Ticket;

public final class PullQueueReceiver extends QueueReceiver{
	private final String hostname;
	private final String queueName;
	AmpqTicketStoreImplementation ticketStore = new AmpqTicketStoreImplementation();
	private final ConnectionFactory connFactory = new ConnectionFactory();

	public PullQueueReceiver(String hostname, String queueName) {
		this.hostname = hostname;
		this.queueName = queueName;
	}

	@Override
	public void run() {
		connFactory.setHost(this.hostname);
		System.out.println("\t [RECEIVER]: Start waiting for messages");
		try (Connection connection = connFactory.newConnection();) {
			Channel channel = connection.createChannel();
			channel.queueDeclare(this.queueName, false, false, false, null);
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
				      .correlationId(delivery.getProperties().getCorrelationId()).build();
				String response = "Started";
				try {
					byte[] body = delivery.getBody();
					String clientdata = new String(new String(body, StandardCharsets.UTF_8));
					Gson json = new GsonBuilder().serializeNulls().create();
					DTO clientRequest = new DTO();
					clientRequest = json.fromJson(clientdata.trim(), DTO.class);
					if (clientRequest.getplay() == 1) {
						ticketStore.storeNewTicket(
						      clientRequest.getTicket().getReporter(), clientRequest.getTicket().getTopic(),
						      clientRequest.getTicket().getDescription(), clientRequest.getTicket().getType(),
						      clientRequest.getTicket().getPriority()
						);
						DTO SaveTicketRequest = new DTO();
						Gson json2 = new GsonBuilder().serializeNulls().create();
						SaveTicketRequest.setTicket(clientRequest.getTicket());
						String ticketreturn = json2.toJson(SaveTicketRequest, DTO.class);
						response = ticketreturn;
					} else if (clientRequest.getplay() == 2) {
						DTO AllTicketRequest = new DTO();
						Gson json2 = new GsonBuilder().serializeNulls().create();
						AllTicketRequest.setTickets(ticketStore.getAllTickets());
						String allticketsreturn = json2.toJson(AllTicketRequest, DTO.class);
						response = allticketsreturn;
					} else if (clientRequest.getplay() == 3) {
						DTO getTickeyById = new DTO();
						Gson json2 = new GsonBuilder().serializeNulls().create();
						List<Ticket> alltickets = ticketStore.getAllTickets();
						System.out.println("Asking for ticket Id from Server" + ((clientRequest.getTicketId()) - 1));
						Ticket a = alltickets.get((clientRequest.getTicketId()) - 1);
						System.out.println("Ticket Server Found in the Ticket Store" + a.getReporter() + " " + a.getTopic());
						getTickeyById.setTicket(a);
						String reply = json2.toJson(getTickeyById, DTO.class);
						response = reply;
					} else if (clientRequest.getplay() == 4) {
						DTO changeStatus = new DTO();
						Gson json2 = new GsonBuilder().serializeNulls().create();
						List<Ticket> alltickets = ticketStore.getAllTickets();
						Ticket a = alltickets.get((clientRequest.getTicketId()) - 1);
						a.setStatus(Status.ACCEPTED);
						//alltickets.get(clientRequest.getTicketId()-1).setStatus(Status.ACCEPTED);
						ticketStore.getAllTickets().set((clientRequest.getTicketId()) - 1, a);
						changeStatus.setTicket(a);
						System.out.println("Ticket Status changed to Accepted");
						String reply = json2.toJson(changeStatus, DTO.class);
						response = reply;
					} else if (clientRequest.getplay() == 5) { // 5 Make Status closed
						DTO changeStatus = new DTO();
						Gson json2 = new GsonBuilder().serializeNulls().create();
						List<Ticket> alltickets = ticketStore.getAllTickets();
						Ticket a = alltickets.get((clientRequest.getTicketId()) - 1);
						a.setStatus(Status.CLOSED);
						//alltickets.get(clientRequest.getTicketId()-1).setStatus(Status.ACCEPTED);
						ticketStore.getAllTickets().set((clientRequest.getTicketId()) - 1, a);
						changeStatus.setTicket(a);
						System.out.println("Ticket Status changed to Closed");
						String reply = json2.toJson(changeStatus, DTO.class);
						response = reply;
					} else if (clientRequest.getplay() == 6) { // 6 Make Status Rejected
						DTO changeStatus = new DTO();
						Gson json2 = new GsonBuilder().serializeNulls().create();
						List<Ticket> alltickets = ticketStore.getAllTickets();
						Ticket a = alltickets.get((clientRequest.getTicketId()) - 1);
						a.setStatus(Status.REJECTED);
						//alltickets.get(clientRequest.getTicketId()-1).setStatus(Status.ACCEPTED);
						ticketStore.getAllTickets().set((clientRequest.getTicketId()) - 1, a);
						changeStatus.setTicket(a);
						System.out.println("Ticket Status changed to Rejected");
						String reply = json2.toJson(changeStatus, DTO.class);
						response = reply;
					} else {
						System.out.println("Wrong Command Sent");
					}
				} catch (RuntimeException e) {
					System.out.println(" [.] " + e.toString());
				} finally {
					channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
					channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
				}
			};
			while (!Thread.currentThread().isInterrupted()) {
				channel.basicConsume(queueName, false, deliverCallback, (consumerTag -> {
				}));
			}
		} catch (IOException e) {
			// TODO: Think of an appropriate exception handling strategy (e.g., retrying, logging, ...)
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO: Think of an appropriate exception handling strategy (e.g., retrying, logging, ...)
			e.printStackTrace();
		}
		System.out.println("\t [RECEIVER]: Stopped.");
	}

	@Override
	public void startServer() {
		this.start();
	}

	@Override
	public void stopServer() {
		System.out.println("\t [RECEIVER]: Stopping to listen for messages.");
		this.interrupt(); // Ask the PullQueueReceiver for a graceful shutdown
	}
}
