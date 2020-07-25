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
import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.eslateButton.ESlateButton;
import gr.cti.eslate.eslateToolBar.DefaultStateSetter;
import gr.cti.eslate.eslateToolBar.ESlateToolBar;
import gr.cti.eslate.eslateToolBar.ToolLocation;
import gr.cti.eslate.eslateToolBar.VisualGroup;

import java.awt.Dimension;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.border.EmptyBorder;

public class StandardToolbarController implements DefaultStateSetter{

  Database dBase;
  String errorStr;
  private ESlateToolBar toolbar;
  private ESlateButton newDB, openDB, saveDB, cut, copy, paste, sortAsc, sortDesc, find, findNext,
              findPrev, addRecord, removeRecord, removeSelectedRecords, imageEdit;
  private ESlateButton addField, removeField, fieldProperties, clearRecSelection, selectAllRecs,
              invertRecSelection, promoteSelectedRecs, insFilePath;
  ResourceBundle infoBundle;


  public StandardToolbarController(Database dB) {
      dBase = dB;
      infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", Locale.getDefault());
      errorStr = infoBundle.getString("Error");
  }

  public void createToolbar(){
      toolbar = new ESlateToolBar("Standard");
      toolbar.setDefaultStateSetter(this);
      setDefaultState(toolbar);
      toolbar.setFocusable(false);
  }

  public void setToolbar(ESlateToolBar bar){
      toolbar = bar;
      toolbar.setDefaultStateSetter(this);
      toolbar.setFocusable(false);

      /*toolbar.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
              if (e.getModifiers() == e.BUTTON3_MASK) {
                  if (toolbar.isVisible())
                      dBase.standardToolBarMenuItem.setSelected(true);
                  if (dBase.isFormattingToolBarVisible())
                      dBase.formattingToolBarMenuItem.setSelected(true);
                  dBase.toolBarMenu.show(toolbar, e.getX(), e.getY());
              }
          }
      });*/


      toolbar.getESlateHandle().addESlateListener(new ESlateAdapter(){
          public void parentChanged(ParentChangedEvent e){
              ESlateHandle oldParent = e.getOldParent();
              ESlateHandle newParent = e.getNewParent();
              if (dBase.getESlateHandle().equals(oldParent) && !dBase.getESlateHandle().equals(newParent))
                  toolbar = null;
          }
      });
  }
  
  void restoreOld() {
      if (toolbar.getTool("New Database") != null){
          newDB = (ESlateButton)toolbar.getTool("New Database");
          newDB.setEnabled(dBase.newDBAction.isEnabled());
      }
      if (toolbar.getTool("Open Database") != null){
          openDB = (ESlateButton)toolbar.getTool("Open Database");
          openDB.setEnabled(dBase.openDBAction.isEnabled());
      }
      if (toolbar.getTool("Save Database") != null){
          saveDB = (ESlateButton)toolbar.getTool("Save Database");
          saveDB.setEnabled(dBase.saveDBAction.isEnabled());
      }
      if (toolbar.getTool("Cut") != null){
          cut = (ESlateButton)toolbar.getTool("Cut");
          cut.setEnabled(dBase.cutAction.isEnabled());
      }
      if (toolbar.getTool("Copy") != null){
          copy = (ESlateButton)toolbar.getTool("Copy");
          copy.setEnabled(dBase.copyAction.isEnabled());
      }
      if (toolbar.getTool("Paste") != null){
          paste = (ESlateButton)toolbar.getTool("Paste");
          paste.setEnabled(dBase.pasteAction.isEnabled());
      }
      if (toolbar.getTool("Sort ascending") != null){
          sortAsc = (ESlateButton)toolbar.getTool("Sort ascending");
          sortAsc.setEnabled(dBase.sortAscAction.isEnabled());
      }
      if (toolbar.getTool("Sort descending") != null){
          sortDesc = (ESlateButton)toolbar.getTool("Sort descending");
          sortDesc.setEnabled(dBase.sortDescAction.isEnabled());
      }
      if (toolbar.getTool("Find in jTable") != null){
          find = (ESlateButton)toolbar.getTool("Find in jTable");
          find.setEnabled(dBase.findAction.isEnabled());
      }
      if (toolbar.getTool("Find previous in jTable") != null){
          findPrev = (ESlateButton)toolbar.getTool("Find previous in jTable");
          findPrev.setEnabled(dBase.findPrevAction.isEnabled());
      }
      if (toolbar.getTool("Find next in jTable") != null){
          findNext = (ESlateButton)toolbar.getTool("Find next in jTable");
          findNext.setEnabled(dBase.findNextAction.isEnabled());
      }
      if (toolbar.getTool("Add record") != null){
          addRecord = (ESlateButton)toolbar.getTool("Add record");
          addRecord.setEnabled(dBase.addRecordAction.isEnabled());
      }
      if (toolbar.getTool("Remove active record") != null){
          removeRecord = (ESlateButton)toolbar.getTool("Remove active record");
          removeRecord.setEnabled(dBase.removeRecordAction.isEnabled());
      }
      if (toolbar.getTool("Remove selected records") != null){
          removeSelectedRecords = (ESlateButton)toolbar.getTool("Remove selected records");
          removeSelectedRecords.setEnabled(dBase.removeSelRecordAction.isEnabled());
      }
      if (toolbar.getTool("Add new field") != null){
          addField = (ESlateButton)toolbar.getTool("Add new field");
          addField.setEnabled(dBase.addFieldAction.isEnabled());
      }
      if (toolbar.getTool("Remove field") != null){
          removeField = (ESlateButton)toolbar.getTool("Remove field");
          removeField.setEnabled(dBase.removeFieldAction.isEnabled());
      }
      if (toolbar.getTool("Field properties") != null){
          fieldProperties = (ESlateButton)toolbar.getTool("Field properties");
          fieldProperties.setEnabled(dBase.fieldPropertiesAction.isEnabled());
      }
      if (toolbar.getTool("Select all records") != null){
          selectAllRecs = (ESlateButton)toolbar.getTool("Select all records");
          selectAllRecs.setEnabled(dBase.selectAllRecsAction.isEnabled());
      }
      if (toolbar.getTool("Clear record selection") != null){
          clearRecSelection = (ESlateButton)toolbar.getTool("Clear record selection");
          clearRecSelection.setEnabled(dBase.clearRecAction.isEnabled());
      }
      if (toolbar.getTool("Invert record selection") != null){
          invertRecSelection = (ESlateButton)toolbar.getTool("Invert record selection");
          invertRecSelection.setEnabled(dBase.invertRecAction.isEnabled());
      }
      if (toolbar.getTool("Promote selected records") != null){
          promoteSelectedRecs = (ESlateButton)toolbar.getTool("Promote selected records");
          promoteSelectedRecs.setEnabled(dBase.promoteSelRecsAction.isEnabled());
      }
      if (toolbar.getTool("Image edit") != null){
          imageEdit = (ESlateButton)toolbar.getTool("Image edit");
          imageEdit.setEnabled(dBase.imageEditAction.isEnabled());
      }
      if (toolbar.getTool("Insert file path") != null){
    	  insFilePath = (ESlateButton)toolbar.getTool("Insert file path");
    	  insFilePath.setEnabled(dBase.insFilePathAction.isEnabled());
      } else {
    	  createInsFilePathButton();
      }
  }

