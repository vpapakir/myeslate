package gr.cti.eslate.database;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.EmptyBorder;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class ColorPanel extends JPanel {
    JButton chooseColor;
    JCheckBox onlyActiveTable;
//    Font panelFont = new Font("Helvetica", Font.PLAIN, 10);
    ColorBoxChooser colorBoxChooser;

    public ColorPanel(final Database database, Color activeColor) {
        super(true);
//        Dimension d = new Dimension(133, 190);
//        setMinimumSize(d);
//        setMaximumSize(d);
//        setPreferredSize(d);
        setBorder(new EmptyBorder(2,2,2,1));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        colorBoxChooser = new ColorBoxChooser();
        if (activeColor != null) {
            int activeColorIndex = colorBoxChooser.getColorIndex(activeColor);
            if (activeColorIndex != -1)
                colorBoxChooser.initActiveColorIndex(activeColorIndex);
            else{
                activeColorIndex = colorBoxChooser.addColor(activeColor);
                colorBoxChooser.initActiveColorIndex(activeColorIndex);
            }
        }

        add(colorBoxChooser);
        add(Box.createVerticalStrut(4));

        chooseColor = new JButton(database.infoBundle.getString("PreferencesDialogMsg29"));
        Dimension dim = new Dimension(colorBoxChooser.getPreferredSize().width, chooseColor.getPreferredSize().height-5);
        chooseColor.setPreferredSize(dim);
        chooseColor.setMinimumSize(dim);
        chooseColor.setMaximumSize(dim);
        Font f = chooseColor.getFont();
        f = f.deriveFont((float) 10); //f.getSize()-2);
        chooseColor.setFont(f);
        chooseColor.setMargin(new java.awt.Insets(0, 0, 0, 0));
        chooseColor.setAlignmentX(CENTER_ALIGNMENT);
        add(chooseColor);
        add(Box.createVerticalStrut(2));

        onlyActiveTable = new JCheckBox(database.infoBundle.getString("ColorPanelMsg1"));
        onlyActiveTable.setFont(f);
        onlyActiveTable.setSelected(true);
        onlyActiveTable.setAlignmentX(CENTER_ALIGNMENT);
        onlyActiveTable.setBorder(new LineBorder(Color.black));
        add(onlyActiveTable);
//        add(Box.createVerticalStrut(3));

        chooseColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(new JFrame(), database.infoBundle.getString("PreferencesDialogMsg29"), colorBoxChooser.getActiveColor());
                if (color != null) {
                    int colorIndex = colorBoxChooser.getColorIndex(color);
                    if (colorIndex == -1)
                        colorIndex = colorBoxChooser.addColor(color);
                    colorBoxChooser.setActiveColorIndex(colorIndex);
                    colorBoxChooser.repaint();
                }
            }
        });

    }

    public void activateColor(Color color) {
        if (color == null) {
            colorBoxChooser.setActiveColorIndex(-1);
            return;
        }

        int colorIndex = colorBoxChooser.getColorIndex(color);
        if (colorIndex != -1)
            colorBoxChooser.setActiveColorIndex(colorIndex);
        else{
            colorIndex = colorBoxChooser.addColor(color);
            colorBoxChooser.setActiveColorIndex(colorIndex);
        }
    }

    public ColorBoxChooser getColorBoxChooser() {
        return colorBoxChooser;
    }

    public boolean getUpdateAllTables() {
        return !onlyActiveTable.isSelected();
    }

    public void updateUI() {
        super.updateUI();
        Font f = null;
        if (chooseColor != null) {
            f = new JButton().getFont();
            f = f.deriveFont(10f);
            chooseColor.setFont(f);
        }
        if (onlyActiveTable != null) {
            if (f == null) {
                f = new JButton().getFont();
                f = f.deriveFont(10f);
            }
            onlyActiveTable.setFont(f);
        }
    }

}

