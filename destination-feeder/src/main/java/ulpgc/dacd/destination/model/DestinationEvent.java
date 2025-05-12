package ulpgc.dacd.destination.model;

import java.time.Instant;
import java.util.List;

public class DestinationEvent {
    public Instant ts;
    public String ss;
    public List<Destination> destinations;

    public DestinationEvent(String ss, List<Destination> destinations) {
        this.ts = Instant.now();
        this.ss = ss;
        this.destinations = destinations;
    }
}
