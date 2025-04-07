package ulpgc.dacd.control;

import ulpgc.dacd.model.Weather.ForecastEntry;

public interface WeatherStorer {
    void storeWeather(ForecastEntry weather);
}
