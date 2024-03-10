package com.iso53;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Scheduler {
    
    /**
     * Shows how much the time value should increase on each for loop. (Waiting Time Scheduling)
     */
    private static final double TIME_INCREASE_VALUE = 1 / ((double) (ProblemData.INCIDENTS.length));

    /**
     * A fully handled incident status should look like this
     */
    private static final String HANDLED_INCIDENT = "0".repeat(ProblemData.LENGTH_OF_PROCESS_TIME_AND_CAPABILITIES);

    /**
     * Shows the sum of severity capability count starting from 1 to SEVERITY_CAPABILITY_COUNT. Let's say the
     * SEVERITY_CAPABILITY_COUNT is equal to 3. That means there are 3 level of severity for incidents and 3 level of
     * capability for units. From left to right their points starts from 1 to SEVERITY_CAPABILITY_COUNT. If a unit with
     * 111 type handles an incident with 111 type you got:
     * 1 point from first bit
     * 2 point from second bit
     * 3 point from third bit, in total of 6 points. This number used for calculating how many different levels of
     * incident a unit can handle on an incident. The more level the unit got or the more the severity increases the
     * more point it gets. Calculated like -> [1 + 2 + 3 + ... + n] => [n * (n + 1) / 2]
     */
    private static final double SUM_OF_SEVERITY_CAPABILITY = (double) (ProblemData.SEVERITY_CAPABILITY_COUNT * (ProblemData.SEVERITY_CAPABILITY_COUNT + 1)) / 2;

    /**
     * Used to weight the importance of the distance between the unit's location and the incident's location in the
     * final result calculation. The value is multiplied by the distance.
     */
    private static final double DISTANCE_COEFFICIENT = 0.40;

    /**
     * Used to weight the importance of the process time in the final result calculation. The value is multiplied by the
     * process time.
     */
    private static final double PROCESS_TIME_COEFFICIENT = 0.45;

    /**
     * Used to weight the importance of the severity of incidents not handled by the unit in the final result
     * calculation. The value is multiplied by the severity.
     */
    private static final double SUM_OF_NOT_HANDLED_SEVERITY_COEFFICIENT = 0.25;

    /**
     * Used to weight the importance of the unit's wait time in the final result calculation. The more a unit waits
     * (isn't assigned to an incident in each for loop), more the wait time value increases of that Unit. The value is
     * then multiplied by the wait time.
     */
    private static final double WAIT_COEFFICIENT = 0.5;
        }

        System.out.println("INCIDENT  | UNIT      | RESULT");
        System.out.println("------------------------------");

        // Iterate incidents
        for (int i = 0; i < incidents.size(); i++) {
            Incident inc = incidents.get(i);
            double[] results = new double[units.size()];

            // Create unit wrappers
            ArrayList<UnitWrapper> unitWrappers = new ArrayList<>(units.size());
            for (Unit unit : units) {
                unitWrappers.add(new UnitWrapper(unit, 0));
            }

            // Iterate units to find the best one
            for (int j = 0; j < unitWrappers.size(); j++) {

                char[] result = new char[inc.status.length()];
                int sumOfHandledSeverity = handle(result, inc.status, unitWrappers.get(j).unit.type);

                // If this unit can't handle that incident make the result MAX because we're looking for the minimum
                // result value
                if (sumOfHandledSeverity == -1) {
                    results[j] = Integer.MAX_VALUE;
                    continue;
                }

                // Calculate each value
                double severityValue = (6 - sumOfHandledSeverity);
                double distanceValue = ProblemData.DISTANCE_MATRIX[unitWrappers.get(j).unit.lastLocationIndex][inc.index];
                double processTimeValue = ProblemData.PROCESS_TIME_AND_CAPABILITIES[unitWrappers.get(j).unit.getUnitType()][inc.getUnitType()];

                // Calculate the result for that unit and incident pair
                results[j] =
                        severityValue * SUM_OF_NOT_HANDLED_SEVERITY_COEFFICIENT +
                                distanceValue * DISTANCE_COEFFICIENT +
                                processTimeValue * PROCESS_TIME_COEFFICIENT +
                                unitWrappers.get(j).waitTime * WAIT_COEFFICIENT;
            }

            // Find the best unit
            int bestUnitIndex = minElementIndex(results);
            Unit bestUnit = units.get(bestUnitIndex);

            // Handle incident with best unit
            char[] handledIncident = inc.status.toCharArray();
            handle(handledIncident, inc.status, bestUnit.type);
            String resultBar = bar('d', ProblemData.DISTANCE_MATRIX[unitWrappers.get(bestUnitIndex).unit.lastLocationIndex][inc.index]) + " " + bar('p', ProblemData.PROCESS_TIME_AND_CAPABILITIES[unitWrappers.get(bestUnitIndex).unit.getUnitType()][inc.getUnitType()]);
            finalResult.get(bestUnitIndex).add(resultBar);
            bestUnit.lastLocationIndex = inc.index;
            unitWrappers.get(bestUnitIndex).waitTime = 0;
//            finalResult.get(bestUnitIndex).add(inc.status);
//            finalResult.get(bestUnitIndex).add(inc.index + "");
            System.out.println(inc.status + " | " + bestUnit.type + " | " + String.valueOf(handledIncident));
            inc.status = String.valueOf(handledIncident);

            // If this incident not fully handled yet, re-assign unit to this
            if (!inc.status.equals("000000000")) {
                // incident fully handled
                i--;
            }

            // Increase not used unit's wait time
            for (UnitWrapper unitWrapper : unitWrappers) {
                if (bestUnit != unitWrapper.unit) {
                    unitWrapper.waitTime++;
                }
            }
        }

        return finalResult;
    }

    public static int handle(char[] result, String incident, String unit) {
        int handledSeverity = 0;

        for (int i = 0; i < incident.length(); i++) {
            if (incident.charAt(i) == '1' && unit.charAt(i) == '1') {
                result[i] = '0';
                handledSeverity += i % 3 + 1;
            }
        }

        return handledSeverity == 0 ? -1 : handledSeverity;
    }

    public static int minElementIndex(double[] arr) {
        double currMin = Double.MAX_VALUE;
        int index = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < currMin) {
                currMin = arr[i];
                index = i;
            }
        }

        return index;
    }

    public static String bar(char type, double length) {
        if (type == 'd') {
            char[] chars = new char[(int) (length)];
            Arrays.fill(chars, '_');
            return new String(chars);
        } else if (type == 'p') {
            char[] chars = new char[(int) (length / 5)];
            Arrays.fill(chars, '#');
            return new String(chars);
        }
        return null;
    }

    private static class UnitWrapper {
        public Unit unit;
        public double waitTime;
        public int lastLocationIndex;

        public UnitWrapper(Unit unit) {
            this.unit = unit;
            this.waitTime = 0.0;
            this.lastLocationIndex = ProblemData.HEADQUARTER.index; // All units start at HQ
        }
    }

    public static class Solution {

        private final ArrayList<LinkedList<Event>> solution;

        public Solution(int size) {
            solution = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                solution.add(new LinkedList<>());
            }
        }

        public void add(Event event, int bestUnitIndex) {
            this.solution.get(bestUnitIndex).add(event);
        }

        public void printSolution() {
            for (int i = 0; i < solution.size(); i++) {
                System.out.print("Unit " + i + " ==>  ");
                for (Event event : solution.get(i)) {
                    System.out.print(event.bar());
                }
                System.out.println();
            }
        }
    }

    public static class Event {

        private final double distanceTime;
        private final double processTime;
        private final double handledSeverityPoint;
        private final double waitValue;
        private final String result;
        private final double score;
        private final String unitType;
        private final String incidentStatus;

        public Event(double distanceTime, double processTime, double handledSeverityPoint, double waitValue, String result, String unitType, String incidentStatus) {
            this.distanceTime = distanceTime;
            this.processTime = processTime;
            this.handledSeverityPoint = handledSeverityPoint;
            this.waitValue = waitValue;
            this.result = result;
            this.score = calculateScore();
            this.unitType = unitType;
            this.incidentStatus = incidentStatus;
        }

        public double getDistanceTime() {
            return distanceTime;
        }

        public double getProcessTime() {
            return processTime;
        }

        public double getHandledSeverityPoint() {
            return handledSeverityPoint;
        }

        public double getWaitValue() {
            return waitValue;
        }

        public String getResult() {
            return result;
        }

        public double getScore() {
            return score;
        }

        private double calculateScore() {
            // If this unit can't handle that incident make the result MAX because we're looking for the minimum result
            // value
            if (this.handledSeverityPoint == 1) {
                return Double.MAX_VALUE;
            }

            return this.handledSeverityPoint * SUM_OF_NOT_HANDLED_SEVERITY_COEFFICIENT +
                    this.distanceTime * DISTANCE_COEFFICIENT +
                    this.processTime * PROCESS_TIME_COEFFICIENT +
                    (3 - this.waitValue) * WAIT_COEFFICIENT; //şimdilik
        }

        public String bar() {
            // Show distance as '_' and process as '#' character
            return "_".repeat((int) (this.distanceTime * 10) + 1) +
                    "|".repeat((int) (this.processTime * 10) + 1);
        }

        public String getUnitType() {
            return unitType;
        }

        public String getIncidentStatus() {
            return incidentStatus;
        }
    }
}
