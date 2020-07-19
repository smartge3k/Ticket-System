package de.uniba.rz.app.AmpqImplementation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.utility.BlockingCell;

import de.uniba.rz.entities.DTO;

public class AmpqQueueSender{
	private final String hostname;
	private final String routingKey;
	private final ConnectionFactory connFactory = new ConnectionFactory();

	public AmpqQueueSender(String hostname, String routingKey) {
		this.hostname = hostname;
		this.routingKey = routingKey;
	}

	public DTO sendMessage(String message) {
		DTO replyDTO = new DTO();
		connFactory.setHost(hostname);
		try (Connection connection = connFactory.newConnection();) {
			Channel channel = connection.createChannel();
			final String corrId = UUID.randomUUID().toString();
			String replyQueueName = channel.queueDeclare().getQueue();
			AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName)
			      .build();
			channel.basicPublish("", routingKey, props, message.getBytes("UTF-8"));
			final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
			String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
				if (delivery.getProperties().getCorrelationId().equals(corrId)) {
					response.offer(new String(delivery.getBody(), "UTF-8"));
				}
			}, consumerTag -> {
			});
			String result = null;
			try {
				result = response.take();
				// String responseString = new String(response.take()).trim();
				Gson json = new GsonBuilder().serializeNulls().create();
				replyDTO = json.fromJson(result, DTO.class);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// System.out.println("Server Responses:" + result.toString());
		} catch (IOException e) {
			// TODO: Think of an appropriate exception handling strategy (e.g., retrying,
			// logging,...)
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO: Think of an appropriate exception handling strategy (e.g., retrying,
			// logging,...)
			e.printStackTrace();
		}
		return replyDTO;
	}
}
