package geotesstest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import gov.sandia.geotess.extensions.rstt.LineScanner;
import gov.sandia.geotess.extensions.rstt.UncertaintyPDU;
import gov.sandia.gmp.util.globals.GMTFormat;

public class MBFileReader {

	public static void main(String[] args) {
		try {
			new MBFileReader().readMBDataFiles("/Users/sballar/Documents/rstt/path_dependent_uncertainty/mike_begnaud_files", 
					"/Users/sballar/Documents/rstt/path_dependent_uncertainty/jim_hipp_files/pdu_<phase>.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param inputDirectory path to the directory containing Mike's files
	 * @param outputFiles output filenames.  Must contain the string '<phase>' which
	 * will be replaced with phase name.
	 * @throws Exception
	 */
	protected void readMBDataFiles(String inputDirectory, String outputFiles) throws Exception
	{
		float[] distBins = {2.0f, 4.0f, 6.0f, 8.0f, 10.0f, 12.0f, 14.0f};
		int gridPoints = 40962;
		String gridId = "808785948EB2350DD44E6C29BDEA6CAE";

		float[] pCrustError = readMBCrustError(inputDirectory, "Uncertainty2_Pcrust_Sh.txt", gridPoints);
		float[] sCrustError = readMBCrustError(inputDirectory, "Uncertainty2_Scrust_Sh.txt", gridPoints);
		float[][][] distDepErrorLg = readMBDistDepError(inputDirectory,
				"Uncertainty2_Lg_Sh.txt", gridPoints, distBins.length);
		float[][][] distDepErrorPg = readMBDistDepError(inputDirectory,
				"Uncertainty2_Pg_Sh.txt", gridPoints, distBins.length);
		float[][][] distDepErrorPn = readMBDistDepError(inputDirectory,
				"Uncertainty2_Pn_Sh.txt", gridPoints, distBins.length);
		float[][][] distDepErrorSn = readMBDistDepError(inputDirectory,
				"Uncertainty2_Sn_Sh.txt", gridPoints, distBins.length);

		writePDUFile("Lg", gridId, outputFiles, distBins, sCrustError, distDepErrorLg[1],
				distDepErrorLg[0], distDepErrorLg[2]);
		writePDUFile("Sn", gridId, outputFiles, distBins, sCrustError, distDepErrorSn[1],
				distDepErrorSn[0], distDepErrorSn[2]);
		writePDUFile("Pg", gridId, outputFiles, distBins, pCrustError, distDepErrorPg[1],
				distDepErrorPg[0], distDepErrorPg[2]);
		writePDUFile("Pn", gridId, outputFiles, distBins, pCrustError, distDepErrorPn[1],
				distDepErrorPn[0], distDepErrorPn[2]);
	}

	private void writePDUFile(String phase, String gridId, String fileName,
			float[] distBins, float[] crustError,	float[][] randomError,
			float[][] modelError, float[][] bias) throws Exception
	{
		// create new model for the input phase and set its' data from the inputs.
		// write the file as ascii

		UncertaintyPDU pdu = new UncertaintyPDU(phase);
		pdu.setData(distBins, crustError, randomError, modelError, bias);

		pdu.setGridId(gridId);
		
		// required properties:
		pdu.getProperties().put("phase", phase);
		
		pdu.getProperties().put("gridId", gridId);
		
		pdu.getProperties().put("nDistanceBins", String.format("%d", pdu.getDistanceBins().length));
		
		pdu.getProperties().put("nVertices", String.format("%d", pdu.getCrustalError().length));
		
		pdu.getProperties().put("includeRandomError", String.format("%b", pdu.getRandomError().length > 0));
		
		// optional properties:
		
		pdu.getProperties().put("author", "Mike Begnaud (mbegnaud@lanl.gov)");
		
		pdu.getProperties().put("software", getClass().getSimpleName());
		
		pdu.getProperties().put("creationDate", GMTFormat.getNow());
		
		System.out.println("Writing file "+fileName.replace("<phase>", phase));
		pdu.writeFileAscii(new File(fileName.replace("<phase>", phase)));		
	}

	/**
	 * Crust error reader used by readMBDataFiles.
	 * 
	 * @param filePath
	 * @param filNam
	 * @param gridPoints
	 * @return
	 * @throws IOException
	 */
	private static float[] readMBCrustError(String filePath, String filNam, int gridPoints) throws IOException
	{
		String fn = filePath + File.separator + filNam;
		System.out.println("Reading file "+fn);
		BufferedReader input = new BufferedReader(new FileReader(fn));
		LineScanner lineScanner = new LineScanner(input);

		float[] crustError = new float[gridPoints];
		for (int i = 0; i < gridPoints; ++i)
		{
			//int id = 
					lineScanner.nextInt();
			crustError[i] = lineScanner.nextFloat();
		}
		input.close();

		return crustError;
	}

	/**
	 * Depth dependent error reader used by readMBDataFiles.
	 * 
	 * @param filePath
	 * @param filNam
	 * @param gridPoints
	 * @param nDist
	 * @return
	 * @throws IOException
	 */
	private static float[][][] readMBDistDepError(String filePath, String filNam,
			int gridPoints, int nDist) throws IOException
	{
		String fn = filePath + File.separator + filNam;
		System.out.println("Reading file "+fn);
		BufferedReader input = new BufferedReader(new FileReader(fn));
		LineScanner lineScanner = new LineScanner(input);

		float[][][] distDepError = new float[3][nDist][gridPoints];
		for (int j = 0; j < nDist; ++j)
		{
			for (int i = 0; i < gridPoints; ++i)
			{
				//int id = 
						lineScanner.nextInt();
				distDepError[0][j][i] = lineScanner.nextFloat();
				distDepError[1][j][i] = lineScanner.nextFloat();
				distDepError[2][j][i] = lineScanner.nextFloat();
			}
		}
		input.close();

		return distDepError;
	}

}
