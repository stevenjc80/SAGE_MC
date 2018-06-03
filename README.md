________________________________________________________________________

SAGE_MC, Copyright (C) 2014, Steven J. Castellucci
________________________________________________________________________

SAGE_MC was written to automate the creation, recording, and grading of
multiple choice questions. These questions are meant to form all or part
of a computer science lab test. Each file in this project has its own
purpose, as listed below. Additional details are included in each file.


MC_QuestionsInput:  Defines the multiple choice test. For each question,
    it specifies the question text, possible answers, and the correct
    answer. This is a test definition file.

MultipleChoiceHTML.java:  Generates the HTML code for the multiple
    choice portion of the test. It takes a test definition file as
    input.

MC_GeneratedOutput.html:  The HTML code corresponding to the multiple
    choice portion of the test. It requires that "labtest1020.cgi" is on
    the web server, and is executable.

labtest1020.cgi:  The CGI script (written in Python) used to record
    students' submitted multiple choice responses. The script creates a
    response file with the student's username, and containing the
    student's responses.

MC_SampleResponse.txt:  A sample response file, containing a student's
    multiple choice test responses.

MultipleChoice.java:  Processes the submitted responses to a multiple
    choice test. Using the test definition file and students' response
    files, this class evaluates the correctness of each question, and
    generates a grade report for each student.

MultipleChoiceQuestion.java:  Encapsulates a multiple choice question,
    including the question number, text, choices available, answer
    given, and correct answer.

MultipleChoiceReport.java:  Encapsulates a text report based on a
    student's responses to a multiple choice test.

Grade_MC.java:  Parses each result file and outputs students' usernames,
    and grades, separated by commas. Each result file is summarized on a
    separate line.
