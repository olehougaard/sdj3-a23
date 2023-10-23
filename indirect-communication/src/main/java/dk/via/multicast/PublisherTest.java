package dk.via.multicast;

import java.net.InetAddress;

public class PublisherTest {
    public static void main(String[] args) throws Exception {
        MulticastPublisher publisher = new MulticastPublisher(8888);
        publisher.multicast(InetAddress.getByName("230.0.0.0"), "Hello, World!");
    }
}
