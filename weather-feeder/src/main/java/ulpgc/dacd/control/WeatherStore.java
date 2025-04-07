package ulpgc.dacd.control;

import ulpgc.dacd.model.Weather.ForecastEntry;

public interface WeatherStore {
    void storeWeather(ForecastEntry weather);
}
