package dk.via.mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MQPublisherTest {
    public static void main(String[] args) throws IOException, TimeoutException {
        try(MessageQueueSystem q = new MessageQueueSystem("localhost", "mq")) {
            q.send("queue1", "Message 1");
            q.send("queue1", "Message 2");
            q.send("queue1", "Last message");
        }
    }
}
