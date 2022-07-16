package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Hello world!
 *
 */
public class App {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("localhost");

        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            String routingKey = getRouting(args);
            String message = getMessage(args);

            channel.basicPublish(EXCHANGE_NAME,
                    routingKey,
                    null,
                    message.getBytes("UTF-8"));

            System.out.println(
                    " [x] Sent '" + routingKey + "':'" + message + "'"
            );
        }
    }

    private static String getMessage(String[] args) {
        return args[1];
    }

    private static String getRouting(String[] args) {

        return args[0];

    }
}
