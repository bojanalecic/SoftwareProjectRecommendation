/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import domain.Project;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.apache.lucene.analysis.StopAnalyzer;

/**
 *
 * @author lecicb
 */
public class StringOperations {

    private LinkedList<Project> projects;
    InputStream modelIn = null;
    POSTaggerME tagger = null;
    POSModel model = null;

    public StringOperations(LinkedList<Project> projects) {
        try {
            this.projects = projects;
            modelIn = new FileInputStream("en-pos-maxent.bin");
            model = new POSModel(modelIn);
            tagger = new POSTaggerME(model);
        } catch (Exception ex) {
            Logger.getLogger(StringOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public LinkedList<Project> getProjects() {
        return projects;
    }

    public void setProjects(LinkedList<Project> projects) {
        this.projects = projects;
    }

    public void prepareTextForGraph() {
        for (Project project : projects) {
            String description = project.getDescription();
            
//          Use also name of project as relevant words  
            description = description + project.getName();
            
            description = description.toLowerCase();

            String[] words = description.split("\\s+");
            String[] result = removeShortWords(words);

            LinkedList<String> resultAfterTagging = tagWords(result);

            
            LinkedList<String> relevantWords = removeStopWords(resultAfterTagging);

            project.setRelevantWords(relevantWords);
        }
    }

    private static ArrayList removeDuplicates(String[] source) {
        ArrayList<String> newList = new ArrayList<String>();
        for (int i = 0; i < source.length; i++) {
            String s = source[i];
            if ((!newList.contains(s)) && s.length() > 2) {
                newList.add(s);
            }
        }
        return newList;
    }

    private LinkedList<String> removeStopWords(LinkedList<String> words) {
        String[] stopWords = StopAnalyzer.ENGLISH_STOP_WORDS;
        boolean found = false;
        LinkedList<String> finalResult = new LinkedList<>();

        for (String s : words) {
            for (String stopWord : stopWords) {
                if (s.equals(stopWord)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                finalResult.add(s);
            } else {
                found = false;
            }
        }
        return finalResult;
    }

    private String[] removeShortWords(String[] source) {
        LinkedList<String> result = new LinkedList<String>();
       
        for (int i = 0; i < source.length; i++) {
            if (source[i].length() > 2) {
                result.add(source[i]);
            }
        }
        String[] finalResult = new String[result.size()];
        for(int i = 0; i < result.size(); i++){
            finalResult[i] = result.get(i);
        }
        return finalResult;
    }

    private LinkedList<String> tagWords(String[] source) {
        LinkedList<String> result = new LinkedList<String>();
        try {
            String tags[] = tagger.tag(source);
            for (int i = 0; i < tags.length; i++) {
                if (tags[i].equals("JJ") || tags[i].equals("NN") || tags[i].equals("NNS") || tags[i].equals("NNP") || tags[i].equals("NNPS")) {
                    result.add(source[i]);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(StringOperations.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
//            if (modelIn != null) {
//                 try {
//                     modelIn.close();
//                }
//                catch (IOException e) {
//                }
//            }
        }
        return result;
    }
}
