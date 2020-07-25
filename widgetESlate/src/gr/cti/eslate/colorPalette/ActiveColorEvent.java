package gr.cti.eslate.colorPalette;

import java.util.EventObject;

public class ActiveColorEvent extends EventObject {
//    private Color activeColor;
    public boolean fore;

    public ActiveColorEvent(Object source, boolean fore) {
        super(source);
//        this.activeColor = activeColor;
        this.fore = fore;

    }

    public boolean isForegroundChanged() {
    		return fore;
    }
}
