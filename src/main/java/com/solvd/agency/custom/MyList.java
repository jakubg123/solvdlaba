package com.solvd.agency.custom;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MyList<T> implements List<T> {

    private Node<T> head;
    private int size;


    @Override
    public boolean add(T data) {
        Node<T> newNode = new Node<>(data);
        if (this.head == null) {
            head = newNode;
            return false;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }

            current.next = newNode;

        }
        size++;
        return true;
    }


    public void display(){
        Node<T> current = head;
        while(current!= null){
            System.out.println(current.data);
            current = current.next;
        }
    }



    @Override
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

    @Override
    public boolean isEmpty(){
        return size == 0;
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
    public T get(int i) {
        return null;
    }

    @Override
    public T set(int i, T t) {
        return null;
    }

    @Override
    public void add(int i, T t) {

    }

    public T remove(int i) {
        return null;
    }


    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int i) {
        return null;
    }

    @NotNull
    @Override
    public List<T> subList(int i, int i1) {
        return null;
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

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removedAll = false;

        for (Object o : c) {
            remove(o);
        }
        removedAll = true;
        return removedAll;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> collection) {
        return false;
    }


    @Override
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
        }
        size--;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        for(T element  : c){
            add(element);
        }
        return true;
    }

    @Override
    public boolean addAll(int i, @NotNull Collection<? extends T> collection) {
        return false;
    }

    @Override
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

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NotNull
    @Override
    public <T1> T1[] toArray(@NotNull T1[] t1s) {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }
}