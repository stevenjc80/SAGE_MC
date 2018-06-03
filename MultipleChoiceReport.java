import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;


/**	This class encapsulates a report based on a student's responses to
 *	a multiple choice test.
 *	
 *	For example:
 *	<pre>
	Hello,

	Below, you will find the results of the multiple choice portion of
	your test. If you have any questions, send me an email.
	
	If you disagree with the marking of the test, you can ask for re-
	marking within one week of receiving this email. To do so, send me
	an email in which you explain where a mistake was made in marking
	your test. I will remark your test completely. As a consequence,
	your mark may increase, but it may also stay the same or even
	decrease.
	
	Regards,
	Steven
	
		
	Question 1:
	Suppose the following method exists in the class Test1 and there is
	an object reference of that class, called myObject.
	
	public static void foo(String s)
	
	What type of parameter does it take?
	
	A. public
	B. void
	C. static
	D. String
	
	Your answer:
	D
	Correct answer:
	D

	
	Question 2:
	A class is a collection of...
	
	A. state
	B. features
	C. identifiers
	D. relationships
	
	Your answer:
	C
	Correct answer:
	B
	
	
	Question 3:
	What is a benefit of declaring a constant?
	
	A. It helps document the significance of the value.
	B. It simplifies editing the code.
	C. It is required by our style guide.
	D. It is a combination of all three choices above.
	
	Your answer:
	D
	Correct answer:
	D

	
	Grade: 2/3
	
	
	There are tutors and study groups services available to students.
	Please see the course website for futher details.
	
 *	</pre>
 *
 *	@author Steven J. Castellucci
 *	@author Franck van Breugel
 *	@version 1.0 - (10/2014)
 *	COPYRIGHT (C) 2014 All Rights Reserved.
 */
public class MultipleChoiceReport
{
	/**	The static text that appears at the top of the report. */
	public static final String PREAMBLE = "Hello,\n\n" +
		"Below, you will find the results of the multiple choice portion of\n" +
		"your test. If you have any questions, send me an email.\n\n" +
		"If you disagree with the marking of the test, you can ask for re-\n" +
		"marking within one week of receiving this email. To do so, send me\n" +
		"an email in which you explain where a mistake was made in marking\n" +
		"your test. I will remark your test completely. As a consequence,\n" +
		"your mark may increase, but it may also stay the same or even\n" +
		"decrease.\n\n" +
		"Regards,\nSteven";

	/**	The static text that appears at the bottom of the report. */
	public static final String POSTAMBLE = "" +
		"There are tutors and study groups services available to students.\n" +
		"Please see the course website for futher details.";

	/**	The width of the header decorations. */
	protected static final int WIDTH = 68;

	/**	The student's EECS account name. */
	protected String studentLogin;

	/**	The test version. */
	protected String version;

	/**	An array of MultipleChoiceQuestions objects. Each encapsulates
	 *	the question asked, the choices available, the answer given
	 *	and the correct answer. */
	protected MultipleChoiceQuestion[] questions;


	/**	Initializes this object with the passed parameters.
	 *
	 *	@param studentLogin the student's EECS login.
	 *	@param questions the questions asked, and the responses given
	 */
	public MultipleChoiceReport(String studentLogin, String version,
		MultipleChoiceQuestion[] questions)
	{
		this.studentLogin = studentLogin;
		this.version = version;
		this.questions = questions;
	}


	/**	Wraps the OutputStream in a PrintWriter and calls
	 *	generate(PrintWriter report).
	 *
	 *	@param out the OutputStream to which to print the report
	 *	@return a summary of the generated report
	 */
	public String generate(OutputStream out)
	{
		PrintWriter report = new PrintWriter(out);
		return generate(report);
	}


	/**	Creates a file with the student's login as its name, wraps
	 *	it in a PrintWriter and calls generate(PrintWriter report).
	 *
	 *	@param path the desired location of the report file
	 *	@return a summary of the generated report
	 *	@throws IOException if unable to create file
	 */
	public String generate(String path) throws IOException
	{
		PrintWriter report = new PrintWriter(path, "UTF-8");
		return generate(report);
	}


	/**	Generates a report and writes it to the passed PrintWriter.
	 *
	 *	@param report the PrintWriter to which to write the report
	 *	@return a summary of the generated report
	 */
	public String generate(PrintWriter report)
	{
		int numPassed = 0;
		int numFailed = 0;

		report.println();
		report.println(PREAMBLE);
		report.println();
		report.println();
		report.println(repeatChar('-', WIDTH));

		for (MultipleChoiceQuestion mcq : questions)
		{
			report.println(mcq);
			report.println();
			report.println(repeatChar('-', WIDTH));
			if (mcq.correct.equals(mcq.given))
			{
				numPassed++;
			}
			else
			{
				numFailed++;
			}
		}

		report.println();
		report.println();
		report.println("Grade: " + numPassed + "/" + (numPassed + numFailed));
		report.println();
		report.println();
		report.println(repeatChar('-', WIDTH));
		report.println();
		report.println();
		report.println(POSTAMBLE);
		report.println();
		
		report.flush();
		report.close();

		return String.format("%-10s%-8s%-12s%-12s", studentLogin, version,
			"Passed: " + numPassed, "Failed: " + numFailed);
	}


	private String repeatChar(char c, int n)
	{
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++)
		{
			sb.append(c);
		}
		return sb.toString();
	}

}
