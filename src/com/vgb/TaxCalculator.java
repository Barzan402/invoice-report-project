package com.vgb;

/**
 * Utility class for tax calculations.
 */
public class TaxCalculator {
    
	// Calculates and rounds tax based on the given amount and tax rate
	public static double calculateTax(double amount, double rate) {
		return Math.round(amount * rate * 100.0) / 100.0;
	}
}
