package dk.via.observer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Publisher implements Closeable {
    private final Connection connection;
    private final Channel channel;
    private String name;

    public Publisher(String host, String name) throws IOException, TimeoutException {
        this.name = name;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(name, "topic");
    }

    public void publish(String message, String... topics) throws IOException {
        String topic = String.join(".", topics);
        channel.basicPublish(name, topic, null, message.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void close() throws IOException {
        connection.close();
    }
}
