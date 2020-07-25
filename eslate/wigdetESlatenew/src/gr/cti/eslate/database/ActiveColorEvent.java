package gr.cti.eslate.database;

import java.util.EventObject;
import java.awt.Color;

public class ActiveColorEvent extends EventObject {
    private Color previousColor;
    private Color activeColor;
    private int colorType;

    public ActiveColorEvent(Object source, Color previousColor, Color activeColor, int colorType) {
        super(source);
        this.previousColor = previousColor;
        this.activeColor = activeColor;
        this.colorType = colorType;
    }

    public Color getPreviousColor() {
        return previousColor;
    }

    public Color getActiveColor() {
        return activeColor;
    }

    public int getColorType() {
        return colorType;
    }
}