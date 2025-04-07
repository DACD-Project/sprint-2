package ulpgc.dacd.control;

import ulpgc.dacd.model.Weather;

public interface WeatherProvider {
    Weather getWeather(double lat, double lon);
}