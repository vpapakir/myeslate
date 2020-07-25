package gr.cti.eslate.tableInspector;

import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.CImageIcon;
import gr.cti.eslate.database.engine.DuplicateKeyException;
import gr.cti.eslate.database.engine.InvalidCellAddressException;
import gr.cti.eslate.database.engine.InvalidDataFormatException;
import gr.cti.eslate.database.engine.NullTableKeyException;
import gr.cti.eslate.database.engine.Table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

class RowElement extends JPanel {
    static ImageIcon icon[];
    static Color selectedURL,unselectedURL,selectedVisited,unselectedVisited;
    protected JLabel field;
    //protected JLabel value; //IE
    protected ScrollableLabel value; //IE
    protected JTextField edit;
    protected JCheckBox query;
    protected QueryButton btnList;
    Component queryStrut=new Component() {
        public Dimension getPreferredSize() {
            return query.getPreferredSize();
        }
        public Dimension getSize() {
            return query.getSize();
        }
    };
    Component btnListStrut=new Component() {
        public Dimension getPreferredSize() {
            return btnList.getPreferredSize();
        }
        public Dimension getSize() {
            return btnList.getSize();
        }
    };

    private JPopupMenu popup=new JPopupMenu() {
        public Dimension getPreferredSize() {
            Dimension d=super.getPreferredSize();
            Dimension l=lstList.getPreferredSize();
            Insets in=popup.getInsets();
            l.height+=in.top+in.bottom;
            if (d.height>l.height)
                d.height=l.height;
            //This is the scroll pane border size
            d.width+=2;
            d.height+=2;

            //Constrain the pop up to the width of 350
            if (d.width>350)
                d.width=350;
            return d;
        }
    };
    private JScrollPane scrList;
    private JList lstList;
    protected Table table;
    protected Object valueObj;
    protected ResourceBundle tooltipBundle;
    Color orgColor;
    TableInspector parent;
    TIPanel myTIPanel;
    boolean enabled=true,hasAcceptedLastValue=true;
    protected boolean hasPerformedQuery=false;
    Object queryValue;
    //Indicates whether this is a row of a selected record. It affects coloring.
    private boolean selectedRecord=false;
    static Border border=new CompoundBorder(BorderFactory.createLoweredBevelBorder(),new EmptyBorder(0,3,0,3));
    boolean queryVisible=true;

    RowElement(String fieldValue, String valueValue, Table table1, TIPanel myTIPanel1, int width) {
        tooltipBundle=ResourceBundle.getBundle("gr.cti.eslate.tableInspector.TooltipBundle",Locale.getDefault());

        this.table=table1;
        this.myTIPanel=myTIPanel1;

        selectedURL=new Color(0,192,255);
        unselectedURL=Color.blue;
        selectedVisited=new Color(255,0,255);
        unselectedVisited=new Color(128,0,128);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0,0,5,1));
        setOpaque(false);

        //This to listen to eslate frame events and restore layout.
        addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
            public void ancestorResized(HierarchyEvent e) {
                Dimension dim=new Dimension((int) (myTIPanel.ti.getWidth()*myTIPanel.ti.getPercentField()/100),22);
                field.setPreferredSize(dim);
                field.setMaximumSize(dim);
                field.setMinimumSize(dim);
                validate();
            }
        });

        field=new JLabel(fieldValue.concat(":"));
        field.setAlignmentY(CENTER_ALIGNMENT);
        Dimension dim=new Dimension((int) (myTIPanel.ti.getWidth()*myTIPanel.ti.getPercentField()/100),22);
        field.setPreferredSize(dim);
        field.setMaximumSize(dim);
        field.setMinimumSize(dim);
        //REMOVED19990929:THOUGHT OF AS DESTRUCTING field.setToolTipText(fieldValue);

        value=new ScrollableLabel() {
            public Color getBackground() {
                try {
                    if (selectedRecord)
                        return myTIPanel.ti.selectedBackground;
                    else
                        return myTIPanel.ti.unselectedBackground;
                } catch(NullPointerException e) {/*Exceptions happen upon component instantiation*/}
                return super.getBackground();
            }
            public Color getForeground() {
                try {
                    if (valueObj instanceof HREF || valueObj instanceof URL)
                        return super.getForeground();
                    if (selectedRecord)
                        return myTIPanel.ti.selectedForeground;
                    else
                        return myTIPanel.ti.unselectedForeground;
                } catch(NullPointerException e) {/*Exceptions happen upon component instantiation*/}
                return super.getBackground();
            }
        };
        value.setAlignmentY(CENTER_ALIGNMENT);
        value.setFont(new Font("Helvetica",Font.PLAIN,12));
        if (myTIPanel.ti.isRowBorderPainted()) {
            value.setOpaque(true);
            value.setBorder(border);
        } else {
            value.setOpaque(false);
            value.setBorder(null);
        }
