package rabbitMq;

import Constants.Constants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public  abstract class RabbitMqClient {


    public void receiveMessage(String queueName) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(ConnectionFactory.DEFAULT_HOST);
            factory.setPassword(Constants.RABBIT_MQ_PASSWORD);
            factory.setVirtualHost(Constants.RABBIT_MQ_VIRTUALHOST);
            factory.setUsername(Constants.RABBIT_MQ_USERNAME);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(queueName, true, false, false, null);
            System.out.println(" [*] Waiting for scores of players.");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                processMessage(message);
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract  void processMessage(String message);

}

