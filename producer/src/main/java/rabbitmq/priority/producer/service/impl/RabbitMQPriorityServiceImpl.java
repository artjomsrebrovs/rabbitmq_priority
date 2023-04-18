package rabbitmq.priority.producer.service.impl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Service;
import rabbitmq.priority.producer.service.RabbitMQPriorityService;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitMQPriorityServiceImpl implements RabbitMQPriorityService {

    private static final String QUEUE_NAME = "q.test-priority";

    private static final int MESSAGE_COUNT = 300_000;

    private static final int MAX_PRIORITY = 255;

    private final Connection connection;

    public RabbitMQPriorityServiceImpl() throws IOException, TimeoutException {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        connection = factory.newConnection();


    }

    @Override
    public void produceMessagesNoPriority() throws IOException, TimeoutException {
        final Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, /*durable*/true, /*exclusive*/false, /*autoDelete*/false, /*arguments*/null);
        channel.queuePurge(QUEUE_NAME);

        final ByteBuffer buffer = ByteBuffer.allocate(1000);

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            final AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .deliveryMode(2 /*PERSISTENT*/)
                    .build();

            channel.basicPublish("", QUEUE_NAME, props, buffer.array());
        }

        channel.close();
    }

    @Override
    public void produceMessagesSamePriority() throws IOException, TimeoutException {
        final Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", MAX_PRIORITY);

        final Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, /*durable*/true, /*exclusive*/false, /*autoDelete*/false, /*arguments*/args);
        channel.queuePurge(QUEUE_NAME);

        final ByteBuffer buffer = ByteBuffer.allocate(1000);

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            final AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .priority(1)
                    .build();

            channel.basicPublish("", QUEUE_NAME, props, buffer.array());
        }

        channel.close();
    }

    @Override
    public void produceMessagesDifferentPriority() throws IOException, TimeoutException {
        final Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", MAX_PRIORITY);

        final Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, /*durable*/true, /*exclusive*/false, /*autoDelete*/false, /*arguments*/args);
        channel.queuePurge(QUEUE_NAME);

        final ByteBuffer buffer = ByteBuffer.allocate(1000);

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            final AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .priority(i % MAX_PRIORITY)
                    .build();

            channel.basicPublish("", QUEUE_NAME, props, buffer.array());
        }

        channel.close();
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
