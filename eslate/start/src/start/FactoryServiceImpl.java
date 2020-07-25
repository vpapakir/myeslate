package start;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookLaunchData;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.CBookWidgetFactoryIF;
import org.cbook.cbookif.CBookWidgetIF;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.ServiceImpl;

import cbookeditor.Wrapper;

public class FactoryServiceImpl extends ServiceImpl {

	public static class Wrap implements CBookWidgetIF, Wrapper {

		private CBookWidgetIF widget;
		private CBookLaunchData data;

		public Wrap(CBookWidgetIF widget, CBookLaunchData data) {
			this.widget = widget;
			this.data = data;
		}

		@Override
		public CBookWidgetInstanceIF getInstance(CBookContext context) {
			return widget.getInstance(context);
		}

		@Override
		public CBookWidgetEditIF getEditor(CBookContext context) {
			CBookWidgetEditIF editor = widget.getEditor(context);
			editor.setLaunchData(data.getLaunchData());
			return editor;
		}

		@Override
		public Icon getIcon() {
			return widget.getIcon();
		}

		public String toString() { 
			return data.toString();
		}
		
		public String getClassName() { 
			return widget.getClass().getName();
		}
	}

	CBookContext context;
	Map<String,CBookWidgetIF> widgets = new HashMap<String,CBookWidgetIF>();
	
	
	public FactoryServiceImpl(CBookContext context) {
		super();
		this.context = context;
	}

	@Override
	public void registerWidget(CBookWidgetIF widget) {
		widgets.put(widget.getClass().getName(), widget);
		if(widget instanceof CBookWidgetFactoryIF)
		{
			CBookWidgetFactoryIF factory = (CBookWidgetFactoryIF) widget;
			Collection<CBookLaunchData> list = factory.getInitialLaunchData(context);
			for (CBookLaunchData data : list) {
				super.registerWidget( wrap( widget, data));
			}
			
		} else
			super.registerWidget(widget);
	}

	private CBookWidgetIF wrap(CBookWidgetIF widget, CBookLaunchData data) {
		return new Wrap(widget, data);
	}

	@Override
	public CBookWidgetIF widgetForName(String clazz) {
		return widgets.get(clazz);
	}

}
