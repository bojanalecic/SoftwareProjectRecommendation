/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwareprojects;

import calculation.CosineSimilarityCalculator;
import calculation.TfIdfCalculator;
import domain.Project;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import services.FileService;
import services.SoftwareProjectServices;
import util.GraphOperations;
import util.StringOperations;
import services.UserServices;

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
//          Current version is developed as console application, user is supposed to enter username in order to get preferences from file
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Hello! Please enter your username!");
            String input;
            String userProject = "";
            try {
                input = br.readLine();
                UserServices.username = input;
                userProject = UserServices.findFavoriteProject();
                if (userProject == "") {
                    throw new Exception("Invalid user!");
                } else {
                    System.out.println("Your last downloaded project is: " + userProject);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            FileService fs = new FileService();
            boolean emptyFile = fs.isFileEmpty();
            HashMap<String, Double> similarities = new HashMap<>();

            LinkedList<Project> projectsWithDesc = SoftwareProjectServices.getProjectsWithDescription();

            if (emptyFile) {
                fs.writeHeader(projectsWithDesc);
            }

//          User preffered project is set to be master project
            Project projectMaster = new Project();
            for (Project p : projectsWithDesc) {
                if (p.getName().equals(userProject)) {
                    projectMaster = p;
                    break;
                }
            }

            if (fs.projectExistInFile(projectMaster.getName())) {
                fs.readSimilarities(projectMaster.getName());
            } else {
                similarities = SoftwareProjectServices.calculateSimilarities(projectMaster, projectsWithDesc);
                LinkedList<Double> temp = new LinkedList<>();
                for (double d : similarities.values()) {
                    temp.add(d);
                }
                fs.writeSimilarities(projectsWithDesc, temp);
            }

            double max = 0;
            String winner = "";

//         Find maximum value in similarities  
            for (String project : similarities.keySet()) {
                if (max < similarities.get(project) && similarities.get(project) != 1) {
                    max = similarities.get(project);
                    winner = project;
                }

            }
            System.out.println("You should see also: " + winner);

        } catch (Exception ex) {
            Logger.getLogger(SoftwareProjects.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
