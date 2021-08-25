package gov.sandia.gmp.util.headerinfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class HeaderInfo
{

	ArrayList<String> javaHeader;
	
	ArrayList<String> cHeader;
	
	int nFilesModified;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

		try
		{
			HeaderInfo hi = new HeaderInfo();
			hi.run(args);
			System.out.printf("HeaderInfo modified %d files.%n", hi.nFilesModified);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	protected void run(String[] args) throws IOException
	{
		nFilesModified = 0;
		loadHeaders(args[0]);
		for (int i=1; i<args.length; ++i)
			replaceHeader(new File(args[i]));
	}

	private void replaceHeader(File dir) throws IOException
	{
		//System.out.println("Processing directory "+dir.getCanonicalPath());
		for (File f : dir.listFiles())
			if (f.isDirectory())
				replaceHeader(f);
			else if (f.isFile())
			{
				if (f.getName().endsWith(".java"))
					replace(f, "package ");
				else if (f.getName().endsWith(".c"))
					replace(f, "#include ");
				else if (f.getName().endsWith(".cc"))
					replace(f, "#ifndef ", "#include ");
				else if (f.getName().endsWith(".h"))
					replace(f, "#ifndef ", "#include ");
			}

	}

	private void replace(File f, String ... triggers) throws IOException
	{
		ArrayList<String> contents = new ArrayList<String>(1000);
		Scanner input = new Scanner(f);
		String line;
		boolean found = false;
		int linenum = 1;
		int nlines = 0;
		while (input.hasNext())
		{
			line = input.nextLine();
			if (!found)
				for (int j=0; j<triggers.length; ++j)
					if (line.startsWith(triggers[j]))
						found = true;

			if (found)
				contents.add(line);
			else
				++linenum;

			++nlines;
		}

		input.close();

		if (!found)
		System.out.printf("%6d / %6d  %s%n", linenum, nlines, f.getName());
		
		ArrayList<String> header = null;
		if (f.getName().endsWith(".java"))
			header = javaHeader;
		else
			header = cHeader;

		BufferedWriter output = new BufferedWriter(new FileWriter(f));
		for (int i=0; i<header.size(); ++i)
		{
			output.write(header.get(i));
			output.newLine();
		}
		for (int i=0; i<contents.size(); ++i)
		{
			output.write(contents.get(i));
			output.newLine();
		}
		++nFilesModified;
		output.close();
	}

	protected void loadHeaders(String file) throws IOException
	{
		ArrayList<String> header = new ArrayList<String>();
		Scanner input = new Scanner(new File(file));
		while (input.hasNext())
			header.add(input.nextLine());
		input.close();
		
		while (header.size() > 0 && header.get(0).trim().length()==0)
			header.remove(0);
		while (header.size() > 0 && header.get(header.size()-1).trim().length()==0)
			header.remove(header.size()-1);
		
		javaHeader = new ArrayList<String>(header.size()+2);
		
		javaHeader.add("/**");
		
		for (String line : header)
			javaHeader.add(" * "+line);
		
		javaHeader.add(" */");
		
		cHeader = new ArrayList<String>(header.size()+3);
		
		cHeader.add("//- ****************************************************************************");
		cHeader.add("//- ");
		
		for (String line : header)
			cHeader.add("//- "+line);
		
		cHeader.add("//-");
		cHeader.add("//- ****************************************************************************");
		cHeader.add("");
	}
}