  	/**
	 * Returns an array containing the current locations of the tools in the toolbar.
	 * 
	 * @return An array containing the current locations of the tools in the toolbar.
	 */
	ToolLocation[] getToolLocations() {
		int nTools=23;
		ToolLocation loc[]=new ToolLocation[nTools];
		loc[0]=toolbar.locateTool(newDB);
		loc[1]=toolbar.locateTool(openDB);
		loc[2]=toolbar.locateTool(saveDB);
		loc[3]=toolbar.locateTool(cut);
		loc[4]=toolbar.locateTool(copy);
		loc[5]=toolbar.locateTool(paste);
		loc[6]=toolbar.locateTool(sortAsc);
		loc[7]=toolbar.locateTool(sortDesc);
		loc[8]=toolbar.locateTool(find);
		loc[9]=toolbar.locateTool(findPrev);
		loc[10]=toolbar.locateTool(findNext);
		loc[11]=toolbar.locateTool(addRecord);
		loc[12]=toolbar.locateTool(removeRecord);
		loc[13]=toolbar.locateTool(removeSelectedRecords);
		loc[14]=toolbar.locateTool(addField);
		loc[15]=toolbar.locateTool(removeField);
		loc[16]=toolbar.locateTool(fieldProperties);
		loc[17]=toolbar.locateTool(selectAllRecs);
		loc[18]=toolbar.locateTool(clearRecSelection);
		loc[19]=toolbar.locateTool(invertRecSelection);
		loc[20]=toolbar.locateTool(promoteSelectedRecs);
		loc[21]=toolbar.locateTool(imageEdit);
		loc[22]=toolbar.locateTool(insFilePath);
		return loc;
	}

