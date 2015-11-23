/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author lecicb
 */
public class UserServices {
    
    public static String username;

    public static String findFavoriteProject(){
          BufferedReader br = null;
        // user preferences (last downloaded/seen project) are stored in file userref.csv
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader("files/userPref.csv"));
            HashMap<String, String[]> userPref = new HashMap<>();
            int index = 0;
            String favProject = "";
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.startsWith(username)) {
                    String[] data = sCurrentLine.split(",");
                    favProject = data[1];
                }
            }
            br.close();

            return favProject;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
}
