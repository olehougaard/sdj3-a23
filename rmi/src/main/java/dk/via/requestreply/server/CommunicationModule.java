package dk.via.requestreply.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

import dk.via.requestreply.Message;
import dk.via.tasks.TaskList;
import dk.via.tasks.rmi.server.Skeleton;
import dk.via.tasks.rmi.server.TaskDispatcher;

public class CommunicationModule {
	public static final int PORT = 9090;
	
	public static void main(String[] args) throws Exception {
		TaskList list = new TaskList();
		Skeleton skeleton = new Skeleton(list);
		TaskDispatcher dispatcher = new TaskDispatcher(skeleton);
		listen(dispatcher);
	}

	private static void listen(Recipient recipient) throws IOException {
		@SuppressWarnings("resource") // Main socket is open until server shuts down.
		ServerSocket socket = new ServerSocket(PORT);
		while (true) {
			try(Socket accept = socket.accept();
					ObjectInputStream ois = new ObjectInputStream(accept.getInputStream());
					ObjectOutputStream oos = new ObjectOutputStream(accept.getOutputStream())) {
				Object objectIn = ois.readObject();
				if (objectIn instanceof Message) {
					try {
						Serializable result = recipient.interpret((Message) objectIn);
						oos.writeObject(result);
					} catch (Throwable e) {
						oos.writeObject(e);
					}
				} else {
					oos.writeObject(new IllegalArgumentException("Illegal class: " + objectIn.getClass()));
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
