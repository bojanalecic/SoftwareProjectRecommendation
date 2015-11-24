/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import calculation.CosineSimilarityCalculator;
import calculation.TfIdfCalculator;
import com.hp.hpl.jena.n3.turtle.parser.ParseException;
import domain.Project;
import domain.SearchString;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import persistence.query.QueryStore;
import softwareprojects.SoftwareProjects;
import util.GraphOperations;
import util.StringOperations;

/**
 *
 * @author lecicb
 */
public class SoftwareProjectServices {
    
    public static LinkedList<Project> getProjectsWithDescription() throws URISyntaxException, ParseException, java.text.ParseException {
        LinkedList<Project> projectsWithDesc = new LinkedList<Project>();
        QueryStore queryStore = new QueryStore();
        SearchString ss = new SearchString();
        projectsWithDesc = queryStore.returnProjectsWithDescriptions();
        return projectsWithDesc;
    }

    public static HashMap<String, Double> calculateSimilarities(Project projectMaster, LinkedList<Project> projects) throws IOException, Exception{
//          Remove all short words, words that are not neither nouns or adjectives, stopwords and irrelevant words  
            StringOperations so = new StringOperations(projects);
            so.prepareTextForGraph();

//          Based on relevant words, create graph using jung library and extract keywords based on degree centrality method        
            GraphOperations go = new GraphOperations(so.getProjects());
            projects = go.createGraphForEachProject();

            TfIdfCalculator tfIdfCalc = new TfIdfCalculator(projects);

//         By using previosly extracted keywords, calculate TF/IDF for project that we want to compare to others           
            tfIdfCalc.calculateTfIDF(projectMaster);
            tfIdfCalc.calculateLength(projectMaster);

            HashMap<String, Double> similarities = new HashMap<>();

            for (Project p : projects) {
//             Calculate TF/IDF for every project in the list  
                tfIdfCalc.calculateTfIDF(p);
                tfIdfCalc.calculateLength(p);

//             Calculate cosine similarity between master project and compared project;
//             Result is double value   
                double similarity = CosineSimilarityCalculator.cosineSimilarity(projectMaster, p);
                similarities.put(p.getName(), similarity);
            }
            
            return similarities;
    }
}
