Simple groovy-application that illustrates the group-related dependencies from the project in a given directory. An image will be rendered from a generated GraphViz dot file.

Technical this program searches all pom.xml in all subdirectories of a specified directory (eg. eclipse workspace), and generates a dot-file with the projects dependencies. Some coloring and forms are assigned (Dropwizard-services are green, libaries and others red), please modify as needed.

Requirements: Groovy and GraphViz installed in the PATH

### Usage

    ./graph.sh <PROJECT_DIR> <OUTPUT_DIR> <RELATED_GROUP_IDS> [FILTERED_PROJECTS] 

### Example

    ./graph.sh $HOME/workspace $PWD 'de.galan'

### Output

![Example output](https://raw.githubusercontent.com/galan/maven-graph/master/images/dependencies-01.png)

(projectnames obfuscated)

