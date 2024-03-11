package com.iso53;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        printData();

        System.out.println("\n------------- TEST BEGINS -------------\n");

        List<Incident> incidents = Arrays.asList(ProblemData.INCIDENTS.clone());

//        Collections.shuffle(incidents);

        // create copies so original data remains unchanged
        Scheduler.Solution solution = Scheduler.schedule(incidents.toArray(new Incident[0]), ProblemData.UNITS.clone());
        System.out.println("\n-------------- TEST ENDS --------------\n");

        System.out.println("\n------ RESULT ------");
        solution.printSolution();

    }

    public static void printData() {
        System.out.println("------- Incident Names Started -------");
        System.out.println(Arrays.toString(ProblemData.INCIDENT_NAMES));
        System.out.println("-------- Incident Names Ended --------\n");

        System.out.println("------- Unit Names Started -------");
        System.out.println(Arrays.toString(ProblemData.UNIT_NAMES));
        System.out.println("-------- Unit Names Ended --------\n");

        System.out.println("------- Incidents Started -------");
        System.out.println(Arrays.toString(ProblemData.INCIDENTS));
        System.out.println("-------- Incidents Ended --------\n");

        System.out.println("------- Units Started -------");
        System.out.println(Arrays.toString(ProblemData.UNITS));
        System.out.println("-------- Units  Ended --------\n");

        System.out.println("------- Distance Matrix Started -------");
        Utils.printMatrix(ProblemData.DISTANCE_MATRIX);
        System.out.println("-------- Distance Matrix Ended --------\n");

        System.out.println("------ Process Time and Capabilities Started ------");
        Utils.printMatrix(ProblemData.PROCESS_TIME_AND_CAPABILITIES);
        System.out.println("------- Process Time and Capabilities Ended -------\n");
    }
}