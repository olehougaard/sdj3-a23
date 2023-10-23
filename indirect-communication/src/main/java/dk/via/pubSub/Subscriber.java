package dk.via.pubSub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Subscriber implements Closeable {

    private final String name;
    private final Connection connection;
    private final Channel channel;
    private final String queueName;

    public Subscriber(String host, String name, MessageConsumer consumer) throws IOException, TimeoutException {
        this.name = name;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection();
        channel = connection.createChannel();
        queueName = channel.queueDeclare().getQueue();
        channel.basicConsume(queueName, true, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            consumer.notify(message);
        }, consumerTag -> {
        });
    }

    public void subscribe(String topic) throws IOException {
        channel.queueBind(queueName, name, "#." + topic + ".#");
    }

    @Override
    public void close() throws IOException {
        channel.queueDelete(queueName);
        connection.close();
    }
}
