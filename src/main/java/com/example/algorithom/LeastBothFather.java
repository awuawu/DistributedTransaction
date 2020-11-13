package com.example.algorithom;

public class LeastBothFather {
    static class Node{
        Node left;
        Node right;
        int data;
        public Node(int data,Node left,Node right){
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }

    public static Node bothFather(Node root,Node n1,Node n2){
        if(root==null)
            return null;
        if(root==n1||root==n2)
            return root;
        if((root.right==n1&&root.left==n2)||(root.right==n2&&root.left==n1))
            return root;
        if(containBoth(root.left,n1,n2))
            return  bothFather(root.left,n1,n2);
        if(containBoth(root.right,n1,n2))
            return  bothFather(root.right,n1,n2);
        return root;
    }

    private static boolean containBoth(Node root, Node n1, Node n2) {
        return containOne(root,n1)&&containOne(root,n2);
    }

    private static boolean containOne(Node root, Node n1) {
        if(root==null)
            return false;
        if(root==n1)
            return true;
        return containOne(root.left,n1)||containOne(root.right,n1);
    }

    public static void main(String[] args) {
        Node n10 = new Node(10,null,null);
        Node n9 = new Node(9,null,n10);
        Node n8 = new Node(8,null,n9);
        Node n7 = new Node(7,null,n8);
        Node n6 = new Node(6,null,null);
        Node n5 = new Node(5,null,null);
        Node n4 = new Node(4,null,null);
        Node n3 = new Node(3,n6,n7);
        Node n2 = new Node(2,n4,n5);
        Node n1 = new Node(1,n2,n3);
//        System.out.println(bothFather(n1,n4,n7).data);
//        System.out.println(containBoth(n2,n4,n5));
        System.out.println(bothFather(n1,n5,n10).data);
    }
}
