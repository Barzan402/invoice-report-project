package com.vgb;

import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;

/**
 * This class is for saving data into a JSON file
 */
public class CreateJson {
	// the filename is the name of the JSON file to be created
	// the data is to be saved in JSON format
	public static void saveToJson(String filename, Object data, Gson gson) {
		try (FileWriter writer = new FileWriter(filename)) {
			gson.toJson(data, writer); // Converts and writes the data to JSON format
			System.out.println("Created JSON: " + filename); // Prints out "Created" in the console to know its been done
		} catch (IOException e) {
		}
	}
}
