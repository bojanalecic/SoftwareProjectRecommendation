/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculation;

import domain.Project;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author lecicb
 */
public class TfIdfCalculator {

    private static LinkedList<String> getAllKeywords(LinkedList<Project> projects) {

        // this method retrieves all  relevant words for projects
        // there is check if word is already in the list
        // if it is not than word will be added in the list
        LinkedList<String> allRelevantWords = new LinkedList<String>();
        for (Project p : projects) {
            for (String relWord : p.getRelevantWords()) {
                if (contains(allRelevantWords, relWord) == 0) {
                    allRelevantWords.add(relWord);
                }
            }
        }
        return allRelevantWords;
    }

//  Calculation of term frequency for keywords of each project  
    private static HashMap<String, Integer> termFrequency(LinkedList<String> relevantWords) {
        HashMap<String, Integer> frequences = new HashMap<String, Integer>();
        int frequency = 0;

        for (String w : relevantWords) {
            if (!frequences.containsKey(w)) {
                for (String compareWord : relevantWords) {
                    if (w.equalsIgnoreCase(compareWord)) {
                        frequency++;
                    }
                }
            }
            frequences.put(w, frequency);
            frequency = 0;
        }
        return frequences;
    }

//    Normalization of previosly calculated term frequencies
    private static HashMap<String, Double> normalizeFrequency(HashMap<String, Integer> termFrequences, int size) {
        HashMap<String, Double> normFreqences = new HashMap<>();
        double normFreq;
        for (String w : termFrequences.keySet()) {
            normFreq = ((double) termFrequences.get(w) / (double)size);
            normFreqences.put(w, normFreq);
        }

        return normFreqences;
    }

//        returns 1 if keywords of compare project contain keyword of master project
    private static int contains(LinkedList<String> words, String word) {
        for (String w : words) {
            if (w.equalsIgnoreCase(word)) {
                return 1;
            }
        }
        return 0;
    }

// number of occurrences  of selected keyword is calculated for all projects
    private static int calculateOccurrences(String keyword, LinkedList<Project> projects) {
        int frequency = 0;
        for (Project p : projects) {
            frequency += contains(p.getKeywords(), keyword);
        }

        return frequency;
    }

    private static double calculateIDF(String word, int numberOfProjects, int occurrences) {
        LinkedList<Integer> idfs = new LinkedList<>();
        double idf = 0.0;
        
//        standard formula to calculate idf
        idf = 1 + Math.log(numberOfProjects / occurrences);
        return idf;
    }

    public static LinkedList<Double> getTfIDF(Project slaveProject, Project masterProject, LinkedList<Project> projects) {
//        HashMap<String, Integer> termFreqencies = termFrequency(masterProject.getRelevantWords());
        
//        Calculate term frequency for keywords 
        HashMap<String, Integer> termFreqencies = termFrequency(masterProject.getKeywords());
        HashMap<String, Double> normFrequencies;
        LinkedList<Double> tfIdf = new LinkedList<>();
//        Normalize term frequencies
         normFrequencies = normalizeFrequency(termFreqencies, masterProject.getKeywords().size());            
         for (String keyword: slaveProject.getKeywords()){
            
//          in order to calculate tf/idf we need number of occurences keyword in all projects   
            int occurrences  = calculateOccurrences(keyword, projects);
//          every word occur at least once; if it is zero, some error occured  
            if (occurrences == 0) occurrences=1;
            
//          idf is calculated for each keyword  
            double idf = calculateIDF(keyword, projects.size(), occurrences);
            
//          finally we calculate tf/idf by multiplying idf with tf  
            double fr = 0;
            if (normFrequencies.containsKey(keyword)){
                fr = normFrequencies.get(keyword);
            }else{
                fr = 0;
            }
            tfIdf.add(idf*fr);
        }        
        
        return tfIdf;

    }

}
