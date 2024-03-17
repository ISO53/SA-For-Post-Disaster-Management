package com.iso53;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        printData();

        // use grid search to find best parameters
        GridSearch gridSearch = new GridSearch.Builder()
                .waitCoefficient(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9)
                .processTimeCoefficient(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9)
                .distanceCoefficient(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9)
                .sumOfNotHandledSeverityCoefficient(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9)
                .unnecessaryPowerfulUnitPenaltyCoefficient(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9)
                .build();
        Map<GridSearch.Coefficient, Double> bestParameters = gridSearch.run();

        // print the best parameters
        System.out.println("**************************** BEST PARAMETERS ***************************");
        for (Map.Entry<GridSearch.Coefficient, Double> entry : bestParameters.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // initialize the Scheduler object with best parameters
        Scheduler scheduler = new Scheduler(
                bestParameters.get(GridSearch.Coefficient.DISTANCE),
                bestParameters.get(GridSearch.Coefficient.PROCESS_TIME),
                bestParameters.get(GridSearch.Coefficient.SUM_OF_NOT_HANDLED_SEVERITY),
                bestParameters.get(GridSearch.Coefficient.WAIT),
                bestParameters.get(GridSearch.Coefficient.UNNECESSARY_POWERFUL_UNIT_PENALTY)
        );

        // iteration count
        int size = 100000;

        // store the solutions here
        ArrayList<Solution> solutions = new ArrayList<>(size);

        // create copies so original data remains unchanged
        Incident[] incidentsArr = ProblemData.INCIDENTS.clone();

        // generic algorithm-ish
        for (int i = 0; i < size; i++) {
            Mutation.insert(incidentsArr);
            Solution solution = scheduler.schedule(Utils.deepCopy(incidentsArr), ProblemData.UNITS.clone());
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