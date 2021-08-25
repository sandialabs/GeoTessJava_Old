package gov.sandia.gmp.util.globals;

/**
 * @author sballar
 * 
 */
public enum OSType
{
	WINDOWS, MACOSX, SUNOS, LINUX,  UNIX, UNRECOGNIZED;
	
	public String toString()
	{
		switch (this)
		{
		case WINDOWS:
			return "Windows";
		case MACOSX:
			return "MacOSX";
		case SUNOS:
			return "SunOS";
		case LINUX:
			return "Linux";
		case UNIX:
			return "Unix";
		default:
			return "Unrecognized";
		}
	}
	
	/**
	 * <ul>
	 * Return the operating system on which Utils is currently running:
	 * <li>OS.WINDOWS
	 * <li>OS.MAC
	 * <li>OS.UNIX
	 * <li>OS.LINUX
	 * <li>OS.UNRECOGNIZED
	 * </ul>
	 * <p>
	 * Here is a pretty comprehensive list of possible os.name values:
	 * http://lopica.sourceforge.net/os.html
	 * 
	 * @return current operating system
	 */
	public static OSType getOS()
	{
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win"))
			return OSType.WINDOWS;
		else if (os.contains("mac"))
			return OSType.MACOSX;
		else if (os.contains("linux"))
			return OSType.LINUX;
		else if (os.contains("unix"))
			return OSType.UNIX;
		else if (os.contains("sun") || os.contains("solaris"))
			return OSType.SUNOS;

		return OSType.UNRECOGNIZED;
	}

}

