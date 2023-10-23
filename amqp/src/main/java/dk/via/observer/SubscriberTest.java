package dk.via.observer;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class SubscriberTest {
    public static void main(String[] args) throws IOException, TimeoutException {
        try(Subscriber s = new Subscriber("localhost", "mq", System.out::println);
            Scanner input = new Scanner(System.in)) {
            for(String topic = input.nextLine(); !"done".equalsIgnoreCase(topic); topic = input.nextLine()) {
                s.subscribe(topic);
            }
        }
    }
}
