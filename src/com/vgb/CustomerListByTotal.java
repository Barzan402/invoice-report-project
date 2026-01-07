package com.vgb;
/*
 * This class stores and maintains a sorted list
 * of CustomerTotal objects
 */
public class CustomerListByTotal {
	private CustomerTotal[] array; // Array to hold the CustomerTotal objects
	private int size; // Tracks the number of items in the list
	
	// This constructor starts the array and sets the initial size to 0
	public CustomerListByTotal() {
		this.array = new CustomerTotal[10];
		this.size = 0;
	}
	// Doubles the size of the array when full
	private void resize() {
		CustomerTotal[] newArray = new CustomerTotal[array.length * 2];
		for (int i = 0; i < size; i++) {
			newArray[i] = array[i];
		}
		array = newArray; // Replace the old array with the new one
	}
	// Adds a new CustomerTotal to the list and keeps it sorted
	public void add(CustomerTotal ct) {
		if (size == array.length) {
			resize();
		}
		
		double newTotal = ct.getTotalAmount(); // Get the total for new item
		String newName = ct.getCustomer().getName(); // Get the customer name
		
		int i = size; // Start from the end of the current list
		while (i > 0) {
			double currentTotal = array[i - 1].getTotalAmount(); // Compare to previous item
			String currentName = array[i - 1].getCustomer().getName();
			
			// Insert in correct order
			if (newTotal < currentTotal || (newTotal == currentTotal && newName.compareTo(currentName) < 0)) {
				array[i] = array[i - 1];
				i--;
			} else {
				break;
			}
		}
		
		array[i] = ct; // Inserts new item
		size++; // Increases list size
	}
	
	// Returns the customer total at the given index
	public CustomerTotal get(int index) { 
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		return array[index];
	}
	// Returns the number of items in the list
	public int size() {
		return size;
	}
}
