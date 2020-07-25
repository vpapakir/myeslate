package gr.cti.eslate.database;

/**
 * <p>Title: Database</p>
 * <p>Description: Your description</p>
 * <p>Copyright: Copyright (c) 1999</p>
 * <p>Company: </p>
 * @author George Tsironis
 * @version
 */
import gr.cti.eslate.base.ESlateAdapter;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ParentChangedEvent;
import gr.cti.eslate.eslateButton.ESlateButton;
import gr.cti.eslate.eslateLabel.ESlateLabel;
import gr.cti.eslate.eslateTextField.ESlateTextField;
import gr.cti.eslate.eslateToolBar.DefaultStateSetter;
import gr.cti.eslate.eslateToolBar.ESlateToolBar;
import gr.cti.eslate.eslateToolBar.ToolLocation;
import gr.cti.eslate.eslateToolBar.VisualGroup;
import gr.cti.eslate.utils.ESlateOptionPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;

public class StatusToolbarController implements DefaultStateSetter{

  Database dBase;
  ResourceBundle infoBundle;
  String errorStr;
  ESlateToolBar toolbar;
//  Font statusBarFont = new Font("Helvetica", Font.PLAIN, 12);
//  FontMetrics helvPlain12Fm;

  private ESlateLabel messageLabel, numOfRecordsLabel;
  private ESlateButton prevRecord, nextRecord, lastRecord, firstRecord, prevTable, nextTable;
  private ESlateTextField recNumberBox;


  public StatusToolbarController(Database dB) {
      dBase = dB;
      infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", Locale.getDefault());
      errorStr = infoBundle.getString("Error");
//      statusBarFont = new Font("Helvetica", Font.PLAIN, 12);
//      helvPlain12Fm =Toolkit.getDefaultToolkit().getFontMetrics(statusBarFont);
  }

  public void createToolbar(){
      toolbar = new ESlateToolBar("Status");
      toolbar.setDefaultStateSetter(this);
      setDefaultState(toolbar);
  }

  public void setToolbar(ESlateToolBar bar){
      toolbar = bar;
      toolbar.setDefaultStateSetter(this);
      toolbar.setFloatable(false);
      toolbar.setBorder(new EtchedBorder(EtchedBorder.RAISED));
      toolbar.setBackground(new Color(96, 103, 144));
      toolbar.setCentered(false);
      toolbar.setSeparation(new Dimension(4, 4));
      toolbar.setLeadingSeparation(new Dimension(3, 3));

      Dimension statusBarSize = new Dimension(10, 20);
      toolbar.setMaximumSize(statusBarSize);
      toolbar.setMinimumSize(statusBarSize);
      toolbar.setPreferredSize(statusBarSize);

      toolbar.getESlateHandle().addESlateListener(new ESlateAdapter(){
          public void parentChanged(ParentChangedEvent e){
              ESlateHandle oldParent = e.getOldParent();
              ESlateHandle newParent = e.getNewParent();
              if (dBase.getESlateHandle().equals(oldParent) && !dBase.getESlateHandle().equals(newParent)){
                  toolbar = null;
              }
          }
      });
  }
  
  void restoreOld() {
      //This restores only old saves and does nothing in new ones that use getToolLocations
      if (toolbar.getTool("Message") != null){
          messageLabel = (ESlateLabel)toolbar.getTool("Message");
      }
      if (toolbar.getTool("Previous Table") != null){
          prevTable = (ESlateButton)toolbar.getTool("Previous Table");
      }
      if (toolbar.getTool("Next") != null){
          nextTable = (ESlateButton)toolbar.getTool("Next");
      }
      if (toolbar.getTool("Record Number") != null){
          recNumberBox = (ESlateTextField)toolbar.getTool("Record Number");
      }
      if (toolbar.getTool("Number of Records") != null){
          numOfRecordsLabel = (ESlateLabel)toolbar.getTool("Number of Records");
      }
      if (toolbar.getTool("First Record") != null){
          firstRecord = (ESlateButton)toolbar.getTool("First Record");
      }
      if (toolbar.getTool("Previous Record") != null){
          prevRecord = (ESlateButton)toolbar.getTool("Previous Record");
      }
      if (toolbar.getTool("Next Record") != null){
          nextRecord = (ESlateButton)toolbar.getTool("Next Record");
      }
      if (toolbar.getTool("Last Record") != null){
          lastRecord = (ESlateButton)toolbar.getTool("Last Record");
      }
  }

