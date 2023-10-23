package dk.via.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class MessageQueueSystem implements Closeable {
    private final Channel channel;
    private final Connection connection;

    public MessageQueueSystem(String host, String name) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void send(String queueName, String message) throws IOException {
        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
    }

    public void receive(String queueName, Consumer consumer) throws IOException {
        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicConsume(queueName, true, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            consumer.notify(message);
        }, consumerTag -> {
        });
    }

    @Override
    public void close() throws IOException {
        connection.close();
    }
}
