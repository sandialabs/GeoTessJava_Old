package gov.sandia.geotess;

import java.util.Collection;
import java.util.LinkedHashSet;

import gov.sandia.gmp.util.globals.Utils;

public class GeoTessJava {

	static public String getVersion() 	{ 
		return Utils.getVersion("geo-tess-java");
	}

	static public Collection<String> getDependencies()
	{
		Collection<String> dependencies = new LinkedHashSet<>();
		addDependencies(dependencies);
		return dependencies;
	}
	
	static public void addDependencies(Collection<String> dependencies)
	{
		dependencies.add("GeoTessJava "+getVersion());
		Utils.addDependencies(dependencies);
	}
	
}
