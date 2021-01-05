package gov.sandia.geotess;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import gov.sandia.geotess.GeoTessExplorer;
import gov.sandia.geotess.extensions.libcorr3d.LibCorr3DModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GeoTessExplorerTest
{
	// save reference to System.out so it can be restored at the end.
	static PrintStream old_out;

	// buffer to receive the bytes sent to System.out
	static ByteArrayOutputStream buffer;

	static private File vtkDirectory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		System.out.println("GeoTessExplorer");

		vtkDirectory = new File("vtkFiles");
		vtkDirectory.mkdir();

		// save reference to System.out so it can be restored at the end.
		old_out = System.out;

		buffer=new ByteArrayOutputStream();
		System.setOut(new PrintStream(buffer));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		// restore system.out
		System.setOut(old_out);

		vtkDirectory.delete();
	}

	@Before
	public void setUp() throws Exception
	{
		// before each test, clear the output buffer
		buffer.reset();
	}

	@After
	public void tearDown() throws Exception
	{
	}

	private void print(String s)
	{
		PrintStream current = System.out;
		System.setOut(old_out);
		System.out.print(s);
		System.setOut(current);
	}

	private void printExpected(String x)
	{
		Scanner s = new Scanner(x);
		StringBuffer buf = new StringBuffer("String expected = \n");
		buf.append("\"").append(s.nextLine()).append("\\n\"\n");
		while (s.hasNext())
			buf.append("+\"").append(s.nextLine()).append("\\n\"\n");
		s.close();
		buf.append(";\n");
		print(buf.toString());
	}

	private boolean compare(String expected, String actual) throws Exception
	{
		return compare(new Scanner(expected), new Scanner(actual));
	}

	private boolean compare(File expected, File actual) throws Exception
	{
		return compare(new Scanner(expected), new Scanner(actual));
	}

	private boolean compare(File expected) throws Exception
	{
		return compare(new Scanner(expected), new Scanner(buffer.toString()));
	}

	private boolean compare(Scanner expected, Scanner actual) throws Exception
	{
		int line = 0;
		String s1, s2;
		while (expected.hasNext() && actual.hasNext())
		{
			++line;
			s1 = expected.nextLine();
			s2 = actual.nextLine();
			if (!s1.equals(s2))
			{
				print(String.format("Difference in line %d%nexpected = %s%nactual   = %s%n",
						line, s1, s2));
				return false;
			}
		}

		if (expected.hasNext()) 
		{
			print("\n\nExpected has more lines:\n");
			while (expected.hasNext())
				print(expected.nextLine()+"\n");
			print("\n");
			return false;
		}
		
		if (actual.hasNext())
		{
			print("\n\nActual has more lines:\n");
			while (actual.hasNext())
				print(actual.nextLine()+"\n");
			print("\n");
			return false;
		}

		return true;
	}

    @Test
    public void testGetClassNameAscii() throws Exception
    {
        String[] args = new String[] { "getClassName", "src/test/resources/permanent_files/small_model.ascii", "."};

        new GeoTessExplorer().getClassName(args);

        //printExpected(buffer.toString());
        String expected = "GeoTessModel\n";
        
        assertTrue(compare(expected, buffer.toString()));

        buffer.reset();
    }

    @Test
    public void testGetClassNameBinary() throws Exception
    {
        String[] args = new String[] { "getClassName", "src/test/resources/permanent_files/crust20.geotess", "."};

        new GeoTessExplorer().getClassName(args);

        //printExpected(buffer.toString());
        String expected = "GeoTessModel\n";
        
        assertTrue(compare(expected, buffer.toString()));

        buffer.reset();
    }

    @Test
    public void testGetClassNameBinaryLibCorrOldFormat() throws Exception
    {
        String[] args = new String[] { "getClassName", "src/test/resources/permanent_files/libcorr3d_files/ASAR_P_TT_2005165_2286324", "."};

        //LibCorr3DModel m = new LibCorr3DModel("src/test/resources/permanent_files/ASAR_P_TT_2005165_2286324");
        
        new GeoTessExplorer().getClassName(args);

        //printExpected(buffer.toString());
        String expected = "LibCorr3DModel\n";
        
        assertTrue(compare(expected, buffer.toString()));

        buffer.reset();
    }

    @Test
    public void testGetClassNameLibCorrBinary() throws Exception
    {
        String[] args = new String[] { "getClassName", "src/test/resources/permanent_files/AAK_PKPbc_TT.geotess"};

//        LibCorr3DModel m = new LibCorr3DModel("src/test/resources/permanent_files/AAK_PKPbc_TT.geotess");
//        m.writeModel("src/test/resources/permanent_files/AAK_PKPbc_TT.ascii");
        
        new GeoTessExplorer().getClassName(args);

        //printExpected(buffer.toString());
        String expected = "LibCorr3DModel\n";
        
        assertTrue(compare(expected, buffer.toString()));

        buffer.reset();
    }

   
    @Test
    public void testGetClassNameLibCorrAscii() throws Exception
    {
        String[] args = new String[] { "getClassName", "src/test/resources/permanent_files/AAK_PKPbc_TT.ascii"};

        new GeoTessExplorer().getClassName(args);

        //printExpected(buffer.toString());
        String expected = "LibCorr3DModel\n";
        
        assertTrue(compare(expected, buffer.toString()));

        buffer.reset();
    }

    @Test
    public void testgetSupportedPhasesAscii() throws Exception
    {
        String[] args = new String[] { "getSupportedPhases", "src/test/resources/permanent_files/libcorr3d_files/java_file_format_2.ascii"};

        new GeoTessExplorer().getSupportedPhases(args);

        //printExpected(buffer.toString());
        String expected = "P, Pn\n";
        
        assertTrue(compare(expected, buffer.toString()));

        buffer.reset();
    }

    @Test
    public void testgetSupportedPhasesBinary() throws Exception
    {
        String[] args = new String[] { "getSupportedPhases", "src/test/resources/permanent_files/libcorr3d_files/java_file_format_2_PKPdf.geotess"};

        new GeoTessExplorer().getSupportedPhases(args);

        //printExpected(buffer.toString());
        String expected = "PKP, PKPdf\n";
        
        assertTrue(compare(expected, buffer.toString()));

        buffer.reset();
    }

    @Test
    public void testgetPhaseAscci() throws Exception
    {
        String[] args = new String[] { "getSupportedPhases", "src/test/resources/permanent_files/libcorr3d_files/java_file_format_2_PKPdf.geotess"};

        new GeoTessExplorer().getPhase(args);

        //printExpected(buffer.toString());
        String expected = "PKPdf\n";
        
        assertTrue(compare(expected, buffer.toString()));

        buffer.reset();
    }

    @Test
    public void testgetPhaseBinary() throws Exception
    {
        String[] args = new String[] { "getSupportedPhases", "src/test/resources/permanent_files/AAK_PKPbc_TT.geotess"};

        new GeoTessExplorer().getPhase(args);

        //printExpected(buffer.toString());
        String expected = "PKPbc\n";
        
        assertTrue(compare(expected, buffer.toString()));

        buffer.reset();
    }

    @Test
    public void testgetSupportedPhasesAscci2() throws Exception
    {
        String[] args = new String[] { "getSupportedPhases", "src/test/resources/permanent_files/libcorr3d_files/cpp_file_format_2.ascii"};

        new GeoTessExplorer().getSupportedPhases(args);

        //printExpected(buffer.toString());
        String expected = "P, Pn\n";
        
        assertTrue(compare(expected, buffer.toString()));

        buffer.reset();
    }

    @Test
    public void testgetSupportedPhasesBinary2() throws Exception
    {
        String[] args = new String[] { "getSupportedPhases", "src/test/resources/permanent_files/libcorr3d_files/cpp_file_format_2.geotess"};
        
//        LibCorr3DModel m = new LibCorr3DModel("/Users/sballar/git/geo-tess-java/src/test/resources/permanent_files/ASAR_Pmantle_TT.geotess");
//        m.writeModel("src/test/resources/permanent_files/ASAR_Pmantle_TT.ascii");

        new GeoTessExplorer().getSupportedPhases(args);

        //printExpected(buffer.toString());
        String expected = "P, Pn\n";
        
        assertTrue(compare(expected, buffer.toString()));

        buffer.reset();
    }

    @Test
    public void testgetPhaseAscci2() throws Exception
    {
        String[] args = new String[] { "getSupportedPhases", "src/test/resources/permanent_files/libcorr3d_files/java_file_format_2.geotess"};

        new GeoTessExplorer().getPhase(args);

        //printExpected(buffer.toString());
        String expected = "Pmantle\n";
        
        assertTrue(compare(expected, buffer.toString()));

        buffer.reset();
    }

    @Test
    public void testgetPhaseBinary2() throws Exception
    {
        String[] args = new String[] { "getSupportedPhases", "src/test/resources/permanent_files/libcorr3d_files/java_file_format_2_PKPdf.geotess"};

        new GeoTessExplorer().getPhase(args);

        //printExpected(buffer.toString());
        String expected = "PKPdf\n";
        
        assertTrue(compare(expected, buffer.toString()));

        buffer.reset();
    }

	@Test
	public void testExtractActiveNodes() throws Exception
	{
		// extract active node geometry and all data values.  Output is written to 
		// System.out which has been redirected to buffer.
		String[] args = new String[] { "extractActiveNodes", "src/test/resources/permanent_files/small_model.ascii", ".",
				"all",  "false",  "null"};
		new GeoTessExplorer().extractActiveNodes(args);
		//print(buffer.toString());
		// compare buffer contents to expected values
		assertTrue(compare(new File("src/test/resources/permanent_files/ActiveNodes/small_model_active_nodes_data.txt")));	
		buffer.reset();
		
		// extract only the geometry, no data
		args = new String[] { "extractActiveNodes", "src/test/resources/permanent_files/small_model.ascii", ".",
				"none",  "false",  "null"};
		new GeoTessExplorer().extractActiveNodes(args);
		//print(buffer.toString());
		// compare buffer contents to expected values
		assertTrue(compare(new File("src/test/resources/permanent_files/ActiveNodes/small_model_active_nodes_nodata.txt")));	
		buffer.reset();
		
		// replace all the attribute values in small_model.ascii with NaNs.  Write the new
		// GeoTessModel to small_model_modified.ascii
		args = new String[] { "replaceAttributeValues", "src/test/resources/permanent_files/small_model.ascii", ".", "null",
				"src/test/resources/permanent_files/ActiveNodes/small_model_active_nodes_nan.txt",  
				"src/test/resources/permanent_files/ActiveNodes/small_model_modified.ascii"};
		new GeoTessExplorer().replaceAttributeValues(args);
		// this generates no output.
		assertTrue(buffer.toString().isEmpty());
		buffer.reset();
		
		// extract the geometry and all the data values from the new model.  The data should all
		// be NaN.
		args = new String[] { "extractActiveNodes", "src/test/resources/permanent_files/ActiveNodes/small_model_modified.ascii", ".",
				"all",  "false",  "null"};
		new GeoTessExplorer().extractActiveNodes(args);
		//print(buffer.toString());
		// compare buffer contents to expected values
		assertTrue(compare(new File("src/test/resources/permanent_files/ActiveNodes/small_model_active_nodes_nan.txt")));	
		buffer.reset();
		
		// delete the new GeoTessModel that got created.
		new File("src/test/resources/permanent_files/ActiveNodes/small_model_modified.ascii").delete();
	}

	@Test
	public void testTranslatePolygon() throws Exception
	{
		String[] args = new String[] {
				"translatePolygon", "src/test/resources/permanent_files/big_S.kmz", "deleteme.ascii"
		};
		new GeoTessExplorer().translatePolygon(args);

		File expected = new File("src/test/resources/permanent_files/big_S.ascii");

		File actual = new File("deleteme.ascii");

		assertTrue(compare(expected, actual));

		actual.delete(); 
	}

	@Test
	public void testToStringStringArray() throws Exception
	{
		String[] args = new String[] {"toString", 
		"src/test/resources/permanent_files/crust20.geotess"};
		new GeoTessExplorer().toString(args);

		assertTrue(buffer.toString().contains("DataType: FLOAT"));
		assertTrue(buffer.toString().contains("gridID = 808785948EB2350DD44E6C29BDEA6CAE"));

	}

	//	@Test
	//	public void testExtractGrid()
	//	{
	//		fail("Not yet implemented"); // TODO
	//	}
	//
	//	@Test
	//	public void testReformat()
	//	{
	//		fail("Not yet implemented"); // TODO
	//	}

	//@Test
	public void testInterpolatePoint() throws Exception
	{
		String[] args = new String[] {"interpolatePoint", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"30.", "90.", "80.", "-1", "linear", "linear", "true"
		};
		new GeoTessExplorer().interpolatePoint(args);

		//printExpected(buffer.toString());
		String expected = 
				"Lat, lon, depth = 30.000000, 90.000000, 80.000\n"
				+"\n"
				+"Layer  4 - MOHO\n"
				+"\n"
				+"pslowness (sec/km)                  8.015 (inverse)\n"
				+"\n"
				+"   Point       Lat        Lon    Depth  Dist(deg)  Coeff   pslowness\n"
				+"   29961  23.41238   94.13632    43.568   7.531  0.054442      8.070 (inverse)\n"
				+"   29960  23.41238   94.13632   165.712   7.531  0.024956      8.090 (inverse)\n"
				+"   20612  31.55087   98.70101   177.966   7.644  0.001346      8.131 (inverse)\n"
				+"   20613  31.55087   98.70101    61.948   7.644  0.007555      8.000 (inverse)\n"
				+"    5853  30.54740   89.53300    73.090   0.679  0.857040      8.000 (inverse)\n"
				+"    5852  30.54740   89.53300   185.393   0.679  0.054661      8.157 (inverse)\n"
				;

		assertTrue(compare(expected, buffer.toString()));

		buffer.reset();

		args = new String[] {"interpolatePoint", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"30.", "90.", "80.", "-1", "nn", "linear", "true"
		};
		new GeoTessExplorer().interpolatePoint(args);

		expected = 
				"Lat, lon, depth = 30.000000, 90.000000, 80.000\n"
						+"\n"
						+"Layer  4 - MOHO\n"
						+"\n"
						+"pslowness (sec/km)                  8.017 (inverse)\n"
						+"\n"
						+"   Point       Lat        Lon    Depth  Dist(deg)  Coeff   pslowness\n"
						+"   29961  23.41238   94.13632    43.568   7.531  0.042464      8.070 (inverse)\n"
						+"   29960  23.41238   94.13632   165.712   7.531  0.019466      8.090 (inverse)\n"
						+"   29885  22.09279   85.27860    34.973   8.944  0.010833      8.200 (inverse)\n"
						+"   20612  31.55087   98.70101   177.966   7.644  0.004139      8.131 (inverse)\n"
						+"   29884  22.09279   85.27860   159.982   8.944  0.006602      8.210 (inverse)\n"
						+"   20613  31.55087   98.70101    61.948   7.644  0.023229      8.000 (inverse)\n"
						+"    5853  30.54740   89.53300    73.090   0.679  0.839462      8.000 (inverse)\n"
						+"    5852  30.54740   89.53300   185.393   0.679  0.053540      8.157 (inverse)\n"
						+"   20536  28.91770   80.60695   173.337   8.260  0.000057      8.210 (inverse)\n"
						+"   20537  28.91770   80.60695    55.005   8.260  0.000208      8.115 (inverse)\n"
						;

		//printExpected(buffer.toString());

		assertTrue(compare(expected, buffer.toString()));


		buffer.reset();

		args = new String[] {"interpolatePoint", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"30.", "90.", "60.", "4", "nn", "linear", "true"
		};
		new GeoTessExplorer().interpolatePoint(args);

		//printExpected(buffer.toString());
		expected = 
				"Lat, lon, depth = 30.000000, 90.000000, 60.000\n"
						+"\n"
						+"Layer  4 - MOHO\n"
						+"\n"
						+"pslowness (sec/km)                  8.008 (inverse)\n"
						+"\n"
						+"   Point       Lat        Lon    Depth  Dist(deg)  Coeff   pslowness\n"
						+"   29961  23.41238   94.13632    43.568   7.531  0.052604      8.070 (inverse)\n"
						+"   29960  23.41238   94.13632   165.712   7.531  0.009325      8.090 (inverse)\n"
						+"   29885  22.09279   85.27860    34.973   8.944  0.013623      8.200 (inverse)\n"
						+"   29884  22.09279   85.27860   159.982   8.944  0.003813      8.210 (inverse)\n"
						+"   20613  31.55087   98.70101    61.948   7.644  0.027368      8.000 (inverse)\n"
						+"    5853  30.54740   89.53300    73.090   0.679  0.893002      8.000 (inverse)\n"
						+"   20536  28.91770   80.60695   173.337   8.260  0.000012      8.210 (inverse)\n"
						+"   20537  28.91770   80.60695    55.005   8.260  0.000253      8.115 (inverse)\n"
						;

		//printExpected(buffer.toString());

		assertTrue(compare(expected, buffer.toString()));
	}

	@Test
	public void testBorehole() throws Exception
	{
		String[] args = new String[] {"borehole", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"30.", "90.", "500.", "0", "100", "linear", "linear", 
				"depth", "true", "all"
		};
		new GeoTessExplorer().borehole(args);

		//printExpected(buffer.toString());
		String expected = 
				"    -5.159   2.500000\n"
						+"    -5.106   2.500000\n"
						+"    -5.106   6.000000\n"
						+"    19.135   6.000000\n"
						+"    19.135   6.400000\n"
						+"    45.153   6.400000\n"
						+"    45.153   7.100000\n"
						+"    71.168   7.100000\n"
						+"    71.168   8.005978\n"
						+"   410.010   9.029357\n"
						+"   410.010   9.360499\n"
						+"   660.010   10.19945\n"
						+"   660.010   10.79037\n"
						+"  1107.254   11.62442\n"
						+"  1554.497   12.25523\n"
						+"  2001.741   12.79666\n"
						+"  2448.985   13.30482\n"
						+"  2896.228   13.66019\n"
						+"  2896.228   8.000068\n"
						+"  3348.251   8.731149\n"
						+"  3800.273   9.321969\n"
						+"  4252.296   9.751903\n"
						+"  4704.318   10.07692\n"
						+"  5156.341   10.28900\n"
						+"  5156.341   11.04271\n"
						+"  5561.835   11.15893\n"
						+"  5967.330   11.23638\n"
						+"  6372.824   11.26220\n"
						;

		assertTrue(compare(expected, buffer.toString()));
	}

