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
import java.io.FileWriter;
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
    FileWriter writer;
    BufferedReader reader;

    public FileService() throws FileNotFoundException, IOException {
        reader = new BufferedReader(new FileReader("files/similarities.csv"));
//	similarities =  new File("files/similarities.csv");
//        outputStream = new FileOutputStream("files/similarities.csv");
//        outputWriter = new OutputStreamWriter(outputStream);
        writer = new FileWriter("files/similarities.csv", true);
    }

    public void writeSimilarities(String projectMaster, LinkedList<Double> similiraties) {

       try {
            String line = projectMaster + ", ";

            for (double value : similiraties) {
                line += value + ",";
            }
            writer.append(line);
            writer.append("\n");
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
        boolean empty;
        reader = new BufferedReader(new FileReader("files/similarities.csv"));
        if ((line = reader.readLine()) != null) {
            empty = false;
        } else {
            empty = true;
        }
        reader.close();
        return empty;
    }

    public void writeHeader(LinkedList<Project> projects) throws IOException {
        String line = "          ,";
        for (Project project : projects) {
                 line = line + project.getName() + ",";
        }
        writer.write(line);
        writer.append("\n");
    }

    public boolean projectExistInFile(String name) throws IOException {
        String line;        
        reader = new BufferedReader(new FileReader("files/similarities.csv"));
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(name)) {
                return true;
            }
        }
        reader.close();
        return false;
    }

    public LinkedList<Double> readSimilarities(String name) throws IOException {        
        reader = new BufferedReader(new FileReader("files/similarities.csv"));
        String line = reader.readLine();
        LinkedList<Double> similarities = new LinkedList<>();
        while((line = reader.readLine())!=null){
            if(line.startsWith(name)){
                break;
            }else{
                line = null;
                continue;
            }
        }        
        
        if (line !=null){
            String[] sims =  line.substring(name.length()+1).split(",");
            for(String s: sims){
                similarities.add(Double.parseDouble(s));
            }
        }
        
        reader.close();
        return similarities;
    }
    public LinkedList<String> readTitles() throws IOException {        
        reader = new BufferedReader(new FileReader("files/similarities.csv"));
        String line = reader.readLine();
        LinkedList<String> titles = new LinkedList<>();
        if (line !=null){
            String[] sims =  line.substring(11).split(",");
            for(String s: sims){
                titles.add(s);
            }
        }
        
        reader.close();
        return titles;
    }

}
