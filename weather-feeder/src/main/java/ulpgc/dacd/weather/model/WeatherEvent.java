package ulpgc.dacd.weather.model;

import java.time.Instant;
import java.util.List;

public class WeatherEvent {
    public Instant ts;
    public String ss;
    public List<Weather.ForecastEntry> forecast;

    public WeatherEvent(String ss, List<Weather.ForecastEntry> forecast) {
        this.ts = Instant.now();
        this.ss = ss;
        this.forecast = forecast;
    }
}
