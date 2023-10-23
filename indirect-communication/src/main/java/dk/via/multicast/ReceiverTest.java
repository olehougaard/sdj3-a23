package dk.via.multicast;

import java.io.IOException;
import java.net.InetAddress;

public class ReceiverTest {
    public static void main(String[] args) throws IOException {
        MulticastReceiver receiver = new MulticastReceiver(8888);
        String message = receiver.receive(InetAddress.getByName("230.0.0.0"));
        System.out.println(message);
    }
}
