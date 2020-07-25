package gr.cti.eslate.tableInspector;

import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.DisconnectionEvent;
import gr.cti.eslate.base.DisconnectionListener;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.NoSuchPlugException;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.SharedObjectPlug;
import gr.cti.eslate.base.SingleInputMultipleOutputPlug;
import gr.cti.eslate.base.SingleInputPlug;
import gr.cti.eslate.base.sharedObject.SharedObject;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.AttributeLockedException;
import gr.cti.eslate.database.engine.DuplicateKeyException;
import gr.cti.eslate.database.engine.IntBaseArrayDesc;
import gr.cti.eslate.database.engine.InvalidCellAddressException;
import gr.cti.eslate.database.engine.InvalidDataFormatException;
import gr.cti.eslate.database.engine.InvalidFieldIndexException;
import gr.cti.eslate.database.engine.InvalidFieldNameException;
import gr.cti.eslate.database.engine.InvalidLogicalExpressionException;
import gr.cti.eslate.database.engine.InvalidRecordIndexException;
import gr.cti.eslate.database.engine.LogicalExpression;
import gr.cti.eslate.database.engine.NullTableKeyException;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.database.engine.TableFieldBaseArray;
import gr.cti.eslate.sharedObject.NumberSO;
import gr.cti.eslate.sharedObject.RecordSO;
import gr.cti.eslate.tableModel.event.ActiveRecordChangedEvent;
import gr.cti.eslate.tableModel.event.CellValueChangedEvent;
import gr.cti.eslate.tableModel.event.ColumnAddedEvent;
import gr.cti.eslate.tableModel.event.ColumnHiddenStateChangedEvent;
import gr.cti.eslate.tableModel.event.ColumnRemovedEvent;
import gr.cti.eslate.tableModel.event.ColumnRenamedEvent;
import gr.cti.eslate.tableModel.event.ColumnTypeChangedEvent;
import gr.cti.eslate.tableModel.event.RecordAddedEvent;
import gr.cti.eslate.tableModel.event.RecordRemovedEvent;
import gr.cti.eslate.tableModel.event.SelectedRecordSetChangedEvent;
import gr.cti.eslate.tableModel.event.TableRenamedEvent;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.StringBaseArray;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.objectspace.jgl.Array;

public class TIPanel extends JScrollPane implements Externalizable {
    private static final long serialVersionUID=19991113L;
    JPanel content;
    JLabel noOf;
    JButton next,previous,query,clear;
    JButtonWithPopup show;
    JToggleButton showHide;
    JPanel tools;
    JViewport viewport;
    IntBaseArrayDesc records;
    Array screenFields;
    protected int noOfObjects,curObj;
    protected Dimension rowDim;
    protected Locale locale;
    protected Table table; //IE
    ResourceBundle messagesBundle;
    boolean hasShowHide,showHideStatus;
    boolean nowQuerying=false;
    boolean showAll;
    protected boolean isHazed=false; //IE
    TableInspector ti;
    SharedObjectPlug synchPlug,recInputPlug;
    SharedObject synchSO;
    protected gr.cti.eslate.tableModel.event.DatabaseTableModelListener tmListen;
    private IntBaseArrayDesc outboundSelectedSet;
    Object[][] locks;
    boolean[] queryToolVisibility;

    public TIPanel() {}

