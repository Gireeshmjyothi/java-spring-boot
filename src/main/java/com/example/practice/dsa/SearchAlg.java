package com.example.practice.dsa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
public class SearchAlg {

    //For linear search
    public static int linearSearch(int[] arr, int target){
        int steps = 0;
        for(int i=0; i<arr.length; i++){
            steps++;
            if(arr[i] == target){
                log.info("Number of steps taken by linear search {}", steps);
                return i;
            }
        }
        log.info("Number of steps taken by linear search {}", steps);
        return -1;
    }

    //For binary search for sorted array
    public static int binarySearch(int[] arr, int target){

        int leftIndex = 0;
        int rightIndex = arr.length -1;
        int steps = 0;

        while(leftIndex <= rightIndex){
            steps++;
            int mid = (leftIndex + rightIndex)/2;
            if(arr[mid] == target){
                log.info("Number of steps taken by binary search {}", steps);
                return mid;
            }else if(arr[mid] < target){
                leftIndex = mid + 1;
            }else rightIndex = mid - 1;
        }
        log.info("Number of steps taken by binary search {}", steps);
        return -1;
    }

    //For binary (recursive) search for sorted array
    public static int binarySearchWithRecursive(int[] arr, int target, int left, int right){

        if(left <= right) {
            int mid = (left + right) / 2;
            if(arr[mid] == target) return mid;
            else if(arr[mid] < target) return  binarySearchWithRecursive(arr, target, mid+1, right);
            else return binarySearchWithRecursive(arr, target, left, mid-1);
        }
        return -1;
    }
}