  /**
   * Identifies the tools in a toolbar that has just been loaded, updating
   * the references to these tools.
   * @param     loc     The locations of the tools in the toolbar.
   */
  void identifyTools(ToolLocation[] loc) {
		VisualGroup[] groups=toolbar.getVisualGroups();
		if (loc[0]!=null) {
			newDB=(ESlateButton) (groups[loc[0].visualGroup].getComponent(loc[0].toolIndex));
		} else {
			newDB=null;
		}
		if (loc[1]!=null) {
			openDB=(ESlateButton) (groups[loc[1].visualGroup].getComponent(loc[1].toolIndex));
		} else {
			openDB=null;
		}
		if (loc[2]!=null) {
			saveDB=(ESlateButton) (groups[loc[2].visualGroup].getComponent(loc[2].toolIndex));
		} else {
			saveDB=null;
		}
		if (loc[3]!=null) {
			cut=(ESlateButton) (groups[loc[3].visualGroup].getComponent(loc[3].toolIndex));
		} else {
			cut=null;
		}
		if (loc[4]!=null) {
			copy=(ESlateButton) (groups[loc[4].visualGroup].getComponent(loc[4].toolIndex));
		} else {
			copy=null;
		}
		if (loc[5]!=null) {
			paste=(ESlateButton) (groups[loc[5].visualGroup].getComponent(loc[5].toolIndex));
		} else {
			paste=null;
		}
		if (loc[6]!=null) {
			sortAsc=(ESlateButton) (groups[loc[6].visualGroup].getComponent(loc[6].toolIndex));
		} else {
			sortAsc=null;
		}
		if (loc[7]!=null) {
			sortDesc=(ESlateButton) (groups[loc[7].visualGroup].getComponent(loc[7].toolIndex));
		} else {
			sortDesc=null;
		}
		if (loc[8]!=null) {
			find=(ESlateButton) (groups[loc[8].visualGroup].getComponent(loc[8].toolIndex));
		} else {
			find=null;
		}
		if (loc[9]!=null) {
			findPrev=(ESlateButton) (groups[loc[9].visualGroup].getComponent(loc[9].toolIndex));
		} else {
			findPrev=null;
		}
		if (loc[10]!=null) {
			findNext=(ESlateButton) (groups[loc[10].visualGroup].getComponent(loc[10].toolIndex));
		} else {
			findNext=null;
		}
		if (loc[11]!=null) {
			addRecord=(ESlateButton) (groups[loc[11].visualGroup].getComponent(loc[11].toolIndex));
		} else {
			addRecord=null;
		}
		if (loc[12]!=null) {
			removeRecord=(ESlateButton) (groups[loc[12].visualGroup].getComponent(loc[12].toolIndex));
		} else {
			removeRecord=null;
		}
		if (loc[13]!=null) {
			removeSelectedRecords=(ESlateButton) (groups[loc[13].visualGroup].getComponent(loc[13].toolIndex));
		} else {
			removeSelectedRecords=null;
		}
		if (loc[14]!=null) {
			addField=(ESlateButton) (groups[loc[14].visualGroup].getComponent(loc[14].toolIndex));
		} else {
			addField=null;
		}
		if (loc[15]!=null) {
			removeField=(ESlateButton) (groups[loc[15].visualGroup].getComponent(loc[15].toolIndex));
		} else {
			removeField=null;
		}
		if (loc[16]!=null) {
			fieldProperties=(ESlateButton) (groups[loc[16].visualGroup].getComponent(loc[16].toolIndex));
		} else {
			fieldProperties=null;
		}
		if (loc[17]!=null) {
			selectAllRecs=(ESlateButton) (groups[loc[17].visualGroup].getComponent(loc[17].toolIndex));
		} else {
			selectAllRecs=null;
		}
		if (loc[18]!=null) {
			clearRecSelection=(ESlateButton) (groups[loc[18].visualGroup].getComponent(loc[18].toolIndex));
		} else {
			clearRecSelection=null;
		}
		if (loc[19]!=null) {
			invertRecSelection=(ESlateButton) (groups[loc[19].visualGroup].getComponent(loc[19].toolIndex));
		} else {
			invertRecSelection=null;
		}
		if (loc[20]!=null) {
			promoteSelectedRecs=(ESlateButton) (groups[loc[20].visualGroup].getComponent(loc[20].toolIndex));
		} else {
			promoteSelectedRecs=null;
		}
		if (loc[21]!=null) {
			imageEdit=(ESlateButton) (groups[loc[21].visualGroup].getComponent(loc[21].toolIndex));
		} else {
			imageEdit=null;
		}
		if (loc.length>22 && loc[22]!=null) {
			insFilePath=(ESlateButton) (groups[loc[22].visualGroup].getComponent(loc[22].toolIndex));
		} else if (loc[21]!=null) {
			insFilePath=(ESlateButton) (groups[loc[21].visualGroup].getComponent(loc[21].toolIndex+1));
		}
  }
  public ESlateToolBar getToolbar(){
      return toolbar;
  }

  public void destroyToolbar(){
      toolbar = null;
  }

  public void setDefaultState(){
      setDefaultState(new ESlateToolBar("Standard"));
      toolbar.setDefaultStateSetter(this);
  }

  public void setDefaultState(ESlateToolBar bar){
      toolbar = bar;
      toolbar.setFocusable(false);
      toolbar.removeAll();
      toolbar.setFloatable(false);
      toolbar.setBorder(new EmptyBorder(1, 0, 1, 0));
      toolbar.setCentered(false);
      toolbar.setSeparation(new Dimension(7, 10));
      toolbar.setLeadingSeparation(new Dimension(5, 5));
      createElements();
      attachStandardToolBarActions();

      toolbar.getESlateHandle().addESlateListener(new ESlateAdapter(){
          public void parentChanged(ParentChangedEvent e){
              ESlateHandle oldParent = e.getOldParent();
              ESlateHandle newParent = e.getNewParent();
              if (dBase.getESlateHandle().equals(oldParent) && !dBase.getESlateHandle().equals(newParent))
                  toolbar = null;
          }
      });
  }

