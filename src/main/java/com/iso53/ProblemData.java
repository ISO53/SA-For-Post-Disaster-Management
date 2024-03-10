package com.iso53;

import java.util.List;

public class ProblemData {
    public static Headquarter HEADQUARTER;
    public static String[] INCIDENT_NAMES = Utils.jsonToNames("Process Time and Capabilities.json", "incident_names");
    public static String[] UNIT_NAMES = Utils.jsonToNames("Process Time and Capabilities.json", "unit_names");

    public static List<Incident> INCIDENTS = Utils.jsonToIncidents("Incident Types and Locations.json");
    public static List<Unit> UNITS = Utils.jsonToUnits("Unit Types.json");

    public static double[][] DISTANCE_MATRIX = Utils.jsonToMatrix("Distance Matrix.json");
    public static double[][] PROCESS_TIME_AND_CAPABILITIES = Utils.jsonToProcessTimeAndCapabilities("Process Time and Capabilities.json");

    // Apply min-max scaling to data
    public static double[][] SCALED_DISTANCE_MATRIX = Utils.minMaxScale(DISTANCE_MATRIX);
    public static double[][] SCALED_PROCESS_TIME_AND_CAPABILITIES = Utils.minMaxScale(PROCESS_TIME_AND_CAPABILITIES);

}
