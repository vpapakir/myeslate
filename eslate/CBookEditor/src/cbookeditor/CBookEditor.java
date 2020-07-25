package cbookeditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookService;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.SuccessStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import cbookeditor.impl.FontChooser;
import cbookeditor.impl.RandomVarsPanel;
import cbookeditor.impl.WidgetContainer;

public class CBookEditor extends JPanel implements Constants {
	private JComponent logger;
	private CBookService service;
	private Container content;
	private JTextPane text;
	private JToolBar toolbar;
	private RandomVarsPanel randomVars = new RandomVarsPanel();
	
	class EditorContext implements CBookContext {
		public Object getProperty(String key) {
			if(FONT.equals(key))
				return text.getFont();
			if(BACKGROUND.equals(key))
				return text.getBackground();
			if(FOREGROUND.equals(key))
				return text.getForeground();
			if("randomVars".equals(key))
				return randomVars.getMap();
			if (parentContext != null)
				return parentContext.getProperty(key);
			return null;
		}
		
	}
	EditorContext context = new EditorContext();
	
	List<WidgetContainer> widgets = new LinkedList<WidgetContainer>();
	boolean viewing;
	
	AbstractAction insertWidget = new AbstractAction("New") {
		public void actionPerformed(ActionEvent arg0) {
			try {
				WidgetContainer panel = WidgetContainer.newInstance(service, listener, text, context);
				if(panel == null) return;
				panel.setAssessmentMode((AssessmentMode) modeBox.getSelectedItem());
				widgets.add(panel);
				text.insertComponent(panel);
			} catch (Exception e) {
				e.printStackTrace(); // should not happen?
			}
		}
	};
	
	Action fontAction = new AbstractAction("Font...") {
		public void actionPerformed(ActionEvent arg0) {
			FontChooser chooser = new FontChooser(JOptionPane.getFrameForComponent(text));
			chooser.show();
			Font f = chooser.getNewFont();
			if(f != null) text.setFont(f);
			Color c = chooser.getNewColor();
			if( c != null)
				text.setForeground(c);
		}
	};
	
	CBookEvent checkEvent = new CBookEvent(this, "check", Collections.singletonMap("checked", true));
	void sendCheckEvent() {
		for(WidgetContainer w: widgets) {
			w.acceptCBookEvent(checkEvent);
		}
	}
	
	
	Action checkAction = new AbstractAction("Check") {

		public void actionPerformed(ActionEvent arg0) {
			sendCheckEvent();
		}
		
	};
	
	
	Action backgroundAction = new AbstractAction("Background...") {

		public void actionPerformed(ActionEvent arg0) {
			Color c = JColorChooser.showDialog(text, "background", text.getBackground());
			if(c != null)
				text.setBackground(c);
		}
		
	};
	
	Action randomAction = new AbstractAction("RandomVars...") {

		public void actionPerformed(ActionEvent arg0) {
			randomVars.showDialog(text);
		}
		
	};
	
	Action viewMode = new AbstractAction("View") {

		public void actionPerformed(ActionEvent arg0) {
			insertWidget.setEnabled(viewing);
			text.setEditable(viewing);
			viewing = !viewing;
			for (Iterator<WidgetContainer> iterator = widgets.iterator(); iterator.hasNext();) 
			{
				WidgetContainer widget = iterator.next();
				if(widget.isShowing())
				{
					widget.setViewMode(viewing);
				} else {
					iterator.remove();
				}
			}
			this.putValue(Action.NAME, viewing?"Edit":"View");
			reset.setEnabled(viewing);
			CBookEvent event = new CBookEvent(CBookEditor.this, "setViewMode",Collections.singletonMap( "viewMode", viewing));		
			listener.acceptCBookEvent(event);

		}
		
	};
	
	Action reset = new AbstractAction("Reset") {
		public void actionPerformed(ActionEvent _) {
			resetWidgets();
		}
	};
	
	private CBookEventListener listener;
	private CBookContext parentContext;
	private ActionListener modeAction = new ActionListener() {

		public void actionPerformed(ActionEvent arg0) {
			AssessmentMode mode = (AssessmentMode) ((JComboBox) arg0.getSource()).getSelectedItem();
			CBookEvent event = new CBookEvent(CBookEditor.this, "setAssessmentMode",Collections.singletonMap( "mode", mode));		
			listener.acceptCBookEvent(event);
			setAssessmentMode(mode);
		}
		
	};
	private JComboBox modeBox;
		
