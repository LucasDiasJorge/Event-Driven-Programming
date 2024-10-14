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

    public DataProducerController(DataProducerService dataProducerService) {
    }

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody Map<String,Object> body) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
