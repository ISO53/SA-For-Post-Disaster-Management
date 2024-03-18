package com.iso53.algorithm;

public class Event {

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

        return this.handledSeverityPoint * Scheduler.SUM_OF_NOT_HANDLED_SEVERITY_COEFFICIENT
                + this.distanceTime * Scheduler.DISTANCE_COEFFICIENT
                + this.processTime * Scheduler.PROCESS_TIME_COEFFICIENT
                + this.waitValue * Scheduler.WAIT_COEFFICIENT;
    }

    public String bar() {
        // Show distance as '_' and process as '#' character
        return ".".repeat((int) (this.distanceTime * 200) + 1) +
                "|".repeat((int) (this.processTime * 50) + 1);
    }

    public static double getMaxNumberOfEventCount() {
        double eventCount = 0;

        for (int i = 0; i < ProblemData.INCIDENTS.length; i++) {
            for (int j = 0; j < ProblemData.INCIDENTS[i].status.length(); j++) {
                if (ProblemData.INCIDENTS[i].status.charAt(j) == '1') {
                    eventCount++;
                }
            }
        }

        return eventCount;
    }
}
