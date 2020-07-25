package widgetsample;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.rm.*;

class SampleEditor extends JPanel implements CBookWidgetEditIF, ItemListener {

	JTextField question;
	JTextField initial;
	JTextField answer;
	JTextField maxScore;
	
	JComboBox  imageChooser;
	JLabel     imageDisplay;
	
	ResourceManager rm;
	Resource[] images;
	
	public JComponent asComponent() {
		return this;
	}

	public Map<String, ?> getLaunchData() {
		Hashtable<String,Object> h = new Hashtable<String, Object>();
		h.put("question", question.getText());
		h.put("initial", initial.getText());
		h.put("answer", answer.getText());
		h.put("maxScore", new Integer(maxScore.getText()));
		if(imageChooser != null)
		{
			Resource r = (Resource) imageChooser.getSelectedItem();
			String name = r == null ? null : r.getName();
			try {
				for (Resource old : rm.getInstanceContainer().list())
				{
					old.remove();
				}
				if(r != null) 
					rm.getInstanceContainer().create(name, r);
			} catch (ResourceException e) {
				e.printStackTrace();
			}
		}
		return h;
	}
	
	public int getMaxScore() {
		return Integer.parseInt(maxScore.getText());
	}

	public void setLaunchData(Map<String, ?> data) {
		question.setText(String.valueOf(data.get("question")));
		initial.setText(String.valueOf(data.get("initial")));
		answer.setText(String.valueOf(data.get("answer")));
		maxScore.setText(String.valueOf(data.get("maxScore")));
		if( imageChooser != null) {
			try {
				Resource[] r = rm.getInstanceContainer().list();
				if(r.length > 0) {
					String name = r[0].getName();
					int cnt = imageChooser.getItemCount();
					for(int i = 0; i < cnt; i++ ) {
						if( name.equals(((Resource) imageChooser.getItemAt(i)).getName()) )
						{
							imageChooser.setSelectedIndex(i);
							break;
						}
					}
				}
			} catch (ResourceException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	SampleEditor(CBookContext context, SampleWidget sampleWidget) {
		super();
		question = new JTextField("question"); add(question);
		initial = new JTextField("initial");   add(initial);
		answer  = new JTextField("answer");    add(answer);
		maxScore = new JTextField("10");	   add(maxScore);
		setBackground(Color.white);
		rm = (ResourceManager) context.getProperty(Constants.RESOURCE_MANAGER);
		if(rm != null) {
			try {
				images = rm.getWidgetContainer().openContainer(rm.IMAGES).list();
				imageDisplay = new JLabel();
				imageDisplay.setPreferredSize(new Dimension(400,400));
				imageChooser = new JComboBox(images);
				imageChooser.setSelectedIndex(-1);
				imageChooser.addItemListener(this);
				add(imageChooser);
				add(imageDisplay);
			} catch (ResourceException e) {
				e.printStackTrace();
			}
			
			
		}
		
		
	}

	
	public String[] getAcceptedCmds() {
		return new String[] { SampleInstance.USER_INPUT, "check" };
	}

	public String[] getSendCmds() {
		return new String[] { SampleInstance.USER_INPUT, SampleInstance.LOGGING };
	}

	public void start() {
		// TODO Auto-generated method stub
	}

	public void stop() {
		// TODO Auto-generated method stub		
	}

	private Dimension instanceSize = new Dimension(200,100);
	
	public void setInstanceWidth(int width) {
		instanceSize.width = width;
	}

	public void setInstanceHeight(int height) {
		instanceSize.height = height;
	}

	public Dimension getInstanceSize() {
		return instanceSize;
	}

	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == e.SELECTED)
		{
			Resource r = (Resource) e.getItem();
			Icon icon;
			try {
				icon = new ImageIcon( getToolkit().getImage(r.getURL()));
				imageDisplay.setIcon(icon);
				imageDisplay.repaint();
			} catch (ResourceException e1) {
				e1.printStackTrace();
			}
			
		}
		
	}

}
