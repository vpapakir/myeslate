//Title:        Stage
//Version:      2Doc1999
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  base constraints' localization service

package gr.cti.eslate.stage.constraints.base;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ListResourceBundle;

public class MessagesBundle extends ListResourceBundle {

    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
     {"Unknown","<Unknown constraint>"},
     {"cantEnforce","Can't enforce constraint"},
     {"notAllowed","Not allowed by constraint "}, //have a space at the end
     //
     {"UnknownPointPoint","<Unknown Point-Point constraint>"},
     {"need2points","A Point-Point constraint needs two ControPoints"},
     //
     {"UnknownMasterSlave","<Unknown Master-Slave constraint>"},
     {"need2members","A Master-Slave constraint needs two members"}
        };

/////////////////

 private static ResourceBundle m;

 static{
  try{
   m = ResourceBundle.getBundle("gr.cti.eslate.stage.constraints.base.MessagesBundle", Locale.getDefault());
  }catch(/*MissingResource*/Exception e){ //catching all Exceptions cause in MS-JVM some "bad path" is thrown when a ResourceBundle is missing
   System.err.println("Couldn't find base contraint messages resource");
  } //if localized bundle is not found it shall load MessagesBundle.class
 }

 public static String[] localizeArray(String s){
  try{return m.getStringArray(s);}
  catch(Exception e){
   return null;
  }
 }

 public static String localize(String s){
  try{return m.getString(s);}
  catch(Exception e){
   //System.out.println("Couldn't localize "+s);
   return s;
  }
 }

}

