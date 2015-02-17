/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import domain.Project;
import java.util.ArrayList;
import java.util.LinkedList;
import org.apache.lucene.analysis.StopAnalyzer;

/**
 *
 * @author lecicb
 */
public class StringOperations {
    private LinkedList<Project> projects;

    public StringOperations(LinkedList<Project> projects) {
        this.projects = projects;
    }
    
     public void prepareTextForGraph() {
        for (Project project : projects) {
            String description = project.getDescription();
            description.toLowerCase();
            
            String[] words = description.split("\\s+");
            LinkedList<String> result = removeShortWords(words);
            
           LinkedList<String> relevantWords = removeStopWords(result);
            
            project.setRelevantWords(relevantWords);
        }
    }
    
    private static ArrayList removeDuplicates(String[] source){
    ArrayList<String> newList = new ArrayList<String>();
    for (int i=0; i<source.length; i++){
        String s = source[i];
        if (!newList.contains(s) && s.length() > 2){
            newList.add(s);
        }
    }
    return newList;
}

    private LinkedList<String> removeStopWords(LinkedList<String> words) {
        String[] stopWords = StopAnalyzer.ENGLISH_STOP_WORDS;
       boolean found = false;
        
        StringBuilder result = new StringBuilder(words.size());
        for (String s : words) {
            for (String stopWord : stopWords) {
                if (s.equals(stopWord)) {
                    found = true;
                    break;
                }
            }
            if (!found){                
                result.append(s);
                result.append(" ");
            }else{
                found = false;
            }
        }
        String[] arrayResult = result.toString().replaceAll("\\p{P}", "").split(" ");
        LinkedList<String> finalResult = new LinkedList<>();
        for (String word : arrayResult) {
            finalResult.add(word);
        }
        return finalResult;
    }
    private LinkedList<String> removeShortWords(String[] source) {
        LinkedList<String> result = new LinkedList<String>();
        for (int i=0; i<source.length; i++){
            if (source[i].length() > 2){
            result.add(source[i]);
        }
    }
    return result;
    }
}
