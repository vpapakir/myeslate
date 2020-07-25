package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author George Tsironis
 */

public class SoundTheme implements java.io.Externalizable {
    static final long serialVersionUID = 12;
    public static final int FORMAT_VERSION = 1;

    public static final int ERROR_SOUND = 0;
    public static final int INFO_SOUND = 1;
    public static final int QUESTION_SOUND = 2;
    public static final int PLUG_SOUND = 3;
    public static final int UNPLUG_SOUND = 4;
    public static final int START_SOUND = 5;
    public static final int EXIT_SOUND = 6;
    public static final int COMPONENT_NEW_SOUND = 7;
    public static final int COMPONENT_SHOWN_SOUND = 8;
    public static final int COMPONENT_HIDDEN_SOUND = 9;
    public static final int COMPONENT_CLOSED_SOUND = 10;
    public static final int MWD_OPENED_SOUND = 11;
    public static final int MWD_CLOSED_SOUND = 12;
    public static final int COMPONENT_ACTIVATED_SOUND = 13;
    public static final int VIEW_CHANGED_SOUND = 14;
    public static final int COMPONENT_MAXIMIZED_SOUND = 15;

    ESlateContainer container;
    SystemSound errorSound, infoSound, questionSound, plugSound, unplugSound;
    SystemSound startSound, exitSound, componentNewSound, componentShownSound;
    SystemSound componentHiddenSound, componentClosedSound, microworldOpenedSound;
    SystemSound microworldClosedSound, componentActivatedSound, viewChangedSound;
    SystemSound componentMaximizedSound;
//    boolean modified = false;

    /** Exists for the Externalization mechanism only.
     */
    public SoundTheme() {
    }

    public SoundTheme(ESlateContainer container) {
        this.container = container;

        errorSound = new SystemSound(container.containerBundle.getString("ErrorSound"), null);
        infoSound = new SystemSound(container.containerBundle.getString("InfoSound"), null);
        questionSound = new SystemSound(container.containerBundle.getString("QuestionSound"), null);
        plugSound = new SystemSound(container.containerBundle.getString("PlugSound"), null);
        unplugSound = new SystemSound(container.containerBundle.getString("UnplugSound"), null);
        startSound = new SystemSound(container.containerBundle.getString("StartSound"), null);
        exitSound = new SystemSound(container.containerBundle.getString("EndSound"), null);
        componentNewSound = new SystemSound(container.containerBundle.getString("ComponentNewSound"), null);
        componentClosedSound = new SystemSound(container.containerBundle.getString("ComponentClosedSound"), null);
        componentShownSound = new SystemSound(container.containerBundle.getString("ComponentShownSound"), null);
        componentHiddenSound = new SystemSound(container.containerBundle.getString("ComponentHiddenSound"), null);
        componentActivatedSound = new SystemSound(container.containerBundle.getString("ComponentActivatedSound"), null);
        componentMaximizedSound = new SystemSound(container.containerBundle.getString("ComponentMaximizedSound"), null);
        microworldOpenedSound = new SystemSound(container.containerBundle.getString("MwdOpenedSound"), null);
        microworldClosedSound = new SystemSound(container.containerBundle.getString("MwdClosedSound"), null);
        viewChangedSound = new SystemSound(container.containerBundle.getString("ViewChangedSound"), null);
    }

    /** Creates a new SoundTheme, which is a replicate of the supplied SoundTheme */
    SoundTheme(SoundTheme theme) {
        if (theme == null) throw new NullPointerException("The supplied sound theme cannot be null");
        container = theme.container;
        errorSound = (SystemSound) theme.errorSound.clone();
        infoSound = (SystemSound) theme.infoSound.clone();
        questionSound = (SystemSound) theme.questionSound.clone();
        plugSound = (SystemSound) theme.plugSound.clone();
        unplugSound = (SystemSound) theme.unplugSound.clone();
        startSound = (SystemSound) theme.startSound.clone();
        exitSound = (SystemSound) theme.exitSound.clone();
        componentNewSound = (SystemSound) theme.componentNewSound.clone();
        componentClosedSound = (SystemSound) theme.componentClosedSound.clone();
        componentShownSound = (SystemSound) theme.componentShownSound.clone();
        componentHiddenSound = (SystemSound) theme.componentHiddenSound.clone();
        componentActivatedSound = (SystemSound) theme.componentActivatedSound.clone();
        componentMaximizedSound = (SystemSound) theme.componentMaximizedSound.clone();
        microworldOpenedSound = (SystemSound) theme.microworldOpenedSound.clone();
        microworldClosedSound = (SystemSound) theme.microworldClosedSound.clone();
        viewChangedSound = (SystemSound) theme.viewChangedSound.clone();
    }

