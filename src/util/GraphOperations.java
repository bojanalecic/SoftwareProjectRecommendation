/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import domain.Project;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author lecicb
 */
public class GraphOperations {
    
    private LinkedList<Project> projects;

    public GraphOperations(LinkedList<Project> projects) {
        this.projects = projects;
    }
    
    public void createGraphForEachProject(){
        for (Project project : projects) {
            Graph g =createGraph(project.getRelevantWords());
            project.setGraph(g);
        }
    }
    
   private  Graph createGraph(LinkedList<String> words) {
        Graph<String, Integer> g = new DirectedSparseGraph();
        for (int i = 0; i < words.size()-1; i++) {
            if (!g.containsVertex(words.get(i))){
                g.addVertex(words.get(i));
                if (!g.containsVertex(words.get(i+1))){
                    g.addVertex(words.get(i+1));
                    g.addEdge(1, words.get(i), words.get(i+1), EdgeType.DIRECTED);
                }else{
                    int edge = g.findEdge(words.get(i), words.get(i+1));
                    g.addEdge(edge+1, words.get(i), words.get(i+1), EdgeType.DIRECTED);
                }
            }else{
                if (!g.containsVertex(words.get(i+1))){
                    g.addVertex(words.get(i+1));
                    g.addEdge(1, words.get(i), words.get(i+1), EdgeType.DIRECTED);
                }else{
                    int edge = g.findEdge(words.get(i), words.get(i+1));
                    g.addEdge(edge+1, words.get(i), words.get(i+1), EdgeType.DIRECTED);
                }
            }          
        }
        return g;
        
    }
    
}
