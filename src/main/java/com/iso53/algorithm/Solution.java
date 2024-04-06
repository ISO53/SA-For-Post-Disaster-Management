package com.iso53.algorithm;

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

    public void add(Event event, int unitIndex) {
        this.solution.get(unitIndex).add(event);
    }

    public ArrayList<LinkedList<Event>> getSolution() {
        return solution;
    }

    public LinkedList<Event> getSolution(int i) {
        return solution.get(i);
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

        return Math.sqrt(variance); // return standard deviation
    }

    public void print() {
        for (int i = 0; i < solution.size(); i++) {
            System.out.print("Unit " + i + " ==>  ");
            for (Event event : solution.get(i)) {
                System.out.print(event.bar());
            }
            System.out.println();
        }
    }

    public double getMaxLineLength() {
        double max = Double.MIN_VALUE;


        double rangeScale = ProblemData.MAX_SCALE_FACTOR - ProblemData.MIN_SCALE_FACTOR;

        for (LinkedList<Event> events : solution) {
            double unitScore = 0;

            for (Event event : events) {
                unitScore += (event.getDistanceTime() + event.getProcessTime()) * rangeScale + 2 * ProblemData.MIN_SCALE_FACTOR;
            }

            if (unitScore > max) {
                max = unitScore;
            }
        }

        return max;
    }
}