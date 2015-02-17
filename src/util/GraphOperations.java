/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import domain.Project;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEvent.Edge;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.event.GraphEvent.Vertex;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import static javafx.scene.input.KeyCode.V;

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
            
            extractKeywordsDegreeCentrality(project);
        }
    }
    
   private  Graph createGraph(LinkedList<String> words) {
        Graph<String, Edge> g = new DirectedSparseGraph();
        for (int i = 0; i < words.size()-1; i++) {
            if (!g.containsVertex(words.get(i))){
                g.addVertex(words.get(i));
                if (!g.containsVertex(words.get(i+1))){
                    g.addVertex(words.get(i+1));
                    Edge e = new Edge(g, GraphEvent.Type.EDGE_ADDED, 1);
                    g.addEdge(e, words.get(i), words.get(i+1));
                }else{
                    Edge e= g.findEdge(words.get(i), words.get(i+1));
                    if (e != null){
                        int weight = (Integer)e.getEdge();
                        g.removeEdge(e);
                        e = new Edge(g, GraphEvent.Type.EDGE_ADDED, weight+1);
                    }else{
                        Edge e2 = new Edge(g, GraphEvent.Type.EDGE_ADDED, 1);
                        g.addEdge(e2, words.get(i), words.get(i+1));
                    }
                }
            }else{
                if (!g.containsVertex(words.get(i+1))){
                    g.addVertex(words.get(i+1));
                    Edge<String, String> e = new Edge(g, GraphEvent.Type.EDGE_ADDED, 1);
                    g.addEdge(e, words.get(i), words.get(i+1));
                }else{
                    Edge e= g.findEdge(words.get(i), words.get(i+1));
                    if (e != null){
                        int weight = (Integer)e.getEdge();
                        g.removeEdge(e);
                        e = new Edge(g, GraphEvent.Type.EDGE_ADDED, weight+1);
                    }else{
                        Edge e2 = new Edge(g, GraphEvent.Type.EDGE_ADDED, 1);
                        g.addEdge(e2, words.get(i), words.get(i+1));
                    }
                }
            }          
        }
        return g;
        
    }

    private void  extractKeywordsDegreeCentrality(Project project) {
        Graph g = project.getGraph();
        HashMap<String, Double> degrees = new HashMap<String, Double>();
        
        Collection<String> vertices = g.getVertices();
        
        for (String vertice : vertices) {
            int connectedNodes = g.getNeighborCount(vertice);
            int numberOfNodes = g.getVertexCount();
            double degreeCentrality = connectedNodes/numberOfNodes;
            degrees.put(vertice, degreeCentrality);
        }
        String[] keywords = new String[5];
        String currentMax = degrees.keySet().toArray()[0].toString();
        for (Map.Entry<String, Double> entry : degrees.entrySet()) {
             String key = entry.getKey();
             Object value = entry.getValue();
   
        }
        
    }
    
}
