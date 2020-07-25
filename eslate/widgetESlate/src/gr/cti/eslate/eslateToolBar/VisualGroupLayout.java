package gr.cti.eslate.eslateToolBar;

import java.awt.*;

import gr.cti.eslate.services.name.*;

/**
 * Object representing the tool bar's visual group layout.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class VisualGroupLayout implements Cloneable
{
  /**
   * Information about the visual groups.
   */
  VisualGroupEditorGroupInfoBaseArray groups =
    new VisualGroupEditorGroupInfoBaseArray();
  /**
   * NameServiceContext for managing group names;
   */
  NameServiceContext groupNames = new NameServiceContext();
  /**
   * NameServiceContext for managing tool names;
   */
  NameServiceContext toolNames = new NameServiceContext();
  /**
   * The toolbar whose visual groups this instance represents.
   */
  ESlateToolBar toolBar;

  /**
   * Construct a VisualGroupLayout instance.
   * @param     toolBar The toolbar whose visual groups this instance will
   *                    represent.
   */
  VisualGroupLayout(ESlateToolBar toolBar)
  {
    this.toolBar = toolBar;
    VisualGroupBaseArray vGroups = toolBar.vGroups;
    int nGroups = vGroups.size();
    for (int i=0; i<nGroups; i++) {
      VisualGroup group = vGroups.get(i);
      VisualGroupEditorToolInfoBaseArray tools =
        new VisualGroupEditorToolInfoBaseArray();
      int nTools = group.getComponentCount();
      for (int j=0; j<nTools; j++) {
        Component c = group.getComponent(j);
        String toolName = toolBar.getESlateHandle(c).getComponentName();
        VisualGroupEditorToolInfo tool = new VisualGroupEditorToolInfo(
          toolName,
          c,
          toolBar.getAssociatedText(c),
          toolBar.isVisible(c)
        );
        tools.add(tool);
        try {
          toolNames.bind(toolName, tool);
        } catch (NameServiceException nse) {
        }
      }
      VisualGroupEditorGroupInfo vInfo = new VisualGroupEditorGroupInfo(
        group.name, group.visible, tools
      );
      groups.add(vInfo);
      try {
        groupNames.bind(group.name, vInfo);
      } catch (NameServiceException nse) {
      }
    }
  }

  /**
   * Clone this object.
   * @return    A copy of this object.
   * @exception CloneNotSupportedException      Not thrown.
   */
  synchronized public Object clone() throws CloneNotSupportedException
  {
    VisualGroupLayout vgl = (VisualGroupLayout)(super.clone());
    vgl.groups = (VisualGroupEditorGroupInfoBaseArray)(vgl.groups.clone());
    vgl.groupNames = new NameServiceContext();
    int nGroups = vgl.groups.size();
    for (int i=0; i<nGroups; i++) {
      try {
        VisualGroupEditorGroupInfo info = vgl.groups.get(i);
        vgl.groupNames.bind(info.name, info);
      } catch(NameServiceException nse) {
      }
    }
    vgl.toolNames = new NameServiceContext();
    NameObjectPair[] list = toolNames.list();
    int nTools = list.length;
    for (int i=0; i<nTools; i++) {
      try {
        vgl.toolNames.bind(list[i].name, list[i].object);
      } catch(NameServiceException nse) {
      }
    }
    return vgl;
  }
}
