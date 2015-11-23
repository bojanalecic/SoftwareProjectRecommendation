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

    private LinkedList<Project> projects;
    private LinkedList<String> allKeywords;
    private LinkedList<Double> idfs;
    
    public TfIdfCalculator(LinkedList<Project> projects) {
        this.projects = projects;
        allKeywords = new LinkedList<>();
        idfs = new LinkedList<>();
        getAllKeywords();

//      calculate IDF for every word in whole list
        calculateIDF();
    }

    public void calculateTfIDF(Project project) {

//        Calculate term frequency for all keywords for both master project and slave project
        LinkedList<Integer> masterTermFreqencies = termFrequency(project.getKeywords());;

         LinkedList<Double> tfIdfs = new LinkedList<Double>();
        
        for(int i = 0; i < masterTermFreqencies.size(); i++){
//          calculate tf/idf by multiplying idf with tf  
            tfIdfs.add(idfs.get(i) * masterTermFreqencies.get(i));
        }
        project.setTfIdf(tfIdfs);
    }
    
    public void calculateLength(Project project){
       LinkedList<Double> tfidf = project.getTfIdf();
        double length = 0.0;
        for (int i = 0; i< tfidf.size(); i++){
            length = length + Math.pow(tfidf.get(i), 2);
        }
        project.setLength(Math.sqrt(length));
    }

    private void getAllKeywords() {

        // this method retrieves all  relevant words for projects
        // there is check if word is already in the list
        // if it is not than word will be added in the list
        for (Project p : projects) {
            for (String relWord : p.getRelevantWords()) {
                if (contains(allKeywords, relWord) == 0) {
                    allKeywords.add(relWord);
                }
            }
        }
    }

//  Calculation of term frequency for keywords of each project  
    private LinkedList<Integer> termFrequency(LinkedList<String> keyWords) {
        LinkedList<Integer> frequences = new LinkedList<Integer>();
        int frequency = 0;

        for (String w : allKeywords) {
                for (String compareWord : keyWords) {
                    if (w.equalsIgnoreCase(compareWord)) {
                        frequency++;
                    }
                }
            frequences.add(frequency);
            frequency = 0;
        }
        return frequences;
    }

    private void calculateIDF() {
        for (String word : allKeywords) {
            int occurrences = calculateOccurrences(word);
//          every word occur at least once; if it is zero, some error occured  
            if (occurrences == 0) {
                occurrences = 1;
            }
            double idf = 0.0;

//        standard formula to calculate idf
            idf = 1 + Math.log(projects.size() / occurrences);
            idfs.add(idf);
        }
    }

    // number of occurrences  of selected keyword is calculated for all projects
    private int calculateOccurrences(String keyword) {
        int frequency = 0;
        for (Project p : projects) {
            frequency += contains(p.getKeywords(), keyword);
        }

        return frequency;
    }

    //        returns 1 if keywords of compare project contain keyword of master project
    private int contains(LinkedList<String> words, String word) {
        for (String w : words) {
            if (w.equalsIgnoreCase(word)) {
                return 1;
            }
        }
        return 0;
    }

}