  private void createElements() {
      // File Group
      VisualGroup fileGroup = null;
      try{
          fileGroup = toolbar.addVisualGroup("FileGroup");
      }catch (Exception exc) {
          fileGroup = toolbar.getVisualGroup("FileGroup");
      }
      toolbar.setSeparation(fileGroup, new Dimension(2, 10));

      newDB = new ESlateButton(dBase.newDBAction.getEnabledIcon());
      newDB.setEnabled(dBase.newDBAction.isEnabled());
      newDB.setToolTipText(infoBundle.getString("DatabaseMsg97"));
      newDB.setMargin(new Insets(0,0,0,0));
      newDB.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      newDB.setRequestFocusEnabled(false);
      newDB.setBorderPainted(false);
      toolbar.add(fileGroup, newDB, infoBundle.getString("DatabaseMsg97"));
      toolbar.setAssociatedText(newDB, infoBundle.getString("DatabaseMsg97"));

      openDB = new ESlateButton(dBase.openDBAction.getEnabledIcon());
      openDB.setEnabled(dBase.openDBAction.isEnabled());
      openDB.setToolTipText(infoBundle.getString("DatabaseMsg98"));
      openDB.setMargin(new Insets(0,0,0,0));
      openDB.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      openDB.setRequestFocusEnabled(false);
      openDB.setBorderPainted(false);
      toolbar.add(fileGroup, openDB, infoBundle.getString("DatabaseMsg98"));
      toolbar.setAssociatedText(openDB, infoBundle.getString("DatabaseMsg98"));

      saveDB = new ESlateButton(dBase.saveDBAction.getEnabledIcon());
      saveDB.setEnabled(dBase.saveDBAction.isEnabled());
      saveDB.setToolTipText(infoBundle.getString("DatabaseMsg99"));
      saveDB.setDisabledIcon(dBase.saveDBAction.getDisabledIcon());
      saveDB.setMargin(new Insets(0,0,0,0));
      saveDB.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      saveDB.setRequestFocusEnabled(false);
      saveDB.setBorderPainted(false);
      toolbar.add(fileGroup, saveDB, infoBundle.getString("DatabaseMsg99"));
      toolbar.setAssociatedText(saveDB, infoBundle.getString("DatabaseMsg99"));

      // Edit Group
      VisualGroup editGroup = null;
      try{
          editGroup = toolbar.addVisualGroup("EditGroup");
      }catch (Exception exc) {
          editGroup = toolbar.getVisualGroup("EditGroup");
      }
      toolbar.setSeparation(editGroup, new Dimension(2, 10));

      cut = new ESlateButton(dBase.cutAction.getEnabledIcon());
      cut.setEnabled(dBase.cutAction.isEnabled());
      cut.setToolTipText(infoBundle.getString("DatabaseMsg100"));
      cut.setDisabledIcon(dBase.cutAction.getDisabledIcon());
      cut.setMargin(new Insets(0,0,0,0));
      cut.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      cut.setRequestFocusEnabled(false);
      cut.setBorderPainted(false);
      toolbar.add(editGroup, cut, infoBundle.getString("DatabaseMsg100"));
      toolbar.setAssociatedText(cut, infoBundle.getString("DatabaseMsg100"));

      copy = new ESlateButton(dBase.copyAction.getEnabledIcon());
      copy.setEnabled(dBase.copyAction.isEnabled());
      copy.setToolTipText(infoBundle.getString("DatabaseMsg101"));
      copy.setDisabledIcon(dBase.copyAction.getDisabledIcon());
      copy.setMargin(new Insets(0,0,0,0));
      copy.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      copy.setRequestFocusEnabled(false);
      copy.setBorderPainted(false);
      toolbar.add(editGroup, copy, infoBundle.getString("DatabaseMsg101"));
      toolbar.setAssociatedText(copy, infoBundle.getString("DatabaseMsg101"));

      paste = new ESlateButton(dBase.pasteAction.getEnabledIcon());
      paste.setEnabled(dBase.pasteAction.isEnabled());
      paste.setToolTipText(infoBundle.getString("DatabaseMsg102"));
      paste.setDisabledIcon(dBase.pasteAction.getDisabledIcon());
      paste.setMargin(new Insets(0,0,0,0));
      paste.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      paste.setRequestFocusEnabled(false);
      paste.setBorderPainted(false);
      toolbar.add(editGroup, paste, infoBundle.getString("DatabaseMsg102"));
      toolbar.setAssociatedText(paste, infoBundle.getString("DatabaseMsg102"));

      // Sort Group
      VisualGroup sortGroup = null;
      try{
          sortGroup = toolbar.addVisualGroup("SortGroup");
      }catch (Exception exc) {
          sortGroup = toolbar.getVisualGroup("SortGroup");
      }
      toolbar.setSeparation(sortGroup, new Dimension(2, 10));

      // Sort ascending
      sortAsc = new ESlateButton(dBase.sortAscAction.getEnabledIcon());
      sortAsc.setEnabled(dBase.sortAscAction.isEnabled());
      sortAsc.setToolTipText(infoBundle.getString("DatabaseMsg103"));
      sortAsc.setDisabledIcon(dBase.sortAscAction.getDisabledIcon());
      sortAsc.setMargin(new Insets(0,0,0,0));
      sortAsc.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      sortAsc.setRequestFocusEnabled(false);
      sortAsc.setBorderPainted(false);
      toolbar.add(sortGroup, sortAsc, infoBundle.getString("DatabaseMsg103"));
      toolbar.setAssociatedText(sortAsc, infoBundle.getString("DatabaseMsg103"));

      // Sort descending
      sortDesc = new ESlateButton(dBase.sortDescAction.getEnabledIcon());
      sortDesc.setEnabled(dBase.sortDescAction.isEnabled());
      sortDesc.setToolTipText(infoBundle.getString("DatabaseMsg104"));
      sortDesc.setDisabledIcon(dBase.sortDescAction.getDisabledIcon());
      sortDesc.setMargin(new Insets(0,0,0,0));
      sortDesc.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      sortDesc.setRequestFocusEnabled(false);
      sortDesc.setBorderPainted(false);
      toolbar.add(sortGroup, sortDesc, infoBundle.getString("DatabaseMsg104"));
      toolbar.setAssociatedText(sortDesc, infoBundle.getString("DatabaseMsg104"));

      // Table Group
      VisualGroup tableGroup = null;
      try{
          tableGroup = toolbar.addVisualGroup("TableGroup");
      }catch (Exception exc) {
          tableGroup = toolbar.getVisualGroup("TableGroup");
      }
      toolbar.setSeparation(tableGroup, new Dimension(2, 10));

      // Find in jTable
      find = new ESlateButton(dBase.findAction.getEnabledIcon());
      find.setEnabled(dBase.findAction.isEnabled());
      find.setToolTipText(infoBundle.getString("DatabaseMsg105"));
      find.setDisabledIcon(dBase.findAction.getDisabledIcon());
      find.setMargin(new Insets(0,0,0,0));
      find.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      find.setRequestFocusEnabled(false);
      find.setBorderPainted(false);
      toolbar.add(tableGroup, find, infoBundle.getString("DatabaseMsg105"));
      toolbar.setAssociatedText(find, infoBundle.getString("DatabaseMsg105"));

      // Find previous in jTable
      findPrev = new ESlateButton(dBase.findPrevAction.getEnabledIcon());
      findPrev.setEnabled(dBase.findPrevAction.isEnabled());
      findPrev.setToolTipText(infoBundle.getString("DatabaseMsg106"));
      findPrev.setDisabledIcon(dBase.findPrevAction.getDisabledIcon());
      findPrev.setMargin(new Insets(0,0,0,0));
      findPrev.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      findPrev.setRequestFocusEnabled(false);
      findPrev.setBorderPainted(false);
      toolbar.add(tableGroup, findPrev, infoBundle.getString("DatabaseMsg106"));
      toolbar.setAssociatedText(findPrev, infoBundle.getString("DatabaseMsg106"));

      // Find next in jTable
      findNext = new ESlateButton(dBase.findNextAction.getEnabledIcon());
      findNext.setEnabled(dBase.findNextAction.isEnabled());
      findNext.setToolTipText(infoBundle.getString("DatabaseMsg107"));
      findNext.setDisabledIcon(dBase.findNextAction.getDisabledIcon());
      findNext.setMargin(new Insets(0,0,0,0));
      findNext.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      findNext.setRequestFocusEnabled(false);
      findNext.setBorderPainted(false);
      toolbar.add(tableGroup, findNext, infoBundle.getString("DatabaseMsg107"));
      toolbar.setAssociatedText(findNext, infoBundle.getString("DatabaseMsg107"));

      // Record Group
      VisualGroup recordGroup = null;
      try{
          recordGroup = toolbar.addVisualGroup("RecordGroup");
      }catch (Exception exc) {
          recordGroup = toolbar.getVisualGroup("RecordGroup");
      }
      toolbar.setSeparation(recordGroup, new Dimension(2, 10));

      // Add record
      addRecord = new ESlateButton(dBase.addRecordAction.getEnabledIcon());
      addRecord.setEnabled(dBase.addRecordAction.isEnabled());
      addRecord.setToolTipText(infoBundle.getString("DatabaseMsg108"));
      addRecord.setDisabledIcon(dBase.addRecordAction.getDisabledIcon());
      addRecord.setMargin(new Insets(0,0,0,0));
      addRecord.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      addRecord.setRequestFocusEnabled(false);
      addRecord.setBorderPainted(false);
      toolbar.add(recordGroup, addRecord, infoBundle.getString("DatabaseMsg108"));
      toolbar.setAssociatedText(addRecord, infoBundle.getString("DatabaseMsg108"));

      // Remove active record
      removeRecord = new ESlateButton(dBase.removeRecordAction.getEnabledIcon());
      removeRecord.setEnabled(dBase.removeRecordAction.isEnabled());
      removeRecord.setToolTipText(infoBundle.getString("DatabaseMsg109"));
      removeRecord.setDisabledIcon(dBase.removeRecordAction.getDisabledIcon());
      removeRecord.setMargin(new Insets(0,0,0,0));
      removeRecord.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      removeRecord.setRequestFocusEnabled(false);
      removeRecord.setBorderPainted(false);
      toolbar.add(recordGroup, removeRecord, infoBundle.getString("DatabaseMsg109"));
      toolbar.setAssociatedText(removeRecord, infoBundle.getString("DatabaseMsg109"));

      // Remove selected records
      removeSelectedRecords = new ESlateButton(dBase.removeSelRecordAction.getEnabledIcon());
      removeSelectedRecords.setEnabled(dBase.removeSelRecordAction.isEnabled());
      removeSelectedRecords.setToolTipText(infoBundle.getString("DatabaseMsg110"));
      removeSelectedRecords.setDisabledIcon(dBase.removeSelRecordAction.getDisabledIcon());
      removeSelectedRecords.setMargin(new Insets(0,0,0,0));
      removeSelectedRecords.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      removeSelectedRecords.setRequestFocusEnabled(false);
      removeSelectedRecords.setBorderPainted(false);
      toolbar.add(recordGroup, removeSelectedRecords, infoBundle.getString("DatabaseMsg110"));
      toolbar.setAssociatedText(removeSelectedRecords, infoBundle.getString("DatabaseMsg110"));

      // Field Group
      VisualGroup fieldGroup = null;
      try{
          fieldGroup = toolbar.addVisualGroup("FieldGroup");
      }catch (Exception exc) {
          fieldGroup = toolbar.getVisualGroup("FieldGroup");
      }
      toolbar.setSeparation(fieldGroup, new Dimension(2, 10));

      // Add new field
      addField = new ESlateButton(dBase.addFieldAction.getEnabledIcon());
      addField.setEnabled(dBase.addFieldAction.isEnabled());
      addField.setToolTipText(infoBundle.getString("DatabaseMsg111"));
      addField.setDisabledIcon(dBase.addFieldAction.getDisabledIcon());
      addField.setMargin(new Insets(0,0,0,0));
      addField.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      addField.setRequestFocusEnabled(false);
      addField.setBorderPainted(false);
      toolbar.add(fieldGroup, addField, infoBundle.getString("DatabaseMsg111"));
      toolbar.setAssociatedText(addField, infoBundle.getString("DatabaseMsg111"));

      // Remove field
      removeField = new ESlateButton(dBase.removeFieldAction.getEnabledIcon());
      removeField.setEnabled(dBase.removeFieldAction.isEnabled());
      removeField.setToolTipText(infoBundle.getString("DatabaseMsg112"));
      removeField.setDisabledIcon(dBase.removeFieldAction.getDisabledIcon());
      removeField.setMargin(new Insets(0,0,0,0));
      removeField.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      removeField.setRequestFocusEnabled(false);
      removeField.setBorderPainted(false);
      toolbar.add(fieldGroup, removeField, infoBundle.getString("DatabaseMsg112"));
      toolbar.setAssociatedText(removeField, infoBundle.getString("DatabaseMsg112"));

      // Field properties
      fieldProperties = new ESlateButton(dBase.fieldPropertiesAction.getEnabledIcon());
      fieldProperties.setEnabled(dBase.fieldPropertiesAction.isEnabled());
      fieldProperties.setToolTipText(infoBundle.getString("DatabaseMsg113"));
      fieldProperties.setDisabledIcon(dBase.fieldPropertiesAction.getDisabledIcon());
      fieldProperties.setMargin(new Insets(0,0,0,0));
      fieldProperties.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      fieldProperties.setRequestFocusEnabled(false);
      fieldProperties.setBorderPainted(false);
      toolbar.add(fieldGroup, fieldProperties, infoBundle.getString("DatabaseMsg113"));
      toolbar.setAssociatedText(fieldProperties, infoBundle.getString("DatabaseMsg113"));

      // Record Group
      VisualGroup selectionGroup = null;
      try{
          selectionGroup = toolbar.addVisualGroup("SelectionGroup");
      }catch (Exception exc) {
          selectionGroup = toolbar.getVisualGroup("SelectionGroup");
      }
      toolbar.setSeparation(selectionGroup, new Dimension(2, 10));

      // Select all records
      selectAllRecs = new ESlateButton(dBase.selectAllRecsAction.getEnabledIcon());
      selectAllRecs.setEnabled(dBase.selectAllRecsAction.isEnabled());
      selectAllRecs.setToolTipText(infoBundle.getString("DatabaseMsg114"));
      selectAllRecs.setDisabledIcon(dBase.selectAllRecsAction.getDisabledIcon());
      selectAllRecs.setMargin(new Insets(0,0,0,0));
      selectAllRecs.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      selectAllRecs.setRequestFocusEnabled(false);
      selectAllRecs.setBorderPainted(false);
      toolbar.add(selectionGroup, selectAllRecs, infoBundle.getString("DatabaseMsg114"));
      toolbar.setAssociatedText(selectAllRecs, infoBundle.getString("DatabaseMsg114"));

      // Clear record selection
      clearRecSelection = new ESlateButton(dBase.clearRecAction.getEnabledIcon());
      clearRecSelection.setEnabled(dBase.clearRecAction.isEnabled());
      clearRecSelection.setToolTipText(infoBundle.getString("DatabaseMsg115"));
      clearRecSelection.setDisabledIcon(dBase.clearRecAction.getDisabledIcon());
      clearRecSelection.setMargin(new Insets(0,0,0,0));
      clearRecSelection.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      clearRecSelection.setRequestFocusEnabled(false);
      clearRecSelection.setBorderPainted(false);
      toolbar.add(selectionGroup, clearRecSelection, infoBundle.getString("DatabaseMsg115"));
      toolbar.setAssociatedText(clearRecSelection, infoBundle.getString("DatabaseMsg115"));

      // Invert record selection
      invertRecSelection = new ESlateButton(dBase.invertRecAction.getEnabledIcon());
      invertRecSelection.setEnabled(dBase.invertRecAction.isEnabled());
      invertRecSelection.setToolTipText(infoBundle.getString("DatabaseMsg116"));
      invertRecSelection.setDisabledIcon(dBase.invertRecAction.getDisabledIcon());
      invertRecSelection.setMargin(new Insets(0,0,0,0));
      invertRecSelection.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      invertRecSelection.setRequestFocusEnabled(false);
      invertRecSelection.setBorderPainted(false);
      toolbar.add(selectionGroup, invertRecSelection, infoBundle.getString("DatabaseMsg116"));
      toolbar.setAssociatedText(invertRecSelection, infoBundle.getString("DatabaseMsg116"));

      // Promote selected records
      promoteSelectedRecs = new ESlateButton(dBase.promoteSelRecsAction.getEnabledIcon());
      promoteSelectedRecs.setEnabled(dBase.promoteSelRecsAction.isEnabled());
      promoteSelectedRecs.setToolTipText(infoBundle.getString("DatabaseMsg117"));
      promoteSelectedRecs.setDisabledIcon(dBase.promoteSelRecsAction.getDisabledIcon());
      promoteSelectedRecs.setMargin(new Insets(0,0,0,0));
      promoteSelectedRecs.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      promoteSelectedRecs.setRequestFocusEnabled(false);
      promoteSelectedRecs.setBorderPainted(false);
      toolbar.add(selectionGroup, promoteSelectedRecs, infoBundle.getString("DatabaseMsg117"));
      toolbar.setAssociatedText(promoteSelectedRecs, infoBundle.getString("DatabaseMsg117"));

      // Record Group
      VisualGroup imageEditGroup = null;
      try{
          imageEditGroup = toolbar.addVisualGroup("ImageEditGroup");
      }catch (Exception exc) {
          imageEditGroup = toolbar.getVisualGroup("ImageEditGroup");
      }
      toolbar.setSeparation(imageEditGroup, new Dimension(2, 10));

      // Image edit
      imageEdit = new ESlateButton(dBase.imageEditAction.getEnabledIcon());
      imageEdit.setEnabled(dBase.imageEditAction.isEnabled());
      imageEdit.setToolTipText(infoBundle.getString("Icon Editor"));
      imageEdit.setDisabledIcon(dBase.imageEditAction.getDisabledIcon());
      imageEdit.setMargin(new Insets(0,0,0,0));
      imageEdit.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
      imageEdit.setRequestFocusEnabled(false);
      imageEdit.setBorderPainted(false);
      toolbar.add(imageEditGroup, imageEdit, infoBundle.getString("Icon Editor"));
      toolbar.setAssociatedText(imageEdit, infoBundle.getString("Icon Editor"));
      
      // Insert file path
      createInsFilePathButton();
    }
  
