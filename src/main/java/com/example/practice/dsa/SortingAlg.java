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

     //quick sort
     public static void quickSort(int[] arr, int low, int high){
        if(low<high){
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }

     }

     private static int partition(int[] arr, int low, int high){
        int pivot = arr[high];
        int i = low - 1;
         for (int j = low; j < high; j++) {
             if(arr[j] < pivot){
                 i++;
                 int temp = arr[i];
                 arr[i] = arr[j];
                 arr[j] = temp;
             }
         }
         int temp = arr[i+1];
         arr[i+1] = arr[high];
         arr[high] = temp;
        return i+1;
     }

     //Merge sort
    public static void mergeSort(int[] arr, int l, int r){
        if(l<r){
            int mid = (l+r)/2;
            mergeSort(arr, l, mid);
            mergeSort(arr, mid+1, r);

            merge(arr, l, mid, r);
        }
    }

    private static void merge(int[] arr, int l, int mid, int r){
        int n1 = mid - l + 1;
        int n2 = r - mid;

        int[] lArr = new int[n1];
        int[] rArr = new int[n2];
        //for left hand side array
        for (int x = 0; x < n1; x++) {
            lArr[x] = arr[l+x];

        }
        //for right hand side array
        for (int x = 0; x < n2; x++) {
            rArr[x] = arr[mid+1+x];

        }

        int i = 0;
        int j = 0;
        int k = l;

        while(i<n1 && j<n2){
            if(lArr[i] <= rArr[j]){
                arr[k] = lArr[i];
                i++;
            }else{
                arr[k] = rArr[j];
                j++;
            }
            k++;
        }
        while(i<n1){
            arr[k] = lArr[i];
            i++;
            k++;
        }
        while(j<n2){
            arr[k] = rArr[j];
            j++;
            k++;
        }

    }
}
