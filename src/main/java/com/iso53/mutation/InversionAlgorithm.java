package com.iso53.mutation;

import java.util.Arrays;
import java.util.Random;

public class InversionAlgorithm implements MutationAlgorithm{

    private static final Random rand = new Random();

    @Override
    public Object[] mutate(Object[] array) {
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
        Object[] newArr = Arrays.copyOf(array, array.length);

        // Invert the substring between the two indices
        for (int i = unique[0], j = unique[1]; i < j; i++, j--) {
            Object temp = newArr[i];
            newArr[i] = newArr[j];
            newArr[j] = temp;
        }

        return newArr;
    }
}
