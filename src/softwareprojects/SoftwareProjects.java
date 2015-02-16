/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwareprojects;

import com.hp.hpl.jena.n3.turtle.parser.ParseException;
import domain.Project;
import domain.SearchString;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.event.GraphEvent.Vertex;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.renderers.Renderer.Edge;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.StopAnalyzer;
import persistence.query.QueryStore;

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
            // TODO code application logic here
            LinkedList<Project> projectsWithDesc = getProjectsWithDescription();

            System.out.println(projectsWithDesc.size());
            
            extractKeywords(projectsWithDesc);
            createGraph();
            
        } catch (URISyntaxException ex) {
            Logger.getLogger(SoftwareProjects.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(SoftwareProjects.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.text.ParseException ex) {
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

    private static void extractKeywords(LinkedList<Project> projectsWithDesc) {
        for (Project project : projectsWithDesc) {
            project.setDescription(project.getDescription().toLowerCase());
            
            //   ArrayList<String> wordsCorrect = removeShortWords(words);
           
            project.setDescription(removeStopWords(project.getDescription()));
           // Graph g = new DirectedSparseGraph();
            
        }
    }
    
    public static ArrayList removeDuplicates(String[] source){
    ArrayList<String> newList = new ArrayList<String>();
    for (int i=0; i<source.length; i++){
        String s = source[i];
        if (!newList.contains(s) && s.length() > 2){
            newList.add(s);
        }
    }
    return newList;
}

    private static String removeStopWords(String source) {
        String[] stopWords = StopAnalyzer.ENGLISH_STOP_WORDS;
       boolean found = false;
        
        StringBuilder result = new StringBuilder(source.length());
        for (String s : source.split("\\s+")) {
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
        String finalResult = result.toString().replaceAll("\\p{P}", "");
        return finalResult;
    }
    private static ArrayList<String> removeShortWords(String source) {
        ArrayList<String> newList = new ArrayList<String>();
        return newList;
//    for (int i=0; i<source.length(); i++){
//        String s = source[i];
//        if (s.length() > 2){
//            newList.add(s);
//        }
//    }
//    return newList;
    }

    private static void createGraph(String source) {
        String[] words = source.split(" ");
        Graph<String, Integer> g = new DirectedSparseGraph();
        for (int i = 0; i < words.length-1; i++) {
            g.addVertex(words[i]);
            g.addVertex(words[i+1]);
            g.addEdge(i, words[i], words[i+1], EdgeType.DIRECTED);
        }
        
    }
    
}
