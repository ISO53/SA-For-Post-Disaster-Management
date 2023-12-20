package com.iso53;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final Gson gson = new Gson();

    public static double[][] jsonToMatrix(String filePath) {
        // Load the JSON file as a resource
        try (InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream != null) {
                // Read the content of the JSON file
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                return gson.fromJson(reader, double[][].class);
            } else {
                System.out.println("File not found: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<District> jsonToDistricts(String filePath) {
        List<District> districts = new ArrayList<>();

        try (InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream != null) {
                // Read the content of the JSON file
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                JsonArray neighborhoods = jsonObject.getAsJsonArray("neighborhoods");

                for (int i = 0; i < neighborhoods.size(); i++) {
                    JsonObject neighborhood = neighborhoods.get(i).getAsJsonObject();
                    districts.add(
                            new District(
                                    neighborhood.get("latitude").getAsDouble(),
                                    neighborhood.get("longitude").getAsDouble(),
                                    neighborhood.get("name").getAsString()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return districts;
    }

    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%.4f\t", matrix[i][j]);
            }
            System.out.println(); // Move to the next line after each row
        }
    }
}
