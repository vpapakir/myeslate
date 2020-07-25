package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ProgressBarInterface;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Timer;

/** This class represents the task which shows the WaitDialog for a microworld which is
 *  being eithr load or save. The task is executed through a Timer contained in this class.
 *  The time for which the task is scheduled is specified by the user of the class.
 *  In the case of microworld loading the scheduled time is not totally respected. The
 *  WaitDialog won't appear at the specified time, if the WaitDialog's info (which is stored
 *  in the class Microworld) has not yet been read. The Timer will fire the task, but if the
 *  info has not been read, the execution stops and is repeated, when the info becomes available.
 */
class WaitDialogTimerTask extends java.util.TimerTask implements ProgressBarInterface {
    WaitDialog dialog = null;
    ESlateContainer container;
    int progress = 0;
    String message = " ";
    boolean loading = true;
    int minimum = 0;
    int maximum = 100;
    boolean isWebDialog;
    boolean progressInfoDisplayed = true;
    Font dialogTitleFont = null;
    Color dialogTitleColor = null;
    String dialogTitle = null;
    Timer timer;
    /** Flag which indicates that the task started execution, as scheduled, but the execution
     *  was halted cause the WaitDialog info hadn't been read.
     */
    boolean taskRunWasPostponed = false;
    /** Flag which indicates whether the microworld's progress dialog info has been read, for
     *  the microworld which is being loaded. The flag is not used in the case that the microworld
     *  is being saved.
     */
    boolean microworldProgressDialogInfoRead = false;

    public WaitDialogTimerTask(ESlateContainer container, boolean loading, boolean constructNow) {
        this.container = container;
        this.loading = loading;
        timer = new Timer();
        /* 'constructNow' is used to specify that the WaitDialog should be displayed
         * immediately and not after some time interval. This can be specified by sceduling
         * the TimerTask to a Date, before the current time. This works fine, as long as the
         * WaitDialog does not need to be displayed during E-Slate start-up. In this case
         * the WaitDialog does not appear at all. Probably this has to do with the fact that
         * the TimerStack runs on a thread different from the main tread. To overcome this
         * problem, I have enchanced the WaitDialogTimerTask's constructor to include the
         * 'constructNow' variable, which causes the WaitDialog to be constructed on the main thread
         * (the thread on which the constructor is being called).
         */
        isWebDialog=false;
        if (constructNow)
            constructWaitDialog();
    }

    public WaitDialogTimerTask(ESlateContainer container, boolean loading, boolean constructNow, WaitDialog dialog) {
        this.dialog=dialog;
        this.container = container;
        this.loading = loading;
        isWebDialog=true;
        if (constructNow)
            showWebWaitDialog();
    }

    synchronized WaitDialog constructWaitDialog() {
        if (dialog != null) {
            System.out.println("Dialog already constructed");
            return null;
        }
////nikosM new
        dialog = new WaitDialog(false, loading, false, dialogTitle, progressInfoDisplayed);
////nikosM new end
        dialog.setMessage(message);
        dialog.setLocalProgress(progress);
        dialog.setTitleColor(dialogTitleColor);
        dialog.setTitleFont(dialogTitleFont);
//        dialog.setSize(250, 50);
        Component componentToCenterAround = container;
        if (!container.isShowing()) componentToCenterAround = null;

        ESlateContainerUtils.showDialog(dialog, componentToCenterAround, true);
//        dialog.repaint();
        dialog.paintImmediately();
        return dialog;
    }
//nikosM
    synchronized WaitDialog showWebWaitDialog() {
        if (dialog == null) {
            System.out.println("Dialog not constructed");
            return null;
        }
        ESlateContainerUtils.showDialog(dialog, container, true);
        dialog.repaint();
        container.paintImmediately(container.getVisibleRect());
        dialog.paintImmediately();
        return dialog;
    }

//nikosM end
    public void run() {
        /* To dispay the WaitDialog, its configuration(appearance) must have been read.
         * The configuration of the dialog is saved in class gr.cti.eslate.base.container.Microworld.
         * When this class is read -while a microworld is being loaded- the
         * 'microworldProgressDialogInfoRead' becomes true. If this flag is not set, the run() method
         * exits immediately and is executed againg, when the flag is set.
         * See 'setMicroworldProgressDialogInfoRead'.
         */
        if (container.loadingMwd && !microworldProgressDialogInfoRead) {
            taskRunWasPostponed = true;
            return;
        }
        if (isWebDialog)
            showWebWaitDialog();
        else
          if (dialog == null)
            constructWaitDialog();
    }

    public void setProgress(int value) {
        progress = value;
        if (dialog != null)
////nikosM
//          if (isWebDialog)
            dialog.setLocalProgress(dialog.getLocalMaximum()*value/100-1);
//          else
//            dialog.setProgress(value);
////nikosM end
    }

    public int getProgress() {
        return progress;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMessage(String msg) {
        if (msg == null) msg = " ";
        message = msg;
        if (dialog != null)
            dialog.setMessage(message);
    }

    public void setDialogTitle(String title) {
        this.dialogTitle = title;
        if (dialog != null)
            dialog.setTitle(title);
    }

    public void setProgressInfoDisplayed(boolean displayed) {
        this.progressInfoDisplayed = displayed;
        if (dialog != null)
            dialog.setProgressInfoDisplayed(displayed);
    }

    public void setDialogTitleFont(Font f) {
        dialogTitleFont = f;
        if (dialog != null)
            dialog.setTitleFont(f);
    }

    public void setDialogTitleColor(Color c) {
        dialogTitleColor = c;
        if (dialog != null)
            dialog.setTitleColor(c);
    }

    synchronized public void disposeDialog() {
//        System.out.println("disposeDialog() dialog: " + dialog);
        if (dialog != null) {
//            System.out.println("disposeDialog() trying to dispose()");
            /* Some peculiar Swing or Java2D bug causes the application to
             * stop executing (without crashing) if the waitDialog gets
             * disposed, during the ESlateContainer's start-up. The bug
             * is avoided if we hide the waitDialog, insted of disposing it.
             * However this leaves the first WaitDialog undisposed in each
             * E-Slate session which is initiated by double-clicking a .mwd archieve
             * (this is the only case in which a WaitDialog is displayed during the
             * E-Slate start-up).
             */
            if (container.isContainerInitializing)
                dialog.setVisible(false);
            else
                dialog.dispose();
//            System.out.println("Dialog was disposed");
            dialog = null;
        }
        cancel();
    }

    /** This method updates the 'microworldProgressDialogInfoRead' flag, which indicates that
     *  the info regarding the appearance(configuration) of the WaitDialog has been read.
     *  If the
     */
    void setMicroworldProgressDialogInfoRead(boolean read) {
        if (!microworldProgressDialogInfoRead && read) {
            microworldProgressDialogInfoRead = read;
            if (taskRunWasPostponed) {
//                System.out.println("Executing WaitDialogTimerTask manually");
                run();
            }
        }
    }
}

