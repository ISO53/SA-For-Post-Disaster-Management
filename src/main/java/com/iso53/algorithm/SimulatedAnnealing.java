package com.iso53.algorithm;

import com.iso53.mutation.InsertAlgorithm;
import com.iso53.mutation.InversionAlgorithm;
import com.iso53.mutation.MutationAlgorithm;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class SimulatedAnnealing {

    private final double coolingRate;
    private final Random random;
    private final Incident[] incidents;
    private double temperature;
    private MutationAlgorithm mutationAlgorithm;

    public SimulatedAnnealing(double temperature, double coolingRate, Incident[] incidents) {
        this.temperature = temperature;
        this.coolingRate = coolingRate;
        this.incidents = Utils.deepCopy(incidents);
        this.random = new Random();
        setAlgorithm(new InsertAlgorithm());
    }

    public Solution run() {
        // use grid search to find the best parameters for scheduler
//        GridSearch gridSearch = new GridSearch.Builder()
//                .waitCoefficient(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9)
//                .processTimeCoefficient(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9)
//                .distanceCoefficient(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9)
//                .sumOfNotHandledSeverityCoefficient(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9)
//                .unnecessaryPowerfulUnitPenaltyCoefficient(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9)
//                .build();
//        Map<GridSearch.Coefficient, Double> bestParameters = gridSearch.run();

        // initialize the Scheduler object with best parameters
//        Scheduler scheduler = new Scheduler(
//                bestParameters.get(GridSearch.Coefficient.DISTANCE),
//                bestParameters.get(GridSearch.Coefficient.PROCESS_TIME),
//                bestParameters.get(GridSearch.Coefficient.SUM_OF_NOT_HANDLED_SEVERITY),
//                bestParameters.get(GridSearch.Coefficient.WAIT),
//                bestParameters.get(GridSearch.Coefficient.UNNECESSARY_POWERFUL_UNIT_PENALTY)
//        );

        Scheduler scheduler = new Scheduler(0.25, 0.15, 0.25, 0.2, 0.15);

        // create incidents arr for first solution
        Incident[] currIncidents = this.incidents;

        // create current solution
        Solution currSolution = scheduler.schedule(currIncidents, ProblemData.UNITS.clone());

        // create first solution
        Solution bestSolution = scheduler.schedule(currIncidents, ProblemData.UNITS.clone());

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.0000", symbols);

        while (temperature > 1) {
            // create new incidents arr for new solution
            Incident[] newIncidents = (Incident[]) mutationAlgorithm.mutate(currIncidents);
            Solution newSolution = scheduler.schedule(newIncidents, ProblemData.UNITS.clone());

            // Get energy of solutions
            double currentEnergy = currSolution.getScore();
            double neighbourEnergy = newSolution.getScore();

            // Decide if we should accept the neighbour
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temperature) > random.nextDouble()) {
                currSolution = newSolution;
                currIncidents = newIncidents;
            }

            // Keep track of the best solution found
            if (newSolution.getScore() < bestSolution.getScore()) {
                bestSolution = newSolution;
            }

            System.out.print(df.format(bestSolution.getScore()) + " ,");

            // Cool system
            temperature *= 1 - coolingRate;
        }

        return bestSolution;
    }

    private double acceptanceProbability(double currentEnergy, double neighbourEnergy, double temperature) {
        // If the new solution is better, accept it
        if (neighbourEnergy < currentEnergy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((currentEnergy - neighbourEnergy) / temperature);
    }

    public void setAlgorithm(MutationAlgorithm mutationAlgorithm) {
        this.mutationAlgorithm = mutationAlgorithm;
    }
}
