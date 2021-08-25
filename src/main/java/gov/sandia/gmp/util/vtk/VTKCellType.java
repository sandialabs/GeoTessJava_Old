package gov.sandia.gmp.util.vtk;

/**
 * Enumeration of all the vtk cell types supported by the VTK format.
 * See http://www.vtk.org/wp-content/uploads/2015/04/file-formats.pdf 
 * figures 2 and 3 for descriptions.
 * <ul>Each element is capable returning two properties:
 * <li>getNPoints() returns the number of points required by the cell type, 
 * or -1 if the cell type requires an arbitrary number of points.
 * <li>getIndex() returns the vtk format index number as defined in 
 * http://www.vtk.org/wp-content/uploads/2015/04/file-formats.pdf 
 * figures 2 and 3 
 * </ul>
 * @author sandy
 *
 */
public enum VTKCellType {
	VTK_VERTEX(1,1),
	VTK_POLY_VERTEX(2, -1),
	VTK_LINE(3, 2),
	VTK_POLY_LINE(4, -1),
	VTK_TRIANGLE(5, 3),
	VTK_TRIANGLE_STRIP(6, -1),
	VTK_POLYGON(7, -1),
	VTK_PIXEL(8, 4),
	VTK_QUAD(9, 4),
	VTK_TETRA(10, 4),
	VTK_VOXEL(11, 8),
	VTK_HEXAHEDRON(12, 8),
	VTK_WEDGE(13, 6),
	VTK_PYRAMID(14, 5),
	VTK_QUADRATIC_EDGE(21, 3),
	VTK_QUADRATIC_TRIANGLE(22, 6),
	VTK_QUADRATIC_QUAD(23, 8),
	VTK_QUADRATIC_TETRA(24, 10),
	VTK_QUADRATIC_HEXAHEDRON(25, 20);
	
	/**
	 * the index defined in VTK format document, figure x.
	 */
	final private int vtkIndex;
	
	/**
	 * Number of points required by this vtk type. 
	 * -1 means the type can support an arbitrary number of points.
	 */
	final private int nPoints;
	
	/**
	 * Constructor
	 * @param index the index defined in VTK format document, figure x.
	 * @param nPoints Number of points required by this vtk type. 
	 * -1 means the type can support an arbitrary number of points.
	 */
	VTKCellType(int index, int nPoints) { this.vtkIndex=index; this.nPoints=nPoints; }
	
	/**
	 * retrieve the index defined in VTK format document, figure x.
	 * @return the index defined in VTK format document, figure x.
	 */
	public int getIndex() {return vtkIndex;}
	
	/**
	 * Retrieve the number of points required by this vtk type. 
	 * -1 means the type can support an arbitrary number of points.
	 * @return the number of points required by this vtk type. 
	 * -1 means the type can support an arbitrary number of points.
	 */
	public int getNPoints() {return nPoints;}
}
