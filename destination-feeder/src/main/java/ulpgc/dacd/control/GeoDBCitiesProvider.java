package ulpgc.dacd.control;

import ulpgc.dacd.model.Destination;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GeoDBCitiesProvider implements DestinationProvider {
    private final HttpClient client = HttpClient.newHttpClient();
    private static final String API_KEY = "d84afcb97cmsh93a4604313a412fp1b409cjsn7c2f138c5ab9";
    private static final String HOST = "wft-geo-db.p.rapidapi.com";
    private static final String API_URL = "https://wft-geo-db.p.rapidapi.com/v1/geo/locations/%s/nearbyCities";

    @Override
    public List<Destination> getDestinations(double lat, double lon) {
        try {
            String latStr = (lat >= 0 ? "+" : "") + String.format(Locale.US, "%.4f", lat);
            String lonStr = (lon >= 0 ? "+" : "") + String.format(Locale.US, "%.4f", lon);
            String fullLocation = latStr + lonStr;
            String url = String.format(API_URL, fullLocation);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-RapidAPI-Key", API_KEY)
                    .header("X-RapidAPI-Host", HOST)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Código de estado: " + response.statusCode());
            System.out.println("Respuesta de la API: " + response.body());

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray data = json.getAsJsonArray("data");

            if (data == null) {
                System.err.println("El campo 'data' es null. JSON completo: " + json);
                return List.of();
            }

            List<Destination> destinations = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                JsonObject obj = data.get(i).getAsJsonObject();
                String name = obj.get("city").getAsString();
                String country = obj.get("country").getAsString();
                double latitude = obj.get("latitude").getAsDouble();
                double longitude = obj.get("longitude").getAsDouble();
                int population = obj.get("population").getAsInt();
                double distance = obj.get("distance").getAsDouble();
                destinations.add(new Destination(name, country, latitude, longitude, population, distance));
            }
            return destinations;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}