//	@Test
//	public void testMapValuesDepth() throws Exception
//	{
//		String[] args = new String[] {"mapValuesDepth", 
//				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
//				"0.", "30.", "2", "70.", "110.", "9", 
//				"4", "80.", "linear", "linear", "true", "all"
//		};
//		new GeoTessExplorer().mapValuesDepth(args);
//
//		//printExpected(buffer.toString());
//		String expected = 
//				"0.000000 70.00000 8.081463\n"
//						+"0.000000 75.00000 8.081489\n"
//						+"0.000000 80.00000 8.080488\n"
//						+"0.000000 85.00000 8.079940\n"
//						+"0.000000 90.00000 8.079506\n"
//						+"0.000000 95.00000 8.081558\n"
//						+"0.000000 100.0000 8.083254\n"
//						+"0.000000 105.0000 8.083523\n"
//						+"0.000000 110.0000 8.083647\n"
//						+"30.00000 70.00000 8.095020\n"
//						+"30.00000 75.00000 8.123919\n"
//						+"30.00000 80.00000 8.117526\n"
//						+"30.00000 85.00000 8.069766\n"
//						+"30.00000 90.00000 8.014627\n"
//						+"30.00000 95.00000 8.025163\n"
//						+"30.00000 100.0000 8.039938\n"
//						+"30.00000 105.0000 8.089537\n"
//						+"30.00000 110.0000 8.113143\n"
//						;
//		assertTrue(compare(expected, buffer.toString()));
//
//	}
//
//	@Test
//	public void testMapValuesLayer() throws Exception
//	{
//		String[] args = new String[] {"mapValuesLayer", 
//				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
//				"0.", "30.", "2", "70.", "110.", "9", 
//				"4", "1.", "linear", "linear", "true", "all"
//		};
//		new GeoTessExplorer().mapValuesLayer(args);
//
//		//printExpected(buffer.toString());
//		String expected = 
//				"0.000000 70.00000 8.040224\n"
//						+"0.000000 75.00000 8.040111\n"
//						+"0.000000 80.00000 8.040000\n"
//						+"0.000000 85.00000 8.040596\n"
//						+"0.000000 90.00000 8.040000\n"
//						+"0.000000 95.00000 8.040494\n"
//						+"0.000000 100.0000 8.042658\n"
//						+"0.000000 105.0000 8.041617\n"
//						+"0.000000 110.0000 8.040596\n"
//						+"30.00000 70.00000 8.061261\n"
//						+"30.00000 75.00000 8.108375\n"
//						+"30.00000 80.00000 8.100817\n"
//						+"30.00000 85.00000 8.060533\n"
//						+"30.00000 90.00000 8.005978\n"
//						+"30.00000 95.00000 8.014407\n"
//						+"30.00000 100.0000 8.020688\n"
//						+"30.00000 105.0000 8.059727\n"
//						+"30.00000 110.0000 8.070922\n"
//						;
//		assertTrue(compare(expected, buffer.toString()));
//	}

	@Test
	public void testValues3DBlock() throws Exception
	{
		String[] args = new String[] {"values3DBlock", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"0.", "30.", "2", "70.", "110.", "3", 
				"4", "5", "depth", "200.", "linear", "linear", "true", "all"
		};
		new GeoTessExplorer().values3DBlock(args);

		//printExpected(buffer.toString());
		String expected = 
				"70.00000 0.00000 410.000 9.030000\n"
						+"70.00000 0.00000 211.315 8.326722\n"
						+"70.00000 0.00000 12.630 8.040224\n"
						+"70.00000 0.00000 12.630 7.100000\n"
						+"70.00000 0.00000 9.130 7.100000\n"
						+"70.00000 30.00000 410.023 9.027818\n"
						+"70.00000 30.00000 225.071 8.354012\n"
						+"70.00000 30.00000 40.119 8.061261\n"
						+"70.00000 30.00000 40.119 7.267842\n"
						+"70.00000 30.00000 30.000 7.267842\n"
						+"90.00000 0.00000 410.000 9.030000\n"
						+"90.00000 0.00000 210.668 8.324970\n"
						+"90.00000 0.00000 11.336 8.040000\n"
						+"90.00000 0.00000 11.336 7.100000\n"
						+"90.00000 0.00000 8.836 7.100000\n"
						+"90.00000 30.00000 410.010 9.029357\n"
						+"90.00000 30.00000 240.589 8.365004\n"
						+"90.00000 30.00000 71.168 8.005978\n"
						+"90.00000 30.00000 71.168 7.100000\n"
						+"90.00000 30.00000 45.153 7.100000\n"
						+"110.00000 0.00000 410.000 9.030000\n"
						+"110.00000 0.00000 220.202 8.354629\n"
						+"110.00000 0.00000 30.405 8.040596\n"
						+"110.00000 0.00000 30.405 7.200000\n"
						+"110.00000 0.00000 20.905 7.200000\n"
						+"110.00000 30.00000 410.027 9.028187\n"
						+"110.00000 30.00000 221.667 8.360125\n"
						+"110.00000 30.00000 33.307 8.070922\n"
						+"110.00000 30.00000 33.307 6.900000\n"
						+"110.00000 30.00000 22.690 6.900000\n"
						;
		assertTrue(compare(expected, buffer.toString()));
	}

	@Test
	public void testMapLayerBoundary() throws Exception
	{
		String[] args = new String[] {"mapLayerBoundary", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"0.", "30.", "3", "70.", "120.", "6", 
				"4", "top", "depth", "linear"
		};
		new GeoTessExplorer().mapLayerBoundary(args);

		//printExpected(buffer.toString());
		String expected = 
				"0.000000 70.00000 12.62985\n"
						+"0.000000 80.00000 12.53521\n"
						+"0.000000 90.00000 11.33573\n"
						+"0.000000 100.0000 26.83370\n"
						+"0.000000 110.0000 30.40495\n"
						+"0.000000 120.0000 31.74084\n"
						+"15.00000 70.00000 12.99990\n"
						+"15.00000 80.00000 28.67358\n"
						+"15.00000 90.00000 21.78522\n"
						+"15.00000 100.0000 40.07377\n"
						+"15.00000 110.0000 21.67805\n"
						+"15.00000 120.0000 16.55903\n"
						+"30.00000 70.00000 40.11868\n"
						+"30.00000 80.00000 59.48401\n"
						+"30.00000 90.00000 71.16844\n"
						+"30.00000 100.0000 55.30667\n"
						+"30.00000 110.0000 33.30714\n"
						+"30.00000 120.0000 30.70587\n"
						;
		assertTrue(compare(expected, buffer.toString()));
	}

	@Test
	public void testMapLayerThickness() throws Exception
	{
		String[] args = new String[] {"mapLayerThickness", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"0.", "30.", "10.", "70.", "120.", "10.", 
				"5", "22", "nn"
		};
		new GeoTessExplorer().mapLayerThickness(args);

		//printExpected(buffer.toString());
		String expected = 
				"0.000000 70.00000 8.763096\n"
						+"0.000000 80.00000 7.944641\n"
						+"0.000000 90.00000 7.375000\n"
						+"0.000000 100.0000 26.83121\n"
						+"0.000000 110.0000 30.49823\n"
						+"0.000000 120.0000 30.47252\n"
						+"10.00000 70.00000 8.924626\n"
						+"10.00000 80.00000 26.80073\n"
						+"10.00000 90.00000 16.49135\n"
						+"10.00000 100.0000 31.37078\n"
						+"10.00000 110.0000 20.98280\n"
						+"10.00000 120.0000 22.99857\n"
						+"20.00000 70.00000 25.18729\n"
						+"20.00000 80.00000 36.11144\n"
						+"20.00000 90.00000 28.75668\n"
						+"20.00000 100.0000 44.77024\n"
						+"20.00000 110.0000 29.29995\n"
						+"20.00000 120.0000 15.89580\n"
						+"30.00000 70.00000 41.01290\n"
						+"30.00000 80.00000 62.37027\n"
						+"30.00000 90.00000 76.27910\n"
						+"30.00000 100.0000 59.10984\n"
						+"30.00000 110.0000 34.25803\n"
						+"30.00000 120.0000 30.94409\n"
						;
		assertTrue(compare(expected, buffer.toString()));
	}

	@Test
	public void testSlice() throws Exception
	{
		String[] args = new String[] {"slice", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"0.", "80.", "30.", "90.", "true", "3",
				"150.", "4", "22", "nn", "linear", "distance,depth",
				"true", "all"
		};
		new GeoTessExplorer().slice(args);

		//printExpected(buffer.toString());

		String expected = 
				" 0.000000 4.592314 2.231427\n"
						+" 0.000000 6.036955 2.231427\n"
						+" 0.000000 6.036955 5.000000\n"
						+" 0.000000 7.737150 5.000000\n"
						+" 0.000000 7.737150 6.600000\n"
						+" 0.000000 10.03695 6.600000\n"
						+" 0.000000 10.03695 7.100000\n"
						+" 0.000000 12.53695 7.100000\n"
						+" 0.000000 12.53695 8.040010\n"
						+" 0.000000 145.0334 8.120702\n"
						+" 0.000000 277.5298 8.545599\n"
						+" 0.000000 410.0262 9.029905\n"
						+" 15.65905 2.992354 3.256104\n"
						+" 15.65905 8.973023 3.256104\n"
						+" 15.65905 8.973023 5.061762\n"
						+" 15.65905 11.05907 5.061762\n"
						+" 15.65905 11.05907 6.600000\n"
						+" 15.65905 13.70320 6.600000\n"
						+" 15.65905 13.70320 7.107226\n"
						+" 15.65905 16.53062 7.107226\n"
						+" 15.65905 16.53062 8.063438\n"
						+" 15.65905 147.7029 8.141572\n"
						+" 15.65905 278.8753 8.547090\n"
						+" 15.65905 410.0476 9.029114\n"
						+" 31.31810 -5.134029 2.500000\n"
						+" 31.31810 -5.082363 2.500000\n"
						+" 31.31810 -5.082363 6.000004\n"
						+" 31.31810 19.14411 6.000004\n"
						+" 31.31810 19.14411 6.399995\n"
						+" 31.31810 45.14585 6.399995\n"
						+" 31.31810 45.14585 7.100004\n"
						+" 31.31810 71.14507 7.100004\n"
						+" 31.31810 71.14507 8.008351\n"
						+" 31.31810 184.1001 8.157639\n"
						+" 31.31810 297.0551 8.588244\n"
						+" 31.31810 410.0101 9.029332\n"
						;
		
		if (!compare(expected, buffer.toString()))
			printExpected(buffer.toString());
		
		assertTrue(compare(expected, buffer.toString()));
	}

	@Test
	public void testSliceDistAz() throws Exception
	{
		String[] args = new String[] {"sliceDistAz", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"0.", "90.", "30.", "0.", "3",
				"150.", "4", "22", "nn", "linear", "distance,depth",
				"true", "all"
		};
		new GeoTessExplorer().sliceDistAz(args);

		//printExpected(buffer.toString());

		String expected = 
				" 0.000000 3.960730 2.046154\n"
						+" 0.000000 4.835730 2.046154\n"
						+" 0.000000 4.835730 5.000000\n"
						+" 0.000000 6.535437 5.000000\n"
						+" 0.000000 6.535437 6.600000\n"
						+" 0.000000 8.835730 6.600000\n"
						+" 0.000000 8.835730 7.100000\n"
						+" 0.000000 11.33573 7.100000\n"
						+" 0.000000 11.33573 8.040000\n"
						+" 0.000000 144.2238 8.116810\n"
						+" 0.000000 277.1118 8.544076\n"
						+" 0.000000 409.9998 9.030000\n"
						+" 15.00000 2.743701 3.171050\n"
						+" 15.00000 8.521824 3.171050\n"
						+" 15.00000 8.521824 5.474120\n"
						+" 15.00000 12.49859 5.474120\n"
						+" 15.00000 12.49859 6.600000\n"
						+" 15.00000 17.24106 6.600000\n"
						+" 15.00000 17.24106 7.151617\n"
						+" 15.00000 22.05985 7.151617\n"
						+" 15.00000 22.05985 8.044412\n"
						+" 15.00000 151.3747 8.136109\n"
						+" 15.00000 280.6896 8.556118\n"
						+" 15.00000 410.0046 9.029893\n"
						+" 30.00000 -5.187042 2.500000\n"
						+" 30.00000 -5.122052 2.500000\n"
						+" 30.00000 -5.122052 6.000002\n"
						+" 30.00000 19.29032 6.000002\n"
						+" 30.00000 19.29032 6.399998\n"
						+" 30.00000 45.49159 6.399998\n"
						+" 30.00000 45.49159 7.100002\n"
						+" 30.00000 71.69089 7.100002\n"
						+" 30.00000 71.69089 8.005745\n"
						+" 30.00000 184.4627 8.157163\n"
						+" 30.00000 297.2345 8.589027\n"
						+" 30.00000 410.0063 9.029507\n"
						;

		assertTrue(compare(expected, buffer.toString()));
	}

	@Test
	public void testVtkDepths() throws Exception
	{
		String[] args = new String[] {"vtkDepths", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"vtkFiles/vtkDepths_layer4.vtk",
				"-1", "410", "60", "50", "true", "0"
		};
		new GeoTessExplorer().vtkDepths(args);

		//printExpected(buffer.toString());

		assertTrue(compare("VTK output successfully written to vtkFiles/vtkDepths_layer4.vtk\n", 
				buffer.toString()));

		buffer.reset();

		args = new String[] {"vtkDepths", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"vtkFiles/vtkDepths_noLayer.vtk",
				"-1", "410", "60", "50", "true", "0"
		};
		new GeoTessExplorer().vtkDepths(args);

		//printExpected(buffer.toString());

		assertTrue(compare("VTK output successfully written to vtkFiles/vtkDepths_noLayer.vtk\n", 
				buffer.toString()));

	}

	@Test
	public void testVtkLayerThickness() throws Exception
	{
		String[] args = new String[] {"vtkLayerThickness", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"vtkFiles/vtkLayerThickness.vtk",
				"5", "99", "linear"
		};
		new GeoTessExplorer().vtkLayerThickness(args);

		//printExpected(buffer.toString());

		assertTrue(compare("VTK output successfully written to vtkFiles/vtkLayerThickness.vtk\n", 
				buffer.toString()));

	}

	@Test
	public void testVtkLayerBoundary() throws Exception
	{
		//System.setOut(old_out);

		String[] args = new String[] {"vtkLayerBoundary", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"vtkFiles/vtkLayerBoundary.vtk", "depth", "linear"
		};
		new GeoTessExplorer().vtkLayerBoundary(args);

		//printExpected(buffer.toString());

		assertTrue(compare("VTK output successfully written to vtkFiles/vtkLayerBoundary.vtk\n", 
				buffer.toString()));

	}

	@Test
	public void testVtkLayersD() throws Exception
	{
		String[] args = new String[] {"vtkLayers", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"vtkFiles/vtkLayers_layer_%d.vtk", 
				"4-99", "true", "all"
		};
		new GeoTessExplorer().vtkLayers(args);

		//printExpected(buffer.toString());
		String expected = 
				"VTK output successfully written to vtkFiles/vtkLayers_layer_4.vtk\n"
						+"VTK output successfully written to vtkFiles/vtkLayers_layer_5.vtk\n"
						+"VTK output successfully written to vtkFiles/vtkLayers_layer_6.vtk\n"
						+"VTK output successfully written to vtkFiles/vtkLayers_layer_7.vtk\n"
						+"VTK output successfully written to vtkFiles/vtkLayers_layer_8.vtk\n"
						;

		assertTrue(compare(expected, buffer.toString()));

	}

	@Test
	public void testVtkLayersS() throws Exception
	{
		String[] args = new String[] {"vtkLayers", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"vtkFiles/vtkLayers_layer_%s.vtk", 
				"4-99", "true", "all"
		};
		new GeoTessExplorer().vtkLayers(args);

		//printExpected(buffer.toString());
		String expected = 
				"VTK output successfully written to vtkFiles/vtkLayers_layer_MOHO.vtk\n"
				+"VTK output successfully written to vtkFiles/vtkLayers_layer_LOWER_CRUST.vtk\n"
				+"VTK output successfully written to vtkFiles/vtkLayers_layer_MIDDLE_CRUST.vtk\n"
				+"VTK output successfully written to vtkFiles/vtkLayers_layer_UPPER_CRUST.vtk\n"
				+"VTK output successfully written to vtkFiles/vtkLayers_layer_SURFACE.vtk\n"
				;

		assertTrue(compare(expected, buffer.toString()));

	}

	//		@Test
	//		public void testFunction()
	//		{
	//			fail("Not yet implemented"); // TODO
	//		}

	@Test
	public void testVtkSolid() throws Exception
	{
		String[] args = new String[] {"vtkSolid", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"vtkFiles/vtkSolid.vtk", "100",
				"0", "99", "nn", "cubic_spline", "true", "all"
		};

		new GeoTessExplorer().vtkSolid(args);

		//printExpected(buffer.toString());

		assertTrue(compare("VTK output written to vtkFiles/vtkSolid.vtk\n", 
				buffer.toString()));
	}

	@Test
	public void testVtkSlice() throws Exception
	{
		String[] args = new String[] {"vtkSlice", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"vtkFiles/vtkSlice.vtk", 
				"0", "90", "50", "90", "true", "100", 
				"50", "0", "4", "nn", "cs", "true", "all"
		};

		new GeoTessExplorer().vtkSlice(args);

		//printExpected(buffer.toString());

		String expected = 
				"X direction (lat, lon) =  65.241482  -90.000000\n"
				+"Y direction (lat, lon) =  25.052509   90.000000\n"
				+"Z direction (lat, lon) =  -0.000000  180.000000\n"
				;

		assertTrue(compare(expected, buffer.toString()));
	}

	@Test
	public void testVtk3DBlock() throws Exception
	{
		String[] args = new String[] {"vtk3DBlock", 
				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
				"vtkFiles/vtk3DBlock.vtk", 
				"0", "30", "100", "70", "110", "100",
				"4", "8", "depth", "20", 
				"linear", "linear", "true", "all"
		};

		new GeoTessExplorer().vtk3DBlock(args);

		//printExpected(buffer.toString());

		String expected = "VTK output written to vtkFiles/vtk3DBlock.vtk\n";

		assertTrue(compare(expected, buffer.toString()));
	}

