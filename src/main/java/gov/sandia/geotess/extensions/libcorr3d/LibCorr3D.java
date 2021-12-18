/**
 * Copyright 2009 Sandia Corporation. Under the terms of Contract
 * DE-AC04-94AL85000 with Sandia Corporation, the U.S. Government
 * retains certain rights in this software.
 * 
 * BSD Open Source License.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *    * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *    * Neither the name of Sandia National Laboratories nor the names of its
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package gov.sandia.geotess.extensions.libcorr3d;

import java.io.File;
import java.io.IOException;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessPosition;
import gov.sandia.gmp.util.globals.InterpolatorType;
import gov.sandia.gmp.util.globals.Site;
import gov.sandia.gmp.util.numerical.vector.VectorGeo;
import gov.sandia.gmp.util.numerical.vector.VectorUnit;

public class LibCorr3D
{
	private static int nextIndex;

	public final int index;

	/**
	 * All the models that are supported by this instance of LibCorr3D are
	 * managed by a separate class that is thread safe. That way multiple
	 * instances of LibCorr3D can all reference a common set of models without
	 * stepping on each other.
	 */
	protected LibCorr3DModels libcorrModels;

	/**
	 * InterpolatorType.LINEAR or InterpolatorType.NATURAL_NEIGHBOR.
	 */
	private InterpolatorType interpTypeHorz, interpTypeRadial;

	/**
	 * Constructor that instantiates a new LibCorr3DModelsGMP.
	 * 
	 * @param rootPath
	 * @param relGridPath
	 * @param preloadModels
	 * @param interpTypeHorz
	 *            .LINEAR or InterpolatorType.NATURAL_NEIGHBOR
	 * @param interpTypeRadial
	 * @throws IOException
	 */
	public LibCorr3D(File rootPath, String relGridPath, boolean preloadModels, 
			InterpolatorType interpTypeHorz, InterpolatorType interpTypeRadial) throws Exception
	{
		libcorrModels = new LibCorr3DModels(rootPath, relGridPath, preloadModels, null,
				interpTypeHorz, interpTypeRadial);
		this.interpTypeHorz = interpTypeHorz;
		this.interpTypeRadial = interpTypeRadial;
		this.index = nextIndex++;
	}

	/**
	 * Constructor that takes a reference to an existing LibCorr3DModels object.
	 * 
	 * @param libcorr3DModels
	 *            a LibCorr3DModels object
	 */
	public LibCorr3D(LibCorr3DModels libcorr3DModels, InterpolatorType interpTypeHorz, 
			InterpolatorType interpTypeRadial)
	{
		this.libcorrModels = libcorr3DModels;
		this.interpTypeHorz = interpTypeHorz;
		this.interpTypeRadial = interpTypeRadial;
		this.index = nextIndex++;
	}

	/**
	 * Retrieve the number of unique Site -&gt; phase -&gt; attribute combinations
	 * that are supported by this LibCorr3DModels object.
	 * 
	 * @return
	 */
	public int size()
	{
		return libcorrModels.getSupportMap().size();
	}

	/**
	 * Retrieve the number of unique Site objects supported by this
	 * LibCorr3DModels object.
	 * 
	 * @return the number of unique Site objects supported by this
	 *         LibCorr3DModels object.
	 */
	public int getNSites()
	{
		return libcorrModels.getNSites();
	}

	/**
	 * Return the LibCorr3DModels object that supports this LibCorr3D object.
	 * LibCorr3DModels manages a collection of LibCorr3DModel objects, each of
	 * which is loaded from a separate file.
	 * 
	 * @return
	 */
	public LibCorr3DModels getLibCorrModels()
	{
		return libcorrModels;
	}

	/**
	 * Is the specified station/phase/attribute supported by LibCorr3DModel
	 * 
	 * @param sta
	 * @param phase
	 * @param attribute
	 * @return
	 */
	public boolean isPathCorrSupported(Site sta, String phase,
			String attribute)
	{
		return libcorrModels.isSupported(sta, phase, attribute);
	}

	/**
	 * Return the index of the model that supports the specified station, phase,
	 * attribute, or -1.
	 * 
	 * @param station
	 * @param phase
	 * @param attribute
	 * @return the index of the model that supports the specified station,
	 *         phase, attribute, or -1.
	 */
	public int getLookupIndex(Site station, String phase, String attribute)
	{
		return libcorrModels.getModelIndex(station, phase, attribute);
	}

	/**
	 * Retrieve a path correction for the specified station, phase, attribute,
	 * or NaN if anything goes wrong.
	 * 
	 * @param lookupIndex
	 * @param sourcePosition
	 * @param sourceRadius
	 * @throws GeoTessException
	 * @throws IOException
	 * @return
	 */
	public double getPathCorrection(int lookupIndex,
			double[] sourcePosition, double sourceRadius)
					throws Exception
	{
		GeoTessPosition pos = libcorrModels.getModel(lookupIndex);
		return pos == null ? Double.NaN : pos.set(0, sourcePosition, sourceRadius).getValue(0);
	}

	/**
	 * Retrieve the derivative of the path correction wrt to source-receiver
	 * separation. Returns NaN if anything goes wrong.
	 * 
	 * @param lookupIndex
	 * @param sourcePosition
	 * @param sourceRadius
	 * @return
	 * @throws GeoTessException
	 * @throws IOException
	 */
	public double getPathCorrDerivHorizontal(int lookupIndex, double[] sourcePosition, double sourceRadius)
			throws Exception
	{
		if (lookupIndex < 0 || VectorGeo.isPole(sourcePosition))
			return Double.NaN; 

		GeoTessPosition gtpos = libcorrModels.getModel(lookupIndex);

		if (gtpos == null)
			return Double.NaN;

		double[] site = ((LibCorr3DModel) gtpos.getModel()).getSite().getUnitVector();

		double az = VectorUnit.azimuth(sourcePosition, site, Double.NaN);

		if (Double.isNaN(az))
			return Double.NaN;

		double[] y = VectorUnit.move(sourcePosition, 1e-3, az + Math.PI);

		double yval = gtpos.set(0, y, sourceRadius).getValue(0);

		double sval = gtpos.set(0, sourcePosition, sourceRadius).getValue(0);

		return (yval - sval) / 1e-3;
	}

	/**
	 * Retrieve the derivative of the path correction wrt to source latitude. 
	 * Returns NaN if anything goes wrong.
	 * 
	 * @param lookupIndex
	 * @param sourcePosition
	 * @param sourceRadius
	 * @return
	 * @throws GeoTessException
	 * @throws IOException
	 */
	public double getPathCorrDerivLat(int lookupIndex, double[] sourcePosition, double sourceRadius)
			throws Exception
	{
		if (lookupIndex < 0 || VectorGeo.isPole(sourcePosition))
			return Double.NaN; 

		GeoTessPosition gtpos = libcorrModels.getModel(lookupIndex);

		if (gtpos == null)
			return Double.NaN;

		double[] y = VectorUnit.moveNorth(sourcePosition, 1e-3);

		double yval = gtpos.set(0, y, sourceRadius).getValue(0);

		double sval = gtpos.set(0, sourcePosition, sourceRadius).getValue(0);

		return (yval - sval) / 1e-3;
	}

	/**
	 * Retrieve the derivative of the path correction wrt to source longitude. 
	 * Returns NaN if anything goes wrong.
	 * 
	 * @param lookupIndex
	 * @param sourcePosition
	 * @param sourceRadius
	 * @return
	 * @throws GeoTessException
	 * @throws IOException
	 */
	public double getPathCorrDerivLon(int lookupIndex, double[] sourcePosition, double sourceRadius)
			throws Exception
	{
		if (lookupIndex < 0 || VectorGeo.isPole(sourcePosition))
			return Double.NaN; 

		GeoTessPosition gtpos = libcorrModels.getModel(lookupIndex);

		if (gtpos == null)
			return Double.NaN;

		double[] y = VectorUnit.move(sourcePosition, 1e-3, Math.PI/2);

		double yval = gtpos.set(0, y, sourceRadius).getValue(0);

		double sval = gtpos.set(0, sourcePosition, sourceRadius).getValue(0);

		return (yval - sval) / 1e-3;
	}

	/**
	 * Retrieve derivative of the path correction with respect to source radius.
	 * Returns NaN if anything goes wrong.
	 * 
	 * @param lookupIndex
	 * @param sourcePosition
	 * @param sourceRadius
	 * @return
	 * @throws GeoTessException
	 * @throws IOException
	 */
	public double getPathCorrDerivRadial(int lookupIndex, double[] sourcePosition, double sourceRadius)
			throws Exception
	{
		GeoTessPosition gtpos = libcorrModels.getModel(lookupIndex);

		if (gtpos == null)
			return Double.NaN;

		gtpos.set(0, sourcePosition, sourceRadius);

		// if layer thickness is zero return zero
		double thick = gtpos.getLayerThickness();
		if (thick < 1e-2)
			return 0.;

		double dr = 0.1;
		// if layer thickness is less than dr, compute derivative
		// from values at top and bottom of the layer.
		if (thick <= dr)
		{
			gtpos.setRadius(0, gtpos.getRadiusTop());
			double pctop = gtpos.getValue(0);
			gtpos.setRadius(0, gtpos.getRadiusBottom());
			return (pctop - gtpos.getValue(0)) / thick;
		}

		if (sourceRadius + dr >= gtpos.getRadiusTop())
		{
			double rtop = gtpos.getRadiusTop();
			gtpos.setRadius(0, rtop);
			double pctop = gtpos.getValue(0);
			gtpos.setRadius(0, rtop - dr);
			return (pctop - gtpos.getValue(0)) / dr;
		}

		if (sourceRadius < gtpos.getRadiusBottom())
		{
			double rbot = gtpos.getRadiusBottom();
			gtpos.setRadius(0, rbot);
			double pcbot = gtpos.getValue(0);
			gtpos.setRadius(0, rbot + dr);
			return (gtpos.getValue(0) - pcbot) / dr;
		}

		double pc = gtpos.getValue(0);
		gtpos.setRadius(0, sourceRadius + dr);
		return (gtpos.getValue(0) - pc) / dr;
	}

	public InterpolatorType getPathCorrInterpolatorTypeHorizontal()
	{
		return interpTypeHorz;
	}

	public InterpolatorType getPathCorrInterpolatorTypeRadial()
	{
		return interpTypeRadial;
	}

	public String getPathCorrRootDirectory() throws IOException
	{
		return libcorrModels.getRootPath().getCanonicalPath();
	}

	public String getPathCorrModelFile(Site station, String phase,
			String attribute) throws IOException
	{
		return libcorrModels.getModelFile(station, phase, attribute)
				.getCanonicalPath();
	}

	public String getUncertaintyType()
	{
		return this.getClass().getName();
	}

	static public String getVersion() {
		return "1.3.1";
	}

	public boolean isUncertaintySupported(Site station, String phase,
			String attribute)
	{
		return libcorrModels.isSupported(station, phase, attribute);
	}

	public double getUncertainty(int lookupIndex, double[] sourcePosition, double sourceRadius)
			throws Exception
	{
		if (lookupIndex < 0)
			return Double.NaN;

		GeoTessPosition gtpos = libcorrModels.getModel(lookupIndex);

		if (gtpos == null)
			return Double.NaN;
		gtpos.set(0, sourcePosition, sourceRadius);
		return gtpos.getValue(1);
	}

	public String getUncertaintyRootDirectory() throws IOException
	{
		return libcorrModels.getRootPath().getCanonicalPath();
	}

	public String getUncertaintyModelFile(int lookupIndex) throws IOException
	{
		return libcorrModels.getModelFile(lookupIndex).getCanonicalPath();
	}

	public String getUncertaintyModelFile(Site station, String phase,
			String attribute) throws IOException
	{
		return libcorrModels.getModelFile(station, phase, attribute)
				.getCanonicalPath();
	}

	public Site getSite(String sta, double epochTime)
	{
		return libcorrModels.getSite(sta, epochTime);
	}

}
