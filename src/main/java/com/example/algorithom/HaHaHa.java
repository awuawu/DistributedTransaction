package com.example.algorithom;

public class HaHaHa {
    static class Node {
        Node next;
        int data;
        public Node(Node next,int data){
            this.next = next;
            this.data = data;
        }
    }


    public static Node fun(Node node){
        if(node==null||node.next==null)
            return node;
        return wrap(fun(node.next),node);
    }

    public static Node wrap(Node node,Node tail){
        Node res = node;
        Node cur = node;
        while(cur.next!=null)
            cur = cur.next;
        cur.next = tail;
        tail.next = null;
        return res;
    }

    public static void printNode(Node node){
        if(node == null)
            return;
        System.out.print(node.data+" ");
        printNode(node.next);
    }

    //单链表按k个反转
    public static Node fanZ(Node node,int k){
        if(node==null||node.next==null||k==1)
            return node;
        Node pre = node;
        Node cur = node;
        int i=k-1;
        while (i>0&&cur.next!=null) {
            cur = cur.next;
            i--;
        }
        Node til = cur;
        Node nxt = til.next;
        til.next = null;

        return haha(fun(pre),fanZ(nxt,k));
    }

    private static Node haha(Node fun, Node fan) {
        Node res = fun;
        Node cur = fun;
        while (cur.next!=null)
            cur=cur.next;
        cur.next=fan;
        return res;
    }

    public static void main(String[] args) {
        Node n5 = new Node(null,5);
        Node n4 = new Node(n5,4);
        Node n3 = new Node(n4,3);
        Node n2 = new Node(n3,2);
        Node n1 = new Node(n2,1);

        Node n9 = new Node(null,9);
        Node n8 = new Node(n9,8);
        Node n7 = new Node(n8,7);
        Node n6 = new Node(n7,6);
        printNode(fanZ(n1,0));
    }
}
