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
import gr.cti.eslate.eslateComboBox.ESlateComboBox;
import gr.cti.eslate.eslateToggleButton.ESlateToggleButton;
import gr.cti.eslate.eslateToolBar.DefaultStateSetter;
import gr.cti.eslate.eslateToolBar.ESlateToolBar;
import gr.cti.eslate.eslateToolBar.ToolLocation;
import gr.cti.eslate.eslateToolBar.VisualGroup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

public class FormattingToolbarController implements DefaultStateSetter{

    Database dBase;
    private ESlateToolBar toolbar;
    ResourceBundle infoBundle;
    String[] fontNames;
    private ESlateComboBox fontType, fontSize;
    private ESlateToggleButton bold, italic;
    private ESlateButton backColor, foreColor, gridColor, selectionColor, activeRecColor, gridChooser;
    private ESlateToggleButton rowColumnSelection, hightLightNonEditableFields;

    protected static int FORMATTING_TOOLBAR_VISIBLE_POS = 4; //IE
    protected final static int BACKGROUND_COLOR_TYPE = 0; //IE
    protected final static int FOREGROUND_COLOR_TYPE = 1; //IE
    protected final static int GRID_COLOR_TYPE = 2; //IE
    protected final static int SELECTION_COLOR_TYPE = 3; //IE
    protected final static int ACTIVE_RECORD_COLOR_TYPE = 4; //IE
    protected final static int NUMBER_COLOR_TYPE = 5; //IE
    protected final static int ALPHANUMERIC_COLOR_TYPE = 6; //IE
    protected final static int BOOLEAN_COLOR_TYPE = 7; //IE
    protected final static int DATE_COLOR_TYPE = 8; //IE
    protected final static int TIME_COLOR_TYPE = 9; //IE
    protected final static int URL_COLOR_TYPE = 10; //IEprotected


    public FormattingToolbarController(Database dB) {
        dBase = dB;
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", Locale.getDefault());
        fontNames =  GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    }

    public void createToolbar(){
        toolbar = new ESlateToolBar("Formatting");
        toolbar.setFocusable(false);
        toolbar.setDefaultStateSetter(this);
        setDefaultState(toolbar);
    }

