/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author lecicb
 */
public class PropertiesReader {
    
    Properties prop;
    String propFileName;
    InputStream inputStream;

    public PropertiesReader() throws FileNotFoundException, IOException {
        propFileName = "files/config.properties";
        this.prop = new Properties();
        inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        
        if (inputStream !=null){
            prop.load(inputStream);
        }else{
            throw new FileNotFoundException("Properties file "+propFileName+"not found!");
        }
    }

    public int getNumberOfWords() {
        return Integer.parseInt(prop.getProperty("numberOfWords"));
    }
    
    public double getDescShare() {
        return Double.parseDouble(prop.getProperty("description"));
    }
    
    public double getProgLangShare() {
        return Double.parseDouble(prop.getProperty("programmingLanguage"));
    }
    
    public double getOSShare() {
        return Double.parseDouble(prop.getProperty("operatingSystem"));
    }
}
