package com.example.practice.dsa;

public class Queue {
    int[] queue = new int[5];
    int front;
    int rear;
    int size;

    public void enQueue(int data){

        if(!isFull()){
            queue[rear] = data;
            rear  = (rear + 1) % 5;
            size = size + 1;
        }else System.out.println("Queue is full");

    }

    public int  deQueue(){
        int data = queue[front];
        if(!isFull()){
            front = (front + 1) % 5;
            size = size - 1;
        }else System.out.println("Queue is empty");

        return data;
    }

    public void show(){
        System.out.print("Elements : ");
        for (int i = 0; i <size ; i++) {
            System.out.print(queue[(front + i) % 5] + " ");
        }
        System.out.println();
        for (int i : queue) {
            System.out.print(i + " ");
        }
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size() == 0;
    }
    public boolean isFull(){
        return size() == 5;
    }
}
