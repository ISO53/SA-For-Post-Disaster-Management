package com.iso53;

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
        this.name = getNameFromStatus(status);
        this.index = index;
        this.lat = lat;
        this.lon = lon;
    }

    private static String getNameFromStatus(String status) {
        int pieceLength = status.length() / 3;

        String piece1 = status.substring(0, pieceLength);
        String piece2 = status.substring(pieceLength, 2 * pieceLength);
        String piece3 = status.substring(2 * pieceLength);

        StringBuilder sb = new StringBuilder();

        if (!piece1.equals("000")) {
            sb.append(ProblemData.INCIDENT_NAMES[0]).append(", ");
        }

        if (!piece2.equals("000")) {
            sb.append(ProblemData.INCIDENT_NAMES[1]).append(", ");
        }

        if (!piece3.equals("000")) {
            sb.append(ProblemData.INCIDENT_NAMES[2]).append(", ");
        }

        return sb.delete(sb.length() - 2, sb.length()).toString();
    }

    public int getUnitType() {
        for(int i = 0; i < 3; i++) {
            String part = this.status.substring(i * 3, i * 3 + 3);
            if (!part.equals("000")) {
                return (i + 1) * 3 - 1;
            }
        }

        return -1;
    }

    @Override
    public String toString() {
        return String.format("(%.6f, %.6f) | Status: %s | Name: %s | Index: %d", this.lat, this.lon, this.status, this.name, this.index);
    }
}
