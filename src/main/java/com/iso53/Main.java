package com.iso53;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        printData();

        int size = 100000;

        // Solutions
        ArrayList<Solution> solutions = new ArrayList<>(size);

        // create copies so original data remains unchanged
        Incident[] incidentsArr = ProblemData.INCIDENTS.clone();

        for (int i = 0; i < size; i++) {
            Mutation.scramble(incidentsArr);
            Solution solution = Scheduler.schedule(deepCopy(incidentsArr), ProblemData.UNITS.clone());
            solutions.add(solution);
        }

        // Best solution overall
        Solution minSolution = Collections.min(solutions, Comparator.comparingDouble(Solution::getScore));


        System.out.println("******************************** RESULT ********************************");
        minSolution.printSolution();
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
}