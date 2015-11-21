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

    private static HashMap<String, Double> normalizeFrequency(HashMap<String, Integer> termFrequences, int size) {
        HashMap<String, Double> normFreqences = new HashMap<>();
        double normFreq;
        for (String w : termFrequences.keySet()) {
            normFreq = ((double) termFrequences.get(w) / (double)size);
            normFreqences.put(w, normFreq);
        }

        return normFreqences;
    }

    private static int contains(LinkedList<String> words, String word) {
        for (String w : words) {
            if (w.equalsIgnoreCase(word)) {
                return 1;
            }
        }
        return 0;
    }

    private static int calculateOccurrences(String keyword, LinkedList<Project> projects) {
        // number of occurrences  of selected keyword is calculated for all projects
        int frequency = 0;
        for (Project p : projects) {
            frequency += contains(p.getRelevantWords(), keyword);
        }

        return frequency;
    }

    private static double calculateIDF(String word, int numberOfProjects, int occurrences) {
        LinkedList<Integer> idfs = new LinkedList<>();
        double idf = 0.0;
        idf = 1 + Math.log(numberOfProjects / occurrences);
        return idf;
    }

    public static LinkedList<Double> getTfIDF(Project slaveProject, Project masterProject, LinkedList<Project> projects) {
        HashMap<String, Double> normFrequencies;
        LinkedList<Double> tfIdf = new LinkedList<>();
        for (String keyword: slaveProject.getKeywords()){
            
            HashMap<String, Integer> termFreqencies = termFrequency(masterProject.getRelevantWords());
            normFrequencies = normalizeFrequency(termFreqencies, masterProject.getRelevantWords().size());            
            int occurrences  = calculateOccurrences(keyword, projects);
            if (occurrences == 0) occurrences=1;
            
            double idf = calculateIDF(keyword, projects.size(), occurrences);
            
            tfIdf.add(idf);
        }        
        
        return tfIdf;

    }

}
