/**
 *  Copyright (C) 2014
 *	Sandia Corporation (Sandia National	Laboratories)
 *	Albuquerque, NM, 87185-1004
 *	All	rights reserved.
 *
 *	This software was developed	at Sandia National Laboratories, which is
 *	operated by	the	Sandia Corporation under contract for the United States
 *	Department of Energy.  This	software is	is protected by	copyright under
 *	the	laws of	the	United States.	This software is not to	be disclosed or
 *	duplicated without express written authorization from Sandia
 *	Corporation.
*/
package gov.sandia.gmp.util.interfaces;


/**
 * Represents a JUnit Test type. 
 * 
 * A UnitTest would be a much smaller test that eliminates external resources
 * such as databases, other IPF components etc...
 * 
 * This object is intended to help us label/identify the different types of
 * automated IPF tests.
 * 
 * See {@link IntegrationTest}
 * 
 * 
 * @author Stephen Heck (sheck@sandia.gov)
 *
 */
public interface UnitTest{}
