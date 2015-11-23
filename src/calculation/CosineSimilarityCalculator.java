/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculation;

import domain.Project;
import java.util.LinkedList;

/**
 *
 * @author lecicb
 */
public class CosineSimilarityCalculator {

    public static double cosineSimilarity(Project projectMaster, Project projectSlave) throws Exception {

        LinkedList<Double> masterTfIdf = projectMaster.getTfIdf();
        LinkedList<Double> slaveTfIdf = projectSlave.getTfIdf();

        if (masterTfIdf.size() != slaveTfIdf.size()) {
            throw new Exception("Error occured");
        }
        
        double a = 0.0;
        double b = 0.0;

        // algorithm for calculation of cosine similarity  
        for (int i = 0; i <slaveTfIdf.size(); i++) {
            a = a + (masterTfIdf.get(i)*slaveTfIdf.get(i)); 
        }
        
        b = projectMaster.getLength() * projectSlave.getLength();
       
      return a/b;
    }

}
