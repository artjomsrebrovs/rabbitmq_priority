package rabbitmq.priority.producer.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface RabbitMQPriorityService extends AutoCloseable {

    void produceMessagesNoPriority() throws IOException, TimeoutException;

    void produceMessagesSamePriority() throws IOException, TimeoutException;

    void produceMessagesDifferentPriority() throws IOException, TimeoutException;
}
