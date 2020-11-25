package com.example.algorithom;

//大顶堆
public class MyHeap {
    public static int[] heap = new int[1000];
    public static int size;
    public static void push(int data){
        if(size==0) {
            heap[0] = data;
            size = 1;
        }else {
            heap[size] = data;
            if (heap[(size - 1) / 2] < heap[size]) {
                handleup(size, heap);
            }
            size++;
        }
    }

    public static void pop(){
        heap[0] = heap[size-1];
        heap[size-1] = 0;
        handledown(0,heap);
        size--;
    }

    public static void handledown(int i, int[] heap) {
        if(i>=size)
            return;
        if(heap[i]<heap[2*i+1]){
            int temp = heap[i];
            heap[i] = heap[2*i+1];
            heap[2*i+1] = temp;
            handledown(2*i+1,heap);
        }
        if(heap[i]<heap[2*i+2]){
            int temp = heap[i];
            heap[i] = heap[2*i+2];
            heap[2*i+2] = temp;
            handledown(2*i+2,heap);
        }


    }

    public static void handleup(int i,int[] heap) {
        if(i==0)
            return;
        if(heap[i]>heap[(i-1)/2]){
            int temp = heap[i];
            heap[i] = heap[(i-1)/2];
            heap[(i-1)/2] = temp;
        }
        handleup((i-1)/2,heap);
    }

    public static void main(String[] args) {
        push(1);
        push(2);
        push(3);
        push(4);
        push(5);
        push(6);
        pop();
        System.out.println("ff");
    }

}
