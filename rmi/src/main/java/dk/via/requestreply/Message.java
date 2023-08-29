package dk.via.requestreply;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	private String method;
	private byte[][] args;
	
	public static final Message VOID = new Message("VOID", new byte[0][]);
	
	public Message(String method, byte[][] args) {
		this.method = method;
		this.args = args;
	}

	public String getMethod() {
		return method;
	}

	public byte[][] getArgs() {
		return args;
	}
}
