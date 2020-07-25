package gr.cti.eslate.base.container;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.cbook.cbookif.CBookContext;


public class ESlateContainerFrame extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2090876175747888342L;
	public static final String USER_INPUT = null;
	public static final String LOGGING = null;
	ESlateContainer container;
    boolean frameWasResized = false;
    int times = 0;
    String testMicroworldFileName = null;
//    JScrollPane scrollPane;
    long start = 0;

    public ESlateContainerFrame(String[] args, String testMicroworldFileName, int times) {
        super();
        this.testMicroworldFileName = testMicroworldFileName;
        this.times = times;
        start = System.currentTimeMillis();

        for (int i = 0; i < args.length; i++) {
            System.out.println("arg:" + args[i]);

        }
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //new ESlateLookAndFeel());
        } catch (Exception e) {
        }
        setIconImage(ESlateContainer.ESLATE_LOGO.getImage());

        initializeContainer();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(container, BorderLayout.CENTER);
/*\
        getRootPane().registerKeyboardAction(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                container.setMenuBarVisible(!container.isMenuBarVisible());
//                System.out.println("ALT + Z pressed ");
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.ALT_MASK, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
*/
        getRootPane().registerKeyboardAction(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispatchEvent(new WindowEvent(ESlateContainerFrame.this, WindowEvent.WINDOW_CLOSING));
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
    
	public ESlateContainerFrame(CBookContext context2,
			ESlateContainer eSlateContainer) {
		// TODO Auto-generated constructor stub
	}

	protected void initializeContainer() {
        container = new ESlateContainer();
        container.initialize();
    }

    protected void processComponentEvent(ComponentEvent e) {
        if (e.getID() == ComponentEvent.COMPONENT_RESIZED)
            frameWasResized = true;

        super.processComponentEvent(e);
    }

    protected void processWindowEvent(WindowEvent e) {
//        System.out.println("ESlateContainerFrame processWindowEvent()");
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
/*            super.processWindowEvent(e);
            if (e.getID() == WindowEvent.WINDOW_OPENED) {
                frameWasResized = false;
System.out.println("ESlateContainer startup time: " + (System.currentTimeMillis()-start));
                PerformanceManager pm = PerformanceManager.getPerformanceManager();
                pm.stop(ESlateContainer.startupTimer);
                pm.displayElapsedTime(ESlateContainer.startupTimer, "", "ms");
                pm.removeGlobalPerformanceTimerGroup(ESlateContainer.startupTimer);
                ESlateContainer.startupTimer = null;
            }
            if (e.getID() == WindowEvent.WINDOW_OPENED) {
                if (testMicroworldFileName != null) {
                    loadTestMicroworld(testMicroworldFileName, times);
                }
            }
        }else{
*/            boolean needsSave = container.microworldChanged;
//\            container.setMicroworldChanged(true);
            int k = container.promptToSaveCurrentMicroworld();
//            System.out.println("k: " + k);

            if (k == container.CANCEL) {
                container.setMicroworldChanged(needsSave);
                return;
            }
            if (k == container.SAVE) {
                if (!container.saveMicroworld(true)) {
                    container.setMicroworldChanged(needsSave);
                    return;
                }
            }
//            System.out.println("Calling closeMicroworld(false)");
            container.closeMicroworld(false);
//            if (frameWasResized)
            System.out.println("Calling System.exit()");
            container.exit(0);
//            System.exit(0);
        }
    }

    public ESlateContainer getESlateContainer() {
        return container;
    }

    public void loadTestMicroworld(final String fileName, final int times) {
        try{
        if (times == 0) {
            System.out.println("Microworld " + testMicroworldFileName + " successfully run all times");
            return;
        }
        container.consoles.showConsole(container);
        container.consoles.bringToFront();
        container.consoles.clearAction();
        container.loadLocalMicroworld(fileName, true, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        try{
                container.paintImmediately(container.getVisibleRect());
                try{
                    Thread.currentThread().sleep(5000);
                }catch (Throwable thr) {thr.printStackTrace();}
                container.closeMicroworld(false);
                container.containerUtils.fireMwdChanged(new gr.cti.eslate.base.container.event.MwdChangedEvent(this));
                container.paintImmediately(container.getVisibleRect());
                if (times != 0)
                    loadTestMicroworld(fileName, times-1);
        }catch (Throwable thr) {
            thr.printStackTrace();
            System.out.println("2. Error in the " + times + "th run of the microworld");
        }
            }
        });
        }catch (Throwable thr) {
            thr.printStackTrace();
            System.out.println("Error in the " + times + "th run of the microworld");
        }
    }

	

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public JComponent asComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	

	
	
}



