#!/cs/local/bin/python

# Creates a file that contains a user's responses to a multiple choice test.
# The contents of such a file are as follows:
#     {'testversion': 'test1TMC', '1': 'C', '3': 'D', '2': 'A', '5': 'B', '4': 'A'} 

import cgi
import os
import errno
import cgitb
cgitb.enable()

def mkdirs(newdir, mode=0770):
   try: os.makedirs(newdir, mode)
   except OSError, err:
      # Reraise the error unless it's about an already existing directory
      if err.errno != errno.EEXIST or not os.path.isdir(newdir):
         raise

def cgiFieldStorageToDict( fieldStorage ):
    """Get a plain dictionary, rather than the '.value' system used by the cgi module."""
    params = {}
    for key in fieldStorage.keys():
        params[ key ] = fieldStorage[ key ].value
    return params

if __name__ == "__main__":
    dict = cgiFieldStorageToDict( cgi.FieldStorage() )
    print "Content-Type: text/plain"
    print

    # ensure login information is available
    if not 'REMOTE_USER' in os.environ:
        print 'Secure login required; did you forget to run mkhtaccess?'
    elif not dict:
        print 'Submission failed; your previous submission will be saved'
    else:
       try:
          user = os.environ['REMOTE_USER']
          submitdir = '/eecs/dept/course/2015-16/F/1030/mcTests/Test1/' + user
          mkdirs(submitdir)
          os.chmod(submitdir, 0770)
          submitfile = submitdir + '/submit.txt'
          file_object = open(submitfile, 'w')
          os.chmod(submitfile, 0660)

          # save submitted questions as a dictionary
          print >> file_object, dict

          file_object.close()

          # echo back the submitted file
          print 'Your submitted answers:'
          print
          F = open(submitfile)
          submitted = F.readline()
          subdict = eval(submitted)
          keys = subdict.keys()
          keys.sort()
          for k in keys:
             if k == "testversion":
                continue
             print k, subdict[k]
             print
          F.close()

       except EnvironmentError, (ErrorNumber, ErrorMessage):
          print 'ERROR SUBMITTING TEST: DO NOT CLOSE THE WEB BROWSER!'
          print 'Please inform the teaching assistant'
          print
          print 'Exception error message:'
          print ErrorMessage
          
