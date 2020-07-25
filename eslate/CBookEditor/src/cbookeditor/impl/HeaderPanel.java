package cbookeditor.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.Constants;

public class HeaderPanel extends JPanel implements Constants {

	CBookWidgetEditIF editor;
	HashMap map;
	private JCheckBox log;
	private JTextField logid;
	public HeaderPanel(CBookWidgetEditIF editor, HashMap<String, Object> map) {
		this.editor = editor;
		this.map = map;
		log = new JCheckBox("Logid");
		logid = new JTextField();
		logid.setColumns(10);
		String text = (String)map.get(LOG_ID);
		if(text == null) text = "";
		logid.setText(text);
		log.setSelected(Boolean.TRUE.equals(map.get(LOGGING)));
		add(log);
		add(logid);
	}
	
	public void updateMap() {
		map.put(LOG_ID, logid.getText());
		map.put(LOGGING, log.isSelected());
	}

}
