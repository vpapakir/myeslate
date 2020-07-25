//Title:        utilsBirb
//Version:      15May2000 
//Copyright:    Copyright (c) 2000
//Author:       George Birbilis
//Company:      CTI
//Description:  My utilities

package gr.cti.utils;

import java.io.*;

public class Cloning {

 public static byte[] storeObject(Object o){
  //make buffer//
  ByteArrayOutputStream bos=new ByteArrayOutputStream();
  ObjectOutputStream oos=null;
  byte[] state=null;
  //persist//
  try{
   oos=new ObjectOutputStream(bos);
   oos.writeObject(o);
   state=bos.toByteArray();
  }catch(IOException ex){
   ex.printStackTrace();
  }finally{
   //free buffer//
   try{oos.close();}catch(IOException e){};
   try{bos.close();}catch(IOException e){};
  }
  //return saved state (or null if failed)//
  return state;
 }

 public static Object restoreObject(byte[] state){
  ObjectInputStream ois=null;
  Object o=null;
  try{
   ois=new ObjectInputStream(new ByteArrayInputStream(state));
   o=ois.readObject(); //construct a new instatnce from the saved state
  }catch(Exception ex){
   ex.printStackTrace();
  }finally{
   try{ois.close();}catch(IOException ex){}
  }
  return o;
 }

 public static Object cloneObject(Object o){
  byte[] state=storeObject(o);
  return restoreObject(state);
 }

}