package dk.via.group;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class GroupTest {
    public static void main(String[] args) throws IOException, TimeoutException {
        try(Group group = new Group("localhost", "test");
            Scanner input = new Scanner(System.in)) {
            Recipient recipient = System.out::println; /* new Recipient() {
                @Override
                public void receive(String message) {
                    System.out.println(message);
                }
            };*/
            String token = group.join(recipient);
            for(String message = input.nextLine(); !"quit".equals(message); message = input.nextLine()) {
                group.send(message);
            }
            group.leave(token);
        }
    }
}
