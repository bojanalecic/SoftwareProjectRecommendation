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
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.event.GraphEvent.Vertex;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.renderers.Renderer.Edge;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.StopAnalyzer;
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
            
            LinkedList<Project> projectsWithDesc = getProjectsWithDescription();
           
            StringOperations so = new StringOperations(projectsWithDesc);
            so.prepareTextForGraph();
            
                 
            GraphOperations go = new GraphOperations(so.getProjects());
            go.createGraphForEachProject();
            
            projectsWithDesc = go.getProjects();
            
           LinkedList<Double> query = TfIdfCalculator.getTfIDF(projectsWithDesc.get(0), projectsWithDesc.get(0), projectsWithDesc);
           LinkedList<Double> masterDocument;
           HashMap<String, Double> similarities = new HashMap<>();
           
           for (Project p: projectsWithDesc) {
               masterDocument = TfIdfCalculator.getTfIDF(projectsWithDesc.get(0), p, projectsWithDesc);
               double similarity = CosineSimilarityCalculator.cosineSimilarity(query, masterDocument);
               similarities.put(p.getName(),similarity);
               similarities.remove(projectsWithDesc.get(0).getName());
           }
           double max = 0;
           String winner;
           for (String project: similarities.keySet()){
               if (max< similarities.get(project)){
               max = similarities.get(project);
               winner = project;
            }
           }
            
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

    
}
