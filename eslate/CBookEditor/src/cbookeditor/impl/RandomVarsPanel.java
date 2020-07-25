package cbookeditor.impl;

import java.awt.Color;
import java.awt.Component;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class RandomVarsPanel extends JTextArea {

	String text;
	public RandomVarsPanel() {
		setRows(4);
		setColumns(10);
		text = "a=5\n";
		this.setText(text);
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	public Map getMap() {
		Map<String,Number> result = new LinkedHashMap<String, Number>();
		String[] lines = text.split("\n");
		for (int i = 0; i < lines.length; i++) {
			try {
				String line = lines[i];
				int n = line.indexOf('=');
				String var = line.substring(0, n).trim();
				String str = line.substring(n+1).trim();
				Number value;
				if ( str.indexOf('.')>=0 )  
					value = new Double(str);
				else value = new Long(str);
				result.put(var, value);
			} catch (Exception e) {
			}
		}
		return result;
	}
	
	public void showDialog(Component parent) {
		setText(text);
		int r = JOptionPane.showConfirmDialog(parent, this, "Random variables", JOptionPane.OK_CANCEL_OPTION);
		if(r == JOptionPane.OK_OPTION)
			text = getText();
	}
	
	public String toString() {
		return getText();
	}
	public static void main(String[] args )
	{
		RandomVarsPanel v = new RandomVarsPanel();
		v.showDialog(null);
		System.out.println(v.getMap());
	}
}
