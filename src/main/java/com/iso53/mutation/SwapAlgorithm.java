package com.iso53.mutation;

import java.util.Arrays;
import java.util.Random;

public class SwapAlgorithm implements MutationAlgorithm{

    private static final Random rand = new Random();

    @Override
    public Object[] mutate(Object[] array) {
        int[] unique = rand.ints(0, array.length)
                .distinct()
                .limit(2)
                .toArray();

        // create copy array
        Object[] newArr = Arrays.copyOf(array, array.length);

        // Swap the elements at the two indices
        Object temp = newArr[unique[0]];
        newArr[unique[0]] = newArr[unique[1]];
        newArr[unique[1]] = temp;

        return newArr;
    }
}
