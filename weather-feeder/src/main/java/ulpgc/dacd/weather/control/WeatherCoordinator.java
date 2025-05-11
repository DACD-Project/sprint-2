package ulpgc.dacd.weather.control;

import ulpgc.dacd.weather.model.Weather;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WeatherCoordinator {

    public void run(String[] args) {
        String apiKey = args[0];
        String dbPath = "jdbc:sqlite:" + args[1];
        double latitude = Double.parseDouble(args[2]);
        double longitude = Double.parseDouble(args[3]);

        String apiUrl = "https://api.openweathermap.org/data/2.5/forecast";

        WeatherProvider provider = new OpenWeatherMapProvider(apiKey, apiUrl);
        WeatherStore store = new SqliteWeatherStore(dbPath);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            Weather weather = provider.getWeather(latitude, longitude);
            if (weather != null) {
                weather.getForecast().forEach(store::storeWeather);
            }
        }, 0, 6, TimeUnit.HOURS);
    }
}
