package com.example.practice.dsa.tree;

public class BinaryTree {
    Node root;
    public void insert(int data){
        root  = insertRec(root, data);

    }

    private Node insertRec(Node root, int data){

        if(root == null)
            root = new Node(data);
        else if (data < root.data )
            root.left = insertRec(root.left,data);
        else if(data > root.data)
            root.right = insertRec(root.right, data);

        return root;
    }

    public void inOrder(){
        inOrderRec(root);
    }

    private void inOrderRec(Node root){
        if(root != null){
            System.out.print(root.data + " ");//Printing here for preOrder.
            inOrderRec(root.left);
//            System.out.print(root.data + " "); // Printing here for inOrder.
            inOrderRec(root.right);
        }
    }
}
