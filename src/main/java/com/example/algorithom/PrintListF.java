package com.example.algorithom;

public class PrintListF {
    static class Node{
        Node next;
        int data;
        public Node(Node next,int data){
            this.next = next;
            this.data = data;
        }
    }
    public static Node findLast(Node node){
        Node cur = node;
        if(cur==null)
            return null;
        while (cur.next!=null)
            cur = cur.next;
        return cur;
    }
    public static void print(Node node,Node cur){
        if(cur!=null)
            System.out.println(cur.data);

        Node pre = find(node,cur);
        if(pre!=null)
        print(node,pre);
    }
    public static Node find(Node node,Node cur){
        if (node==null||node.next==null||cur==node)
            return null;
        Node pre = node;
        while (pre.next!=cur)
            pre = pre.next;
        return pre;
    }

    public static String handle(String s){
        String[] ht = s.split("\\.");
        int i=0;
        while(ht[0].charAt(i)=='0')
            i++;
        ht[0] = ht[0].substring(i,ht[0].length());
        int lh = ht[0].length()%3;
        ht[0] = addH(ht[0],lh);

        int j=ht[1].length()-1;
        while(ht[1].charAt(j)=='0')
            j--;
        ht[1] = ht[1].substring(0,j+1);
        int lt = ht[1].length()-ht[1].length()%3;
        ht[1] = addT(ht[1],lt);

        return ht[0]+"."+ht[1];
    }

    public static String addT(String s,int lo){
        StringBuffer sb = new StringBuffer();
        String zs = s.substring(0,lo);
        int start = 0;
        int step = 3;
        while (start<zs.length()){
            sb.append(zs.charAt(start));
            step--;
            if(step==0) {
                sb.append(",");
                step=3;
            }
            start++;
        }
        if(lo!=s.length())
            sb.append(s.substring(lo,s.length()));
        return sb.toString();
    }

    public static String addH(String s,int lo){
        StringBuffer sb = new StringBuffer();
        if(lo!=0)
            sb.append(s.substring(0,lo));
        String zs = s.substring(lo,s.length());
        int start = 0;
        while (start<zs.length()){
            if(start%3==0)
                sb.append(",");
            sb.append(zs.charAt(start));
            start++;
        }
        return sb.toString();
    }

    static class Person{
        private String name;
        public Person(String name){
            this.name = name;
        }
    }

    public static Person hand(Person p){
        p = new Person("hehhe");
        return p;
    }

    public static int firstMax(int[] nums,int from,int to,int x){
        if(nums.length==0)
            return Integer.MIN_VALUE;
        if(nums.length==1)
            return nums[0]>x?nums[0]:Integer.MIN_VALUE;
        if(from==to)
            return nums[to]>x?nums[to]:Integer.MIN_VALUE;

        int mid = (from+to)/2;
        if(nums[mid]==x){
            int i = mid+1;
            while (i< nums.length&&nums[i]==x)
                i++;
            return i==nums.length?Integer.MIN_VALUE:nums[i];
        }else if(nums[mid]>x){
            return firstMax(nums,from,mid,x);
        }else {
            return firstMax(nums,mid+1,to,x);
        }
    }

    public static void main(String[] args) {
//        Node n5 = new Node(null,5);
//        Node n4 = new Node(n5,4);
//        Node n3 = new Node(n4,3);
//        Node n2 = new Node(n3,2);
//        Node n1 = new Node(n2,1);
//        print(n1,findLast(n1));
        System.out.println(handle("000012345.2345670000"));
        Person p = new Person("hahah");
        p = hand(p);
        System.out.println(p.name);
        int[] nums = new int[]{1,2,2,3,3,4,4,5,5,6,7,8,8,9};
        System.out.println(firstMax(nums,0,nums.length-1,-1));
    }
}
