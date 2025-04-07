package ulpgc.dacd.control;

import ulpgc.dacd.model.Destination;
import java.util.List;

public class DestinationController {
    private final DestinationProvider provider;
    private final DestinationStore store;

    public DestinationController(DestinationProvider provider, DestinationStore store) {
        this.provider = provider;
        this.store = store;
    }

    public void execute(double lat, double lon) {
        List<Destination> destination = provider.getDestinations(lat, lon);
        if (!destination.isEmpty()) {
            store.storeDestination(destination.get(0));
        }
    }
}