package ulpgc.dacd.shared;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.time.Instant;

public class Publisher {
    private final String topic;
    private final Gson gson;
    private final ConnectionFactory factory;

    public Publisher(String topic) {
        this.topic = topic;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, (com.google.gson.JsonSerializer<Instant>) (src, typeOfSrc, context) ->
                        new com.google.gson.JsonPrimitive(src.toString()))
                .create();
        this.factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
    }

    public void publish(Object event) {
        Connection connection = null;
        try {
            connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(topic);
            MessageProducer producer = session.createProducer(destination);

            String json = gson.toJson(event);
            TextMessage message = session.createTextMessage(json);
            producer.send(message);

            session.close();
            producer.close();

        } catch (Exception e) {
            System.err.println("Failed to publish event to topic: " + topic);
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}

