package com.vgb;

import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/*
 * this class loads data from the CSV files
 * then it converts it into JSON
 * and saves it as files
 */
public class DataConverter {
	public static void main(String[] args) {
		// Creates a Gson object with pretty printing which formats it into JSON
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		// loads data from CSV files and also creates lists of objects
		List<Person> persons = ParseForCSV.readPersons("data/Persons.csv");
		List<Company> companies = ParseForCSV.readCompanies("data/Companies.csv", persons);
		List<Item> items = ParseForCSV.readItems("data/Items.csv", companies);
		
		// converts and saves to JSON format and saves them to files
		CreateJson.saveToJson("data/Persons.json", persons, gson);
		CreateJson.saveToJson("data/Companies.json", companies, gson);
		CreateJson.saveToJson("data/Items.json", items, gson);
    } 
}
