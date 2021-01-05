//- ****************************************************************************
//- 
//- Copyright 2009 Sandia Corporation. Under the terms of Contract
//- DE-AC04-94AL85000 with Sandia Corporation, the U.S. Government
//- retains certain rights in this software.
//- 
//- BSD Open Source License.
//- All rights reserved.
//- 
//- Redistribution and use in source and binary forms, with or without
//- modification, are permitted provided that the following conditions are met:
//- 
//-    * Redistributions of source code must retain the above copyright notice,
//-      this list of conditions and the following disclaimer.
//-    * Redistributions in binary form must reproduce the above copyright
//-      notice, this list of conditions and the following disclaimer in the
//-      documentation and/or other materials provided with the distribution.
//-    * Neither the name of Sandia National Laboratories nor the names of its
//-      contributors may be used to endorse or promote products derived from
//-      this software without specific prior written permission.
//- 
//- THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
//- AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//- IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//- ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
//- LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
//- CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
//- SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
//- INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
//- CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
//- ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
//- POSSIBILITY OF SUCH DAMAGE.
//-
//- ****************************************************************************

package gov.sandia.geotess.apps;

import gov.sandia.geotess.Data;
import gov.sandia.geotess.GeoTessMetaData;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.GeoTessUtils;
import gov.sandia.gmp.util.globals.DataType;
import gov.sandia.gmp.util.numerical.polygon.Polygon;

import java.io.File;
import java.util.Date;

/**
 * An example of how to generate a GeoTessModel and populate it with data.
 * At every node in the 4 degree tessellation:
 * <ul>
 * <li>populate a new model with some simple data. The data consists of the
 * distance in radians from every grid node to the geographic location of
 * seismic station ANMO located at Latitude: 34.9462N Longitude: 106.4567W.
 * <li>modify the data by converting it from radians to degrees.
 * <li>interpolate a value from the grid and print the result to the screen.
 * </ul>
 * <p>
 * 
 * @author sballar
 * 
 */
public class PopulateRayDensity
{
	/**
	 * An example of how to generate a GeoTessModel and populate it with data.
	 * At every node in the 4 degree tessellation:
	 * <ul>
	 * <li>populate a new model with some simple data. The data consists of the
	 * distance in radians from every grid node to the geographic location of
	 * seismic station ANMO located at Latitude: 34.9462N Longitude: 106.4567W.
	 * <li>modify the data by converting it from radians to degrees.
	 * <li>interpolate a value from the grid and print the result to the screen.
	 * </ul>
	 * <p>
	 * 
	 * @param args path to file geotess_grid_04000.geotess
	 */
	public static void main(String[] args)
	{
		try
		{
			System.out.println("Start simple example");
			System.out.println();

			// Create a MetaData object in which we can specify information
			// needed for model contruction.
			GeoTessMetaData metaData = new GeoTessMetaData();

			// Specify a description of the model. This information is not
			// processed in any way by GeoTess. It is carried around for
			// information purposes.
			metaData.setDescription(String
					.format("Simple example of a GeoTess model,%n"
							+ "storing the ray density from a tomography calculation%n"));

			// Specify a list of layer names. A model could have many layers,
			// e.g., ("core", "mantle", "crust"), specified in order of
			// increasing radius. This simple example has only one layer.
			metaData.setLayerNames("surface");

			// Set layerID equal to the index of the one-and-only layer 
			// in this model.
			int layerID = 0;

			// specify the names of the attributes and the units of the
			// attributes in two String arrays. This model only includes
			// one attribute.
			// If this model had two attributes, they would be specified 
			// like this: setAttributes("Distance; Depth", "degrees; km");
			metaData.setAttributes("RAYDENSITY", "");

			// specify the DataType for the data. All attributes, in all
			// profiles, will have the same data type.
			metaData.setDataType(DataType.FLOAT);

			// specify the name of the software that is going to generate
			// the model.  This gets stored in the model for future reference.
			metaData.setModelSoftwareVersion("PopulateRayDensity 1.0.0");

			// specify the date when the model was generated.  This gets 
			// stored in the model for future reference.
			metaData.setModelGenerationDate(new Date().toString());

			// specify the path to the file containing the grid to be used for
			// this test.  This information was passed in as a command line
			// argument.  Grids were included in the software delivery and
			// are available from the GeoTess website.
			String gridFile = "eurasia_grid.geotess";

			// call a GeoTessModel constructor to build the model. This will
			// load the grid, and initialize all the data structures to null.
			// To be useful, we will have to populate the data structures.
			GeoTessModel model = new GeoTessModel(gridFile, metaData);

			Polygon eurasia_polygon = new Polygon(new File("eurasia_polygon.kmz"));

			// Each grid vertex will be assigned a single data value consisting
			// of the ray density.  As a surrogate for ray density i will use
			// distance in degrees from Mount Everest.
			double[] mt_everest = model.getEarthShape().getVectorDegrees(27.988, 86.925);

			// generate some data and store it in the model. The data consists
			// of the angular distance in degrees from each vertex of the model
			// grid to station ANMO near Albuquerque, NM, USA.
			for (int vtx = 0; vtx < model.getGrid().getNVertices(); ++vtx)
			{
				// retrieve the unit vector corresponding to the i'th vertex of
				// the grid.
				double[] vertex = model.getGrid().getVertex(vtx);

				float raydensity = Float.NaN;

				// if current vertex is inside the polygon, then set ray density 
				// to distance from mount everest.
				if (eurasia_polygon.contains(vertex))
					raydensity = (float) GeoTessUtils.angleDegrees(mt_everest, vertex);

				// Construct a new Data object that holds a single value of 
				// type float. Data.getData() can be called with multiple values
				// (all of the same type), or an array of values.  In this 
				// very simple example, there is only one value: distance.
				Data data = Data.getDataFloat(raydensity);

				// associate the Data object with the specified vertex of the model.  
				// This instance of setProfile always creates a ProfileSurface object.
				model.setProfile(vtx, data);

			}

			// At this point, we have a fully functional GeoTessModel object
			// that we can work with.

			// print a bunch of information about the model to the screen.
			System.out.println(model.toString());
			
			model.writeModel("eurasia_raydensity_model.geotess");

			System.out.printf("%nDone.%n%n");

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

}
