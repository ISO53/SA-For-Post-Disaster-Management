package com.iso53;

import java.util.*;

public class Main {
    public static void main(String[] args) {
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

        System.out.println("********************** Process Time and Capabilities *********************");
        Utils.printMatrix(ProblemData.PROCESS_TIME_AND_CAPABILITIES);
        System.out.println();

        SimulatedAnnealing SA = new SimulatedAnnealing(100, 0.003, ProblemData.INCIDENTS);
        Solution solution = SA.run();

        System.out.println("********************************* RESULT *********************************");
        solution.print();
    }
}