import java.util.Scanner;
import java.io.*;


/**	This class parses the result files and summarizes each grade.
 *	Each line of output contains a CSE username, followed by its grade,
 *	separated by a comma. The output can be redirected to a file ending
 *	in ".csv", which can be opened by most spreadsheet applications. 
 *
 *	<p>Usage:
 *	<pre>
 *	java Grade <i>results_dir</i>
 *	</pre>
 *	
 *	For example:
 *	<pre>
 *	java Grade ./Results
 *	</pre       
 * 
 *	@author Steven J. Castellucci
 *	@version 1.1 - (11/2008)
 *	@version 1.2 - (09/2012): Added option to specify results directory
 *	@version 1.3 - (09/2014): Removed restriction of "cse" usernames
 *	COPYRIGHT (C) 2008-2014 All Rights Reserved.
 */
public class Grade_MC
{
	public static void main(String[] args) throws Exception
	{
		if (args.length == 0)
		{
			System.out.println("\nUsage: java Grade <results_dir>\n");
			System.exit(0);
		}
		
		File resultsDirectory = new File(args[0]);
		File files[] = resultsDirectory.listFiles();

		for (File f : files)
		{
			Scanner input = new Scanner(new FileInputStream(f));
			String login = f.getName().substring(0, f.getName().indexOf("."));
			input.nextLine();
			input.nextLine();
			input.nextLine();
			String name = input.nextLine();
			String line = "";
			boolean gradeFound = false;
			while (input.hasNextLine() && !gradeFound)
			{
				line = input.nextLine();
				if (line.startsWith("Grade:"))
				{
					gradeFound = true;
				}
			}
			if (gradeFound)
			{
				//String login = parseLogin(name);
				String grade = parseGrade(line);
				System.out.println(login + "," + grade);
			}
			else
			{
				System.out.println("Grade not found for " + f.getName());
			}
		}
	}

	private static String parseLogin(String line)
	{
		final int OFFSET = 2;
		int start = line.indexOf(":");
		int end = line.indexOf("Name");
		return line.substring(start+OFFSET, end).trim();
	}
	
	private static String parseGrade(String line)
	{
		final int OFFSET = 2;
		int start = line.indexOf(":");
		int end = line.indexOf("/");
		return line.substring(start+OFFSET, end);
	}
}