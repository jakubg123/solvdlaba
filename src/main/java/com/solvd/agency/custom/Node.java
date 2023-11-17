package com.solvd.agency.custom;

public class Node<T> {
    T data;
    public Node<T> next;

    public Node(T data){
        this.data = data;
        this.next = null;
    }

}
