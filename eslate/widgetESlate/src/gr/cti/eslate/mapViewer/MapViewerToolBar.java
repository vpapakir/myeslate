package gr.cti.eslate.mapViewer;

import gr.cti.eslate.eslateToolBar.ESlateToolBar;
import gr.cti.eslate.utils.ESlateFieldMap2;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Only adds a setOrientation method that adjusts ZoomSlider accordingly. For some strange reason
 * (Java bug, JBuilder bug?) I could not make it an inner class of MapViewer.
 */

public class MapViewerToolBar extends ESlateToolBar implements Externalizable {
	public MapViewerToolBar() {
		super();
	}

	public MapViewerToolBar(MapViewer viewer,int orientation) {
		super(orientation);
		this.viewer=viewer;
	}

	public void setOrientation(int orientation) {
		if (viewer!=null && viewer.zoom!=null)
			viewer.zoom.setOrientation(orientation);
		super.setOrientation(orientation);
	}

	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		super.readExternal(in);
		try {
			ESlateFieldMap2 ht=(ESlateFieldMap2) in.readObject();
			setVisible(ht.get("visible",true));
		} catch(Exception ex) {}
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("visible",isVisible());
		out.writeObject(ht);
	}

// GT -start
 /**
   * This method creates and adds a PerformanceListener to the E-Slate's
   * Performance Manager. The PerformanceListener attaches the component's
   * timers when the Performance Manager becomes enabled.
   */
/*  private void createPerformanceManagerListener(PerformanceManager pm)
  {
	if (perfListener == null) {
	  perfListener = new PerformanceAdapter() {
		public void performanceManagerStateChanged(PropertyChangeEvent e)
		{
		  boolean enabled = ((Boolean) e.getNewValue()).booleanValue();
		  // When the Performance Manager is enabled, try to attach the
		  // timers.
		  if (enabled) {
			attachTimers();
		  }
		}
	  };
	  pm.addPerformanceListener(perfListener);
	}
  }
*/
  /**
   * This method creates and attaches the component's timers. The timers are
   * created only once and are assigned to global variables. If the timers
   * have been already created, they are not re-created. If the timers have
   * been already attached, they are not attached again.
   * This method does not create any timers while the PerformanceManager is
   * disabled.
   */
/*  private void attachTimers(){
	PerformanceManager pm = PerformanceManager.getPerformanceManager();
	boolean pmEnabled = pm.isEnabled();

	// If the performance manager is disabled, install a listener which will
	// re-invoke this method when the performance manager is enabled.
	if (!pmEnabled && (perfListener == null)) {
	  createPerformanceManagerListener(pm);
	}

	// Do nothing if the PerformanceManager is disabled.
	if (!pmEnabled) {
	  return;
	}

	boolean timersCreated = (loadTimer != null);
	// If the timers have already been constructed and attached, there is
	// nothing to do.
	if (!timersCreated) {
		// Get the performance timer group for this component.
	  PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(this);
	  // Construct and attach the component's timers.
	  constructorTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
		compoTimerGroup, viewer.messagesBundle.getString("ConstructorTimer"), true
	  );
	  loadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
		compoTimerGroup, viewer.messagesBundle.getString("LoadTimer"), true
	  );
	  saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
		compoTimerGroup, viewer.messagesBundle.getString("SaveTimer"), true
	  );
	  initESlateAspectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
		compoTimerGroup, viewer.messagesBundle.getString("InitESlateAspectTimer"), true
	  );
	  pm.registerPerformanceTimerGroup(
		PerformanceManager.CONSTRUCTOR, constructorTimer, this
	  );
	  pm.registerPerformanceTimerGroup(
		PerformanceManager.LOAD_STATE, loadTimer, this
	  );
	  pm.registerPerformanceTimerGroup(
		PerformanceManager.SAVE_STATE, saveTimer, this
	  );
	  pm.registerPerformanceTimerGroup(
		PerformanceManager.INIT_ESLATE_ASPECT, initESlateAspectTimer, this
	  );
	}
  }

	PerformanceTimer constructorTimer, loadTimer, saveTimer, initESlateAspectTimer;
	PerformanceListener perfListener;
// GT -end
*/
	MapViewer viewer;
}