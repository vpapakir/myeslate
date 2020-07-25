//Title:        utilsBirb
//Version:
//Copyright:    Copyright (c) 1998
//Author:       George Birbilis
//Company:      CTI
//Description:  Trace

package gr.cti.utils;

public class Trace {

 public static void printStackTrace(){ //19Aug1999
  new Throwable().fillInStackTrace().printStackTrace();  //don't reuse one Throwable object, cause there is no way to dispose the stack trace from the throwable and release resources
 }

}