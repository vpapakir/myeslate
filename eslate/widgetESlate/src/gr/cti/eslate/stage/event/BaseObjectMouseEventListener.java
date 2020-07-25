//Title:        Stage
//Version:      26Feb2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage

package gr.cti.eslate.stage.event;

import java.util.EventListener;

public interface BaseObjectMouseEventListener extends EventListener {

 public void baseObjectMouseDragged(BaseObjectMouseEvent e);
 public void baseObjectMouseClicked(BaseObjectMouseEvent e);
 public void baseObjectMousePressed(BaseObjectMouseEvent e);
 public void baseObjectMouseReleased(BaseObjectMouseEvent e);

} 
