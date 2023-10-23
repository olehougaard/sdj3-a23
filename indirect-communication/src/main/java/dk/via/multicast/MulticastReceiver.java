package dk.via.multicast;

import java.io.IOException;
import java.net.*;

public class MulticastReceiver {
    private final int port;

    public MulticastReceiver(int port) {
        this.port = port;
    }

    public String receive(InetAddress group) throws IOException {
        assert group.getAddress()[0] >= (byte)224 && group.getAddress()[0] < (byte)240;
        MulticastSocket socket = new MulticastSocket(port);
        socket.joinGroup(new InetSocketAddress(group, port), NetworkInterface.getByInetAddress(group));
        try {
            byte[] content = new byte[32768];
            DatagramPacket packet = new DatagramPacket(content, content.length);
            socket.receive(packet);
            String message = new String(packet.getData(), 0, packet.getLength());
            return message;
        } finally {
            socket.leaveGroup(new InetSocketAddress(group, port), NetworkInterface.getByInetAddress(group));
            socket.close();
        }
    }
}
