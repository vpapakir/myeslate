package gr.cti.eslate.utils;


public class WorkareaFunctions {

  public native static String getWorkareaSize();

  static {
     try{
       System.loadLibrary("vcWorkarea");
     } catch (UnsatisfiedLinkError e){
        System.err.println("I can not load the visual c++ library");
        e.printStackTrace();
     }
   }  //end of loading the vc++ dll

   static {
     try{
       System.loadLibrary("vbWorkarea");
     } catch (UnsatisfiedLinkError e){
        System.err.println("I can not load the visual basic library");
        e.printStackTrace();
     }
   }  //end of loading the vc++ dll


   public static int[] getAvailableWorkareaSize() {
System.out.println("-----In WORKAREAFUNCTIONS,  calling work area functions..");
      String workareaSize=getWorkareaSize();
System.out.println("getting work area functions.. " + workareaSize);
      int prevIndex=0;
      int newIndex=0;
      int i=0;
      int[] screenCoordinates=new int[4];

System.out.println("calculating top, bottom, height,.... ");
      while (newIndex!=-1) {
           newIndex = workareaSize.indexOf(",", prevIndex);
           if (newIndex!=-1)
             screenCoordinates[i]=new Integer (workareaSize.substring(prevIndex, newIndex)).intValue();
           else
             screenCoordinates[i]=new Integer (workareaSize.substring(prevIndex, workareaSize.length())).intValue();
System.out.println("Inside " +  screenCoordinates[i]);
           i=i+1;
           prevIndex=newIndex+1;
      }
System.out.println("-----Exiting Workareafunctions ");
      return screenCoordinates;
    }

/*
   public static void main(String args[]) {

     System.out.println("Inside WorkareaFunctions...\n");

     int[] workareaSize= new int[4];
     workareaSize=getAvailableWorkareaSize();

     for (int j=0; j<workareaSize.length; j++)
         System.out.println("outside coordinate  ... : " + workareaSize[j]);

     System.out.println("BACK TO JAVA...  \n");

  }
*/
}