package com.solvd.agency.custom;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;

public class MyList<T> {

    private Node<T> head;


    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (this.head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }

            current.next = newNode;
        }
    }

    public void display(){
        Node<T> current = head;
        while(current!= null){
            System.out.println(current.data);
            current = current.next;
        }
    }

    public int size() {
        if (head == null)
            return 0;
        Node<T> current = head;
        int counter = 0;
        while (current != null) {
            current = current.next;
            counter++;
        }
        return counter;
    }

    public void clear() {
        this.head = null;
    }


    @Override
    public int hashCode() {

        int result = 17;
        Node<T> current = head;
        while (current != null) {
            result = 31 * result + Objects.hashCode(current.data);
            current = current.next;
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        MyList<T> otherList = (MyList<T>) obj;

        if (this.size() != otherList.size()) return false;

        Node<T> thisCurrent = head;
        Node<T> otherCurrent = otherList.head;

        while (thisCurrent != null) {
            if (!Objects.equals(thisCurrent.data, otherCurrent.data)) {
                return false;
            }
            thisCurrent = thisCurrent.next;
            otherCurrent = otherCurrent.next;
        }

        return true;

    }

    public boolean removeAll(Collection<?> c) {
        boolean removedAll = false;

        for (Object o : c) {
            remove(o);
        }
        removedAll = true;
        return removedAll;
    }

    public boolean remove(Object o){
        if(o == null)
            throw new NoSuchElementException("No such an element");
        Node<T> previous = null;
        Node<T> current = head;

        while(current.next!=null && !Objects.equals(current.data,o)){
            previous = current;
            current = current.next;
        }

        if(current!=null){
            if(previous!=null){
                head = current.next;
            }
            else {
                previous.next = current.next;
            }
            return true;
        }
        return false;
    }

    boolean addAll(Collection<? extends T> c)
    {
        for(T o : c){
            add(o);
        }
        return true;
    }
    public boolean contains(Object o) {
        Node<T> current = head;
        while (current != null) {
            if (Objects.equals(o, current.data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }
}