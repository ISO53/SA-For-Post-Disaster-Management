package com.iso53;

public class Unit {
    String name;
    int type;

    public Unit(int type) {
        this.type = type;
    }

    public boolean canHandle(int incident) {
        return ProblemData.UNITS_CAPABILITIES[type][incident] == 1;
    }

    public int getType() {
        return type;
    }
}
