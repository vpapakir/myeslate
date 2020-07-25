package gr.cti.eslate.utils;

import java.io.*;
import java.util.*;

/**
 * This class obtains a stack trace with access to the individual components
 * of the trace.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class StackTrace
{
  /**
   * Buffer for obtaining the stack trace.
   */
  private ArrayList<String> trace = new ArrayList<String>();
  /**
   * buffer for appending characters from the stack trace as it is being
   * generated.
   */
  private StringBuffer sb = new StringBuffer();
  /**
   * The names of the invoked classes in the stack trace.
   */
  private String[] className;
  /**
   * The names of the invoked methods in the stack trace.
   */
  private String[] methodName;
  /**
   * The file names containing the invoked classes in the stack trace.
   */
  private String[] fileName;
  /**
   * The line numbers in the file names containing the invoked classes in the
   * stack trace.
   */
  private String[] lineNumber;
  /**
   * The depth of the stack trace.
   */
  private int depth;

  /**
   * Create a stack trace.
   */
  public StackTrace()
  {
    Throwable th = new Throwable();
    th.printStackTrace(new PrintStream(new OutputStream(){
        public void write(int b)
        {
          if (b != '\n') {
            sb.append((char)b);
          }else{
            trace.add(sb.toString());
            try {
              sb.delete(0, sb.length());
            } catch (StringIndexOutOfBoundsException e) {
            }
          }
        }
      }));
      int skip = 2;
      depth = trace.size() - skip;
      int end = trace.size() - 1;
      className = new String[depth];
      methodName = new String[depth];
      fileName = new String[depth];
      lineNumber = new String[depth];
      for (int i=skip; i<=end; i++) {
        String line = trace.get(i);
        StringTokenizer st = new StringTokenizer(line.substring(4), "()");
        String classMethod = st.nextToken();
        String fileLine = st.nextToken();
        st = new StringTokenizer(classMethod, ".");
        int j = i - skip;
        //className[j] = st.nextToken();
        //methodName[j] = st.nextToken();
        StringBuffer tmp = new StringBuffer();
        tmp.append(st.nextToken());
        int n = st.countTokens() - 1;
        for (int k=0; k<n; k++) {
          tmp.append('.');
          tmp.append(st.nextToken());
        }
        className[j] = tmp.toString();
        methodName[j] = st.nextToken();
        st = new StringTokenizer(fileLine, ":,");
        fileName[j] = st.nextToken();
        if (st.hasMoreTokens()) {
          lineNumber[j] = st.nextToken();
        }else{
          lineNumber[j] = " ";
        }
        String s = lineNumber[j];
        if (s.charAt(0) == ' ') {
          lineNumber[j] = s.substring(1);
        }
      }
    }

  /**
   * Returns the depth of the stack trace.
   * @return    The requested depth.
   */
  public int getDepth()
  {
    return depth;
  }

  /**
   * Returns the class name at a given depth of the stack trace.
   * @param     i       The depth.
   * @return    The requested name.
   */
  public String getClassName(int i)
  {
    return className[i];
  }

  /**
   * Returns the method name at a given depth of the stack trace.
   * @param     i       The depth.
   * @return    The requested name.
   */
  public String getMethodName(int i)
  {
    return methodName[i];
  }

  /**
   * Returns the file name at a given depth of the stack trace.
   * @param     i       The depth.
   * @return    The requested name.
   */
  public String getFileName(int i)
  {
    return fileName[i];
  }

  /**
   * Returns the line number at a given depth of the stack trace.
   * @param     i       The depth.
   * @return    The requested line number. This can be either a line number,
   *            if the method at that line is executed by the interpretter, or
   *            the string "Compiled code" if the method has been compiled
   *            into native code.
   */
  public String getLineNumber(int i)
  {
    return lineNumber[i];
  }

  /**
   * Prints a full stack trace.
   */
  public void print()
  {
    print(System.out);
  }

  /**
   * Prints a full stack trace.
   * @param     p       The stream on which to print the trace.
   */
  public void print(PrintStream p)
  {
    int length = trace.size();
    for (int i=0; i<length; i++) {
      p.println(trace.get(i));
    }
  }

/* Test the code.
  public static void main(String[] args)
  {
    doit();
  }

  static void doit()
  {
    StackTrace s = new StackTrace();
    int depth = s.getDepth();
    for (int i=0; i<depth; i++) {
      System.out.println(
        s.getClassName(i) + "\t" +
        s.getMethodName(i) + "\t" +
        s.getFileName(i) + "\t" +
        s.getLineNumber(i)
      );
    }
  }
*/
}
