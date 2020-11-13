package com.example.algorithom;

public class TreeTopK {
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

    public static TreeNode topk(TreeNode root,int k){
        int leftNum = treeNum(root.left);
        if(leftNum==k-1)
            return root;
        if(leftNum<k-1&&root.right!=null)
            return topk(root.right,k-leftNum-1);
        if(leftNum>k-1&&root.left!=null)
            return topk(root.left,k);
        return null;
    }

    private static int treeNum(TreeNode root) {
        if(root==null)
            return 0;
        return treeNum(root.left)+treeNum(root.right)+1;
    }

    public static void main(String[] args) {
        TreeNode n6 = new TreeNode(null,null,6);
        TreeNode n8 = new TreeNode(null,null,8);
        TreeNode n2 = new TreeNode(null,null,2);
        TreeNode n4 = new TreeNode(null,null,4);
        TreeNode n3 = new TreeNode(n2,n4,3);
        TreeNode n7 = new TreeNode(n6,n8,7);
        TreeNode n5 = new TreeNode(n3,n7,5);
        System.out.println(topk(n5,8).val);
    }
}
