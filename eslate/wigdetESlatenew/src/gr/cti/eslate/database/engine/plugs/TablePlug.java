package gr.cti.eslate.database.engine.plugs;

import java.awt.*;
import java.awt.image.*;
import java.util.Date;
import java.util.Vector;
import java.net.URL;
import javax.swing.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.sharedObject.*;
//import gr.cti.eslate.protocol.*;
import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.tableModel.event.*;
//import gr.cti.eslate.event.*;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.tableModel.ITableModel;
import gr.cti.typeArray.StringBaseArray;

import com.objectspace.jgl.Array;
import java.util.ArrayList;

/**
 * The TablePlug is a plug which handles automaticaly all the subplugs of a table (record and fields).
 * It is a database listener so it manipulates renamings, deletions, additions etc. It has an index
 * indicating which record is show by the plugs.
 * Apart from the standard behavior, one may attach another database listener for additional behavior.
 * <p>
 *
 * @author  Giorgos Vasiliou
 * @version 1.0, 5 Jul 2000
 */
public class TablePlug extends MaleSingleIFMultipleConnectionProtocolPlug {
//long start = System.currentTimeMillis();
/*    public TablePlug(ESlateHandle handle, ILayerView lv, Color color, Class soClass, SharedObject tableSO) throws InvalidPinParametersException {
        this(handle,((LayerView) lv).getLayer().getTable(),Map.bundlePlug.getString("record")+" "+Map.bundlePlug.getString("of")+" \""+((LayerView) lv).getLayer().getTable().getTitle()+"\"",color,soClass,tableSO);
    }

    public TablePlug(ESlateHandle handle, ILayerView lv, String plugTitle, Color color, Class soClass, SharedObject tableSO) throws InvalidPinParametersException {
        this(handle,((LayerView) lv).getLayer().getTable(),plugTitle,color,soClass,tableSO);
    }
*/
    public TablePlug(Table table) throws InvalidPlugParametersException {
        this(Table.bundle.getString("Record")+" "+Table.bundle.getString("of")+" \""+table.getTitle()+"\"", table);
    }