    public SystemSound getSound(int id) {
        if (id < ERROR_SOUND || id > COMPONENT_MAXIMIZED_SOUND)
            return null;
        switch (id) {
            case ERROR_SOUND:
                return errorSound;
            case INFO_SOUND:
                return infoSound;
            case QUESTION_SOUND:
                return questionSound;
            case PLUG_SOUND:
                return plugSound;
            case UNPLUG_SOUND:
                return unplugSound;
            case START_SOUND:
                return startSound;
            case EXIT_SOUND:
                return exitSound;
            case COMPONENT_NEW_SOUND:
                return componentNewSound;
            case COMPONENT_SHOWN_SOUND:
                return componentShownSound;
            case COMPONENT_HIDDEN_SOUND:
                return componentHiddenSound;
            case COMPONENT_CLOSED_SOUND:
                return componentClosedSound;
            case MWD_OPENED_SOUND:
                return microworldOpenedSound;
            case MWD_CLOSED_SOUND:
                return microworldClosedSound;
            case COMPONENT_ACTIVATED_SOUND:
                return componentActivatedSound;
            case VIEW_CHANGED_SOUND:
                return viewChangedSound;
            case COMPONENT_MAXIMIZED_SOUND:
                return componentMaximizedSound;
        }
        return null;
    }

    public SystemSound[] getESlateSounds() {
        SystemSound[] eslateSounds = new SystemSound[7];
        eslateSounds[0] = errorSound;
        eslateSounds[1] = infoSound;
        eslateSounds[2] = questionSound;
        eslateSounds[3] = plugSound;
        eslateSounds[4] = unplugSound;
        eslateSounds[5] = startSound;
        eslateSounds[6] = exitSound;

        return eslateSounds;
    }

    public SystemSound[] getMicroworldSounds() {
        SystemSound[] mwdSounds = new SystemSound[9];
        mwdSounds[0] = componentNewSound;
        mwdSounds[1] = componentShownSound;
        mwdSounds[2] = componentHiddenSound;
        mwdSounds[3] = componentMaximizedSound;
        mwdSounds[4] = componentActivatedSound;
        mwdSounds[5] = componentClosedSound;
        mwdSounds[6] = microworldOpenedSound;
        mwdSounds[7] = microworldClosedSound;
        mwdSounds[8] = viewChangedSound;

        return mwdSounds;
    }

    /* Watch out, ORDER MATTERS. The order of the sounds in the array that this method
     * returns is important, and it should be the same as the incremental order of the
     * sound constants in SoundTheme.java. This stands because the sounds of the SoundTheme
     * may also be accessed through the getSound(id) method of the SoundTheme.
     */
    public SystemSound[] getThemeSounds() {
        SystemSound[] themeSounds = new SystemSound[16];
        themeSounds[0] = errorSound;
        themeSounds[1] = infoSound;
        themeSounds[2] = questionSound;
        themeSounds[3] = plugSound;
        themeSounds[4] = unplugSound;
        themeSounds[5] = startSound;
        themeSounds[6] = exitSound;
        themeSounds[7] = componentNewSound;
        themeSounds[8] = componentShownSound;
        themeSounds[9] = componentHiddenSound;
        themeSounds[10] = componentClosedSound;
        themeSounds[11] = microworldOpenedSound;
        themeSounds[12] = microworldClosedSound;
        themeSounds[13] = componentActivatedSound;
        themeSounds[14] = viewChangedSound;
        themeSounds[15] = componentMaximizedSound;
        return themeSounds;
    }

    /** Returns the number of SystemSounds in this theme that use the specified sound file
     *  in the Structured storage file of the theme.
     */
    public int getSoundCount(String internalFileName) {
        if (internalFileName == null)
            return 0;
        SystemSound[] sounds = getThemeSounds();
        int count = 0;
        for (int i=0; i<sounds.length; i++) {
            if (sounds[i].soundFileName != null && sounds[i].soundFileName.equals(internalFileName))
                count++;
        }
        return count;
    }

/*    public void setErrorSound(SystemSound sound) {
        errorSound = sound;
        modified = true;
    }

    public void setInfoSound(SystemSound sound) {
        infoSound = sound;
        modified = true;
    }

    public void setQuestionSound(SystemSound sound) {
        questionSound = sound;
        modified = true;
    }
    public void setPlugSound(SystemSound sound) {
        plugSound = sound;
        modified = true;
    }
    public void setUnplugSound(SystemSound sound) {
        unplugSound = sound;
        modified = true;
    }
    public void setStartSound(SystemSound sound) {
        startSound = sound;
        modified = true;
    }
    public void setExitSound(SystemSound sound) {
        exitSound = sound;
        modified = true;
    }
    public void setComponentNewSound(SystemSound sound) {
        componentNewSound = sound;
        modified = true;
    }
    public void setComponentShownSound(SystemSound sound) {
        componentShownSound = sound;
        modified = true;
    }
    public void setComponentHiddenSound(SystemSound sound) {
        componentHiddenSound = sound;
        modified = true;
    }
    public void setComponentClosedSound(SystemSound sound) {
        componentClosedSound = sound;
        modified = true;
    }
    public void setMicroworldOpenedSound(SystemSound sound) {
        microworldOpenedSound = sound;
        modified = true;
    }
    public void setMicroworldClosedSound(SystemSound sound) {
        microworldClosedSound = sound;
        modified = true;
    }
    public void setComponentActivatedSound(SystemSound sound) {
        componentActivatedSound = sound;
        modified = true;
    }
    public void setViewChangedSound(SystemSound sound) {
        viewChangedSound = sound;
        modified = true;
    }
    public void setComponentMaximizedSound(SystemSound sound) {
        componentMaximizedSound = sound;
        modified = true;
    }
*/

