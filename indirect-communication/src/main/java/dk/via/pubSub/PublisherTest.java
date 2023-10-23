package dk.via.pubSub;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class PublisherTest {
    public static void main(String[] args) throws IOException, TimeoutException {
        try(Publisher q = new Publisher("localhost", "mq");
            Scanner input = new Scanner(System.in)) {
            input.nextLine(); // Wait for subscribers
            q.publish("Message 1", "sport", "politics");
            q.publish("Message 2", "sport", "gossip");
            q.publish("Last message", "gossip");
        }
    }
}
