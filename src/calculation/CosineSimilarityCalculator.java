/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculation;

import java.util.LinkedList;

/**
 *
 * @author lecicb
 */
public class CosineSimilarityCalculator {
    
    public static double cosineSimilarity(LinkedList<Double> projectMaster, LinkedList<Double> projectSlave) {
		// algorithm for calculation of cosine similarity  
		double dotProduct = 0.0;
		double magnitude1 = 0.0;
		double magnitude2 = 0.0;
		double cosineSimilarity = 0.0;
		// docVector1 and docVector2 must be of same length
		for (int i = 0; i < projectMaster.size(); i++) {
			dotProduct += projectMaster.get(i) * projectSlave.get(i); // a.b
			magnitude1 += Math.pow(projectMaster.get(i), 2); // (a^2)
			magnitude2 += Math.pow(projectSlave.get(i), 2); // (b^2)
		}

		magnitude1 = Math.sqrt(magnitude1);// sqrt(a^2)
		magnitude2 = Math.sqrt(magnitude2);// sqrt(b^2)

		if (magnitude1 != 0.0 | magnitude2 != 0.0) {
			cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
		} else {
			return 0.0;
		}
		return cosineSimilarity;
	}

}
