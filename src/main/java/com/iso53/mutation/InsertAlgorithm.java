package com.iso53.mutation;

import java.util.Arrays;
import java.util.Random;

public class InsertAlgorithm implements MutationAlgorithm{

    private static final Random rand = new Random();

    @Override
    public Object[] mutate(Object[] array) {
        int[] unique = rand.ints(0, array.length)
                .distinct()
                .limit(2)
                .sorted() // Ensure indices are ordered
                .toArray();

        // create copy array
        Object[] newArr = Arrays.copyOf(array, array.length);

        // move the element at index2 to follow index1, shifting the rest along
        Object value = newArr[unique[1]];
        System.arraycopy(newArr, unique[0] + 1, newArr, unique[0] + 2, unique[1] - unique[0] - 1);
        newArr[unique[0] + 1] = value;

        return newArr;
    }
}