    public TablePlug(String recPlugTitle, Table table) throws InvalidPlugParametersException {
//        super(tableSO.getHandle(), null, tableSO.getTable().getTitle(), TABLE_PLUG_COLOR, soClass, tableSO);
        super(table.getESlateHandle(), null, table.getTitle(), TABLE_PLUG_COLOR);
//System.out.println("TablePlug constructor: " + table.getESlateHandle());
        java.util.Vector exportedif = new java.util.Vector();
        exportedif.addElement(Table.class);
        exportedif.addElement(ITableModel.class);
        setExportedInterfaces(exportedif);
        setHostingPlug(true);
        index=-1;
//        this.tableSO = tableSO;
//        Table table = tableSO.getTable();
        this.table = table;
        this.handle = table.getESlateHandle();
//        ((TableSO) tableSO).setTable(table);
        //
/*        addConnectionListener(new ConnectionListener() {
            public void handleConnectionEvent(ConnectionEvent e) {
//              System.out.println("TablePlug CONNECTED");
//              Thread.currentThread().dumpStack();
//                Table table = (Table) e.getOwnPlug().getHandle().getComponent();
//                SharedObjectEvent soe = new SharedObjectEvent(e.getOwnPlug().getHandle(), ((SharedObjectPlug) e.getOwnPlug()).getSharedObject());
//                ((SharedObjectPlug) e.getPlug()).getSharedObjectListener().handleSharedObjectEvent(soe);
            }
        });

        addDisconnectionListener(new DisconnectionListener() {
           public void handleDisconnectionEvent(DisconnectionEvent e) {
//              System.out.println("TablePlug disconnected");
//              Thread.currentThread().dumpStack();
//                Table table = (Table) e.getOwnPlug().getHandle().getComponent();
//                SharedObjectEvent soe = new SharedObjectEvent(e.getOwnPlug().getHandle(), ((SharedObjectPlug) e.getOwnPlug()).getSharedObject());
//                ((SharedObjectPlug) e.getPlug()).getSharedObjectListener().handleSharedObjectEvent(soe);
            }
        });
*/
        //Create Record Pin depended to the table pin
        try {
            Class soClass=Class.forName("gr.cti.eslate.sharedObject.RecordSO");
            RecordSO recSO=new RecordSO(handle);
            //Set Field names on Record SO
            recSO.setFieldNames(table.getFieldNames());
            //Set Field types on Record SO
            ArrayList ft=new ArrayList();
            for (int z=0;z<table.getFieldCount();z++)
                try {
                    ft.add(table.getTableField(z).getDataType());
                } catch(InvalidFieldIndexException exs) {}
            recSO.setFieldTypes(ft);

            recPlug=new MultipleOutputPlug(handle, null, recPlugTitle, new Color(255,228,181), soClass, recSO);
            recPlug.setNameLocaleIndependent("record of "+table.getTitle());
//            addPlug(recPlug);
//System.out.println("TablePlug 3: " + (System.currentTimeMillis()-start));
/*            recPlug.addConnectionListener(new ConnectionListener() {
                public void handleConnectionEvent(ConnectionEvent e) {
                    SharedObjectEvent soe = new SharedObjectEvent(e.getOwnPlug().getHandle(), ((SharedObjectPlug) e.getOwnPlug()).getSharedObject());
                    ((SharedObjectPlug) e.getPlug()).getSharedObjectListener().handleSharedObjectEvent(soe);
                }
            });
*/
            //Set Field names on Record SO
//System.out.println("TablePlug 3.1: " + (System.currentTimeMillis()-start));
            recSO.setFieldNames(table.getFieldNames());
//System.out.println("TablePlug 3.2: " + (System.currentTimeMillis()-start));
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println("TablePlug#200004032231: Cannot create record or field plugs.");
        }

//System.out.println("TablePlug 4: " + (System.currentTimeMillis()-start));
        createFieldPlugs();
//System.out.println("TablePlug 5: " + (System.currentTimeMillis()-start));

//        table.getCDatabase().addDatabaseListener(dbAdapter);
        table.addTableModelListener(tableListener);
//System.out.println("TablePlug end: " + (System.currentTimeMillis()-start));
    }

    public void createFieldPlugs() {
        // The following has been removed. The field plug creation mechanism
        // takes care that the field plugs are only created once, if when this
        // method is called multiple times (as it happens).
        //Destroy all the field plugs that belong to the recordPlug
/*        for (int i=0; i<recPlug.getChildPlugs().length; ) {
            try{
                recPlug.removePlug(recPlug.getChildPlugs()[i]);
            }catch (NoSuchPlugException exc) {
                exc.printStackTrace();
            }
        }
*/

        //Create child pins
//      Table table = tableSO.getTable();
        StringBaseArray n=table.getFieldNames();
        Plug[] childPlugs = recPlug.getChildPlugs();
        /* If no field plug has been created, go ahead and create all of them. */
        if (childPlugs.length == 0) {
            int fieldCount = table.getFieldCount();
            for (int j=0;j<fieldCount;j++) {
                try{
                    createFieldPlug(j);
                }catch (Throwable thr) {
                    System.out.println("Error while creating plug for a field of the table \"" + table.getTitle() + "\" 1");
                }
            }
        }else{
            int fieldCount = table.getFieldCount();
            for (int j=0;j<fieldCount;j++) {
                try{
                    String fieldName = table.getTableField(j).getName();
                    /* Check if there already exists a child plug with this name.
                     * If one exists, then don't create a new plug.
                     */
                    boolean plugExists = false;
                    for (int i=childPlugs.length-1; i>=0; i--) {
                        if (childPlugs[i].getName().equals(fieldName)) {
                            plugExists = true;
                            break;
                        }
                    }
                    if (!plugExists)
                        createFieldPlug(j);
                }catch (Throwable thr) {
                    System.out.println("Error while creating plug for a field of the table \"" + table.getTitle() + "\" 1");
                }
            }
        }
    }