//p        dim=new Dimension(width*7/10,height);
//p        value.setPreferredSize(dim);
//p        value.setMaximumSize(dim);
//p        value.setMinimumSize(dim);
        //REMOVED19990929:THOUGHT OF AS DESTRUCTING value.setToolTipText(valueValue);
        value.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if ((!value.isEnabled()) || (myTIPanel.getNoOfObjects()==0)) return;
                try {
                AbstractTableField f=table.getTableField(field.getText().substring(0,field.getText().length()-1));
                if ((valueObj instanceof URL) || (valueObj instanceof HREF)) {
                    if ((valueObj instanceof HREF) && (((HREF) valueObj).url==null)) return;
                    value.setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else if (f.isEditable() && f.getDataType()!=gr.cti.eslate.database.engine.CImageIcon.class) {
                    value.setCursor(new Cursor(Cursor.TEXT_CURSOR));
                }
                } catch(gr.cti.eslate.database.engine.InvalidFieldNameException ex) {}
            }

            public void mouseExited(MouseEvent e) {
                if ((!value.isEnabled()) || (myTIPanel.getNoOfObjects()==0)) return;
                value.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            public void mousePressed(MouseEvent e) {
                if ((!value.isEnabled()) || (myTIPanel.getNoOfObjects()==0)) return;
                if ((valueObj instanceof URL) || (valueObj instanceof HREF)) {
                    if ((valueObj instanceof HREF) && (((HREF) valueObj).url==null)) return;
                    orgColor=value.getForeground();
                    value.setForeground(Color.red);
                    value.repaint();
                } else
                    myMouseClick();
            }

            public void mouseReleased(MouseEvent e) {
                if ((!value.isEnabled()) || (myTIPanel.getNoOfObjects()==0)) return;
                if ((valueObj instanceof URL) || (valueObj instanceof HREF)) {
                    if ((valueObj instanceof HREF) && (((HREF) valueObj).url==null)) return;
                    /*if (value.getForeground()==Color.red) {
                        value.setForeground(orgColor);
                    }*/
                    //Set color
                    if (selectedRecord)
                        value.setForeground(selectedVisited);
                    else
                        value.setForeground(unselectedVisited);
                    value.repaint();
                    //Invoke browser
                    /*java.applet.AppletContext appletContext=null;
                    if (valueObj instanceof URL) {
                        if (valueObj!=null) {
                            String[] cmd=new String[2];
                            cmd[0]="start";
                            cmd[1]=((URL)valueObj).toString();
                            try {
                                Runtime.getRuntime().exec(cmd);
                                parent.addPage((URL) valueObj);
                            } catch(java.io.IOException e1) {
                                JOptionPane.showMessageDialog(null,"Cannot show URL!","Cannot show URL!",JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else if (valueObj instanceof HREF) {
                        if (((HREF) valueObj).url!=null) {
                            String[] cmd=new String[2];
                            cmd[0]="start";
                            cmd[1]=(((HREF) valueObj).url).toString();
                            try {
                                Runtime.getRuntime().exec(cmd);
                                parent.addPage(((HREF) valueObj).url);
                            } catch(java.io.IOException e1) {
                                JOptionPane.showMessageDialog(null,"Cannot show URL!","Cannot show URL!",JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }*/
                    if (valueObj instanceof URL)
                        parent.showURL((URL) valueObj);
                    else if (valueObj instanceof HREF)
                        if (((HREF) valueObj).url!=null)
                            parent.showURL(((HREF) valueObj).url);
                }
            }

        });

        edit=new JTextField() {
            public Dimension getMaximumSize() {
                Dimension d=super.getMaximumSize();
                d.width=value.getSize().width;

                return d;
            }
            public Dimension getMinimumSize() {
                Dimension d=super.getMinimumSize();
                d.width=value.getSize().width;

                return d;
            }
            public Dimension getPreferredSize() {
                Dimension d=super.getPreferredSize();
                d.width=value.getSize().width;

                return d;
            }
            public void processKeyEvent(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                    if (e.getID()==KeyEvent.KEY_PRESSED)
                        acceptNewValue(true);
                } else if (e.getKeyCode()==KeyEvent.VK_ESCAPE) {
                    if (e.getID()==KeyEvent.KEY_PRESSED)
                        acceptNewValue(false);
                } else
                    super.processKeyEvent(e);
            }
            public Color getBackground() {
                try {
                    if (selectedRecord)
                        return myTIPanel.ti.selectedBackground;
                    else
                        return myTIPanel.ti.unselectedBackground;
                } catch(NullPointerException e) {/*Exceptions happen upon component instantiation*/}
                return super.getBackground();
            }
            public Color getForeground() {
                try {
                    if (valueObj instanceof HREF || valueObj instanceof URL)
                        return super.getForeground();
                    if (selectedRecord)
                        return myTIPanel.ti.selectedForeground;
                    else
                        return myTIPanel.ti.unselectedForeground;
                } catch(NullPointerException e) {/*Exceptions happen upon component instantiation*/}
                return super.getBackground();
            }
        };
        edit.setAlignmentY(CENTER_ALIGNMENT);
        edit.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        edit.setBorder(BorderFactory.createLoweredBevelBorder());
        edit.setRequestFocusEnabled(true);
