package com.example.algorithom;

import java.util.LinkedList;

public class MinStack {
    private LinkedList<Integer> data = new LinkedList<>();
    private int min = Integer.MAX_VALUE;
    public void push(int node) {
        data.addFirst(node);
        if(node<min) {
            min = node;
        }
    }

    public void pop() {
        data.removeFirst();
        for (int i = 0; i < data.size(); i++) {
            if(data.get(i)<min)
                min = data.get(i);
        }
    }

    public int top() {
        return data.getFirst();
    }

    // 返回当前栈中最小值
    public int min() {
        return min;
    }

    public static void main(String[] args) {
        MinStack stack = new MinStack();
        stack.push(1);
        System.out.println(stack.top());
        stack.push(2);
        stack.push(3);
        System.out.println(stack.top());
        stack.pop();
        System.out.println(stack.top());
        System.out.println(stack.min());
        stack.push(-1);
        System.out.println(stack.min());
    }
}
