package gov.sandia.geotessbuilder;

import java.util.Collection;
import java.util.LinkedHashSet;

import gov.sandia.geotess.GeoTessJava;
import gov.sandia.gmp.util.globals.Utils;

public class GeoTessBuilder {

	static public String getVersion() 	{ 
		return Utils.getVersion("geo-tess-builder");
	}

	static public Collection<String> getDependencies()
	{
		Collection<String> dependencies = new LinkedHashSet<>();
		addDependencies(dependencies);
		return dependencies;
	}
	
	static public void addDependencies(Collection<String> dependencies)
	{
		dependencies.add("GeoTessBuilder "+getVersion());
		GeoTessJava.addDependencies(dependencies);
	}
	
}
