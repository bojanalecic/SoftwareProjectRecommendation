/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import domain.Project;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEvent.Edge;
import edu.uci.ics.jung.graph.event.GraphEvent.Vertex;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import services.PropertiesReader;

/**
 *
 * @author lecicb
 */
public class GraphOperations {

    private LinkedList<Project> projects;
    public int numberOfWords;

    public GraphOperations(LinkedList<Project> projects) throws IOException {
        this.projects = projects;
        PropertiesReader pr = new PropertiesReader();
        numberOfWords = pr.getNumberOfWords();

    }

    public LinkedList<Project> getProjects() {
        return projects;
    }

    public void setProjects(LinkedList<Project> projects) {
        this.projects = projects;
    }

    public LinkedList<Project> createGraphForEachProject() {
        for (Project project : projects) {
            Graph g = createGraph(project.getRelevantWords());
            project.setGraph(g);

//          Once graph is created, we extract keywords based on Degree centrality method  
            extractKeywordsDegreeCentrality(project);
        }
        return projects;
    }

    private Graph createGraph(LinkedList<String> words) {
        Graph<String, Edge> g = new UndirectedSparseGraph<>();
        for (int i = 0; i < words.size() - 1; i++) {
            if (!g.containsVertex(words.get(i))) {
                g.addVertex(words.get(i));
                if (!g.containsVertex(words.get(i + 1))) {
                    g.addVertex(words.get(i + 1));
                    Edge e = new Edge(g, GraphEvent.Type.EDGE_ADDED, 1);
                    g.addEdge(e, words.get(i), words.get(i + 1));
                } else {
                    Edge e = g.findEdge(words.get(i), words.get(i + 1));
                    if (e != null) {
                        int weight = (Integer) e.getEdge();
                        g.removeEdge(e);
                        e = new Edge(g, GraphEvent.Type.EDGE_ADDED, weight + 1);
                    } else {
                        Edge e2 = new Edge(g, GraphEvent.Type.EDGE_ADDED, 1);
                        g.addEdge(e2, words.get(i), words.get(i + 1));
                    }
                }
            } else {
                if (!g.containsVertex(words.get(i + 1))) {
                    g.addVertex(words.get(i + 1));
                    Edge<String, String> e = new Edge(g, GraphEvent.Type.EDGE_ADDED, 1);
                    g.addEdge(e, words.get(i), words.get(i + 1));
                } else {
                    Edge e = g.findEdge(words.get(i), words.get(i + 1));
                    if (e != null) {
                        int weight = (Integer) e.getEdge();
                        g.removeEdge(e);
                        e = new Edge(g, GraphEvent.Type.EDGE_ADDED, weight + 1);
                    } else {
                        Edge e2 = new Edge(g, GraphEvent.Type.EDGE_ADDED, 1);
                        g.addEdge(e2, words.get(i), words.get(i + 1));
                    }
                }
            }
        }
        return g;

    }

    private void extractKeywordsDegreeCentrality(Project project) {
        Graph g = project.getGraph();
        LinkedHashMap<String, Double> degrees = new LinkedHashMap<String, Double>();

        Collection<String> vertices = g.getVertices();
        
        for (String vertice : vertices) {
            double numberOfNodes = g.getVertexCount();
            int degree = g.outDegree(vertice);
            double degreeCentrality = degree / numberOfNodes;
            degrees.put(vertice, degreeCentrality);
        }
        LinkedList<String> keywords = new LinkedList<>();

        ValueComparator bvc = new ValueComparator(degrees);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);

        sorted_map.putAll(degrees);
        int counter = 0;
        for (Map.Entry<String, Double> entrySet : sorted_map.entrySet()) {
            String key = entrySet.getKey();
            Double value = entrySet.getValue();
            keywords.add(key);
            counter++;
            if (counter == numberOfWords) {
                break;
            }
        }

        project.setKeywords(keywords);
    }

}
