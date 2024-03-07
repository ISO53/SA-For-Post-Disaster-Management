package com.iso53;

public class Unit {

    String type;
    String name;
    int lastLocationIndex;

    public Unit(String type) {
        this.type = type;
        this.name = getNameFromType(type);
        this.lastLocationIndex = 14;
    }

    private static String getNameFromType(String type) {
        int pieceLength = type.length() / 3;

        String piece1 = type.substring(0, pieceLength);
        String piece2 = type.substring(pieceLength, 2 * pieceLength);
        String piece3 = type.substring(2 * pieceLength);

        int n = 0;
        if (!piece1.equals("000")) {
            n = 0;
        } else if (!piece2.equals("000")) {
            n = 1;
        } else if (!piece3.equals("000")) {
            n = 2;
        }

        return ProblemData.UNIT_NAMES[n];
    }

    public boolean canHandle(int incident) {
//        return ProblemData.UNITS_CAPABILITIES[type][incident] != -1;
        // ??????
        return false;
    }

    public int getUnitType() {
        for(int i = 0; i < 3; i++) {
            String part = this.type.substring(i * 3, i * 3 + 3);
            if (!part.equals("000")) {
                return (i + 1) * 3 - 1;
            }
        }

        return -1;
    }

    @Override
    public String toString() {
        return String.format("Type: %s, Name: %s", this.type, this.name);
    }
}
