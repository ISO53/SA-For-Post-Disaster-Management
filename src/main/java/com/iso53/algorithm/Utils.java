package com.iso53.algorithm;

import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final Gson gson = new Gson();

    public static double[][] jsonToMatrix(File file) {
        // Load the JSON file as a resource
        try {
            URL url = file.toURI().toURL();
            try (InputStream inputStream = url.openStream()) {
                // Read the content of the JSON file
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                return gson.fromJson(reader, double[][].class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Incident[] jsonToIncidents(File file) {
        Incident[] incidents = null;

        try {
            URL url = file.toURI().toURL();
            try (InputStream inputStream = url.openStream()) {
                if (inputStream != null) {
                    // Read the content of the JSON file
                    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    JsonArray incidentsAsJsonArr = JsonParser.parseReader(reader).getAsJsonArray();

                    // Initialize the array
                    incidents = new Incident[incidentsAsJsonArr.size() - 1];

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
                        incidents[i] = new Incident(
                                incident.get("status").getAsString(),
                                i,
                                incident.get("latitude").getAsDouble(),
                                incident.get("longitude").getAsDouble()
                        );
                    }
                } else {
                    System.out.println("File not found: " + file.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return incidents;
    }

    public static Unit[] jsonToUnits(File file) {
        Unit[] units = null;

        try {
            URL url = file.toURI().toURL();
            try (InputStream inputStream = url.openStream()) {
                if (inputStream != null) {
                    // Read the content of the JSON file
                    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    JsonArray unitsAsJsonArr = JsonParser.parseReader(reader).getAsJsonArray();

                    // Initialize the array
                    units = new Unit[unitsAsJsonArr.size()];

                    for (int i = 0; i < unitsAsJsonArr.size(); i++) {
                        units[i] = new Unit(unitsAsJsonArr.get(i).getAsJsonObject().get("type").getAsString());
                    }
                } else {
                    System.out.println("File not found: " + file.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return units;
    }

    public static String[] jsonToNames(File file, String type) {
        List<String> names = new ArrayList<>();

        try {
            URL url = file.toURI().toURL();
            try (InputStream inputStream = url.openStream()) {
                if (inputStream != null) {
                    // Read the content of the JSON file
                    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                    JsonArray namesAsJsonArr = jsonObject.getAsJsonArray(type);

                    for (int i = 0; i < namesAsJsonArr.size(); i++) {
                        names.add(namesAsJsonArr.get(i).getAsString());
                    }
                } else {
                    System.out.println("File not found: " + file.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return names.toArray(new String[0]);
    }

    public static double[][] jsonToProcessTimeAndCapabilities(File file) {
        double[][] processTimeAndCapabilities = null;

        try {
            URL url = file.toURI().toURL();
            try (InputStream inputStream = url.openStream()) {
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
                    System.out.println("File not found: " + file.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return processTimeAndCapabilities;
    }

    public static int lengthOfProcessTimeAndCapabilities(File file) {
        int length = 0;

        try {
            URL url = file.toURI().toURL();
            try (InputStream inputStream = url.openStream()) {
                if (inputStream != null) {
                    // Read the content of the JSON file
                    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                    JsonArray processArray = jsonObject.getAsJsonArray("process_time_and_capabilities");
                    length = processArray.size();
                } else {
                    System.out.println("File not found: " + file.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return length;
    }

    public static int lengthOfIncidents(File file) {
        int length = 0;

        try {
            URL url = file.toURI().toURL();
            try (InputStream inputStream = url.openStream()) {
                if (inputStream != null) {
                    // Read the content of the JSON file
                    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                    JsonArray array = jsonObject.getAsJsonArray("incident_names");
                    length = array.size();
                } else {
                    System.out.println("File not found: " + file.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return length;
    }

    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == -1) {
                    System.out.print("  ~~  \t");
                } else {
                    System.out.printf("%.4f\t", matrix[i][j]);
                }
            }
            System.out.println(); // Move to the next line after each row
        }
    }

    public static double min(double[][] matrix) {
        double min = Double.MAX_VALUE;

        for (double[] row : matrix) {
            for (double value : row) {
                if (value <= 0) {
                    continue;
                }
                if (value < min) {
                    min = value;
                }
            }
        }

        return min;
    }

    public static double max(double[][] matrix) {
        double max = Double.MIN_VALUE;

        for (double[] row : matrix) {
            for (double value : row) {
                if (value <= 0) {
                    continue;
                }
                if (value > max) {
                    max = value;
                }
            }
        }

        return max;
    }

    public static double[][] minMaxScale(double[][] matrix) {
        // Find the minimum and maximum values
        double min = Math.min(min(ProblemData.DISTANCE_MATRIX), min(ProblemData.PROCESS_TIME_AND_CAPABILITIES));
        double max = Math.max(max(ProblemData.DISTANCE_MATRIX), max(ProblemData.PROCESS_TIME_AND_CAPABILITIES));

        double range = max - min;

        // Apply min-max scaling
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] > 0) {
                    matrix[i][j] = (matrix[i][j] - min) / range;
                }
            }
        }

        return matrix;
    }

    public static Incident[] deepCopy(Incident[] incidentsArr) {
        if (incidentsArr == null) {
            return null;
        }

        Incident[] copy = new Incident[incidentsArr.length];

        for (int i = 0; i < incidentsArr.length; i++) {
            // Perform a deep copy of each Incident object
            copy[i] = new Incident(incidentsArr[i]); // Assuming Incident has a copy constructor
        }

        return copy;
    }
}