//p        dim=new Dimension(width*7/10,height);
//p        edit.setPreferredSize(dim);
//p        edit.setMaximumSize(dim);
//p        edit.setMinimumSize(dim);
        edit.setFont(new Font("Helvetica",Font.PLAIN,12));
        edit.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent fe) {
                if (!hasAcceptedLastValue)
                    acceptNewValue(true);
            }
        });

        if (valueValue.equals(""))
            setValue(null);
        else
            setValue(valueValue);

        query=new JCheckBox(icon[0],false) {
            public String getToolTipText() {
                if (query.isSelected())
                    return tooltipBundle.getString("notquery")+queryValue+"\"";
                else
                    return tooltipBundle.getString("querytick");
            }
        };
        ToolTipManager.sharedInstance().registerComponent(query);

        query.setOpaque(false);
        query.setAlignmentY(CENTER_ALIGNMENT);
        query.setAlignmentX(RIGHT_ALIGNMENT);
        dim=new Dimension(24,22);
        query.setSelectedIcon(icon[1]);
        query.setPreferredSize(dim);
        query.setMaximumSize(dim);
        query.setMinimumSize(dim);
        query.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Do the query. If selected add to the selected set, if not selected remove from it.
                if (RowElement.this.isSelected())
                    makeQuery(valueObj);
                else
                    clearQuery();
                myTIPanel.checkStatusChanged();
                btnList.revalidate();
                repaint();
            }
        });

        btnList=new QueryButton();
        lstList=new JList();
        //lstList.setBackground(UIManager.getColor("control"));
        lstList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                popup.setVisible(false);
                //Do the query. If selected add to the selected set, if not selected remove from it.
                if (lstList.getSelectedIndex()!=-1)
                    makeQuery(lstList.getSelectedValue());
            }
        });
        scrList=new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrList.setBorder(new EmptyBorder(1,1,1,1));
        scrList.setViewportView(lstList);

        popup.setLayout(new BorderLayout());
        popup.setLightWeightPopupEnabled(false);
        //popup.setPreferredSize(new Dimension(270,150));

        add(field,BorderLayout.WEST);
