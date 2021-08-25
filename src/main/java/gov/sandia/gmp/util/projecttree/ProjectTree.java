package gov.sandia.gmp.util.projecttree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;  

/**
 * A ProjectTree starts from a single project in a maven/git collection of projects
 * and builds a project tree that includes all the dependencies of the root project.  
 * It accomplishes this by reading the root project's pom file and creating a root ProjectNode.  
 * Each ProjectNode records the node's groupId, artifactId (project name), and version.  
 * In addition, each node maintains a Collection of other ProjectNodes upon with the node depends.
 * Pom files are loaded recursively to read in the entire dependency tree for the root project.
 * 
 * @author sballar
 *
 */
public class ProjectTree extends ProjectNode {

	/**
	 * Main method that demonstrates initializing a ProjectTree, accessing the 
	 * ProjectNodes in the tree, and printing out version files for every node of the tree.
	 * @param args the full file path to a project in the user's git directory
	 * and the output directory to receive the version files.
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 0)
			{
				System.out.println("\nMust specify 2 or 3 command line arguments:");
				System.out.println("0 - A directory containing a pom.xml file which will be the root of the project tree.");
				System.out.println("1 - The name of a directory where the project.version files will be written.");
				System.out.println("    Default is 'screen'.");
				System.out.println("2 - GroupId filter. A ';' delimited list of groupIds to include in the output. ");
				System.out.println("    Default is 'all'");
				System.exit(0);
			}

			File pomFile = new File(args[0]);

			File outputDir = null;
			if (args.length > 1 && !args[1].equalsIgnoreCase("screen"))
			{ outputDir = new File(args[1]); outputDir.mkdirs(); }

			Set<String> filter = new HashSet<>();
			if (args.length == 3 && !args[2].equalsIgnoreCase("all"))
				for (String f : args[2].split(";"))
					filter.add(f.trim());


			// if users specified the project directory instead of the pom file in the 
			// directory, add the name of the pom file.
			if (pomFile.isDirectory())
				pomFile = new File(pomFile, "pom.xml");
			
			if (!pomFile.exists())
				throw new Exception(pomFile.getAbsolutePath()+" does not exist.");

			ProjectTree tree = new ProjectTree(pomFile.getCanonicalPath());

			Set<ProjectNode> projectSet = tree.getSet(true);

			String timestamp = LocalDateTime.now().format(
					DateTimeFormatter.ofPattern("yyyyMMdd-HHmm"));

			for (ProjectNode node : projectSet)
				if (!node.getVersion().contains("${"))
					if (filter.isEmpty() || filter.contains(node.getGroupId()))
					{
						if (outputDir == null)
							System.out.println(node.getVersionFile(timestamp));
						else
						{
							File output = new File(outputDir, node.getProjectId()+".version");				
							BufferedWriter bw = new BufferedWriter(new FileWriter(output));
							bw.write(node.getVersionFile(timestamp));
							bw.close();
						}
					}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor.
	 * @param projectDir the path to the project directory in the user's git directory
	 * that will represent the root of the dependency tree.
	 * @throws Exception
	 */
	public ProjectTree(String projectSource) throws Exception {
		// build the ProjectNode for root, then copy it into this.
		this.copy(getDependents(projectSource));
	}

	public void writeVersionFiles(File outputDir)
	{

	}

	/**
	 * Retrieve a String representation of the entire tree, including all dependencies.  
	 * There will be many duplicate nodes represented.
	 * @return
	 */
	public String getString() {
		StringBuffer buffer = new StringBuffer();
		toString(buffer, "");
		return buffer.toString();
	}

	public String getCode() {
		StringBuffer buffer = new StringBuffer();
		code(buffer);
		return buffer.toString();
	}

	private ProjectNode getDependents(String projectSource) throws Exception
	{
		InputStream stream = getInputStream(projectSource);
		if (stream == null)
			return null;

		ProjectNode projectNode = parsePom(stream);

		for (ProjectNode dependent : projectNode.dependents)
		{
			ProjectNode d = getDependents(projectSource.replace(projectNode.projectId, dependent.projectId));
			if (d != null)
				dependent.dependents.addAll(d.dependents);	
		}
		return projectNode;  
	}

	/**
	 * Extract information for ProjectNode from a pom file.
	 * @param input a stream pointing to the contents of a pom file.
	 * @return
	 * @throws Exception
	 */
	private ProjectNode parsePom(InputStream input) throws Exception
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		DocumentBuilder db = dbf.newDocumentBuilder();  
		Document doc = db.parse(input);  
		doc.getDocumentElement().normalize();  

		// get a reference to the root element, which is 'project'
		Element root = (Element)doc.getElementsByTagName("project").item(0); 

		// get the groupId, artifactId and version of this pom file and 
		// populate a new ProjectNode object, except for the dependents.
		ProjectNode projectNode = new ProjectNode(
				((Element)root.getElementsByTagName("groupId").item(0)).getTextContent(),
				((Element)root.getElementsByTagName("artifactId").item(0)).getTextContent(),
				((Element)root.getElementsByTagName("version").item(0)).getTextContent()
				);

		// get a reference to the dependencies node.
		NodeList dependencies = root.getElementsByTagName("dependencies");

		// if there are any dependencies, iterate over all of them
		if (dependencies.getLength() > 0)
		{
			// get a list of all the <dependency> nodes and iterate over them
			NodeList dependencyList = ((Element)dependencies.item(0)).getElementsByTagName("dependency");
			for (int itr = 0; itr < dependencyList.getLength(); itr++)   
			{  
				Node dependency = dependencyList.item(itr);  
				if (dependency.getNodeType() == Node.ELEMENT_NODE)   
				{  
					Element eElement = (Element) dependency; 

					ProjectNode dependent = new ProjectNode(
							((Element)eElement.getElementsByTagName("groupId").item(0)).getTextContent(),
							((Element)eElement.getElementsByTagName("artifactId").item(0)).getTextContent(),
							((Element)eElement.getElementsByTagName("version").item(0)).getTextContent()
							);
					projectNode.dependents.add(dependent);				
				}  
			}
		}
		return projectNode;  
	}

	/**
	 * Given the full path to a pom file, return an inputStream for the file.
	 * If the file does not exist, or anything else goes wrong, return null.
	 * @param pomFile
	 * @return
	 */
	private InputStream getInputStream(String pomFile) 
	{
		try {
			return new FileInputStream(new File(pomFile));
		} catch (Exception e) {
			return null;
		}
	}

}
