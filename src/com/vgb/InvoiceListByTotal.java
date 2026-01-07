package com.vgb;

public class InvoiceListByTotal {
	private Invoice[] array;
	private int size;
	
	// Constructor starts the array with size 10
	public InvoiceListByTotal() {
		array = new Invoice[10];
		size = 0;
	}
	// Adds a new invoice to the list in decending order by total amount (Like the webgrader) 
	public void addInvoice(Invoice invoice) {
		if (size == array.length) {
			resize();
		}
		
		// Finds where to insert the new invoice (Highest total first) 
		int i = size;
		while (i > 0 && invoice.calculateGrandTotal() > array[i - 1].calculateGrandTotal()) {
			array[i] = array[i - 1];
			i--;
		}
		array[i] = invoice; // Inserts the invoice at the correct spot
		size++; // Increases the size counter
	}
	
	// Doubles the size of array when full
	private void resize() {
		Invoice[] newArray = new Invoice[array.length * 2];
		for (int i = 0; i < size; i++) {
			newArray[i] = array[i];
		}
		array = newArray; // Replaces old array with new one
	}
	// Returns the invoice at a specific index
	public Invoice get(int index) {
		// If it is invalid it throws an error
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		return array[index];
	}
	// Returns the number of invoices in the list
	public int size() {
		return size;
	}
}
