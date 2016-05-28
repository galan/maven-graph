package graph

/*
Note about this little program:
This program searches all pom.xml in all subdirectories of a specified directory (eg. eclipse workspace), and generates a dot with the
projects dependencies. Services green, libaries and others red.
 */

class GraphMaven {

	public static main(String[] args) {
		File projectDirectory = new File(args[0])
		File outputDirectory = new File(args[1])
		String[] RELATED_GROUP_IDS = args[2].tokenize(",")
		String[] FILTERED_PROJECTS = args.size() > 3 ? args[3]?.tokenize(",") : []


		def poms = []

		// collect poms
		projectDirectory.eachFile {
			if (it.isDirectory()) {
				def filePom = new File(it, "pom.xml");
				if (filePom.exists()) {
					poms << filePom
				}
			}
		}

		def assocs = []
		for (it in poms) {
			def project = new XmlSlurper().parse(it)
			if ((project?.parent?.groupId in RELATED_GROUP_IDS) && !(project?.artifactId in FILTERED_PROJECTS)) {
				println(it)
				boolean isService = false
				// check service (dropwizard indicator)
				project?.build?.plugins?.plugin?.each {
					if (it?.artifactId.text() == 'maven-shade-plugin') {
						isService = true
					}
				}

				def projectArtifact = project.artifactId.text()
				//def projectPackaging = project?.packaging.text() ?: 'jar'
				def color = isService ? "[shape=box,color=green]" : "[shape=ellipse,color=red]"
				//def version = project.version.text() ?: 'jar'
				assocs << "    \"${projectArtifact}\" ${color}"

				// iterate thru dependencies
				project?.dependencies?.dependency.findAll {it.groupId.text() in RELATED_GROUP_IDS}.each {
					def depArtifact = it.artifactId.text()
					//def depVersion = it?.version.text() ?: 'jar'
					def dep = "    \"${projectArtifact}\"  -> \"${depArtifact}\""
					assocs << dep
				}
			}
			println ''
		}


		String today = new Date().format("yyyy-MM-dd")
		def dotFile = new File(outputDirectory, "graph-${today}.dot");
		if (dotFile.exists()) {
			dotFile.delete();
		}

		dotFile << 'digraph dependencies {\n'
		assocs.each {
			dotFile << it + ';\n'
		}
		dotFile << '}'
	}

}
