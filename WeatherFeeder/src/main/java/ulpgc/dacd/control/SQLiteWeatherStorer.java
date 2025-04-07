package ulpgc.dacd.control;

import ulpgc.dacd.model.Weather.ForecastEntry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLiteWeatherStorer implements WeatherStorer {
    private static final String DB_URL = "jdbc:sqlite:weather.db";

    public SQLiteWeatherStorer() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "CREATE TABLE IF NOT EXISTS weather (timestamp INTEGER PRIMARY KEY, temperature REAL, humidity INTEGER, date_time TEXT)";
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void storeWeather(ForecastEntry weather) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT OR REPLACE INTO weather (timestamp, temperature, humidity, date_time) VALUES (?, ?, ?, ?)")) {
            stmt.setLong(1, weather.getTimestamp());
            stmt.setDouble(2, weather.getTemperature());
            stmt.setInt(3, weather.getHumidity());
            stmt.setString(4, weather.getDateTime());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
