package ulpgc.dacd.control;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("Usage: java -jar weather-feeder.jar <API_KEY> <DB_PATH> <LAT> <LON>");
            return;
        }

        String apiKey = args[0];
        String dbPath = "jdbc:sqlite:" + args[1];
        double latitude = Double.parseDouble(args[2]);
        double longitude = Double.parseDouble(args[3]);

        String apiUrl = "https://api.openweathermap.org/data/2.5/forecast";

        WeatherProvider provider = new OpenWeatherMapProvider(apiKey, apiUrl);
        WeatherStore store = new SQLiteWeatherStore(dbPath);
        WeatherController controller = new WeatherController(provider, store);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> controller.execute(latitude, longitude), 0, 6, TimeUnit.HOURS);
    }
}
