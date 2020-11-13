package com.example.algorithom;

public class TreeFanZ {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(TreeNode left, TreeNode right, int val) {
            this.left = left;
            this.right = right;
            this.val = val;
        }
    }
    public static void fun(TreeNode root){
        if(root==null)
            return;
        TreeNode left = root.left;
        TreeNode right = root.right;
        root.left = right;
        root.right = left;
        fun(root.left);
        fun(root.right);
    }

    public static void main(String[] args) {
        TreeNode n5 = new TreeNode(null,null,5);
        TreeNode n4 = new TreeNode(null,null,4);
        TreeNode n2 = new TreeNode(n4,n5,2);
        TreeNode n3 = new TreeNode(null,null,3);
        TreeNode n1 = new TreeNode(n2,n3,1);
        fun(n1);
        System.out.println("dshdhj");
    }
}
