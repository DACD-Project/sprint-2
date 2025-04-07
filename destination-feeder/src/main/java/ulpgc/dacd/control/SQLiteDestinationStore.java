package ulpgc.dacd.control;

import ulpgc.dacd.model.Destination;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLiteDestinationStore implements DestinationStore {
    private static final String DB_URL = "jdbc:sqlite:destinations.db";

    public SQLiteDestinationStore() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "CREATE TABLE IF NOT EXISTS destination (name TEXT, country TEXT, latitude REAL, longitude REAL, population INTEGER, distance REAL, PRIMARY KEY(name, country))";
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void storeDestination(Destination destination) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT OR REPLACE INTO destination (name, country, latitude, longitude, population, distance) VALUES (?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, destination.getName());
            stmt.setString(2, destination.getCountry());
            stmt.setDouble(3, destination.getLatitude());
            stmt.setDouble(4, destination.getLongitude());
            stmt.setInt(5, destination.getPopulation());
            stmt.setDouble(6, destination.getDistance());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
