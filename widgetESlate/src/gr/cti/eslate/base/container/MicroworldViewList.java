package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.structfile.Entry;
import gr.cti.structfile.StructFile;
import gr.cti.structfile.StructOutputStream;

import java.io.BufferedOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;


public class MicroworldViewList implements java.io.Externalizable, Cloneable {
    static final long serialVersionUID = 12;
    public static final String STR_FORMAT_VERSION = "1.0";
    static final int FORMAT_VERSION = 2;
    public static final String VIEW_ICON_DIR_NAME = "Microworld View Icons";
    MicroworldView[] viewList;
    ResourceBundle containerBundle;
    ESlateContainer container;

    public MicroworldViewList() {
        viewList = new MicroworldView[0];
        containerBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ContainerBundle", Locale.getDefault());
    }

    public MicroworldViewList(ESlateContainer container) {
        this();
        this.container = container;
    }

    public final void addView(MicroworldView view) {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.viewCreationAllowed, "viewCreationAllowed");
        addViewInternal(view);
    }

    final void addViewInternal(MicroworldView view) {
        int l = viewList.length;
        view.viewName = makeUnique(view.viewName);
        MicroworldView[] newView = new MicroworldView[l+1];
        for (int i=0; i<l; i++)
            newView[i] = viewList[i];
        newView[l] = view;
        viewList = newView;
    }

    public final MicroworldView removeView(String viewName) {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.viewRemovalAllowed, "viewRemovalAllowed");
        MicroworldView removedView = removeViewInternal(viewName);
		if (removedView != null) {
            if (container instanceof ESlateComposer) {
                ViewMenu viewMenu = ((ESlateComposer) container).menuPanel.microworldViewMenu;
                viewMenu.removeItem(viewName);
            }
		}
		return removedView;
    }

    final MicroworldView removeViewInternal(String viewName) {
        int index = getViewIndex(viewName);
        if (index == -1) return null;
		MicroworldView removedView = viewList[index];

        int l = viewList.length;
        MicroworldView[] newView = new MicroworldView[l-1];
        for (int i=0; i<l; i++) {
            if (i<index) {
                newView[i] = viewList[i];
            }else if (i>index) {
                newView[i-1] = viewList[i];
            }
        }
        viewList = newView;
		return removedView;
    }

    public final void removeAll() {
        viewList = new MicroworldView[0];
    }

    /** Replaces the given 'oldView' in the MicroworldViewList, with the
     *  'newView'. This happens only if the MicroworldViewList contains
     *  'oldView'. The 'newView' is assigned the name of the 'oldView'.
     */
    public final void replaceView(MicroworldView oldView, MicroworldView newView) {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.viewRemovalAllowed, "viewRemovalAllowed");
        replaceViewInternal(oldView, newView);
    }

    /** Replaces the given 'oldView' in the MicroworldViewList, with the
     *  current view of the microworld. This happens only if the MicroworldViewList contains
     *  'oldView'.
     */
    public final MicroworldView replaceView(MicroworldView oldView) {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.viewRemovalAllowed, "viewRemovalAllowed");
        MicroworldView view = new MicroworldView(container, container.currentView);
        view.createDesktopItemInfos(container, container.microworld.isStoreSkinsPerView());
        replaceView(oldView, view);
        return view;
    }

    final void replaceViewInternal(MicroworldView oldView, MicroworldView newView) {
        int index = getViewIndex(oldView);
//System.out.println("Replacing oldView: " + oldView + ", newView: " + newView + ", index: " + index);
        if (index != -1) {
            viewList[index] = newView;
            newView.viewName = oldView.viewName;
        }
    }

    /** Renames a view. Does nothing if there is no open microworld, or if the opened
     *  microworld has forbidden view renaming.
     *  @param oldName The current name of the view to be renamed.
     *  @param newName The new name of the view.
     */
    public final void renameView(String oldName, String newName) {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.viewRenameAllowed, "viewRenameAllowed");
        renameViewInternal(oldName, newName);
    }

    final void renameViewInternal(String oldName, String newName) {
        MicroworldView view = getView(oldName);
        if (view != null)
            view.viewName = newName;
    }

    /* Gets the view that is either after or before the view with the supplied name */
    public MicroworldView getView(String name, boolean upwards) {
        int viewPos = getViewIndex(name);
        if (viewPos == -1) return null;
        if (viewPos >= (viewList.length-1) && upwards) return null;
        if (viewPos <= 0 && !upwards) return null;
        if (upwards)
            return viewList[viewPos+1];
        else
            return viewList[viewPos-1];
    }

    public int advanceView(String name, boolean upwards) {
        int viewPos = getViewIndex(name);
        if (viewPos == -1) {
            System.out.println("Inconsistency error in MicroworldViewList advanceView()");
            return -1;
        }

        int newPos = viewPos-1;
        if (upwards)
            newPos = viewPos+1;
        MicroworldView view = viewList[viewPos];
        MicroworldView secView = viewList[newPos];
        viewList[newPos] = view;
        viewList[viewPos] = secView;
        return newPos;
    }

    public MicroworldView getView(String name) {
        if (name == null) return null;

        int l = viewList.length;
        for (int i=0; i<l; i++) {
            if (viewList[i].viewName.equals(name))
                return viewList[i];
        }
        return null;
    }

    public int getViewIndex(String name) {
        if (name == null) return -1;

        int l = viewList.length;
        for (int i=0; i<l; i++) {
            if (viewList[i].viewName.equals(name))
                return i;
        }
        return -1;
    }

    public int getViewIndex(MicroworldView view) {
        if (view == null) return -1;

        int l = viewList.length;
        for (int i=0; i<l; i++) {
            if (viewList[i] == view)
                return i;
        }
        return -1;
    }

    public String[] getViewNames() {
        String[] viewNames = new String[viewList.length];
        for (int i=0; i<viewNames.length; i++)
            viewNames[i] = viewList[i].viewName;
        return viewNames;
    }

    private String makeUnique(String name) {
        if (name == null || name.trim().length() == 0)
            name = containerBundle.getString("View");
        String initialName = name;
        String proposedName = initialName;
        int counter = 0;
        while (true) {
            boolean proposedNameChanged = false;
            for (int i=0; i<viewList.length; i++) {
                if (viewList[i].viewName.equals(proposedName)) {
//System.out.println("Found view with the same name as view " + name + ". Index: " + i);
                    counter++;
                    proposedName = initialName + ' ' + counter;
                    proposedNameChanged = true;
                    break;
                }
            }
            if (!proposedNameChanged)
                break;
        }
        return proposedName;
    }

    public final void clear() {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.viewRemovalAllowed, "viewRemovalAllowed");
        clearInternal();
    }

    final void clearInternal() {
        viewList = new MicroworldView[0];
    }

    public int size() {
        return viewList.length;
    }

    public Object clone() {
        Object o = null;
        try{
            o = super.clone();
            ((MicroworldViewList) o).container = container;
        }catch (CloneNotSupportedException exc) {}
        return o;
    }

