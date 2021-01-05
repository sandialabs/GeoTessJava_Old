#!/bin/bash

# This script creates bash script in the current directory
# called 'geotess'.  It will call the geotess.jar 
# file as a runnable jar.
# 
# The script also prints to screen recommended addition to 
# the user's .cshrc or .bash_profile that will make the new
# executable available via the PATH.  No changes to the user's
# environment are actually made.

echo "Creating executable script file geotess that launches GeoTessExplorer"
echo "#!/bin/bash" > geotess
echo "#" >> geotess
echo "# The substring '-Xmx????m' in the following execution" >> geotess
echo "# command specifies the amount of memory to make available" >> geotess
echo "# to the application, in megabytes." >> geotess
echo "#" >> geotess
echo "java -Xmx1400m -jar `pwd`/geotess.jar \$*" >> geotess
chmod 777 geotess
echo "Recommended modification to environment:"

if [ `uname -s` = Darwin ]; then
	echo "export PATH=`pwd`:\$PATH"
else
	echo "set path=( `pwd` \$path )"
fi
