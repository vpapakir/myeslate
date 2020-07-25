package gr.cti.eslate.base.container;

import java.awt.Dimension;

import javax.swing.JTextField;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 1999
 * Company:
 * @author George Tsironis
 */

public class StandardHeightTextField extends JTextField {
    int height = 20;
    Dimension dim = new Dimension(0, height);

    public StandardHeightTextField() {
        super();
    }

    public StandardHeightTextField(String text) {
        super(text);
    }

    public StandardHeightTextField(int height) {
        super();
        this.height = height;
    }

    public Dimension getPreferredSize() {
        dim.width = super.getPreferredSize().width;
        return dim;
    }

    public void setHeight(int height) {
        if (this.height == height) return;
        this.height = height;
        repaint();
    }

    public int getHeight() {
        return height;
    }
}