  	private void createInsFilePathButton() {
  		if (insFilePath==null) {
  	      VisualGroup imageEditGroup = null;
  	      try{
  	          imageEditGroup = toolbar.addVisualGroup("ImageEditGroup");
  	      }catch (Exception exc) {
  	          imageEditGroup = toolbar.getVisualGroup("ImageEditGroup");
  	      }
  	      insFilePath = new ESlateButton(dBase.insFilePathAction.getEnabledIcon());
  	      insFilePath.setEnabled(dBase.insFilePathAction.isEnabled());
  	      insFilePath.setToolTipText(infoBundle.getString("Insert File Path"));
  	      insFilePath.setDisabledIcon(dBase.insFilePathAction.getDisabledIcon());
  	      insFilePath.setMargin(new Insets(0,0,0,0));
  	      insFilePath.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
  	      insFilePath.setRequestFocusEnabled(false);
  	      insFilePath.setBorderPainted(false);
  	      toolbar.add(imageEditGroup, insFilePath, infoBundle.getString("Insert File Path"));
  	      toolbar.setAssociatedText(insFilePath, infoBundle.getString("Insert File Path"));
  		}
  	}

    void attachStandardToolBarActions() {
        /*toolbar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getModifiers() == e.BUTTON3_MASK) {
                    if (toolbar.isVisible())
                        dBase.standardToolBarMenuItem.setSelected(true);
                    if (dBase.isFormattingToolBarVisible())
                        dBase.formattingToolBarMenuItem.setSelected(true);
                    dBase.toolBarMenu.show(toolbar, e.getX(), e.getY());
                }
            }
        });*/

        /* Add the action listeners for all the standard toolbar buttons
         */
        newDB.addActionListener(dBase.newDBAction);
        openDB.addActionListener(dBase.openDBAction);
        saveDB.addActionListener(dBase.saveDBAction);

        cut.addActionListener(dBase.cutAction);
        copy.addActionListener(dBase.copyAction);
        paste.addActionListener(dBase.pasteAction);
        sortAsc.addActionListener(dBase.sortAscAction);
        sortDesc.addActionListener(dBase.sortDescAction);
        find.addActionListener(dBase.findAction);
        findPrev.addActionListener(dBase.findPrevAction);
        findNext.addActionListener(dBase.findNextAction);

        addRecord.addActionListener(dBase.addRecordAction);
        removeRecord.addActionListener(dBase.removeRecordAction);
        removeSelectedRecords.addActionListener(dBase.removeSelRecordAction);
        addField.addActionListener(dBase.addFieldAction);
        removeField.addActionListener(dBase.removeFieldAction);
        clearRecSelection.addActionListener(dBase.clearRecAction);

        fieldProperties.addActionListener(dBase.fieldPropertiesAction);
        selectAllRecs.addActionListener(dBase.selectAllRecsAction);
        invertRecSelection.addActionListener(dBase.invertRecAction);
        promoteSelectedRecs.addActionListener(dBase.promoteSelRecsAction);

        imageEdit.addActionListener(dBase.imageEditAction);
        createInsFilePathButton();
        insFilePath.addActionListener(dBase.insFilePathAction);
    }