/*    public MicroworldViewList copy() {
        MicroworldViewList mwdList = new MicroworldViewList();
        for (int i=0; i<viewList.length; i++) {
            mwdList.addView(new MicroworldView(viewList[i]);
        }
    }
*/
    public void writeExternal(ObjectOutput out) throws java.io.IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION, 1);
        fieldMap.put("Views", viewList);
        out.writeObject(fieldMap);


//        System.out.println("MicroworldViewList contains: " + viewList.length + ",  writeExternal() size: " + ESlateContainerUtils.getFieldMapContentLength(fieldMap));
    }

    public void readExternal(ObjectInput in) throws java.io.IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        viewList = (MicroworldView[]) fieldMap.get("Views");
    }

    /* This method saves the background icons of all the microworld views in separate
     * files in the structfile, where microworld saving is done. The 'bgrIconFileName'
     * of each MicroworldView is set to the file name of the icon in the structfile. Only
     * the new icons are saved, so this way microworld saving becomes a lot faster, when
     * the view bgr icons do not change, which is common at mwd runtime.
     * This methods except for the views contained in the microworld's view list also treats
     * the current view of the microworld, which is a separate non-immutable view, which
     * always exists, but which is not included in the MicroworldViewList.
     */
    protected void saveViewIcons(ESlateContainer container, StructFile structfile) {
        // Check if there exists a directory with the new "Microworld view icons"
        Entry viewIconDir = null;
        try {
            structfile.changeDir(structfile.getRootEntry());
            viewIconDir = structfile.findEntry(VIEW_ICON_DIR_NAME);
        }catch (Throwable thr) {
//            System.out.println("Unable to save view icons. Error: " + thr.getMessage());
//            return;
        }
        boolean viewIconDirJustCreated = false;
        if (viewIconDir == null) {
            try{
                viewIconDir = structfile.createDir(VIEW_ICON_DIR_NAME);
            }catch (Throwable thr) {
                System.out.println("Unable to save view icons. The view directory cannot be created. Error: " + thr.getMessage());
                return;
            }
            viewIconDirJustCreated = true;
        }

        // Next we create an array with all the filenames of the saved (in the structfile) icons.
        String[] iconFileNames = new String[viewList.length+1];
        for (int i=0; i<viewList.length; i++)
            iconFileNames[i] = viewList[i].bgrIconFileName;
        iconFileNames[iconFileNames.length-1] = container.currentView.bgrIconFileName;

        /* Next we find the unique values in the 'iconFileNames' array. Its is very common
         * for views to share icons and these icons should not be stored more than once.
         */
        ArrayList uniqueFileNames = new ArrayList();
        for (int i=0; i<iconFileNames.length; i++) {
            String fileName = iconFileNames[i];
            if (fileName == null) continue;
            uniqueFileNames.add(fileName);
            for (int k=i+1; k<iconFileNames.length; k++) {
                if (fileName.equals(iconFileNames[k]))
                    iconFileNames[k] = null;
            }
        }

        /* Next we get all the new view icons. These are the icons which have just been set
         * and have not been saved yet in the structfile. For each such file we generate
         * the name of the file where the view will be saved. This file name is then
         * assigned to the corresponding views.
         */
        NewRestorableImageIcon[] viewNewIcons = new NewRestorableImageIcon[viewList.length+1];
        for (int i=0; i<viewList.length; i++) {
            /* The following 'if' ensures that saved icons which have not been loaded yet,
             * will not be loaded. Only new icons will be checked.
             */
            if (viewList[i].bgrIconFileName == null)
                viewNewIcons[i] = viewList[i].getBackgroundIcon(this, structfile);
        }
        if (container.currentView.bgrIconFileName == null)
            viewNewIcons[viewNewIcons.length-1] = container.currentView.getBackgroundIcon(this, structfile);

        /* Next find the unique icons among the 'viewNewIcons' and generate a unique file
         * name for each of them.
         */
        ArrayList uniqueNewIcons = new ArrayList();
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

        /* Now we have all the unique new icons of the microworld's views along with the file
         * names they should be saved in the structfile. If the directory in the structfile,
         * where the icons are saved has just been created, then simply save all the icons
         * in it. If the directory existed, then we have to check all the files that exist
         * in it. Some of them may be deleted because they refer to views which do not
         * exist, some new may be created and some may be left intact.
         */
        try{
            structfile.changeDir(viewIconDir);
        }catch (Throwable thr) {
            System.out.println("Unable to save view icons. Cannot change dir to the view icon directory. Error: " + thr.getMessage());
            return;
        }
        String[] existingIconFiles = null;
        try{
            existingIconFiles = structfile.listFiles();
        }catch (Throwable thr) {
            System.out.println("Unable to synchronize current contents of the view icon directory. Error: " + thr.getMessage());
        }

        for (int i=0; existingIconFiles != null && i<existingIconFiles.length; i++) {
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
        for (int i=0; i<uniqueNewIcons.size(); i++) {
            try{
//System.out.println("Saving new view icon file: " + (String) newIconFileNames.get(i));
                Entry iconFile = structfile.createFile((String) newIconFileNames.get(i));
                BufferedOutputStream bos = new BufferedOutputStream(new StructOutputStream(structfile, iconFile));
                ((NewRestorableImageIcon) uniqueNewIcons.get(i)).saveImage(NewRestorableImageIcon.PNG, bos);
                bos.flush();
                bos.close();
//System.out.println("Icon saved");
            }catch (Throwable thr) {
				thr.printStackTrace();
                System.out.println("Unable to save view icon. Cannot write the icon file. Error: " + thr.getMessage());
            }
        }
        try {
            structfile.changeDir(structfile.getRootEntry());
        }catch (Throwable thr) {}
    }
}