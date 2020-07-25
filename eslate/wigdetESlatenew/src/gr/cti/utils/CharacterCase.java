//Copyright:    Copyright (c) 1999-2000
//Company:      Computer Technology Institute

package gr.cti.utils;

/**
 * Greek case-insensitive comparison methods (and uppercase methods that strip-off punctuation marks)
 *
 * @version 2.0.0, 19-May-2006
 * @author George Birbilis [birbilis@cti.gr]
 */
public class CharacterCase {

 /**
  * A replacement for the method "char Character.toUppercase(char)" of Java
  * This one supports greek char and is useful for comparisons cause upper chars produced with this method aren't accented
  *
  * @param c the character to make upper
  * @return the uppercased character (with punctuation marks in Greek chars stripped-off)
  */

 public static char toUpperCase(char c){
        int p=greekLower.indexOf(c);
        if(p!=-1) c=greekUpper.charAt(p);
  return Character.toUpperCase(c); //convert Engish, and other language chars
 }


 /**
  * Converts a string into upper case, using the rules of the given locale,
  * applying correctly the rules for the greek locale (capital letters are
  * not accented in capitalized strings, terminal sigma is capitalized
  * correctly).
  *
  * @param      s       The string to capitalize.
  * @return     The capitalized string, or "" if s==null.
  */
 public static String upperCase(String s){
  if(s==null) return ""; //18May2000
  char[] sc = s.toCharArray();
  for (int i=0; i<sc.length; i++) sc[i]=toUpperCase(sc[i]);
  return new String(sc);
 }

 /**
  * Compares two strings, ignoring case (and ingnoring punctuation marks for Greek chars)
  *
  * @param s1 the first string (if s1=null it will be treated as if s1="")
  * @param s2 the second string (if s2=null it will be treated as if s2="")
  * @return     whether the capitalized versions (ignoring punctuation chars in Greek) of the strings are the same
  */
 public static boolean areEqualIgnoreCase(String s1,String s2){
  return upperCase(s1).equals(upperCase(s2)); //cause it calls upperCase which returns "" for null param, this one will treat null params as if they were ""
 }

 /**
  * Lower case letters in the Greek locale.
  */
 public static final String greekLower = "áÜâãäåÝæçÞèéßúÀêëìíîïüðñóòôõýûàö÷øùþ";

 /**
  * Equivalent upper case Greek letters in the Greek locale.
  */
 public static final String greekUpper = "ÁÁÂÃÄÅÅÆÇÇÈÉÉÚÚÊËÌÍÎÏÏÐÑÓÓÔÕÕÛÛÖ×ØÙÙ";

}

