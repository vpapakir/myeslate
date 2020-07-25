package gr.cti.eslate.agent;

import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.MultipleOutputPlug;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.SharedObjectPlug;
import gr.cti.eslate.base.SingleInputMultipleOutputPlug;
import gr.cti.eslate.base.sharedObject.SharedObject;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.BooleanTableField;
import gr.cti.eslate.database.engine.CImageIcon;
import gr.cti.eslate.database.engine.DateTableField;
import gr.cti.eslate.database.engine.ImageTableField;
import gr.cti.eslate.database.engine.StringTableField;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.database.engine.URLTableField;
import gr.cti.eslate.sharedObject.BooleanSO;
import gr.cti.eslate.sharedObject.DateSO;
import gr.cti.eslate.sharedObject.IconSO;
import gr.cti.eslate.sharedObject.NumberSO;
import gr.cti.eslate.sharedObject.RecordSO;
import gr.cti.eslate.sharedObject.StringSO;
import gr.cti.eslate.sharedObject.UrlSO;
import gr.cti.eslate.tableModel.event.CellValueChangedEvent;
import gr.cti.eslate.tableModel.event.ColumnAddedEvent;
import gr.cti.eslate.tableModel.event.ColumnEditableStateChangedEvent;
import gr.cti.eslate.tableModel.event.ColumnRemovedEvent;
import gr.cti.eslate.tableModel.event.ColumnRenamedEvent;
import gr.cti.eslate.tableModel.event.ColumnTypeChangedEvent;
import gr.cti.eslate.tableModel.event.DatabaseTableModelAdapter;
import gr.cti.eslate.tableModel.event.DatabaseTableModelListener;
import gr.cti.eslate.tableModel.event.TableRenamedEvent;
import gr.cti.eslate.utils.NewRestorableImageIcon;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.JComponent;

/**
 * The TableRecordPlug is a plug which handles automaticaly all the subplugs of a table (record and fields).
 * It is a database listener so it manipulates renamings, deletions, additions etc. It has an index
 * indicating which record is shown by the plugs.
 *
 * It is used to provide table data independently from the table plug, which shows only the active record.
 * <p>
 *
 * @author  Giorgos Vasiliou
 * @version 3.0, 23 Mar 2001
 */
public class TableRecordPlug extends MultipleOutputPlug {
	public TableRecordPlug(ESlateHandle handle,Table table) throws InvalidPlugParametersException {
		super(handle,null,"#__TMP__NAME__ABOUT__TO__CHANGE__#",new Color(255,228,181),gr.cti.eslate.sharedObject.RecordSO.class,new RecordSO(table.getESlateHandle()));
		this.table=table;
		String addOn="";
		String indipName="record of "+table.getTitle();
		int c=0;
		while (handle.getPlugLocaleIndependent(indipName+addOn)!=null)
			addOn=" ("+(++c)+")";
		indipName=indipName+addOn;
		try {
			setName(Agent.bundlePlugs.getString("recordof")+" \""+table.getTitle()+addOn+"\"");
			setNameLocaleIndependent(indipName);
		} catch(PlugExistsException ex) {
			//The previous loop ensures that this will not happen.
		}
		addConnectionListener(new ConnectionListener() {
			public void handleConnectionEvent(ConnectionEvent e) {
				SharedObjectEvent soe = new SharedObjectEvent(getSharedObject());
				((SharedObjectPlug)e.getPlug()).getSharedObjectListener().handleSharedObjectEvent(soe);
			}
		});
		//Set Column names on Record SO
		((RecordSO) getSharedObject()).setFieldNames(table.getFieldNames());

		//Create child pins
		for (int j=0;j<table.getColumnCount();j++) {
			createFieldPlug(j);
		}

		table.addTableModelListener(tmAdapter);
	}

	public void destroy() {
		table.removeTableModelListener(tmAdapter);
	}

	public Table getTable() {
		return table;
	}

	public void updatePlugValues(int idx) {
		index=idx;
		//Update record plug
		if (idx!=-1)
			try {
				((RecordSO) getSharedObject()).setRecordIndex(idx);
			} catch(Exception ex) {
			}
		else
			((RecordSO) getSharedObject()).setRecordIndex(-1);
		//Update field plugs
		for (int i=0;i<getChildPlugs().length;i++)
			updateFieldPlugValue(getChildPlugs()[i],idx);
	}