    public void updateIconEditStatus() {
        if (dBase.activeDBTable == null || dBase.activeDBTable.jTable.getSelectedColumn() == -1) {
            if (imageEdit != null)
                imageEdit.setEnabled(false);
            return;
        }
        String fieldName;
//1        Array columnOrder = dBase.activeDBTable.viewStructure.tableView.getColumnOrder();
        AbstractTableField fld = dBase.activeDBTable.getTableField(dBase.activeDBTable.activeColumn);
/*1        try{
            fieldName = dBase.activeDBTable.tableModel.getColumnName(((Integer) columnOrder.at(dBase.activeDBTable.activeColumn)).intValue());
        }catch (Exception exc) {
            System.out.println("Serious error because of JTable behaviour when removing columns. Correcting...");
            if (imageEdit != null)
                imageEdit.setEnabled(false);
            return;
        }
        AbstractTableField fld;
        try{
            fld = dBase.activeDBTable.table.getTableField(fieldName);
        }catch (InvalidFieldNameException e) {
            System.out.println("Serious inconsistency error in DBase setCutCopyPasteStatus(): 1");
            return;
        }
1*/
        if (imageEdit != null){
            if (dBase.iconEditorClass != null && fld.isEditable() && fld.getDataType().getName().equals("gr.cti.eslate.database.engine.CImageIcon") && dBase.activeDBTable.table.isDataChangeAllowed())
                imageEdit.setEnabled(true);
            else
                imageEdit.setEnabled(false);
        }
    }

