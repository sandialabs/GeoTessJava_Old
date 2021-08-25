// Module:        $RCSfile: AbstractLogOutput.java,v $
// Revision:      $Revision: 1.2 $
// Last Modified: $Date: 2009/07/21 17:03:57 $
// Last Check-in: $Author: mchang $

package gov.sandia.gmp.util.logmanager;

import gov.sandia.gmp.util.globals.Globals;

abstract class AbstractLogOutput {

	/**
	 * Prefix to prepend to output messages.
	 */
	protected String prefix = "";
	
	/**
	 * New-line.
	 */
	protected static String newLine = Globals.NL;
	
	/**
	 * Output error message and stack trace.
	 * @param exception exception
	 * @return true on success, false on failure
	 */
	protected abstract boolean outputError(Exception exception);
	
	/**
	 * Output status message and stack trace.
	 * @param exception exception
	 * @return true on success, false on failure
	 */
	protected abstract boolean outputStatus(Exception exception);
	
	/**
	 * Output warning message and stack trace.
	 * @param exception exception
	 * @return true on success, false on failure
	 */
	protected abstract boolean outputWarning(Exception exception);
	
	/**
	 * Output error message.
	 * @param msg string message
	 * @return true on success, false on failure
	 */
	protected abstract boolean outputError(String msg);
	
	/**
	 * Output status message.
	 * @param msg string message
	 * @return true on success, false on failure
	 */
	protected abstract boolean outputStatus(String msg);	
	
	/**
	 * Output warning message.
	 * @param msg string message
	 * @return true on success, false on failure
	 */
	protected abstract boolean outputWarning(String msg);
	
}