    public void setToolbar(ESlateToolBar bar){
        toolbar = bar;
        toolbar.setDefaultStateSetter(this);
        toolbar.setFocusable(false);
        /*toolbar.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
        if (e.getModifiers() == e.BUTTON3_MASK) {
        if (dBase.isStandardToolbarVisible())
        dBase.standardToolBarMenuItem.setSelected(true);
        if (toolbar.isVisible())
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
        if (toolbar.getTool("FontType") != null)
            fontType = (ESlateComboBox)toolbar.getTool("FontType");
        if (toolbar.getTool("FontSize") != null)
            fontSize = (ESlateComboBox)toolbar.getTool("FontSize");
        if (toolbar.getTool("Bold") != null)
            bold = (ESlateToggleButton)toolbar.getTool("Bold");
        if (toolbar.getTool("Italic") != null)
            italic = (ESlateToggleButton)toolbar.getTool("Italic");
        if (toolbar.getTool("Table background color") != null)
            backColor = (ESlateButton)toolbar.getTool("Table background color");
        if (toolbar.getTool("Table foreground color") != null)
            foreColor = (ESlateButton)toolbar.getTool("Table foreground color");
        if (toolbar.getTool("Table grid color") != null)
            gridColor = (ESlateButton)toolbar.getTool("Table grid color");
        if (toolbar.getTool("Table selection color") != null)
            selectionColor = (ESlateButton)toolbar.getTool("Table selection color");
        if (toolbar.getTool("Table active record color") != null)
            activeRecColor = (ESlateButton)toolbar.getTool("Table active record color");
        if (toolbar.getTool("Grid type") != null)
            gridChooser = (ESlateButton)toolbar.getTool("Grid type");
        if (toolbar.getTool("Selection type") != null)
            rowColumnSelection = (ESlateToggleButton)toolbar.getTool("Selection type");
        if (toolbar.getTool("Hightlight non-editable fields") != null)
            hightLightNonEditableFields = (ESlateToggleButton)toolbar.getTool("Hightlight non-editable fields");
    }
    
  	/**
	 * Returns an array containing the current locations of the tools in the toolbar.
	 * 
	 * @return An array containing the current locations of the tools in the toolbar.
	 */
	ToolLocation[] getToolLocations() {
		int nTools=12;
		ToolLocation loc[]=new ToolLocation[nTools];
		loc[0]=toolbar.locateTool(fontType);
        loc[1]=toolbar.locateTool(fontSize);
        loc[2]=toolbar.locateTool(bold);
        loc[3]=toolbar.locateTool(italic);
        loc[4]=toolbar.locateTool(backColor);
        loc[5]=toolbar.locateTool(foreColor);
        loc[6]=toolbar.locateTool(gridColor);
        loc[7]=toolbar.locateTool(selectionColor);
        loc[8]=toolbar.locateTool(activeRecColor);
        loc[9]=toolbar.locateTool(gridChooser);
        loc[10]=toolbar.locateTool(rowColumnSelection);
        loc[11]=toolbar.locateTool(hightLightNonEditableFields);
        return loc;
	}

	  /**
		 * Identifies the tools in a toolbar that has just been loaded, updating the references to these tools.
		 * 
		 * @param loc
		 *            The locations of the tools in the toolbar.
		 */
	  void identifyTools(ToolLocation[] loc) {
			VisualGroup[] groups=toolbar.getVisualGroups();
			if (loc[0]!=null) {
				fontType=(ESlateComboBox) (groups[loc[0].visualGroup].getComponent(loc[0].toolIndex));
			} else {
				fontType=null;
			}
			if (loc[1]!=null) {
				fontSize=(ESlateComboBox) (groups[loc[1].visualGroup].getComponent(loc[1].toolIndex));
			} else {
				fontSize=null;
			}
			if (loc[2]!=null) {
				bold=(ESlateToggleButton) (groups[loc[2].visualGroup].getComponent(loc[2].toolIndex));
			} else {
				bold=null;
			}
			if (loc[3]!=null) {
				italic=(ESlateToggleButton) (groups[loc[3].visualGroup].getComponent(loc[3].toolIndex));
			} else {
				italic=null;
			}
			if (loc[4]!=null) {
				backColor=(ESlateButton) (groups[loc[4].visualGroup].getComponent(loc[4].toolIndex));
			} else {
				backColor=null;
			}
			if (loc[5]!=null) {
				foreColor=(ESlateButton) (groups[loc[5].visualGroup].getComponent(loc[5].toolIndex));
			} else {
				foreColor=null;
			}
			if (loc[6]!=null) {
				gridColor=(ESlateButton) (groups[loc[6].visualGroup].getComponent(loc[6].toolIndex));
			} else {
				gridColor=null;
			}
			if (loc[7]!=null) {
				selectionColor=(ESlateButton) (groups[loc[7].visualGroup].getComponent(loc[7].toolIndex));
			} else {
				selectionColor=null;
			}
			if (loc[8]!=null) {
				activeRecColor=(ESlateButton) (groups[loc[8].visualGroup].getComponent(loc[8].toolIndex));
			} else {
				activeRecColor=null;
			}
			if (loc[9]!=null) {
				gridChooser=(ESlateButton) (groups[loc[9].visualGroup].getComponent(loc[9].toolIndex));
			} else {
				gridChooser=null;
			}
			if (loc[10]!=null) {
				rowColumnSelection=(ESlateToggleButton) (groups[loc[10].visualGroup].getComponent(loc[10].toolIndex));
			} else {
				rowColumnSelection=null;
			}
			if (loc[11]!=null) {
				hightLightNonEditableFields=(ESlateToggleButton) (groups[loc[11].visualGroup].getComponent(loc[11].toolIndex));
			} else {
				hightLightNonEditableFields=null;
			}
	  }
    public String getFontName() {
        return fontType.getSelectedItem().toString();
    }

    public String getFontSize() {
        Object selectedSize = fontSize.getSelectedItem();
        if (selectedSize == null) return null;
        return selectedSize.toString();
    }

    public boolean isBoldSelected(){
        return bold.isSelected();
    }

    public boolean isItalicSelected(){
        return italic.isSelected();
    }

    public ESlateToolBar getToolbar(){
        return toolbar;
    }

    public void destroyToolbar(){
        toolbar = null;
    }

    public void setDefaultState(){
        setDefaultState(new ESlateToolBar("Formatting"));
        toolbar.setDefaultStateSetter(this);
    }

    public void setToolBarStatus() {
        if (!toolbar.isVisible())
            return;

        if (dBase.activeDBTable == null) {
            if (fontType != null){
                fontType.setSelectedIndex(-1);
                fontType.setEnabled(false);
            }
            if (fontSize != null){
                fontSize.setSelectedIndex(-1);
                fontSize.setEnabled(false);
            }
            if (bold != null){
                bold.setBorderPainted(false);
                bold.setSelected(false);
                bold.setEnabled(false);
            }
            if (italic != null){
                italic.setBorderPainted(false);
                italic.setSelected(false);
                italic.setEnabled(false);
            }
            if (backColor != null)
                backColor.setEnabled(false);
            if (foreColor != null)
                foreColor.setEnabled(false);
            if (gridColor != null)
                gridColor.setEnabled(false);
            if (selectionColor != null)
                selectionColor.setEnabled(false);
            if (activeRecColor != null)
                activeRecColor.setEnabled(false);
            if (gridChooser != null)
                gridChooser.setEnabled(false);
            if (rowColumnSelection != null){
                rowColumnSelection.setBorderPainted(false);
                rowColumnSelection.setSelected(false);
                rowColumnSelection.setEnabled(false);
            }
            if (hightLightNonEditableFields != null){
                hightLightNonEditableFields.setBorderPainted(false);
                hightLightNonEditableFields.setSelected(false);
                hightLightNonEditableFields.setEnabled(false);
            }
        }else{
            if (fontType != null)
                fontType.setEnabled(true);
            if (fontSize != null)
                fontSize.setEnabled(true);
            if (bold != null)
                bold.setEnabled(true);
            if (italic != null)
                italic.setEnabled(true);
            if (backColor != null)
                backColor.setEnabled(true);
            if (foreColor != null)
                foreColor.setEnabled(true);
            if (gridColor != null)
                gridColor.setEnabled(true);
            if (selectionColor != null)
                selectionColor.setEnabled(true);
            if (activeRecColor != null)
                activeRecColor.setEnabled(true);
            if (gridChooser != null)
                gridChooser.setEnabled(true);
            if (rowColumnSelection != null)
                rowColumnSelection.setEnabled(true);
            if (hightLightNonEditableFields != null)
                hightLightNonEditableFields.setEnabled(true);
            updateToolBarStatus();
        }
    }

    public void updateToolBarStatus() {
        if (!toolbar.isVisible() || dBase.activeDBTable == null)
            return;

        Font activeTableFont = dBase.activeDBTable.getTableFont();
        dBase.iterateEvent = false;
        DefaultComboBoxModel model = (DefaultComboBoxModel) fontType.getModel();
        int index = model.getIndexOf(activeTableFont.getFontName());
/* If the name of the font is not directly found, then do some caseless
* search. The reason is that the SanSerif font's name is reported as
* 'sanserif', i.e. in lower case, and this is the default font of any
* new jTable.
*/
        if (index == -1) {
            int length = model.getSize();
            for (int i=0; i<length; i++) {
                if (((String) model.getElementAt(i)).equalsIgnoreCase(activeTableFont.getFontName())) {
                    index = i;
                    break;
                }
            }
        }
        if (fontType != null)
            fontType.setSelectedIndex(index);
        if (fontSize != null) {
            fontSize.setSelectedItem(new Integer(activeTableFont.getSize()).toString());
        }
        dBase.iterateEvent = true;

        boolean boo = activeTableFont.isBold();
        if (bold != null){
            if (boo)
                bold.setBorderPainted(true);
            else
                bold.setBorderPainted(false);
            bold.setSelected(boo);
        }

        if (italic != null){
            boo = activeTableFont.isItalic();
            if (boo)
                italic.setBorderPainted(true);
            else
                italic.setBorderPainted(false);
            italic.setSelected(boo);
        }

        boo = dBase.activeDBTable.isSimultaneousFieldRecordActivation();

        if (rowColumnSelection != null){
            if (boo)
                rowColumnSelection.setBorderPainted(true);
            else
                rowColumnSelection.setBorderPainted(false);
            rowColumnSelection.setSelected(boo);
        }

        boo = dBase.activeDBTable.isNonEditableFieldsHighlighted();

        if (hightLightNonEditableFields != null){
            if (boo)
                hightLightNonEditableFields.setBorderPainted(true);
            else
                hightLightNonEditableFields.setBorderPainted(false);
            hightLightNonEditableFields.setSelected(boo);
        }
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
        attachFormattingToolBarActions();

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
        // Font Group
        VisualGroup fontGroup = null;
        try{
            fontGroup = toolbar.addVisualGroup("FontGroup");
        }catch (Exception exc) {
            fontGroup = toolbar.getVisualGroup("FontGroup");
        }
        toolbar.setSeparation(fontGroup, new Dimension(2, 10));

        fontType = new ESlateComboBox();
        Dimension fontTypeDim = new Dimension(130, 20);
        fontType.setPreferredSize(fontTypeDim);
        fontType.setMinimumSize(fontTypeDim);
        fontType.setMaximumSize(fontTypeDim);
        fontType.setModel(new DefaultComboBoxModel(fontNames));
        fontType.setRequestFocusEnabled(false);
        fontType.setFocusable(false);

        if (dBase != null){
            if (dBase.activeDBTable != null){
                Font activeTableFont = dBase.activeDBTable.getTableFont();
                dBase.iterateEvent = false;
                DefaultComboBoxModel model = (DefaultComboBoxModel) fontType.getModel();
                int index = model.getIndexOf(activeTableFont.getFontName());
/* If the name of the font is not directly found, then do some caseless
* search. The reason is that the SanSerif font's name is reported as
* 'sanserif', i.e. in lower case, and this is the default font of any
* new jTable.
*/
                if (index == -1) {
                    int length = model.getSize();
                    for (int i=0; i<length; i++) {
                        if (((String) model.getElementAt(i)).equalsIgnoreCase(activeTableFont.getFontName())) {
                            index = i;
                            break;
                        }
                    }
                }
                fontType.setSelectedIndex(index);
            }
        }
        fontType.setToolTipText(infoBundle.getString("FontType"));
        toolbar.add(fontGroup, fontType, infoBundle.getString("FontType"));
        toolbar.setAssociatedText(fontType, infoBundle.getString("FontType"));

        fontSize = new ESlateComboBox();
        Dimension fontSizeDim = new Dimension(42, 20);
        fontSize.setPreferredSize(fontSizeDim);
        fontSize.setMinimumSize(fontSizeDim);
        fontSize.setMaximumSize(fontSizeDim);

        String[] fontSizes = new String[] {"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "24", "36", "72"};
        fontSize.setModel(new DefaultComboBoxModel(fontSizes));
        fontSize.setRequestFocusEnabled(false);
        fontSize.setFocusable(false);
        fontSize.setSelectedIndex(4);
        fontSize.setToolTipText(infoBundle.getString("FontSize"));
        toolbar.add(fontGroup, fontSize, infoBundle.getString("FontSize"));
        toolbar.setAssociatedText(fontSize, infoBundle.getString("FontSize"));

        // Bold button
        ImageIcon boldIcon = new ImageIcon(getClass().getResource("images/toolbar/bold.gif"));
        ImageIcon boldDisabledIcon = new ImageIcon(getClass().getResource("images/toolbar/boldDisabled.gif"));
        bold = new ESlateToggleButton();
        bold.setIcon(boldIcon);
        bold.setDisabledIcon(boldDisabledIcon);
        bold.setMargin(new Insets(0,0,0,0));
        bold.setFocusable(false);
        bold.setToolTipText(infoBundle.getString("Bold"));
        toolbar.add(fontGroup, bold, infoBundle.getString("Bold"));
        toolbar.setAssociatedText(bold, infoBundle.getString("Bold"));

        // Italic button
        ImageIcon italicIcon = new ImageIcon(getClass().getResource("images/toolbar/italic.gif"));
        ImageIcon italicDisabledIcon = new ImageIcon(getClass().getResource("images/toolbar/italicDisabled.gif"));
        italic = new ESlateToggleButton(italicIcon);
        italic.setDisabledIcon(italicDisabledIcon);
        italic.setMargin(new Insets(0,0,0,0));
        italic.setFocusable(false);
        italic.setToolTipText(infoBundle.getString("Italic"));
        toolbar.add(fontGroup, italic, infoBundle.getString("Italic"));
        toolbar.setAssociatedText(italic, infoBundle.getString("Italic"));

        // The jTable color button group
        VisualGroup tableColorGroup = null;
        try{
            tableColorGroup = toolbar.addVisualGroup("TableColorGroup");
        }catch (Exception exc) {
            tableColorGroup = toolbar.getVisualGroup("TableColorGroup");
        }
        toolbar.setSeparation(tableColorGroup, new Dimension(2, 10));

        // Background color button
        ImageIcon backColorIcon = new ImageIcon(getClass().getResource("images/toolbar/backColor.gif"));
        ImageIcon backColorDisabledIcon = new ImageIcon(getClass().getResource("images/toolbar/backColorDisabled.gif"));
        backColor = new ESlateButton();
        backColor.setIcon(backColorIcon);
        backColor.setDisabledIcon(backColorDisabledIcon);
        backColor.setMargin(new Insets(0,0,0,0));
        backColor.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
        backColor.setRequestFocusEnabled(false);
        backColor.setFocusable(false);
        backColor.setBorderPainted(false);
        backColor.setToolTipText(infoBundle.getString("BackColor"));
        toolbar.add(tableColorGroup, backColor, infoBundle.getString("BackColor"));
        toolbar.setAssociatedText(backColor, infoBundle.getString("BackColor"));

        // Foreground color button
        ImageIcon foreColorIcon = new ImageIcon(getClass().getResource("images/toolbar/foreColor.gif"));
        ImageIcon foreColorDisabledIcon = new ImageIcon(getClass().getResource("images/toolbar/foreColorDisabled.gif"));
        foreColor = new ESlateButton();
        foreColor.setIcon(foreColorIcon);
        foreColor.setDisabledIcon(foreColorDisabledIcon);
        foreColor.setMargin(new Insets(0,0,0,0));
        foreColor.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
        foreColor.setRequestFocusEnabled(false);
        foreColor.setFocusable(false);
        foreColor.setBorderPainted(false);
        foreColor.setToolTipText(infoBundle.getString("ForeColor"));
        toolbar.add(tableColorGroup, foreColor, infoBundle.getString("ForeColor"));
        toolbar.setAssociatedText(foreColor, infoBundle.getString("ForeColor"));

        // Grid color button
        ImageIcon gridColorIcon = new ImageIcon(getClass().getResource("images/toolbar/gridColor.gif"));
        ImageIcon gridColorDisabledIcon = new ImageIcon(getClass().getResource("images/toolbar/gridColorDisabled.gif"));
        gridColor = new ESlateButton();
        gridColor.setIcon(gridColorIcon);
        gridColor.setDisabledIcon(gridColorDisabledIcon);
        gridColor.setMargin(new Insets(0,0,0,0));
        gridColor.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
        gridColor.setRequestFocusEnabled(false);
        gridColor.setFocusable(false);
        gridColor.setBorderPainted(false);
        gridColor.setToolTipText(infoBundle.getString("GridColor"));
        toolbar.add(tableColorGroup, gridColor, infoBundle.getString("GridColor"));
        toolbar.setAssociatedText(gridColor, infoBundle.getString("GridColor"));

        // Selection color button
        ImageIcon selectionColorIcon = new ImageIcon(getClass().getResource("images/toolbar/selectionColor.gif"));
        ImageIcon selectionColorDisabledIcon = new ImageIcon(getClass().getResource("images/toolbar/selectionColorDisabled.gif"));
        selectionColor = new ESlateButton();
        selectionColor.setIcon(selectionColorIcon);
        selectionColor.setDisabledIcon(selectionColorDisabledIcon);
        selectionColor.setMargin(new Insets(0,0,0,0));
        selectionColor.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
        selectionColor.setRequestFocusEnabled(false);
        selectionColor.setFocusable(false);
        selectionColor.setBorderPainted(false);
        selectionColor.setToolTipText(infoBundle.getString("SelectionColor"));
        toolbar.add(tableColorGroup, selectionColor, infoBundle.getString("SelectionColor"));
        toolbar.setAssociatedText(selectionColor, infoBundle.getString("SelectionColor"));

        // Active record color button
        ImageIcon activeRecColorIcon = new ImageIcon(getClass().getResource("images/toolbar/activeRecColor.gif"));
        ImageIcon activeRecColorDisabledIcon = new ImageIcon(getClass().getResource("images/toolbar/activeRecColorDisabled.gif"));
        activeRecColor = new ESlateButton();
        activeRecColor.setIcon(activeRecColorIcon);
        activeRecColor.setDisabledIcon(activeRecColorDisabledIcon);
        activeRecColor.setMargin(new Insets(0,0,0,0));
        activeRecColor.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
        activeRecColor.setRequestFocusEnabled(false);
        activeRecColor.setFocusable(false);
        activeRecColor.setBorderPainted(false);
        activeRecColor.setToolTipText(infoBundle.getString("ActiveRecColor"));
        toolbar.add(tableColorGroup, activeRecColor, infoBundle.getString("ActiveRecColor"));
        toolbar.setAssociatedText(activeRecColor, infoBundle.getString("ActiveRecColor"));

        // Other formatting tools
        VisualGroup otherFormattingToolsGroup = null;
        try{
            otherFormattingToolsGroup = toolbar.addVisualGroup("Other formatting tools");
        }catch (Exception exc) {
            otherFormattingToolsGroup = toolbar.getVisualGroup("Other formatting tools");
        }
        toolbar.setSeparation(otherFormattingToolsGroup, new Dimension(7, 10));

        // Grid chooser button
        ImageIcon gridChooserIcon = new ImageIcon(getClass().getResource("images/toolbar/gridChooser.gif"));
        ImageIcon gridChooserDisabledIcon = new ImageIcon(getClass().getResource("images/toolbar/gridChooserDisabled.gif"));
        gridChooser = new ESlateButton();
        gridChooser.setIcon(gridChooserIcon);
        gridChooser.setDisabledIcon(gridChooserDisabledIcon);
        gridChooser.setMargin(new Insets(0,0,0,0));
        gridChooser.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
        gridChooser.setRequestFocusEnabled(false);
        gridChooser.setFocusable(false);
        gridChooser.setBorderPainted(false);
        gridChooser.setToolTipText(infoBundle.getString("GridChooser"));
        toolbar.add(otherFormattingToolsGroup, gridChooser, infoBundle.getString("GridChooser"));
        toolbar.setAssociatedText(gridChooser, infoBundle.getString("GridChooser"));

        // Row/Column selection button
        ImageIcon rowOnlySelectionIcon = new ImageIcon(getClass().getResource("images/toolbar/rowOnlySelection.gif"));
        ImageIcon rowOnlySelectionDisabledIcon = new ImageIcon(getClass().getResource("images/toolbar/rowOnlySelectionDisabled.gif"));
        ImageIcon columnRowSelectionIcon = new ImageIcon(getClass().getResource("images/toolbar/columnRowSelection.gif"));
        rowColumnSelection = new ESlateToggleButton();
        rowColumnSelection.setIcon(rowOnlySelectionIcon);
        rowColumnSelection.setDisabledIcon(rowOnlySelectionDisabledIcon);
        rowColumnSelection.setSelectedIcon(columnRowSelectionIcon);
        rowColumnSelection.setMargin(new Insets(0,0,0,0));
        rowColumnSelection.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
        rowColumnSelection.setRequestFocusEnabled(false);
        rowColumnSelection.setFocusable(false);
        rowColumnSelection.setBorderPainted(false);
        rowColumnSelection.setToolTipText(infoBundle.getString("RowColumnSelection"));
        toolbar.add(otherFormattingToolsGroup, rowColumnSelection, infoBundle.getString("RowColumnSelection"));
        toolbar.setAssociatedText(rowColumnSelection, infoBundle.getString("RowColumnSelection"));

        // Highlight non-editable fields button
        ImageIcon highlightNonEditableIcon = new ImageIcon(getClass().getResource("images/toolbar/highlightNonEditable.gif"));
        ImageIcon highlightNonEditableDisabledIcon = new ImageIcon(getClass().getResource("images/toolbar/highlightNonEditableDisabled.gif"));
        hightLightNonEditableFields = new ESlateToggleButton();
        hightLightNonEditableFields.setIcon(highlightNonEditableIcon);
        hightLightNonEditableFields.setDisabledIcon(highlightNonEditableDisabledIcon);
        hightLightNonEditableFields.setMargin(new Insets(0,0,0,0));
        hightLightNonEditableFields.setAlignmentY(ESlateToolBar.CENTER_ALIGNMENT);
        hightLightNonEditableFields.setRequestFocusEnabled(false);
        hightLightNonEditableFields.setFocusable(false);
        hightLightNonEditableFields.setBorderPainted(false);
        hightLightNonEditableFields.setToolTipText(infoBundle.getString("HightLightNonEditableFields"));
        toolbar.add(otherFormattingToolsGroup, hightLightNonEditableFields, infoBundle.getString("HightLightNonEditableFields"));
        toolbar.setAssociatedText(hightLightNonEditableFields, infoBundle.getString("HightLightNonEditableFields"));
    }

    void attachFormattingToolBarActions() {
        /*toolbar.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
        if (e.getModifiers() == e.BUTTON3_MASK) {
        if (dBase.isStandardToolbarVisible())
        dBase.standardToolBarMenuItem.setSelected(true);
        if (toolbar.isVisible())
        dBase.formattingToolBarMenuItem.setSelected(true);
        dBase.toolBarMenu.show(toolbar, e.getX(), e.getY());
        }
        }
        });*/
        fontType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
                    dBase.visiblePopupMenu.setVisible(false);
                if (!dBase.iterateEvent)
                    return;
                System.out.println("Calling fontChangeAction() ");
                dBase.fontChangeAction();
            }
        });

        fontSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
                    dBase.visiblePopupMenu.setVisible(false);
                if (!dBase.iterateEvent)
                    return;
                dBase.fontChangeAction();
            }
        });

        bold.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dBase.fontChangeAction();
            }
        });

        italic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dBase.fontChangeAction();
            }
        });

        backColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
/*1              if (dBase.mouseClicksSinceEnteredButton%2 == 1) {
dBase.mouseClicksSinceEnteredButton++;
dBase.colorPopupMenu.setVisible(false);
return;
}
dBase.mouseClicksSinceEnteredButton++;
1*/
                if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
                    dBase.visiblePopupMenu.setVisible(false);

                if (dBase.activeDBTable != null)
                    dBase.initializeColorPanel(dBase.activeDBTable.getBackgroundColor());
                dBase.colorPanel.getColorBoxChooser().setColorType(BACKGROUND_COLOR_TYPE);

                dBase.colorPopupMenu.show(dBase.toolMenuPanel, backColor.getLocation().x, dBase.toolMenuPanel.getSize().height);
                dBase.visiblePopupMenu = dBase.colorPopupMenu;
            }
        });

        foreColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
/*1              if (dBase.visiblePopupMenu != null && !dBase.visiblePopupMenu.isVisible()) {
if (dBase.mouseClicksSinceEnteredButton%2 == 1) {
dBase.mouseClicksSinceEnteredButton++;
return;
}
dBase.mouseClicksSinceEnteredButton++;
}else
dBase.visiblePopupMenu.setVisible(false);
1*/
                JPopupMenu typeMenu = new JPopupMenu();

                JMenuItem all = new JMenuItem(infoBundle.getString("DatabaseMsg130"));
                all.setFont(new Font(all.getFont().getName(), Font.BOLD, all.getFont().getSize()));
                typeMenu.add(all);
                typeMenu.addSeparator();
                JMenuItem numberType = new JMenuItem(infoBundle.getString("Number"));
                typeMenu.add(numberType);
                JMenuItem aplhaType = new JMenuItem(infoBundle.getString("Alphanumeric"));
                typeMenu.add(aplhaType);
                JMenuItem booleanType = new JMenuItem(infoBundle.getString("Boolean"));
                typeMenu.add(booleanType);
                JMenuItem dateType = new JMenuItem(infoBundle.getString("Date"));
                typeMenu.add(dateType);
                JMenuItem timeType = new JMenuItem(infoBundle.getString("Time"));
                typeMenu.add(timeType);
                JMenuItem urlType = new JMenuItem(infoBundle.getString("URL"));
                typeMenu.add(urlType);

                all.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (dBase.activeDBTable != null) {
//1                          TableView tableView = dBase.activeDBTable.viewStructure.tableView;
//1                          Color numColor = dBase.activeDBTable.viewStructure.tableView.getDoubleColor();
                            Color numColor = dBase.activeDBTable.doubleColor;
                            if (dBase.activeDBTable.stringColor.equals(numColor) &&
                                    dBase.activeDBTable.booleanColor.equals(numColor) &&
                                    dBase.activeDBTable.dateColor.equals(numColor) &&
                                    dBase.activeDBTable.timeColor.equals(numColor) &&
                                    dBase.activeDBTable.urlColor.equals(numColor))

                                dBase.initializeColorPanel(numColor);
                            else
                                dBase.colorPanel.getColorBoxChooser().initActiveColorIndex(-1);
                        }
                        dBase.colorPanel.getColorBoxChooser().setColorType(FOREGROUND_COLOR_TYPE);
                        dBase.colorPopupMenu.show(dBase.toolMenuPanel, foreColor.getLocation().x, dBase.toolMenuPanel.getSize().height);
                        dBase.visiblePopupMenu = dBase.colorPopupMenu;
                    }
                });

                numberType.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (dBase.activeDBTable != null)
                            dBase.initializeColorPanel(dBase.activeDBTable.doubleColor);

                        dBase.colorPanel.getColorBoxChooser().setColorType(NUMBER_COLOR_TYPE);
                        dBase.colorPopupMenu.show(dBase.toolMenuPanel, foreColor.getLocation().x, dBase.toolMenuPanel.getSize().height);
                        dBase.visiblePopupMenu = dBase.colorPopupMenu;
                    }
                });

                aplhaType.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (dBase.activeDBTable != null)
                            dBase.initializeColorPanel(dBase.activeDBTable.stringColor);

                        dBase.colorPanel.getColorBoxChooser().setColorType(ALPHANUMERIC_COLOR_TYPE);
                        dBase.colorPopupMenu.show(dBase.toolMenuPanel, foreColor.getLocation().x, dBase.toolMenuPanel.getSize().height);
                        dBase.visiblePopupMenu = dBase.colorPopupMenu;
                    }
                });

                booleanType.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (dBase.activeDBTable != null)
                            dBase.initializeColorPanel(dBase.activeDBTable.booleanColor);

                        dBase.colorPanel.getColorBoxChooser().setColorType(BOOLEAN_COLOR_TYPE);
                        dBase.colorPopupMenu.show(dBase.toolMenuPanel, foreColor.getLocation().x, dBase.toolMenuPanel.getSize().height);
                        dBase.visiblePopupMenu = dBase.colorPopupMenu;
                    }
                });

                dateType.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (dBase.activeDBTable != null)
                            dBase.initializeColorPanel(dBase.activeDBTable.dateColor);

                        dBase.colorPanel.getColorBoxChooser().setColorType(DATE_COLOR_TYPE);
                        dBase.colorPopupMenu.show(dBase.toolMenuPanel, foreColor.getLocation().x, dBase.toolMenuPanel.getSize().height);
                        dBase.visiblePopupMenu = dBase.colorPopupMenu;
                    }
                });

                timeType.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (dBase.activeDBTable != null)
                            dBase.initializeColorPanel(dBase.activeDBTable.timeColor);

                        dBase.colorPanel.getColorBoxChooser().setColorType(TIME_COLOR_TYPE);
                        dBase.colorPopupMenu.show(dBase.toolMenuPanel, foreColor.getLocation().x, dBase.toolMenuPanel.getSize().height);
                        dBase.visiblePopupMenu = dBase.colorPopupMenu;
                    }
                });

                urlType.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (dBase.activeDBTable != null)
                            dBase.initializeColorPanel(dBase.activeDBTable.urlColor);

                        dBase.colorPanel.getColorBoxChooser().setColorType(URL_COLOR_TYPE);
                        dBase.colorPopupMenu.show(dBase.toolMenuPanel, foreColor.getLocation().x, dBase.toolMenuPanel.getSize().height);
                        dBase.visiblePopupMenu = dBase.colorPopupMenu;
                    }
                });

                typeMenu.show(dBase.toolMenuPanel, foreColor.getLocation().x, dBase.toolMenuPanel.getSize().height);
            }
        });

        gridColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
