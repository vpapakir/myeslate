package gr.cti.eslate.base.help;

import javax.help.event.*;

import gr.cti.eslate.utils.browser.*;

   /*
    * =====================================
    * Tracking events from the navigators
    * =====================================
    *
    * When something changes at the navigators(TOC, index, search)
    * we get the correct new referenced page and pass to the browser
    * to show it.
    *
    */
class UpdateHistory implements HelpModelListener{
        private ESlateBrowser browser;
        private boolean firstTime;

        public UpdateHistory(ESlateBrowser b) {
                browser=b;
                firstTime=true;
        }

    public void idChanged(HelpModelEvent e) {
                if (firstTime&&browser.getCurrentLocation().equals(HelpSystemViewer.startingPage.toString())) {
                HelpSystemViewer.backHistory.pop();
                }else {
                                HelpSystemViewer.leftButton.setEnabled(true);
                }
                HelpSystemViewer.backHistory.push(browser.getCurrentLocation());
                firstTime=false;
        }
}
