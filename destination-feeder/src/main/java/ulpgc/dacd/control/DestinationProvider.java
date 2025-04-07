package ulpgc.dacd.control;

import ulpgc.dacd.model.Destination;
import java.util.List;

public interface DestinationProvider {
    List<Destination> getDestinations(double lat, double lon);
}