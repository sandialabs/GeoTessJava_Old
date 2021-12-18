#!/bin/bash 

# the location of the geotess jar file on the user's system.
jarfile=/Users/$USER/github/GeoTessJava/target/geotess-2.6.8-jar-with-dependencies.jar

# the java command to execute geotessbuilder application.
java -cp $jarfile gov.sandia.geotessbuilder.GeoTessBuilderMain $@

# Users may want to add the following line to their .bash_profile file
# PATH=$PATH:<path to directory where this file resides>
