package com.iso53.algorithm;

public class Unit {

    final String type;
    final String name;

    public Unit(String type) {
        this.type = type;
        this.name = getNameFromType();
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    private String getNameFromType() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.type.length(); i += ProblemData.SEVERITY_CAPABILITY_COUNT) {
            String piece = this.type.substring(i, i + ProblemData.SEVERITY_CAPABILITY_COUNT);
            if (!piece.equals(ProblemData.NO_TYPE)) {
                sb.append(ProblemData.UNIT_NAMES[i / ProblemData.SEVERITY_CAPABILITY_COUNT]).append(", ");
            } else {
                sb.append("~".repeat(ProblemData.UNIT_NAMES[i / ProblemData.SEVERITY_CAPABILITY_COUNT].length())).append(", ");
            }
        }

        return sb.delete(sb.length() - 2, sb.length()).toString();
    }

    public int getTypeIndex(int n) {
        return this.type.substring(0, n + 1).lastIndexOf('1');
    }

    @Override
    public String toString() {
        return String.format("\nType: %s, Name: %s", this.type, this.name);
    }
}
