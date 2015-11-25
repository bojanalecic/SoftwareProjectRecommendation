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
import java.util.Map;
import java.util.TreeMap;
import persistence.query.QueryStore;
import softwareprojects.SoftwareProjects;
import util.GraphOperations;
import util.StringOperations;
import util.ValueComparator;

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

    public static LinkedList<Double> calculateSimilarities(Project projectMaster, LinkedList<Project> projects) throws IOException, Exception {
        LinkedList<Double> similarities = new LinkedList<>();

        LinkedList<Double> descSimilarities = calculateDescriptionSimilarity(projectMaster, projects);

        LinkedList<Integer> progLangSimilarities = calculateProgLangSimilarities(projectMaster, projects);

        LinkedList<Integer> osSimilarities = calculateOSSimilarities(projectMaster, projects);

        PropertiesReader pr = new PropertiesReader();
        double descShare = pr.getDescShare();
        double progLangShare = pr.getProgLangShare();
        double osShare = pr.getOSShare();

        for (int i = 0; i < descSimilarities.size(); i++) {
            double finalSim = descSimilarities.get(i) * descShare + progLangSimilarities.get(i) * progLangShare + osSimilarities.get(i) * osShare;
            similarities.add(finalSim);
        }
        return similarities;
    }

    private static LinkedList<Double> calculateDescriptionSimilarity(Project projectMaster, LinkedList<Project> projects) throws IOException, Exception {
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

        LinkedList<Double> similarities = new LinkedList<>();

        for (Project p : projects) {
//             Calculate TF/IDF for every project in the list  
            tfIdfCalc.calculateTfIDF(p);
            tfIdfCalc.calculateLength(p);

//             Calculate cosine similarity between master project and compared project;
//             Result is double value   
            double similarity = CosineSimilarityCalculator.cosineSimilarity(projectMaster, p);

            similarities.add(similarity);
        }
        return similarities;
    }

    private static LinkedList<Integer> calculateProgLangSimilarities(Project projectMaster, LinkedList<Project> projects) {
        LinkedList<Integer> sims = new LinkedList<>();
        for (Project p : projects) {
            try {
                if (p.getProgramminglanguage().iterator().next() == projectMaster.getProgramminglanguage().iterator().next()) {
                    sims.add(1);
                } else {
                    sims.add(0);
                }
            } catch (Exception e) {
                sims.add(0);
            }
        }
        return sims;
    }

    private static LinkedList<Integer> calculateOSSimilarities(Project projectMaster, LinkedList<Project> projects) {
        LinkedList<Integer> sims = new LinkedList<>();
        for (Project p : projects) {
            try {
                if (p.getOs().iterator().next() == projectMaster.getOs().iterator().next()) {
                    sims.add(1);
                } else {
                    sims.add(0);
                }
            } catch (Exception e) {
                sims.add(0);
            }
        }
        return sims;
    }

    public static LinkedList<String> findMostSimilar(LinkedList<Double> similarities, LinkedList<String> titles) {
        double max = 0;
        HashMap<String, Double> map = new HashMap<>();
        LinkedList<String> winners = new LinkedList<String>();
        String winner = "";
        for (int i = 0; i < similarities.size(); i++) {
            map.put(titles.get(i), similarities.get(i));

        }
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);

        sorted_map.putAll(map);
        int counter = 0;
        for (Map.Entry<String, Double> entrySet : sorted_map.entrySet()) {
            String key = entrySet.getKey();
            Double value = entrySet.getValue();
            if (value == 1) continue;
            winners.add(key);
            counter++;
            if (counter == 5) {
                break;
            }
        }

        return winners;
    }
}