  /**
   * Identifies the tools in a toolbar that has just been loaded, updating
   * the references to these tools.
   * @param     loc     The locations of the tools in the toolbar.
   */
  void identifyTools(ToolLocation[] loc) {
		VisualGroup[] groups=toolbar.getVisualGroups();
		if (loc[0]!=null) {
			messageLabel=(ESlateLabel) (groups[loc[0].visualGroup].getComponent(loc[0].toolIndex));
		} else {
			messageLabel=null;
		}
		if (loc[1]!=null) {
			prevTable=(ESlateButton) (groups[loc[1].visualGroup].getComponent(loc[1].toolIndex));
		} else {
			prevTable=null;
		}
		if (loc[2]!=null) {
			nextTable=(ESlateButton) (groups[loc[2].visualGroup].getComponent(loc[2].toolIndex));
		} else {
			nextTable=null;
		}
		if (loc[3]!=null) {
			recNumberBox=(ESlateTextField) (groups[loc[3].visualGroup].getComponent(loc[3].toolIndex));
		} else {
			recNumberBox=null;
		}
		if (loc[4]!=null) {
			numOfRecordsLabel=(ESlateLabel) (groups[loc[4].visualGroup].getComponent(loc[4].toolIndex));
		} else {
			numOfRecordsLabel=null;
		}
		if (loc[5]!=null) {
			firstRecord=(ESlateButton) (groups[loc[5].visualGroup].getComponent(loc[5].toolIndex));
		} else {
			firstRecord=null;
		}
		if (loc[6]!=null) {
			prevRecord=(ESlateButton) (groups[loc[6].visualGroup].getComponent(loc[6].toolIndex));
		} else {
			prevRecord=null;
		}
		if (loc[7]!=null) {
			nextRecord=(ESlateButton) (groups[loc[7].visualGroup].getComponent(loc[7].toolIndex));
		} else {
			nextRecord=null;
		}
		if (loc[8]!=null) {
			lastRecord=(ESlateButton) (groups[loc[8].visualGroup].getComponent(loc[8].toolIndex));
		} else {
			lastRecord=null;
		}

	}
  
  /**
	 * Returns an array containing the current locations of the tools in the toolbar.
	 * 
	 * @return An array containing the current locations of the tools in the toolbar.
	 */
  ToolLocation[] getToolLocations() {
		int nTools=9;
		ToolLocation loc[]=new ToolLocation[nTools];
		loc[0]=toolbar.locateTool(messageLabel);
  		loc[1]=toolbar.locateTool(prevTable);
  		loc[2]=toolbar.locateTool(nextTable);
  		loc[3]=toolbar.locateTool(recNumberBox);
  		loc[4]=toolbar.locateTool(numOfRecordsLabel);
  		loc[5]=toolbar.locateTool(firstRecord);
  		loc[6]=toolbar.locateTool(prevRecord);
  		loc[7]=toolbar.locateTool(nextRecord);
  		loc[8]=toolbar.locateTool(lastRecord);
  		return loc;
  }

 
  public ESlateToolBar getToolbar(){
      return toolbar;
  }

  public void destroyToolbar(){
      toolbar = null;
  }

  public void setDefaultState(){
      setDefaultState(new ESlateToolBar("Status"));
      toolbar.setDefaultStateSetter(this);
  }

  public void setDefaultState(ESlateToolBar bar){
      toolbar = bar;
      toolbar.removeAll();
      toolbar.setFloatable(false);
      toolbar.setBorder(new EtchedBorder(EtchedBorder.RAISED));
      toolbar.setBackground(new Color(96, 103, 144));
      toolbar.setCentered(false);
      toolbar.setSeparation(new Dimension(4, 4));
      toolbar.setLeadingSeparation(new Dimension(3, 3));


      Dimension statusBarSize = new Dimension(10, 20);
      toolbar.setMaximumSize(statusBarSize);
      toolbar.setMinimumSize(statusBarSize);
      toolbar.setPreferredSize(statusBarSize);


      createElements();
      attachStatusBarActions();
      if (dBase.activeDBTable != null)
          updateStatusBar(dBase.activeDBTable);

      toolbar.getESlateHandle().addESlateListener(new ESlateAdapter(){
          public void parentChanged(ParentChangedEvent e){
              ESlateHandle oldParent = e.getOldParent();
              ESlateHandle newParent = e.getNewParent();
              if (dBase.getESlateHandle().equals(oldParent) && !dBase.getESlateHandle().equals(newParent)){
                  toolbar = null;
              }
          }
      });
  }

