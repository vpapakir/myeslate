package cbookeditor.impl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookService;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.CBookWidgetIF;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.SuccessStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import cbookeditor.Wrapper;

public class WidgetContainer extends JPanel implements CBookContext {

	private String id = UUID.randomUUID().toString();

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
		map.put(Constants.UUID, id);
	}
	
	static class SizesPanel extends JPanel implements ActionListener {
		JTextField w,h;
		CBookWidgetEditIF editor;
		JCheckBox popup;
		public SizesPanel(CBookWidgetEditIF editor) {
			super();
			this.editor = editor;
			setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
			Dimension dim = editor.getInstanceSize();
			w = new JTextField(String.valueOf(dim.width));
			h = new JTextField(String.valueOf(dim.height));
			w.addActionListener(this);
			h.addActionListener(this);
			add(new JLabel("dim:"));
			add(w);
			add(new JLabel("×"));
			add(h);
			popup = new JCheckBox("popup");
			add(popup);
		}

		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if(source == w) {
				editor.setInstanceWidth(Integer.parseInt(w.getText()));
			} else if(source == h ) {
				editor.setInstanceHeight(Integer.parseInt(h.getText()));
			}
		}
				
		public Dimension getInstanceSize() {
			editor.setInstanceWidth(Integer.parseInt(w.getText()));
			editor.setInstanceHeight(Integer.parseInt(h.getText()));
			return editor.getInstanceSize();
		}
		
		public boolean isPopup() {
			return popup.isSelected();
		}
		public void setPopup(boolean popup) {
			this.popup.setSelected(popup);
		}
	}
	

	private static final String EMPTY_MAP = "{}";

	static DataFlavor widgetFlavor;
	static {
		try {
			widgetFlavor = 
			new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + Holder.class.getName() + '\"', null, Holder.class.getClassLoader());
		} catch (ClassNotFoundException e) {
			// Should not happen!
			e.printStackTrace();
		}
		//System.out.println(widgetFlavor);
		//new DataFlavor(Holder.class, "WidgetContainer Holder");
	}

	static DataFlavor[] flavors = { widgetFlavor };

	
	static class GlassComponent extends JPanel implements MouseListener, DropTargetListener {
		
		class MyTransferHandler extends TransferHandler{
			
			MyTransferHandler() {
				super("container");
			}
			



			@Override
			public Icon getVisualRepresentation(Transferable arg0) {
				//return super.getVisualRepresentation(arg0);
				return container.icon;
			}

			public boolean canImport(JComponent c, DataFlavor[] f){
			   for(DataFlavor d:f){
				   if(d.equals(widgetFlavor))
					   return true;
			   }
			   return false;
			}
			
			public boolean importData(JComponent comp, Transferable t, Point p){
			  return true;
			
			}
			
			@Override
			public int getSourceActions(JComponent c) {
				return DnDConstants.ACTION_REFERENCE;
			}

			public Transferable createTransferable(JComponent c){
				return new Transferable() {

					public DataFlavor[] getTransferDataFlavors() {
						return flavors;
					}

					public boolean isDataFlavorSupported(DataFlavor flavor) {
						return widgetFlavor.equals(flavor);
					}

					public Object getTransferData(DataFlavor flavor)
							throws UnsupportedFlavorException, IOException {
						if (isDataFlavorSupported(flavor))
						{	//dragSource = getContainer();
							return new Holder(getContainer());
						}
						throw new UnsupportedFlavorException(flavor);
					}
				};
						
			}
			
		}

		private WidgetContainer container;
		private TransferHandler transfer = new MyTransferHandler();
		private DropTarget target;
		private boolean exited;

		public WidgetContainer getContainer() {
			return this.container;
		}

		public GlassComponent(WidgetContainer container) {
			super(null);
			this.container = container;
			setOpaque(false);
			setTransferHandler(transfer);
			target = new DropTarget(this, this);
			addMouseListener(this);
		}

		public void mouseClicked(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			if(e.isShiftDown()) // is linking...
			{ 
				exited = true;
				TransferHandler handle = getTransferHandler();
				handle.exportAsDrag(this, e, handle.getSourceActions(null));
			} else
				exited = false;
		}

		public void mouseReleased(MouseEvent e) {
			if(!exited) container.openEditorDialog();
			
		}
		public void mouseEntered(MouseEvent e) {			
		}
		public void mouseExited(MouseEvent e) {
			exited = true;
		}
		public void dragEnter(DropTargetDragEvent dtde) {
			//System.out.println("drag enter " + dtde);
		}

		public void dragOver(DropTargetDragEvent dtde) {}

		public void dropActionChanged(DropTargetDragEvent dtde) {
		}
		public void dragExit(DropTargetEvent dtde) {
		}
		
		@SuppressWarnings("unchecked")
		public void drop(DropTargetDropEvent dtde) {
			//System.out.println("drag drop " + dtde);
			Transferable t = dtde.getTransferable();
			DataFlavor tt = t.getTransferDataFlavors()[0];
			try {
				Holder o = (Holder) t.getTransferData(tt);
				WidgetContainer source = o.getObject();
				String[] acceptedCmds = container.getAcceptedCmds();
				if(acceptedCmds != null)
				{	
					String[] sendCmds = source.getSendCmds();
					if(sendCmds != null)
					{	Set<String> destSet = new TreeSet<String>(Arrays.asList(sendCmds));
						Set<String> sourceSet = new TreeSet<String>(Arrays.asList(acceptedCmds));
						destSet.retainAll(sourceSet);
						if(!destSet.isEmpty())
						{
							source.connect(container, destSet);
						}
					}
				}
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			dtde.dropComplete(true);
		}
		
	}
		
	JComponent content;
	GlassComponent glass = new GlassComponent(this);
	private CBookWidgetIF widget;
	private CBookWidgetInstanceIF instance;
	private CBookWidgetEditIF editor;
	private CBookEventHandler handler = new CBookEventHandler(this);
	private Icon icon;
	public String[] getAcceptedCmds() {
		return editor.getAcceptedCmds();
	}
	
	List connections = new JSONArray();
	
	public void connect(WidgetContainer dest, Set set) {
		if(dest == this) return;
		CBookEventListener listener = dest.instance.asEventListener();
		if(listener == null) return;
		Object[] possibleValues = set.toArray();
		Object selectedValue = JOptionPane.showInputDialog(this,
		"Choose one", "Command",
		JOptionPane.INFORMATION_MESSAGE, null,
		possibleValues, possibleValues[0]);
		String command = (String) selectedValue;
		if(command != null)
		{
			instance.addCBookEventListener(listener, command);			
			connections.add(Collections.singletonMap(command, dest.getId()));
		}		
	}

	public String[] getSendCmds() {
		return editor.getSendCmds();
	}

	private HashMap<String, Object> map = new HashMap();
	private CBookContext context;
	String state = EMPTY_MAP;

	private String launchData = EMPTY_MAP;
	private Dimension instanceSize;
	private boolean popup;
	private JButton popupBtn;
	private JDialog popupDlg;
	
	private ActionListener popupAction = new ActionListener() {

		public void actionPerformed(ActionEvent arg0) {
			if(popupDlg == null)
			{
				popupDlg = new JDialog(JOptionPane.getFrameForComponent(WidgetContainer.this), widget.toString(), false);
				popupDlg.setContentPane(content);
				popupDlg.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
				popupDlg.setSize(content.getSize()); // TODO insets.
			}
			popupDlg.setVisible(true);
		}
		
	};
	
	public Dimension getMaximumSize() {
		return getSize();
	}
	
	public Dimension getPreferredSize() {
		if(popup)
			return popupBtn.getPreferredSize();
		return new Dimension(instanceSize);
	}
	
	WidgetContainer(CBookWidgetIF widget, CBookEventListener listener, CBookContext context) {
		super(null);
		this.context = context;
		this.widget = widget;
		handler.addCBookEventListener(listener, null);
		instance = widget.getInstance(this);
		editor = widget.getEditor(this);
		try {
			icon = widget.getIcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(icon == null) icon = new DefaultIcon();
		popupBtn = new JButton(icon);
		launchData = JSONValue.toJSONString(editor.getLaunchData());
		instance.setAssessmentMode(AssessmentMode.OEFENEN);
		instance.addCBookEventListener(listener, null);
		content  = instance.asComponent();
		instanceSize = editor.getInstanceSize();
		map.put(Constants.INSTANCE_SIZE, instanceSize);
		map.put(Constants.UUID, id);
		content.setSize(instanceSize);
		content.setLocation(0, 0);
		glass.setSize(instanceSize);
		setSize(instanceSize);
		add(glass); // top?
		add(content);
		add(popupBtn);
		instance.init();
		Map launchmap = (Map)JSONValue.parse(launchData);
		if(launchmap == null)
			launchmap = Collections.emptyMap();
		else
			launchmap = Collections.unmodifiableMap(launchmap);
		try {
			instance.setLaunchData(launchmap, getRandomVars());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean openEditorDialog() {
		
		JComponent component = editor.asComponent();
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(component, BorderLayout.CENTER);
		SizesPanel sizes = new SizesPanel(editor);
		sizes.setPopup(popup);
		panel.add(sizes, BorderLayout.SOUTH);
// HeaderPanel?
		HeaderPanel header = new HeaderPanel(editor, map);
		//JPanel header = new JPanel();
		
		panel.add(header, BorderLayout.NORTH);
		
		Map<String,?> data = (Map<String,?>) JSONValue.parse(launchData);
		data = Collections.unmodifiableMap(data);
		editor.setLaunchData(data);
		int r = JOptionPane.showConfirmDialog(this, panel, widget.toString(),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, icon);
		if(r == JOptionPane.OK_OPTION)
		{
			launchData = JSONValue.toJSONString(editor.getLaunchData());
			handler.fire("getLaunchData", "launchData", launchData);
			instanceSize = sizes.getInstanceSize();
			state = EMPTY_MAP;
			popup = sizes.isPopup();
			setSize(getPreferredSize());
			glass.setSize(getSize());
			if(popup) {
				remove(content);
				popupBtn.setVisible(true);
				popupBtn.setSize(popupBtn.getPreferredSize());
			} else {
				add(content);
				popupBtn.setVisible(false);	
			}
			header.updateMap();
			map.put(Constants.INSTANCE_SIZE, instanceSize);
			content.setSize(instanceSize);
			content.invalidate();
			content.validate();
			instance.init();
			data = (Map<String,?>)JSONValue.parse(launchData);
			if(data == null)
				data = Collections.emptyMap();
			else
				data = Collections.unmodifiableMap(data);
			instance.setLaunchData(data, getRandomVars());
			return true;
		}
		return false;
	}

	private Map getRandomVars() {
		Object o = context.getProperty("randomVars");
		if(o instanceof Map) {
			return (Map)o;
		}
		return Collections.EMPTY_MAP;
	}

	public void setViewMode(boolean viewing) {
		if(viewing)
		{	glass.setVisible(false);
			popupBtn.addActionListener(popupAction);
// TODO een nieuwe instance gebruiken?
//			instance = widget.getInstance(context);
//			component = instance.asComponent();
//			listener  = instace.asEventListener();
//			wiring...
			
			instance.setState(getState());
			instance.start();
		} else {
			popupBtn.removeActionListener(popupAction);
			glass.setVisible(true);
			instance.stop();
			Map stateMap = instance.getState();
			setState(stateMap);
			handler.fire("getState", "state", state);
			instance.setState(Collections.EMPTY_MAP);
			if(popupDlg != null) popupDlg.hide();
		}
			
		;
	}
	
	public void setState(Map map) {
		state = JSONValue.toJSONString(map);
	}
	
	public Object getProperty(String key) {
		if(map.containsKey(key))
			return map.get(key);
		return context.getProperty(key);
	}

	public int getScore() {
		return instance.getScore();
	}

	public SuccessStatus getSuccessStatus() {
		return instance.getSuccessStatus();
	}

	public int getMaxScore() {
		return editor.getMaxScore();
	}

	public static WidgetContainer newInstance(CBookService service,
			CBookEventListener listener, JTextPane text, CBookContext context) {
		Object[] possibleValues = service.getRegisteredWidgets().toArray();
		Object selectedValue = JOptionPane.showInputDialog(text,
		"Choose one", "Input",
		JOptionPane.INFORMATION_MESSAGE, null,
		possibleValues, possibleValues[0]);
		if(selectedValue instanceof CBookWidgetIF)
		{
			WidgetContainer wc = new WidgetContainer((CBookWidgetIF) selectedValue, listener, context);
			if( wc.openEditorDialog() );
				return wc;
		}
		return null;
	}

	public void reset() {
		instance.reset();
	}

	public void destroy() {
		if(!glass.isVisible())
			instance.stop();
		instance.destroy();
		
	}

	public void setAssessmentMode(AssessmentMode mode) {
		instance.setAssessmentMode(mode);		
	}

	public void acceptCBookEvent(CBookEvent event) {
		String[] cmds = editor.getAcceptedCmds();
		if(cmds == null) return;
		List<String> accepts = Arrays.asList(cmds);
		if(accepts.contains(event.getCommand()))
			instance.asEventListener().acceptCBookEvent(event);
	}

	public Map getLaunchData() {
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("launchData", JSONValue.parse(launchData));
		data.put("width", instanceSize.width);
		data.put("height", instanceSize.height);
		data.put("popup", popup);
		data.put(Constants.MAX_SCORE, getMaxScore());
		data.put(Constants.LOGGING, getProperty(Constants.LOGGING));
		data.put(Constants.LOG_ID, getProperty(Constants.LOG_ID));
		data.put("class", widget instanceof Wrapper ? ((Wrapper) widget).getClassName() : widget.getClass().getName());
		data.put("connections", connections);
		data.put("id", getId());
		return data;
	}

	public WidgetContainer(Map launchData, CBookService service, CBookContext context, CBookEventListener listener) {
		super(null);
		this.context = context;
		map.putAll(launchData);
		handler.addCBookEventListener(listener, null);
		setLocale((Locale) getProperty("locale"));
		
		Object value;
		value = map.remove("id");
		if(value!=null) setId(value.toString());
		value = map.remove("connections");
		if(value!=null) connections = (List) value;
		value = map.remove("class");
		widget = service.widgetForName(value.toString());
		editor = widget.getEditor(this);
		value = map.get("launchData");
		this.launchData = JSONValue.toJSONString(value);
		editor.setLaunchData((Map<String, ?>) value);
		instanceSize = editor.getInstanceSize();
		value = map.remove("width");
		if(value!=null)
		{
			instanceSize.width = ((Number) value).intValue();
			editor.setInstanceWidth(instanceSize.width);
		}
		value = map.remove("height");
		if(value!=null)
		{
			instanceSize.height = ((Number)value).intValue();
			editor.setInstanceHeight(instanceSize.height);
		}
		map.put(Constants.INSTANCE_SIZE, instanceSize);
		value = map.remove("popup");
		popup = Boolean.TRUE.equals(value);
		instance = widget.getInstance(this);
		content = instance.asComponent();
		content.setSize(instanceSize);
		content.setLocation(0, 0);
		try {
			icon = widget.getIcon();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(icon == null) icon = new DefaultIcon();
		popupBtn = new JButton(icon);
		popupBtn.addActionListener(popupAction);
		popupBtn.setVisible(popup);
		popupBtn.setSize(popupBtn.getPreferredSize());
		setSize(getPreferredSize());
		glass.setSize(getSize());
		add(glass);
		if(!popup) add(content);		
		add(popupBtn);
		instance.init();
		try {
			Map launchmap = (Map)map.remove("launchData");
			if (launchmap == null)
				launchmap = Collections.emptyMap();
			else
				launchmap = Collections.unmodifiableMap(launchmap);
			instance.setLaunchData(launchmap, getRandomVars());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initConnections(CBookContext ids) {
		Iterator iter = connections.iterator();
		while (iter.hasNext()) {
			Map<String,String> type = (Map) iter.next();
			Map.Entry<String,String> entry = type.entrySet().iterator().next();
			WidgetContainer w = (WidgetContainer) ids.getProperty(entry.getValue());
			if(w != null)
				instance.addCBookEventListener(w.instance.asEventListener(), entry.getKey());
			else
				iter.remove();
		}
		
	}
	
	public Map getState() {
		return (Map) JSONValue.parse(state);
	}
}