//	@Test
//	public void testVtkRobinson() throws Exception
//	{
//		String[] args = new String[] {"vtkRobinson", 
//				"src/test/resources/permanent_files/unified_crust20_ak135.geotess", ".",
//				"vtkFiles/vtkRobinson.vtk", 
//				"155", "100.", "4", "true", "linear", "true", "all"
//		};
//
//		new GeoTessExplorer().vtkRobinson(args);
//
//		//printExpected(buffer.toString());
//
//		String expected = 
//				"VTK output written to /Users/sballar/work/GeoTessTesting/vtkFiles/vtkRobinson.vtk\n"
//				+"Coastlines written to file /Users/sballar/work/GeoTessTesting/vtkFiles/map_coastlines_centerLon_155.vtk\n"
//				;
//
//		assertTrue(compare(expected, buffer.toString()));
//	}

	@Test
	public void testParseList() throws Exception
	{	
		assertArrayEquals(new int[] {0,1,2,3}, new GeoTessExplorer().parseList("all", 3));
		assertArrayEquals(new int[] {0,1,2,3}, new GeoTessExplorer().parseList("0-n", 3));
		assertArrayEquals(new int[] {3}, new GeoTessExplorer().parseList("n", 3));
		assertArrayEquals(new int[] {0,1,2}, new GeoTessExplorer().parseList("0-2", 30));
		assertArrayEquals(new int[] {1,2,3}, new GeoTessExplorer().parseList("1-3", 30));
		assertArrayEquals(new int[] {1,2,3,6}, new GeoTessExplorer().parseList("1-3, 6", 30));
		assertArrayEquals(new int[] {1,2,3}, new GeoTessExplorer().parseList("1-5", 3));
	}

	@Test
	public void testGetLatitudes() throws Exception
	{
		new GeoTessExplorer().getLatitudes(new String[] {"getLatitudes", "30.", "40.", "1."});

		//printExpected(buffer.toString());

		String expected = 
				" 30.00000\n"
						+" 31.00000\n"
						+" 32.00000\n"
						+" 33.00000\n"
						+" 34.00000\n"
						+" 35.00000\n"
						+" 36.00000\n"
						+" 37.00000\n"
						+" 38.00000\n"
						+" 39.00000\n"
						+" 40.00000\n"
						;
		assertTrue(compare(expected, buffer.toString()));

		buffer.reset();
		new GeoTessExplorer().getLatitudes(new String[] {"getLatitudes", "30.", "40.", "6"});

		//printExpected(buffer.toString());

		expected = 
				" 30.00000\n"
						+" 32.00000\n"
						+" 34.00000\n"
						+" 36.00000\n"
						+" 38.00000\n"
						+" 40.00000\n"
						;
		assertTrue(compare(expected, buffer.toString()));
	}

	@Test
	public void testGetLongitudes() throws Exception
	{
		new GeoTessExplorer().getLongitudes(new String[] {"getLongitudes", "30.", "40.", "1."});

		//printExpected(buffer.toString());

		String expected = 
				" 30.00000\n"
						+" 31.00000\n"
						+" 32.00000\n"
						+" 33.00000\n"
						+" 34.00000\n"
						+" 35.00000\n"
						+" 36.00000\n"
						+" 37.00000\n"
						+" 38.00000\n"
						+" 39.00000\n"
						+" 40.00000\n"
						;
		assertTrue(compare(expected, buffer.toString()));

		buffer.reset();
		new GeoTessExplorer().getLongitudes(new String[] {"getLongitudes", "30.", "40.", "6"});

		//printExpected(buffer.toString());

		expected = 
				" 30.00000\n"
						+" 32.00000\n"
						+" 34.00000\n"
						+" 36.00000\n"
						+" 38.00000\n"
						+" 40.00000\n"
						;
		assertTrue(compare(expected, buffer.toString()));


		buffer.reset();
		new GeoTessExplorer().getLongitudes(new String[] {"getLongitudes", "170.", "-170.", "4."});

		//printExpected(buffer.toString());

		expected = 
				" 170.0000\n"
						+" 174.0000\n"
						+" 178.0000\n"
						+" 182.0000\n"
						+" 186.0000\n"
						+" 190.0000\n"
						;
		assertTrue(compare(expected, buffer.toString()));

		buffer.reset();
		new GeoTessExplorer().getLongitudes(new String[] {"getLongitudes", "170.", "-170.", "5"});

		//printExpected(buffer.toString());

		expected = 
				" 170.0000\n"
						+" 175.0000\n"
						+" 180.0000\n"
						+" 185.0000\n"
						+" 190.0000\n"
						;
		assertTrue(compare(expected, buffer.toString()));
	}

	@Test
	public void testGetDistanceDegrees() throws Exception
	{
		new GeoTessExplorer().getDistanceDegrees(new String[] {"getDistanceDegrees", "30.", "40.", "40.", "50.", "10"});

		//printExpected(buffer.toString());

		String expected = 
				" 0.000000\n"
						+" 1.433541\n"
						+" 2.867082\n"
						+" 4.300623\n"
						+" 5.734164\n"
						+" 7.167705\n"
						+" 8.601246\n"
						+" 10.03479\n"
						+" 11.46833\n"
						+" 12.90187\n"
						;

		assertTrue(compare(expected, buffer.toString()));

	}

}
