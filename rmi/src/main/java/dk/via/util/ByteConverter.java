package dk.via.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Scanner;

public class ByteConverter {
	public static byte[] toByteArray(Serializable o) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try(ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(o);
		} catch (IOException e) {
			// Shouldn't be possible
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
	
	public static byte[] toByteArray(String s) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(baos);
		pw.println(s);
		return baos.toByteArray();
	}

	public static byte[] toByteArray(int i) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream pw = new DataOutputStream(baos);
		try {
			pw.writeInt(i);
		} catch (IOException e) {
			// Shouldn't be possible
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
	
	public static byte[] toByteArray(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try(ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(e);
		} catch (IOException e2) {
			// Shouldn't be possible
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
	
	public static Serializable serializableFromByteArray(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		try(ObjectInputStream scanner = new ObjectInputStream(bais)) {
			return (Serializable) scanner.readObject();
		}
	}
	
	public static String stringFromByteArray(byte[] bytes) {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		try(Scanner scanner = new Scanner(bais)) {
			return scanner.nextLine();
		}
	}
	
	public static int intFromByteArray(byte[] bytes) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		DataInputStream dis = new DataInputStream(bais);
		return dis.readInt();
	}

	public static Exception exceptionFromByteArray(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		try(ObjectInputStream scanner = new ObjectInputStream(bais)) {
			return (Exception) scanner.readObject();
		}
	}
}
