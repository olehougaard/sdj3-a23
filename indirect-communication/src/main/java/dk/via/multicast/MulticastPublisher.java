package dk.via.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MulticastPublisher {
    private final int port;

    public MulticastPublisher(int port) {
        this.port = port;
    }

    public void multicast(InetAddress group, String message) throws IOException {
        try(DatagramSocket socket = new DatagramSocket()) {
            byte[] content = message.getBytes();
            DatagramPacket packet = new DatagramPacket(content, content.length, group, port);
            socket.send(packet);
        }
    }
}
