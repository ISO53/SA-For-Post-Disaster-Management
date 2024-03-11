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

    public static Solution schedule(Incident[] incidents, Unit[] units) {
        // Initialize solution
        Solution solution = new Solution(units.length);

        // Create unit wrappers
        UnitWrapper[] unitWrappers = new UnitWrapper[units.length];
        for (int i = 0; i < units.length; i++) {
            unitWrappers[i] = new UnitWrapper(units[i]);
        }

        // Iterate incidents
        for (int i = 0; i < incidents.length; i++) {

            // Create an array to store the events for each unit-incident pair
            Event[] events = new Event[units.length];

            // Iterate units to collect all possible unit-incident pairs (Events)
            for (int j = 0; j < unitWrappers.length; j++) {
                events[j] = handle(incidents[i], unitWrappers[j]);
            }

            // Find the best unit
            int bestUnitIndex = findBestUnitIndex(events);
            Unit bestUnit = units[bestUnitIndex];

            // Handle incident with best unit
            incidents[i].status = events[bestUnitIndex].result;

            // Add this event to solution
            solution.add(events[bestUnitIndex], bestUnitIndex);

            // Update the last location of the best unit with current incident location
            unitWrappers[bestUnitIndex].lastLocationIndex = incidents[i].index;

            // Reset the wait time of the best unit
            unitWrappers[bestUnitIndex].waitTime = 0;

            // If this incident not fully handled yet, re-assign unit to this
            if (!incidents[i].status.equals(HANDLED_INCIDENT)) {
                // incident not fully handled
                i--;
            }

            // Increase not used unit's wait time
            for (UnitWrapper unitWrapper : unitWrappers) {
                if (bestUnit != unitWrapper.unit) {
                    unitWrapper.waitTime += TIME_INCREASE_VALUE;
                }
            }
        }

        return solution;
    }

    private static Event handle(Incident incident, UnitWrapper unitWrapper) {
        StringBuilder sb = new StringBuilder();

        // Calculate handled severities and total process time. Keep in mind that one unit may be handling more than one
        // event
        double handledSeverityPoint = 0;
        double totalProcessTime = 0;
        for (int i = 0; i < incident.status.length(); i++) {
            if (incident.status.charAt(i) == '1' && unitWrapper.unit.type.charAt(i) == '1') {
                sb.append('0');
                handledSeverityPoint += (i % ProblemData.SEVERITY_CAPABILITY_COUNT + 1) * (1 / SUM_OF_SEVERITY_CAPABILITY);
                totalProcessTime += ProblemData.SCALED_PROCESS_TIME_AND_CAPABILITIES[unitWrapper.unit.getTypeIndex(floorBased(i, ProblemData.SEVERITY_CAPABILITY_COUNT))][i];
            } else {
                sb.append(incident.status.charAt(i));
            }
        }

        // Calculate total distance time
        double totalDistanceTime = ProblemData.SCALED_DISTANCE_MATRIX[unitWrapper.lastLocationIndex][incident.index];

        // Get the remaining handled severity point cuz we're aiming for minimum
        handledSeverityPoint = 1 - handledSeverityPoint;

        return new Event(totalDistanceTime, totalProcessTime, handledSeverityPoint, unitWrapper.waitTime, sb.toString(), unitWrapper.unit.type, incident.status);
    }

    private static int findBestUnitIndex(Event[] events) {
        double currMin = Double.MAX_VALUE;
        int index = 0;

        for (int i = 0; i < events.length; i++) {
            if (events[i].score < currMin) {
                currMin = events[i].score;
                index = i;
            }
        }

        return index;
    }

    private static int floorBased(int number, int n) {
        return (number / n) * n + n - 1;
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

        public String getUnitType() {
            return unitType;
        }

        public String getIncidentStatus() {
            return incidentStatus;
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


        }
    }
}
