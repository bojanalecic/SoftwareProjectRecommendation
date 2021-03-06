/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import edu.uci.ics.jung.graph.Graph;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;
import util.Constants;

/**
 *
 * @author Boban
 */
@Namespace(Constants.DOAP_NS)
@RdfType("Project")
public class Project extends Thing {

    @RdfProperty(Constants.DOAP_NS + "name")
    private String name;
    @RdfProperty(Constants.DC_NS + "description")
    private String description;
    @RdfProperty(Constants.RDF_NS + "seeAlso")
    private URI seeAlso;
    @RdfProperty(Constants.DOAP_NS + "download-page")
    private URI downloadpage;
    @RdfProperty(Constants.DOAP_NS + "homepage")
    private URI homepage;
    @RdfProperty(Constants.DOAP_NS + "category")
    private Collection<String> category;
    @RdfProperty(Constants.DOAP_NS + "license")
    private Collection<String> license;
    @RdfProperty(Constants.DOAP_NS + "programming-language")
    private Collection<String> programminglanguage;
    @RdfProperty(Constants.DOAP_NS + "os")
    private Collection<String> os;
    @RdfProperty(Constants.DOAP_NS + "release")
    private Collection<Version> release;
    @RdfProperty(Constants.DOAP_NS + "maintainer")
    private Person maintainer;
    
    private LinkedList<String> keywords;
    private LinkedList<String> keywordsWeighted;
    private LinkedList<String> relevantWords;
    private Graph graph;
    private LinkedList<Double> tfIdf;
    private double length;

    public Project() {
        keywords = new LinkedList<>();
        keywordsWeighted = new LinkedList<>();
        relevantWords = new LinkedList<>();
    }

    public Project(String name, String description, URI adress, URI downloadLink, URI homePage, List<String> tags, List<String> licenses, List<String> programmingLanguages, List<String> operatingSystems, List<Version> releaseList) {
        this.name = name;
        this.description = description;

        this.seeAlso = adress;
        this.downloadpage = downloadLink;
        this.homepage = homePage;
        this.category = tags;
        this.license = licenses;
        this.programminglanguage = programmingLanguages;
        this.os = operatingSystems;
        this.release = releaseList;
        keywords = new LinkedList<>();
        relevantWords = new LinkedList<>();
        keywordsWeighted = new LinkedList<>();
        tfIdf = new LinkedList<Double>();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the adress
     */
    public URI getSeeAlso() {
        return seeAlso;
    }

    /**
     * @param adress the adress to set
     */
    public void setSeeAlso(URI adress) {
        this.seeAlso = adress;
    }

    /**
     * @return the downloadLink
     */
    public URI getDownloadpage() {
        return downloadpage;
    }

    /**
     * @param downloadpage the downloadLink to set
     */
    public void setDownloadpage(URI downloadLink) {
        this.downloadpage = downloadLink;
    }

    /**
     * @return the homePage
     */
    public URI getHomepage() {
        return homepage;
    }

    /**
     * @param homePage the homePage to set
     */
    public void setHomepage(URI homePage) {
        this.homepage = homePage;
    }

    /**
     * @return the tags
     */
    public Collection<String> getCategory() {
        return category;
    }

    /**
     * @param tags the tags to set
     */
    public void setCategory(Collection<String> tags) {
        this.category = tags;
    }

    /**
     * @return the licenses
     */
    public Collection<String> getLicense() {
        return license;
    }

    /**
     * @param licenses the licenses to set
     */
    public void setLicense(Collection<String> licenses) {
        this.license = licenses;
    }

    /**
     * @return the programmingLanguages
     */
    public Collection<String> getProgramminglanguage() {
        return programminglanguage;
    }

    /**
     * @param programmingLanguages the programmingLanguages to set
     */
    public void setProgramminglanguages(Collection<String> programmingLanguages) {
        this.programminglanguage = programmingLanguages;
    }

    /**
     * @return the operatingSystems
     */
    public Collection<String> getOs() {
        return os;
    }

    /**
     * @param operatingSystems the operatingSystems to set
     */
    public void setOs(Collection<String> operatingSystems) {
        this.os = operatingSystems;
    }

    /**
     * @return the release
     */
    public Collection<Version> getRelease() {
        return release;
    }

    /**
     * @param release the release to set
     */
    public void setRelease(Collection<Version> releaseList) {
        this.release = releaseList;
    }

    /**
     * @return the maintainer
     */
    public Person getMaintainer() {
        return maintainer;
    }

    /**
     * @param maintainer the maintainer to set
     */
    public void setMaintainer(Person maintainer) {
        this.maintainer = maintainer;
    }
    
     public LinkedList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(LinkedList<String> keywords) {
        this.keywords = keywords;
    }
    
    public LinkedList<String> getRelevantWords() {
        return relevantWords;
    }

    public void setRelevantWords(LinkedList<String> relevantWords) {
        this.relevantWords = relevantWords;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public LinkedList<String> getKeywordsWeighted() {
        return keywordsWeighted;
    }

    public void setKeywordsWeighted(LinkedList<String> keywordsWeighted) {
        this.keywordsWeighted = keywordsWeighted;
    }

    public LinkedList<Double> getTfIdf() {
        return tfIdf;
    }

    public void setTfIdf(LinkedList<Double> tfIdf) {
        this.tfIdf = tfIdf;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
    
    
    
    
}
