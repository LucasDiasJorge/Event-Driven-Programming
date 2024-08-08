package message.kafka.controller;

import message.kafka.service.DataProducerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Map;

@RestController
@RequestMapping(value = "/data")
public class DataProducerController {

    private final DataProducerService dataProducerService;

    public DataProducerController(DataProducerService dataProducerService) {
        this.dataProducerService = dataProducerService;
    }

    // Basic controller to send message from client, Topic is created automatically
    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody Map<String,Object> body) {
        dataProducerService.sendMessage((Serializable) body);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
