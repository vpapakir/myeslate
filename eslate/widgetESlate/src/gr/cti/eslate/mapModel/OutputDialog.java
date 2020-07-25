package gr.cti.eslate.mapModel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * A dialog that displays errors and warnings when creating an esm file.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 14-Feb-2000
 */
public class OutputDialog extends JDialog {

    public OutputDialog(MapCreator creator) {
        super(creator);
        this.creator=creator;
        getContentPane().setLayout(new BorderLayout());
        setCursor(Helpers.waitCursor);
        setSize(650,350);
        setModal(true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        area=new JTextArea();
        area.setLineWrap(true);
        area.setEditable(false);
        scroll=new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setView(area);
        console=new Console(area);
        getContentPane().add(scroll,BorderLayout.CENTER);
        accept=new JButton(MapCreator.bundleCreator.getString("closeandexit"));
        accept.setEnabled(false);
        accept.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                OutputDialog.this.creator.dispose();
            }
        });
        reject=new JButton(MapCreator.bundleCreator.getString("returntocreator"));
        reject.setEnabled(false);
        reject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel panel=new JPanel();
        panel.add(accept);
        panel.add(reject);
        getContentPane().add(panel,BorderLayout.SOUTH);
    }

    public void show() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                compile();
            }
        });
        super.show();
    }

    public void setVisible(boolean value) {
        super.setVisible(value);
        if (value) compile();
    }

    public Console getConsole() {
        return console;
    }

    private void compile() {
        int result=creator.checkIntegrity(console);
        if (result==MapCreator.NO_ERRORS)
            console.append(MapCreator.bundleCreator.getString("noerrors"));
        else if (result==MapCreator.ERRORS_IN_MAP) {
            console.append(" ");
            console.append(MapCreator.bundleCreator.getString("errors"));
        } else if (result==MapCreator.WARNINGS_IN_MAP) {
            console.append(" ");
            console.append(MapCreator.bundleCreator.getString("warnings"));
        }
        accept.setEnabled(true);
        reject.setEnabled(true);
        setCursor(Helpers.normalCursor);
    }

    class Console {
        private Console(JTextArea area) {
            this.area=area;
        }

        protected void append(String s) {
            area.append(s);
            area.append("\n");
        }

        JTextArea area;
    }

    private JScrollPane scroll;
    private JTextArea area;
    private JButton accept,reject;
    private Console console;
    private MapCreator creator;
}