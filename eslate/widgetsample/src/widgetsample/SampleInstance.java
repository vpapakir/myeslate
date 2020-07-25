package widgetsample;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.SuccessStatus;

class SampleInstance extends JPanel implements CBookWidgetInstanceIF, ActionListener, CBookEventListener, Constants  {

	private JLabel question;
	private JTextField answer;
	private JButton    check;
	private String correct;
	private Number maxScore = new Integer(10);
	private Boolean checked;
	private int score, failures;
	private String initial = "";
	private CBookContext context;
	
	private CBookEventHandler handler = new CBookEventHandler(this);
	private boolean showResult = true;
	private Object logging, logid;
	private boolean aftrek;
	private AssessmentMode mode;
	
	public int getScore() {
		return Math.max(0, score - failures);
	}

	public void setScore(int score) {
		this.score = score;
	}

	public JComponent asComponent() {
		return this;
	}

	SampleInstance(CBookContext context, SampleWidget sampleWidget) {
		super(new BorderLayout());
		this.context = context;
		initialize();
		stop();
		
	}

	private void initialize() {
		setBackground(Color.white);
		question = new JLabel();
		answer   = new JTextField();
		check    = new JButton("check");
		correct = "42";
		add(question, BorderLayout.NORTH);
		add(answer, BorderLayout.CENTER);
		add(check, BorderLayout.SOUTH);
		check.addActionListener(this);
		answer.addActionListener(this);
	}


	public void setLaunchData(Map<String, ?> data, Map<String,Number> randomVars) {
		String questionStr = (String) data.get("question");
		initial     = (String) data.get("initial");
		correct     = (String) data.get("answer");
		questionStr = convert(questionStr, randomVars);
		initial = convert(initial, randomVars);
		correct = convert(correct, randomVars);
		
		maxScore    = (Number) data.get("maxScore");
		if(maxScore == null) 
			maxScore = new Integer(10);
		answer.setText(initial);
// Ask the question to the user:		
		String name = context.getProperty(LEARNER_ID) + ": ";
		question.setText(name + questionStr  + "?");

		check.setText("check");
	}

	private String convert(String str, Map<String, Number> randomVars) {
		StringBuffer sb = new StringBuffer();
		String[] x = str.split("#");
		boolean flip = false;
		for (int i = 0; i < x.length; i++) {
			if(flip) {
				Object object = randomVars.get(x[i]);
				if(object == null) object = "#" + x[i] + "#";
				sb.append(object);
			} else {
				sb.append(x[i]);
			}
			flip = !flip;
		}
		return sb.toString();
	}

	public void setState(Map<String,?> state) {
		String answer = (String) state.get("answer");		
		checked = (Boolean) state.get("checked");
		if(checked != null)
		{
			showResult = true;
			setCheckMark(checked.booleanValue());
		}
		else
		{
			check.setText("check");
			score = 0;
		}
		if(answer != null)
			this.answer.setText(answer);
		else
			this.answer.setText(initial);
	}

	private void setCheckMark(boolean correct) {
		String text = "KO";
		score = 0;
		if(correct) {
			text = "OK";
			score = maxScore.intValue();
		}
		if(showResult) 
		{
			check.setText(text);
			checked = Boolean.valueOf(correct);
		}
		else
		{
			check.setText("check");
			checked = null;
		}
	}

	public Map<String,?> getState() {
		Hashtable<String,Object> h = new Hashtable<String, Object>();
		h.put("answer", answer.getText());
		if(checked != null) 
			h.put("checked", checked);
		return h;
	}

	public void actionPerformed(ActionEvent _) {
		String input = answer.getText().trim();
		setCheckMark( correct .equals( input));
		if( aftrek && ! checked.booleanValue() )
			failures += 1;
// Single command, single message
		handler.fire(USER_INPUT, input);
		handler.fire(CHANGED);
// Do logging via an event listener
		if(isLogging())
		{	
			HashMap<String,Object> map = new HashMap<String, Object>();
			map.put(USER_INPUT, input);
			map.put("success_status", getSuccessStatus());
			map.put("score", new Integer(getScore()));
			map.put(LOG_ID, logid);
			handler.fire(LOGGING, map);
		}
	}

// boilerplate	
	public void addCBookEventListener(CBookEventListener listener,
			String command) {		
		handler.addCBookEventListener(listener, command);
	}

	public void removeCBookEventListener(CBookEventListener listener,
			String command) {
		handler.removeCBookEventListener(listener, command);
	}

	public void acceptCBookEvent(CBookEvent event) {
		String command = event.getCommand();
		if(USER_INPUT.equals(command))
		{
			answer.setText(String.valueOf(event.getMessage()));
		}
		if("check".equals(command))
		{
			// no parameters: kijkna()
			// met parameter "checked" Boolean zet nagekeken, save in state!
			Object checked = event.getParameter("checked");
			if(Boolean.TRUE.equals(checked))
				showResult = true;
			if(Boolean.FALSE.equals(checked))
				showResult = false;
			String input = answer.getText().trim();
			setCheckMark(input.equals(correct));
			handler.fire(USER_INPUT, input);
			repaint();
		}
	}

	public SuccessStatus getSuccessStatus() {
		if(checked == null)
			return SuccessStatus.UNKNOWN;
		if(checked.booleanValue())
			return SuccessStatus.PASSED;
		return SuccessStatus.FAILED;
	}

	public void setAssessmentMode(AssessmentMode mode) {
		this.mode = mode;
		showResult = 
				mode == AssessmentMode.OEFENEN ||
				mode == AssessmentMode.OEFENEN_STRAFPUNTEN;
		aftrek = mode == AssessmentMode.OEFENEN_STRAFPUNTEN;
	}

	public void init() {
		logging = context.getProperty(LOGGING);
		logid = context.getProperty(LOG_ID);
		if(isLogging())
			System.err.println("log to " + logid);
		Color foreground = (Color)context.getProperty(FOREGROUND);
		setForeground(foreground);
		Color background = (Color)context.getProperty(BACKGROUND);
		setBackground(background);
		Font font = (Font) context.getProperty(FONT);
		setFont(font);
	}

	private boolean isLogging() {
		return Boolean.TRUE.equals(logging);
	}

	public void start() {
		check.setEnabled(true);
		answer.setEnabled(true);
	}

	public void stop() {
		check.setEnabled(false);
		answer.setEnabled(false);
	}

	public void destroy() {
	}

	public void reset() {
		setAssessmentMode(mode);
		setState(Collections.<String, String> emptyMap());
		failures = 0;
	}

	public CBookEventListener asEventListener() {
		return this;
	}

}
