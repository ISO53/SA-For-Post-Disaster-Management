package com.iso53;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Mutation<T> {

    private static final Random rand = new Random();

    public static <T> T[] insert(T[] array) {
        int[] unique = rand.ints(0, array.length)
                .distinct()
                .limit(2)
                .sorted() // Ensure indices are ordered
                .toArray();

        // create copy array
        T[] newArr = Arrays.copyOf(array, array.length);

        // move the element at index2 to follow index1, shifting the rest along
        T value = newArr[unique[1]];
        System.arraycopy(newArr, unique[0] + 1, newArr, unique[0] + 2, unique[1] - unique[0] - 1);
        newArr[unique[0] + 1] = value;

        return newArr;
    }

    public static <T> T[] swap(T[] array) {
        int[] unique = rand.ints(0, array.length)
                .distinct()
                .limit(2)
                .toArray();

        // create copy array
        T[] newArr = Arrays.copyOf(array, array.length);

        // Swap the elements at the two indices
        T temp = newArr[unique[0]];
        newArr[unique[0]] = newArr[unique[1]];
        newArr[unique[1]] = temp;

        return newArr;
    }

    public static <T> T[] inversion(T[] array) {
        int[] unique = rand.ints(0, array.length)
                .distinct()
                .limit(2)
                .toArray();

        // Ensure unique[0] is less than unique[1]
        if (unique[0] > unique[1]) {
            int temp = unique[0];
            unique[0] = unique[1];
            unique[1] = temp;
        }

        // create copy array
        T[] newArr = Arrays.copyOf(array, array.length);

        // Invert the substring between the two indices
        for (int i = unique[0], j = unique[1]; i < j; i++, j--) {
            T temp = newArr[i];
            newArr[i] = newArr[j];
            newArr[j] = temp;
        }

        return newArr;
    }

    public static <T> T[] scramble(T[] array) {
        // Generate a random subset size between 1 and array length
        int subsetSize = rand.nextInt(array.length) + 1;

        // Generate a subset of unique random indices
        int[] indices = rand.ints(0, array.length)
                .distinct()
                .limit(subsetSize)
                .toArray();

        // Collect the elements at these indices
        List<T> subset = Arrays.stream(indices)
                .mapToObj(i -> array[i])
                .collect(Collectors.toList());

        // Randomly rearrange the elements
        Collections.shuffle(subset, rand);

        // create copy array
        T[] newArr = Arrays.copyOf(array, array.length);

        // Put the rearranged elements back into the new array
        for (int i = 0; i < indices.length; i++) {
            newArr[indices[i]] = subset.get(i);
        }

        return newArr;
    }
}
