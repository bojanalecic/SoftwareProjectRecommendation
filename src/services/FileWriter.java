/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import domain.Project;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author lecicb
 */
public class FileWriter {
    
    public static void writeSimilarities(LinkedList<Project> projects, HashMap<String, LinkedList<Double>> similiraties) {
		
		// similarities are written in csv file together with the movie title and url
		File similarities = new File("files/similarities.csv");
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(similarities);
			OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
			BufferedWriter writer = new BufferedWriter(outputWriter);
			String line = "";

			for (Project p : projects) {
				LinkedList<Double> values = similiraties.get(p.getName());
				line = p.getName() + ",";
				for (double value : values) {
					line = value + ",";
				}
				writer.write(line);
				writer.newLine();
				line = "";
			}

			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    
     public static void writeSimilarities(LinkedList<Project> projects,LinkedList<Double> similiraties) {
		
		// similarities are written in csv file together with the movie title and url
		File similarities = new File("files/similarities.csv");
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(similarities);
			OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
			BufferedWriter writer = new BufferedWriter(outputWriter);
			String line = "";

			for (Project p : projects) {
				line = p.getName() + ",";
				for (double value : similiraties) {
					line = value + ",";
				}
				writer.write(line);
				writer.newLine();
				line = "";
			}

			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    
}
