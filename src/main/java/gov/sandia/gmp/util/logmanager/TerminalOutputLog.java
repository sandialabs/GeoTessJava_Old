// Module:        $RCSfile: TerminalOutputLog.java,v $
// Revision:      $Revision: 1.1 $
// Last Modified: $Date: 2008/10/29 17:33:05 $
// Last Check-in: $Author: mchang $

package gov.sandia.gmp.util.logmanager;

public class TerminalOutputLog extends AbstractLogOutput {

	
	/**
	 * Create a terminal output log which will print messages to the
	 * terminal window.  Note that all messages are output to System.err
	 * in order to avoid timing issues when mixing output with System.out.
	 * 
	 * @param modulePrefix a string prefix to prepend to output messages (typically a module name)
	 */
	public TerminalOutputLog(String modulePrefix) {
		if(modulePrefix != null)
			prefix = modulePrefix;
	}
	
	/**
	 * Output error message to screen.
	 */
	@Override
	public boolean outputError(String msg) {
		System.err.println(newLine + "-"
				+ prefix + " Error-" + newLine + msg);
		return true;
	}

	/**
	 * Output status message to screen.
	 */
	@Override
	public boolean outputStatus(String msg) {
		System.err.println(newLine + "-"
				+ prefix + " Status-" + newLine + msg);
		return true;
	}

	/**
	 * Output warning message to screen.
	 */
	@Override
	public boolean outputWarning(String msg) {
		System.err.println(newLine + "-"
				+ prefix + " Warning-" + newLine + msg);
		return true;
	}


	/**
	 * Output error message and stack trace to screen.
	 */
	@Override
	public boolean outputError(Exception exception) {
		// output message and stack trace
		outputError(exception.getMessage());
		outputExceptionStackTrace(exception);
		return false;
	}

	/**
	 * Output status message and stack trace to screen.
	 */
	@Override
	public boolean outputStatus(Exception exception) {
		// output message and stack trace
		outputStatus(exception.getMessage());
		outputExceptionStackTrace(exception);
		return false;
	}

	/**
	 * Output warning message and stack trace to screen.
	 */
	@Override
	public boolean outputWarning(Exception exception) {
		// output message and stack trace
		outputWarning(exception.getMessage());
		outputExceptionStackTrace(exception);
		return false;
	}

	/**
	 * Output exception's stack trace to screen.
	 * 
	 * @param exception exception
	 */
	private void outputExceptionStackTrace(Exception exception) {
		exception.printStackTrace();
	}



}
