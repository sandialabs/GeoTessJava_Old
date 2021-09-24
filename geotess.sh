#!/bin/bash 

# the location of the geotess jar file on the user's system.
jarfile=/Users/$USER/github/GeoTessJava/target/geotess-2.6.6-jar-with-dependencies.jar

# the java command to execute geotess application.
java -jar $jarfile $@

# Users may want to add the following line to their .bash_profile file
# PATH=$PATH:<path to directory where this file resides>
