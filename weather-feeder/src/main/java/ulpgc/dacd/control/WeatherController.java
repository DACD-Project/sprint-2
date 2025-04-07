package ulpgc.dacd.control;

import ulpgc.dacd.model.Weather;

public class WeatherController {
    private final WeatherProvider provider;
    private final WeatherStore store;

    public WeatherController(WeatherProvider provider, WeatherStore store) {
        this.provider = provider;
        this.store = store;
    }

    public void execute(double lat, double lon) {
        Weather weather = provider.getWeather(lat, lon);
        if (weather != null) {
            weather.getForecast().forEach(store::storeWeather);
        }
    }
}