    public boolean isModified() {
        if (errorSound.modified) return true;
        if (infoSound.modified) return true;
        if (questionSound.modified) return true;
        if (plugSound.modified) return true;
        if (unplugSound.modified) return true;
        if (startSound.modified) return true;
        if (exitSound.modified) return true;
        if (componentNewSound.modified) return true;
        if (componentShownSound.modified) return true;
        if (componentHiddenSound.modified) return true;
        if (componentClosedSound.modified) return true;
        if (microworldOpenedSound.modified) return true;
        if (microworldClosedSound.modified) return true;
        if (componentActivatedSound.modified) return true;
        if (viewChangedSound.modified) return true;
        if (componentMaximizedSound.modified) return true;
        return false;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("ERROR_SOUND", errorSound);
        fieldMap.put("INFO_SOUND", infoSound);
        fieldMap.put("QUESTION_SOUND", questionSound);
        fieldMap.put("PLUG_SOUND", plugSound);
        fieldMap.put("UNPLUG_SOUND", unplugSound);
        fieldMap.put("START_SOUND", startSound);
        fieldMap.put("EXIT_SOUND", exitSound);
        fieldMap.put("COMPONENT_NEW_SOUND", componentNewSound);
        fieldMap.put("COMPONENT_SHOWN_SOUND", componentShownSound);
        fieldMap.put("COMPONENT_HIDDEN_SOUND", componentHiddenSound);
        fieldMap.put("COMPONENT_CLOSED_SOUND", componentClosedSound);
        fieldMap.put("MWD_OPENED_SOUND", microworldOpenedSound);
        fieldMap.put("MWD_CLOSED_SOUND", microworldClosedSound);
        fieldMap.put("COMPONENT_ACTIVATED_SOUND", componentActivatedSound);
        fieldMap.put("VIEW_CHANGED_SOUND", viewChangedSound);
        fieldMap.put("COMPONENT_MAXIMIZED_SOUND", componentMaximizedSound);
        out.writeObject(fieldMap);
        errorSound.modified = false;
        infoSound.modified = false;
        questionSound.modified = false;
        plugSound.modified = false;
        unplugSound.modified = false;
        startSound.modified = false;
        exitSound.modified = false;
        componentNewSound.modified = false;
        componentShownSound.modified = false;
        componentHiddenSound.modified = false;
        componentClosedSound.modified = false;
        microworldOpenedSound.modified = false;
        microworldClosedSound.modified = false;
        componentActivatedSound.modified = false;
        viewChangedSound.modified = false;
        componentMaximizedSound.modified = false;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        errorSound = (SystemSound) fieldMap.get("ERROR_SOUND");
        infoSound = (SystemSound) fieldMap.get("INFO_SOUND");
        questionSound = (SystemSound) fieldMap.get("QUESTION_SOUND");
        plugSound = (SystemSound) fieldMap.get("PLUG_SOUND");
        unplugSound = (SystemSound) fieldMap.get("UNPLUG_SOUND");
        startSound = (SystemSound) fieldMap.get("START_SOUND");
        exitSound = (SystemSound) fieldMap.get("EXIT_SOUND");
        componentNewSound = (SystemSound) fieldMap.get("COMPONENT_NEW_SOUND");
        componentShownSound = (SystemSound) fieldMap.get("COMPONENT_SHOWN_SOUND");
        componentHiddenSound = (SystemSound) fieldMap.get("COMPONENT_HIDDEN_SOUND");
        componentClosedSound = (SystemSound) fieldMap.get("COMPONENT_CLOSED_SOUND");
        microworldOpenedSound = (SystemSound) fieldMap.get("MWD_OPENED_SOUND");
        microworldClosedSound = (SystemSound) fieldMap.get("MWD_CLOSED_SOUND");
        componentActivatedSound = (SystemSound) fieldMap.get("COMPONENT_ACTIVATED_SOUND");
        viewChangedSound = (SystemSound) fieldMap.get("VIEW_CHANGED_SOUND");
        componentMaximizedSound = (SystemSound) fieldMap.get("COMPONENT_MAXIMIZED_SOUND");
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        for (int i=ERROR_SOUND; i<=COMPONENT_MAXIMIZED_SOUND; i++) {
            SystemSound sound = getSound(i);
            String path = sound.pathToSound;
            String internalFile = (String) sound.getValue();
            if (path != null) {
                if (i != ERROR_SOUND)
                    buff.append(", ");
                buff.append('[' + (String) sound.getKey() + ": " + path + ']');
            }else if (internalFile != null) {
                if (i != ERROR_SOUND)
                    buff.append(", ");
                buff.append('[' + (String) sound.getKey() + ": " + internalFile + ']');
            }
        }
        if (buff.length() == 0)
            buff.append("Empty Sound Theme");
        return buff.toString();
    }
}
