package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.structfile.Entry;
import gr.cti.structfile.StructFile;
import gr.cti.structfile.StructInputStream;
import gr.cti.structfile.StructOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 1999</p>
 * <p>Company: </p>
 * @author George Tsironis
 */

public class ScriptUtils {
	private static ScriptUtils instance = null;
	// This variable is set when the scripts are loaded from their files in the
	// structfile and attached to the microworld Scripts and ScriptListeners.
	boolean scriptsLoaded = false;

	private ScriptUtils() {
	}

	public static ScriptUtils getInstance() {
		if (instance == null) {
			instance = new ScriptUtils();
		}
		return instance;
	}

	public void saveMicroworldScripts(ESlateContainer container, StructFile targetStructfile, StructFile sourceStructfile) {
		/* If the microworld scripts have been loaded, then collect them from all the
         * Scripts and ScriptListeners of the microworld and save them.
		 */
//System.out.println("saveMicroworldScripts() scriptsLoaded: " + scriptsLoaded);
		if (scriptsLoaded) {
			saveLoadedScripts(container, targetStructfile);
		}else{
			/* If the scripts have not been loaded yet, then just copy their folder
             * 'Microworld Scripts' from the soyrce structfile to the target one.
			 */
			String targetPath = targetStructfile.getFile().getAbsolutePath();
			String sourcePath = sourceStructfile.getFile().getAbsolutePath();
			ArrayList scriptDirPath = new ArrayList();
			scriptDirPath.add(ESlateContainer.SCRIPT_DIR_NAME);
//System.out.println("targetPath: " + targetPath + ", sourcePath: " + sourcePath);
			if (!sourcePath.equals(targetPath)) {
				try{
					// If it appears that the scripts haven't been loaded and the
					// microworld is being saved to a new structfile, then the
					// original microworld was created by an older version of the
					// Composer, where the script texts were saved by the Scripts
					// and ScriptListeners as part of the microworld state and not
					// externally to the 'Microworld Scripts' directory of the
					// microworld file. In this case consider the scripts loaded.
					boolean scriptDirExists = false;
					try{
						ArrayList path = new ArrayList();
						path.add(ESlateContainer.SCRIPT_DIR_NAME);
						scriptDirExists = (sourceStructfile.newFindEntry(path, false) != null);
					}catch (Throwable thr) {}
					if (!scriptDirExists) {
						saveLoadedScripts(container, targetStructfile);
System.out.println("saveMicroworldScripts() old microworld file returning...");
						return;
					}
				}catch (Throwable thr) {return;}
				try{
					sourceStructfile.newCopySubFile(scriptDirPath, false, targetStructfile, new ArrayList(), false);
				}catch (Throwable thr) {
					thr.printStackTrace();
					ESlateOptionPane.showMessageDialog(container, "Unable to copy the scripts folder from the source microworld file to the target microworld file", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private static void clearCurrentDirectory(ESlateContainer container, StructFile structfile) {
		String[] existingScriptFiles = null;
		try{
			existingScriptFiles = structfile.listFiles();
		}catch (Throwable thr) {
			ESlateOptionPane.showMessageDialog(container, "Unable to synchronize current contents of the script directory. Error: " + thr.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		for (int i=0; i<existingScriptFiles.length; i++) {
			try{
				structfile.deleteFile(existingScriptFiles[i]);
			}catch (Throwable thr) {
				System.out.println("Unable to delete script file + " +existingScriptFiles[i] + " from the script directory. Error: " + thr.getMessage());
			}
		}
	}

	private static void saveLoadedScripts(ESlateContainer container, StructFile structfile) {
		// Check if there exists a directory with the new "Microworld scripts"
		Entry scriptDir = null;
		try {
			structfile.changeDir(structfile.getRootEntry());
			scriptDir = structfile.findEntry(ESlateContainer.SCRIPT_DIR_NAME);
		}catch (Throwable thr) {
//            System.out.println("Unable to save view icons. Error: " + thr.getMessage());
//            return;
		}
		boolean scriptDirJustCreated = false;
		if (scriptDir == null) {
			try{
				scriptDir = structfile.createDir(ESlateContainer.SCRIPT_DIR_NAME);
			}catch (Throwable thr) {
				ESlateOptionPane.showMessageDialog(container, "Unable to save microworld scripts. The script directory cannot be created. Error: " + thr.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			scriptDirJustCreated = true;
		}

		/* Next we find the unique values in the 'iconFileNames' array. Its is very common
		 * for views to share icons and these icons should not be stored more than once.
		 */
/*		ArrayList uniqueFileNames = new ArrayList();
		for (int i=0; i<iconFileNames.length; i++) {
			String fileName = iconFileNames[i];
			if (fileName == null) continue;
			uniqueFileNames.add(fileName);
			for (int k=i+1; k<iconFileNames.length; k++) {
				if (fileName.equals(iconFileNames[k]))
					iconFileNames[k] = null;
			}
		}
*/
		/* Next we get all the new view icons. These are the icons which have just been set
		 * and have not been saved yet in the structfile. For each such file we generate
		 * the name of the file where the view will be saved. This file name is then
		 * assigned to the corresponding views.
		 */
//		NewRestorableImageIcon[] viewNewIcons = new NewRestorableImageIcon[viewList.length+1];
//		for (int i=0; i<viewList.length; i++) {
			/* The following 'if' ensures that saved icons which have not been loaded yet,
			 * will not be loaded. Only new icons will be checked.
			 */
//			if (viewList[i].bgrIconFileName == null)
//				viewNewIcons[i] = viewList[i].getBackgroundIcon(this, structfile);
//		}
//		if (container.currentView.bgrIconFileName == null)
//			viewNewIcons[viewNewIcons.length-1] = container.currentView.getBackgroundIcon(this, structfile);

		/* Next find the unique icons among the 'viewNewIcons' and generate a unique file
		 * name for each of them.
		 */
/*		ArrayList uniqueNewIcons = new ArrayList();
		ArrayList newIconFileNames = new ArrayList();
		long id = System.currentTimeMillis();
		for (int i=0; i<viewNewIcons.length; i++) {
			NewRestorableImageIcon icon = viewNewIcons[i];
			if (icon == null) continue;
			uniqueNewIcons.add(icon);
			id++;
			String iconFileName = "Icon_" + Long.toHexString(id);
			uniqueFileNames.add(iconFileName);
			newIconFileNames.add(iconFileName);
			if (i < viewList.length)
				viewList[i].bgrIconFileName = iconFileName;
			else
				container.currentView.bgrIconFileName = iconFileName;
			for (int k=i+1; k<viewNewIcons.length; k++) {
				if (icon == viewNewIcons[k]) {
					viewNewIcons[k] = null;
					if (k < viewList.length)
						viewList[k].bgrIconFileName = iconFileName;
					else
						container.currentView.bgrIconFileName = iconFileName;
				}
			}
		}
*/
		/* Now we have all the unique new icons of the microworld's views along with the file
		 * names they should be saved in the structfile. If the directory in the structfile,
		 * where the icons are saved has just been created, then simply save all the icons
		 * in it. If the directory existed, then we have to check all the files that exist
		 * in it. Some of them may be deleted because they refer to views which do not
		 * exist, some new may be created and some may be left intact.
		 */
		try{
			structfile.changeDir(scriptDir);
		}catch (Throwable thr) {
			ESlateOptionPane.showMessageDialog(container, "Unable to save view icons. Cannot change dir to the script directory. Error: " + thr.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!scriptDirJustCreated) {
			clearCurrentDirectory(container, structfile);
		}

/*		for (int i=0; existingScriptFiles != null && i<existingScriptFiles.length; i++) {
			boolean deleteFile = true;
			for (int k=0; k<uniqueFileNames.size(); k++) {
				if (existingIconFiles[i].equals(uniqueFileNames.get(k))) {
					deleteFile = false;
					uniqueFileNames.remove(k);
//                        uniqueIcons.remove(k);
					break;
				}
			}
			if (deleteFile) {
				try{
					structfile.deleteFile(existingIconFiles[i]);
				}catch (Throwable thr) {
					System.out.println("Unable to delete icon file + " +existingIconFiles[i] + " from the view icon directory. Error: " + thr.getMessage());
				}
			}
		}
*/
		ScriptMap scriptMap = container.scriptMap;
		ScriptListenerMap scriptListenerMap = container.componentScriptListeners;
		int scriptCount = scriptMap.scripts.size() + scriptListenerMap.size();
		String[] scriptFileNames = new String[scriptCount];
		String[] scriptTexts = new String[scriptCount];
		int i = 0;
		for (; i<scriptMap.scripts.size(); i++) {
			Script script = (Script) scriptMap.scripts.get(i);
//			scriptFileNames[i] = script.getFullClassName();
			scriptTexts[i] = script.getScript();
			if (scriptTexts[i] == null) scriptTexts[i] = "";
		}
		for (int j=0; j<scriptListenerMap.size(); j++) {
			ScriptListener scriptListener = scriptListenerMap.getScriptListener(j);
//System.out.println("scriptListener: " + scriptListener + ", listenerClassName: " + scriptListener.listenerClassName + ", methodName: " + scriptListener.methodName + ", path: " + getScriptListenerFileName(scriptListener));
//            scriptFileNames[i] = scriptListener.getListenerClassName();
//			scriptFileNames[i] = getOldScriptListenerFileName(scriptListener);
            scriptFileNames[i] = scriptListener.scriptFilename;
			scriptTexts[i] = scriptListener.getScript();
			i++;
		}

		for (i=0; i<scriptFileNames.length; i++) {
			try{
				Entry scriptFile = structfile.createFile(scriptFileNames[i]);
//System.out.println("scriptFile: " + scriptFile + ", scriptFileNames[i]: " + scriptFileNames[i] + "scriptTexts[i]: " + scriptTexts[i].length());
				BufferedOutputStream bos = new BufferedOutputStream(new StructOutputStream(structfile, scriptFile));
				ObjectOutputStream oos = new ObjectOutputStream(bos);
				oos.writeObject(scriptTexts[i]);
				oos.flush();
				oos.close();
			}catch (Throwable thr) {
				thr.printStackTrace();
				ESlateOptionPane.showMessageDialog(container, "Unable to save script. Cannot write the script file. Error: " + thr.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		try {
			structfile.changeDir(structfile.getRootEntry());
		}catch (Throwable thr) {}
	}

	/** Loads the sources of the microworld scripts to the Scripts
	 *  and ScriptListeners of the microworld.
	 */
	void loadMicroworldScripts(ESlateContainer container, StructFile structfile) {
//System.out.println("loadMicroworldScripts() scriptsLoaded: " + scriptsLoaded);
		if (scriptsLoaded) return;

		ScriptMap scriptMap = container.scriptMap;
		ScriptListenerMap scriptListenerMap = container.componentScriptListeners;

		if (structfile == null || !structfile.isOpen()) {
			ESlateOptionPane.showMessageDialog(container, "Cannot load the scrips because the structfile is not specified or is not open.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Entry iconDirEntry = null;
		try {
			structfile.changeDir(structfile.getRootEntry());
			iconDirEntry = structfile.findEntry(ESlateContainer.SCRIPT_DIR_NAME);
		}catch (Throwable thr) {
//			System.out.println("Cannot get the view bgr icon, cause there is not directory " + iconDirName + " in the structfile.");
			return;
		}

		try{
			structfile.changeDir(iconDirEntry);
		}catch (Throwable thr) {
			ESlateOptionPane.showMessageDialog(container, "Cannot get microworld scripts. Error while changing dir to "+ ESlateContainer.SCRIPT_DIR_NAME + ". Message: " + thr.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Get the file names of all the script files.
		String[] scriptFileNames = null;
		try{
			scriptFileNames = structfile.listFiles();
		}catch (Throwable thr) {
			ESlateOptionPane.showMessageDialog(container, "Unable to get the contents of the script directory. Error: " + thr.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		// Load the scripts in the script files to the scriptTexts array.
		String[] scriptTexts = new String[scriptFileNames.length];
		for (int i=0; i<scriptFileNames.length; i++) {
//System.out.println("Looking for script: " + scriptFileNames[i]);
			Entry scriptFileEntry = null;
			try {
				scriptFileEntry = structfile.findEntry(scriptFileNames[i]);
			}catch (Throwable thr) {
				ESlateOptionPane.showMessageDialog(container, "Cannot get script file. There is no file " + scriptFileNames[i] + " in the directory " + ESlateContainer.SCRIPT_DIR_NAME + " of the structfile.", "Error", JOptionPane.ERROR_MESSAGE);
				continue;
			}
			try{
				BufferedInputStream bis = new BufferedInputStream(new StructInputStream(structfile, scriptFileEntry));
				ObjectInputStream ois = new ObjectInputStream(bis);
				scriptTexts[i] = (String) ois.readObject();
				if (scriptTexts[i] == null) scriptTexts[i] = "";

//System.out.println("loaded script: " + scriptFileNames[i] + ", scriptTexts[i].length: " + scriptTexts[i].length());
				ois.close();
			}catch (Throwable thr) {
				ESlateOptionPane.showMessageDialog(container, "Unable to load script from file " + scriptFileNames[i] + "  Error: " + thr.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		// Bind the loaded to script texts to the proper Scripts and ScriptListeners
		// First bind the Script scripts
		for (int i=0; i<scriptMap.scripts.size(); i++) {
			Script script = (Script) scriptMap.scripts.get(i);
			String scriptName = script.getFullClassName();
			// Find the proper loaded script text. The file names of the Script files
			// are named after the 'fullClassName' of each Script.
			for (int j=0; j<scriptFileNames.length; j++) {
				if (scriptFileNames[j].equals(scriptName)) {
					script.script = scriptTexts[j];
					break;
				}
			}
		}
		// Next bind the ScriptListener scripts
		for (int i=0; i<scriptListenerMap.size(); i++) {
			ScriptListener scriptListener = scriptListenerMap.getScriptListener(i);
//            String scriptName = scriptListener.listenerClassName;
            String scriptName = scriptListener.scriptFilename;
            if (scriptName == null)
                scriptName = getOldScriptListenerFileName(scriptListener);
            boolean listenerFound = false;
            // Find the proper loaded script text. The file names of the ScriptListener files
			// are named after the 'listenerClassName' of each ScriptListener.
			for (int j=0; j<scriptFileNames.length; j++) {
				if (scriptFileNames[j].equals(scriptName)) {
					scriptListener.setScript(scriptTexts[j]);
                    listenerFound = true;
                    break;
				}
			}
        }

		// Go back to the root dir of the struct file
		try {
			structfile.changeDir(structfile.getRootEntry());
		}catch (Throwable thr) {}

		scriptsLoaded = true;
	}

    /**
     * Returns the name of the file in the structured storage file in which the script of the specified listener
     * will be stored. This method has been deprecated. Now the name of the script's file in the structured storage
     * file is taken from the ScriptListener.scriptFileName field.
     * @param scriptListener the script listener
     */
    private static String getOldScriptListenerFileName(ScriptListener scriptListener) {
        return scriptListener.pathToComponent.getPathInDotNotation() + '.' + scriptListener.listenerClassName;
    }
}
