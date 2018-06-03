import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;


/**	This class generates the HTML code for the multiple choice portion of the
 *	test. It requires a test definition file, such as the following:
 *  
 *	<pre>
	3
	Suppose the following method exists in the class Test1 and there is
	an object reference of that class, called myObject.
	
	public static void foo(String s)
	
	What is the return type for that method?
	
	A. public
	B. static
	C. void
	D. String
	===
	C
	===
	How could you fix the following compilation error?
	
	cannot find symbol ToolBox
	
	A. Ask the TA for help.
	B. Change the file extension.
	C. Change the variable's name.
	D. Import the appropriate class.
	===
	D
	===
	Suppose the following method exists in the class Test1 and there is
	an object reference of that class, called myObject.
	
	public void foobar(double n)
	
	What casting will occur with the following code segment?
	
	int a = 1;
	double b = 2;
	myObject.foobar(a + b);
	
	A. The variables will be promoted and then added.
	B. The variables will be added and the sum promoted to a double.
	C. No casting will occur and a compilation error will result.
	D. The variable a will be promoted, but not b.
	===
	D

 *	</pre>
 *	 
 *	@author Steven J. Castellucci
 *	@version 1.0 - (10/2014)
 *	COPYRIGHT (C) 2014 All Rights Reserved.
 */
public class MultipleChoiceHTML
{
	public static void main(String[] args) throws IOException
	{
		if (args.length < 1)
		{
			System.out.println("\nUsage: java MultipleChoiceHTML <test_definition>\n");
			System.out.println("<test_definition> is the test definition file.");
			System.exit(1);
		}

		String version = args[0];
		PrintStream output = System.out;
		Scanner input = new Scanner(new File(version));
		MultipleChoiceHTMLQuestion[] test = parseDefFile(input);
		
		output.println("<h3>Multiple Choice Questions</h3>");
		output.println("<form action=\"./labtest1020.cgi\" method=\"post\" target=\"_blank\"");
		output.println("<input type=\"hidden\" name=\"testversion\" value=\"" + version + "\" />");
		output.println();
		for (int i = 0; i < test.length; i++)
		{
			printQuestion(output, test[i], i+1);
		}
		output.println("<div align=center><input type=\"submit\" value=\"Submit My Short Answer Questions\"/></div>");
		output.println("<br/>");
		output.println("<br/>");
		output.println("<hr/>");
	}
	
	private static void printQuestion(PrintStream output,
		MultipleChoiceHTMLQuestion q, int number)
	{
		output.println("<fieldset>");
		output.println("<legend>Question " + number + "</legend>");
		output.println(q.text);
		for (String c : q.choices)
		{
			char letter = c.charAt(0);
			output.println("<input type=\"radio\" name=\"" + number +
				"\" value=\"" + letter + "\"/> " + c);
		}
		output.println("<br/>");
		output.println("</fieldset>");
		output.println("<br/>");
	}
	
	private static MultipleChoiceHTMLQuestion[] parseDefFile(Scanner input)
	{
		int numQs = input.nextInt();
		input.nextLine(); // skip the rest of the line
		MultipleChoiceHTMLQuestion[] result = new MultipleChoiceHTMLQuestion[numQs];
		String line = "<br/>\n";
		while (input.hasNextLine())
		{
			line += input.nextLine() + "<br/>\n";
		}
		final String DELIM = "===";
		String[] part = line.split(DELIM);
		for (int i = 0; i < numQs; i++)
		{
			result[i] = parseQuestion(part[2 * i].trim());
		}
		return result;
	}
	
	private static MultipleChoiceHTMLQuestion parseQuestion(String text)
	{
		int endOfQuestion = text.indexOf("A.");
		String question = text.substring(0, endOfQuestion);
		Scanner choicesText = new Scanner(text.substring(endOfQuestion));
		Vector<String> choices = new Vector<String>();
		while (choicesText.hasNextLine())
		{
			choices.add(choicesText.nextLine());
		}
		return new MultipleChoiceHTMLQuestion(question,
			choices.toArray(new String[0]));
	}
}	

class MultipleChoiceHTMLQuestion
{
	public String text;
	public String[] choices;
	
	public MultipleChoiceHTMLQuestion(String text, String[] choices)
	{
		this.text = text;
		this.choices = choices;
	}
}
