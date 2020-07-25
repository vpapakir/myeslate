package fi.ltiwidget;

import java.applet.AppletContext;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import netscape.javascript.JSObject;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.SuccessStatus;

import fi.ltiwidget.impl.SimpleSwingBrowser;
import fi.ltiwidget.impl.URIBuilder;

public class LTIInstance extends AbstractAction implements
		CBookWidgetInstanceIF, Constants {

	private String uuid = java.util.UUID.randomUUID().toString();
	private URI uri;
	private CBookContext context;
	private JButton btn;
	private SimpleSwingBrowser browser;
	private boolean useBrowser;
	
	private JPanel container = new JPanel(new BorderLayout());

	private static final URIBuilder builder = new URIBuilder(URI.create("http://mc2dme.appspot.com/lti/blti.jsp"));
	
	public LTIInstance(CBookContext context, Icon icon) {
		super("Open URL", icon);
		this.context = context;
		btn = new JButton(this);
		btn.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn.setHorizontalAlignment(SwingConstants.CENTER);
		browser = new SimpleSwingBrowser();
	}

	@Override
	public void actionPerformed(ActionEvent _) {
		try {
			JSObject jso = (JSObject) context.getProperty("JSObject");
			if(jso != null)
			{		
				Object[] args = new Object[5];
	        	args[0] = uri.toString();
	        	args[1] = uuid;
				args[2] = Integer.toString(btn.getWidth());
	        	args[3] = Integer.toString(btn.getHeight());
	        	args[4] = "yes";

				Object result = jso.call("NewPopUp", args);
				return;
			}
			AppletContext ctx = (AppletContext) context.getProperty("AppletContext");
			if(ctx != null)
			{
				ctx.showDocument(uri.toURL(), uuid);
				return;
			}
			Desktop.getDesktop().browse(uri);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public JComponent asComponent() {		
		container.setBackground((Color) context.getProperty(BACKGROUND));
		container.setForeground((Color) context.getProperty(FOREGROUND));
		return container;
	}

	@Override
	public void setLaunchData(Map<String, ?> data,
			Map<String, Number> randomValues) {
		String url = (String) data.get("url");
		if(url == null) url = "about:blank";
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("endpoint", url);
		putValue(NAME, url);
		map.put("key", (String) data.get("key"));
		map.put("secret", (String) data.get("secret"));
		map.put("launch_presentation_css_url", (String) data.get("css"));
		map.put("resource_link_title", (String)data.get("title"));
		Map<String,String> custom = (Map<String, String>) data.get("custom");
		StringBuilder sb = new StringBuilder();
		if(custom != null)
			for(Map.Entry<String,String> entry: custom.entrySet()) {
				if(entry.getValue().length() > 0)
					sb.append(entry.getKey())
						.append('=')
						.append(entry.getValue())
						.append('\n');
			}
		if(sb.length() > 0)
			map.put("custom", sb.toString());
		
// from context
		map.put("user_id", (String) context.getProperty(LEARNER_ID));
		String learner_name = (String) context.getProperty(LEARNER_NAME);
		String surname = "";
		String given = "";
		int index = -1;
		if(learner_name != null) 
			index = learner_name.lastIndexOf(',');
		
		if(index >= 0) {
			given = learner_name.substring(index+1).trim();
			surname = learner_name.substring(0, index).trim();
			learner_name = given + " " + surname;
		}
		map.put("lis_person_name_given", given);
		map.put("lis_person_name_family", surname);
		map.put("lis_person_name_full", learner_name);

		map.put("launch_presentation_locale", JComponent.getDefaultLocale().toString());

		String cuuid = (String) context.getProperty(UUID);
		if( cuuid != null) uuid = cuuid;
		else {
			cuuid = (String) data.get(UUID);
			if( cuuid != null) uuid = cuuid;
		}
		
		map.put("resource_link_id", uuid);
		map.put("roles", "Learner"); // Or Instructor
		Dimension size = (Dimension) context.getProperty("instanceSize");
		if(size != null)
		{
			map.put("launch_presentation_width", Integer.toString(size.width));
			map.put("launch_presentation_height", Integer.toString(size.height));
		}
		
		useBrowser = "true".equals( data.get("btn"));
		uri = builder.getRequestURI(map);
		container.removeAll();
		if(useBrowser)
			container.add(btn, BorderLayout.CENTER);
		else
			container.add(browser, BorderLayout.CENTER);
	}

	@Override
	public void setAssessmentMode(AssessmentMode mode) {
	}

	@Override
	public void setState(Map<String, ?> state) {
	}

	@Override
	public Map<String, ?> getState() {
		return Collections.EMPTY_MAP;
	}

	@Override
	public int getScore() {
		return 0; // TODO
	}

	@Override
	public SuccessStatus getSuccessStatus() {
		return SuccessStatus.PASSED;
	}

	@Override
	public void addCBookEventListener(CBookEventListener listener,
			String command) {
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener,
			String command) {
	}

	@Override
	public void init() {
		if(!useBrowser) {
			browser.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		}
	}

	@Override
	public void start() {
		if(!useBrowser)
		{	
			browser.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			browser.loadURL(uri.toString());
			System.out.println("start" + browser);
		}
	}

	@Override
	public void stop() {
		init();
	}

	@Override
	public void destroy() {
	}

	@Override
	public void reset() {
	}

	@Override
	public CBookEventListener asEventListener() {
		return null;
	}

}
