/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import domain.Project;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

/**
 *
 * @author lecicb
 */
public class FileService {

    File similarities;
    FileOutputStream outputStream;
    OutputStreamWriter outputWriter;
    BufferedWriter writer;
    BufferedReader reader;

    public FileService() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader("files/similarities.csv"));
//	similarities =  new File("files/similarities.csv");
        outputStream = new FileOutputStream("files/similarities.csv");
        outputWriter = new OutputStreamWriter(outputStream);    
        writer = new BufferedWriter(outputWriter);
    }

   
    public void writeSimilarities(LinkedList<Project> projects, LinkedList<Double> similiraties) {

        File similarities = new File("files/similarities.csv");
        FileOutputStream outputStream;
        try {
            String line = "";

            for (Project p : projects) {
                line = p.getName() + ",";
                for (double value : similiraties) {
                    line += value + ",";
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

    public boolean isFileEmpty() throws FileNotFoundException, IOException {
       String line;
		if ((line = reader.readLine()) != null) {
		        return false;
                }else{
                    return true;
                }
    }

    public void writeHeader(LinkedList<Project> projects) throws IOException {
        String line = "           ,";
        for (Project project: projects){
            line = line + project.getName() + ",";
        }
        writer.write(line);
        writer.close();
    }

    public boolean projectExistInFile(String name) throws IOException {
        String line;
        while (reader.readLine() !=null){
            line = reader.readLine();
            if (line.startsWith(name)){
                return true;
            }
        }
        return false;
    }

    public void readSimilarities(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
