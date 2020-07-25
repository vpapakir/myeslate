package gr.cti.eslate.mapViewer;

/**
 * Tags an ESlateButton or ESlateTogggleButton as a MapViewer tool component.
 */

public interface ToolComponent {
    /**
     * Defines that the tool has the default icon.
     */
    public void clearIconFlag();
    /**
     * Indicates a change in the icon.
     */
    public boolean isIconChanged();
    /**
     * Defines that the tool has the default help text.
     */
    public void clearHelpTextFlag();
    /**
     * Indicates a change in the help text.
     */
    public boolean isHelpTextChanged();
    /**
     * Defines that the tool has the default tooltip text.
     */
    public void clearToolTipTextFlag();
    /**
     * Indicates a change in the tooltip text.
     */
    public boolean isToolTipTextChanged();
    /**
     * The help text is written in the status/message bar when the tool is selected.
     */
    public void setHelpText(String ht);
    /**
     * The help text is written in the status/message bar when the tool is selected.
     */
    public String getHelpText();

    public java.awt.Dimension SIZE=new java.awt.Dimension(25,25);
}