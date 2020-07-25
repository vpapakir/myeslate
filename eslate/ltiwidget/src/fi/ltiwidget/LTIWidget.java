package fi.ltiwidget;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookLaunchData;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.CBookWidgetFactoryIF;
import org.cbook.cbookif.CBookWidgetInstanceIF;

import fi.ltiwidget.impl.SampleLaunchData;

public class LTIWidget implements CBookWidgetFactoryIF {

	@Override
	public CBookWidgetInstanceIF getInstance(CBookContext context) {
		return new LTIInstance(context, getIcon());
	}

	@Override
	public CBookWidgetEditIF getEditor(CBookContext context) {
		return new LTIEditor(context);
	}

	@Override
	public Icon getIcon() {
		URL resource = getClass().getResource("resources/imsglobal.jpg");
		if(resource != null) return new ImageIcon(resource);
		return null;
	}

	@Override
	public Collection<CBookLaunchData> getInitialLaunchData(CBookContext context) {
		CBookLaunchData data = new LTILaunchData();
		ArrayList<CBookLaunchData> list = new ArrayList<CBookLaunchData>(2);
		Locale locale = JComponent.getDefaultLocale();

		list.add(new SampleLaunchData( "fi.ltiwidget.resources.1", locale));
		list.add(new SampleLaunchData( "fi.ltiwidget.resources.2", locale));
		list.add(data);
		return Collections.unmodifiableCollection(list);
	}

	public String toString() {
		return "LTI Widget";
	}
	
}
