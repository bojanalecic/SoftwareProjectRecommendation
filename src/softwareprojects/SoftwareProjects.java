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
import util.GraphOperations;
import util.StringOperations;

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
//           Current version is developed as console application, user is supposed to enter username in order to get preferences from file
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Hello! Please enter your username!");
            String input;
            String userProject = "";
            try {
                input = br.readLine();
                userProject = getUserPreferences(input);
                if (userProject == "") {
                    throw new Exception("Invalid user!");
                }else{
                    System.out.println("Your last downloaded project is: "+userProject);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }

            LinkedList<Project> projectsWithDesc = getProjectsWithDescription();

//          Remove all stopwords and irrelevant words  
            StringOperations so = new StringOperations(projectsWithDesc);
            so.prepareTextForGraph();

//          Based on relevant words, create graph using jung library and extract keywords based on degree centrality method        
            GraphOperations go = new GraphOperations(so.getProjects());
            go.createGraphForEachProject();

            projectsWithDesc = go.getProjects();

            
            

            Project projectMaster = new Project();
            for (Project p : projectsWithDesc) {
                if (p.getName().equals(userProject)) {
                    projectMaster = p;
                }
            }
            
            

//         By using previosly extracted keywords, calculate TF/IDF for project that we want to compare to others    
            LinkedList<Double> query = TfIdfCalculator.getTfIDF(projectMaster, projectMaster, projectsWithDesc);
            LinkedList<Double> comparison;
            HashMap<String, Double> similarities = new HashMap<>();

            for (Project p : projectsWithDesc) {
//             Calculate TF/IDF for every project in the list  
                comparison = TfIdfCalculator.getTfIDF(projectMaster, p, projectsWithDesc);

//             Calculate cosine similarity between master project and compared project;
//             Result is double value   
                double similarity = CosineSimilarityCalculator.cosineSimilarity(query, comparison);
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

    private static LinkedList<Project> getProjectsWithDescription() throws URISyntaxException, ParseException, java.text.ParseException {
        LinkedList<Project> projectsWithDesc = new LinkedList<Project>();
        QueryStore queryStore = new QueryStore();
        SearchString ss = new SearchString();
        projectsWithDesc = queryStore.returnProjectsWithDescriptions();
        return projectsWithDesc;
    }

    private static String getUserPreferences(String username) {

        BufferedReader br = null;
        // user preferences (last downloaded/seen project) are stored in file userref.csv
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader("files/userPref.csv"));
            HashMap<String, String[]> userPref = new HashMap<>();
            int index = 0;
            String favProject = "";
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.startsWith(username)) {
                    String[] data = sCurrentLine.split(",");
                    favProject = data[1];
                }
            }
            br.close();

            return favProject;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