  private void createElements() {
      ImageIcon prevRecIcon = new ImageIcon(getClass().getResource("images/prevRecord.gif"));
      ImageIcon nextRecIcon = new ImageIcon(getClass().getResource("images/nextRecord.gif"));
      ImageIcon lastRecIcon = new ImageIcon(getClass().getResource("images/lastRecord.gif"));
      ImageIcon firstRecIcon = new ImageIcon(getClass().getResource("images/firstRecord.gif"));
      ImageIcon prevTableIcon = new ImageIcon(getClass().getResource("images/prevTab.gif"));
      ImageIcon nextTableIcon = new ImageIcon(getClass().getResource("images/nextTab.gif"));
      // Message Group
      VisualGroup messageGroup = null;
      try{
          messageGroup = toolbar.addVisualGroup("MessageGroup");
      }catch (Exception exc) {
          messageGroup = toolbar.getVisualGroup("MessageGroup");
      }
      toolbar.setSeparation(messageGroup, new Dimension(4, 4));

      messageLabel = new ESlateLabel();
//      messageLabel.setFont(statusBarFont);
      messageLabel.setForeground(Color.white);

      toolbar.add(messageGroup, messageLabel, infoBundle.getString("Message"));
      //toolbar.add(messageGroup, Box.createHorizontalStrut(10), "glue");

      ESlateLabel bar= new ESlateLabel();
      ESlateLabel bar2 = new ESlateLabel();
      ESlateLabel bar3 = new ESlateLabel();
      Dimension barDimension = new Dimension(5, 22);
      bar.setMaximumSize(barDimension);
      bar.setMinimumSize(barDimension);
      bar.setPreferredSize(barDimension);
      bar2.setMaximumSize(barDimension);
      bar2.setMinimumSize(barDimension);
      bar2.setPreferredSize(barDimension);
      bar3.setMaximumSize(barDimension);
      bar3.setMinimumSize(barDimension);
      bar3.setPreferredSize(barDimension);
      bar.setBackground(new Color(96, 103, 144));
      bar2.setBackground(new Color(96, 103, 144));
      bar3.setBackground(new Color(96, 103, 144));
      bar.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED)); //new EtchedBorder(EtchedBorder.RAISED));
      bar2.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED)); //new EtchedBorder(EtchedBorder.RAISED));
      bar3.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED)); //new EtchedBorder(EtchedBorder.RAISED));

      toolbar.add(messageGroup, bar, "|1");


      // Button Table Group
      VisualGroup buttonTableGroup = null;
      try{
          buttonTableGroup = toolbar.addVisualGroup("ButtonTableGroup");
      }catch (Exception exc) {
          buttonTableGroup = toolbar.getVisualGroup("ButtonTableGroup");
      }
      toolbar.setSeparation(buttonTableGroup, new Dimension(4, 4));

/* Change active jTable region.
       */
      Insets buttonInsets = new Insets(0, 0, 0, 0);
      Dimension buttonSize = new Dimension(23, 15);

      prevTable = new ESlateButton(prevTableIcon);
      prevTable.setMaximumSize(buttonSize);
      prevTable.setMinimumSize(buttonSize);
      prevTable.setPreferredSize(buttonSize);
      prevTable.setMargin(buttonInsets);
      prevTable.setRequestFocusEnabled(false);
      prevTable.setFocusPainted(false);
      prevTable.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      prevTable.setBorderPainted(true);
      prevTable.setToolTipText(infoBundle.getString("Previous Table"));
      toolbar.add(buttonTableGroup, prevTable, infoBundle.getString("Previous Table"));

      nextTable = new ESlateButton(nextTableIcon);
      nextTable.setMaximumSize(buttonSize);
      nextTable.setMinimumSize(buttonSize);
      nextTable.setPreferredSize(buttonSize);
      nextTable.setMargin(buttonInsets);
      nextTable.setRequestFocusEnabled(false);
      nextTable.setFocusPainted(false);
      nextTable.setBorderPainted(true);
      nextTable.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      nextTable.setToolTipText(infoBundle.getString("Next Table"));
      toolbar.add(buttonTableGroup, nextTable, infoBundle.getString("Next Table"));
      toolbar.add(buttonTableGroup, bar2, "|2");


      /* Current record number region.
       */
      // Current record number Group
      VisualGroup recordGroup = null;
      try{
          recordGroup = toolbar.addVisualGroup("RecordGroup");
      }catch (Exception exc) {
          recordGroup = toolbar.getVisualGroup("RecordGroup");
      }
      toolbar.setSeparation(recordGroup, new Dimension(4, 4));

      ESlateLabel recLabel = new ESlateLabel();
      recLabel.setText(infoBundle.getString("DatabaseMsg82"));
