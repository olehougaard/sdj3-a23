package dk.via.mq;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class MQConsumerTest {
    public static void main(String[] args) throws IOException, TimeoutException {
        try(MessageQueueSystem q = new MessageQueueSystem("localhost", "mq");
            Scanner newLineReader = new Scanner(System.in)) {
            q.receive("queue1", System.out::println);
            newLineReader.nextLine(); //To keep the program from terminating
        }
    }
}
