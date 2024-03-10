package com.iso53;

public class Unit {

    final String type;
    final String name;

    public Unit(String type) {
        this.type = type;
        this.name = getNameFromType(type);
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

    public int getTypeIndex(int n) {
        return this.type.substring(0, n + 1).lastIndexOf('1');
    }

    @Override
    public String toString() {
        return String.format("\nType: %s, Name: %s", this.type, this.name);
    }
}
