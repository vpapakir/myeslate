package gr.cti.eslate.eslateToolBar;

import java.util.*;
import java.awt.*;
import javax.swing.*;

import gr.cti.eslate.services.name.*;

/**
 * Object representing the tool bar's button group layout.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class ButtonGroupLayout implements Cloneable
{
  /**
   * Information about the button groups.
   */
  ButtonGroupEditorGroupInfoBaseArray groups =
    new ButtonGroupEditorGroupInfoBaseArray();
  /**
   * Information about the button that do not belong to a button group.
   */
  ButtonGroupEditorToolInfoBaseArray freeButtons =
    new ButtonGroupEditorToolInfoBaseArray();
  /**
   * NameServiceContext for managing group names;
   */
  NameServiceContext groupNames = new NameServiceContext();
  /**
   * NameServiceContext for managing tool names;
   */
  NameServiceContext toolNames = new NameServiceContext();
  /**
   * The toolbar whose button groups this instance represents.
   */
  ESlateToolBar toolBar;

  /**
   * Construct a ButtonGroupLayout instance.
   * @param     toolBar The toolbar whose button groups this instance will
   *                    represent.
   */
  ButtonGroupLayout(ESlateToolBar toolBar)
  {
    this.toolBar = toolBar;
    ButtonGroupBaseArray bGroups = toolBar.bGroups;
    int nGroups = bGroups.size();
    HashMap<AbstractButton, AbstractButton> map =
      new HashMap<AbstractButton, AbstractButton>();
    // Add description of button groups.
    for (int i=0; i<nGroups; i++) {
      ESlateButtonGroup group = bGroups.get(i);
      ButtonGroupEditorToolInfoBaseArray tools =
        new ButtonGroupEditorToolInfoBaseArray();
      int nTools = group.getButtonCount();
      for (int j=0; j<nTools; j++) {
        AbstractButton c = group.getButton(j);
        String toolName = toolBar.getESlateHandle(c).getComponentName();
        ButtonGroupEditorToolInfo tool = new ButtonGroupEditorToolInfo(
          toolName,
          c
        );
        tools.add(tool);
        try {
          toolNames.bind(toolName, tool);
        } catch (NameServiceException nse) {
        }
        map.put(c, c);
      }
      ButtonGroupEditorGroupInfo bInfo = new ButtonGroupEditorGroupInfo(
        group.name, tools
      );
      groups.add(bInfo);
      try {
        groupNames.bind(group.name, bInfo);
      } catch (NameServiceException nse) {
      }
    }
    // Add description of buttons that do not belong to a button group.
    Component[] tools = toolBar.getTools();
    int nTools = tools.length;
    for (int i=0; i<nTools; i++) {
      Component c = tools[i];
      if ((c instanceof AbstractButton) && !(map.containsKey(c))) {
        freeButtons.add(
          new ButtonGroupEditorToolInfo(
            toolBar.getESlateHandle(c).getComponentName(), (AbstractButton)c
          )
        );
      }
    }
    map.clear();
  }

  /**
   * Clone this object.
   * @return    A copy of this object.
   * @exception CloneNotSupportedException      Not thrown.
   */
  synchronized public Object clone() throws CloneNotSupportedException
  {
    ButtonGroupLayout bgl = (ButtonGroupLayout)(super.clone());
    bgl.groups = (ButtonGroupEditorGroupInfoBaseArray)(bgl.groups.clone());
    bgl.groupNames = new NameServiceContext();
    int nGroups = bgl.groups.size();
    for (int i=0; i<nGroups; i++) {
      try {
        ButtonGroupEditorGroupInfo info = bgl.groups.get(i);
        bgl.groupNames.bind(info.name, info);
      } catch(NameServiceException nse) {
      }
    }
    bgl.toolNames = new NameServiceContext();
    NameObjectPair[] list = toolNames.list();
    int nTools = list.length;
    for (int i=0; i<nTools; i++) {
      try {
        bgl.toolNames.bind(list[i].name, list[i].object);
      } catch(NameServiceException nse) {
      }
    }
    return bgl;
  }
}
