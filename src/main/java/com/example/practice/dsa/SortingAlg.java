package com.example.practice.dsa;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SortingAlg {
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
}