	public CBookEditor(JComponent logger, CBookService service, CBookEventListener listener, CBookContext parent)
			throws HeadlessException {
		super();
		this.logger = logger;
		this.service = service;
		this.listener = listener;
		this.parentContext = parent;
		content = this;
		content.setLayout(new BorderLayout());
		content.add(new JScrollPane(logger), BorderLayout.SOUTH);
		text = new JTextPane();
		add(new JScrollPane(text), BorderLayout.CENTER);
		toolbar = new JToolBar();
		add(toolbar, BorderLayout.NORTH);
		toolbar.add(insertWidget);
		toolbar.add(viewMode);
		reset.setEnabled(false);
		toolbar.add(reset);
		toolbar.add(randomAction);
		toolbar.add(fontAction);
		toolbar.add(backgroundAction);
		modeBox = new JComboBox(AssessmentMode.values());
		modeBox.addActionListener(modeAction);
		toolbar.add(modeBox);
		toolbar.add(checkAction);
		setSize(600,400);
		setPreferredSize(getSize());
		text.setText("This is a sample\nCBook.\n");	
	}

	protected void setAssessmentMode(AssessmentMode mode) {
		for(WidgetContainer w: widgets)
			w.setAssessmentMode(mode);
	}

	protected void resetWidgets() {
		for(WidgetContainer w : widgets)
		{
			w.reset();
		}
		CBookEvent event = new CBookEvent(this, "RESET");
		listener.acceptCBookEvent(event);
	}

	public int getMaxScore() {
		int sum = 0;
		for(WidgetContainer w : widgets)
		{
			sum += w.getMaxScore();
		}
		return sum;
	}

	public int getScore() {
		int sum = 0;
		for(WidgetContainer w : widgets)
		{
			sum += w.getScore();
		}
		return sum;
	}

	public SuccessStatus getSuccessStatus() {
		SuccessStatus result = SuccessStatus.PASSED;
		for(WidgetContainer w : widgets)
		{
			SuccessStatus s = w.getSuccessStatus();
			if(s == SuccessStatus.UNKNOWN)
				return s;
			if(s == SuccessStatus.FAILED)
				result = s;
		}
		return result;		
	}

	public void destroy() {
		for(WidgetContainer w : widgets)
		{
			w.destroy();
		}
	}
	
	public void writeLaunchData(Writer writer) throws IOException {
		JSONArray launchData = new JSONArray();
		StyledDocument document = text.getStyledDocument();
	    Element root = document.getDefaultRootElement();
	    ElementIterator it = new ElementIterator(root);
	    Element next;
	    while( null != (next = it.next()))
	    {
	        AttributeSet set = next.getAttributes();
	        WidgetContainer w = (WidgetContainer) StyleConstants.getComponent(set);
	        if(w != null)
	        {
	            launchData.add(w.getLaunchData());
	        } else
	        if(next.isLeaf())
	        {
	            int offset = next.getStartOffset();
	            int length = next.getEndOffset()-offset;
				try {
					String content = document.getText(offset, length);
		            launchData.add(content);
				} catch (BadLocationException _) {
				}
	         }	        
	    }
	    JSONObject map = new JSONObject();
	    map.put("document", launchData);
	    map.put("randomvars", randomVars.getText());
	    map.put("assessmentMode", modeBox.getSelectedItem().toString());
	    map.writeJSONString(writer);
	}
	
	public void writeSuspendData(Writer out) throws IOException {
		JSONObject map = new JSONObject();
		for(WidgetContainer w: widgets) {
			map.put(w.getId(), w.getState());
		}
		map.writeJSONString(out);
	}
	
	public void readSuspendData(Reader in) throws IOException, ParseException {
		Map map = (Map) JSONValue.parseWithException(in);
		for(WidgetContainer w: widgets) {
			w.setState((Map) map.get(w.getId()));
		}
		
	}
	
	
	
	public void readLaunchData(Reader in) throws IOException, ParseException {
		text.setText("");
		Map map = (Map) JSONValue.parseWithException(in);
		randomVars.setText(String.valueOf(map.get("randomvars")));
		List list = (List) map.get("document");
		AssessmentMode mode = AssessmentMode.valueOf(map.get("assessmentMode").toString());
		for(Object item : list ) {
			Document document = text.getDocument();
			if(item instanceof Map) {
				try {
					WidgetContainer w = new WidgetContainer( (Map)item, service, context, listener);
					w.setAssessmentMode(mode);
					text.insertComponent(w);
					widgets.add(w);
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			} else
			{
				try {
					document.insertString(text.getCaretPosition(), item.toString(), null);
				} catch (BadLocationException _) {
				}
			}
			text.setCaretPosition(document.getLength());
		}
		text.setCaretPosition(0);
		modeBox.setSelectedItem(mode);
		CBookContext ids = new CBookContext() {

			public Object getProperty(String key) {
				for(WidgetContainer w: widgets)
					if(w.getId().equals(key)) return w;
				return null;
			}};

		for(WidgetContainer w : widgets) {
			w.initConnections(ids);
		} 
	}
	
	
}
