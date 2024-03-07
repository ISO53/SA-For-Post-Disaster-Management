package com.iso53;

import com.google.gson.*;

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

    public static List<Incident> jsonToIncidents(String filePath) {
        List<Incident> incidents = new ArrayList<>();

        try (InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream != null) {
                // Read the content of the JSON file
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                JsonArray incidentsAsJsonArr = JsonParser.parseReader(reader).getAsJsonArray();

                for (int i = 0; i < incidentsAsJsonArr.size(); i++) {
                    if (i == incidentsAsJsonArr.size() - 1) {
                        JsonObject headquarter = incidentsAsJsonArr.get(i).getAsJsonObject();
                        ProblemData.HEADQUARTER = new Headquarter(
                                i,
                                headquarter.get("latitude").getAsDouble(),
                                headquarter.get("longitude").getAsDouble()
                        );
                        break;
                    }
                    JsonObject incident = incidentsAsJsonArr.get(i).getAsJsonObject();
                    incidents.add(
                            new Incident(
                                    incident.get("status").getAsString(),
                                    i,
                                    incident.get("latitude").getAsDouble(),
                                    incident.get("longitude").getAsDouble()
                            )
                    );
                }
            } else {
                System.out.println("File not found: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return incidents;
    }

    public static List<Unit> jsonToUnits(String filePath) {
        List<Unit> units = new ArrayList<>();

        try (InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream != null) {
                // Read the content of the JSON file
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                JsonArray unitsAsJsonArr = JsonParser.parseReader(reader).getAsJsonArray();

                for (int i = 0; i < unitsAsJsonArr.size(); i++) {
                    units.add(new Unit(unitsAsJsonArr.get(i).getAsJsonObject().get("type").getAsString()));
                }
            } else {
                System.out.println("File not found: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return units;
    }

    public static String[] jsonToNames(String filePath, String type) {
        List<String> names = new ArrayList<>();

        try (InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream != null) {
                // Read the content of the JSON file
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                JsonArray namesAsJsonArr = jsonObject.getAsJsonArray(type);

                for (int i = 0; i < namesAsJsonArr.size(); i++) {
                    names.add(namesAsJsonArr.get(i).getAsString());
                }
            } else {
                System.out.println("File not found: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return names.toArray(new String[0]);
    }

    public static double[][] jsonToProcessTimeAndCapabilities(String filePath) {
        double[][] processTimeAndCapabilities = null;

        try (InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream != null) {
                // Read the content of the JSON file
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                JsonArray processArray = jsonObject.getAsJsonArray("process_time_and_capabilities");

                // Determine the dimensions of the array
                int rows = processArray.size();
                int cols = processArray.get(0).getAsJsonArray().size();

                // Initialize the matrix
                processTimeAndCapabilities = new double[rows][cols];

                // Fill the matrix with values from the JSON array
                for (int i = 0; i < rows; i++) {
                    JsonArray rowArray = processArray.get(i).getAsJsonArray();
                    for (int j = 0; j < cols; j++) {
                        processTimeAndCapabilities[i][j] = rowArray.get(j).getAsDouble();
                    }
                }
            } else {
                System.out.println("File not found: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return processTimeAndCapabilities;
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
