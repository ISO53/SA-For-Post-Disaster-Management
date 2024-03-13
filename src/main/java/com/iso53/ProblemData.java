package com.iso53;

public class ProblemData {

    /**
     * Length of process time and capabilities matrix. Should be calculated before any of the other data read.
     */
    public static final int LENGTH_OF_PROCESS_TIME_AND_CAPABILITIES = Utils.lengthOfProcessTimeAndCapabilities("Process Time and Capabilities.json");

    /**
     * Length of process time and capabilities matrix. Should be calculated before any of the other data read.
     */
    public static final int LENGTH_INCIDENTS_NAME = Utils.lengthOfIncidents("Process Time and Capabilities.json");

    /**
     * Shows how many different types of severity (used in incidents) / capability (used in units) are in the given
     * problem data. Maximum level of severity must be equal to maximum level of capability. It is calculated from
     * Process Time and Capabilities.json file by dividing LENGTH_OF_PROCESS_TIME_AND_CAPABILITIES to
     * INCIDENT_NAMES.length or UNIT_NAMES.length (should be same).
     */
    public static final int SEVERITY_CAPABILITY_COUNT = LENGTH_OF_PROCESS_TIME_AND_CAPABILITIES / LENGTH_INCIDENTS_NAME;

    /**
     * A unit or incident part with no type should look like this.
     */
    public static final String NO_TYPE = "0".repeat(SEVERITY_CAPABILITY_COUNT);

    public static Headquarter HEADQUARTER; // Initialized on another method
    public static String[] INCIDENT_NAMES = Utils.jsonToNames("Process Time and Capabilities.json", "incident_names");
    public static String[] UNIT_NAMES = Utils.jsonToNames("Process Time and Capabilities.json", "unit_names");

    public static Incident[] INCIDENTS = Utils.jsonToIncidents("Incident Types and Locations.json");
    public static Unit[] UNITS = Utils.jsonToUnits("Unit Types.json");

    public static double[][] DISTANCE_MATRIX = Utils.jsonToMatrix("Distance Matrix.json");
    public static double[][] PROCESS_TIME_AND_CAPABILITIES = Utils.jsonToProcessTimeAndCapabilities("Process Time and Capabilities.json");

    // Apply min-max scaling to data
    public static double[][] SCALED_DISTANCE_MATRIX = Utils.minMaxScale(DISTANCE_MATRIX);
    public static double[][] SCALED_PROCESS_TIME_AND_CAPABILITIES = Utils.minMaxScale(PROCESS_TIME_AND_CAPABILITIES);
}
