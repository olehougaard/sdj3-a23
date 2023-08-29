package dk.via.requestreply.server;

import dk.via.requestreply.Message;

public interface Recipient {

	byte[] interpret(Message message);

}