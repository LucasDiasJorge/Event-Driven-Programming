package com.example.socketio.services;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Service;

@Service
public class SocketService {

    private final SocketIOServer server;

    public SocketService(SocketIOServer server) {
        this.server = server;
    }

    public void sendMessage(String message, String event, Object senderClient, String room) {
        server.getRoomOperations(room).sendEvent(event, message);
    }
}
