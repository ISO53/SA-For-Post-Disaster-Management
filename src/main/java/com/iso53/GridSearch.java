package com.iso53;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GridSearch {

    public enum Coefficient {
        WAIT,
        PROCESS_TIME,
        DISTANCE,
        SUM_OF_NOT_HANDLED_SEVERITY,
        UNNECESSARY_POWERFUL_UNIT_PENALTY
    }

    private final HashMap<Coefficient, Double> parameters;
    private final List<Double> waitCoefficients;
    private final List<Double> processTimeCoefficients;
    private final List<Double> distanceCoefficients;
    private final List<Double> sumOfNotHandledSeverityCoefficients;
    private final List<Double> unnecessaryPowerfulUnitPenaltyCoefficients;

    private GridSearch(List<Double> waitCoefficients, List<Double> processTimeCoefficients, List<Double> distanceCoefficients, List<Double> sumOfNotHandledSeverityCoefficients, List<Double> unnecessaryPowerfulUnitPenaltyCoefficients) {
        this.waitCoefficients = waitCoefficients;
        this.processTimeCoefficients = processTimeCoefficients;
        this.distanceCoefficients = distanceCoefficients;
        this.sumOfNotHandledSeverityCoefficients = sumOfNotHandledSeverityCoefficients;
        this.unnecessaryPowerfulUnitPenaltyCoefficients = unnecessaryPowerfulUnitPenaltyCoefficients;
        this.parameters = new HashMap<>(5);
    }

    public HashMap<Coefficient, Double> run() {
        // initialized as object cuz lambda expressions must use final variables inside
        final double[] bestScore = new double[]{Double.MAX_VALUE};

        // Iterate over all combinations of coefficients
        for (double distanceCoefficient : distanceCoefficients) {
            for (double processTimeCoefficient : processTimeCoefficients) {
                for (double sumOfNotHandledSeverityCoefficient : sumOfNotHandledSeverityCoefficients) {
                    for (double waitCoefficient : waitCoefficients) {
                        for (double unnecessaryPowerfulUnitPenaltyCoefficient : unnecessaryPowerfulUnitPenaltyCoefficients) {
                            // Set the coefficients
                            Scheduler scheduler = new Scheduler(distanceCoefficient, processTimeCoefficient, sumOfNotHandledSeverityCoefficient, waitCoefficient, unnecessaryPowerfulUnitPenaltyCoefficient);

                            // Run the scheduler and get the score
                            double score = scheduler.schedule(Utils.deepCopy(ProblemData.INCIDENTS.clone()), ProblemData.UNITS.clone()).getScore();

                            // store the best results
                            if (score < bestScore[0]) {
                                System.out.println(bestScore[0]);
                                bestScore[0] = score;
                                parameters.put(Coefficient.WAIT, waitCoefficient);
                                parameters.put(Coefficient.DISTANCE, distanceCoefficient);
                                parameters.put(Coefficient.PROCESS_TIME, processTimeCoefficient);
                                parameters.put(Coefficient.SUM_OF_NOT_HANDLED_SEVERITY, sumOfNotHandledSeverityCoefficient);
                                parameters.put(Coefficient.UNNECESSARY_POWERFUL_UNIT_PENALTY, unnecessaryPowerfulUnitPenaltyCoefficient);
                            }
                        }
                    }
                }
            }
        }

        return parameters;
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
            return new GridSearch(waitCoefficients, processTimeCoefficients, distanceCoefficients, sumOfNotHandledSeverityCoefficients, unnecessaryPowerfulUnitPenaltyCoefficients);
        }
    }
}