import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.TreeMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;


/**	This class facilitates the grading of web-based multiple choice questions.
 *	Submissions are processed in a batch, using a thread pool. Submission
 *	directories to be skipped should contain a dot in the name (e.g., mine.old). 
 *	
 *	The submission directories (one per student) each contain a file called
 *	"submit.txt" and has the following contents: 
 *
 *	<pre>
	{'testversion': 'test1A', '1': 'A', '2': 'B', '3': 'C', '4': 'D', '5': 'A'}
	
 *	</pre>    
 *  
 *	The test version specifies the test definition file, which contains the
 *	questions, choices, and correct answers for the test. The test definition
 *	file starts with the number of questions and has the following contents:
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
public class MultipleChoice implements Runnable
{
	/**	The name of the directory (created in the current working directory)
	 *	to store the feedback file for each student. */	
	public static final String DEFAULT_RESULTS_DIR = "Results";

	/**	The name of the directory containing the test definition files. These
	 *	files contain the questions and answers for a test or version. */	
	public static final String DEFAULT_VERSIONS_DIR = ".";

	/**	The directory containing all of the students' submissions. Each
	 *	student's submission should be in a subdirectory with his/her login as
	 *	the directory name. */
	protected static File submitDir;

	/**	The directory that will contain the testing reports. */
	protected static String resultsDir;

	/**	The directory containing the test definition files. These files
	 *	contain the questions and answers for a test or version. */
	protected static String testsDir;

	/**	The login of the student represented by this object. */
	protected String student;

	/**	The file (buffered) containing the test version and answers. */
	protected Scanner file;
	
	/**	The collection of tests. The key is the test version and the value is
	 *	a sorted map of questions, keyed with the question number. */
	protected static ConcurrentHashMap<String,
		TreeMap<String, MultipleChoiceQuestion>> versions;


	/**	Initializes this object.
	 *	@param student the login of the student represented by this object.
	 *	@param file the file containing the test version and answers.
	 */	 
	public MultipleChoice(String student, Scanner file)
	{
		this.student = student;
		this.file = file;
		if (versions == null)
		{
			versions = new ConcurrentHashMap<String,
				TreeMap<String, MultipleChoiceQuestion>>();
		}
	}
	
	@Override
	public void run()
	{
		// Read test version
		String line = file.nextLine();
		String version = getValue(line, "testversion");
		TreeMap<String, MultipleChoiceQuestion> test = versions.get(version);
		if (test == null) // 1st time, read from file
		{
			test = readTest(version);
			versions.put(version, test);
		}
		
		// Combine questions with given answers
		int i = 0;
		MultipleChoiceQuestion[] q = new MultipleChoiceQuestion[test.size()];
		for (String key : test.keySet())
		{
			MultipleChoiceQuestion mcq = test.get(key);
			String answer = getValue(line, mcq.number);
			q[i++] = new MultipleChoiceQuestion(mcq, answer);
		}
		
		// Generate report
		MultipleChoiceReport report = new MultipleChoiceReport(
			student, version, q);
		String resultFile = resultsDir + File.separator + student + ".txt";
		try
		{
			System.out.println(report.generate(resultFile));
		}
		catch (IOException ioe)
		{
			System.err.println("Unable to create result file: " +
				resultsDir + File.separator + student + ".txt");
		}
	}
	
	private TreeMap<String, MultipleChoiceQuestion> readTest(String version)
	{
		TreeMap<String, MultipleChoiceQuestion> questions = new TreeMap<String,
			MultipleChoiceQuestion>();
		try
		{
			Scanner input = new Scanner(new FileInputStream(
				testsDir + File.separator + version));
			int numberOfQuestions = input.nextInt();
			input.nextLine(); // ignore rest of line
			String line = "";
			while (input.hasNextLine())
			{
				line += input.nextLine() + "\n";
			}
			final String DELIM = "===";
			String[] part = line.split(DELIM);
			for (int i = 0; i < numberOfQuestions; i++)
			{
				questions.put("" + i, new MultipleChoiceQuestion(
					"" + (i+1),                // number
					part[2 * i].trim(),        // text
					part[2 * i + 1].trim()));  // correct answer
			}
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Cannot find version file: " +
				testsDir + File.separator + version);
		}
		return questions;
	}

    private String getValue(String line, String key)
	{
		// for test version, key = testversion
		// for test answer, key = questionNumber
		String result = null;
		try
		{
			String pattern = "'" + key + "': '";
			int start = line.indexOf(pattern) + pattern.length();
			int end = line.indexOf("'", start+1);
			result = line.substring(start, end);
		}
		catch (IndexOutOfBoundsException ioobe)
		{
			System.out.println(
				"Missing key-value pair in submission from: " + student);
		}
		return result;
    }
    
	public static void main(String[] args) throws FileNotFoundException
	{
		final int NUM_THREADS = 5;
		if (args.length < 1)
		{
			System.out.println("\nUsage: java MultipleChoice <submit> [-t <tests>] [-r <results>] [-s <cse_dir>]\n");
			System.out.println("<submit> is the directory containing all the submissions.");
			System.out.println("-t <tests> to override the default test version directory with <tests>.");
			System.out.println("-r <results> to override the default results directory with <results>.");
			System.out.println("-s <cse_dir> to test only the individual submission listed.\n");
			System.exit(1);
		}

		submitDir = new File(args[0]);
		String[] students = submitDir.list();
		resultsDir = DEFAULT_RESULTS_DIR;
		testsDir = DEFAULT_VERSIONS_DIR;
		for (int i = 1; i < args.length; i = i+2)
		{
			if (args[i].equals("-r"))
			{
				resultsDir = args[i+1];
			}
			else if (args[i].equals("-s"))
			{
				students = new String[1];
				students[0] = args[i+1];
			}
			else if (args[i].equals("-t"))
			{
				testsDir = args[i+1];
			}
		}
		System.out.println();
		System.out.println("Processing...");
		System.out.println("Current time: "+(new Date()));

		// Create results directory.
		File rd = new File(resultsDir);
		try
		{
			rd.mkdir();
		}
		catch (SecurityException se) {}
		if (rd.exists() && rd.isDirectory() && rd.canWrite())
		{
			System.out.println("Results in "+resultsDir+File.separator);
		}
		else
		{
			System.out.println("Error: Could not create results directory!");
			return;
		}
		System.out.println();

		// Test each student's submission.
		ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
		for (String student : students)
		{
			if (student.indexOf(".") < 0) // ignore e.g., mine.old
			{
				File submitFile = new File(submitDir + File.separator +
					student + File.separator + "submit.txt");
				if (submitFile.exists())
				{
					Runnable worker = new MultipleChoice(student,
						new Scanner(submitFile));
					executor.execute(worker);
				}
			}
		}
		executor.shutdown();
		while (!executor.isTerminated()) {}
		System.out.println();
		System.out.println("Process completed successfully.");
		System.out.println("Current time: " + (new Date()));
    }
}
