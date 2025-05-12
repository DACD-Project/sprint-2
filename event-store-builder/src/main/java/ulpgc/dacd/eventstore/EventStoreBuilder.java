package ulpgc.dacd.eventstore;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class EventStoreBuilder {

    public static void main(String[] args) throws Exception {
        String[] topics = {"Weather", "Destination"};
        String brokerUrl = "tcp://localhost:61616";

        ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = factory.createConnection();
        connection.setClientID("event-store-client");
        connection.start();
        System.out.println("Connection to ActiveMQ established");

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        for (String topicName : topics) {
            Topic topic = session.createTopic(topicName);
            MessageConsumer consumer = session.createDurableSubscriber(topic, topicName + "-subscriber");

            final String currentTopic = topicName;

            System.out.println("Subscribed to topic: " + currentTopic);

            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    try {
                        String json = ((TextMessage) message).getText();
                        System.out.println("Event received from topic [" + currentTopic + "]: " + json);

                        JsonObject event = JsonParser.parseString(json).getAsJsonObject();
                        storeEvent(currentTopic, event);
                    } catch (Exception e) {
                        System.err.println("Error processing message from topic " + currentTopic);
                        e.printStackTrace();
                    }
                }
            });
        }


        System.out.println("Event store builder is running");
    }

    private static void storeEvent(String topic, JsonObject event) {
        try {
            String ss = event.get("ss").getAsString();
            Instant instant = Instant.parse(event.get("ts").getAsString());
            LocalDate date = instant.atZone(ZoneOffset.UTC).toLocalDate();

            Path dirPath = Paths.get("eventstore", topic, ss);
            Files.createDirectories(dirPath);

            String filename = String.format("%s.events", date.toString().replace("-", ""));
            Path filePath = dirPath.resolve(filename);

            try (FileWriter writer = new FileWriter(filePath.toFile(), true)) {
                writer.write(event.toString());
                writer.write(System.lineSeparator());
            }

            System.out.println("Event saved to: " + filePath);

        } catch (IOException e) {
            System.err.println("Error writing the event to file");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in storeEvent()");
            e.printStackTrace();
        }
    }
}
