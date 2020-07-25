package sort;

import java.util.*;
import gr.cti.typeArray.*;
import com.objectspace.jgl.*;

public class sort
{
  public static int N;

  public static void main(String[] args)
  {
    if (args.length > 0) {
      N = Integer.valueOf(args[0]).intValue();
    }else{
      N = 1024;
    }
    System.out.println(N + " elements:");
    sortVector();
    sortArray();
    sortIntegerBaseArray();
    sortIntBaseArray();
  }

  //@SuppressWarnings(value={"unchecked"})
  static void sortVector()
  {
    Vector v = new Vector(N);
    for (int i=N; i>=0; i--) {
      v.addElement(new Integer(i));
    }
    int limit1 = N - 1;
    long t1 = System.currentTimeMillis();
    for (int i=0; i<limit1; i++) {
      for (int j=i+1; j<N; j++) {
        Integer n1 = (Integer)(v.elementAt(i));
        Integer n2 = (Integer)(v.elementAt(j));
        if (n1.intValue() > n2.intValue()) {
          v.setElementAt(n2, i);
          v.setElementAt(n1, j);
        }
      }
    }
    long t2 = System.currentTimeMillis();
    System.out.println("Vector: " + (t2 - t1) + " msec");
  }

  static void sortArray()
  {
    Array v = new Array();
    v.ensureCapacity(N);
    for (int i=N; i>=0; i--) {
      v.add(new Integer(i));
    }
    int limit1 = N - 1;
    long t1 = System.currentTimeMillis();
    for (int i=0; i<limit1; i++) {
      for (int j=i+1; j<N; j++) {
        Integer n1 = (Integer)(v.at(i));
        Integer n2 = (Integer)(v.at(j));
        if (n1.intValue() > n2.intValue()) {
          v.put(i,n2);
          v.put(i,n1);
        }
      }
    }
    long t2 = System.currentTimeMillis();
    System.out.println("Array: " + (t2 - t1) + " msec");
  }

  static void sortIntegerBaseArray()
  {
    IntegerBaseArray v = new IntegerBaseArray(N);
    for (int i=N; i>=0; i--) {
      v.add(new Integer(i));
    }
    int limit1 = N - 1;
    long t1 = System.currentTimeMillis();
    for (int i=0; i<limit1; i++) {
      for (int j=i+1; j<N; j++) {
        Integer n1 = v.get(i);
        Integer n2 = v.get(j);
        if (n1.intValue() > n2.intValue()) {
          v.set(i, n2);
          v.set(i, n1);
        }
      }
    }
    long t2 = System.currentTimeMillis();
    System.out.println("IntegerBaseArray: " + (t2 - t1) + " msec");
  }

  static void sortIntBaseArray()
  {
    IntBaseArray v = new IntBaseArray(N);
    for (int i=N; i>=0; i--) {
      v.add(i);
    }
    int limit1 = N - 1;
    long t1 = System.currentTimeMillis();
    for (int i=0; i<limit1; i++) {
      for (int j=i+1; j<N; j++) {
        int n1 = v.get(i);
        int n2 = v.get(j);
        if (n1 > n2) {
          v.set(i, n2);
          v.set(i, n1);
        }
      }
    }
    long t2 = System.currentTimeMillis();
    System.out.println("IntBaseArray: " + (t2 - t1) + " msec");
  }
}
