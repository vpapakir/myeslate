// updated @26/3/98 by G.Birbilis (added round_n & fractionDigits)
// @29/4/98: added to package gr.cti.eSlate.slider
// @29/9/98: changed package to gr.cti.eSlate.utils
//           renamed from myUtil to NumberUtil
//           made all methods public
//           fixed fractionDigits, added fraction and fract methods

package gr.cti.utils;

public class NumberUtil{

 public static double fraction(double x){
  return (x-(int)x);
 } //29-9-1998

 public static boolean isInteger(double x) {
  return (fraction(x)==0);
 } //29-9-1998: using fraction(x)

 public static double sqr(double x)
 {
  return x*x;
 }

 public double realRemainder(double x, double y)
 {
  return x-((int)(x/y))*y;
 }

 //Returns a double in a string with no fractional part if there is not one (intead of .0)
 static public String num2str(double x) {
  if (isInteger(x)) return Integer.toString((int)x);
  else return Double.toString(x);
   //return num2str((int)x)+'.'+num2str(fract(x)); //don't use fraction(x): would recurse till stack overflowed
 }

 //Same for integers (just for convinience)
 static public String num2str(int x) {return Integer.toString(x);}

 //Rounds a double up to n fractional digits
 static public double round_n(double x, byte n) {
  double precision=Math.pow(10,n);
  return Math.round(x*precision)/precision;
 }

 static public double round_n(double x, int n) { //added for convenience, since numbers set in the Java source code by the user are "int" by default 
  double precision=Math.pow(10,n);
  return Math.round(x*precision)/precision;
 }

 static public byte fractionDigits(double x) {
  if ((x-(int)x)==0) return 0;
  String s=Double.toString(x);
  return (byte)(s.length()-s.indexOf('.')-1);
 }

 //Returns the number of fractional digits of a number (5.0 returns 0)
/*
 static public int fractionDigits(double x) { //doesn't work: does endless loop as x seems to get infinite digits
  int count=0;
  x=fraction(x); //!!!
  while(!isInteger(x)){
   x*=10;count++; //place a breakpoint here and debug for x=1.43
  }
  return count;
 }
 
 static public int fract(double x){ //doesn't work: does endless loop as x seems to get infinite digits
  x=fraction(x); //!!!
  while(!isInteger(x)) 
   x*=10; //place a breakpoint here and debug for x=1.43
  return (int)x;
 }
*/

 static public int sign(double x){
  return (x>0)?1:((x<0)?-1:0);
 }

    /**
     * Converts an angle measured in degrees to the equivalent angle
     * measured in radians.
     *
     * @param   angdeg   an angle, in degrees
     * @return  the measurement of the angle <code>angdeg</code>
     *          in radians.
     * got from JDK1.2 java.lang.Math source
     */
    public static double toRadians(double angdeg) {
        return angdeg / 180.0 * Math.PI;
    }

    /**
     * Converts an angle measured in radians to the equivalent angle
     * measured in degrees.
     *
     * @param   angrad   an angle, in radians
     * @return  the measurement of the angle <code>angrad</code>
     *          in degrees.
     * got from JDK1.2 java.lang.Math source
     */
    public static double toDegrees(double angrad) {
        return angrad * 180.0 / Math.PI;
    }
}
