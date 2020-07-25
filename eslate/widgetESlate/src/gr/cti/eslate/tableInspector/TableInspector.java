package gr.cti.eslate.tableInspector;

import gr.cti.eslate.base.BadPlugAliasException;
import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.DisconnectionEvent;
import gr.cti.eslate.base.DisconnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateAdapter;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlateMicroworld;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.FemaleSingleIFMultipleConnectionProtocolPlug;
import gr.cti.eslate.base.HandleDisposalEvent;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.MultipleOutputPlug;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.InvalidFieldIndexException;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.sharedObject.UrlSO;
import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.NoBorderButton;
import gr.cti.eslate.utils.NoBorderToggleButton;
import gr.cti.eslate.utils.NoTopOneLineBevelBorder;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

public class TableInspector extends JPanel implements Externalizable, ESlatePart {
    private static final String version="2";
    private Toolbar bottom;
    JPanel tools;
    JTabbedPane middle;
    JLabel noOf,drive;
    NoBorderButton next,previous;
    JButton query,clear;
    JButtonWithPopup show;
    JToggleButton showHide;
    private Locale locale;
    Cursor WAIT_CURSOR, DEFAULT_CURSOR;
    private Plug urlPlug;
    private FemaleSingleIFMultipleConnectionProtocolPlug pin;
    static ResourceBundle messagesBundle, tooltipBundle, infoBundle, pinsBundle;
    private ArrayList<String> visitedPages=new ArrayList<String>(10);
    protected static java.applet.AppletContext appletContext;
    private ImageIcon haze;
    protected boolean hasBeenDestroyed=false;
    private ESlateHandle handle;
    private UrlSO urlSO;
    private BrowserFrame browserFrame;

    private boolean navigateVisible=true,selectViewVisible=true,hasShowHide=false,sadefault=true,toolbarVisible=true,messagebarVisible=true;
    private boolean borderChanged=false;

    private Icon background;
    private String backImageFile;
    private int tabPos=SwingConstants.TOP;
    private int align;
    private int percentField;
    private MySecurityManager securityManager;
    private boolean isTabVisible=true;
    private boolean queryVisible=true;
    private boolean rowBorderPainted=true;
    private boolean followActiveRecord=true;

    public static int QUERY_TICK=0;
    public static int QUERY_COMBO=1;

    private int queryType=QUERY_TICK;

    Color selectedBackground=new java.awt.Color(0,48,96);
    Color selectedForeground=Color.white;
    Color unselectedBackground=new java.awt.Color(224,240,224);
    Color unselectedForeground=Color.black;

    TableInspectorEventMulticaster listeners=new TableInspectorEventMulticaster();


