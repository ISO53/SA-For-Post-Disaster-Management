package com.iso53;

import java.util.List;

public class ProblemData {
    public static double[][] DISTANCE_MATRIX = Utils.jsonToMatrix("Bakırköy Distance Matrix.json");
    public static List<District> DISTRICT_LOCATIONS = Utils.jsonToDistricts("Bakırköy District Locations.json");
    public static double[][] UNITS_CAPABILITIES = Utils.jsonToMatrix("Units Capabilities.json");
    public static double[][] PROCESS_TIME = Utils.jsonToMatrix("Process Time.json");
}
