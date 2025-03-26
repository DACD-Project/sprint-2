package ulpgc.dacd.control;

public class Main {
    public static void main(String[] args) {
        WeatherProvider provider = new OpenWeatherMapProvider();
        WeatherStorer storer = new SQLiteWeatherStorer();
        WeatherController controller = new WeatherController(provider, storer);
        controller.execute(40.4168, -3.7038); // Latitud y longitud de Madrid
    }
}