//      recLabel.setFont(statusBarFont);
      recLabel.setForeground(Color.white);
      toolbar.add(recordGroup, recLabel, infoBundle.getString("Record"));

      Dimension recNumberBoxSize = new Dimension(53, 17);
      recNumberBox = new ESlateTextField();
      recNumberBox.setMaximumSize(recNumberBoxSize);
      recNumberBox.setMinimumSize(recNumberBoxSize);
      recNumberBox.setPreferredSize(recNumberBoxSize);
      recNumberBox.setBackground(Color.white);
      recNumberBox.setForeground(Color.black);
      recNumberBox.setHorizontalAlignment(SwingConstants.RIGHT);
      recNumberBox.setToolTipText(infoBundle.getString("Record Number"));
      toolbar.add(recordGroup, recNumberBox, infoBundle.getString("Record Number"));

      numOfRecordsLabel = new ESlateLabel();
//      numOfRecordsLabel.setFont(statusBarFont);
      numOfRecordsLabel.setMaximumSize(recNumberBoxSize);
      numOfRecordsLabel.setMinimumSize(recNumberBoxSize);
      numOfRecordsLabel.setPreferredSize(recNumberBoxSize);
      numOfRecordsLabel.setForeground(Color.white);
      numOfRecordsLabel.setToolTipText(infoBundle.getString("Number of Records"));
      toolbar.add(recordGroup, numOfRecordsLabel, infoBundle.getString("Number of Records"));
      toolbar.add(recordGroup, bar3, "|3");

      /* Record navigation region.
       */
      VisualGroup recordNavigationGroup = null;
      try{
          recordNavigationGroup = toolbar.addVisualGroup("RecordNavigationGroup");
      }catch (Exception exc) {
          recordNavigationGroup = toolbar.getVisualGroup("RecordNavigationGroup");
      }
      toolbar.setSeparation(recordNavigationGroup, new Dimension(4, 4));

      prevRecord = new ESlateButton(prevRecIcon);
      nextRecord = new ESlateButton(nextRecIcon);
      firstRecord = new ESlateButton(firstRecIcon);
      lastRecord = new ESlateButton(lastRecIcon);

      prevRecord.setMaximumSize(buttonSize);
      prevRecord.setMinimumSize(buttonSize);
      prevRecord.setPreferredSize(buttonSize);

      nextRecord.setMaximumSize(buttonSize);
      nextRecord.setMinimumSize(buttonSize);
      nextRecord.setPreferredSize(buttonSize);

      firstRecord.setMaximumSize(buttonSize);
      firstRecord.setMinimumSize(buttonSize);
      firstRecord.setPreferredSize(buttonSize);

      lastRecord.setMaximumSize(buttonSize);
      lastRecord.setMinimumSize(buttonSize);
      lastRecord.setPreferredSize(buttonSize);

      prevRecord.setMargin(buttonInsets);
      nextRecord.setMargin(buttonInsets);
      firstRecord.setMargin(buttonInsets);
      lastRecord.setMargin(buttonInsets);

      prevRecord.setBorderPainted(true);
      nextRecord.setBorderPainted(true);
      firstRecord.setBorderPainted(true);
      lastRecord.setBorderPainted(true);



      prevRecord.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      nextRecord.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      firstRecord.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      lastRecord.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);

      prevRecord.setRequestFocusEnabled(false);
      nextRecord.setRequestFocusEnabled(false);
      firstRecord.setRequestFocusEnabled(false);
      lastRecord.setRequestFocusEnabled(false);
      prevRecord.setFocusPainted(false);
      nextRecord.setFocusPainted(false);
      firstRecord.setFocusPainted(false);
      lastRecord.setFocusPainted(false);
      prevRecord.setToolTipText(infoBundle.getString("Previous Record"));
      nextRecord.setToolTipText(infoBundle.getString("Next Record"));
      firstRecord.setToolTipText(infoBundle.getString("First Record"));
      lastRecord.setToolTipText(infoBundle.getString("Last Record"));

      
      toolbar.add(recordNavigationGroup, firstRecord, infoBundle.getString("First Record"));
      toolbar.add(recordNavigationGroup, prevRecord, infoBundle.getString("Previous Record"));
      toolbar.add(recordNavigationGroup, nextRecord, infoBundle.getString("Next Record"));
      toolbar.add(recordNavigationGroup, lastRecord, infoBundle.getString("Last Record"));
  }

  void attachStatusBarActions() {
    if (prevTable != null)
        prevTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
                    dBase.visiblePopupMenu.setVisible(false);
                dBase.activateDBTableAt(dBase.activeDBTableIndex-1);
            }
        });
    if (nextTable != null)
        nextTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
                    dBase.visiblePopupMenu.setVisible(false);
                dBase.activateDBTableAt(dBase.activeDBTableIndex+1);
            }
        });
    if (nextRecord != null)
        nextRecord.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dBase.activeDBTable.downAction.execute();
            }
        });
    if (prevRecord != null)
        prevRecord.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dBase.activeDBTable.upAction.execute();
            }
        });
    if (firstRecord != null)
        firstRecord.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dBase.activeDBTable.homeAction.execute();
            }
        });
    if (lastRecord != null)
        lastRecord.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dBase.activeDBTable.endAction.execute();
            }
        });
    if (recNumberBox != null){
        recNumberBox.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
                    dBase.visiblePopupMenu.setVisible(false);
                recNumberBox.getCaret().setVisible(true);
            }
        });
        recNumberBox.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (dBase.activeDBTable == null || e.getKeyCode() != KeyEvent.VK_ENTER)
                    return;

                try{
                    int recordNumber = new Integer(recNumberBox.getText()).intValue();
                    if (recordNumber < 1 || recordNumber > dBase.activeDBTable.table.getRecordCount()) {
                        ESlateOptionPane.showMessageDialog(dBase.dbComponent, dBase.dbComponent.infoBundle.getString("DatabaseMsg90"), dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int newYPosition = dBase.activeDBTable.jTable.getCellRect(recordNumber-1, 0, true).y - (dBase.activeDBTable.scrollpane.getViewport().getExtentSize().height/2);
                    if (newYPosition < 0)
                        newYPosition = 0;

                    int viewHeight = dBase.activeDBTable.scrollpane.getViewport().getViewSize().height;
                    if (newYPosition > viewHeight - dBase.activeDBTable.scrollpane.getViewport().getExtentSize().height)
                        newYPosition = viewHeight - dBase.activeDBTable.scrollpane.getViewport().getExtentSize().height;

                    dBase.activeDBTable.scrollpane.getViewport().setViewPosition(new Point(
                        dBase.activeDBTable.scrollpane.getViewport().getViewPosition().x,
                        newYPosition
                    ));
                    dBase.activeDBTable.setActiveRow(recordNumber-1);
                    dBase.activeDBTable.refresh();

                }catch (NumberFormatException exc) {
                    ESlateOptionPane.showMessageDialog(dBase.dbComponent, infoBundle.getString("DatabaseMsg91") + "\"" + recNumberBox.getText() + "\"" + infoBundle.getString("DatabaseMsg89"), errorStr, JOptionPane.ERROR_MESSAGE);
                }
                recNumberBox.getCaret().setVisible(false);
                dBase.activeDBTable.requestFocus();
            }
        });
    }
}

