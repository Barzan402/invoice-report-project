package com.vgb;

public class InvoiceListByCustomer {
	private Invoice[] array;
	private int size;
	
	// Constructor starts the array with capacity 10 and sets size to 0
	public InvoiceListByCustomer() {
		array = new Invoice[10];
		size = 0;
	}
	// Adds an invoice to the list while keeping the list sorted by customer name and UUID
	public void addInvoice(Invoice invoice) {
		if (size == array.length) {
			resize();
		}
		// Get customer name and UUID for the new invoice
		String newName = invoice.getCustomer().getName();
		String newID = invoice.getInvoiceId().toString();
		// Starts from the end of the filled array
		int i = size;
		// Moves the existing elements right until finds the correct spot for the new invoice
		while (i > 0) {
			String currentName = array[i - 1].getCustomer().getName();
			String currentID = array[i - 1].getInvoiceId().toString();
			
			// Compare by customer name, then by Invoice Id to break ties
			int nameCompare = newName.compareTo(currentName);
			if (nameCompare < 0 || (nameCompare == 0 && newID.compareTo(currentID) < 0)) {
				array[i] = array[i - 1];
				i--;
			} else {
				break;
			}
		}
		// Places new invoice in the sorted position
		array[i] = invoice;
		size++;
	}
	// Doubles the array size when capacity is reached
	private void resize() {
		Invoice[] newArray = new Invoice[array.length * 2];
		for (int i = 0; i < size; i++) {
			newArray[i] = array[i]; // Copy old elements to the new array
		}
		array = newArray;
	}
	// Returns the invoice at the given index
	public Invoice get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		return array[index];
	}
	// Returns the current number of invoices in the list
	public int size() {
		return size;
	}
}
