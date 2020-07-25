//Title:        Stage
//Version:      26Feb2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage

package gr.cti.eslate.stage.event;

import java.awt.geom.Point2D;
import gr.cti.eslate.stage.BaseObject;
import java.util.ArrayList;

public class BaseObjectMouseEventSupport {

 private ArrayList<BaseObjectMouseEventListener> listeners =
   new ArrayList<BaseObjectMouseEventListener>();

 public BaseObjectMouseEventSupport() {

 }

 public void addBaseObjectMouseEventListener(BaseObjectMouseEventListener listener){
  listeners.add(listener);
 }

 public void removeBaseObjectMouseEventListener(BaseObjectMouseEventListener listener){
  listeners.remove(listener);
 }

 //

 private BaseObjectMouseEvent makeEventObject(BaseObject source, double worldX, double worldY){
  Point2D location=source.getLocation2D();
  return new BaseObjectMouseEvent(
   source,
   worldX-location.getX(), //convert x from world-coord to source-relative-coord
   worldY-location.getY()  //convert y from world-coord to source-relative-coord
  );
 }


 public void fireBaseObjectMouseDragged(BaseObject source, double worldX, double worldY){
  if(listeners.size()==0) return;
  BaseObjectMouseEvent event=makeEventObject(source,worldX,worldY);
  for(int i=listeners.size();i-->0;)
   listeners.get(i).baseObjectMouseDragged(event);
 }

 public void fireBaseObjectMouseClicked(BaseObject source, double worldX, double worldY){
  if(listeners.size()==0) return;
  BaseObjectMouseEvent event=makeEventObject(source,worldX,worldY);
  for(int i=listeners.size();i-->0;)
   listeners.get(i).baseObjectMouseClicked(event);
 }

 public void fireBaseObjectMousePressed(BaseObject source, double worldX, double worldY){
  if(listeners.size()==0) return;
  BaseObjectMouseEvent event=makeEventObject(source,worldX,worldY);
  for(int i=listeners.size();i-->0;)
   listeners.get(i).baseObjectMousePressed(event);
 }

 public void fireBaseObjectMouseReleased(BaseObject source, double worldX, double worldY){
  if(listeners.size()==0) return;
  BaseObjectMouseEvent event=makeEventObject(source,worldX,worldY);
  for(int i=listeners.size();i-->0;)
   listeners.get(i).baseObjectMouseReleased(event);
 }


}
