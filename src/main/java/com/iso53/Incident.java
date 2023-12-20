package com.iso53;

public class Incident {

    /**
     * Incident status represented with 9 bits
     * light-wounded / wounded / heavy-wounded / light-fire / fire / heavy-fire / light-ruin / ruin / heavy-ruin
     * The bits with 1 on it means there is an incident with specified incident type.
     * <p>
     * Example Incident Status -> 110100010
     * <p>
     * It Means:
     * light-wounded: 1 (there are light-wounded peoples)
     * wounded      : 1 (there are wounded peoples)
     * heavy-wounded: 0
     * light-fire   : 1 (there is a light-fire)
     * fire         : 0
     * heavy-fire   : 0
     * light-ruin   : 0
     * ruin         : 1 (there is a ruin)
     * heavy-ruin   : 0
     */
    private int status = 0;

    public Incident(String statusAsStr) {
        this.status = Integer.parseInt(statusAsStr, 2);
    }

    public void handledBy(Unit unit) {

    }
}
