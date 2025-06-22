package ulpgc.dacd.destination.control;

import ulpgc.dacd.destination.model.Destination;
import ulpgc.dacd.destination.model.DestinationEvent;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DestinationCoordinator {

    public void run(String[] args) {
        String apiKey = args[0];
        String dbPath = "jdbc:sqlite:" + args[1];
        double latitude = Double.parseDouble(args[2]);
        double longitude = Double.parseDouble(args[3]);
        String brokerUrl = args[4];
        String topic = args[5];
        String sourceId = args[6];

        String apiHost = "wft-geo-db.p.rapidapi.com";
        String apiUrl = "https://wft-geo-db.p.rapidapi.com/v1/geo/locations/%s/nearbyCities";

        DestinationProvider provider = new GeoDBCitiesProvider(apiKey, apiHost, apiUrl);
        DestinationStore store = new SqliteDestinationStore(dbPath);
        DestinationPublisher publisher = new DestinationPublisher(brokerUrl, topic, sourceId);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            List<Destination> destinations = provider.getDestinations(latitude, longitude);
            if (!destinations.isEmpty()) {
                Destination destination = destinations.get(0);
                store.storeDestination(destination);

                DestinationEvent event = new DestinationEvent(sourceId, List.of(destination));
                publisher.publish(event);
            } else {
                System.out.println("No destinations retrieved.");
            }
        }, 0, 6, TimeUnit.HOURS);
    }
}
