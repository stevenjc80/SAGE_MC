
/**	This class encapsulates a multiple choice question, including the
 *	question number, the question text and choices available, the
 *	answer given, and the correct answer. 
 *
 *	@author Steven J. Castellucci
 *	@version 1.0 - (10/2014)
 *	COPYRIGHT (C) 2014 All Rights Reserved.
 */
public class MultipleChoiceQuestion
{
	/**	The number or code that uniquely identifies this question. */
	public String number;

	/**	The text of this question. */
	public String text;

	/**	The letter or code corresponding to the student's answer. */
	public String given;

	/**	The letter or code corresponding to the correct answer. */
	public String correct;


	/**	Initializes this object. */
	public MultipleChoiceQuestion()
	{
		number = "";
		text = "";
		given = "";
		correct = "";
	}
	
	/**	Initializes this object with the passed values and an empty 'given'. */
	public MultipleChoiceQuestion(String number, String text, String correct)
	{
		this.number = number;
		this.text = text;
		this.given = "";
		this.correct = correct;
	}
	
	/**	Initializes this object using the state of the passed objects. */
	public MultipleChoiceQuestion(MultipleChoiceQuestion mcq, String answer)
	{
		number = mcq.number;
		text = mcq.text;
		given = answer;
		correct = mcq.correct;
	}
	
	public String toString()
	{
		String result = "Question " + number + ":\n" + text +
			"\n\nYour answer:    " + given +
			"\nCorrect answer: " + correct + "\n";
		return result;			
	}
}
