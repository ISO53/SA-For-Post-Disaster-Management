package com.iso53.algorithm;

public class Incident {

    /**
     * Incident status represented with 9 bits
     * light-wounded / wounded / heavy-wounded / light-fire / fire / heavy-fire / light-ruin / ruin / heavy-ruin
     * The bits with 1 on it means there is an incident with specified incident type.
     * <p>
     * Example Incident Status -> 110100010
     * <p>
     * <p>It Means:
     * <p>light-wounded: 1 (there are light-wounded peoples)
     * <p>wounded      : 1 (there are wounded peoples)
     * <p>heavy-wounded: 0
     * <p>light-fire   : 1 (there is a light-fire)
     * <p>fire         : 0
     * <p>heavy-fire   : 0
     * <p>light-ruin   : 0
     * <p>ruin         : 1 (there is a ruin)
     * <p>heavy-ruin   : 0
     */
    String status;
    final String name;
    final double lat;
    final double lon;
    final int index;

    public Incident(String status, int index, double lat, double lon) {
        this.status = status;
        this.name = getNameFromStatus();
        this.index = index;
        this.lat = lat;
        this.lon = lon;
    }

    public Incident(Incident other) {
        this.status = other.status;
        this.name = other.name;
        this.lat = other.lat;
        this.lon = other.lon;
        this.index = other.index;
    }

    private String getNameFromStatus() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.status.length(); i += ProblemData.SEVERITY_CAPABILITY_COUNT) {
            String piece = status.substring(i, i + ProblemData.SEVERITY_CAPABILITY_COUNT);
            if (!piece.equals(ProblemData.NO_TYPE)) {
                sb.append(ProblemData.INCIDENT_NAMES[i / ProblemData.SEVERITY_CAPABILITY_COUNT]).append(", ");
            } else {
                sb.append("~".repeat(ProblemData.INCIDENT_NAMES[i / ProblemData.SEVERITY_CAPABILITY_COUNT].length())).append(", ");
            }
        }

        return sb.delete(sb.length() - 2, sb.length()).toString();
    }

    public int getTypeIndex(int n) {
        return this.status.substring(0, n + 1).lastIndexOf('1');
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return String.format("\n(%.6f, %.6f) | Status: %s | Name: %s | Index: %d", this.lat, this.lon, this.status, this.name, this.index);
    }
}