    public TableInspector() {
        super();

        setBorder(new NoTopOneLineBevelBorder(BevelBorder.RAISED));
        borderChanged=false;
        handle=ESlate.registerPart(this);
        handle.addESlateListener(new ESlateAdapter() {
            public void handleDisposed(HandleDisposalEvent e) {
                destroy();
            }
        });
        handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.TableInspectorPrimitives");

        WAIT_CURSOR=new Cursor(Cursor.WAIT_CURSOR);
        DEFAULT_CURSOR=new Cursor(Cursor.DEFAULT_CURSOR);
        RowElement.icon=new ImageIcon[2];
        RowElement.icon[0]=loadImageIcon("notquery.gif");
        RowElement.icon[1]=loadImageIcon("query.gif");

        percentField=30;

        //Create pin
        try {
            pinsBundle=ResourceBundle.getBundle("gr.cti.eslate.tableInspector.PinsBundle",Locale.getDefault());
            //Table pin
            pin=new FemaleSingleIFMultipleConnectionProtocolPlug(handle, null, pinsBundle.getString("table"), new Color(102,88,187), Table.class);
            pin.setNameLocaleIndependent("table");
            pin.setHostingPlug(true);
            handle.addPlug(pin);
            pin.addConnectionListener(new ConnectionListener() {
                public void handleConnectionEvent(ConnectionEvent soe) {
                    Table t=(Table) soe.getPlug().getHandle().getComponent();

                    //Check if this table has already been connected by another plug.
                    //If so, connect but don't add a tab and ###EXIT###!!!
                    for (int i=0;i<middle.getTabCount();i++)
                        if (((TIPanel) middle.getComponentAt(i)).table==t)
                            return; //###EXIT from here!

                    //Set plug aliases
                    ESlateMicroworld mwd=handle.getESlateMicroworld();
                    AbstractTableField tf;
                    TIPanel tip=new TIPanel(t,TableInspector.this);
                    try {
                        //Aliasing for table current record
                        mwd.setPlugAliasForLoading(t.getTablePlug().getRecordPlug(),handle,new String[]{"record of \""+t.getTitle()+"\""});
                        //Aliasing for table fields
                        for (int j=0;j<t.getColumnCount();j++) {
                            try {
                                tf=t.getTableField(j);
                                mwd.setPlugAliasForLoading(t.getTablePlug().getRecordPlug().getPlug(tf.getName()),handle,new String[]{tf.getName(),"record of \""+t.getTitle()+"\""});
                            } catch(InvalidFieldIndexException ex) {
                                System.err.println("TABLEINSPECTOR#200101122103: Field does not exist.");
                            }
                        }
                        //Aliasing for current record ID output plug
                        mwd.setPlugAliasForLoading(tip.synchPlug,handle,new String[]{"current of \""+t.getTitle()+"\""});
                        //Aliasing for current record ID input plug
                        mwd.setPlugAliasForLoading(tip.synchPlug,handle,new String[]{"inputid of \""+t.getTitle()+"\""});
                    } catch(BadPlugAliasException ex) {}


                    tip.setFont(getFont());
                    middle.addTab(t.getTitle(),tip);
                    setSelectedTabIndex(middle.getTabCount()-1);
                    middle.setBackground(getBackground());
                    middle.setOpaque(false);
                    middle.repaint();
                }
            });
            pin.addDisconnectionListener(new DisconnectionListener() {
                public void handleDisconnectionEvent(DisconnectionEvent e) {
                    if ((hasBeenDestroyed) || (middle==null) || (tools==null)) return;
                    Table ct=(Table) e.getPlug().getHandle().getComponent();

                    //Check if there is another plug with this table connected.
                    //If so, don't remove the tab. It will be removed when all plugs
                    //with this table disconnect.
                    Plug[] prv=pin.getProviders();
                    for (int i=0;i<prv.length;i++)
                        if (prv[i].getHandle().getComponent()==ct)
                            //### The table exists! EXIT!!!
                            return;

                    for (int i=0;i<middle.getTabCount();i++)
                        if (((TIPanel) middle.getComponentAt(i)).getTable().equals(ct)) {
                            ((TIPanel) middle.getComponentAt(i)).die();
                            middle.removeTabAt(i);
                            middle.validate();
                            middle.repaint();
                            if ((middle.getTabCount()==0) && (hasShowHide)) {
                                tools.remove(showHide);
                                tools.validate();
                            }
                            break;
                        }

                    if (middle.getTabCount()==0) {
                        middle.setBackground((Color) UIManager.getColor("controlShadow"));
                        middle.setOpaque(true);
                        previous.setEnabled(false);
                        next.setEnabled(false);
                        show.setEnabled(false);
                        query.setEnabled(false);
                        //clear.setEnabled(false);
                        noOf.setText("");
                    }
                }
            });

            //URL click pin
            Class soClass=Class.forName("gr.cti.eslate.sharedObject.UrlSO");
            urlSO=new UrlSO(handle);
            urlPlug=new MultipleOutputPlug(handle, null, pinsBundle.getString("url"), new Color(188,143,143), soClass, urlSO);
            urlPlug.setNameLocaleIndependent("url");
            handle.addPlug(urlPlug);
            urlPlug.addConnectionListener(new ConnectionListener() {
                @SuppressWarnings("deprecation")
				public void handleConnectionEvent(ConnectionEvent ce) {
                    SharedObjectEvent soe = new SharedObjectEvent(this, urlSO);
                    urlSO.fireSharedObjectChanged(soe);
                }
            });

        } catch (InvalidPlugParametersException e) {
            System.out.println("Invalid Plug Parameters.");
        } catch (PlugExistsException e) {
            System.out.println("Plug Exists.");
        } catch (ClassNotFoundException e) {
            //Possibly couldn't find the interface/SO. No Plug.
        } catch (NoClassDefFoundError e) {
            //Possibly couldn't find the interface/SO. No Plug.
        }


        //Localization
        locale=Locale.getDefault();
        messagesBundle=ResourceBundle.getBundle("gr.cti.eslate.tableInspector.MessagesBundle",locale);
        tooltipBundle=ResourceBundle.getBundle("gr.cti.eslate.tableInspector.TooltipBundle",locale);
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.tableInspector.InfoBundle",locale);
        try {
            handle.setUniqueComponentName(messagesBundle.getString("componame"));
        } catch(RenamingForbiddenException e) {}
        setInfo();

        //Define help file
        //if (Locale.getDefault().toString().equals("el_GR"))
        //    handle.setHelp(infoBundle.getString("compo"),"help/main_el_GR.html",640,480);
        //else
        //    handle.setHelp(infoBundle.getString("compo"),"help/main.html",640,480);

        ///LAYOUT definition

        setPreferredSize(new Dimension(360,280));


        //BOTTOM PANEL
        bottom=new Toolbar();
        bottom.setBorder(BorderFactory.createEtchedBorder());
        bottom.setOpaque(false);
        bottom.setPreferredSize(new Dimension(360,32));

        //This buttons are not currently used.
        query=new JButton(loadImageIcon("tick.gif")) {
            //Trick to make this button invisible without removing the logic. We may want it again some time.
            public boolean isVisible() {
                return false;
            }
        };
        query.setBorder(BorderFactory.createEtchedBorder());
        query.setFocusPainted(false);
        query.setRequestFocusEnabled(false);
        query.setAlignmentY(CENTER_ALIGNMENT);
        Dimension dim=new Dimension(24,22);
        query.setPreferredSize(dim);
        query.setMaximumSize(dim);
        query.setMinimumSize(dim);
        query.setToolTipText(tooltipBundle.getString("submitquery"));
        query.setEnabled(false);
        query.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getSelectedTIPanel().query(0);
            }
        });


        clear=new JButton(loadImageIcon("x.gif"));
        clear.setBorder(BorderFactory.createEtchedBorder());
        clear.setFocusPainted(false);
        clear.setRequestFocusEnabled(false);
        clear.setAlignmentY(CENTER_ALIGNMENT);
        dim=new Dimension(24,22);
        clear.setPreferredSize(dim);
        clear.setMaximumSize(dim);
        clear.setMinimumSize(dim);
        //clear.setEnabled(false);
        clear.setVisible(false); //As of 20001129.
        clear.setToolTipText(tooltipBundle.getString("clear"));
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getSelectedTIPanel().clear();
                query.setEnabled(false);
                //clear.setEnabled(false);
            }
        });


        //MIDDLE PANEL
        middle=new JTabbedPane() {
            /**
             * Known method.
             */
            public void setFont(Font f) {
                super.setFont(f);
                for (int i=0;i<getTabCount();i++)
                    middle.getComponentAt(i).setFont(f);
            }
            /**
             * Known method.
             */
            public void setBackground(Color c) {
                super.setBackground(c);
                for (int i=0;i<getTabCount();i++)
                    middle.getComponentAt(i).setBackground(c);
            }
            /**
             * Known as well!
             */
            public void updateUI() {
                super.updateUI();
                if (this.getTabCount()==0)
                    this.setBackground((Color) UIManager.getColor("controlShadow"));
            }
        };

        middle.setOpaque(true);
        middle.setBackground((Color) UIManager.getColor("controlShadow"));

        middle.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (hasBeenDestroyed) return;
                if ((TIPanel) middle.getSelectedComponent()!=null)
                    ((TIPanel) middle.getSelectedComponent()).informTI();
                listeners.activeTabChanged(new TableInspectorEvent(this,TableInspectorEvent.RB_ACTIVE_TAB_CHANGED));
            }
        });

        setLayout(new BorderLayout());

        add(middle,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
        //Ensure that the panels will have the correct colors
        setBackground(getBackground());
        middle.setBackground((Color) UIManager.getColor("controlShadow"));
    }

    public void addTableInspectorListener(TableInspectorListener l) {
        listeners.add(l);
    }

    public void removeTableInspectorListener(TableInspectorListener l) {
        listeners.remove(l);
    }

    protected int getTableIndex(Table table) {
        for (int i=0;i<middle.getTabCount();i++)
            if (((TIPanel) middle.getComponentAt(i)).getTable().equals(table))
                return i;
        return -1;
    }

    protected Table[] getTables() {
        if (middle == null || middle.getTabCount() == 0)
            return null;
        Table[] tables = new Table[middle.getTabCount()];
        for (int i=0;i<middle.getTabCount();i++)
            tables[i] = ((TIPanel) middle.getComponentAt(i)).getTable();
        return tables;
    }

    //To be removed with the dependency to ESlateContainer
    public boolean isDesktopComponent(ESlateHandle handle) {
        return ((gr.cti.eslate.base.container.ESlateContainer) handle.getESlateMicroworld().getMicroworldEnvironment()).componentOnDesktop(handle);
    }

    protected void setHaze(boolean value) {
        if (drive==null) return;
        drive.setVisible(value);
    }

    protected boolean hasVisited(URL u2c) {
        if (u2c==null) return false;
        for (int i=0;i<visitedPages.size();i++) {
            if (visitedPages.get(i).equalsIgnoreCase(u2c.toString()))
                return true;
        }
        return false;
    }

    protected void addPage(URL a) {
        visitedPages.add(a.toString());
    }

    protected void showStateChanged() {
        if (middle.getSelectedComponent()!=null && show.isShowAllSelected()!=((TIPanel) middle.getSelectedComponent()).showAll)
            ((TIPanel) middle.getSelectedComponent()).selectShow(show.isShowAllSelected());
    }

    void enableSecurityManager() {
        if (securityManager == null)
            securityManager = new MySecurityManager();
        securityManager.enable();
        System.setSecurityManager(securityManager);
    }

    void disableSecurityManager() {
        if (securityManager == null)
            return;
        securityManager.disable();
        System.setSecurityManager(null);
    }

    protected void showURL(URL url) {
        if (urlPlug!=null)
            if (urlPlug.getDependents().length!=0)
                urlSO.setURL(url);
            else {
                //REMOVED 20000606 if (gr.cti.eslate.base.container.ESlateContainerApplet.getContext()==null)
                    try {
                        String cmd=ESlate.getNativeProgram("navigate.exe")+" "+messagesBundle.getString("book")+" \""+url+"\" "+((Toolkit.getDefaultToolkit().getScreenSize().width-650)/2)+" "+((Toolkit.getDefaultToolkit().getScreenSize().height-500)/2)+" 650 500";
                        Runtime.getRuntime().exec(cmd);
                    } catch(Exception e) {
                        try {
                            if (browserFrame==null) {
                                enableSecurityManager();
                                browserFrame=new DefaultBrowserFrame(messagesBundle.getString("book"));
                                disableSecurityManager();
                                browserFrame.setSize(580,400);
                            }
                            browserFrame.show();
                            browserFrame.setURL(url);
                        } catch(Exception e2) {
                            Toolkit.getDefaultToolkit().beep();
                        } catch(NoClassDefFoundError e2) {
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }
                    /* REMOVED20000222:try {
                        if (browserFrame==null) {
                            browserFrame=new ICEBrowserFrame(messagesBundle.getString("book"));
                            browserFrame.setSize(580,400);
                        }
                        browserFrame.setURL(url);
                        browserFrame.show();
                    } catch(NoClassDefFoundError e1) {
                        try {
                            if (browserFrame==null) {
                                browserFrame=new IEBrowserFrame(messagesBundle.getString("book"));
                                browserFrame.setSize(580,400);
                            }
                            browserFrame.setURL(url);
                            browserFrame.show();
                        } catch(NoClassDefFoundError e2) {

                        }
                    }*/
                /* REMOVED 20000606 else {
                   gr.cti.eslate.base.container.ESlateContainerApplet.getContext().showDocument(url,"_blank");
                }*/
            }
    }

    @SuppressWarnings("unused")
	private String findPathToNavigate()
    {
        {
            String jarPath = findPathToJar().trim();
            String fileSeparator = System.getProperty("file.separator");
            if(jarPath.endsWith(fileSeparator))
                jarPath = jarPath.substring(0, jarPath.length() - fileSeparator.length());
            File jarDir = new File(jarPath);
            if(!jarDir.exists() || !jarDir.isDirectory())
                return null;
            File navigateDir = null;
            try
            {
                navigateDir = new File(jarDir.getParent());
                if(navigateDir == null)
                {
                    String s = null;
                    return s;
                }
                navigateDir = new File(navigateDir.getParent());
                if(navigateDir == null)
                {
                    String s1 = null;
                    return s1;
                }
            }
            catch(Exception exc)
            {
                String s3 = null;
                return s3;
            }
            navigateDir = new File(navigateDir, "bin");
            if(!navigateDir.exists() || !navigateDir.isDirectory())
                return null;
            String s4;
            try
            {
                String s2 = navigateDir.getCanonicalPath();
                return s2;
            }
            catch(IOException exc)
            {
                s4 = null;
            }
            return s4;
        }
    }

    private String findPathToJar()
    {
        String classpath = System.getProperty("java.class.path");
        char pathSeparator = System.getProperty("path.separator").charAt(0);
        String jarName = "ESlateTableInspector";
        int lastIndex = classpath.toLowerCase().indexOf(jarName.toLowerCase());
        if(lastIndex != -1)
        {
            int firstIndex;
            for(firstIndex = lastIndex; firstIndex != -1 && classpath.charAt(firstIndex) != pathSeparator; firstIndex--);
            return classpath.substring(firstIndex + 1, lastIndex);
        } else {
            return null;
        }
    }

    private ImageIcon loadImageIcon(String filename) {
        return new ImageIcon(TableInspector.class.getResource("images/"+filename));
    }

    protected TIPanel getSelectedTIPanel() {
        return ((TIPanel) middle.getSelectedComponent());
    }

    private void setInfo() {
        String[] info={infoBundle.getString("part"),infoBundle.getString("development"),infoBundle.getString("contribution"),infoBundle.getString("copyright")};
        handle.setInfo(new ESlateInfo(infoBundle.getString("compo")+" "+version,info));
    }

    public ESlateHandle getESlateHandle() {
        return handle;
    }
    /**
     * Known method.
     */
    public void setBorder(Border b) {
        borderChanged=true;
        super.setBorder(b);
    }
    /**
     * Known method.
     */
    public void setFont(Font f) {
        super.setFont(f);
        for (int i=0;i<getComponentCount();i++)
            ((Component) getComponents()[i]).setFont(f);
    }

    @SuppressWarnings("deprecation")
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
        Object compatCheck=in.readObject();
        if (compatCheck instanceof StorageStructure) {
            StorageStructure fm=(StorageStructure) compatCheck;
            setToolSelectViewVisible(fm.get("selectViewVisible",selectViewVisible));
            setToolShowRecordVisible(fm.get("hasShowHide",hasShowHide));
            setToolSelectViewDefaultShowAll(fm.get("sadefault",sadefault));
            setToolbarVisible(fm.get("toolbarVisible",toolbarVisible));
            setMessagebarVisible(fm.get("messagebarVisible",messagebarVisible));
            setToolNavigateVisible(fm.get("navigateVisible",navigateVisible));
            backImageFile=fm.get("backImageFile",backImageFile);
            background=fm.get("background",(Icon) null);
            if (background!=null)
                setBackgroundImage(background);
            setTabPlacement(fm.get("tabPos",tabPos));
            align=fm.get("align",align);
            setOpaque(fm.get("opaque",isOpaque()));
            setVisible(fm.get("visible",isVisible()));
            setPercentField(fm.get("percentField",percentField));
            setToolQueryVisible(fm.get("queryVisible",true));
            setWidgetsOpaque(fm.get("widgetsOpaque",false));
            if (fm.containsKey("backgroundColor"))
                setBackground((Color) fm.get("backgroundColor"));
            if (fm.containsKey("border")){
                try {
                    BorderDescriptor bd=(BorderDescriptor) fm.get("border");
                    setBorder(bd.getBorder());
                } catch (Throwable thr) {/* No Border*/}
            }
            if (fm.containsKey("font"))
                setFont((Font) fm.get("font"));

            //Restore children tables
            handle.restoreChildren(fm, "childrenTables");

            final int activeTab=fm.get("activetab",-1);
            //This existed in a previous version
            if (fm.containsKey("showAllStatus")) {
                final boolean[] sas=(boolean[]) fm.get("showAllStatus");
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        for (int i=0;i<middle.getComponents().length;i++)
                            ((TIPanel) middle.getComponentAt(i)).selectShow(sas[i]);
                        if (activeTab!=-1)
                            setSelectedTabIndex(activeTab);
                    }
                });
            } else {
            	if (fm.containsKey("panels")) {
            		final TIPanel[] pnls=(TIPanel[]) fm.get("panels");
            		if (pnls!=null) {
            			SwingUtilities.invokeLater(new Runnable() {
            				public void run() {
            					for (int i=0;i<middle.getComponents().length;i++) {
            						TIPanel p=(TIPanel) middle.getComponentAt(i);
            						p.onLoadSelectShow(pnls[i].showAll);
            						if (pnls[i].curObj!=-1)
            							p.setCurObj(pnls[i].curObj,false);
            						
            						Object[][] locks=pnls[i].locks;
            						if (locks!=null)
            							for (int j=0;j<locks.length;j++)
            								p.lock(((Integer) locks[j][0]).intValue(),locks[j][1].toString());
            						boolean[] qtv=pnls[i].queryToolVisibility;
            						if (qtv!=null)
            							p.setFieldQueryToolVisibility(qtv);
            					}
            					if (activeTab!=-1)
            						setSelectedTabIndex(activeTab);
            					TIPanel t=(TIPanel) middle.getSelectedComponent();
            					if (t!=null)
            						t.informTI();
            				}
            			});
            		}
            	} else {
            		int count=fm.get("noOfPanels",0);
            		final StorageStructure[] states=new StorageStructure[count];
            		for (int i=0;i<count;i++)
            			states[i]=(StorageStructure) fm.get("panel"+i);
        			SwingUtilities.invokeLater(new Runnable() {
        				public void run() {
        					for (int i=0;i<middle.getComponents().length;i++) {
        						TIPanel p=(TIPanel) middle.getComponentAt(i);
        						p.onLoadSelectShow(states[i].get("showall",true));
        						int curObj=states[i].get("curobj",-1);
        						if (curObj!=-1)
        							p.setCurObj(curObj,false);
        						
        				    	int count=states[i].get("locks",0);
        				    	if (count>0) {
        				    		Object[][] locks=new Object[count][2];
        				    		int lc=1;
        				    		while (lc<=count) {
        				    			locks[lc-1][0]=new Integer(states[i].get("lock"+lc,0));
        				    			locks[lc-1][1]=states[i].get("lock"+lc+"value");
        				    			lc++;
        				    		}
        				    		for (int j=0;j<locks.length;j++)
        				    			p.lock(((Integer) locks[j][0]).intValue(),locks[j][1].toString());
        				    	}
        						boolean[] qtv=(boolean[]) states[i].get("queryvisibility");
        						if (qtv!=null)
        							p.setFieldQueryToolVisibility(qtv);
        						p.informTI();
        					}
        				}
        			});
            	}
            }
            setTabVisible(fm.get("tabVisible",isTabVisible));
            setQueryType(fm.get("queryType",queryType));
            unselectedBackground=fm.get("fieldback",unselectedBackground);
            unselectedForeground=fm.get("fieldfore",unselectedForeground);
            selectedBackground=fm.get("fieldselback",selectedBackground);
            selectedForeground=fm.get("fieldselfore",selectedForeground);
            rowBorderPainted=fm.get("rowBorderPainted",rowBorderPainted);
            followActiveRecord=fm.get("followActiveRecord",followActiveRecord);
        } else { //Old version
            Vector r=(Vector) compatCheck;
            selectViewVisible=((Boolean) r.elementAt(0)).booleanValue();
            setToolSelectViewVisible(selectViewVisible);
            hasShowHide=((Boolean) r.elementAt(1)).booleanValue();
            setToolShowRecordVisible(hasShowHide);
            sadefault=((Boolean) r.elementAt(2)).booleanValue();
            setToolSelectViewDefaultShowAll(sadefault);
            toolbarVisible=((Boolean) r.elementAt(3)).booleanValue();
            setToolbarVisible(toolbarVisible);
            messagebarVisible=((Boolean) r.elementAt(4)).booleanValue();
            setMessagebarVisible(messagebarVisible);
            navigateVisible=((Boolean) r.elementAt(5)).booleanValue();
            setToolNavigateVisible(navigateVisible);
            backImageFile=(String) r.elementAt(6);
            background=(Icon) r.elementAt(7);
            tabPos=((Integer) r.elementAt(8)).intValue();
            align=((Integer) r.elementAt(9)).intValue();
            setOpaque(((Boolean) r.elementAt(10)).booleanValue());
            try {
                setVisible(((Boolean) r.elementAt(11)).booleanValue());
            } catch(Exception ex) {
            }
        }
    }

    @SuppressWarnings("deprecation")
	public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fm=new ESlateFieldMap2();
        fm.put("selectViewVisible",selectViewVisible);
        fm.put("hasShowHide",hasShowHide);
        fm.put("sadefault",sadefault);
        fm.put("toolbarVisible",toolbarVisible);
        fm.put("messagebarVisible",messagebarVisible);
        fm.put("navigateVisible",navigateVisible);
        fm.put("backImageFile",backImageFile);
        if (background!=null) {
            if (background instanceof NewRestorableImageIcon)
                fm.put("background",background);
            else {
                BufferedImage img=new BufferedImage(background.getIconWidth(),background.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
                background.paintIcon(this,img.getGraphics(),0,0);
                fm.put("background",new NewRestorableImageIcon(img));
            }
        }
        fm.put("tabPos",tabPos);
        fm.put("align",align);
        fm.put("opaque",isOpaque());
        fm.put("visible",isVisible());
        fm.put("percentField",percentField);
        fm.put("queryVisible",isToolQueryVisible());
        fm.put("widgetsOpaque",isWidgetsOpaque());
        if (!(getBackground() instanceof javax.swing.plaf.ColorUIResource))
            fm.put("backgroundColor",getBackground());
        if (borderChanged) {
            try {
                BorderDescriptor bd=ESlateUtils.getBorderDescriptor(getBorder(),this);
                fm.put("border", bd);
            } catch (Throwable thr) {}
        }
        if (!(getFont() instanceof javax.swing.plaf.FontUIResource))
            fm.put("font",getFont());
        fm.put("tabVisible",isTabVisible);
        fm.put("queryType",queryType);
        fm.put("fieldback",unselectedBackground);
        fm.put("fieldfore",unselectedForeground);
        fm.put("fieldselback",selectedBackground);
        fm.put("fieldselfore",selectedForeground);
        fm.put("activetab",middle.getSelectedIndex());
        fm.put("rowBorderPainted",rowBorderPainted);
        fm.put("followActiveRecord",followActiveRecord);
        //Put the status per tab
        fm.put("noOfPanels",middle.getComponents().length);
        for (int i=0,s=middle.getComponents().length;i<s;i++)
        	fm.put("panel"+i,((TIPanel) middle.getComponentAt(i)).getState());

        //Save the children that need saving by this component
        try {
            Table[] tables=getTables();
            ArrayList<ESlateHandle> storeTables = new ArrayList<ESlateHandle>();
            for (int i=0; i<tables.length; i++) {
                if (tables[i].getESlateHandle().getParentHandle()==handle)
                    storeTables.add(tables[i].getESlateHandle());
            }
            ESlateHandle[] tHandles=new ESlateHandle[storeTables.size()];
            for (int i=0;i<tHandles.length;i++)
                tHandles[i]=(ESlateHandle) storeTables.get(i);
            handle.saveChildren(fm, "childrenTables", tHandles);
        } catch(Throwable t) {
            System.err.println("Record Browser: Problem saving child tables.");
            t.printStackTrace();
        }
        Enumeration en=fm.keys();
        while (en.hasMoreElements()) {
        	Object o=en.nextElement();
        	System.out.println(o+" "+(fm.get((String) o)).getClass());
        }
        out.writeObject(fm);
    }


    public void destroy() {
        try {
            backImageFile=null;
            if (background!=null)
                ((NewRestorableImageIcon) background).getImage().flush();
            background=null;
            ((ImageIcon) next.getIcon()).getImage().flush();
            ((ImageIcon) previous.getIcon()).getImage().flush();
            ((ImageIcon) query.getIcon()).getImage().flush();
            ((ImageIcon) clear.getIcon()).getImage().flush();
            ((ImageIcon) show.getIcon()).getImage().flush();
            ((ImageIcon) showHide.getIcon()).getImage().flush();
            if (bottom!=null) {
                bottom.removeAll();
                bottom=null;
            }
            drive=null;
            locale=null;
            WAIT_CURSOR=null;
            DEFAULT_CURSOR=null;
            messagesBundle=null;
            tooltipBundle=null;
            infoBundle=null;
            pinsBundle=null;
            if (visitedPages!=null) {
                visitedPages.clear();
                visitedPages=null;
            }
            if (haze!=null) {
                haze.getImage().flush();
                haze=null;
            }

            //super.destroy();
            pin=null;
            if (middle!=null) {
                middle.removeAll();
                middle=null;
            }
            if (tools!=null) {
                tools.removeAll();
                tools=null;
            }
            noOf=null;
        } catch(Exception e) {
            System.out.println("Problems encountered during destroying Record Browser!\n"+e.getMessage());
        }
    }

    public void setSelectedTabIndex(int i) {
        if (i<0 || i>=middle.getComponentCount())
            throw new IllegalArgumentException("Bad tab number");
        int old;
        if ((old=middle.getSelectedIndex())!=i) {
            middle.setSelectedIndex(i);
            middle.invalidate();
            middle.validate();
            repaint();
            listeners.activeTabChanged(new TableInspectorEvent(this,TableInspectorEvent.RB_ACTIVE_TAB_CHANGED,new Integer(old),new Integer(i)));
        }
    }

    public int getSelectedTabIndex() {
        return middle.getSelectedIndex();
    }

    public void setOpaque(boolean v) {
        super.setOpaque(v);
        repaint();
    }

    public void setToolNavigateVisible(boolean value) {
        navigateVisible=value;
        next.setVisible(value);
        previous.setVisible(value);
    }


    public boolean isToolNavigateVisible() {

        return navigateVisible;

    }


    public void setToolSelectViewVisible(boolean value) {
        selectViewVisible=value;
        show.setVisible(value);
    }


    public boolean isToolSelectViewVisible() {

        return selectViewVisible;

    }


    public void setToolQueryVisible(boolean value) {
        //query.setVisible(value);
        //clear.setVisible(value);
        queryVisible=value;
        for (int i=0;i<middle.getTabCount();i++)
            ((TIPanel) middle.getComponentAt(i)).queryTools(value);

        repaint();
    }


    public boolean isToolQueryVisible() {

        return queryVisible;

    }


    public void setToolSelectViewDefaultShowAll(boolean value) {
        sadefault=value;
        //This affects the default value. The change will be shown only if no tables are connected.
        if (middle.getComponentCount()==0) {
            if (value)
                show.setValueAsynchronously(0);
            else
                show.setValueAsynchronously(1);
        }
    }


    public boolean getToolSelectViewDefaultShowAll() {

        return sadefault;

    }


    public void setToolShowRecordVisible(boolean value) {

        hasShowHide=value;
        showHide.setVisible(value);
        //Inform all the panels
        for (int i=0;i<middle.getTabCount();i++) {
            ((TIPanel) middle.getComponentAt(i)).hasShowHide=value;

            ((TIPanel) middle.getComponentAt(i)).showHide();

        }

    }


    public boolean isToolShowRecordVisible() {

        return hasShowHide;

    }


    public void setToolbarVisible(boolean value) {

        toolbarVisible=value;

        if (value) {

            add(bottom,BorderLayout.SOUTH);

            repaint();

        } else {

            remove(bottom);

            repaint();

        }

        revalidate();

    }


    public boolean isToolbarVisible() {

        return toolbarVisible;

    }


    public void setMessagebarVisible(boolean value) {

        messagebarVisible=value;

        noOf.setVisible(value);

        bottom.revalidate();

    }


    public boolean isMessagebarVisible() {

        return messagebarVisible;

    }


    public void setBackgroundImageFile(String s) {

        backImageFile=s;

        Icon ii=null;
        if ((s.toLowerCase().endsWith(".gif")) || (s.toLowerCase().endsWith(".jpg"))) {
            try {
                ii=new NewRestorableImageIcon(new URL("file:/"+s));
            } catch(Exception e) {System.out.println(e.getMessage());}
        }
        if (ii!=null) {
            background=(Icon) ii;
            middle.setOpaque(false);
            middle.setBackground((Color) UIManager.getColor("control"));
            setOpaque(false);
            repaint();
        }
    }


    public String getBackgroundImageFile() {

        return backImageFile;

    }


    public Icon getBackgroundImage() {

        return background;

    }


    public void setBackgroundImage(Icon ic) {

        BufferedImage bi=new BufferedImage(ic.getIconWidth(),ic.getIconHeight(),BufferedImage.TYPE_INT_ARGB);

        ic.paintIcon(this,bi.getGraphics(),0,0);

        background=new NewRestorableImageIcon(bi);

    }


    public void setBackground(Color c) {

        super.setBackground(c);

        if (bottom!=null)

            bottom.setBackground(c);

        if (middle!=null)

            middle.setBackground(c);

        repaint();

    }


    public void setTabPlacement(int i) {

        middle.setTabPlacement(i);

        tabPos=i;

        repaint();

    }


    public int getTabPlacement() {

        return tabPos;

    }


    void renameTab(TIPanel ti,String name) {

        for (int i=0;i<middle.getTabCount();i++)

            if (middle.getComponentAt(i).equals(ti)) {

                middle.setTitleAt(i,name);

                break;

            }

        middle.repaint();

    }


    public void setImageAlignment(int i) {

        align=i;

        repaint();

    }


    public int getImageAlignment() {

        return align;

    }


    public void setPercentField(int d) {

        if (d<0)

            return;

        percentField=d;

        for (int i=0;i<middle.getTabCount();i++)

            ((TIPanel) middle.getComponentAt(i)).percentChanged();

    }


    public int getPercentField() {

        return percentField;

    }


    public void setWidgetsOpaque(boolean b) {

        bottom.setOpaque(b);

        for (int i=0;i<middle.getTabCount();i++)

            ((TIPanel) middle.getComponentAt(i)).setOpaque(b);

        repaint();

    }


    public boolean isWidgetsOpaque() {

        return bottom.isOpaque();

    }


    private javax.swing.plaf.TabbedPaneUI cacheUI;


    public void setTabVisible(boolean value) {

        isTabVisible=value;

        if (value) {

            if (cacheUI!=null)

                middle.setUI(cacheUI);

        } else {

            cacheUI=middle.getUI();

            middle.setUI(new NoTabUI());

        }

        invalidate();

        revalidate();

        repaint();

    }


    public boolean isTabVisible() {

        return isTabVisible;

    }


    public void setQueryType(int i) {

        if (i!=QUERY_TICK && i!=QUERY_COMBO)

            throw new IllegalArgumentException("Incorrect query type.");

        if (i==queryType)

            return;

        queryType=i;

        for (int j=0;j<middle.getTabCount();j++)

            ((TIPanel) middle.getComponentAt(j)).checkQueryType();

        repaint();

    }


    public int getQueryType() {

        return queryType;

    }


    public void setRowBackgroundColor(Color c) {

        unselectedBackground=c;

        repaint();

    }


    public Color getRowBackgroundColor() {

        return unselectedBackground;

    }


    public void setRowForegroundColor(Color c) {

        unselectedForeground=c;

        repaint();

    }


    public Color getRowForegroundColor() {

        return unselectedForeground;

    }


    public void setRowSelectedBackgroundColor(Color c) {

        selectedBackground=c;

        repaint();

    }


    public Color getRowSelectedBackgroundColor() {

        return selectedBackground;

    }


    public void setRowSelectedForegroundColor(Color c) {

        selectedForeground=c;

        repaint();

    }


    public Color getRowSelectedForegroundColor() {

        return selectedForeground;

    }


    public void setRowBorderPainted(boolean b) {

        rowBorderPainted=b;

        for (int j=0;j<middle.getTabCount();j++)

            ((TIPanel) middle.getComponentAt(j)).setRowBorderPainted(b);

    }


    public boolean isRowBorderPainted() {

        return rowBorderPainted;

    }


    public void setFollowActiveRecord(boolean b) {

        followActiveRecord=b;

    }


    public boolean getFollowActiveRecord() {

        return followActiveRecord;

    }


    /**

     * Locks the field in the given tab to its current value. If already locked, it is unlocked
     * and then relocked to the new value.
     */
    public void lock(int tabNumber,String fieldName) {
        if (tabNumber<1 || tabNumber>middle.getTabCount())
            throw new IllegalArgumentException("Bad tab number.");
        ((TIPanel) middle.getComponentAt(tabNumber-1)).lock(fieldName);
    }
    /**

     * Locks the field in the given tab to the given value. If already locked, it is unlocked
     * and then relocked to the new value. If the value doesnot exist, the call is omitted.
     */
    public void lock(int tabNumber,String fieldName,String value) {
        if (tabNumber<1 || tabNumber>middle.getTabCount())
            throw new IllegalArgumentException("Bad tab number.");
        if (getQueryType()==QUERY_TICK)
            return;

        ((TIPanel) middle.getComponentAt(tabNumber-1)).lock(fieldName,value);
    }
    /**

     * Unlocks the field.
     */
    public void unlock(int tabNumber,String fieldName) {
        if (tabNumber<1 || tabNumber>middle.getTabCount())
            throw new IllegalArgumentException("Bad tab number.");
        ((TIPanel) middle.getComponentAt(tabNumber-1)).unlock(fieldName);
    }


    public void setFieldQueryToolVisible(int tabNumber,String fieldName,boolean state) {

        if (tabNumber<1 || tabNumber>middle.getTabCount())

            throw new IllegalArgumentException("Bad tab number.");
        ((TIPanel) middle.getComponentAt(tabNumber-1)).setFieldQueryToolVisible(fieldName,state);

    }


    public boolean isFieldQueryToolVisible(int tabNumber,String fieldName) {

        if (tabNumber<1 || tabNumber>middle.getTabCount())

            throw new IllegalArgumentException("Bad tab number.");
        return ((TIPanel) middle.getComponentAt(tabNumber-1)).isFieldQueryToolVisible(fieldName);

    }


    public void paintComponent(Graphics g) {

       if (background!=null)

            if (align==1)

                background.paintIcon(this,g,getWidth()-background.getIconWidth(),0);

            else

                background.paintIcon(this,g,0,0);

        super.paintComponent(g);

    }


    //This class to avoid security problems from ICE browser

    class MySecurityManager extends SecurityManager {

        boolean enabled = false;

        public void checkPermission(java.security.Permission perm) {
            if (!enabled) return;
            if (perm.getName().equals("setSecurityManager"))
                super.checkPermission(perm);
        }

        void enable() {
            enabled = true;
        }

        void disable() {
            enabled = false;
        }
    }


    public class NoTabUI extends javax.swing.plaf.basic.BasicTabbedPaneUI {
        protected void installListeners() {

            super.installListeners();

            //Deactivate tab change

            tabPane.removeMouseListener(mouseListener);
        }
        protected void paintTab(Graphics g, int tabPlacement,

                            Rectangle[] rects, int tabIndex,
                            Rectangle iconRect, Rectangle textRect) {
        }
        protected void paintFocusIndicator(Graphics g, int tabPlacement,

                                       Rectangle[] rects, int tabIndex,
                                       Rectangle iconRect, Rectangle textRect,
                                       boolean isSelected) {
        }
        protected void paintTabBorder(Graphics g, int tabPlacement,

                                  int tabIndex,
                                  int x, int y, int w, int h,
                                  boolean isSelected ) {
        }
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {

        }

    }


    public class Toolbar extends JPanel {

        SmartLayout smartLayout1 = new SmartLayout();
        {
            tools=new JPanel();
            drive=new JLabel();
            next=new NoBorderButton();
            previous=new NoBorderButton();
            noOf=new JLabel();
        }

        public Toolbar() {
            try  {
                jbInit();
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            noOf.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (noOf.getText().equals("") || e.getClickCount()!=2) return;
                    String s=JOptionPane.showInputDialog(TableInspector.this, messagesBundle.getString("gotomessage"), messagesBundle.getString("gototitle"), JOptionPane.QUESTION_MESSAGE);
                    try {
                        if (s!=null) {
                            if (!(getSelectedTIPanel().trySetCurObject(new Integer(Integer.valueOf(s).intValue()-1)))) {
                                JOptionPane.showMessageDialog(TableInspector.this, messagesBundle.getString("notinrange")+" (1-"+getSelectedTIPanel().getNoOfObjects()+")", messagesBundle.getString("error"), JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(TableInspector.this, messagesBundle.getString("invalidnumber"), messagesBundle.getString("error"), JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            //Show/Hide button
            showHide=new NoBorderToggleButton(loadImageIcon("eyeclosed.gif"));
            showHide.setSelectedIcon(loadImageIcon("eyeopen.gif"));
            showHide.setSelected(false);

            showHide.setToolTipText(tooltipBundle.getString("showhide"));
            showHide.setFocusPainted(false);
            showHide.setRequestFocusEnabled(false);
            showHide.setOpaque(false);
            showHide.setAlignmentY(CENTER_ALIGNMENT);
            Dimension dim=new Dimension(28,22);
            showHide.setPreferredSize(dim);
            showHide.setMaximumSize(dim);
            showHide.setMinimumSize(dim);
            showHide.setEnabled(false);
            showHide.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    TableInspector.this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    getSelectedTIPanel().showHide();
                    TableInspector.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
            tools.add(showHide);
            showHide.setVisible(false);

            //Show all/selected button
            show=new JButtonWithPopup(loadImageIcon("showAll.gif"),loadImageIcon("showSelected.gif"));
            show.setFocusPainted(false);
            show.setRequestFocusEnabled(false);
            show.setOpaque(false);
            show.setAlignmentY(CENTER_ALIGNMENT);
            dim=new Dimension(22,22);
            show.setPreferredSize(dim);
            show.setMaximumSize(dim);
            show.setMinimumSize(dim);
            show.setToolTipText(tooltipBundle.getString("show"));
            show.addElement(messagesBundle.getString("showall"));
            show.addElement(messagesBundle.getString("showselected"));
            show.setValueAsynchronously((sadefault?0:1));
            show.setEnabled(false);
            tools.add(show);

            //Previous
            previous.setIcon(loadImageIcon("previous.gif"));
            previous.setToolTipText(tooltipBundle.getString("previous"));
            previous.setEnabled(false);
            previous.setOpaque(false);
            previous.setFocusPainted(false);
            previous.setRequestFocusEnabled(false);
            previous.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    getSelectedTIPanel().previous();
                }
            });

            //Haze
            drive.setIcon(loadImageIcon("haze.gif"));
            drive.setVisible(false);

            //Next button
            next.setIcon(loadImageIcon("next.gif"));
            next.setToolTipText(tooltipBundle.getString("next"));
            next.setEnabled(false);
            next.setOpaque(false);
            next.setFocusPainted(false);
            next.setRequestFocusEnabled(false);
            next.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    getSelectedTIPanel().next();
                }
            });

        }

        private void jbInit() throws Exception {
            this.setLayout(smartLayout1);
            tools.setLayout(new BoxLayout(tools,BoxLayout.X_AXIS));
            tools.setOpaque(false);
            noOf.setHorizontalAlignment(SwingConstants.RIGHT);
            this.add(drive, new com.thwt.layout.LayoutConstraint(
                        new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, -2),
                        new com.thwt.layout.FractionAnchor(null, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.VerticalCenter, 0.0, -1),
                        new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
                        new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
            this.add(tools, new com.thwt.layout.LayoutConstraint(
                        new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 51),
                        new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 30),
                        new com.thwt.layout.ContainerAnchor(Anchor.Left, 2),
                        new com.thwt.layout.FractionAnchor(null, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.VerticalCenter, 0.0, 0)));
            this.add(previous, new com.thwt.layout.LayoutConstraint(
                        new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 40),
                        new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 22),
                        new com.thwt.layout.FractionAnchor(null, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.VerticalCenter, 0.0, 0),
                        new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, -1)));
            this.add(next, new com.thwt.layout.LayoutConstraint(
                        new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 40),
                        new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 22),
                        new com.thwt.layout.FractionAnchor(null, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.VerticalCenter, 0.0, 0),
                        new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 1)));
            this.add(noOf, new com.thwt.layout.LayoutConstraint(
                        new com.thwt.layout.ContainerAnchor(Anchor.Right, 2),
                        new com.thwt.layout.FractionAnchor(null, Anchor.VerticalCenter, 0.0, Anchor.Right, Anchor.VerticalCenter, 0.0, 0),
                        new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
                        new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
        }
    }

}







































