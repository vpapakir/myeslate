package gr.cti.eslate.birbilis;

import javax.swing.*;
import java.awt.BorderLayout;

public class RunnerApplet extends JApplet
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
 BaseComponent base;

 public BaseComponent getBaseComponent(){ //11Jun1999
  return base;
 }

 public void setBaseComponent(BaseComponent base){
  this.base=base;
 }

 public void init(){
  getContentPane().setLayout(new BorderLayout()); //place in init
  getContentPane().add(base,BorderLayout.CENTER);
  //base.init();
 }

 public void start(){
  //base.start();
 }

 public void stop(){
  //base.stop();
 }

 public void destroy(){
  //base.destroy();
 }

 public String getAppletPath(){ //2-9-1998:adapted from one sent by K.Kyrimis
  try {return new java.io.File(getCodeBase().getFile()).getCanonicalPath();}
  catch (Exception e){return "";} //25-3-1999: catching all Exceptions and not only IOException, since when run as an application a null pointer exception is thrown (JDK1.2)
 }

}
