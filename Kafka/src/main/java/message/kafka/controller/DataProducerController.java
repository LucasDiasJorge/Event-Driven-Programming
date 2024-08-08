package message.kafka.controller;

import message.kafka.service.DataProducerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/data")
public class DataProducerController {

    private final DataProducerService dataProducerService;

    public DataProducerController(DataProducerService dataProducerService) {
        this.dataProducerService = dataProducerService;
    }

    // Basic controller to send message from client, Topic is created automatically
    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message) {
        dataProducerService.sendMessage("fromDataController", message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
