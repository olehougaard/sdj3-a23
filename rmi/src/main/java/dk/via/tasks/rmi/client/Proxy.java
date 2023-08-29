package dk.via.tasks.rmi.client;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;

import dk.via.requestreply.Message;
import dk.via.requestreply.client.CommunicationModule;
import dk.via.tasks.Task;
import dk.via.tasks.rmi.TaskListInterface;
import dk.via.util.ByteConverter;

public class Proxy implements TaskListInterface {
	private void callVoid(String message, byte[]... args) throws RemoteException {
		byte[] reply = CommunicationModule.doOperation(new Message(message, args));
		if (reply.length != 0) {
			throw new RemoteException("Invalid reply");
		}
	}

	private int callInt(String message, byte[]... args) throws RemoteException {
		byte[] reply = CommunicationModule.doOperation(new Message(message, args));
		Serializable o;
		try {
			o = ByteConverter.serializableFromByteArray(reply);
			if (o instanceof Exception) {
				Exception e = (Exception) o;
				throw new RemoteException(e.getMessage(), e);
			}
		} catch (ClassNotFoundException | IOException e) {
			// Not an exception
		}
		try {
			return ByteConverter.intFromByteArray(reply);
		} catch (IOException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}
	
	private Serializable call(String message, byte[]... args) throws RemoteException {
		byte[] reply = CommunicationModule.doOperation(new Message(message, args));
		Serializable o;
		try {
			o = ByteConverter.serializableFromByteArray(reply);
		} catch (ClassNotFoundException | IOException e) {
			throw new RemoteException(e.getMessage(), e);
		}
		if (o instanceof Exception) {
			Exception e = (Exception) o;
			throw new RemoteException(e.getMessage(), e);
		} else {
			return o;
		}

	}
	
	private<T extends Serializable> T cast(Serializable s, Class<T> clazz) throws RemoteException {
		if (clazz.isInstance(s)) {
			return clazz.cast(s);
		} else {
			throw new RemoteException("Class cast exception: " + s.getClass());
		}
	}

	@Override
	public void add(Task task) throws RemoteException {
		callVoid("add", ByteConverter.toByteArray(task));
	}

	@Override
	public Task getAndRemoveNextTask() throws RemoteException {
		Serializable reply = call("getAndRemoveNextTask");
		return cast(reply, Task.class);
	}

	@Override
	public int size() throws RemoteException {
		int reply = callInt("size");
		return reply;
	}
}
