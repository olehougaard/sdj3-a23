package dk.via.group;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Group implements Closeable {
    private final Connection connection;
    private final Channel channel;
    private final String name;

    public Group(String host, String name) throws IOException, TimeoutException {
        this.name = name;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(name, "fanout");
    }

    public String join(Recipient recipient) throws IOException {
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, name, "");
        channel.basicConsume(queueName, true, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            recipient.receive(message);
        }, consumerTag -> {
        });
        return queueName;
    }

    public void leave(String token) throws IOException {
        channel.queueDelete(token);
    }

    public void send(String message) throws IOException {
        channel.basicPublish(name, "", null, message.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void close() throws IOException {
        connection.close();
    }
}
