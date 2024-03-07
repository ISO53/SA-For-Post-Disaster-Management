package com.iso53;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        printData();

        System.out.println("\n------------- TEST BEGINS -------------\n");

        ArrayList<Incident> incidents = new ArrayList<>(ProblemData.INCIDENTS);
        ArrayList<Unit> units = new ArrayList<>(ProblemData.UNITS);

//        Collections.shuffle(incidents);
//        Collections.shuffle(units);

        ArrayList<LinkedList<String>> result = Scheduler.schedule(incidents, units);

        System.out.println("\n------ RESULT ------");
        for (int i = 0; i < result.size(); i++) {
            System.out.print("Unit " + i + " ==>  ");
            for (String incidentStr : result.get(i)) {
                if (!incidentStr.isEmpty()) {
                    System.out.print(incidentStr + " "); //  + " -> "
                }
            }
            System.out.println();
        }
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