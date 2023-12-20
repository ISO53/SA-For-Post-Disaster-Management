package com.iso53;

public class Main {
    public static void main(String[] args) {
        System.out.println("------- Distance Matrix Started -------");
        Utils.printMatrix(ProblemData.DISTANCE_MATRIX);
        System.out.println("-------- Distance Matrix Ended --------\n");

        System.out.println("------ District Locations Started ------");
        ProblemData.DISTRICT_LOCATIONS.forEach(System.out::println);
        System.out.println("------- District Locations Ended -------");
    }
}