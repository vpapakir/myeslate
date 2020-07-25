package gr.cti.eslate.base.container;

/**
 * User: Yiorgos Tsironis
 */
public class CompoEntry {
    public String name, className,groupName;
    public boolean availability, visual;
    public CompoEntry(String s1, String s2, boolean b, boolean b2,String groupName) {
        name = s1;
        className = s2;
        availability = b;
        visual = b2;
        this.groupName=groupName;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append('\t');
        sb.append(className);
        sb.append('\t');
        sb.append(availability);
        sb.append('\t');
        sb.append(visual);
        sb.append('\t');
        sb.append(groupName);
        return sb.toString();
    }
}

