package ulpgc.dacd.destination.control;

import ulpgc.dacd.destination.model.Destination;

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

        String apiHost = "wft-geo-db.p.rapidapi.com";
        String apiUrl = "https://wft-geo-db.p.rapidapi.com/v1/geo/locations/%s/nearbyCities";

        DestinationProvider provider = new GeoDBCitiesProvider(apiKey, apiHost, apiUrl);
        DestinationStore store = new SqliteDestinationStore(dbPath);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            List<Destination> destinations = provider.getDestinations(latitude, longitude);
            if (!destinations.isEmpty()) {
                store.storeDestination(destinations.get(0));
            }
        }, 0, 6, TimeUnit.HOURS);
    }
}
