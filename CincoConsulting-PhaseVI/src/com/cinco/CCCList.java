package com.cinco;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public class CCCList<T> implements Iterable<T>{

	private T arr[];
	private int size;

	//CCCList constructor based on an array of dynamic size
	@SuppressWarnings("unchecked")
	public CCCList() {
		this.arr = (T[]) new Object[size];
		this.size = 0;
	}

	public int getSize() {
		return size;
	}

	//for familiarity reasons, the method is named after Java's ArrayList add()
	public void add(int index, T item) {
		//check for illegal index input that is either too large or too small
		if(index < 0 || index > this.size) {
			throw new IllegalArgumentException("index is out of bounds!");
		}
		//before we can add elements, we need to check if the size will allow for additions:
		if(this.size == this.arr.length) {
			//if the array is full, create a copy of it but with size one greater
			this.arr = Arrays.copyOf(this.arr, this.size + 1);
		}

		//for loop to shift elements after the specified index up to make room for the new element
		for(int i=this.size-1; i>=index; i--) {
			this.arr[i+1] = this.arr[i];
		}
		this.arr[index] = item;
		this.size++;
	}

	//adds an element to the end of the list
	public void add(T item) {
		this.add(this.size, item);
	}

	//gets the element at the specified index
	public T get(int index) {
		if(index < 0 || index >= this.size) {
			throw new IllegalArgumentException("index is out of bounds!");
		}
		return this.arr[index];
	}

	//toString method to print elements of the list
	public String toString() {
		if(this.size == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder();
		//traditional array notation with square brackets
		sb.append("[");
		//for loop to add commas after each element
		for(int i=0; i<this.size-1; i++) {
			sb.append(this.arr[i] + ", ");
		}
		//ends the list with a "]"
		sb.append(this.arr[this.size-1]);
		sb.append("]");
		return sb.toString();
	}

	//binary search method inspired by Java's Collections.binarySearch() method:
	//When binary search does not match an index exactly, it returns a
	//negative number (negative index - 1) as to follow tradition
	//So if an object would be inserted at index 5, -6 would be returned.
	//But if an index is matched exactly, it returns that index
    public static <T> int binarySearch(CCCList<T> list, T key, Comparator<T> c) {
    	//first index
        int low = 0;
        //last index
        int high = list.getSize()-1;

        while (low <= high) {
        	//Bitwise shift:
            int middle = (low + high) >>> 1;
			//Divide and conquer tactics of searching:
            T midValue = list.get(middle);
            int comparison = c.compare(midValue, key);

            if (comparison < 0)
                low = middle + 1;
            else if (comparison > 0)
                high = middle - 1;
            else
            	//case of the key matching
                return middle;
        }
        //case of the key not matching
        return -(low + 1);
    }

    //allows for the list ADT to go through an enhanced for loop:
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return size > index;
			}

			@Override
			public T next() {
				return get(index++);
			}

		};
	}


}
