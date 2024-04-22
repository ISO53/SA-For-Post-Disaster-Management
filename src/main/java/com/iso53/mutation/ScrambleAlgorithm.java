package com.iso53.mutation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ScrambleAlgorithm implements MutationAlgorithm {

    private static final Random rand = new Random();

    @Override
    public Object[] mutate(Object[] array) {
        // Generate a random subset size between 1 and array length
        int subsetSize = rand.nextInt(array.length) + 1;

        // Generate a subset of unique random indices
        int[] indices = rand.ints(0, array.length)
                .distinct()
                .limit(subsetSize)
                .toArray();

        // Collect the elements at these indices
        List<Object> subset = Arrays.stream(indices)
                .mapToObj(i -> array[i])
                .collect(Collectors.toList());

        // Randomly rearrange the elements
        Collections.shuffle(subset, rand);

        // create copy array
        Object[] newArr = Arrays.copyOf(array, array.length);

        // Put the rearranged elements back into the new array
        for (int i = 0; i < indices.length; i++) {
            newArr[indices[i]] = subset.get(i);
        }

        return newArr;
    }
}
