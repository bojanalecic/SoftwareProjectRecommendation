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
            LinkedList<Double> similarities = new LinkedList<Double>();
            String input;
            String userProject = "";
            Project projectMaster = new Project();
            LinkedList<Project> projectsWithDesc;

            System.out.println("Hello! Please enter your username!");
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

            if (fs.projectExistInFile(userProject)) {
                similarities = fs.readSimilarities(userProject);
            } else {
                boolean emptyFile = fs.isFileEmpty();
                projectsWithDesc = SoftwareProjectServices.getProjectsWithDescription();
                if (emptyFile) {
                    fs.writeHeader(projectsWithDesc);
                }

                //          User preffered project is set to be master project
                for (Project p : projectsWithDesc) {
                    if (p.getName().equals(userProject)) {
                        projectMaster = p;
                        break;
                    }
                }
                similarities = SoftwareProjectServices.calculateSimilarities(projectMaster, projectsWithDesc);
                fs.writeSimilarities(projectMaster.getName(), similarities);
            }

            LinkedList<String> titles = fs.readTitles();
            titles = SoftwareProjectServices.findMostSimilar(similarities, titles);
            String output = "You should see also: ";
            for (String title : titles) {
                output = output + title + ", ";
            }
            System.out.println(output);
        } catch (Exception ex) {
            Logger.getLogger(SoftwareProjects.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
