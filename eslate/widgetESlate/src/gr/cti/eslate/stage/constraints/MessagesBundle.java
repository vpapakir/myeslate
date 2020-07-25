//Title:        Stage
//Version:      18May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  base constraints' localization service

package gr.cti.eslate.stage.constraints;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ListResourceBundle;

public class MessagesBundle extends ListResourceBundle {

    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
     {"PointsArithmeticMedianPoint","points' arithmetic median point"}, //19Apr2000
     {"PointsGeometricMedianPoint","points' geometric median point"}, //19Apr2000
     {"PointOntoPoint","point onto point"},
     {"PointIntoShape","point into shape"},
     {"FixedPointOffsetFromPoint","fixed point offset from point"},
     {"FixedPointDistanceFromPoint","fixed point distance from point"},
     {"MinPointDistanceFromPoint","minimum point distance from point"},
     {"MaxPointDistanceFromPoint","maximum point distance from point"}
        };

/////////////////

 private static ResourceBundle m;

 static{
  try{
   m = ResourceBundle.getBundle("gr.cti.eslate.stage.constraints.MessagesBundle", Locale.getDefault());
  }catch(/*MissingResource*/Exception e){ //catching all Exceptions cause in MS-JVM some "bad path" is thrown when a ResourceBundle is missing
   System.err.println("Couldn't find contraint messages resource");
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

