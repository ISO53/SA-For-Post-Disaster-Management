package com.iso53.algorithm;

public class Scheduler {

    /**
     * Shows how may event can occur in worst case scenario. The worst case is when every unit completes only one bit of
     * incident. Calculates by counting all the '1' bits in all the incidents.
     */
    private static final double MAX_NUMBER_OF_EVENT_COUNT = Event.getMaxNumberOfEventCount();

    /**
     * Shows how much the time value should increase on each for loop. (Waiting Time Scheduling). Divided by
     * MAX_NUMBER_OF_EVENT_COUNT to min-max scale the time values.
     */
    private static final double TIME_INCREASE_VALUE = 1 / MAX_NUMBER_OF_EVENT_COUNT;

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
    public static double DISTANCE_COEFFICIENT = 0.10;

    /**
     * Used to weight the importance of the process time in the final result calculation. The value is multiplied by the
     * process time.
     */
    public static double PROCESS_TIME_COEFFICIENT = 0.15;

    /**
     * Used to weight the importance of the severity of incidents not handled by the unit in the final result
     * calculation. The value is multiplied by the severity.
     */
    public static double SUM_OF_NOT_HANDLED_SEVERITY_COEFFICIENT = 0.25;

    /**
     * Used to weight the importance of the unit's wait time in the final result calculation. The more a unit waits
     * (isn't assigned to an incident in each for loop), more the wait time value increases of that Unit. The value is
     * then multiplied by the wait time.
     */
    public static double WAIT_COEFFICIENT = 0.5;

    /**
     * When the unit unnecessarily powerful for an incident on some cases it shouldn't be used. For example let's say
     * we have and incident: 000110000, unit1: 000111000, unit2: 000110000. Both units can handle this incident very
     * well. In fact the unit1 can handle it faster (better units has less process time). But if we pair all the
     * incidents with more powerful units the less powerful units can't get any incident. And that is what we're trying
     * to avoid. That's why if an 'unnecessary powerful unit' handles an incident we give it a penalty to that score.
     */
    public static double UNNECESSARY_POWERFUL_UNIT_PENALTY_COEFFICIENT = 0.4;

    /**
     * Initializes with default coefficients.
     */
    public Scheduler() {
    }

    /**
     * Initializes with given coefficients.
     */
    public Scheduler(double distanceCoefficient, double processTimeCoefficient, double sumOfHandledSeverityCoefficient, double waitCoefficient, double unnecessaryPowerfulUnitPenaltyCoefficient) {
        DISTANCE_COEFFICIENT = distanceCoefficient;
        PROCESS_TIME_COEFFICIENT = processTimeCoefficient;
        SUM_OF_NOT_HANDLED_SEVERITY_COEFFICIENT = sumOfHandledSeverityCoefficient;
        WAIT_COEFFICIENT = waitCoefficient;
        UNNECESSARY_POWERFUL_UNIT_PENALTY_COEFFICIENT = unnecessaryPowerfulUnitPenaltyCoefficient;
    }

    public Solution schedule(Incident[] incidentsArr, Unit[] units) {
        // Initialize solution
        Solution solution = new Solution(units.length);

        // Create unit wrappers
        UnitWrapper[] unitWrappers = new UnitWrapper[units.length];
        for (int i = 0; i < units.length; i++) {
            unitWrappers[i] = new UnitWrapper(units[i]);
        }

        // Create deep copy of incidents so original data remains unchanged
        Incident[] incidents = Utils.deepCopy(incidentsArr);

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
            incidents[i].status = events[bestUnitIndex].getResult();

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
        double numberOfIncidentBitsHandled = 0;
        for (int i = 0; i < incident.status.length(); i++) {
            if (incident.status.charAt(i) == '1' && unitWrapper.unit.type.charAt(i) == '1') {
                sb.append('0');
                handledSeverityPoint += (i % ProblemData.SEVERITY_CAPABILITY_COUNT + 1) * (1 / SUM_OF_SEVERITY_CAPABILITY);
                totalProcessTime += ProblemData.SCALED_PROCESS_TIME_AND_CAPABILITIES[unitWrapper.unit.getTypeIndex(floorBased(i, ProblemData.SEVERITY_CAPABILITY_COUNT))][i];
                numberOfIncidentBitsHandled++;
            } else if (incident.status.charAt(i) == '0' && unitWrapper.unit.type.charAt(i) == '1') {
                sb.append(incident.status.charAt(i));
                numberOfIncidentBitsHandled++;
                handledSeverityPoint *= 1 - UNNECESSARY_POWERFUL_UNIT_PENALTY_COEFFICIENT;
            } else {
                sb.append(incident.status.charAt(i));
            }
        }

        // Calculate total distance time
        double totalDistanceTime = ProblemData.SCALED_DISTANCE_MATRIX[unitWrapper.lastLocationIndex][incident.index];

        // Get the remaining handled severity point cuz we're aiming for minimum
        handledSeverityPoint = 1 - handledSeverityPoint;
        double waitValue = 1 - unitWrapper.waitTime;

        // Find the average ...
        if (numberOfIncidentBitsHandled != 0) {
            totalProcessTime = totalProcessTime / numberOfIncidentBitsHandled;
        }

        // (handledSeverityPoint < 0 || handledSeverityPoint > 1) || (totalProcessTime < 0 || totalProcessTime > 1) || (totalDistanceTime < 0 || totalDistanceTime > 1) || (waitValue < 0 || waitValue > 1)
        return new Event(totalDistanceTime, totalProcessTime, handledSeverityPoint, waitValue, sb.toString(), unitWrapper.unit.type, incident.status);
    }

    private static int findBestUnitIndex(Event[] events) {
        double currMin = Double.MAX_VALUE;
        int index = 0;

        for (int i = 0; i < events.length; i++) {
            if (events[i].getScore() < currMin) {
                currMin = events[i].getScore();
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

    public static double getDistanceCoefficient() {
        return DISTANCE_COEFFICIENT;
    }

    public static double getProcessTimeCoefficient() {
        return PROCESS_TIME_COEFFICIENT;
    }

    public static double getSumOfNotHandledSeverityCoefficient() {
        return SUM_OF_NOT_HANDLED_SEVERITY_COEFFICIENT;
    }

    public static double getWaitCoefficient() {
        return WAIT_COEFFICIENT;
    }

    public static double getUnnecessaryPowerfulUnitPenaltyCoefficient() {
        return UNNECESSARY_POWERFUL_UNIT_PENALTY_COEFFICIENT;
    }
}
