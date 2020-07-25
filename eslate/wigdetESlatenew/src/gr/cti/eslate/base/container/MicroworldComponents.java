package gr.cti.eslate.base.container;

import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;


public class MicroworldComponents {
    protected ESlateComponentArray components;
    protected ESlateComponent activeComponent = null;

    public MicroworldComponents() {
        components = new ESlateComponentArray();
    }

    public int indexOf(Object object) {
        for (int i=0; i<components.size(); i++) {
            if (components.get(i).object == object)
                return i;
        }
        return -1;
    }

    public int indexOf(ESlateComponent component) {
        for (int i=0; i<components.size(); i++) {
            if (components.get(i) == component)
                return i;
        }
        return -1;
    }

    public int indexOf(String componentName) {
        for (int i=0; i<components.size(); i++) {
            if (components.get(i).handle.getComponentName().equals(componentName))
                return i;
        }
        return -1;
    }

    public int size() {
        return components.size();
    }

    public Object[] getComponents() {
        Object[] objects = new Object[components.size()];
        for (int i=0; i<objects.length; i++)
            objects[i] = components.get(i).object;
        return objects;
    }

    public ESlateComponent[] getESlateComponents() {
        return components.toArray();
/*        Object[] objects = new Object[components.size()];
        for (int i=0; i<objects.length; i++)
            objects[i] = components.get(i).object;
        return objects;
*/
    }

    public String[] getComponentNames() {
        String[] names = new String[components.size()];
        for (int i=0; i<names.length; i++)
            names[i] = components.get(i).handle.getComponentName();
        return names;
    }

    public ESlateComponent getComponent(String name) {
        int index = indexOf(name);
        if (index == -1) return null;
        ESlateComponent component = components.get(index);
        return component;
    }

    public ESlateInternalFrame getComponentFrame(String name) {
        ESlateComponent component = getComponent(name);
        if (component == null)
            return null;
        return component.frame;
    }

    public ESlateComponent getComponent(Object component) {
        for (int i=0; i<components.size(); i++) {
            if (components.get(i).object == component)
                return components.get(i);
        }
        return null;
    }

    public ESlateInternalFrame getComponentFrame(Object component) {
        ESlateComponent ecomponent = getComponent(component);
        if (ecomponent == null)
            return null;
        return ecomponent.frame;
    }

    public ESlateInternalFrame[] getComponentFrames() {
        // Count the visible components. Only the visual components have frames;
        ESlateInternalFrame[] frames = new ESlateInternalFrame[getFrameCount()];
        int k = 0;
        for (int i=0; i<components.size(); i++) {
            if (components.get(i).frame != null) {
                frames[k] = components.get(i).frame;
                k++;
            }
        }
        return frames;
    }

    public ESlateComponent getComponent(ESlateInternalFrame frame) {
        int count = size();
        for (int i=0; i<count; i++) {
            if (components.get(i).frame == frame)
                return components.get(i);
        }
        return null;
    }

    public ESlateComponent[] getComponentsWithESlateMenuBar() {
        ESlateComponentArray componentArray = new ESlateComponentArray();
        for (int i=0; i<components.size(); i++) {
            if (components.get(i).desktopItem.displaysESlateMenuBar())
                componentArray.add(components.get(i));
        }
        return componentArray.toArray();
    }

    public ESlateComponent[] getComponentsUsingGlassPane() {
        ESlateComponentArray componentArray = new ESlateComponentArray();
        for (int i=0; i<components.size(); i++) {
            if (components.get(i).desktopItem.usesGlassPane())
                componentArray.add(components.get(i));
        }
        return componentArray.toArray();
    }

    public ESlateComponent[] getComponentsOfClass(String className) {
        ESlateComponentArray array = new ESlateComponentArray();
        for (int i=0; i<components.size(); i++) {
            if (components.get(i).object.getClass().getName().equals(className))
                array.add(components.get(i));
        }
        return array.toArray();
    }

    /* Returns the number of components of the specified class in the microworld */
    public int getComponentsOfClassCount(String className) {
        int counter = 0;
        for (int i=0; i<components.size(); i++) {
            if (components.get(i).object.getClass().getName().equals(className))
                counter++;
        }
        return counter;
    }

    public ESlateComponent[] getVisualComponents() {
        // Count the visible components. Only the visual components have frames;
        ESlateComponentArray array = new ESlateComponentArray();
        for (int i=0; i<components.size(); i++) {
            if (components.get(i).visualBean)
                array.add(components.get(i));
        }
        return array.toArray();
    }

    public int getVisualComponentCount() {
        int count = 0;
        for (int i=0; i<components.size(); i++) {
            if (components.get(i).visualBean)
                count++;
        }
        return count;
    }

    public int getFrameCount() {
        int count = 0;
        for (int i=0; i<components.size(); i++) {
            if (components.get(i).frame != null)
                count++;
        }
        return count;
    }

/*    public ESlateComponent getModalFrame() {
        for (int i=0; i<components.size(); i++) {
            ESlateComponent comp = components.get(i);
            if (comp.frame != null && comp.frame.isModal()) {
                return comp;
            }
        }
        return null;
    }

    void resetModalFrame() {
        int compoCount = components.size();
        for (int i=0; i<compoCount; i++) {
            ESlateComponent comp = components.get(i);
            if (comp.frame != null && comp.frame.isModal())
                comp.frame.setModal(false);
        }
    }
*/
    int remove(ESlateComponent component) {
        int removed = components.removeElements(component);
        if (removed != 0 && activeComponent == component)
            activeComponent = null;
        return removed;
    }

    void clear() {
        components.clear();
        activeComponent = null;
    }
}

