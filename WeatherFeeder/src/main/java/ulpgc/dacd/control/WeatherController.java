package ulpgc.dacd.control;

import ulpgc.dacd.model.Weather;

public class WeatherController {
    private final WeatherProvider provider;
    private final WeatherStorer storer;

    public WeatherController(WeatherProvider provider, WeatherStorer storer) {
        this.provider = provider;
        this.storer = storer;
    }

    public void execute(double lat, double lon) {
        Weather weather = provider.getWeather(lat, lon);
        if (weather != null) {
            weather.getForecast().forEach(storer::storeWeather);
        }
    }
}