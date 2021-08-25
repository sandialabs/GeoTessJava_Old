package gov.sandia.gmp.util.vtk;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This class facilitates writing a VTK file.  It is assumed the caller has a set of points
 * (each point is a 3-element vector).  The caller also supplies a list of VTKCell
 * objects, each of which specifies a vtk cell that includes the cell type (triangle, quad, etc.)
 * and the indices of the points that define the cell.  The caller can optionally specify a bunch of 
 * attribute data to attach to each point.  Given all this information, this class will write the 
 * information to an output file in vtk format suitable for viewing with ParaView.
 * <p>
 * Here a code snippet that writes some data from GeoTessModel to a vtk file:
 * <p>
 * <br>GeoTessModel model = new GeoTessModel("C:\\Users\\sballar\\git\\geo-tess-java\\resources\\permanent_files\\crust20.geotess");
 * <br>System.out.println(model);
 * <br>
 * <br>GeoTessGrid grid = model.getGrid();
 * <br>
 * <br>ArrayList<VTKCell> cells = new ArrayList<VTKCell>(10000);
 * <br>int tessId= 0;
 * <br>int level = grid.getLastLevel(tessId);
 * <br>for (int t=grid.getFirstTriangle(tessId, level); t <= grid.getLastTriangle(tessId, level); ++t)
 * <br>    cells.add(new VTKCell(VTKCellType.VTK_TRIANGLE, grid.getTriangleVertexIndexes(t)));
 * <br>
 * <br>float[][] data = new float[grid.getNVertices()][model.getNAttributes()];
 * <br>for (int i=0; i<grid.getNVertices(); ++i)
 * <br>	for (int j=0; j<model.getNAttributes(); ++j)
 * <br>		data[i][j] = model.getProfile(i, 0).getDataTop().getFloat(j);
 * <br>
 * <br>VTKDataSet.write(new File("C:\\Users\\sballar\\Desktop\\deleteme.vtk"), grid.getVertices(), cells, 
 * <br>		model.getMetaData().getAttributeNames(), data);
 * <br>
 * @author sballar
 */
public class VTKDataSet 
{
	/**
	 * If set to true, minimal information about the dataset will be output to screen.
	 */
	static public boolean DEBUG = false;
	/**
	 * Write a vtk dataset to an output file.
	 * @param outputFile name of the file to receive the vtk output
	 * @param points the list of 3-component points to be written to the output file
	 * @param cells a List of VTKCell objects that define the indices of the points that comprise each cell.
	 * @param attributeNames (optional - may be null) the names of the data attributes
	 * @param attributes (optional - may be null) the 2D array of attribute values.  
	 * The first dimension must be equal to the  size of the points array.  
	 * The second dimension must be equal to the number of attributeNames.
	 * @throws IOException
	 */
	static public void write(File outputFile, List<double[]> points, Collection<VTKCell> cells,
			List<String> attributeNames, List<float[]> attributes) throws IOException
	{
		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));

		writeBytes(output,String.format("# vtk DataFile Version 2.0%n"));
		writeBytes(output,String.format("VTK DataSet%n"));
		writeBytes(output,String.format("BINARY%n"));
		writeBytes(output,String.format("DATASET UNSTRUCTURED_GRID%n"));
		writeBytes(output,String.format("POINTS %d double%n", points.size()));

		for (double[] point : points)
		{
			output.writeDouble(point[0]);
			output.writeDouble(point[1]);
			output.writeDouble(point[2]);
		}

		int totalPoints = 0;
		for (VTKCell cell : cells)
			totalPoints += cell.size();

		writeBytes(output,String.format("CELLS %d %d%n", cells.size(), totalPoints+cells.size()));
		for (VTKCell cell : cells) cell.writeCell(output);

		writeBytes(output,String.format("CELL_TYPES %d%n", cells.size()));
		for (VTKCell cell : cells) cell.writeCellType(output);

		if (attributeNames != null)
		{
			writeBytes(output,String.format("POINT_DATA %d%n", points.size()));

			for (int a = 0; a < attributeNames.size(); ++a)
			{
				writeBytes(output, String.format("SCALARS %s float 1%nLOOKUP_TABLE default%n",
						attributeNames.get(a).replaceAll(" ", "_")));

				for (int i = 0; i < attributes.size(); ++i)
					output.writeFloat(attributes.get(i)[a]);
				
			}
		}
		
		if (DEBUG) System.out.println();
		
		output.close();
	}
	
	/**
	 * Write a vtk dataset to an output file.
	 * @param outputFile name of the file to receive the vtk output
	 * @param points the array of 3-component points to be written to the output file
	 * @param cells a List of VTKCell objects that define the indices of the points that comprise each cell.
	 * @param attributeNames (optional - may be null) the names of the data attributes
	 * @param attributes (optional - may be null) the 2D array of attribute values.  
	 * The first dimension must be equal to the  size of the points array.  
	 * The second dimension must be equal to the number of attributeNames.
	 * @throws IOException
	 */
	public static void write(File outputFile, double[][] points, VTKCell[] cells, 
			String[] attributeNames, float[][] attributes) throws IOException 
	{ write(outputFile, Arrays.asList(points), Arrays.asList(cells), Arrays.asList(attributeNames), Arrays.asList(attributes)); }

	/**
	 * Write a vtk dataset to an output file.
	 * @param outputFile name of the file to receive the vtk output
	 * @param points the list of 3-component points to be written to the output file
	 * @param cells a List of VTKCell objects that define the indices of the points that comprise each cell.
	 * @throws IOException
	 */
	static public void write(File outputFile, List<double[]> points, Collection<VTKCell> cells) throws IOException
	{ write(outputFile, points, cells, null, null);	}

	/**
	 * Write a vtk dataset to an output file.
	 * @param outputFile name of the file to receive the vtk output
	 * @param points the array of 3-component points to be written to the output file
	 * @param cells a List of VTKCell objects that define the indices of the points that comprise each cell.
	 * @throws IOException
	 */
	public static void write(File outputFile, double[][] points, VTKCell[] cells) throws IOException 
	{ write(outputFile, Arrays.asList(points), Arrays.asList(cells), null, null); }


	private static void writeBytes(DataOutputStream output, String s) throws IOException
	{ if (DEBUG) System.out.print(s); output.writeBytes(s); }

}
