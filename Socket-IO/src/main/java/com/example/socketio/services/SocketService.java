package com.example.socketio.services;

import com.corundumstudio.socketio.SocketIOClient;
import com.example.socketio.message.Message;
import com.example.socketio.message.MessageType;
import org.springframework.stereotype.Service;

@Service
public class SocketService {

    public void sendMessage(String room,String eventName, SocketIOClient senderClient, String message) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent(eventName,
                        new Message(MessageType.SERVER, message));
            }
        }
    }

}