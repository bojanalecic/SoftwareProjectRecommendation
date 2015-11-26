# Software Project Recommendation
============
# 1. About the project
The idea of this project is recommendation of software projects based on user's past actions and preferences. The assumption is that user's preferences are already known (currently user preferences are reduced to the software project the user has recently expressed interest in). The data repository (an- RDF file) is already filled by extracting data about software projects from websites containing software application catalogs. Initially, project used data from the Freecode website, and now it is expanded with data from SourceForge website.

When user enters username, prefrences are read from local file. Similarity calculation engine calculates top 5 most similar projects to the user's preffered project and recommends those projects to the user.
There are three criterias for project comparison: description of project, programming language in which project is developed and operating system. It is possible to configure (via the properties file) how much each criteria affects similarity.

To be used for recommendation purposes, descriptions of projects must be formatted and processed to be usefull for different calculations. The idea is to extract particular number of keywords from each description. The name of project is added to the description as it could be used as keyword also.  The number of keywords is also configurable (through the properties file). The process of keywords extraction follows these steps:

- Lower case all words in a description

- Remove all words shorter than 3 characters

- POS-tag words to avoid using of verbs, adverbs, articles, etc.; only nouns and adjectives are considered useful

- Remove stop-words

When this process is finished, Graph is created from relevant words. For this purpose, [jung](http://jung.sourceforge.net/ ) library is used. We use weighted undirected graph since relation between words is not relevant in this case. Every word from list of relevant words extracted in previos step is one node in the graph. Edges correspond to unique bigrams. More precisely, if word w1 immediately preceded word w2 in the processed document, then an edge w1 − w2 was added to the network. If the same relation occurs again, the edge weight is increased.For more details, please see the document used as reference: [Keyword and Keyphrase Extraction Using Centrality Measures on Collocation Networks](http://arxiv.org/pdf/1401.6571v1.pdf).

After creation of graph, nodes should be ranked. To rank nodes, different centrality measures can be used. In this project, degree centrality method is found as most relevant. This method uses number of edges incident to a node to calculate rank of the node. After calculation of node rank, that value is divided by number of nodes. With finishing this step, we have degrees for all relevant words. In the next step, degrees are sorted by using Comparator interface. Once we have sorted map of words and corresponding degrees, we set first N words as keywords for given project.

Similarities between projects are calculated based on those keywords by using the Cosine similarity metric. In order to calculate Cosine similarity for two projects, the prerequisite is to calculate TF-IDF (Term Frequency/Inverse Document Frequency) metric for each project (i.e., its textual representation). This metric must be calculated for every keyword extracted from project's decsription and title. By using this [document](http://www.site.uottawa.ca/~diana/csi4107/cosine_tf_idf_example.pdf) as reference, next steps are followed:

- We calculate the term frequency for all the words as number of occurences in each particular project
- In order to calculate IDF we count number of projects in which particular word appears; then we use following formula to calculate idf:  idf = 1 + Math.log(projects.size() / occurrences);

- When computing the tf-idf values for the words we divide the frequency by the maximum frequency  (number of projects) and multiply with the idf values; the result is tf/idf metric for each keyword per project (matrix). 

- Calculate Length for each project by using formula  

When TF-IDF is calculated, it is possible to calculate Cosine Similarity. Formula used for calculating Cosine Similarity is:
Cosine Similarity (d1, d2) =  Dot product(d1, d2) / ||d1|| * ||d2||

For programming language and operating system, comparison is based on "matching". It is only checked if the programming language of one project matches the programming language of another. The same applies to the operating system. 

At the end, all similarities - between description, programming language and operating system - are summarized and the final similarity value is calculated. Once similarities are calculated between the given project and all other projects from the database, they are stored in a local CSV file (similarities.csv). Every time the program is run, it checks if similarities for the given project are already calculated; if so, the program only reads them from the CSV file, extract TOP 5 projects and displays recommendations to the user.

# 2. Domain model

Domain model is created and it is depicted in Picture 1.

![Picture 1 - Domain model](rdf.jpg)

Class *Project* contains basic information about a project. Those are: name, decription, download-page, homepage, seeAlso, programing-languages, operating-systems, license. It has reference to its maintainer (class *Person*), its category (class *Category*), and its release (class *Version*).

Class *Person* contains maintainers's name and URI seeAlso.

Class *Category* contains name and URI seeAlso.

Class *Version* contains basic information of project release such as name, date when it is created, width, revision and description.

# 3. The solution

The application collects metadata about software projects from the websites [Freecode](http://freecode.com/) and [SourceForge](http://sourceforge.net). The data is extracted by the crawler and used for recommendation process.
The application allows user to interact through console.

- User enters username

- User's preferences (currently user preferences are reduced to the software project the user has recently expressed interest in) are read from local file (userPref.csv)

- Check if similarities are already calculated for the given project;

    - If yes, show TOP 5 recommendations;
    
    - Otherwise, start the calculation service;

- Store the computed similarities in the local CSV file;

- Display recommendations to the user

# 4. Technical realisation

This application is written in programming language Java as a console application.

POS model and StopAnalyzer from the OpenNLP tools are used for tagging words in description and removing stop-words. It provides couple of methods for this purpose, and StopAnalyzer provides list of English stop-words.

[Jung](http://jung.sourceforge.net/ ) library is used for graph creation. In this case, undirected sparse graph is used.

[TF/IDF](http://www.site.uottawa.ca/~diana/csi4107/cosine_tf_idf_example.pdf) and [Cosine Similarity](https://github.com/xiejuncs/cross-document-coreference-resolution/blob/master/util/CosineSimilarity.java) metrics are used for calculations.

# 5. Acknowledgements

This application has been developed as a part of the project assignment for the course [Intelligent Systems](http://ai.fon.bg.ac.rs/osnovne/inteligentni-sistemi/) at the Faculty of Organization Sciences, University of Belgrade, Serbia.


# 6. Licence

This software is licensed under the MIT License.

The MIT License (MIT)

Copyright (c) 2015 Bojana Lecic - bojanalcc@gmail.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

