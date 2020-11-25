package com.example.algorithom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeastBoth {
    static class TreeNode{
        TreeNode left;
        TreeNode right;
        int data;
        public TreeNode(TreeNode left,TreeNode right,int data) {
            this.left = left;
            this.right = right;
            this.data = data;
        }
    }
    public static List<List<Integer>> levelTree(TreeNode root){
        if(root==null)
            return null;
        return wrap(root.data,levelTree(root.left),levelTree(root.right));
    }

    private static List<List<Integer>> wrap(int data, List<List<Integer>> left, List<List<Integer>> right) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> root = Arrays.asList(data);
        res.add(root);
        if(left==null&&right==null)
            return res;
        if(left==null) {
            res.addAll(right);
            return res;
        }
        if(right==null) {
            res.addAll(left);
            return res;
        }
        int i = 0;
        for (; i < left.size() && i < right.size();) {
            List<Integer> temp = new ArrayList<>();
            for (int j = 0; j < left.get(i).size(); j++) {
                temp.add(left.get(i).get(j));
            }
            for (int k = 0; k < right.get(i).size(); k++) {
                temp.add(right.get(i).get(k));
            }
            res.add(temp);
            i++;
        }
        if(left.size()==right.size())
            return res;
        if(left.size()>right.size()){
            for (int m = i; m < left.size(); m++) {
                res.add(left.get(m));
            }
        }
        if(left.size()<right.size()){
            for (int m = i; m < right.size(); m++) {
                res.add(right.get(m));
            }
        }
        return res;
    }

    public static boolean isContain(TreeNode root,TreeNode node){
        boolean isLeft = false;boolean isRight = false;
        if(root.data==node.data)
            return true;
        if(root!=null&&root.left==null&&root.right==null)
            return root.data==node.data;
        if(root.left!=null)
            isLeft = isContain(root.left,node);
        if(root.right!=null)
            isRight = isContain(root.right,node);
        return isLeft||isRight;
    }
    public static boolean isLeastBoth(TreeNode root,TreeNode n1,TreeNode n2){
        return isContain(root,n1)&&isContain(root,n2);
    }
    public static TreeNode findLeastBoth(TreeNode root,TreeNode n1,TreeNode n2){
        boolean both=false,left=false,right=false;
        both = isLeastBoth(root,n1,n2);
        if(root.left!=null)
            left = isLeastBoth(root.left,n1,n2);
        if(root.right!=null)
            right = isLeastBoth(root.right,n1,n2);
        if(both&&!left&&!right)
            return root;
        if(both&&left&&!right)
            return findLeastBoth(root.left,n1,n2);
        if(both&&!left&&right)
            return findLeastBoth(root.right,n1,n2);
        return null;
    }

    public static TreeNode findLeastBothEasy(TreeNode root,TreeNode n1,TreeNode n2){//在root中找n1和n2的公共祖先
        if(root==null)
            return null;
        if(n1==root||n2==root)//n1和n2在一条链路上
            return root;
        TreeNode left = findLeastBothEasy(root.left,n1,n2);
        TreeNode right = findLeastBothEasy(root.right,n1,n2);
        if(left==null)
            return right;
        if(right==null)
            return left;
        if(left!=null&&right!=null)
            return root;
        return null;
    }

    public static void print(List<List<Integer>> list){
        if(list==null||list.size()==0)
            return;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                System.out.print(list.get(i).get(j)+" ");
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        TreeNode n10 = new TreeNode(null,null,10);
        TreeNode n8 = new TreeNode(null,null,8);
        TreeNode n9 = new TreeNode(null,null,9);
        TreeNode n6 = new TreeNode(n8,null,6);
        TreeNode n7 = new TreeNode(n10,n9,7);
        TreeNode n4 = new TreeNode(null,null,4);
        TreeNode n5 = new TreeNode(null,null,5);
        TreeNode n3 = new TreeNode(n4,n5,3);
        TreeNode n2 = new TreeNode(n6,n7,2);
        TreeNode n1 = new TreeNode(n2,n3,1);
//        System.out.println(findLeastBoth(n1,n9,n10).data);
//        System.out.println(findLeastBothEasy(n1,n9,n10).data);


//        List<List<Integer>> l1 = Arrays.asList(
//                Arrays.asList(new Integer[]{2}),
//                Arrays.asList(new Integer[]{3}));
//
//        List<List<Integer>> l2 = Arrays.asList(
//                Arrays.asList(new Integer[]{4}),
//                Arrays.asList(new Integer[]{5}));
//
//        List<List<Integer>> res = wrap(1,l1,l2);
        List<List<Integer>> res = levelTree(n6);
        print(res);
                System.out.println("csdhg");

    }
}
