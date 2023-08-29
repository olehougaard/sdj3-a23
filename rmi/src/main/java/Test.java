import java.rmi.RemoteException;

import dk.via.tasks.Task;
import dk.via.tasks.rmi.TaskListInterface;
import dk.via.tasks.rmi.client.Proxy;

public class Test {
	public static void main(String[] args) throws RemoteException {
		TaskListInterface proxy = new Proxy();
		proxy.add(new Task("task 1", 8700));
		proxy.add(new Task("task 2", 10000));
		System.out.println(proxy.size());
		System.out.println(proxy.getAndRemoveNextTask());
		System.out.println(proxy.size());
	}
}
