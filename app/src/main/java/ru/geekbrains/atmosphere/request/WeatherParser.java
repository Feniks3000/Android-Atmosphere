package ru.geekbrains.atmosphere.request;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;

public class WeatherParser {

    public static WeatherRequest parser(BufferedReader bufferedReader) throws IOException {
        return (new Gson()).fromJson(convertToString(bufferedReader), WeatherRequest.class);
    }

    private static String convertToString(BufferedReader bufferedReader) throws IOException {
        // Since version API 24 => return bufferedReader.lines().collect(Collectors.joining("\n"));

        StringBuilder stringWithJson = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringWithJson.append(line).append("\n");
        }
        return stringWithJson.toString();
    }

}
