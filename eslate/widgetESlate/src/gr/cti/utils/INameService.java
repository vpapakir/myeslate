//Title:        INameService
//Version:      26Jul1999 (29Aug1999: revised "setName" method declaration)
//Copyright:    Copyright (c)1999
//Author:       George Birbilis
//Company:      CTI
//Description:  A naming service i/f that supports unique naming

package gr.cti.utils;

public interface INameService {

 ////////////////

 public class NameExistsException extends Exception
 {
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
   
  private String name;

  public NameExistsException(String name){
   super("Name '"+name+"' already exists!");
   this.name=name;
  }

  public String getConflictingName(){
   return name;
  }

 }

 ////////////////

 public boolean contains(Object obj); //17Sep1999

 public void put(String name, Object obj) throws NameExistsException;

 public void putNumbered(String name,Object obj);

 public void putUnique(String name,Object obj) throws NameExistsException;

 public Object get(String name);

 public String getName(Object obj);

 public void setName(Object obj,String name) throws NameExistsException; //29Aug1999: now throws NameExistsException

 public Object removeByName(String name);

 public Object removeByReference(Object obj);

 public Object[] getAllObjects(); //get all available objects

 public String[] getAllNames(); //get a names listing

 public static NameService globalNameService=new NameService();

}
