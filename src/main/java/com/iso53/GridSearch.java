package com.iso53;

import java.util.*;
import java.util.concurrent.*;

public class GridSearch {

    public enum Coefficient {
        WAIT,
        PROCESS_TIME,
        DISTANCE,
        SUM_OF_NOT_HANDLED_SEVERITY,
        UNNECESSARY_POWERFUL_UNIT_PENALTY
    }

    private final List<Double> waitCoefficients;
    private final List<Double> processTimeCoefficients;
    private final List<Double> distanceCoefficients;
    private final List<Double> sumOfNotHandledSeverityCoefficients;
    private final List<Double> unnecessaryPowerfulUnitPenaltyCoefficients;

    private GridSearch(List<Double> waitCoefficients,
                       List<Double> processTimeCoefficients,
                       List<Double> distanceCoefficients,
                       List<Double> sumOfNotHandledSeverityCoefficients,
                       List<Double> unnecessaryPowerfulUnitPenaltyCoefficients) {
        this.waitCoefficients = waitCoefficients;
        this.processTimeCoefficients = processTimeCoefficients;
        this.distanceCoefficients = distanceCoefficients;
        this.sumOfNotHandledSeverityCoefficients = sumOfNotHandledSeverityCoefficients;
        this.unnecessaryPowerfulUnitPenaltyCoefficients = unnecessaryPowerfulUnitPenaltyCoefficients;
    }

    public Map<Coefficient, Double> run() {
        // initialized as object cuz lambda expressions must use final variables inside
        final List<Pair> synchronizedList = Collections.synchronizedList(new ArrayList<>());

        // Create executor for multi-thread processing
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // Iterate over all combinations of coefficients
        for (double distanceCoefficient : distanceCoefficients) {
            for (double processTimeCoefficient : processTimeCoefficients) {
                for (double sumOfNotHandledSeverityCoefficient : sumOfNotHandledSeverityCoefficients) {
                    for (double waitCoefficient : waitCoefficients) {
                        for (double unnecessaryPowerfulUnitPenaltyCoefficient : unnecessaryPowerfulUnitPenaltyCoefficients) {
                            executor.submit(() -> {
                                // Set the coefficients
                                Scheduler scheduler = new Scheduler(
                                        distanceCoefficient,
                                        processTimeCoefficient,
                                        sumOfNotHandledSeverityCoefficient,
                                        waitCoefficient,
                                        unnecessaryPowerfulUnitPenaltyCoefficient);

                                // Run the scheduler and get the score
                                double currScore = scheduler.schedule(
                                        Utils.deepCopy(ProblemData.INCIDENTS.clone()),
                                        ProblemData.UNITS.clone()).getScore();

                                // store the parameters
                                HashMap<Coefficient, Double> currParameters = new HashMap<>();
                                currParameters.put(Coefficient.WAIT, waitCoefficient);
                                currParameters.put(Coefficient.DISTANCE, distanceCoefficient);
                                currParameters.put(Coefficient.PROCESS_TIME, processTimeCoefficient);
                                currParameters.put(Coefficient.SUM_OF_NOT_HANDLED_SEVERITY, sumOfNotHandledSeverityCoefficient);
                                currParameters.put(Coefficient.UNNECESSARY_POWERFUL_UNIT_PENALTY, unnecessaryPowerfulUnitPenaltyCoefficient);

                                // store the results
                                synchronizedList.add(new Pair(currParameters, currScore));
                                return null;
                            });
                        }
                    }
                }
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Find the parameters with the lowest score
        Pair minPair = Collections.min(synchronizedList, Comparator.comparingDouble(Pair::getScore));

        return minPair.getParameters();
    }

    private static class Pair {
        final private HashMap<Coefficient, Double> parameters;
        final private double score;

        public Pair(HashMap<Coefficient, Double> parameters, double score) {
            this.parameters = parameters;
            this.score = score;
        }

        public HashMap<Coefficient, Double> getParameters() {
            return parameters;
        }

        public double getScore() {
            return score;
        }
    }

    public static class Builder {
        private final List<Double> waitCoefficients = new ArrayList<>();
        private final List<Double> processTimeCoefficients = new ArrayList<>();
        private final List<Double> distanceCoefficients = new ArrayList<>();
        private final List<Double> sumOfNotHandledSeverityCoefficients = new ArrayList<>();
        private final List<Double> unnecessaryPowerfulUnitPenaltyCoefficients = new ArrayList<>();

        public Builder waitCoefficient(double... values) {
            for (double value : values) {
                waitCoefficients.add(value);
            }
            return this;
        }

        public Builder processTimeCoefficient(double... values) {
            for (double value : values) {
                processTimeCoefficients.add(value);
            }
            return this;
        }

        public Builder distanceCoefficient(double... values) {
            for (double value : values) {
                distanceCoefficients.add(value);
            }
            return this;
        }

        public Builder sumOfNotHandledSeverityCoefficient(double... values) {
            for (double value : values) {
                sumOfNotHandledSeverityCoefficients.add(value);
            }
            return this;
        }

        public Builder unnecessaryPowerfulUnitPenaltyCoefficient(double... values) {
            for (double value : values) {
                unnecessaryPowerfulUnitPenaltyCoefficients.add(value);
            }
            return this;
        }

        public GridSearch build() {
            return new GridSearch(waitCoefficients, processTimeCoefficients, distanceCoefficients,
                    sumOfNotHandledSeverityCoefficients, unnecessaryPowerfulUnitPenaltyCoefficients);
        }
    }
}