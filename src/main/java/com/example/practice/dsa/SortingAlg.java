package com.example.practice.dsa;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SortingAlg {

    //For bubble sort
    public static int[] bubbleSort(int[] arr){
        int temp = 0;
        for(int i=0; i<arr.length;i++){
            for(int j=0; j<arr.length-i-1;j++){
                if(arr[j]>arr[j+1]){
                    temp = arr[j+1];
                    arr[j+1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        return arr;
    }

    //for selection sort
    public static int[] selectionSort(int[] arr){
        int temp = 0;
        int minIndex = -1;
        for (int i = 0; i < arr.length-1; i++) {
               minIndex = i;
            for (int j = i+1; j < arr.length; j++) {
                if(arr[minIndex] > arr[j]){
                       minIndex = j;
                }
            }
            temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }
        return arr;
    }

    //for insertion sort
     public static int[] insertionSort(int[] arr){
         for (int i = 1; i < arr.length; i++) {
                int key  = arr[i];
                int j = i-1;
                while(j>=0 && arr[j]>key){
                    arr[j+1] = arr[j];
                    j--;
                }
                arr[j+1] = key;
         }
        return arr;
     }
}
