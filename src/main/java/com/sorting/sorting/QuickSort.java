package com.sorting.sorting;

import org.jetbrains.annotations.NotNull;

public class QuickSort {

    // Utility method to swap two elements in the array
    private static void swap(int @NotNull [] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // The main function that implements QuickSort
    public static void quickSort(int[] array, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(array, begin, end);

            quickSort(array, begin, partitionIndex-1);
            quickSort(array, partitionIndex+1, end);
        }
    }

    private static int partition(int @NotNull [] array, int begin, int end) {
        int pivot = array[end];
        int i = (begin-1);

        for (int j = begin; j < end; j++) {
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
        }

        swap(array, i+1, end);

        return i+1;
    }
}

