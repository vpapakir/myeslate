package fi.ltiwidget;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.text.ParseException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.Constants;

public class LTIEditor extends JPanel implements CBookWidgetEditIF {

	protected static final AbstractFormatter URI_FORMATTER = new AbstractFormatter() {

		@Override
		public Object stringToValue(String text) throws ParseException {
			if("".equals(text)) return null;
			try {
				return new URI(text);
			} catch(Exception e)
			{
				throw new ParseException(text,0);
			}
		}

		@Override
		public String valueToString(Object value) throws ParseException {
			if(value == null) return "";
			return ((URI)value).toString();
		}};

	private static final AbstractFormatterFactory TF = new AbstractFormatterFactory() {
		
		@Override
		public AbstractFormatter getFormatter(JFormattedTextField tf) {
			return URI_FORMATTER;
		}
	};
	private Dimension size = new Dimension(200,200);
	private CBookContext context;
	private JFormattedTextField urlField = new JFormattedTextField();
	private JTextField keyField = new JTextField(20);
	private JTextField cssField = new JTextField(20);
	private JPasswordField secretField = new JPasswordField(8);
	private JCheckBox btnBox = new JCheckBox("Use Browser");
	private JTextField titleField = new JTextField(20);
	private JTextArea  customField = new JTextArea();

	private String uuid; 
	
	
	public LTIEditor(CBookContext context) {
		super(null);
		this.context = context;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		urlField.setFormatterFactory(TF);
		urlField.setValue(URI.create("about:blank"));
		urlField.setColumns(60);
		
		add(btnBox);
		Box h;
		add(h = Box.createHorizontalBox());
		h.add(new JLabel("URL "));h.add(urlField);h.add(Box.createGlue());
		add(h = Box.createHorizontalBox());
		h.add(new JLabel("Key "));h.add(keyField); h.add(Box.createGlue());
		add(h = Box.createHorizontalBox());
		h.add(new JLabel("Secret "));h.add(secretField);h.add(Box.createGlue());
		add(Box.createVerticalStrut(10));
		
		add(h = Box.createHorizontalBox());
		h.add(new JLabel("Title ")); h.add(titleField);h.add(Box.createGlue());
		add(h = Box.createHorizontalBox());
		h.add(new JLabel("CSS "));h.add(cssField);h.add(Box.createGlue());

		add(Box.createVerticalStrut(10));
		add(h = Box.createHorizontalBox());
		h.add(new JLabel("Custom parameters"));h.add(Box.createGlue());
		customField.setRows(5);
		JScrollPane scrollPane = new JScrollPane(customField);
		//scrollPane.setBorder(BorderFactory.createLineBorder(Color.gray));
		add(h = Box.createHorizontalBox());h.add(Box.createHorizontalStrut(20));
		h.add(scrollPane);
		
		
		
		
		
		add(Box.createVerticalGlue());
	}

	@Override
	public JComponent asComponent() {
		JPanel p = new JPanel();
		p.add(this);
		return p;
	}

	@Override
	public Map<String, ?> getLaunchData() {
		Map<String,Object> map = new TreeMap<String,Object>();
		map.put("url", urlField.getValue().toString());
		map.put("key", keyField.getText());
		map.put("secret", secretField.getText());
		map.put("css", cssField.getText());
		map.put("btn", String.valueOf(btnBox.isSelected()));
		map.put("title", titleField.getText());
		Properties p = new Properties();
		try {
			p.load(new StringReader(customField.getText()));
		} catch (IOException e) {
		}
		map.put("custom", p);
		if(context.getProperty(Constants.UUID) == null)
			map.put(Constants.UUID, uuid); // DME does not support UUID yet
		
		return map;
	}

	@Override
	public void setLaunchData(Map<String, ?> data) {
		String url = (String) data.get("url");
		try {
			urlField.setValue(URI.create(url));
		} catch (Exception e) {
			urlField.setText("");
		}
		String key = (String) data.get("key");
		if(key != null) keyField.setText(key); else keyField.setText("");
		String secret = (String) data.get("secret");
		if(secret != null) secretField.setText(secret); else secretField.setText("");
		String css = (String) data.get("css");
		if(css != null) cssField.setText(css); else cssField.setText("");
		String title = (String) data.get("title");
		if(title != null) titleField.setText(title); else titleField.setText("");
		Properties custom = new Properties();
		if(data.containsKey("custom")) {
			custom.putAll((Map)data.get("custom"));
		} else {
			Set<String> keys = data.keySet();
			for(String k: keys) {
				if(k.startsWith("custom_"))
					custom.put(k.substring(7), data.get(k));
			}
		}
		StringWriter writer = new StringWriter();
		try {
			custom.store(writer, null);
		} catch (IOException _) {}
		StringBuffer buffer = writer.getBuffer();
		int next = 1 + buffer.indexOf("\n");
		buffer.delete(0,next);
		customField.setText(buffer.toString());
		

		String w = (String) data.get("width");
		if(w != null) size.width = Integer.parseInt(w);
		String h = (String) data.get("height");
		if(h != null) size.height = Integer.parseInt(h);
		
		Object b = data.get("btn");
		btnBox.setSelected("true".equals(b));
// More to follow...
		uuid = (String) context.getProperty(Constants.UUID);
		if( uuid == null ) uuid = (String) data.get(Constants.UUID);
		if( uuid == null ) uuid = java.util.UUID.randomUUID().toString();		
	}

	@Override
	public int getMaxScore() {
		return 0; // TODO Just LTIv1p0: no score yet!
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Override
	public void setInstanceWidth(int width) {
		size.width = width;
	}

	@Override
	public void setInstanceHeight(int height) {
		size.height = height;
	}

	@Override
	public Dimension getInstanceSize() {
		return size;
	}

	@Override
	public String[] getAcceptedCmds() {
		return null;
	}

	@Override
	public String[] getSendCmds() {
		return null;
	}

}
