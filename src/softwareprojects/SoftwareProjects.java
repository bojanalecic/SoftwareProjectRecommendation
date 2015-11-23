/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwareprojects;

import calculation.CosineSimilarityCalculator;
import calculation.TfIdfCalculator;
import com.hp.hpl.jena.n3.turtle.parser.ParseException;
import domain.Project;
import domain.SearchString;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistence.query.QueryStore;
import services.SoftwareProjectServices;
import util.GraphOperations;
import util.StringOperations;
import services.UserServices;

/**
 *
 * @author lecicb
 */
public class SoftwareProjects {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
//          Current version is developed as console application, user is supposed to enter username in order to get preferences from file
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Hello! Please enter your username!");
            String input;
            String userProject = "";
            try {
                input = br.readLine();
                UserServices.username = input;
                userProject = UserServices.findFavoriteProject();
                if (userProject == "") {
                    throw new Exception("Invalid user!");
                } else {
                    System.out.println("Your last downloaded project is: " + userProject);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            LinkedList<Project> projectsWithDesc = SoftwareProjectServices.getProjectsWithDescription();

//          Remove all short words, words that are not neither nouns or adjectives, stopwords and irrelevant words  
            StringOperations so = new StringOperations(projectsWithDesc);
            so.prepareTextForGraph();

//          Based on relevant words, create graph using jung library and extract keywords based on degree centrality method        
            GraphOperations go = new GraphOperations(so.getProjects());
            projectsWithDesc = go.createGraphForEachProject();

//          User preffered project is set to be master project
            Project projectMaster = new Project();
            for (Project p : projectsWithDesc) {
                if (p.getName().equals(userProject)) {
                    projectMaster = p;
                    break;
                }
            }

            TfIdfCalculator tfIdfCalc = new TfIdfCalculator(projectsWithDesc);
   
//         By using previosly extracted keywords, calculate TF/IDF for project that we want to compare to others           
            tfIdfCalc.calculateTfIDF(projectMaster);
            tfIdfCalc.calculateLength(projectMaster);
            
            HashMap<String, Double> similarities = new HashMap<>();

            for (Project p : projectsWithDesc) {
//             Calculate TF/IDF for every project in the list  
                tfIdfCalc.calculateTfIDF(p);
                tfIdfCalc.calculateLength(p);

//             Calculate cosine similarity between master project and compared project;
//             Result is double value   
                double similarity = CosineSimilarityCalculator.cosineSimilarity(projectMaster, p);
                similarities.put(p.getName(), similarity);
            }

//             No need to store similarity with itself, it is always 1  
            similarities.remove(projectMaster.getName());

            double max = 0;
            String winner = "";

//         Find maximum value in similarities  
            for (String project : similarities.keySet()) {
                if (max < similarities.get(project)) {
                    max = similarities.get(project);
                    winner = project;
                }

            }
            System.out.println("You should see also: " + winner);

        } catch (Exception ex) {
            Logger.getLogger(SoftwareProjects.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