    public TIPanel(Table table1,TableInspector ti1) {
        super(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        getVerticalScrollBar().setBlockIncrement(27);
        getVerticalScrollBar().setUnitIncrement(27);
        getHorizontalScrollBar().setBlockIncrement(54);
        getHorizontalScrollBar().setUnitIncrement(10);

        setBorder(new EmptyBorder(1,1,1,1));

        //if (ti1.getBackgroundImage()!=null)
        //    setBorder(BorderFactory.createEtchedBorder());

        //"Localize" TI's controls
        this.table=table1;
        outboundSelectedSet=table.getSelectedSubset();
        this.ti=ti1;
        previous=ti.previous;
        next=ti.next;
        clear=ti.clear;
        query=ti.query;
        showHide=ti.showHide;
        noOf=ti.noOf;
        show=ti.show;
        tools=ti.tools;
        messagesBundle=TableInspector.messagesBundle;
        hasShowHide=ti.isToolShowRecordVisible();
        showAll=ti.getToolSelectViewDefaultShowAll();

        curObj=noOfObjects=0;
        screenFields=new Array();
        records=new IntBaseArrayDesc();
        RowElement.icon=new ImageIcon[2];
        RowElement.icon[0]=loadImageIcon("notquery.gif");
        RowElement.icon[1]=loadImageIcon("query.gif");

        //Create Panel
        setOpaque(ti.isWidgetsOpaque());
        setBackground(ti.getBackground());
        content=new MyJPanel();
        content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
        content.setOpaque(false);

        TableFieldBaseArray a=table.getFields();
        content.removeAll();
        content.add(Box.createVerticalStrut(5));
        screenFields.clear();
        RowElement re;
        for (int i=0;i<a.size();i++) {
            if (!a.get(i).isHidden()) {
                content.add((re=new RowElement(a.get(i).getName(),"",table,this,content.getWidth())));
                screenFields.add(re);
            }
        }

        show.setEnabled(true);
        showHide.setEnabled(true);

        if (hasShowHide) {
            showHide.setSelected(false);
            tools.removeAll();
            tools.add(showHide);
            tools.add(Box.createHorizontalStrut(3));
            //tools.add(copy);
            //tools.add(paste);
            tools.add(show);
            tools.validate();
            disableView();
        }

        viewport=getViewport();
        viewport.setView(content);
        viewport.setOpaque(false);
        //dim=new Dimension(width-180,height-18-30-50);
        viewport.setViewPosition(new Point(0, 0));

        tmListen=new gr.cti.eslate.tableModel.event.DatabaseTableModelAdapter() {
            //ACTIVE RECORD CHANGED/////////////////////////////////
            public void activeRecordChanged(ActiveRecordChangedEvent are) {
                if (!ti.getFollowActiveRecord())
                    return;
                boolean iAmActive=TIPanel.this.equals(ti.getSelectedTIPanel()); //When false donot update the visual elements

                //When not active return
                if (!iAmActive) {
                    if (showAll) {
                        curObj=are.getActiveRecord()+1;
                    } else {
                        int ii;
                        if ((ii=records.indexOf(are.getActiveRecord()))!=-1) {
                            curObj=ii+1;
                        } else
                            curObj=0;
                    }
                    showCurrentRecord(false);
                    return;
                }

                if ((showHide.isSelected()) && (hasShowHide))
                    showHide.doClick();

                if (are.getActiveRecord()==-1) {
                    setCurObj(0,false);
                    return;
                }

                if (showAll) {
                    setCurObj(are.getActiveRecord()+1,false);
                } else {
                    int ii;
                    if ((ii=records.indexOf(are.getActiveRecord()))!=-1) {
                        setCurObj(ii+1,false);
                    } else {
                        setCurObj(0,false);
                    }
                }
            }

            //SELECTED RECORDSET CHANGED/////////////////////////////////
            public void selectedRecordSetChanged(SelectedRecordSetChangedEvent re) {
                //Clear query fields when the selected set is not changed by this Table Inspector
                if (!nowQuerying) {
                    outboundSelectedSet=table.getSelectedSubset();
                    query(3);
                    return;
                }
                showSelectedSet();
            }

            //RECORD ADDED EVENT/////////////////////////////////
            public void recordAdded(RecordAddedEvent e) {
                boolean iAmActive=TIPanel.this.equals(ti.getSelectedTIPanel()); //When false donot update the visual elements
                if (iAmActive) {
                    setNoOfObjects(table.getRecordCount());
                    setCurObj(table.getActiveRecord()+1,false);
                } else {
                    noOfObjects=table.getRecordCount();
                }
            }

            //EMPTY RECORD ADDED EVENT/////////////////////////////////
            @SuppressWarnings("unused")
			public void emptyRecordAdded(RecordAddedEvent e) {
                boolean iAmActive=TIPanel.this.equals(ti.getSelectedTIPanel()); //When false donot update the visual elements

                if (iAmActive) {
                    setNoOfObjects(table.getRecordCount());
                    setCurObj(table.getActiveRecord()+1,false);
                } else
                    noOfObjects=table.getRecordCount();
            }

            //RECORD REMOVED EVENT/////////////////////////////////
            public void recordRemoved(RecordRemovedEvent e) {
                boolean iAmActive=TIPanel.this.equals(ti.getSelectedTIPanel()); //When false donot update the visual elements
                if (iAmActive)
                    setNoOfObjects(table.getRecordCount());
                else
                    noOfObjects=table.getRecordCount();
                showCurrentRecord(false);
            }

            //CELL VALUE CHANGED EVENT/////////////////////////////////
            public void cellValueChanged(CellValueChangedEvent e) {
                boolean iAmActive=TIPanel.this.equals(ti.getSelectedTIPanel()); //When false donot update the visual elements
                if ((iAmActive) && curObj>0 && ((showAll && curObj-1==e.getRecordIndex()) || (!showAll && records.get(curObj-1)==e.getRecordIndex()))) {
                    for (int i=0;i<screenFields.size();i++)
                        if (((RowElement) screenFields.at(i)).getFieldName().equals(e.getColumnName())) {
                            try {
								((RowElement) screenFields.at(i)).setValue(table.getCell(e.getColumnName(),e.getRecordIndex()));
							} catch (InvalidCellAddressException e1) {
								e1.printStackTrace();
							}
                            break;
                        }
                }
            }

            //COLUMN ADDED EVENT/////////////////////////////////
            public void columnAdded(ColumnAddedEvent e) {
                try {
                    if (!table.getTableField(e.getColumnIndex()).isHidden()) {
                        RowElement re;
                        content.add((re=new RowElement(table.getTableField(e.getColumnIndex()).getName(),"",table,TIPanel.this,content.getWidth())));
                        screenFields.add(re);
                        re.viewAsSelectedRecord(table.getSelectedSubset().contains(table.getActiveRecord()));
                        content.doLayout();
                    }
                } catch(InvalidFieldIndexException ex) {}
            }

            //COLUMN REMOVED EVENT/////////////////////////////////
            public void columnRemoved(ColumnRemovedEvent e) {
                String fieldName=e.getColumnName();
                //Remove field from screen, if visible.
                for (int i=0;i<screenFields.size();i++)
                    if (((RowElement) screenFields.at(i)).getFieldName().equals(fieldName)) {
                        content.remove((Component) screenFields.at(i));
                        screenFields.remove(i);
                        break;
                    }
                content.doLayout();
            }

            //COLUMN HIDDEN STATE CHANGED EVENT/////////////////////////////////
            public void columnHiddenStateChanged(ColumnHiddenStateChangedEvent e) {
                if (e.isHidden()) {
                    for (int i=0;i<screenFields.size();i++)
                        if (((RowElement) screenFields.at(i)).getFieldName().equals(e.getColumnName())) {
                            content.remove((Component) screenFields.at(i));
                            screenFields.remove(i);
                            break;
                        }
                } else {
                    try {
                        final RowElement re=new RowElement(e.getColumnName(),"",table,TIPanel.this,content.getWidth());
                        Component[] c=content.getComponents();
                        content.removeAll();
                        //Find the insertion point
                        int ni=table.getFieldIndex(e.getColumnName());
                        int added=-1;
                        for (int i=0;i<c.length;i++) {
                            if (c[i] instanceof RowElement) {
                                if (added==-1 && table.getFieldIndex(((RowElement) c[i]).getFieldName())>ni) {
                                    content.add(re);
                                    content.add(c[i]);
                                    added=i;
                                } else
                                    content.add(c[i]);
                            } else
                               content.add(c[i]);
                        }
                        //Add in the tail, if not added previously
                        if (added==-1) {
                            content.add(re);
                            added=c.length;
                        }
                        if (added==0)
                            screenFields.insert(0,re);
                        else
                            screenFields.insert(added-1,re);
                        re.viewAsSelectedRecord(table.getSelectedSubset().contains(table.getActiveRecord()));
                        showCurrentRecord(false);
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                //Trick to make the row get the correct layout
                                re.setValue(re.getValue());
                            }
                        });
                    } catch(Exception ex) {ex.printStackTrace();}
                }
                content.doLayout();
            }

            public void tableRenamed(TableRenamedEvent e) {
                ti.renameTab(TIPanel.this,e.getNewTitle());
                ResourceBundle pinsBundle=TableInspector.pinsBundle;
                try {
                    recInputPlug.setName(pinsBundle.getString("inputrecord")+" "+pinsBundle.getString("of")+" \""+table.getTitle()+"\"");
                    recInputPlug.setNameLocaleIndependent("inputrecord of \""+table.getTitle()+"\"");
                } catch(PlugExistsException ex) {}
            }

            public void columnRenamed(ColumnRenamedEvent e) {
                for (int i=0;i<screenFields.size();i++)
                    if (((RowElement) screenFields.at(i)).getFieldName().equals(e.getOldName())) {
                        ((RowElement) screenFields.at(i)).setFieldName(e.getNewName());
                        break;
                    }
            }

            public void columnTypeChanged(ColumnTypeChangedEvent e) {
                String fieldName=e.getColumnName();
                for (int i=0;i<screenFields.size();i++)
                    if (((RowElement) screenFields.at(i)).getFieldName().equals(fieldName)) {
                        try {
                            if (showAll)
                                ((RowElement) screenFields.at(i)).setValue(table.getTableField(fieldName).getCellObject(curObj-1));
                            else
                                ((RowElement) screenFields.at(i)).setValue(table.getTableField(fieldName).getCellObject(records.get(curObj-1)));
                        } catch(Throwable t) {
                            System.err.println("TABLEINSPECTOR#200011221424: Can't set transformed value.");
                            t.printStackTrace();
                        }
                        break;
                    }
            }

