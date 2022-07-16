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

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("localhost");

        String argsMessage = "";

        if(args[0] != null) {
            argsMessage = args[0];
        }

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            /**
             * basicQos é feito para fazer com que os consumidores
             * funcionem de forma diferente do rodizio padrão
             */
            //channel.basicQos(1);

            /**
             * Segundo parâmetro dessa função é para setar
             * fila como duravel, então caso a mensagem também
             * esteja marcado como duravel as mensagens serão
             * persistidas
             */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            String message = "{\"nome\": \"" + argsMessage + "\"}";

            /**
             * Terceiro parâmetro dessa mensagem pode ser
             * passado a classe MessageProperties e a contante
             * PERSISTENT_TEXT_PLAIN que irá persistir as mensagens
             * caso a fila seja durável
             */
            channel.basicPublish("",
                    QUEUE_NAME,
                    null,
                    message.getBytes());

            System.out.println(" [x] Sent '" + message + "'");

        }

    }

}