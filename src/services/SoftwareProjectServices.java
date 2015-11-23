/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.hp.hpl.jena.n3.turtle.parser.ParseException;
import domain.Project;
import domain.SearchString;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import persistence.query.QueryStore;
import softwareprojects.SoftwareProjects;

/**
 *
 * @author lecicb
 */
public class SoftwareProjectServices {
    
    public static LinkedList<Project> getProjectsWithDescription() throws URISyntaxException, ParseException, java.text.ParseException {
        LinkedList<Project> projectsWithDesc = new LinkedList<Project>();
        QueryStore queryStore = new QueryStore();
        SearchString ss = new SearchString();
        projectsWithDesc = queryStore.returnProjectsWithDescriptions();
        return projectsWithDesc;
    }

    
}