	private void updateFieldPlugValue(Plug pl,int idx) {
		//Set the values of the shared objects to the correct cell value or null if the active
		//record is -1.
		SharedObjectPlug fp=(SharedObjectPlug) pl;
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
			if (idx!=-1 && ic!=null) {
				BufferedImage bi=new BufferedImage(ic.getIconWidth(),ic.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
				ic.paintIcon(new JComponent() {},bi.getGraphics(),0,0);
				((IconSO) fp.getSharedObject()).setIconSO(new NewRestorableImageIcon(bi));
			} else
				((IconSO) fp.getSharedObject()).setIconSO(null);
		}
		} catch(Exception ex) {
			System.err.println("MAP#200004041434: Cannot update field plug value for "+fp.getName()+".");
		}
	}

	private void createFieldPlug(int fieldIdx) {
		try {
		final int fieldIndex=fieldIdx;
		AbstractTableField field=table.getTableField(fieldIndex);
		Class soClass; Plug pin;
		Class type=field.getDataType();
		/********* String *********/
		if (type==StringTableField.DATA_TYPE) {
			soClass=Class.forName("gr.cti.eslate.sharedObject.StringSO");
			SharedObject childSO=new StringSO(getHandle());
			if (field.isEditable()) {
				SharedObjectListener sol=new SharedObjectListener() {
					public void handleSharedObjectEvent(SharedObjectEvent e) {
						if (index==-1) return;
						try {
							Object so=((StringSO) e.getSharedObject()).getString();
							table.setCell(fieldIndex,index,so);
						} catch(Exception ex) {}
					}
				};
				addPlug(pin=new SingleInputMultipleOutputPlug(getHandle(),null,field.getName(),new Color(139,117,0),soClass,childSO,sol));
			} else
				addPlug(pin=new MultipleOutputPlug(getHandle(),null,field.getName(), new Color(139,117,0), soClass, childSO));
			pin.addConnectionListener(new ConnectionListener() {
				public void handleConnectionEvent(ConnectionEvent e) {
					SharedObject so=((SharedObjectPlug)e.getOwnPlug()).getSharedObject();
					//Make sure the connectee gets proper data upon connection
					SharedObjectEvent soe = new SharedObjectEvent(so);
					so.fireSharedObjectChanged(soe);
				}
			});
		/********* Date *********/
		} else if (type==DateTableField.DATA_TYPE) {
			soClass=Class.forName("gr.cti.eslate.sharedObject.DateSO");
			SharedObject childSO=new DateSO(getHandle());
			if (field.isEditable()) {
				SharedObjectListener sol=new SharedObjectListener() {
					public void handleSharedObjectEvent(SharedObjectEvent e) {
						if (index==-1) return;
						try {
							Object so=((DateSO) e.getSharedObject()).getDate();
							table.setCell(fieldIndex,index,so);
						} catch(Exception ex) {}
					}
				};
				addPlug(pin=new SingleInputMultipleOutputPlug(getHandle(),null,field.getName(),new Color(34,139,134),soClass,childSO,sol));
			} else
				addPlug(pin=new MultipleOutputPlug(getHandle(), null,field.getName(), new Color(34,139,134), soClass, childSO));
			pin.addConnectionListener(new ConnectionListener() {
				public void handleConnectionEvent(ConnectionEvent e) {
					SharedObject so=((SharedObjectPlug)e.getOwnPlug()).getSharedObject();
					//Make sure the connectee gets proper data upon connection
					SharedObjectEvent soe = new SharedObjectEvent(so);
					so.fireSharedObjectChanged(soe);
				}
			});
		/********* URL *********/
		} else if (type==URLTableField.DATA_TYPE) {
			soClass=Class.forName("gr.cti.eslate.sharedObject.UrlSO");
			SharedObject childSO=new UrlSO(getHandle());
			if (field.isEditable()) {
				SharedObjectListener sol=new SharedObjectListener() {
					public void handleSharedObjectEvent(SharedObjectEvent e) {
						if (index==-1) return;
						try {
							Object so=((UrlSO) e.getSharedObject()).getURL();
							table.setCell(fieldIndex,index,so);
						} catch(Exception ex) {}
					}
				};
				addPlug(pin=new SingleInputMultipleOutputPlug(getHandle(), null, field.getName(), new Color(188,143,143), soClass, childSO,sol));
			} else
				addPlug(pin=new MultipleOutputPlug(getHandle(), null, field.getName(), new Color(188,143,143), soClass, childSO));
			pin.addConnectionListener(new ConnectionListener() {
				public void handleConnectionEvent(ConnectionEvent e) {
					SharedObject so=((SharedObjectPlug)e.getOwnPlug()).getSharedObject();
					//Make sure the connectee gets proper data upon connection
					SharedObjectEvent soe = new SharedObjectEvent(so);
					so.fireSharedObjectChanged(soe);
				}
			});
		/********* Number *********/
		} else if (Number.class.isAssignableFrom(type)) {
			soClass=Class.forName("gr.cti.eslate.sharedObject.NumberSO");
			SharedObject childSO=new NumberSO(getHandle(),null);
			if (field.isEditable()) {
				SharedObjectListener sol=new SharedObjectListener() {
					public void handleSharedObjectEvent(SharedObjectEvent e) {
						if (index==-1) return;
						try {
							Number n=((NumberSO) e.getSharedObject()).value();
							Object so;
							if (n!=null)
								so=new Double(n.doubleValue());
							else
								so=null;
							table.setCell(fieldIndex,index,so);
						} catch(Exception ex) {}
					}
				};
				addPlug(pin=new SingleInputMultipleOutputPlug(getHandle(), null, field.getName(), new Color(135,206,250), soClass, childSO,sol));
			} else
				addPlug(pin=new MultipleOutputPlug(getHandle(), null, field.getName(), new Color(135,206,250), soClass, childSO));
			pin.addConnectionListener(new ConnectionListener() {
				public void handleConnectionEvent(ConnectionEvent e) {
					SharedObject so=((SharedObjectPlug)e.getOwnPlug()).getSharedObject();
					//Make sure the connectee gets proper data upon connection
					SharedObjectEvent soe = new SharedObjectEvent(so);
					so.fireSharedObjectChanged(soe);
				}
			});
		/********* Boolean *********/
		} else if (type==BooleanTableField.DATA_TYPE) {
			soClass=Class.forName("gr.cti.eslate.sharedObject.BooleanSO");
			SharedObject childSO=new BooleanSO(getHandle());
			if (field.isEditable()) {
				SharedObjectListener sol=new SharedObjectListener() {
					public void handleSharedObjectEvent(SharedObjectEvent e) {
						if (index==-1) return;
						try {
							Object so=((BooleanSO) e.getSharedObject()).getBoolean();
							table.setCell(fieldIndex,index,so);
						} catch(Exception ex) {}
					}
				};
				addPlug(pin=new SingleInputMultipleOutputPlug(getHandle(), null, field.getName(), new Color(240,230,140), soClass, childSO,sol));
			} else
				addPlug(pin=new MultipleOutputPlug(getHandle(), null, field.getName(), new Color(240,230,140), soClass, childSO));
			pin.addConnectionListener(new ConnectionListener() {
				public void handleConnectionEvent(ConnectionEvent e) {
					SharedObject so=((SharedObjectPlug)e.getOwnPlug()).getSharedObject();
					//Make sure the resultant vector gets proper data upon connection
					SharedObjectEvent soe = new SharedObjectEvent(so);
					so.fireSharedObjectChanged(soe);
				}
			});
		/********* ImageIcon *********/
		} else if (type==ImageTableField.DATA_TYPE) {
			soClass=Class.forName("gr.cti.eslate.sharedObject.IconSO");
			SharedObject childSO=new IconSO(getHandle());
			if (field.isEditable()) {
				SharedObjectListener sol=new SharedObjectListener() {
					public void handleSharedObjectEvent(SharedObjectEvent e) {
						if (index==-1) return;
						try {
							Object so=((IconSO) e.getSharedObject()).getIconSO();
							CImageIcon ii=new CImageIcon();
							Icon ic=(Icon) so;
							if (so==null)
								table.setCell(fieldIndex,index,null);
							else {
								BufferedImage bi=new BufferedImage(ic.getIconWidth(),ic.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
								ic.paintIcon(new JComponent(){},bi.getGraphics(),0,0);
								ii.setImage(bi);
								table.setCell(fieldIndex,index,ii);
							}
						} catch(Exception ex) {}
					}
				};
				addPlug(pin=new SingleInputMultipleOutputPlug(getHandle(), null, field.getName(), new Color(50, 151, 220), soClass, childSO,sol));
			} else
				addPlug(pin=new MultipleOutputPlug(getHandle(), null, field.getName(), new Color(50, 151, 220), soClass, childSO));
			pin.addConnectionListener(new ConnectionListener() {
				public void handleConnectionEvent(ConnectionEvent e) {
					SharedObject so=((SharedObjectPlug)e.getOwnPlug()).getSharedObject();
					//Make sure the resultant vector gets proper data upon connection
					SharedObjectEvent soe = new SharedObjectEvent(so);
					so.fireSharedObjectChanged(soe);
				}
			});
		}
		} catch(Exception ef) {}
	}

	private Table table;
	/**
	 * The index defines which record is shown by the plug.
	 */
	private int index;
	private DatabaseTableModelListener addedListener;
	private DatabaseTableModelListener tmAdapter=new DatabaseTableModelAdapter() {
		public void cellValueChanged(CellValueChangedEvent e) {
			if (e.getRecordIndex()==index)
				//Update field plugs
				for (int i=0;i<getChildPlugs().length;i++)
					if (getChildPlugs()[i].getName().equals(e.getColumnName()))
						updateFieldPlugValue(getChildPlugs()[i],index);
		}

		public void columnAdded(ColumnAddedEvent e) {
			try {
				createFieldPlug(e.getColumnIndex());
			} catch(Exception ex) {
				System.err.println("MAP#200004041414: Cannot create plug for the new field.");
			}
		}

		public void columnEditableStateChanged(ColumnEditableStateChangedEvent e) {
			for (int i=0;i<getChildPlugs().length;i++) {
				if (getChildPlugs()[i].getName().equals(e.getColumnName())) {
					try {
						removePlug(getChildPlugs()[i]);
						createFieldPlug(table.getColumnIndex(e.getColumnName()));
					} catch(Exception ex) {
						System.err.println("MAP#200004041404: Cannot change the field plug editable property.");
					}
					break;
				}
			}
		}

		public void columnRemoved(ColumnRemovedEvent e) {
			for (int i=0;i<getChildPlugs().length;i++) {
				if (getChildPlugs()[i].getName().equals(e.getColumnName())) {
					try {
						removePlug(getChildPlugs()[i]);
					} catch(Exception ex) {
						System.err.println("MAP#200004041403: Cannot remove field plug.");
					}
					break;
				}
			}
		}

		public void columnRenamed(ColumnRenamedEvent e) {
			for (int i=0;i<getChildPlugs().length;i++) {
				if (getChildPlugs()[i].getName().equals(e.getOldName())) {
					try {
						getChildPlugs()[i].setName(e.getNewName());
						getChildPlugs()[i].setNameLocaleIndependent(e.getNewName());
					} catch(Exception ex) {
						System.err.println("MAP#200004041402: Cannot rename field plug.");
					}
					break;
				}
			}
		}

		public void columnTypeChanged(ColumnTypeChangedEvent e) {
			for (int i=0;i<getChildPlugs().length;i++) {
				if (getChildPlugs()[i].getName().equals(e.getColumnName())) {
					try {
						removePlug(getChildPlugs()[i]);
						createFieldPlug(table.getColumnIndex(e.getColumnName()));
					} catch(Exception ex) {
						System.err.println("MAP#200004041358: Cannot change the type of the field plug.");
					}
					break;
				}
			}
		}

		public void tableRenamed(TableRenamedEvent e) {
			String addOn="";
			String indipName="record of "+table.getTitle();
			int c=0;
			while (getHandle().getPlugLocaleIndependent(indipName+addOn)!=null)
				addOn=" ("+(++c)+")";
			indipName=indipName+addOn;
			try {
				setName(Agent.bundlePlugs.getString("recordof")+" \""+table.getTitle()+addOn+"\"");
				setNameLocaleIndependent(indipName);
			} catch(PlugExistsException ex) {
				//The previous loop ensures that this will not happen.
			}
		}
	};
}
