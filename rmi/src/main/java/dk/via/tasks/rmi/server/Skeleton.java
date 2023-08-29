package dk.via.tasks.rmi.server;

import java.io.IOException;
import java.io.Serializable;

import dk.via.tasks.Task;
import dk.via.tasks.TaskList;
import dk.via.util.ByteConverter;

public class Skeleton {
	private TaskList tasks;
	
	public Skeleton(TaskList tasks) {
		this.tasks = tasks;
	}

	@SafeVarargs
	private final Serializable[] unmarshal(byte[][] args, Class<? extends Serializable>... expected) {
		if (args == null) throw new NullPointerException();
		if (args.length != expected.length) throw new IllegalArgumentException("Wrong number of arguments: " + args.length);
		Serializable[] sar = new Serializable[args.length];
		for(int i = 0; i < args.length; i++) {
			try {
				sar[i] = ByteConverter.serializableFromByteArray(args[i]);
			} catch (ClassNotFoundException | IOException e) {
				throw new IllegalArgumentException(e);
			}
			if (!expected[i].isInstance(sar[i])) throw new IllegalArgumentException("Incompatible argument type");
		}
		return sar;
	}
	
	private byte[] marshall(Task task) {
		return ByteConverter.toByteArray(task);
	}
	
	private byte[] marshall(int size) {
		return ByteConverter.toByteArray(size);
	}
	
	public byte[] add(byte[][] args) {
		try {
			Serializable[] unmarshalled = unmarshal(args, Task.class);
			tasks.add((Task) unmarshalled[0]);
			return new byte[0];
		} catch (Exception e) {
			return ByteConverter.toByteArray(e);
		}
	}
	
	public byte[] getAndRemoveNextTask(byte[][] args) {
		unmarshal(args);
		Task task = tasks.getAndRemoveNextTask();
		return marshall(task);
	}

	public byte[] size(byte[][] args) {
		unmarshal(args);
		int size = tasks.size();
		return marshall(size);
	}
}
