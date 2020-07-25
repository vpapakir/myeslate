package gr.cti.eslate.scene3d.viewer;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

//~--- classes ----------------------------------------------------------------

/**
 * Created by A.Mantes at 4 Δεκ 2003, 1:36:31 μμ
 *
 * This is the list of all 3d objects rendered in scene
 */
public class Object3DList {
    ArrayList<Object3D> object3DList;

    //~--- constructors -------------------------------------------------------

    public Object3DList() {
        object3DList = new ArrayList<Object3D>();
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param obj
     *
     * @return
     */
    public Object3D add(Object3D obj) {
        object3DList.add(obj);

        return obj;
    }

    /**
     * Method description
     *
     *
     * @param obj
     * @param index
     *
     * @return
     */
    public Object3D add(Object3D obj, int index) {
        object3DList.add(index, obj);

        return obj;
    }

    /**
     * Method description
     *
     *
     * @param index
     */
    public void remove(int index) {
        object3DList.remove(index);
    }

    /**
     * Method description
     *
     *
     * @param obj
     */
    public void remove(Object3D obj) {
        object3DList.remove(obj);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param index
     *
     * @return
     */
    public Object3D get(int index) {
        Object o = null;

        try {
            o = object3DList.get(index);
        } catch (Exception exc) {

            // System.out.println("Invalid index. Returning null object");
            exc.printStackTrace();

            return null;
        }

        if (!(o instanceof Object3D)) {

            // System.out.println("Object not instanceof IObject3D. Returning null");
            return null;
        }

        return (Object3D) o;
    }
}
