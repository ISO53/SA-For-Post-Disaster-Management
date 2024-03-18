package com.iso53.algorithm;

import java.io.File;

public class ProblemData {

    /**
     * Length of process time and capabilities matrix. Should be calculated before any of the other data read.
     */
    public static int LENGTH_OF_PROCESS_TIME_AND_CAPABILITIES;

    /**
     * Length of process time and capabilities matrix. Should be calculated before any of the other data read.
     */
    public static int LENGTH_INCIDENTS_NAME;

    /**
     * Shows how many different types of severity (used in incidents) / capability (used in units) are in the given
     * problem data. Maximum level of severity must be equal to maximum level of capability. It is calculated from
     * Process Time and Capabilities.json file by dividing LENGTH_OF_PROCESS_TIME_AND_CAPABILITIES to
     * INCIDENT_NAMES.length or UNIT_NAMES.length (should be same).
     */
    public static int SEVERITY_CAPABILITY_COUNT;

    /**
     * A unit or incident part with no type should look like this.
     */
    public static String NO_TYPE;

    public static Headquarter HEADQUARTER;
    public static String[] INCIDENT_NAMES;
    public static String[] UNIT_NAMES;

    public static Incident[] INCIDENTS;
    public static Unit[] UNITS;

    public static double[][] DISTANCE_MATRIX;
    public static double[][] PROCESS_TIME_AND_CAPABILITIES;

    // Apply min-max scaling to data
    public static double[][] SCALED_DISTANCE_MATRIX;
    public static double[][] SCALED_PROCESS_TIME_AND_CAPABILITIES;

    public static void init(File processTimesFile, File incidentsFile, File unitsFile, File distanceMatrixFile) {
        LENGTH_OF_PROCESS_TIME_AND_CAPABILITIES = Utils.lengthOfProcessTimeAndCapabilities(processTimesFile);
        LENGTH_INCIDENTS_NAME = Utils.lengthOfIncidents(processTimesFile);

        SEVERITY_CAPABILITY_COUNT = LENGTH_OF_PROCESS_TIME_AND_CAPABILITIES / LENGTH_INCIDENTS_NAME;
        NO_TYPE = "0".repeat(SEVERITY_CAPABILITY_COUNT);

        INCIDENT_NAMES = Utils.jsonToNames(processTimesFile, "incident_names");
        UNIT_NAMES = Utils.jsonToNames(processTimesFile, "unit_names");

        INCIDENTS = Utils.jsonToIncidents(incidentsFile);
        UNITS = Utils.jsonToUnits(unitsFile);

        DISTANCE_MATRIX = Utils.jsonToMatrix(distanceMatrixFile);
        PROCESS_TIME_AND_CAPABILITIES = Utils.jsonToProcessTimeAndCapabilities(processTimesFile);

        SCALED_DISTANCE_MATRIX = Utils.minMaxScale(DISTANCE_MATRIX);
        SCALED_PROCESS_TIME_AND_CAPABILITIES = Utils.minMaxScale(PROCESS_TIME_AND_CAPABILITIES);
    }
}
