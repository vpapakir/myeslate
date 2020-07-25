package start;

import java.util.Iterator;
import java.util.Locale;
import java.util.ServiceLoader;

import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import netscape.javascript.JSObject;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookService;
import org.cbook.cbookif.CBookWidgetIF;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.ServiceImpl;

import widgetsample.SampleWidget;
import cbookeditor.CBookEditor;

public class StartApplet extends JApplet implements CBookContext, Constants, CBookEventListener {

	private JTextArea area;
	private CBookService service;
	private CBookEditor editor;
	private String learner_id;
	
	@Override
	public void init() {
		JComponent.setDefaultLocale(new Locale("en"));
// request some sort of identifier.
		learner_id = JOptionPane.showInputDialog(this, "Username");
		if(learner_id == null) learner_id = "";
		else learner_id = learner_id.trim();
		
		area = new JTextArea();
		area.setRows(5);

		service = new FactoryServiceImpl(this);
		ServiceLoader<CBookWidgetIF> load = ServiceLoader.load(CBookWidgetIF.class, getClass().getClassLoader());
		Iterator<CBookWidgetIF> widgets =  load.iterator();
		while (widgets.hasNext()) {
			CBookWidgetIF widget =  widgets.next();
			service.registerWidget(widget);
		}
// This one always there.
		service.registerWidget(new SampleWidget());

		editor = createCBookEditor();
		setContentPane(editor);
		setSize(editor.getPreferredSize());
	}

	private CBookEditor createCBookEditor() {
		return new CBookEditor(new JScrollPane(area), service, this, this);
	}

	@Override
	public Object getProperty(String key) {
		if( LEARNER_ID .equals( key)) {
			return learner_id;
		}
		
		if( "AppletContext".equals(key))
			return getAppletContext();
// can throw ClassNotFoundError
		try {
			if("JSObject".equals(key))
				return JSObject.getWindow(this);
		} catch (Throwable _) {
		}
		return null;
	}

	@Override
	public void acceptCBookEvent(CBookEvent event) {
		area.append(event.getSource().getClass().getName());
		area.append(": ");
		area.append(event.getCommand());
		area.append(", ");
		area.append(String.valueOf(event.getParameters()));
		area.append("\n");
		String tekst = editor.getSuccessStatus() + " " + editor.getScore() + "/" + editor.getMaxScore();
		showStatus(tekst);		
	}

	@Override
	public void destroy() {
		showStatus("destroy");
		editor.destroy();
	}

	
}
