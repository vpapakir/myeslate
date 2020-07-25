package gr.cti.eslate.database;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.CompoundBorder;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.GridLayout;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;


public class GridChooser extends JPanel implements Externalizable {
    static ImageIcon fullGridIcon = null;
    static ImageIcon horLinesOnlyIcon = null;
    static ImageIcon vertLinesOnlyIcon = null;
    static ImageIcon noGridIcon = null;
    private DBTable dbTable = null;
    JToggleButton noGrid, horLinesOnly, vertLinesOnly, fullGrid;

    public GridChooser(DBTable dbtable) {
        super(true);
        this.dbTable = dbtable;
        setBorder(new CompoundBorder(new CompoundBorder(new EmptyBorder(3,3,3,3), new EtchedBorder(EtchedBorder.RAISED)), new EmptyBorder(1,1,1,1)));

        if (fullGridIcon == null)
            fullGridIcon = new ImageIcon(getClass().getResource("images/toolbar/fullGrid.gif"));
        if (horLinesOnlyIcon == null)
            horLinesOnlyIcon = new ImageIcon(getClass().getResource("images/toolbar/horLinesOnly.gif"));
        if (vertLinesOnlyIcon == null)
            vertLinesOnlyIcon = new ImageIcon(getClass().getResource("images/toolbar/vertLinesOnly.gif"));
        if (noGridIcon == null)
            noGridIcon = new ImageIcon(getClass().getResource("images/toolbar/noGrid.gif"));

        fullGrid = new JToggleButton(fullGridIcon);
	    fullGrid.setToolTipText(dbTable.bundle.getString("GridChooser1"));
        fullGrid.setMargin(new Insets(0,0,0,0));
        fullGrid.setAlignmentY(CENTER_ALIGNMENT);
        fullGrid.setRequestFocusEnabled(false);
        fullGrid.setBorderPainted(false);

        horLinesOnly = new JToggleButton(horLinesOnlyIcon);
	    horLinesOnly.setToolTipText(dbTable.bundle.getString("GridChooser2"));
        horLinesOnly.setMargin(new Insets(0,0,0,0));
        horLinesOnly.setAlignmentY(CENTER_ALIGNMENT);
        horLinesOnly.setRequestFocusEnabled(false);
        horLinesOnly.setBorderPainted(false);

        vertLinesOnly = new JToggleButton(vertLinesOnlyIcon);
	    vertLinesOnly.setToolTipText(dbTable.bundle.getString("GridChooser3"));
        vertLinesOnly.setMargin(new Insets(0,0,0,0));
        vertLinesOnly.setAlignmentY(CENTER_ALIGNMENT);
        vertLinesOnly.setRequestFocusEnabled(false);
        vertLinesOnly.setBorderPainted(false);

        noGrid = new JToggleButton(noGridIcon);
	    noGrid.setToolTipText(dbTable.bundle.getString("GridChooser4"));
        noGrid.setMargin(new Insets(0,0,0,0));
        noGrid.setAlignmentY(CENTER_ALIGNMENT);
        noGrid.setRequestFocusEnabled(false);
        noGrid.setBorderPainted(false);

        ButtonGroup bgrp = new ButtonGroup();
        bgrp.add(fullGrid);
        bgrp.add(horLinesOnly);
        bgrp.add(vertLinesOnly);
        bgrp.add(noGrid);

        setLayout(new GridLayout(2, 2, 0, 0));
        add(fullGrid);
        add(horLinesOnly);
        add(vertLinesOnly);
        add(noGrid);

        fullGrid.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (dbTable != null) {
                    dbTable.closeDatabasePopupMenu();
                    dbTable.setVerticalLinesVisible(true);
                    dbTable.setHorizontalLinesVisible(true);
                }
            }
        });

        horLinesOnly.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (dbTable != null) {
                    dbTable.closeDatabasePopupMenu();
                    dbTable.setVerticalLinesVisible(false);
                    dbTable.setHorizontalLinesVisible(true);
                }
            }
        });

        vertLinesOnly.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (dbTable != null) {
                    dbTable.closeDatabasePopupMenu();
                    dbTable.setVerticalLinesVisible(true);
                    dbTable.setHorizontalLinesVisible(false);
                }
            }
        });

        noGrid.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (dbTable != null) {
                    dbTable.closeDatabasePopupMenu();
                    dbTable.setVerticalLinesVisible(false);
                    dbTable.setHorizontalLinesVisible(false);
                }
            }
        });
        fullGrid.addItemListener(new GridItemListener(fullGrid));
        vertLinesOnly.addItemListener(new GridItemListener(vertLinesOnly));
        horLinesOnly.addItemListener(new GridItemListener(horLinesOnly));
        noGrid.addItemListener(new GridItemListener(noGrid));

        // Initialize
        initialize();
    }

    void setDBTable(DBTable dbTable) {
        if (dbTable == null) return;
        this.dbTable = dbTable;
        initialize();
    }

    void initialize() {
        if (dbTable == null) return;
        if (dbTable.isHorizontalLinesVisible() && dbTable.isVerticalLinesVisible())
            fullGrid.setSelected(true);
        else if (dbTable.isHorizontalLinesVisible() && !dbTable.isVerticalLinesVisible())
            horLinesOnly.setSelected(true);
        else if (!dbTable.isHorizontalLinesVisible() && dbTable.isVerticalLinesVisible())
            vertLinesOnly.setSelected(true);
        else if (!dbTable.isHorizontalLinesVisible() && !dbTable.isVerticalLinesVisible())
            noGrid.setSelected(true);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    }

    class GridItemListener implements ItemListener {
        JToggleButton button = null;
        public GridItemListener(JToggleButton button) {
            this.button = button;
        }
        public void itemStateChanged(ItemEvent e) {
            button.setBorderPainted(button.isSelected());
        }
    }
}