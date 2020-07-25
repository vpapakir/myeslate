package widgetESlate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JComponent;
import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookLaunchData;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.CBookWidgetFactoryIF;
import org.cbook.cbookif.CBookWidgetInstanceIF;

public class ESlateWidget implements CBookWidgetFactoryIF {

	private static final Icon ICON = new ESlateIcon();
	ResourceBundle rb;
	private ESlateInstance esi;
	private ESlateEditor ese;
	
	public CBookWidgetInstanceIF getInstance(CBookContext context) {
		this.esi = new ESlateInstance(context, this);
		return esi;
	}

	public CBookWidgetEditIF getEditor(CBookContext context) {
		this.ese = new ESlateEditor(context, this); 
		return ese;
	}

	public String toString() {
		return rb.getString("widget-name");
	}


	public ESlateWidget(Locale locale) {
		super();
		this.rb = ResourceBundle.getBundle("widgetESlate.text.Text", locale);
	}
	
	public ESlateWidget() {
		this(JComponent.getDefaultLocale());
	}

	public Icon getIcon() {
		return ICON;
	}

	public Collection<CBookLaunchData> getInitialLaunchData(CBookContext context) {
		ArrayList<ESlateLaunchData> list = new ArrayList<ESlateLaunchData>(5);
		Locale locale = JComponent.getDefaultLocale();
		list.add(new ESlateLaunchData( "widgetESlate.resources.1", locale));
		list.add(new ESlateLaunchData( "widgetESlate.resources.2", locale));
		list.add(new ESlateLaunchData( "widgetESlate.resources.3", locale));
		list.add(new ESlateLaunchData( "widgetESlate.resources.4", locale));
		//list.add(new ESlateLaunchData( "widgetESlate.resources.5", locale));
		list.add(new ESlateLaunchData( "widgetESlate.resources.6", locale));
		return Collections.<CBookLaunchData>unmodifiableCollection(list);
	}

 }