public void updateStatusBar(DBTable dbTable) {
      int recordCount = dbTable.table.getRecordCount();
      int numOfSelectedRecords = dbTable.table.getSelectedSubset().size();
      int activeRow = dbTable.activeRow;
      if (numOfRecordsLabel != null){
          numOfRecordsLabel.setText(infoBundle.getString("DatabaseMsg81") + recordCount);
          dBase.updateNumOfSelectedRecords(numOfSelectedRecords);
      }

      if (activeRow > 0 && activeRow < recordCount) {
          activeRow = activeRow+1;
          if (recNumberBox != null)
              recNumberBox.setText(new Integer(activeRow).toString());

          if (activeRow == 1) {
              if (prevRecord != null)
                  prevRecord.setEnabled(false);
              if (firstRecord != null)
                  firstRecord.setEnabled(false);
          }else{
              if (prevRecord != null)
                  prevRecord.setEnabled(true);
              if (firstRecord != null)
                  firstRecord.setEnabled(true);
          }
          if (activeRow == recordCount) {
              if (lastRecord != null)
                  lastRecord.setEnabled(false);
              if (nextRecord != null)
                  nextRecord.setEnabled(false);
          }else{
              if (lastRecord != null)
                  lastRecord.setEnabled(true);
              if (nextRecord != null)
                  nextRecord.setEnabled(true);
          }
      }else{
          if (recNumberBox != null)
              recNumberBox.setText("");
          if (prevRecord != null)
              prevRecord.setEnabled(true);
          if (firstRecord != null)
              firstRecord.setEnabled(true);
          if (lastRecord != null)
              lastRecord.setEnabled(true);
          if (nextRecord != null)
              nextRecord.setEnabled(true);
      }

      if (prevTable != null)
          if (dBase.dbTables.indexOf(dbTable) == 0)
              prevTable.setEnabled(false);
          else
              prevTable.setEnabled(true);
      if (nextTable != null)
          if (dBase.dbTables.indexOf(dbTable) == dBase.dbTables.size()-1)
              nextTable.setEnabled(false);
          else
              nextTable.setEnabled(true);
  }

  public void setMessageLabelInWaitState() {
      if (toolbar == null)
          return;
      if (messageLabel == null)
          return;
      String message = infoBundle.getString("WaitMessage");

      messageLabel.setForeground(Color.yellow);
      messageLabel.setText(message);


      int newMessageWidth = messageLabel.getFontMetrics(messageLabel.getFont()).stringWidth(message); // helvPlain12Fm.stringWidth(message);
      int statusBarWidth = toolbar.getSize().width;
      if (statusBarWidth-newMessageWidth >= 360) { //360 is the standard width of the rest of the elements in the status bar
          messageLabel.setPreferredSize(new Dimension(newMessageWidth, 15));
          messageLabel.setMinimumSize(new Dimension(newMessageWidth, 15));
          messageLabel.setMaximumSize(new Dimension(newMessageWidth, 15));
      }else{
          newMessageWidth = statusBarWidth - 360;
          if (newMessageWidth < 0) newMessageWidth = 0;
          messageLabel.setPreferredSize(new Dimension(newMessageWidth, 15));
          messageLabel.setMinimumSize(new Dimension(newMessageWidth, 15));
          messageLabel.setMaximumSize(new Dimension(newMessageWidth, 15));
      }
      toolbar.revalidate();
      toolbar.paintImmediately(toolbar.getVisibleRect());
  }

  public void setRecordNumber(String s){
      if (recNumberBox != null)
          recNumberBox.setText(s);
  }

  public void setNumOfRecordsLabelText(String s){
      if (numOfRecordsLabel != null){
          numOfRecordsLabel.setText(s);
          numOfRecordsLabel.validate();
          numOfRecordsLabel.repaint();
      }
  }

  public void setMessageLabelColor(Color c){
      if (messageLabel != null)
          messageLabel.setForeground(c);
  }

  public void setMessageLabelText(String s){
      if (messageLabel != null)
          messageLabel.setText(s);
  }

  public void setMessageLabelSize(Dimension d){
      if (messageLabel != null){
          messageLabel.setPreferredSize(d);
          messageLabel.setMinimumSize(d);
          messageLabel.setMaximumSize(d);
          messageLabel.invalidate();
      }
  }

  public int getMessageLabelSize(String message) {
      return messageLabel.getFontMetrics(messageLabel.getFont()).stringWidth(message); // helvPlain12Fm.stringWidth(message);
  }



  public void setMessageLabelEnabled(boolean b){
      if (messageLabel != null)
          messageLabel.setEnabled(b);
  }

  public void setNumOfRecordsLabelEnabled(boolean b){
      if (numOfRecordsLabel != null)
          numOfRecordsLabel.setEnabled(b);
  }

  public void setPrevRecordEnabled(boolean b){
      if (prevRecord != null)
          prevRecord.setEnabled(b);
  }

  public void setNextRecordEnabled(boolean b){
      if (nextRecord != null)
          nextRecord.setEnabled(b);
  }

  public void setLastRecordEnabled(boolean b){
      if (lastRecord != null)
          lastRecord.setEnabled(b);
  }

  public void setFirstRecordEnabled(boolean b){
      if (firstRecord != null)
          firstRecord.setEnabled(b);
  }

  public void setPrevTableEnabled(boolean b){
      if (prevTable != null)
          prevTable.setEnabled(b);
  }

  public void setNextTableEnabled(boolean b){
      if (nextTable != null)
          nextTable.setEnabled(b);
  }

  public void setRecNumberBoxEnabled(boolean b){
      if (recNumberBox != null)
          recNumberBox.setEnabled(b);
  }
}