            //TABLE REMOVED EVENT////////////////////////////////////
            /*public void tableRemoved(TableRemovedEvent e) {
                if (table.equals(e.getTableRemoved())) {
                    recPlug.disconnect();
                    idPlug.disconnect();
                    synchPlug.disconnect();
                    recInputPlug.disconnect();

            }*/

        };

        table.addTableModelListener(tmListen);

        try {
            ResourceBundle pinsBundle=TableInspector.pinsBundle;
            //Table pin
            Class soClass=Class.forName("gr.cti.eslate.sharedObject.RecordSO");
            SharedObjectListener sol=new SharedObjectListener() {
                public void handleSharedObjectEvent(SharedObjectEvent soe) {
                    SharedObject so=soe.getSharedObject();
                    StringBaseArray fn=((RecordSO) so).getFieldNames();
                    ArrayList ft=((RecordSO) so).getFieldTypes();
                    ArrayList r=null;
					try {
						r=((RecordSO) so).getTable().getRecord(((RecordSO) so).getRecordIndex());
					} catch (InvalidRecordIndexException e1) {
					}
                    if (r==null) return;

                    int recordNo;
                    if (showAll)
                        recordNo=curObj-1;
                    else {
                        if (records.size()==0)
                            return;
                        else
                            recordNo=records.get(curObj-1);
                    }

                    int fin=-1;
                    for (int i=0;i<fn.size();i++) {
                        try {
                            if ((fin=table.getFieldIndex(((String) fn.get(i))))!=-1) {
                            	if (table.getTableField(fin).getDataType().equals(ft.get(i))) {
                                    try {
                                        if ((table.getRecord(recordNo).get(fin)==null) && (r.get(i)!=null)) {
                                            table.setCell(fin,recordNo,r.get(i));
                                            for (int j=0;j<screenFields.size();j++) {
                                                if (((RowElement) screenFields.at(j)).getFieldName().equals(fn.get(i))) {
                                                    ((RowElement) screenFields.at(j)).setValue(r.get(i));
                                                }
                                            }
                                        }
                                    } catch(DuplicateKeyException e) {
                                    } catch(InvalidDataFormatException e) {
                                    } catch(InvalidCellAddressException e) {
                                    } catch(NullTableKeyException e) {
                                    } catch(InvalidRecordIndexException e) {
                                    } catch(AttributeLockedException e) {
                                    }
                                }
                            }
                        } catch(InvalidFieldNameException e) {
                        } catch(InvalidFieldIndexException e) {
                        }
                    }
                }
            };
            String addOn="";
            String plgName="inputrecord of \""+table.getTitle()+"\"";
            int c=0;
            while (ti.getESlateHandle().getPlugLocaleIndependent(plgName+addOn)!=null)
                addOn=" ("+(++c)+")";
            recInputPlug=new SingleInputPlug(ti.getESlateHandle(), null, pinsBundle.getString("inputrecord")+" "+pinsBundle.getString("of")+" \""+table.getTitle()+"\""+addOn, new Color(255,228,181), soClass,sol);
            recInputPlug.setNameLocaleIndependent(plgName+addOn);
            ti.getESlateHandle().addPlug(recInputPlug);
            recInputPlug.addConnectionListener(new ConnectionListener() {
                public void handleConnectionEvent(ConnectionEvent ce) {
                    ((SharedObjectPlug) ce.getPlug()).getSharedObject().addSharedObjectChangedListener(recInputPlug.getSharedObjectListener());
                }
            });
            recInputPlug.addDisconnectionListener(new DisconnectionListener() {
                public void handleDisconnectionEvent (DisconnectionEvent de) {
                    if (ti.hasBeenDestroyed) return;
                    ((SharedObjectPlug) de.getPlug()).getSharedObject().removeSharedObjectChangedListener(recInputPlug.getSharedObjectListener());
                }
            });
        } catch (InvalidPlugParametersException e) {
            System.out.println("InvalidPlugParametersException");
        } catch (PlugExistsException e) {
            System.out.println("PlugExistsException");
        } catch (ClassNotFoundException e) {
            //Possibly couldn't find the interface/SO. No Plug.
        } catch (NoClassDefFoundError e) {
            //Possibly couldn't find the interface/SO. No Plug.
        }

        //Create Record Synchronizer Plug
        try {
            ResourceBundle pinsBundle=TableInspector.pinsBundle;
            //Table pin
            Class soClass=Class.forName("gr.cti.eslate.sharedObject.NumberSO");
            SharedObjectListener sol=new SharedObjectListener() {
                @SuppressWarnings("deprecation")
				public void handleSharedObjectEvent(SharedObjectEvent soe) {
                    SharedObject so=soe.getSharedObject();
                    trySetCurObject(((NumberSO) so).value(),soe.getPath());
                }
            };
            synchSO=new NumberSO(ti.getESlateHandle(),null);
            synchPlug=new SingleInputMultipleOutputPlug(ti.getESlateHandle(),pinsBundle,"current", new Color(135,206,250), soClass, synchSO, sol);
            recInputPlug.addPlug(synchPlug);
            synchPlug.addConnectionListener(new ConnectionListener() {
                @SuppressWarnings("deprecation")
				public void handleConnectionEvent(ConnectionEvent e) {
                    if (e.getType()==Plug.OUTPUT_CONNECTION) {
                        //Make sure the plug gets proper data upon connection
                        SharedObjectEvent soe = new SharedObjectEvent(ti, synchSO,new Vector());
                        synchSO.fireSharedObjectChanged(soe);
                    } else { //INPUT_CONNECTION
                        isHazed=true;
                        previous.setEnabled(false);
                        next.setEnabled(false);
                        ti.setHaze(true);
                        ((SharedObjectPlug) e.getPlug()).getSharedObject().addSharedObjectChangedListener(synchPlug.getSharedObjectListener());
                    }
                }
            });
            synchPlug.addDisconnectionListener(new DisconnectionListener() {
                public void handleDisconnectionEvent (DisconnectionEvent de) {
                    if (de.getType()==Plug.OUTPUT_CONNECTION) {
                        if (ti.hasBeenDestroyed) return;
                        isHazed=false;
                        informTI();
                        ((SharedObjectPlug) de.getPlug()).getSharedObject().removeSharedObjectChangedListener(synchPlug.getSharedObjectListener());
                    }
                }
            });
        } catch (InvalidPlugParametersException e) {
        } catch (PlugExistsException e) {
        } catch (ClassNotFoundException e) {
            //Possibly couldn't find the interface/SO. No Plug.
        } catch (NoClassDefFoundError e) {
            //Possibly couldn't find the interface/SO. No Plug.
        } catch(NullPointerException e) {
            //The record plug has not been created.
        }

        //Copy selected set array
        records=table.getSelectedSubset();
        if ((records.size()!=0) && (!showAll)) {
            curObj=records.indexOf(table.getActiveRecord())+1;
/*            if (curObj==0) {
                raiseTheActiveRecordEvent=true;
                curObj=1;
            }*/
            setNoOfObjects(records.size());
        }

        if (showAll) {
            curObj=table.getActiveRecord()+1;
            setNoOfObjects(table.getRecordCount());
        }

        informTI();
        showCurrentRecord(false);
    }

    protected void checkStatusChanged() {
        ti.query.setEnabled(false);
        //ti.clear.setEnabled(false);
        for (int i=0;i<screenFields.size();i++)
            if (((RowElement) screenFields.at(i)).isSelected()) {
                ti.query.setEnabled(true);
                //ti.clear.setEnabled(true);
                break;
            }

    }

    private void showSelectedSet() {
        boolean iAmActive=TIPanel.this.equals(ti.getSelectedTIPanel()); //When false donot update the visual elements

        IntBaseArrayDesc tempSelectedSet=table.getSelectedSubset();
        if (!showAll) {
            noOfObjects=tempSelectedSet.size();
            if (noOfObjects==0) {
                clear();
                curObj=0;
                if (iAmActive) {
                    previous.setEnabled(false);
                    next.setEnabled(false);
                    query.setEnabled(false);
                    //clear.setEnabled(false);
                    noOf.setText(""+noOfObjects);
                    //noOf.setText(noOfObjects+" "+messagesBundle.getString("objects"));
                    for (int i=0;i<screenFields.size();i++)
                        ((RowElement) screenFields.at(i)).setValue(null);
                }
            } else {
                //Do this in order to show the same object after the query
                int curpos=-1;
                try {
                    curpos=records.get(curObj-1);
                } catch(Exception e) {
                    curpos=-1;
                }

                if ((curpos!=-1) && (tempSelectedSet.indexOf(curpos)!=-1)) {
                    curObj=tempSelectedSet.indexOf(curpos)+1;
                } else {
                    //if (table.getActiveRecord()!=-1) {
                        curObj=1;
                    //} Removed due to error array index out of bounds
                }
            }
        }

        //Copy selected set array
        records=tempSelectedSet;

        if (iAmActive) {
            informTI();
            showCurrentRecord(true);
        }
    }

    void addToSelectedSubset(RowElement re,String fieldName, Object value) {
        int mode;
        if (table.getSelectedSubset().size()==0)
            mode=LogicalExpression.NEW_SELECTION;
        else
            mode=LogicalExpression.SELECT_FROM_SELECTION;
        String queryStr=queryString(re,fieldName,value);
        if (queryStr!=null && queryStr.length()!=0) {
            try {
                ti.setCursor(ti.WAIT_CURSOR);
                nowQuerying=true;
                LogicalExpression le=new LogicalExpression(table,queryStr,mode,false);
                table.setSelectedSubset(le.getQueryResults());
                if (showAll) {
                    IntBaseArrayDesc ia=table.getSelectedSubset();
                    if (!ia.contains(curObj-1) && ia.size()!=0)
                        setCurObj(ia.get(0)+1,true);
                } else {
                    if (curObj==0)
                        setCurObj(1,true);
                }
            } catch(InvalidLogicalExpressionException ie) {
                nowQuerying=false;
                JOptionPane.showMessageDialog(TIPanel.this, messagesBundle.getString("couldntquery")+"\n"+ie.getMessage(), "Oops!", JOptionPane.ERROR_MESSAGE);
            }
            nowQuerying=false; //This is here and not in the db listener because the events are handled synchronously
            repaint();
            ti.setCursor(ti.DEFAULT_CURSOR);
        }
    }

    void removeFromSelectedSubset(String fieldName, Object value) {
        boolean atLeastOne=false;
        for (int i=0;i<screenFields.size();i++)
            if (((RowElement) screenFields.at(i)).isSelected()) {
                atLeastOne=true;
                break;
            }
        if (atLeastOne)
            query(0);
        else {
            //If all records are shown reset the subset. If only selected are shown, select everything
            //so the user can make new queries.
            if (showAll) {
                if (table.getSelectedSubset().size()!=0)
                    table.resetSelectedSubset();
            } else
                table.setSelectedSubset(outboundSelectedSet);
        }
        repaint();
    }

    void checkQueryType() {
        for (int i=0;i<screenFields.size();i++)
            ((RowElement) screenFields.at(i)).checkQueryType();
    }

    String queryString(RowElement re,String fieldName, Object value) {
        String queryStr=new String();
        if ((fieldName!=null) && (value!=null)) {
            if (value instanceof java.lang.String) {
                queryStr=queryStr.concat("("+fieldName+" = \""+(value.toString())+"\")");
                /*REMOVED as of 20001129-2.0.0: Queries not performed after tokenizing. Check for equality.
                StringTokenizer st=new StringTokenizer(value.toString()," ,\"",false);
                while (st.hasMoreTokens())
                    queryStr=queryStr.concat("("+fieldName+" "+messagesBundle.getString("CONTAINS")+" "+st.nextToken()+") "+messagesBundle.getString("OR")+" ");
                queryStr=queryStr.substring(0,queryStr.length()-3);*/
            } else if (value instanceof java.net.URL) {
                queryStr=queryStr.concat("("+fieldName+" = \""+(value.toString())+"\")");
                /*REMOVED as of 20001129-2.0.0: Queries not performed after tokenizing. Check for equality.
                queryStr=queryStr.concat("("+fieldName+
                    " "+messagesBundle.getString("CONTAINS")+" "+value.toString()+") "+messagesBundle.getString("AND")+" ("+
                    fieldName+" "+messagesBundle.getString("CONTAINED")+" "+value.toString()+
                    "))");*/
            } else if (value instanceof gr.cti.eslate.database.engine.CDate) {
                queryStr=queryStr.concat("("+fieldName+" = "+(value.toString())+")");
            } else if (value instanceof gr.cti.eslate.database.engine.CTime) {
                queryStr=queryStr.concat("("+fieldName+" = "+(value.toString())+")");
            } else if (value instanceof java.lang.Double) {
                queryStr=queryStr.concat("("+fieldName+" = "+table.getNumberFormat().format((Double) value)+")");
            } else
                queryStr=queryStr.concat("("+fieldName+" = "+value.toString()+")");
            re.setHasPerformedQuery(true);
        } else
            re.setHasPerformedQuery(false);
        return queryStr;
    }

    protected void setNoOfObjects(int no) {
        noOfObjects=no;
        if ((no!=0) && (curObj!=0)) {
            noOf.setText(curObj+" / "+no);
            //noOf.setText(messagesBundle.getString("object")+curObj+messagesBundle.getString("of")+no);
        } else {
            for (int i=0;i<screenFields.size();i++)
                ((RowElement) screenFields.at(i)).setValue(null);
            //noOf.setText(noOfObjects+" "+messagesBundle.getString("objects"));
            noOf.setText(""+noOfObjects);
            //Assure that there is no invalid curObj value
            curObj=0;
        }
        //ADDED19990929, to implement record cycle
        if (noOfObjects!=0) {
            previous.setEnabled(true);
            next.setEnabled(true);
        }
        /*REMOVED19990929, to implement record cycle
        if (curObj==1)
            previous.setEnabled(false);
        else if (!isHazed)
            previous.setEnabled(true);
        if (curObj==noOfObjects)
            next.setEnabled(false);
        else if (!isHazed)
            next.setEnabled(true);
        */
    }

    protected int getNoOfObjects() {
        return noOfObjects;
    }

    protected boolean trySetCurObject(Number I){
        return trySetCurObject(I,null);
    }

    protected boolean trySetCurObject(Number I,Vector path){
        //The argument is a double because a double is exported from the database
        if (I==null) {
            setCurObj(0,true);
            return false;
        }

        int i=I.intValue();
        if ((i<1) || (i>noOfObjects))
            return false;
        setCurObj(i,true,path);
        return true;
    }

    protected void setCurObj(int no,boolean raiseTheActiveRecordEvent) {
        setCurObj(no,raiseTheActiveRecordEvent,null);
    }

    protected void setCurObj(int no,boolean raiseTheActiveRecordEvent,Vector path) {
        if ((no<=noOfObjects) && (no>-1) && curObj!=no){
            curObj=no;
            if ((curObj!=0) && (noOfObjects!=0)) {
                noOf.setText(curObj+" / "+noOfObjects);
                //noOf.setText(messagesBundle.getString("object")+curObj+messagesBundle.getString("of")+noOfObjects);
                showHide.setEnabled(true);
            } else {
                noOf.setText(""+noOfObjects);
                showHide.setEnabled(false);
            }

            showCurrentRecord(raiseTheActiveRecordEvent,path);
        }/* else if (no==0) {
            curObj=0;
            noOf.setText(noOfObjects+" "+messagesBundle.getString("objects"));
            showCurrentRecord(raiseTheActiveRecordEvent);
        }*/

    }

    protected void showCurrentRecord(boolean raiseTheActiveRecordEvent) {
        showCurrentRecord(raiseTheActiveRecordEvent,null);
    }

    protected void showCurrentRecord(boolean raiseTheActiveRecordEvent,Vector path) {
        if (curObj>noOfObjects)
        	curObj=noOfObjects;
        StringBaseArray n=table.getFieldNames();
        int sfi=0;
        boolean selected=true;
        try {

        //Check whether the record belongs to the selected set
        if (showAll) {
            if (records.indexOf(curObj-1)==-1)
                selected=false;
        }

        ArrayList curRec;
        if (curObj!=0)
            curRec=getRecord(curObj);
        else {
            curRec=new ArrayList<Object>(table.getFieldCount());
            disableView();
        }

        if (curRec==null) curRec=new ArrayList(table.getFieldCount());

        for (int i=0;i<table.getFieldCount();i++) {
            try {
                if (table.getTableField(i).isHidden())
                    continue;

                ((RowElement) screenFields.at(sfi)).viewAsSelectedRecord(selected);
                try {
                    //Check whether an HREF object can be formed
                    HREF href=null; int ii;
                    if ((ii=n.indexOf((table.getTableField(i).getName().concat("_HREF"))))!=-1) {
                        try {
                            href=new HREF(curRec.get(i).toString(),(URL) curRec.get(ii));
                        } catch(Exception ex) {
                            try {
                                href=new HREF(curRec.get(i).toString(),new URL(ti.getESlateHandle().getESlateMicroworld().getDocumentBase(),(String) curRec.get(ii)));
                            } catch(Exception e) {
                                href=null;
                            }
                        }
                    }

                    if (href!=null) {
                        ((RowElement) screenFields.at(sfi)).setValue(href);
                    } else {
                        ((RowElement) screenFields.at(sfi)).setValue(curRec.get(i));
                    }

                } catch(Exception e) {
                    ((RowElement) screenFields.at(sfi)).setValue(null);
                }
                sfi++;
            } catch(InvalidFieldIndexException e) {}
        }

        //Update the current record pin
        if (path==null)
            path=new Vector();
        if (getRecord(curObj)!=null) {
            if (showAll) {
                ((NumberSO) synchSO).setValue(curObj,path);
            } else {
                ((NumberSO) synchSO).setValue(records.get(curObj-1),path);
            }
        } else
            ((NumberSO) synchSO).setValue(null,path);

        repaint();

        } catch(InvalidRecordIndexException iri) {
            ((NumberSO) synchSO).setValue(null,path);
        }

        //Update the active record
        if (raiseTheActiveRecordEvent && ti.getFollowActiveRecord()) {
            if (curObj>0) {
                if (showAll)
                    table.setActiveRecord(curObj-1);
                else
                    table.setActiveRecord(records.get(curObj-1));
            } else {
                table.setActiveRecord(-1);
            }
        }

        //Produce an event to inform about the change in the active record.
        int newVal;
        if (showAll) {
            newVal=curObj;
        } else {
            try {
                newVal=records.get(curObj-1)+1;
            } catch(Throwable e) {
                newVal=0;
            }
        }

        ti.listeners.activeRecordBrowserRecordChanged(new TableInspectorEvent(this,TableInspectorEvent.RB_ACTIVE_RECORD_CHANGED,ti.middle.indexOfComponent(this),new Integer(-1),new Integer(newVal)));

    }

    protected ArrayList getRecord(int i) throws InvalidRecordIndexException {
        if (showAll)
            return table.getRecord(i-1);
        else
            try {
                return table.getRecord(records.get(i-1));
            } catch (Exception e) {
                return null;
            }
    }

    /**
     * @return  The value added.
     */
    protected Object acceptNewValue(RowElement re,Object nv) throws InvalidDataFormatException, NullTableKeyException, InvalidCellAddressException, DuplicateKeyException {
        try {
            //Care for the booleans. Locale dependend strings for true/false.
            if (table.getTableField(re.getFieldName()).getDataType().equals(java.lang.Boolean.class)) {
                String acceptedTrue=TableInspector.messagesBundle.getString("acceptedtrue");
                StringTokenizer st=new StringTokenizer(acceptedTrue," ,",false);
                while (st.hasMoreTokens()) {
                    String tok=st.nextToken();
                    if (tok.equalsIgnoreCase((String) nv)) {
                        nv=new Boolean(true);
                        break;
                    }
                }
                if (!(nv instanceof Boolean)) {
                    String acceptedFalse=TableInspector.messagesBundle.getString("acceptedfalse");
                    StringTokenizer st2=new StringTokenizer(acceptedFalse," ,",false);
                    while (st2.hasMoreTokens()) {
                        String tok=st2.nextToken();
                        if (tok.equalsIgnoreCase((String) nv)) {
                            nv=new Boolean(false);
                            break;
                        }
                    }
                }
            }
            if (showAll) {
                table.setCell(re.getFieldName(),curObj-1,nv);
                return table.getCell(re.getFieldName(),curObj-1);
            } else {
                table.setCell(re.getFieldName(),records.get(curObj-1),nv);
                return table.getCell(re.getFieldName(),records.get(curObj-1));
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
            //////////To DOOOOOOOOOOOOOOOOOOOOOOOOO
        }
    }

    protected void enableView() {
        checkStatusChanged();
        for (int i=0;i<screenFields.size();i++)
            ((RowElement) screenFields.at(i)).setEnabled(true);
        validate();
        repaint();
    }

    protected void disableView() {
        query.setEnabled(false);
        //clear.setEnabled(false);
        for (int i=0;i<screenFields.size();i++) {
//            if (((((RowElement) screenFields.at(i)).isSelected()) && (((RowElement) screenFields.at(i)).hasPerformedQuery())) && (noOfObjects!=0))
            if (((((RowElement) screenFields.at(i)).isSelected()) &&
                (((RowElement) screenFields.at(i)).hasPerformedQuery()) && (noOfObjects!=0) && (curObj!=0))
                || (!hasShowHide))
                ((RowElement) screenFields.at(i)).setEnabled(true);
            else {
                ((RowElement) screenFields.at(i)).setEnabled(false);
                ((RowElement) screenFields.at(i)).setSelected(false);
            }
        }
        validate();
        repaint();
    }

    protected Table getTable() {
        return table;
    }

    private ImageIcon loadImageIcon(String filename) {
        return new ImageIcon(TableInspector.class.getResource("images/"+filename));
    }

    //Button Actions
    protected void previous() {
        requestFocus(); //Dummy. Nothing Happens. Used only to take the focus from the editing textbox

        //Inform the current being edited row to update the cell
        for (int i=0;i<screenFields.size();i++)
            if (!((RowElement) screenFields.at(i)).hasAcceptedLastValue) {
                ((RowElement) screenFields.at(i)).acceptNewValue(true);
            }

        //clear();
      //REMOVED19990929, to implement cycling of records if (curObj>1) {
            curObj--;
            /*REMOVED19990929, to implement cycling of records
            next.setEnabled(true);
            if (curObj==1)
                previous.setEnabled(false);*/
            //ADDED19990929, to implement cycling of records
            if (curObj<1)
                curObj=noOfObjects;
            noOf.setText(curObj+" / "+noOfObjects);
            //noOf.setText(messagesBundle.getString("object")+curObj+messagesBundle.getString("of")+noOfObjects);
            if (hasShowHide) {
                showHide.setSelected(false);
                showHideStatus=false;
                disableView();
                showCurrentRecord(true);
            } else {
                showCurrentRecord(true);
            }
        repaint();
        //}
    }

    protected void next() {
        requestFocus(); //Dummy. Nothing Happens. Used only to take the focus from the editing textbox

        //Inform the current being edited row to update the cell
        for (int i=0;i<screenFields.size();i++)
            if (!((RowElement) screenFields.at(i)).hasAcceptedLastValue) {
                ((RowElement) screenFields.at(i)).acceptNewValue(true);
            }

        //clear();
        //REMOVED19990929, to implement cycling of records if (curObj<=noOfObjects) {
            curObj++;
            //REMOVED19990929, to implement cycling of records previous.setEnabled(true);
            if (curObj>noOfObjects)
                curObj=1;
                //REMOVED19990929, to implement cycling of records next.setEnabled(false);
            noOf.setText(curObj+" / "+noOfObjects);
            //noOf.setText(messagesBundle.getString("object")+curObj+messagesBundle.getString("of")+noOfObjects);
            if (hasShowHide) {
                showHide.setSelected(false);
                showHideStatus=false;
                disableView();
                showCurrentRecord(true);
            } else {
                showCurrentRecord(true);
            }
        repaint();
        //}
    }

    protected void query(int mode) {
        requestFocus(); //Dummy. Nothing Happens. Used only to take the focus from the editing textbox

        RowElement re;
        String queryStr=new String("");

        if (table!=null) for (int i=0;i<screenFields.size();i++) {
            if ((re=((RowElement) screenFields.at(i))).isSelected()) {
                try {
                    Class fieldType=table.getTableField(re.getFieldName()).getDataType();
                    if ((re.getFieldName()!=null) && (re.getQueryValue()!=null)) {
                        if (fieldType.equals(Class.forName("java.lang.String"))) {
                            queryStr=queryStr.concat("("+((String)re.getFieldName())+" = \""+(re.getQueryValue().toString())+"\") "+messagesBundle.getString("AND")+" ");
                            /*REMOVED as of 20001129-2.0.0: Queries not performed after tokenizing. Check for equality.
                            StringTokenizer st=new StringTokenizer((String) re.getQueryValue()," ,\"",false);
                            queryStr=queryStr.concat("(");
                            while (st.hasMoreTokens())
                                queryStr=queryStr.concat("("+((String) re.getFieldName())+" "+messagesBundle.getString("CONTAINS")+" "+st.nextToken()+") "+messagesBundle.getString("OR")+" ");
                            queryStr=queryStr.substring(0,queryStr.length()-3);
                            queryStr=queryStr.concat(") "+messagesBundle.getString("AND")+" ");*/
                        } else if (fieldType.equals(Class.forName("java.net.URL"))) {
                            queryStr=queryStr.concat("("+((String)re.getFieldName())+" = \""+(re.getQueryValue().toString())+"\") "+messagesBundle.getString("AND")+" ");
                            /*REMOVED as of 20001129-2.0.0: Queries not performed after tokenizing. Check for equality.
                                queryStr=queryStr.concat("(("+((String) re.getFieldName())+
                                " "+messagesBundle.getString("CONTAINS")+" "+re.getQueryValue().toString()+") "+messagesBundle.getString("AND")+" ("+
                                ((String) re.getFieldName())+" "+messagesBundle.getString("CONTAINED")+" "+re.getQueryValue().toString()+
                                ")) "+messagesBundle.getString("AND")+" ");*/
                        } else if (fieldType.equals(Class.forName("gr.cti.eslate.database.engine.CDate"))) {
                            queryStr=queryStr.concat("("+((String)re.getFieldName())+" = "+(re.getQueryValue().toString())+") "+messagesBundle.getString("AND")+" ");
                        } else if (fieldType.equals(Class.forName("gr.cti.eslate.database.engine.CTime"))) {
                            queryStr=queryStr.concat("("+((String)re.getFieldName())+" = "+(re.getQueryValue().toString())+") "+messagesBundle.getString("AND")+" ");
                        } else if (fieldType.equals(Class.forName("java.lang.Double"))) {
                            queryStr=queryStr.concat("("+((String)re.getFieldName())+" = "+table.getNumberFormat().format((Double) re.getQueryValue())+") "+messagesBundle.getString("AND")+" ");
                        } else
                            queryStr=queryStr.concat("("+((String)re.getFieldName())+" = "+re.getQueryValue().toString()+") "+messagesBundle.getString("AND")+" ");
                        re.setHasPerformedQuery(true);
                    } else
                        re.setHasPerformedQuery(false);
                } catch (ClassNotFoundException ce) {
                    re.setHasPerformedQuery(false);
                    //Nothing to be done. In fact this is impossible!
                } catch (InvalidFieldNameException ce) {
                    re.setHasPerformedQuery(false);
                    //Nothing to be done. In fact this is impossible!
                }
            }
        }

        if (queryStr.length()!=0) {
            queryStr=queryStr.substring(0,queryStr.length()-5);
            try {
                ti.setCursor(ti.WAIT_CURSOR);
                nowQuerying=true;
                LogicalExpression le=new LogicalExpression(table,queryStr,mode,false);
                //If an outbound selection exists, find the intersection
                if (outboundSelectedSet!=null && outboundSelectedSet.size()>0) {
                    IntBaseArrayDesc qr=le.getQueryResults();
                    IntBaseArrayDesc intersect=(IntBaseArrayDesc) qr.clone();
                    for (int i=qr.size()-1;i>-1;i--)
                        if (!outboundSelectedSet.contains(qr.get(i)))
                            intersect.remove(i);
                    table.setSelectedSubset(intersect);
                } else
                    table.setSelectedSubset(le.getQueryResults());
            } catch(InvalidLogicalExpressionException ie) {
                nowQuerying=false;
                JOptionPane.showMessageDialog(TIPanel.this, messagesBundle.getString("couldntquery")+"\n"+ie.getMessage(), "Oops!", JOptionPane.ERROR_MESSAGE);
            }
            nowQuerying=false; //This is here and not in the db listener because the events are handled synchronously
            ti.setCursor(ti.DEFAULT_CURSOR);
        } else {
            showSelectedSet();
        }

    }

    protected void clear() {
        requestFocus(); //Dummy. Nothing Happens. Used only to take the focus from the editing textbox

        RowElement re;
        for (int i=0;i<screenFields.size();i++)
            if ((re=((RowElement) screenFields.at(i))).isSelected()) {
                re.setSelected(false);
                re.setHasPerformedQuery(false);
            }
        //If all records are shown reset the subset. If only selected are shown, select everything
        //so the user can make new queries.
        /*As of 20001129-2.0.0:Removed and left simply to clear the fields
        if (showAll) {
            if (table.getSelectedSubset().size()!=0)
                table.resetSelectedSubset();
        } else
            table.setSelectedSubset(outboundSelectedSet);*/
        repaint();
    }

    protected void showHide() {
        requestFocus(); //Dummy. Nothing Happens. Used only to take the focus from the editing textbox
        if ((curObj==0) && (showHide.isSelected())) {
            showHide.setSelected(false);
            return;
        }
        if (showHide.isSelected())
            enableView();
        else
            disableView();
        showHideStatus=showHide.isSelected();
    }

    protected void selectShow(boolean all) {
        onLoadSelectShow(all);
        informTI();
        showCurrentRecord(true);
    }

    protected void onLoadSelectShow(boolean all) {
        showAll=all;
        if (showAll) {
        //This means that not all records were shown previously
            setNoOfObjects(table.getRecordCount());
            //Do this in order to show the same object after the query
            try {
                curObj=records.get(curObj-1)+1;
            } catch(Exception w) {
                curObj=((table.getRecordCount()>0)?1:0);
            }
        } else {
            //Do this in order to show the same object after the query
            int curpos=-1;
            try {
                if (records.size()==0)
                    curpos=-1;
                else
                    curpos=records.indexOf(curObj-1)+1;  /***!!!***/
            } catch(Exception e) {
                curpos=-1;
            }

            if (curpos<=0) //curpos becomes 0 when the /***!!!***/ command returns -1
                curObj=1;
            else
                curObj=curpos;

            setNoOfObjects(records.size());

/*            if (curpos!=-1) && (records.indexOf(new Integer(curpos))!=-1))
                curObj=records.indexOf(new Integer(curpos))+1;
            else
                curObj=1;*/
        }
        informTI();
    }

    public void setRowBorderPainted(boolean b) {
        if (b)

            for (int i=0;i<screenFields.size();i++) {

                ((RowElement) screenFields.at(i)).value.setBorder(RowElement.border);
                ((RowElement) screenFields.at(i)).value.setOpaque(true);
            }
        else

            for (int i=0;i<screenFields.size();i++) {

                ((RowElement) screenFields.at(i)).value.setBorder(null);
                ((RowElement) screenFields.at(i)).value.setOpaque(false);
            }
        repaint();
    }


    protected void informTI() {
        if (noOfObjects==0) {
            showHide.setSelected(showHideStatus);
            noOf.setText(""+noOfObjects);
            //noOf.setText(noOfObjects+" "+messagesBundle.getString("objects"));
            query.setEnabled(false);
            //clear.setEnabled(false);
            previous.setEnabled(false);
            next.setEnabled(false);
        } else {
            showHide.setSelected(showHideStatus);
            if ((curObj!=0) && (noOfObjects!=0))
                noOf.setText(curObj+" / "+noOfObjects);
                //noOf.setText(messagesBundle.getString("object")+curObj+messagesBundle.getString("of")+noOfObjects);
            else
                noOf.setText(""+noOfObjects);
                //noOf.setText(noOfObjects+" "+messagesBundle.getString("objects"));
            checkStatusChanged();
        }

        if (isHazed) {
            ti.setHaze(true);
            previous.setEnabled(false);
            next.setEnabled(false);
        } else {
            ti.setHaze(false);
            //ADDED19990929, to implement record cycle.
            if (noOfObjects!=0) {
                next.setEnabled(true);
                previous.setEnabled(true);
            }
            /*REMOVED19990929, to implement record cycle.
            if (curObj<=1)
                previous.setEnabled(false);
            else
                previous.setEnabled(true);

            if (curObj>=noOfObjects)
                next.setEnabled(false);
            else
                next.setEnabled(true);
            */
        }

        show.setValueAsynchronously(((showAll)?0:1));

    }

    protected void die() {
        //Remove plugs
        try {
            recInputPlug.removePlug(synchPlug);
        } catch (NoSuchPlugException e) {}
        try {
            ti.getESlateHandle().removePlug(recInputPlug);
        } catch (NoSuchPlugException e) {}
        //Remove Listener
        table.removeTableModelListener(tmListen);
        table=null;
    }

    protected void focusNextField(RowElement rr) {
        int i=screenFields.indexOf(rr);
        if (i!=-1)
            ((RowElement) screenFields.at(i)).edit();
    }

    protected void queryTools(boolean value) {
        for (int i=0;i<screenFields.size();i++)
            ((RowElement) screenFields.at(i)).queryTools(value);
    }

    /**
     * Called when the width of the field name must change, due to a change in the percentage property of TI.
     */
    void percentChanged() {
        for (int i=0;i<screenFields.size();i++)
            ((RowElement) screenFields.at(i)).percentChanged();
    }
    /**
     * Known method.
     */
    public void setFont(Font f) {
        super.setFont(f);
        for (int i=0;i<getComponentCount();i++)
            ((Component) getComponents()[i]).setFont(f);
        if (content!=null)
            content.setFont(f);
    }

    Object[] getDistinctFieldValues(String fieldName) {
        try {
            HashSet<Object> set=new HashSet<Object>();
            AbstractTableField tf=table.getTableField(fieldName);
            int rc=table.getRecordCount();
            Object[] o=new Object[rc];
            //Make a set of the distinct values
            if (showAll)
                for (int i=0;i<rc;i++)
                    set.add(tf.getCellObject(i));
            else
                for (int i=0;i<records.size();i++)
                    set.add(tf.getCellObject(records.get(i)));
            Iterator it=set.iterator();
            int counter=0;
            //Put in an array and sort.
            while (it.hasNext()) {
                Object obj=it.next();
                if (obj!=null)
                    o[counter++]=obj;
            }
            //Trim the array
            Object[] distinct=new Object[counter];
            if (counter!=0) {
                System.arraycopy(o,0,distinct,0,counter);
                Arrays.sort(distinct);
            }
            return distinct;
        } catch(Throwable t) {
            System.err.println("TABLEINSPECTOR#200011271158: Cannot get distinct field values.");
            t.printStackTrace();
            return new Object[0];
        }
    }
    /**
     * Locks the field to its current value. If already locked, it is unlocked
     * and then relocked to the new value.
     */
    void lock(String fieldName) {
        boolean found=false;
        for (int i=0;i<screenFields.size();i++)
            if (((RowElement) screenFields.at(i)).getFieldName().equalsIgnoreCase(fieldName)) {
                ((RowElement) screenFields.at(i)).lock();
                found=true;
            }
        if (!found)
            throw new IllegalArgumentException("No field exists with this name.");
    }
    /**
     * Locks the field to the given value. If already locked, it is unlocked
     * and then relocked to the new value.
     */
    void lock(String fieldName,String value) {
        boolean found=false;
        for (int i=0;i<screenFields.size();i++)
            if (((RowElement) screenFields.at(i)).getFieldName().equalsIgnoreCase(fieldName)) {
                Object[] v=getDistinctFieldValues(fieldName);
                for (int j=0;j<v.length;j++)
                    if (v[j].toString().equals(value)) {
                        ((RowElement) screenFields.at(i)).lock(value);
                        break;
                    }
                found=true;
            }
        if (!found)
            throw new IllegalArgumentException("No field exists with this name.");
    }
    /**
     * Locks the field to the given value. If already locked, it is unlocked
     * and then relocked to the new value.
     */
    void lock(int fieldIndex,String value) {
        Object[] v=getDistinctFieldValues(((RowElement) screenFields.at(fieldIndex)).getFieldName());
        for (int j=0;j<v.length;j++)
            if (v[j].toString().equals(value)) {
                ((RowElement) screenFields.at(fieldIndex)).lock(value);
                break;
            }
    }
    /**
     * Unlocks the field.
     */
    void unlock(String fieldName) {
        boolean found=false;
        for (int i=0;i<screenFields.size() && !found;i++)
            if (((RowElement) screenFields.at(i)).getFieldName().equalsIgnoreCase(fieldName)) {
                ((RowElement) screenFields.at(i)).unlock();
                found=true;
            }
        if (!found)
            throw new IllegalArgumentException("No field exists with this name.");
    }

    void setFieldQueryToolVisible(String fieldName,boolean state) {
        boolean found=false;
        for (int i=0;i<screenFields.size() && !found;i++)
            if (((RowElement) screenFields.at(i)).getFieldName().equalsIgnoreCase(fieldName)) {
                ((RowElement) screenFields.at(i)).setQueryVisible(state);
                found=true;
            }
        if (!found)
            throw new IllegalArgumentException("No field exists with this name.");
    }

    boolean isFieldQueryToolVisible(String fieldName) {
        for (int i=0;i<screenFields.size();i++)
            if (((RowElement) screenFields.at(i)).getFieldName().equalsIgnoreCase(fieldName))
                return ((RowElement) screenFields.at(i)).isQueryVisible();
        throw new IllegalArgumentException("No field exists with this name.");
    }

    boolean[] getFieldQueryToolVisibility() {
        boolean[] b=new boolean[screenFields.size()];
        for (int i=0;i<b.length;i++)
            b[i]=((RowElement) screenFields.at(i)).isQueryVisible();
        return b;
    }

    void setFieldQueryToolVisibility(boolean[] b) {
        for (int i=0;i<screenFields.size();i++)
            ((RowElement) screenFields.at(i)).setQueryVisible(b[i]);
    }
    
    public StorageStructure getState() {
    	ESlateFieldMap2 fm=new ESlateFieldMap2(2);
    	fm.put("showall",showAll);
    	//if (!ti.getFollowActiveRecord())
    	fm.put("curobj",curObj);
    	int count=0;
    	for (int i=0;i<screenFields.size();i++) {
    		if (((RowElement) screenFields.at(i)).isSelected()) {
    			fm.put("lock"+(++count),i);
    			if (((RowElement) screenFields.at(i)).getQueryValue() instanceof Double)
    				fm.put("lock"+count+"value",table.getNumberFormat().format((Double) ((RowElement) screenFields.at(i)).getQueryValue()));
    			else
    				fm.put("lock"+count+"value",((RowElement) screenFields.at(i)).getQueryValue().toString());
    		}
    	}
    	fm.put("locks",count);
    	fm.put("queryvisibility",getFieldQueryToolVisibility());
    	return fm;
    }
    
   	public void setState(StorageStructure fm) {
    	showAll=fm.get("showall",showAll);
    	
    	curObj=fm.get("curobj",-1);
    	int count=fm.get("locks",0);
    	if (count>0) {
    		locks=new Object[count][2];
    		int lc=1;
    		while (lc<=count) {
    			locks[lc-1][0]=new Integer(fm.get("lock"+lc,0));
    			locks[lc-1][1]=fm.get("lock"+lc+"value");
    			lc++;
    		}
    	}
    	queryToolVisibility=(boolean[]) fm.get("queryvisibility",(boolean[]) null);
    }

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		setState((StorageStructure) in.readObject());
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(getState());
	}
}


