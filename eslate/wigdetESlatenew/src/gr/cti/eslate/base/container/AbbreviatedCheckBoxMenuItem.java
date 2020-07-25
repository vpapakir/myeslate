package gr.cti.eslate.base.container;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;


class AbbreviatedCheckBoxMenuItem extends JCheckBoxMenuItem {
    public static final int ITEM_HEIGHT = 21;
    public static final int EMPTY_SPACE = 30;
    String fullText = null;
    int maxWidth = 0;
    Dimension prefSize = new Dimension(0, ITEM_HEIGHT);
/////////////nikosM
    boolean remote=false;
/////////////nikosM end

    public AbbreviatedCheckBoxMenuItem(String text, int maxWidth) {
        super();
        this.maxWidth = maxWidth;
        setFullText(text);
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String text) {
        fullText = text;
        setText(abbreviate(text));
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int w) {
        if (this.maxWidth == w) return;
        maxWidth = w;
        setText(abbreviate(fullText));
    }
//////nikosM
    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean value) {
        remote=value;
    }

//////nikosM end

    protected String abbreviate(String fileName) {
        FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(getFont());
//        System.out.println(fm.stringWidth(fileName));
        int currWidth = fm.stringWidth(fileName);
        if (currWidth <= maxWidth) {
            prefSize.width = currWidth + EMPTY_SPACE;
            return fileName;
        }

        StringBuffer abbreviation = new StringBuffer(fileName);
        char fileSeparator = System.getProperty("file.separator").charAt(0);
        int sepCount = 0;
        ArrayList<Integer> array = new ArrayList<Integer>();
        for (int i=0; i<abbreviation.length(); i++) {
            if (abbreviation.charAt(i) == fileSeparator) {
                sepCount++;
                array.add(new Integer(i));
            }
        }

        /* Start cutting down parts of the fileName, until its width becomes
         * less or equal to maxWidth. The parts which are cut from the fileName
         * are directory names. First we cut the directory before the last one.
         * We continue with the directories to its right. If we reach the first
         * directory and abbreviation is still longer than maxWidth, we continue
         * with the last directory. Finally, if needed, we cut the first directory's
         * name too.
         */
        boolean abbreviationOccured = false;
        for (int i=array.size()-2; i>1; i--) {
            int index = ((Integer) array.get(i)).intValue();
            int prevIndex = ((Integer) array.get(i-1)).intValue();
            if (!abbreviationOccured) {
                abbreviation = abbreviation.replace(prevIndex+1, index, "...");
                abbreviationOccured = true;
            }else
                abbreviation = abbreviation.replace(prevIndex, index, "");
//            System.out.println("abbreviation.toString(): " + abbreviation.toString());
            currWidth = fm.stringWidth(abbreviation.toString());
            if (currWidth <= maxWidth) break;
        }
        if (currWidth > maxWidth) {
            int lastIndex = -1;
            int prevLastIndex = -1;
            int k = abbreviation.length()-1;
            for (; k >= 0; k--) {
                if (abbreviation.charAt(k) == fileSeparator) {
                    lastIndex = k;
                    break;
                }
            }
            k--;
            for (; k >= 0; k--) {
                if (abbreviation.charAt(k) == fileSeparator) {
                    prevLastIndex = k;
                    break;
                }
            }
            if (lastIndex != -1 && prevLastIndex != -1) {
                if (!abbreviationOccured) {
                    abbreviation = abbreviation.replace(prevLastIndex+1, lastIndex, "...");
                    abbreviationOccured = true;
                }else
                    abbreviation = abbreviation.replace(prevLastIndex, lastIndex, "");
//                System.out.println("abbreviation.toString(): " + abbreviation.toString());
                currWidth = fm.stringWidth(abbreviation.toString());
            }
        }
        if (currWidth > maxWidth) {
            int firstIndex = -1;
            int secondIndex = -1;
            int k = 0;
            for (; k < abbreviation.length(); k++) {
                if (abbreviation.charAt(k) == fileSeparator) {
                    firstIndex = k;
                    break;
                }
            }
            k++;
            for (; k < abbreviation.length(); k++) {
                if (abbreviation.charAt(k) == fileSeparator) {
                    secondIndex = k;
                    break;
                }
            }
            if (firstIndex != -1 && secondIndex != -1) {
                if (!abbreviationOccured) {
                    abbreviation = abbreviation.replace(firstIndex+1, secondIndex, "...");
                    abbreviationOccured = true;
                }else
                    abbreviation = abbreviation.replace(firstIndex+1, secondIndex+1, "");
//                System.out.println("abbreviation.toString(): " + abbreviation.toString());
                currWidth = fm.stringWidth(abbreviation.toString());
            }
        }
        prefSize.width = currWidth + EMPTY_SPACE;

        return abbreviation.toString();
    }

    public Dimension getPreferredSize() {
        return prefSize;
    }

}


