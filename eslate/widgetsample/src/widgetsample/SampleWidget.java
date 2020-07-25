package widgetsample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookLaunchData;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.CBookWidgetFactoryIF;
import org.cbook.cbookif.CBookWidgetIF;
import org.cbook.cbookif.CBookWidgetInstanceIF;

public class SampleWidget implements CBookWidgetFactoryIF {

	private static final Icon ICON = new SampleIcon();
	ResourceBundle rb;
	
	public CBookWidgetInstanceIF getInstance(CBookContext context) {
		return new SampleInstance(context, this);
	}

	public CBookWidgetEditIF getEditor(CBookContext context) {
		return new SampleEditor(context, this);
	}

	public String toString() {
		return rb.getString("widget-name");
	}


	public SampleWidget(Locale locale) {
		super();
		this.rb = ResourceBundle.getBundle("widgetsample.text.Text", locale);
	}
	
	public SampleWidget() {
		this(JComponent.getDefaultLocale());
	}

	public Icon getIcon() {
		return ICON;
	}

	public Collection<CBookLaunchData> getInitialLaunchData(CBookContext context) {
		ArrayList<SampleLaunchData> list = new ArrayList<SampleLaunchData>(2);
		Locale locale = JComponent.getDefaultLocale();

		list.add(new SampleLaunchData( "widgetsample.resources.1", locale));
		list.add(new SampleLaunchData( "widgetsample.resources.2", locale));

		return Collections.<CBookLaunchData>unmodifiableCollection(list);
	}
	
	
 }