    public void updateInsertFilePathStatus() {
    	if (dBase.activeDBTable == null || dBase.activeDBTable.jTable.getSelectedColumn() == -1) {
    		if (insFilePath != null)
    			insFilePath.setEnabled(false);
    		return;
    	}
    	AbstractTableField fld = dBase.activeDBTable.getTableField(dBase.activeDBTable.activeColumn);
    	if (insFilePath != null){
    		if (fld.isEditable() && fld.getDataType().getName().equals("java.lang.String") && dBase.activeDBTable.table.isDataChangeAllowed())
    			insFilePath.setEnabled(true);
    		else
    			insFilePath.setEnabled(false);
    	}
    }
    
    public void setNewDBEnabled(boolean b){
        if (newDB != null)
            newDB.setEnabled(b);
    }
    public boolean isNewDBEnabled(){
        if (newDB != null)
            return newDB.isEnabled();
        return false;
    }
    public void setOpenDBEnabled(boolean b){
        if (openDB != null)
            openDB.setEnabled(b);
    }
    public boolean isOpenDBEnabled(){
        if (openDB != null)
            return openDB.isEnabled();
        return false;
    }
    public void setSaveDBEnabled(boolean b){
        if (saveDB != null)
            saveDB.setEnabled(b);
    }
    public boolean isSaveDBEnabled(){
        if (saveDB != null)
            return saveDB.isEnabled();
        return false;
    }
    public void setCutEnabled(boolean b){
        if (cut != null)
            cut.setEnabled(b);
    }
    public boolean isCutEnabled(){
        if (cut != null)
            return cut.isEnabled();
        return false;
    }
    public void setCopyEnabled(boolean b){
        if (copy != null)
            copy.setEnabled(b);
    }
    public boolean isCopyEnabled(){
        if (copy != null)
            return copy.isEnabled();
        return false;
    }
    public void setPasteEnabled(boolean b){
        if (paste != null)
            paste.setEnabled(b);
    }
    public boolean isPasteEnabled(){
        if (paste != null)
            return paste.isEnabled();
        return false;
    }
    public void setSortAscEnabled(boolean b){
        if (sortAsc != null)
            sortAsc.setEnabled(b);
    }
    public boolean isSortAscEnabled(){
        if (sortAsc != null)
            return sortAsc.isEnabled();
        return false;
    }
    public void setSortDescEnabled(boolean b){
        if (sortDesc != null)
            sortDesc.setEnabled(b);
    }
    public boolean isSortDescEnabled(){
        if (sortDesc != null)
            return sortDesc.isEnabled();
        return false;
    }
    public void setFindEnabled(boolean b){
        if (find != null)
            find.setEnabled(b);
    }
    public boolean isFindEnabled(){
        if (find != null)
            return find.isEnabled();
        return false;
    }
    public void setFindNextEnabled(boolean b){
        if (findNext != null)
            findNext.setEnabled(b);
    }
    public boolean isFindNextEnabled(){
        if (findNext != null)
            return findNext.isEnabled();
        return false;
    }
    public void setFindPrevEnabled(boolean b){
        if (findPrev != null)
            findPrev.setEnabled(b);
    }
    public boolean isFindPrevEnabled(){
        if (findPrev != null)
            return findPrev.isEnabled();
        return false;
    }
    public void setAddRecordEnabled(boolean b){
        if (addRecord != null)
            addRecord.setEnabled(b);
    }
    public boolean isAddRecordEnabled(){
        if (addRecord != null)
            return addRecord.isEnabled();
        return false;
    }
    public void setRemoveRecordEnabled(boolean b){
        if (removeRecord != null)
            removeRecord.setEnabled(b);
    }
    public boolean isRemoveRecordEnabled(){
        if (removeRecord != null)
            return removeRecord.isEnabled();
        return false;
    }
    public void setRemoveSelectedRecordsEnabled(boolean b){
        if (removeSelectedRecords != null)
            removeSelectedRecords.setEnabled(b);
    }
    public boolean isRemoveSelectedRecordsEnabled(){
        if (removeSelectedRecords != null)
            return removeSelectedRecords.isEnabled();
        return false;
    }
    public void setImageEditEnabled(boolean b){
        if (imageEdit != null)
            imageEdit.setEnabled(b);
    }
    public boolean isImageEditEnabled(){
        if (imageEdit != null)
            return imageEdit.isEnabled();
        return false;
    }
    public void setInsertFilePathEnabled(boolean b){
    	if (insFilePath != null)
    		insFilePath.setEnabled(b);
    }
    public boolean isInsertFilePathEnabled(){
    	if (insFilePath != null)
    		return insFilePath.isEnabled();
    	return false;
    }
    public void setAddFieldEnabled(boolean b){
        if (addField != null)
            addField.setEnabled(b);
    }
    public boolean isAddFieldEnabled(){
        if (addField != null)
            return addField.isEnabled();
        return false;
    }
    public void setRemoveFieldEnabled(boolean b){
        if (removeField != null)
            removeField.setEnabled(b);
    }
    public boolean isRemoveFieldEnabled(){
        if (removeField != null)
            return removeField.isEnabled();
        return false;
    }
    public void setFieldPropertiesEnabled(boolean b){
        if (fieldProperties != null)
            fieldProperties.setEnabled(b);
    }
    public boolean isFieldPropertiesEnabled(){
        if (fieldProperties != null)
            return fieldProperties.isEnabled();
        return false;
    }
    public void setClearRecSelectionEnabled(boolean b){
        if (clearRecSelection != null)
            clearRecSelection.setEnabled(b);
    }
    public boolean isClearRecSelectionEnabled(){
        if (clearRecSelection != null)
            return clearRecSelection.isEnabled();
        return false;
    }
    public void setSelectAllRecsEnabled(boolean b){
        if (selectAllRecs != null)
            selectAllRecs.setEnabled(b);
    }
    public boolean isSelectAllRecsEnabled(){
        if (selectAllRecs != null)
            return selectAllRecs.isEnabled();
        return false;
    }
    public void setInvertRecSelectionEnabled(boolean b){
        if (invertRecSelection != null)
            invertRecSelection.setEnabled(b);
    }
    public boolean isInvertRecSelectionEnabled(){
        if (invertRecSelection != null)
            return invertRecSelection.isEnabled();
        return false;
    }
    public void setPromoteSelectedRecsEnabled(boolean b){
        if (promoteSelectedRecs != null)
            promoteSelectedRecs.setEnabled(b);
    }
    public boolean isPromoteSelectedRecsEnabled(){
        if (promoteSelectedRecs != null)
            return promoteSelectedRecs.isEnabled();
        return false;
    }
}