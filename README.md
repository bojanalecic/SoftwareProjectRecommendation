# SoftwareProjectRecommendation
============
# 1. About the project
The idea of this project is recommendation of software projects based on user's past actions and preferences. The assumption is that user's preferences are already known. 
The database - RDF file is already filled by extracting data about software projects from websites containing software application catalogs. Initially, project extracted data from the [Freecode](http://freecode.com/) website, and now it is expanded with data from [SourceForge](http://sourceforge.net)  website.

When user enters username, prefrences are read from local file, similarity calculation engine calculates top5 most similar projects and recommend user to see those projects.
There are three criterias for project comparison: description of project, programming language in which project is developed and operating system. It is possible to configure how much each criteria affects similarity.

Description of project firstly must be formatted and processed to be usefull for different calculation. The idea is to extract particular number of keywords from description. That number is also configurable. Process of keywords extraction follows those steps:
-Lower case all words in description
-Remove all words shoter than 3 characters
-Tag words to avoid using of verbs, adverbs, articles etc; only nouns and adjectives are useful
-Remove stopwords

When this process is finished, Graph is created from relevant words. For this purpose, [jung](http://jung.sourceforge.net/ ) library is used. For this purpose we use undirected graph since relation between words is not relevant in this case.

After creation of graph, nodes should be ranked. To rank nodes, different centrality measures can be used. In this project, degree centrality methos is used. After calculation of degrees, keywords are extracted.
Similarities between projects are calculated based on those keywords by using the Cosine similarity metric. In order to calculate Cosine similarity for two projects, the preriquesite is to calculate TF-IDF (Term Frequency/Inverse Document Frequency) metric. This must be calculated for every keyword from project decsription. When TF-IDF is calculated for one project for one keyweord, it is possible to calculate Cosine Similarity.

For programming language and operating system, method based on "match" is used. It only check if programming languag of one project matches the programming language of another. 

At the end, all similarities - between description, programming language and operating system are sumerized and final value is calculated. Once similarity is calculated between given project and all other projects from database, it is stored in local CSV file. Every time, engine is run, it checks if similarities are already calculated; if so, program only reads from CSV file, extract TOP5 projects and displays recommendations to user.

# 2. Domain model

Domain model is created and it is depicted in Picture 1.

![Picture 1 - Domain model](rdf.jpg)

Class *Project* contains basic information about a project. Those are: name, decription, download-page, homepage, seeAlso, programing-languages, operating-systems, license. It has reference to its maintainer (class *Person*), its category (class *Category*), and its release (class *Version*).

Class *Person* contains maintainers's name and URI seeAlso.

Class *Category* contains name and URI seeAlso.

Class *Version* contains basic information of project release such as name, date when it is created, width, revision and description.


