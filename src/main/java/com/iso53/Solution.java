package com.iso53;

import java.util.ArrayList;
import java.util.LinkedList;

public class Solution {

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

    public double getScore() {
        double sum = 0;
        double sumOfSquares = 0;
        int count = 0;

        for (LinkedList<Event> events : solution) {
            double unitScore = 0;

            for (Event event : events) {
                unitScore += event.getDistanceTime() + event.getProcessTime();
            }

            sum += unitScore;
            sumOfSquares += Math.pow(unitScore, 2);
            count++;
        }

        double mean = sum / count;
        double variance = sumOfSquares / count - Math.pow(mean, 2);
        double standardDeviation = Math.sqrt(variance);

        return standardDeviation;
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