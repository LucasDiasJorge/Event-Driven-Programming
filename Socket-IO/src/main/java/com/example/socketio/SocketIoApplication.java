package com.example.socketio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class SocketIoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocketIoApplication.class, args);
	}

}
