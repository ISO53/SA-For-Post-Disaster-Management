package com.iso53;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
//        printData();
    }

    public static void printData() {
        System.out.println("------- Incident Names Started -------");
        System.out.println(Arrays.toString(ProblemData.INCIDENT_NAMES));
        System.out.println("-------- Incident Names Ended --------\n");

        System.out.println("------- Unit Names Started -------");
        System.out.println(Arrays.toString(ProblemData.UNIT_NAMES));
        System.out.println("-------- Unit Names Ended --------\n");

        System.out.println("------- Incidents Started -------");
        ProblemData.INCIDENTS.forEach(System.out::println);
        System.out.println("-------- Incidents Ended --------\n");

        System.out.println("------- Units Started -------");
        ProblemData.UNITS.forEach(System.out::println);
        System.out.println("-------- Units  Ended --------\n");

        System.out.println("------- Distance Matrix Started -------");
        Utils.printMatrix(ProblemData.DISTANCE_MATRIX);
        System.out.println("-------- Distance Matrix Ended --------\n");

        System.out.println("------ Process Time and Capabilities Started ------");
        Utils.printMatrix(ProblemData.PROCESS_TIME_AND_CAPABILITIES);
        System.out.println("------- Process Time and Capabilities Ended -------");
    }
}