/*1              if (dBase.mouseClicksSinceEnteredButton%2 == 1) {
dBase.mouseClicksSinceEnteredButton++;
dBase.colorPopupMenu.setVisible(false);
return;
}
dBase.mouseClicksSinceEnteredButton++;
1*/
                if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
                    dBase.visiblePopupMenu.setVisible(false);

                if (dBase.activeDBTable != null)
                    dBase.initializeColorPanel(dBase.activeDBTable.getGridColor());
                dBase.colorPanel.getColorBoxChooser().setColorType(GRID_COLOR_TYPE);

                dBase.colorPopupMenu.show(dBase.toolMenuPanel, gridColor.getLocation().x, dBase.toolMenuPanel.getSize().height);
                dBase.visiblePopupMenu = dBase.colorPopupMenu;
            }
        });

        selectionColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
/*1              if (dBase.mouseClicksSinceEnteredButton%2 == 1) {
dBase.mouseClicksSinceEnteredButton++;
dBase.colorPopupMenu.setVisible(false);
return;
}
dBase.mouseClicksSinceEnteredButton++;
1*/
                if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
                    dBase.visiblePopupMenu.setVisible(false);

                if (dBase.activeDBTable != null)
                    dBase.initializeColorPanel(dBase.activeDBTable.getSelectionBackground());
                dBase.colorPanel.getColorBoxChooser().setColorType(SELECTION_COLOR_TYPE);

                dBase.colorPopupMenu.show(dBase.toolMenuPanel, selectionColor.getLocation().x, dBase.toolMenuPanel.getSize().height);
                dBase.visiblePopupMenu = dBase.colorPopupMenu;
            }
        });

        activeRecColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
