package com.example.farm;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {

    private static final String GEO_API_URL = "https://geocoding-api.open-meteo.com/v1/search?name=%s&count=1&language=en&format=json";
    private static final String WEATHER_API_URL = "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current=temperature_2m,relative_humidity_2m,weather_code&daily=precipitation_sum&timezone=auto";

    public WeatherData getWeather(String city, String country) {
        try {
            // Append country to city search for better accuracy if needed, but per user
            // code we use city
            String geoUrl = String.format(GEO_API_URL, city.replace(" ", "+"));
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest geoRequest = HttpRequest.newBuilder().uri(URI.create(geoUrl)).build();
            HttpResponse<String> geoResponse = client.send(geoRequest, HttpResponse.BodyHandlers.ofString());

            JsonObject geoJson = JsonParser.parseString(geoResponse.body()).getAsJsonObject();
            if (!geoJson.has("results"))
                return null;

            JsonObject response = geoJson.getAsJsonArray("results").get(0).getAsJsonObject();
            double lat = response.get("latitude").getAsDouble();
            double lon = response.get("longitude").getAsDouble();

            String weatherUrl = String.format(WEATHER_API_URL, lat, lon);
            HttpRequest weatherRequest = HttpRequest.newBuilder().uri(URI.create(weatherUrl)).build();
            HttpResponse<String> weatherResponse = client.send(weatherRequest, HttpResponse.BodyHandlers.ofString());

            JsonObject weatherJson = JsonParser.parseString(weatherResponse.body()).getAsJsonObject();
            JsonObject current = weatherJson.getAsJsonObject("current");
            JsonObject daily = weatherJson.getAsJsonObject("daily");

            int temp = current.get("temperature_2m").getAsInt();
            int humidity = current.get("relative_humidity_2m").getAsInt();
            int code = current.get("weather_code").getAsInt();
            String desc = decodeWeatherCode(code);

            double rainSum = daily.getAsJsonArray("precipitation_sum").get(0).getAsDouble();
            boolean rainExpected = rainSum > 0.5;

            return new WeatherData(temp, desc, humidity, rainExpected);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String decodeWeatherCode(int code) {
        if (code == 0)
            return "Clear Sky";
        if (code >= 1 && code <= 3)
            return "Partly Cloudy";
        if (code >= 45 && code <= 48)
            return "Foggy";
        if (code >= 51 && code <= 55)
            return "Drizzle";
        if (code >= 61 && code <= 67)
            return "Rain";
        if (code >= 71 && code <= 77)
            return "Snow";
        if (code >= 80 && code <= 82)
            return "Showers";
        if (code >= 95 && code <= 99)
            return "Thunderstorm";
        return "Unknown";
    }
}
