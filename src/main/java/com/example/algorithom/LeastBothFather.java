package com.example.algorithom;

import java.util.ArrayList;
import java.util.List;

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

    static class LevelData{
        int data;
        int level;
        public LevelData(int data,int level){
            this.data = data;
            this.level = level;
        }
    }

//    static List<LevelData> levelData = new ArrayList<>();

    public static List<LevelData> levelTrar(Node node,int level,List<LevelData> levelData){
        if(node==null)
            return levelData;
        levelData.add(new LevelData(node.data,level));
        levelTrar(node.right, level+1, levelData);
        levelTrar(node.left, level+1, levelData);
        return levelData;
    }

    public static List<Integer> rightView(Node node){
        List<LevelData> levelData = levelTrar(node,0,new ArrayList<LevelData>());
        List<Integer> res = new ArrayList<>();
        int level = 0;
        for(LevelData lel:levelData){
            if(level==lel.level){
                res.add(lel.data);
                level++;
            }
        }
        return res;
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

    public static int distance(Node root,Node n){
        if(root==null)
            return Integer.MIN_VALUE;
        if(root==n)
            return 0;
        return Math.max(distance(root.left,n),distance(root.right, n))+1;

    }

    public static int distanceTwo(Node root,Node n1,Node n2){
        Node parent = bothFather(root,n1,n2);
        return distance(parent,n1)+distance(parent,n2);
    }

    public static void print(List<Integer> list){
        list.stream().forEach(e->{
            System.out.print(e+" ");
        });
    }

    public static String changeFormatNumber (String number) {
        // write code here
        return Integer.toBinaryString(Integer.parseInt(number))+":"+Integer.toHexString(Integer.parseInt(number));
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
//        System.out.println(bothFather(n1,n5,n10).data);
//        List<LevelData> levelData = levelTrar(n1,0,new ArrayList<LevelData>());
        List<Integer> res = rightView(n1);
        print(res);
        System.out.println(distanceTwo(n1,n6,n7));
        System.out.println(changeFormatNumber("2"));
    }
}
