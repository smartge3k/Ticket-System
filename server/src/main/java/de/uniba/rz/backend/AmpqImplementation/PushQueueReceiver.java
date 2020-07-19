package de.uniba.rz.backend.AmpqImplementation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;

public final class PushQueueReceiver extends QueueReceiver{
	private final String hostname;
	private final String queueName;
	private final ConnectionFactory connFactory = new ConnectionFactory();

	public PushQueueReceiver(String hostname, String queueName) {
		this.hostname = hostname;
		this.queueName = queueName;
	}

	@Override
	public void run() {
		// Data structure exchanging data between the receiver's thread and the dispatcher thread
		final BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(1, true);
		// Note: Could be omitted because localhost is set be default
		connFactory.setHost(this.hostname);
		System.out.println("\t [RECEIVER]: Start waiting for me");
		try (Connection connection = connFactory.newConnection();) {
			Channel channel = connection.createChannel();
			channel.queueDeclare(this.queueName, false, false, false, null);
		} catch (IOException e) {
			// TODO: Think of an appropriate exception handling strategy (e.g., retrying, logging, ...)
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO: Think of an appropriate exception handling strategy (e.g., retrying, logging, ...)
			e.printStackTrace();
		}
	}

	@Override
	public void startServer() {
		this.start();
	}

	@Override
	public void stopServer() {
		System.out.println("\t [RECEIVER]: Stopping to listen for messages.");
		this.interrupt(); // Ask the PushQueueReceiver for a graceful shutdown
	}
}