//p        add(Box.createHorizontalStrut(1));
        add(value,BorderLayout.CENTER);
//p        add(Box.createHorizontalStrut(5));
        if (myTIPanel.ti.isToolbarVisible() && myTIPanel.ti.isToolQueryVisible())
            checkQueryType();

    }

    private void makeQuery(Object qValue) {
        btnList.querying=true;
        queryValue=qValue;
        myTIPanel.addToSelectedSubset(this,getFieldName(),queryValue);
        //Synchronize query views
        query.setSelected(true);
    }
    private void clearQuery() {
        Object tempValue=queryValue;
        queryValue=null;
        //Synchronize query views
        setSelected(false);
        myTIPanel.removeFromSelectedSubset(getFieldName(),tempValue);
    }

    private String booleanString(Boolean b) {
        if (((Boolean) b).booleanValue())
            return TableInspector.messagesBundle.getString("true");
        else
            return TableInspector.messagesBundle.getString("false");
    }

    protected void acceptNewValue(boolean accept) {
        hasAcceptedLastValue=true;
        if (accept)
            try {
                setValue(myTIPanel.acceptNewValue(this,edit.getText()));
            } catch(InvalidDataFormatException e) { //Hold the old value
            } catch(NullTableKeyException e) {
            } catch(InvalidCellAddressException e) {
            } catch(DuplicateKeyException e) {
            }

        removeAll();
        add(field,BorderLayout.WEST);
        add(value,BorderLayout.CENTER);
        if (myTIPanel.ti.isToolQueryVisible())
            checkQueryType();
        validate();
        repaint();
    }

    protected Object getValue() {
        return valueObj;
    }

    protected void setValue(Object newValue) {
        valueObj=newValue;
        value.setHorizontalAlignment(SwingConstants.LEFT);
        edit.setHorizontalAlignment(JTextField.LEFT);
        if (newValue!=null) {
            if (value.isEnabled()) {
                try {
                    if (valueObj.getClass().equals(Class.forName("gr.cti.eslate.database.engine.CImageIcon")) || value instanceof Icon) {
                        query.setEnabled(false);
                        btnList.setEnabled(false);
                    }
                } catch (ClassNotFoundException e) {
                    query.setEnabled(true);
                    btnList.setEnabled(true);
                }

                if (newValue instanceof CImageIcon) {
                    value.setText("");
                    value.setIcon(((CImageIcon) newValue).getIcon());
                    value.setToolTipText(tooltipBundle.getString("icon"));
                } else if (newValue instanceof Double) {
                    value.setHorizontalAlignment(SwingConstants.RIGHT);
                    edit.setHorizontalAlignment(JTextField.RIGHT);
                    value.setText(table.getNumberFormat().format((Double) newValue));
                    value.setIcon(null);
                    //REMOVED19990929:THOUGHT OF AS DESTRUCTING value.setToolTipText(table.getNumberFormat().format((Double) newValue));
                } else if ((newValue instanceof URL) || (newValue instanceof HREF)) {
                    parent=myTIPanel.ti;

                    //Check whether the url should be painted unvisited or visited
                    if (!selectedRecord) {
                        //is an unselected record
                        if (newValue instanceof URL) {
                            if (parent.hasVisited((URL) newValue))
                                value.setForeground(unselectedVisited);
                            else
                                value.setForeground(unselectedURL);
                        } else {
                            if (((HREF) newValue).url==null)
                                value.setForeground(myTIPanel.ti.unselectedForeground);
                            else if (parent.hasVisited(((HREF) newValue).url))
                                value.setForeground(unselectedVisited);
                            else
                                value.setForeground(unselectedURL);
                        }
                    } else {
                        //is a selected record
                        if (newValue instanceof URL) {
                            if (parent.hasVisited((URL) newValue))
                              value.setForeground(selectedVisited);
                            else
                                value.setForeground(selectedURL);
                        } else {
                            if (((HREF) newValue).url==null)
                                value.setForeground(myTIPanel.ti.selectedForeground);
                            else if (parent.hasVisited(((HREF) newValue).url))
                                value.setForeground(selectedVisited);
                            else
                                value.setForeground(selectedURL);
                        }
                    }


                    value.setIcon(null);
                    value.setText(newValue.toString());
                    value.setToolTipText(tooltipBundle.getString("followlink"));
                } else if (newValue instanceof Boolean) {
                    value.setText(booleanString((Boolean) newValue));
                    value.setIcon(null);
                    value.setToolTipText(null);
                } else {
                    value.setIcon(null);
                    value.setText(newValue.toString());
                    value.setToolTipText(null);
                    //REMOVED19990929:THOUGHT OF AS DESTRUCTING if (newValue.toString().equals(""))
                    //REMOVED19990929:THOUGHT OF AS DESTRUCTING     value.setToolTipText(null);
                    //REMOVED19990929:THOUGHT OF AS DESTRUCTING else
                    //REMOVED19990929:THOUGHT OF AS DESTRUCTING     value.setToolTipText(newValue.toString());
                }
            }
        } else {
            valueObj=null;
            value.setIcon(null);
            value.setText("");
            value.setToolTipText(null);
        }
    }

    protected void setHasPerformedQuery(boolean value) {
        hasPerformedQuery=value;
    }

    protected boolean hasPerformedQuery() {
        return hasPerformedQuery;
    }

    protected void setFieldName(String s) {
        field.setText(s+":");
    }

    protected String getFieldName() {
        return field.getText().substring(0,field.getText().length()-1);
    }

    protected Class getFieldType() {
        if (valueObj!=null)
            return valueObj.getClass();
        else return null;
    }

    protected boolean isSelected() {
        if (myTIPanel.ti.getQueryType()==TableInspector.QUERY_TICK)
            return query.isSelected();
        else
            return btnList.querying;
    }

    protected void setSelected(boolean val) {
        query.setSelected(val);
        btnList.querying=val;
        btnList.repaint();
    }

    protected Object getQueryValue() {
        return queryValue;
    }
    /* This method adds or removes the components*/
    protected void queryTools(boolean value) {
        if (value) {
            checkQueryType();
            revalidate();
        } else {
            remove(query);
            remove(btnList);
            remove(queryStrut);
            remove(btnListStrut);
            revalidate();
        }
    }

    /*While this changes its visibility status*/
    void setQueryVisible(boolean b) {
        queryVisible=b;
        checkQueryType();
        repaint();
    }

    boolean isQueryVisible() {
        return queryVisible;
    }

    protected void viewAsSelectedRecord(boolean boole) {
       selectedRecord=boole;
    }

    protected void myMouseClick() {
        try {
            AbstractTableField f=table.getTableField(field.getText().substring(0,field.getText().length()-1));
            if (f.isEditable() && f.getDataType()!=gr.cti.eslate.database.engine.CImageIcon.class) {
                removeAll();
                add(field,BorderLayout.WEST);
                add(edit,BorderLayout.CENTER);
                if (f.getDataType()==java.lang.Double.class)
                    edit.setHorizontalAlignment(SwingConstants.RIGHT);
                else
                    edit.setHorizontalAlignment(SwingConstants.LEFT);
                if (myTIPanel.ti.isToolbarVisible() && myTIPanel.ti.isToolQueryVisible())
                    checkQueryType();
                hasAcceptedLastValue=false;
                edit.setText(value.getText());
                edit.setCaretPosition((edit.getText().length()==0)?0:(edit.getText().length()));
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        edit.requestFocus();
                    }
                });
                validate();
            }
        } catch(gr.cti.eslate.database.engine.InvalidFieldNameException ex) {}
    }

    protected void edit() {
        myMouseClick();
    }

    public void setEnabled(boolean val) {
        enabled=val;
        if (val) {
            field.setEnabled(true);
            value.setEnabled(true);
            query.setEnabled(true);
            btnList.setEnabled(true);
            setValue(valueObj);
        } else {
            field.setEnabled(false);
            value.setEnabled(false);
            query.setEnabled(false);
            btnList.setEnabled(false);
            value.setText("");
            value.setToolTipText(null);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
    /**
     * Called when the width of the field name must change, due to a change in the percentage property of TI.
     */
    void percentChanged() {
        Dimension dim=new Dimension((int) (myTIPanel.ti.getWidth()*myTIPanel.ti.getPercentField()/100),22);
        field.setPreferredSize(dim);
        field.setMaximumSize(dim);
        field.setMinimumSize(dim);
        invalidate();
        validate();
    }
    /**
     * Known method.
     */
    public void setFont(Font f) {
        super.setFont(f);
        if (edit!=null)
            edit.setFont(f);
        for (int i=0;i<getComponentCount();i++)
            ((Component) getComponents()[i]).setFont(f);
    }

    void checkQueryType() {
        remove(query);
        remove(btnList);
        remove(queryStrut);
        remove(btnListStrut);
        if (myTIPanel.ti.isToolQueryVisible()) {
            if (myTIPanel.ti.getQueryType()==TableInspector.QUERY_TICK) {
                if (queryVisible)
                    add(query,BorderLayout.EAST);
                else
                    add(queryStrut,BorderLayout.EAST);
            } else {
                if (queryVisible)
                    add(btnList,BorderLayout.EAST);
                else
                    add(btnListStrut,BorderLayout.EAST);
            }
        }
    }
    /**
     * Locks the field to its current value. If already locked, it is unlocked and then relocked
     * to the new value.
     */
    void lock() {
        if (isSelected()/*locked*/)
            unlock();
        makeQuery(valueObj);
    }
    /**
     * Locks the field to the given value. If already locked, it is unlocked and then relocked
     * to the new value.
     */
    void lock(String value) {
        if (isSelected()/*locked*/)
            unlock();
        makeQuery(value);
    }
    /**
     * Unlocks the field.
     */
    void unlock() {
        if (isSelected())
            query.doClick();
        clearQuery();
    }

    private static ImageIcon q;
    private static ImageIcon nq;
    private class QueryButton extends gr.cti.eslate.utils.NoBorderButton {
        boolean querying;

        QueryButton() {
            super();
            querying=false;
            q=new ImageIcon(TableInspector.class.getResource("images/key.gif"));
            nq=new ImageIcon(TableInspector.class.getResource("images/arrow.gif"));
            setFocusPainted(false);
            setOpaque(false);
            setMargin(new Insets(0,0,0,0));
            setBorder(new CompoundBorder(new EmptyBorder(0,3,0,0),getBorder()));
            setDefaultCapable(false);
            setAlignmentY(CENTER_ALIGNMENT);
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    MenuSelectionManager.defaultManager().clearSelectedPath();
                    //If querying, the field is locked. Unlock and wait for a new click.
                    if (querying) {
                        clearQuery();
                        return;
                    }
                    //Otherwise popup the list with possible values
                    popup.removeAll();
                    popup.add(scrList,BorderLayout.CENTER);
                    DefaultListModel md=new DefaultListModel();
                    Object[] o=myTIPanel.getDistinctFieldValues(getFieldName());
                    for (int i=0;i<o.length;i++)
                        md.addElement(o[i]);
                    lstList.setModel(md);
                    lstList.repaint();
                    //Calculate the location of the popup
                    //int w=popup.getPreferredSize().width+10;
                    //popup.setSize(w,popup.getPreferredSize().height);
                    popup.show(QueryButton.this,QueryButton.this.getWidth()-popup.getPreferredSize().width,QueryButton.this.getHeight());
                }
            });
            ToolTipManager.sharedInstance().registerComponent(this);
        }

        public String getToolTipText() {
            if (querying)
                return tooltipBundle.getString("notquery")+queryValue+"\"";
            else
                return tooltipBundle.getString("query");
        }

        public Icon getIcon() {
            if (querying)
                return q;
            else
                return nq;
        }

    }
}
