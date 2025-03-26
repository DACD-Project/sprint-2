package ulpgc.dacd.control;

import ulpgc.dacd.model.Weather;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherMapProvider implements WeatherProvider {
    private final HttpClient client = HttpClient.newHttpClient();
    private static final String API_KEY = "eb2c3e84400f89c3bc587c4804ca3568"; // Reemplaza con tu key real
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/forecast";

    @Override
    public Weather getWeather(double lat, double lon) {
        try {
            String url = API_URL + "?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY + "&units=metric";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Imprime para depurar
            System.out.println("Código de estado: " + response.statusCode());
            System.out.println("Respuesta de la API: " + response.body());

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray forecastList = json.getAsJsonArray("list");

            if (forecastList == null) {
                System.err.println("El campo 'list' es null. JSON completo: " + json);
                return null;
            }

            List<Weather.ForecastEntry> forecast = new ArrayList<>();
            for (int i = 0; i < forecastList.size(); i++) {
                JsonObject entry = forecastList.get(i).getAsJsonObject();
                double temp = entry.getAsJsonObject("main").get("temp").getAsDouble();
                int humidity = entry.getAsJsonObject("main").get("humidity").getAsInt();
                long timestamp = entry.get("dt").getAsLong();
                String dateTime = entry.get("dt_txt").getAsString();
                forecast.add(new Weather.ForecastEntry(temp, humidity, timestamp, dateTime));
            }
            return new Weather(forecast);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}