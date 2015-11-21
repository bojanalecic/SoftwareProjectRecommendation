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
		double query = 0.0;
		double document = 0.0;
		double cosineSimilarity = 0.0;
		// docVector1 and docVector2 must be of same length
		for (int i = 0; i < projectMaster.size(); i++) {
			dotProduct += projectMaster.get(i) * projectSlave.get(i); // a.b
			query += Math.pow(projectMaster.get(i), 2); // (a^2)
			document += Math.pow(projectSlave.get(i), 2); // (b^2)
		}

		query = Math.sqrt(query);// sqrt(a^2)
		document = Math.sqrt(document);// sqrt(b^2)

		if (query != 0.0 & document != 0.0) {
			cosineSimilarity = dotProduct / (query * document);
		} else {
			return 0.0;
		}
		return cosineSimilarity;
	}

}