    /**
     * Only one DatabaseListener may exist.
     */
/*    public void addDatabaseListener(DatabaseListener dbListener) {
        if (addedListener!=null)
            table.getCDatabase().removeDatabaseListener(addedListener);
        addedListener=dbListener;
        table.getCDatabase().addDatabaseListener(dbListener);
    }

    public void removeAdditionalDatabaseListener() {
        if (addedListener==null) return;
        table.getCDatabase().removeDatabaseListener(addedListener);
        addedListener=null;
    }
*/
    public void destroy() {
        table.removeTableModelListener(tableListener);
        table = null;
//        tableSO.getTable().removeTableModelListener(tableListener);
//        table.getCDatabase().removeDatabaseListener(dbAdapter);
//        if (addedListener!=null)
//            table.getCDatabase().removeDatabaseListener(addedListener);
    }

    public Table getTable() {
        return table; //tableSO.getTable();
    }

    public Plug getRecordPlug() {
        return recPlug;
    }

    public void updatePlugValues(int idx) {
//System.out.println("updatePlugValues()");
        index=idx;
        //Update record plug
        if (idx!=-1)
            try {
//                ((RecordSO) recPlug.getSharedObject()).setRecord(tableSO.getTable().getRecord(idx));
//                ((RecordSO) recPlug.getSharedObject()).setRecord(table.getRecord(idx));
                ((RecordSO) recPlug.getSharedObject()).setRecordIndex(idx);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        else
            ((RecordSO) recPlug.getSharedObject()).setRecordIndex(-1);
        //Update field plugs
        for (int i=0;i<recPlug.getChildPlugs().length;i++) {
            updateFieldPlugValue((SharedObjectPlug) recPlug.getChildPlugs()[i],idx);
        }
    }

    private void updateFieldPlugValue(SharedObjectPlug fp,int idx) {
//System.out.println("updateFieldPlugValue() 1 fp: " + fp);
//System.out.println("updateFieldPlugValue() 1 fp.getSharedObject(): " + fp.getSharedObject().getClass());
//        Table table = tableSO.getTable();
        //Set the values of the shared objects to the correct cell value or null if the active
        //record is -1.
        try {
        /********* String *********/
        if (fp.getSharedObject() instanceof StringSO) {
            if (idx!=-1)
                ((StringSO) fp.getSharedObject()).setString((String) table.getCell(fp.getName(),idx));
            else
                ((StringSO) fp.getSharedObject()).setString(null);
        /********* Date *********/
        } else if (fp.getSharedObject() instanceof DateSO) {
            if (idx!=-1)
                ((DateSO) fp.getSharedObject()).setDate((Date) table.getCell(fp.getName(),idx));
            else
                ((DateSO) fp.getSharedObject()).setDate(null);
        /********* URL *********/
        } else if (fp.getSharedObject() instanceof UrlSO) {
            if (idx!=-1)
                ((UrlSO) fp.getSharedObject()).setURL((URL) table.getCell(fp.getName(),idx));
            else
                ((UrlSO) fp.getSharedObject()).setURL(null);
        /********* Number *********/
        } else if (fp.getSharedObject() instanceof NumberSO) {
            if (idx!=-1)
                ((NumberSO) fp.getSharedObject()).setValue((Double) table.getCell(fp.getName(),idx));
            else
                ((NumberSO) fp.getSharedObject()).setValue(null);
        /********* Boolean *********/
        } else if (fp.getSharedObject() instanceof BooleanSO) {
            if (idx!=-1)
                ((BooleanSO) fp.getSharedObject()).setBoolean((Boolean) table.getCell(fp.getName(),idx));
            else
                ((BooleanSO) fp.getSharedObject()).setBoolean(null);
        /********* ImageIcon *********/
        } else if (fp.getSharedObject() instanceof IconSO) {
            Icon ic=((IconSO) fp.getSharedObject()).getIconSO();
            if (ic != null && ImageIcon.class.isAssignableFrom(ic.getClass()))
                ((ImageIcon) ic).getImage().flush();
            if (idx!=-1) {// && ic!=null) {
                CImageIcon cimageIcon = (CImageIcon) table.getTableField(fp.getName()).getCellObject(idx);
                RestorableImageIcon icon = null;
                if (cimageIcon != null)
                    icon = new RestorableImageIcon(cimageIcon.getImage());
//                    RestorableImageIcon icon = new RestorableImageIcon( ((CImageIcon) table.getCell(fp.getName(),idx)).getImage());
                ((IconSO) fp.getSharedObject()).setIconSO(icon); //GT new RestorableImageIcon(bi));
            } else
                ((IconSO) fp.getSharedObject()).setIconSO(null);
        }
        } catch(Throwable thr) {
            thr.printStackTrace();
            System.err.println("TablePlug#200004041434: Cannot update field plug value for "+fp.getName()+".");
        }
    }

    private void createFieldPlug(int fieldIdx) {
        try {
        final int fieldIndex=fieldIdx;
//        TableField field=tableSO.getTable().getTableField(fieldIndex);
        AbstractTableField field=table.getTableField(fieldIndex);
//System.out.println("createFieldPlug() field: " + field.getName());
        Class obj=field.getDataType();
        Class soClass; SharedObjectPlug pin;
        /********* String *********/
//System.out.println("field: " + field + ", data type: " + obj);
        if (obj.equals(Class.forName("java.lang.String"))) {
            soClass=Class.forName("gr.cti.eslate.sharedObject.StringSO");
            SharedObject childSO=new StringSO(handle);
            if (field.isEditable()) {
                SharedObjectListener sol=new SharedObjectListener() {
                    public void handleSharedObjectEvent(SharedObjectEvent e) {
                        if (index==-1) return;
                        try {
//                            Object o=tableSO.getTable().getCell(fieldIndex,index);
                            Object o=table.getCell(fieldIndex,index);
                            Object so=((StringSO) e.getSharedObject()).getString();
                            if ((o!=null && o.equals(so)) || (o==null && so==null));
                            else
                                table.setCell(fieldIndex,index,so);
//                                tableSO.getTable().setCell(fieldIndex,index,so);
                        } catch(Exception ae) {
                            ae.printStackTrace();
                        }
                    }
                };
                recPlug.addPlug(pin=new SingleInputMultipleOutputPlug(handle, null, field.getName(), new Color(139,117,0), soClass, childSO,sol));
            } else
                recPlug.addPlug(pin=new MultipleOutputPlug(handle, null, field.getName(), new Color(139,117,0), soClass, childSO));
/*            pin.addConnectionListener(new ConnectionListener() {
                public void handleConnectionEvent(ConnectionEvent e) {
                    SharedObject so= ((SharedObjectPlug) e.getOwnPlug()).getSharedObject();
                    //Make sure the connectee gets proper data upon connection
                    SharedObjectEvent soe = new SharedObjectEvent(handle, so);
                    so.fireSharedObjectChanged(soe);
                }
            });
*/
        /********* Date *********/
        } else if (Date.class.isAssignableFrom(obj)) { //if (obj.equals(Class.forName("java.util.Date"))) {
            soClass=Class.forName("gr.cti.eslate.sharedObject.DateSO");
            SharedObject childSO=new DateSO(handle);
            if (field.isEditable()) {
                SharedObjectListener sol=new SharedObjectListener() {
                    public void handleSharedObjectEvent(SharedObjectEvent e) {
                        if (index==-1) return;
                        try {
//                            Object o=tableSO.getTable().getCell(fieldIndex,index);
                            Object o=table.getCell(fieldIndex,index);
                            Object so=((DateSO) e.getSharedObject()).getDate();
                            if ((o!=null && o.equals(so)) || (o==null && so==null));
                            else
                                table.setCell(fieldIndex,index,so);
//                                tableSO.getTable().setCell(fieldIndex,index,so);
                        } catch(Exception ae) {
                        }
                    }
                };
                recPlug.addPlug(pin=new SingleInputMultipleOutputPlug(handle, null, field.getName(), new Color(34,139,134), soClass, childSO,sol));
            } else
                recPlug.addPlug(pin=new MultipleOutputPlug(handle, null, field.getName(), new Color(34,139,134), soClass, childSO));
/*            pin.addConnectionListener(new ConnectionListener() {
                public void handleConnectionEvent(ConnectionEvent e) {
                    SharedObject so= ((SharedObjectPlug) e.getOwnPlug()).getSharedObject();
                    //Make sure the connectee gets proper data upon connection
                    SharedObjectEvent soe = new SharedObjectEvent(handle, so);
                    so.fireSharedObjectChanged(soe);
                }
            });
*/
        /********* URL *********/
        } else if (obj.equals(Class.forName("java.net.URL"))) {
            soClass=Class.forName("gr.cti.eslate.sharedObject.UrlSO");
            SharedObject childSO=new UrlSO(handle);
            if (field.isEditable()) {
                SharedObjectListener sol=new SharedObjectListener() {
                    public void handleSharedObjectEvent(SharedObjectEvent e) {
                        if (index==-1) return;
                        try {
//                            Object o=tableSO.getTable().getCell(fieldIndex,index);
                            Object o=table.getCell(fieldIndex,index);
                            Object so=((UrlSO) e.getSharedObject()).getURL();
                            if ((o!=null && o.equals(so)) || (o==null && so==null));
                            else
                                table.setCell(fieldIndex,index,so);
//                                tableSO.getTable().setCell(fieldIndex,index,so);
                        } catch(Exception ae) {
                        }
                    }
                };
                recPlug.addPlug(pin=new SingleInputMultipleOutputPlug(handle, null, field.getName(), new Color(188,143,143), soClass, childSO,sol));
            } else
                recPlug.addPlug(pin=new MultipleOutputPlug(handle, null, field.getName(), new Color(188,143,143), soClass, childSO));
/*            pin.addConnectionListener(new ConnectionListener() {
                public void handleConnectionEvent(ConnectionEvent e) {
                    SharedObject so = ((SharedObjectPlug) e.getOwnPlug()).getSharedObject();
                    //Make sure the connectee gets proper data upon connection
                    SharedObjectEvent soe = new SharedObjectEvent(handle, so);
                    so.fireSharedObjectChanged(soe);
                }
            });
*/
        /********* Number *********/
        } else if (obj.equals(Class.forName("java.lang.Double"))) {
//long s = System.currentTimeMillis();
            soClass=Class.forName("gr.cti.eslate.sharedObject.NumberSO");
            SharedObject childSO=new NumberSO(handle,null);
            if (field.isEditable()) {
                SharedObjectListener sol=new SharedObjectListener() {
                    public void handleSharedObjectEvent(SharedObjectEvent e) {
                        if (index==-1) return;
                        try {
                            Object o=table.getCell(fieldIndex,index);
//                            Object o=tableSO.getTable().getCell(fieldIndex,index);
                            Number n=((NumberSO) e.getSharedObject()).value();
                            Object so;
                            if (n!=null)
                                so=new Double(n.doubleValue());
                            else
                                so=null;
                            if ((o!=null && o.equals(so)) || (o==null && so==null));
                            else
                                table.setCell(fieldIndex,index,so);
//                                tableSO.getTable().setCell(fieldIndex,index,so);
                        } catch(Exception ae) {
                        }
                    }
                };
                recPlug.addPlug(pin=new SingleInputMultipleOutputPlug(handle, null, field.getName(), new Color(135,206,250), soClass, childSO,sol));
            } else
                recPlug.addPlug(pin=new MultipleOutputPlug(handle, null, field.getName(), new Color(135,206,250), soClass, childSO));
/*            pin.addConnectionListener(new ConnectionListener() {
                public void handleConnectionEvent(ConnectionEvent e) {
                    SharedObject so = ((SharedObjectPlug) e.getOwnPlug()).getSharedObject();
                    //Make sure the connectee gets proper data upon connection
                    SharedObjectEvent soe = new SharedObjectEvent(handle, so);
                    so.fireSharedObjectChanged(soe);
                }
            });
*/
//System.out.println("Double: " + (System.currentTimeMillis()-s));
        /********* Boolean *********/
        } else if (obj.equals(Class.forName("java.lang.Boolean"))) {
            soClass=Class.forName("gr.cti.eslate.sharedObject.BooleanSO");
            SharedObject childSO=new BooleanSO(handle);
            if (field.isEditable()) {
                SharedObjectListener sol=new SharedObjectListener() {
                    public void handleSharedObjectEvent(SharedObjectEvent e) {
                        if (index==-1) return;
                        try {
//                            Object o=tableSO.getTable().getCell(fieldIndex,index);
                            Object o=table.getCell(fieldIndex,index);
                            Object so=((BooleanSO) e.getSharedObject()).getBoolean();
                            if ((o!=null && o.equals(so)) || (o==null && so==null));
                            else
                                table.setCell(fieldIndex,index,so);
//                                tableSO.getTable().setCell(fieldIndex,index,so);
                        } catch(Exception ae) {
                        }
                    }
                };
                recPlug.addPlug(pin=new SingleInputMultipleOutputPlug(handle, null, field.getName(), new Color(240,230,140), soClass, childSO,sol));
            } else
                recPlug.addPlug(pin=new MultipleOutputPlug(handle, null, field.getName(), new Color(240,230,140), soClass, childSO));
/*            pin.addConnectionListener(new ConnectionListener() {
                public void handleConnectionEvent(ConnectionEvent e) {
                    SharedObject so = ((SharedObjectPlug) e.getOwnPlug()).getSharedObject();
                    //Make sure the resultant vector gets proper data upon connection
                    SharedObjectEvent soe = new SharedObjectEvent(handle, so);
                    so.fireSharedObjectChanged(soe);
                }
            });
*/
        /********* ImageIcon *********/
        } else if (obj.equals(Class.forName("gr.cti.eslate.database.engine.CImageIcon"))) {
            soClass=Class.forName("gr.cti.eslate.sharedObject.IconSO");
            SharedObject childSO=new IconSO(handle);
            if (field.isEditable()) {
                SharedObjectListener sol=new SharedObjectListener() {
                    public void handleSharedObjectEvent(SharedObjectEvent e) {
                        if (index==-1) return;
                        try {
//                            Object o=tableSO.getTable().getCell(fieldIndex,index);
                            Object o=table.getCell(fieldIndex,index);
                            Object so=((IconSO) e.getSharedObject()).getIconSO();
                            if ((o!=null && o.equals(so)) || (o==null && so==null));
                            else {
                                CImageIcon ii=new CImageIcon();
                                Icon ic=(Icon) so;
                                if (so==null)
                                    table.setCell(fieldIndex,index,null);
//                                    tableSO.getTable().setCell(fieldIndex,index,null);
                                else {
                                    BufferedImage bi=new BufferedImage(ic.getIconWidth(),ic.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
                                    ic.paintIcon(new JComponent(){},bi.getGraphics(),0,0);
                                    ii.setImage(bi);
                                    table.setCell(fieldIndex,index,ii);
//                                    tableSO.getTable().setCell(fieldIndex,index,ii);
                                }
                            }
                        } catch(Exception ae) {
                        }
                    }
                };
                recPlug.addPlug(pin=new SingleInputMultipleOutputPlug(handle, null, field.getName(), new Color(50, 151, 220), soClass, childSO,sol));
            } else
                recPlug.addPlug(pin=new MultipleOutputPlug(handle, null, field.getName(), new Color(50, 151, 220), soClass, childSO));
/*            pin.addConnectionListener(new ConnectionListener() {
                public void handleConnectionEvent(ConnectionEvent e) {
                    SharedObject so= ((SharedObjectPlug) e.getOwnPlug()).getSharedObject();
                    //Make sure the resultant vector gets proper data upon connection
                    SharedObjectEvent soe = new SharedObjectEvent(handle, so);
                    so.fireSharedObjectChanged(soe);
                }
            });
*/
        }
        } catch(Exception ef) {
            ef.printStackTrace();
        }
    }

//    private TableSO tableSO;
    private Table table;
    private ESlateHandle handle;
    private SharedObjectPlug recPlug;
    /**
     * This vector is used to remove cycles.
     */
//    private Vector path;
    /**
     * The index defines which record is shown by the plug.
     */
    private int index;

    public static final Color TABLE_PLUG_COLOR = new Color(102, 88, 187);
//    private DatabaseListener addedListener;
    private DatabaseTableModelListener tableListener = new DatabaseTableModelAdapter() {
        public void cellValueChanged(CellValueChangedEvent e) {
//System.out.println("TablePlug cellValueChanged()");
//            if (table.getCDatabase().getCTableAt(e.getTableIndex())!=table)
                //Not for this table.
//                return;
            if (e.getRecordIndex()==index)
                //Update field plugs
                for (int i=0;i<recPlug.getChildPlugs().length;i++)
                    if (recPlug.getChildPlugs()[i].getName().equals(e.getColumnName()))
                        updateFieldPlugValue((SharedObjectPlug) recPlug.getChildPlugs()[i],index);
        }

        public void columnAdded(ColumnAddedEvent e) {
//System.out.println("TablePlug columnAdded()");
//            if (table.getCDatabase().getCTableAt(e.getTableIndex())!=table)
                //Not for this table.
//                return;
            Table table = (Table) e.getSource();
            RecordSO recSO = (RecordSO) recPlug.getSharedObject();
            //Set Field names on Record SO
            recSO.setFieldNames(table.getFieldNames());
            //Set Field types on Record SO
            ArrayList ft=new ArrayList();
            for (int z=0;z<table.getFieldCount();z++)
                try {
                    ft.add(table.getTableField(z).getDataType());
                } catch(InvalidFieldIndexException exs) {}
            recSO.setFieldTypes(ft);

            try {
                createFieldPlug(e.getColumnIndex());
            } catch(Exception ex) {
                System.err.println("TablePlug#200004041414: Cannot create plug for the new field.");
            }
        }

        public void columnEditableStateChanged(ColumnEditableStateChangedEvent e) {
//            if (table.getCDatabase().getCTableAt(e.getTableIndex())!=table)
                //Not for this table.
//                return;
            for (int i=0;i<recPlug.getChildPlugs().length;i++) {
                if (recPlug.getChildPlugs()[i].getName().equals(e.getColumnName())) {
                    try {
                        recPlug.removePlug(recPlug.getChildPlugs()[i]);
//                        createFieldPlug(tableSO.getTable().getFieldIndex(e.getColumnName()));
                        createFieldPlug(table.getFieldIndex(e.getColumnName()));
                    } catch(Exception ex) {
                        System.err.println("TablePlug#200004041404: Cannot change the field plug editable property.");
                    }
                    break;
                }
            }
        }

        public void columnRemoved(ColumnRemovedEvent e) {
//            if (table.getCDatabase().getCTableAt(e.getTableIndex())!=table)
                //Not for this table.
//                return;
            Table table = (Table) e.getSource();
            RecordSO recSO = (RecordSO) recPlug.getSharedObject();
            //Set Field names on Record SO
            recSO.setFieldNames(table.getFieldNames());
            //Set Field types on Record SO
            ArrayList ft=new ArrayList();
            for (int z=0;z<table.getFieldCount();z++)
                try {
                    ft.add(table.getTableField(z).getDataType());
                } catch(InvalidFieldIndexException exs) {}
            recSO.setFieldTypes(ft);

            for (int i=0;i<recPlug.getChildPlugs().length;i++) {
                if (recPlug.getChildPlugs()[i].getName().equals(e.getColumnName())) {
                    try {
                        recPlug.removePlug(recPlug.getChildPlugs()[i]);
                    } catch(Exception ex) {
                        System.err.println("TablePlug#200004041403: Cannot remove field plug.");
                    }
                    break;
                }
            }
        }

        public void columnRenamed(ColumnRenamedEvent e) {
//System.out.println("TablePlug columnRenamed()");
//            if (table.getCDatabase().getCTableAt(e.getTableIndex())!=table)
                //Not for this table.
//                return;
            Table table = (Table) e.getSource();
            RecordSO recSO = (RecordSO) recPlug.getSharedObject();
            //Set Field names on Record SO
            recSO.setFieldNames(table.getFieldNames());
            //Set Field types on Record SO
            ArrayList ft=new ArrayList();
            for (int z=0;z<table.getFieldCount();z++)
                try {
                    ft.add(table.getTableField(z).getDataType());
                } catch(InvalidFieldIndexException exs) {}
            recSO.setFieldTypes(ft);

            for (int i=0;i<recPlug.getChildPlugs().length;i++) {
                if (recPlug.getChildPlugs()[i].getName().equals(e.getOldName())) {
                    try {
                        recPlug.getChildPlugs()[i].setName(e.getNewName());
                        recPlug.getChildPlugs()[i].setNameLocaleIndependent(e.getNewName());
                    } catch(Exception ex) {
                        System.err.println("TablePlug#200004041402: Cannot rename field plug.");
                    }
                    break;
                }
            }
        }

        public void columnTypeChanged(ColumnTypeChangedEvent e) {
//            if (table.getCDatabase().getCTableAt(e.getTableIndex())!=table)
                //Not for this table.
//                return;
            Table table = (Table) e.getSource();
            RecordSO recSO = (RecordSO) recPlug.getSharedObject();
            //Set Field names on Record SO
            recSO.setFieldNames(table.getFieldNames());
            //Set Field types on Record SO
            ArrayList ft=new ArrayList();
            for (int z=0;z<table.getFieldCount();z++)
                try {
                    ft.add(table.getTableField(z).getDataType());
                } catch(InvalidFieldIndexException exs) {}
            recSO.setFieldTypes(ft);
            for (int i=0;i<recPlug.getChildPlugs().length;i++) {
                if (recPlug.getChildPlugs()[i].getName().equals(e.getColumnName())) {
                    try {
                        recPlug.removePlug(recPlug.getChildPlugs()[i]);
                        createFieldPlug(table.getFieldIndex(e.getColumnName()));
//                        createFieldPlug(tableSO.getTable().getFieldIndex(e.getColumnName()));
                    } catch(Exception ex) {
                        System.err.println("TablePlug#200004041358: Cannot change the type of the field plug.");
                    }
                    break;
                }
            }
        }

        public void tableRenamed(TableRenamedEvent e) {
//System.out.println("TablePlug tableRenamed()");
//            if (table.getCDatabase().getCTableAt(e.getIndex())!=table)
                //Not for this table.
//                return;
//System.out.println("TablePlug tableRenamed(): " + e.getNewTitle());
            try {
                setName(e.getNewTitle());
                setNameLocaleIndependent(e.getNewTitle());
                recPlug.setName(Table.bundle.getString("Record")+" "+Table.bundle.getString("of")+" \""+e.getNewTitle()+"\"");
                recPlug.setNameLocaleIndependent("record of "+e.getNewTitle());
            } catch(PlugExistsException ex) {
                System.err.println("TablePlug#200004041330: Cannot rename plugs after renaming the table.");
            }
        }

        public void activeRecordChanged(ActiveRecordChangedEvent e) {
//System.out.println("TablePlug activeRecordChanged()");
//            if (tp.getTable().getCDatabase().getCTableAt(e.getTableIndex())!=tp.getTable())
                //Not for this table.
//                return;
            updatePlugValues(e.getActiveRecord());
        }

/*        public void tableRemoved(TableRemovedEvent e) {
            if (e.getRemovedTable()!=table)
                //Not for this table.
                return;
            try {
                destroy();
                handle.removePin(TablePlug.this);
            } catch(NoSuchPinException ex) {}
        }
*/
    };
}