/*1              if (dBase.mouseClicksSinceEnteredButton%2 == 1) {
dBase.mouseClicksSinceEnteredButton++;
dBase.colorPopupMenu.setVisible(false);
return;
}
dBase.mouseClicksSinceEnteredButton++;
1*/
                if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
                    dBase.visiblePopupMenu.setVisible(false);

                if (dBase.activeDBTable != null)
                    dBase.initializeColorPanel(dBase.activeDBTable.getActiveRecordColor());
                dBase.colorPanel.getColorBoxChooser().setColorType(ACTIVE_RECORD_COLOR_TYPE);

                dBase.colorPopupMenu.show(dBase.toolMenuPanel, activeRecColor.getLocation().x, dBase.toolMenuPanel.getSize().height);
                dBase.visiblePopupMenu = dBase.colorPopupMenu;
            }
        });

        gridChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
/*1              if (dBase.mouseClicksSinceEnteredButton%2 == 1) {
dBase.mouseClicksSinceEnteredButton++;
dBase.gridPopupMenu.setVisible(false);
return;
}
dBase.mouseClicksSinceEnteredButton++;
1*/
                if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
                    dBase.visiblePopupMenu.setVisible(false);

                if (dBase.activeDBTable != null) {
                    if (!dBase.gridChooserPanel.fullGrid.isEnabled()) {
                        dBase.gridChooserPanel.fullGrid.setEnabled(true);
                        dBase.gridChooserPanel.horLinesOnly.setEnabled(true);
                        dBase.gridChooserPanel.vertLinesOnly.setEnabled(true);
                        dBase.gridChooserPanel.noGrid.setEnabled(true);
                    }
                    boolean horLinesVisible = dBase.activeDBTable.isHorizontalLinesVisible();
                    boolean vertLinesVisible = dBase.activeDBTable.isVerticalLinesVisible();
                    if (horLinesVisible && vertLinesVisible) {
                        dBase.gridChooserPanel.fullGrid.setBorderPainted(true);
                        dBase.gridChooserPanel.horLinesOnly.setBorderPainted(false);
                        dBase.gridChooserPanel.vertLinesOnly.setBorderPainted(false);
                        dBase.gridChooserPanel.noGrid.setBorderPainted(false);
                        dBase.gridChooserPanel.fullGrid.setSelected(true);
                    }else if (horLinesVisible && !vertLinesVisible) {
                        dBase.gridChooserPanel.fullGrid.setBorderPainted(false);
                        dBase.gridChooserPanel.horLinesOnly.setBorderPainted(true);
                        dBase.gridChooserPanel.vertLinesOnly.setBorderPainted(false);
                        dBase.gridChooserPanel.noGrid.setBorderPainted(false);
                        dBase.gridChooserPanel.horLinesOnly.setSelected(true);
                    }else if (!horLinesVisible && vertLinesVisible) {
                        dBase.gridChooserPanel.fullGrid.setBorderPainted(false);
                        dBase.gridChooserPanel.horLinesOnly.setBorderPainted(false);
                        dBase.gridChooserPanel.vertLinesOnly.setBorderPainted(true);
                        dBase.gridChooserPanel.noGrid.setBorderPainted(false);
                        dBase.gridChooserPanel.vertLinesOnly.setSelected(true);
                    }else if (!horLinesVisible && !vertLinesVisible) {
                        dBase.gridChooserPanel.fullGrid.setBorderPainted(false);
                        dBase.gridChooserPanel.horLinesOnly.setBorderPainted(false);
                        dBase.gridChooserPanel.vertLinesOnly.setBorderPainted(false);
                        dBase.gridChooserPanel.noGrid.setBorderPainted(true);
                        dBase.gridChooserPanel.noGrid.setSelected(true);
                    }
                }else{
                    dBase.gridChooserPanel.fullGrid.setBorderPainted(false);
                    dBase.gridChooserPanel.horLinesOnly.setBorderPainted(false);
                    dBase.gridChooserPanel.vertLinesOnly.setBorderPainted(false);
                    dBase.gridChooserPanel.noGrid.setBorderPainted(false);
                    dBase.gridChooserPanel.fullGrid.setEnabled(false);
                    dBase.gridChooserPanel.horLinesOnly.setEnabled(false);
                    dBase.gridChooserPanel.vertLinesOnly.setEnabled(false);
                    dBase.gridChooserPanel.noGrid.setEnabled(false);
                }
                ((GridChooser) dBase.gridPopupMenu.getComponent(0)).setDBTable(dBase.activeDBTable);
                dBase.gridPopupMenu.show(dBase.toolMenuPanel, gridChooser.getLocation().x, dBase.toolMenuPanel.getSize().height);
                dBase.visiblePopupMenu = dBase.gridPopupMenu;
            }
        });

        rowColumnSelection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
                    dBase.visiblePopupMenu.setVisible(false);
                ESlateToggleButton b = (ESlateToggleButton) e.getSource();
                if (b.isBorderPainted())
                    b.setBorderPainted(false);
                else
                    b.setBorderPainted(true);

                dBase.activeDBTable.setSimultaneousFieldRecordActivation(b.isSelected());
            }
        });

        hightLightNonEditableFields.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
                    dBase.visiblePopupMenu.setVisible(false);
                ESlateToggleButton b = (ESlateToggleButton) e.getSource();
                if (b.isBorderPainted())
                    b.setBorderPainted(false);
                else
                    b.setBorderPainted(true);

                dBase.activeDBTable.setNonEditableFieldsHighlighted(b.isSelected());
            }
        });
    }
}