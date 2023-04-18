package rabbitmq.priority.producer.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rabbitmq.priority.producer.controller.RabbitMQPriorityController;
import rabbitmq.priority.producer.service.RabbitMQPriorityService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RestController
public class RabbitMQPriorityControllerImpl implements RabbitMQPriorityController {

    private final RabbitMQPriorityService rabbitMQPriorityService;

    @Autowired
    public RabbitMQPriorityControllerImpl(RabbitMQPriorityService rabbitMQPriorityService) {
        this.rabbitMQPriorityService = rabbitMQPriorityService;
    }

    @GetMapping("no_priority")
    public ResponseEntity produceMessagesNoPriority() throws IOException, TimeoutException {
        rabbitMQPriorityService.produceMessagesNoPriority();
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("same_priority")
    public ResponseEntity produceMessagesSamePriority() throws IOException, TimeoutException {
        rabbitMQPriorityService.produceMessagesSamePriority();
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("different_priority")
    public ResponseEntity produceMessagesDifferentPriority() throws IOException, TimeoutException {
        rabbitMQPriorityService.produceMessagesDifferentPriority();
        return new ResponseEntity(HttpStatus.OK);
    }
}
