package ulpgc.dacd.shared;

import com.google.gson.Gson;
import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Publisher {

    private final String topicName;
    private final String brokerUrl = "tcp://localhost:61616";
    private final Gson gson = GsonFactory.create();

    public Publisher(String topicName) {
        this.topicName = topicName;
    }

    public void publish(Object event) {
        Connection connection = null;
        Session session = null;

        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
            connection = factory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            MessageProducer producer = session.createProducer(topic);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            String json = gson.toJson(event);
            TextMessage message = session.createTextMessage(json);
            producer.send(message);

            System.out.println("event published to topic [" + topicName + "]: " + json);
        } catch (Exception e) {
            System.err.println("error publishing to topic: " + topicName);
            e.printStackTrace();
        } finally {
            try {
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
