package ulpgc.dacd.weather.control;

import ulpgc.dacd.weather.model.Weather;
import ulpgc.dacd.weather.model.WeatherEvent;
import ulpgc.dacd.shared.Publisher;

import java.util.List;
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
        Publisher publisher = new Publisher("Weather");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            Weather weather = provider.getWeather(latitude, longitude);
            if (weather != null) {
                List<Weather.ForecastEntry> forecast = weather.getForecast();
                forecast.forEach(store::storeWeather);

                WeatherEvent event = new WeatherEvent("weather-feeder", forecast);
                publisher.publish(event);
            }
        }, 0, 6, TimeUnit.HOURS);
    }
}
