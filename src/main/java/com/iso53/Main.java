package com.iso53;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        printData();

        // create copies so original data remains unchanged
        List<Incident> incidents = Arrays.asList(ProblemData.INCIDENTS.clone());

        //        Collections.shuffle(incidents);

        Scheduler.Solution solution = Scheduler.schedule(incidents.toArray(new Incident[0]), ProblemData.UNITS.clone());

        System.out.println("******************************** RESULT ********************************");
        solution.printSolution();
    }

    public static void printData() {
        System.out.println();

        System.out.println("***************************** Incident Names *****************************");
        System.out.println(Arrays.toString(ProblemData.INCIDENT_NAMES));
        System.out.println();

        System.out.println("******************************* Unit Names *******************************");
        System.out.println(Arrays.toString(ProblemData.UNIT_NAMES));
        System.out.println();

        System.out.println("******************************* Incidents ********************************");
        System.out.println(Arrays.toString(ProblemData.INCIDENTS));
        System.out.println();

        System.out.println("********************************* Units **********************************");
        System.out.println(Arrays.toString(ProblemData.UNITS));
        System.out.println();

        System.out.println("**************************** Distance Matrix *****************************");
        Utils.printMatrix(ProblemData.DISTANCE_MATRIX);
        System.out.println();

        System.out.println("******************************* Process Time and Capabilities *******************************");
        Utils.printMatrix(ProblemData.PROCESS_TIME_AND_CAPABILITIES);
        System.out.println();
    }

    public static Incident[] deepCopy(Incident[] incidentsArr) {
        if (incidentsArr == null) {
            return null;
        }

        Incident[] copy = new Incident[incidentsArr.length];

        for (int i = 0; i < incidentsArr.length; i++) {
            // Perform a deep copy of each Incident object
            copy[i] = new Incident(incidentsArr[i]); // Assuming Incident has a copy constructor
        }

        return copy;
    }
}