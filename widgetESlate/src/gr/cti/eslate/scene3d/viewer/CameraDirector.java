package gr.cti.eslate.scene3d.viewer;
//~--- JDK imports ------------------------------------------------------------

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//~--- classes ----------------------------------------------------------------

/**
 * Created by A.Mantes at 4 Δεκ 2003, 4:16:07 μμ
 */
public class CameraDirector {
    ArrayList<Camera> cameras = new ArrayList<Camera>();
    int               activeCam;

    //~--- constructors -------------------------------------------------------

    public CameraDirector() {}

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param camera
     */
    public void addCamera(Camera camera) {
        cameras.add(camera);
    }

    /**
     * Method description
     *
     *
     * @param camera
     * @param position
     */
    public void addCamera(Camera camera, int position) {
        cameras.add(position, camera);
    }

    private static void center(Component component,
                               Dimension containerDimension) {
        Dimension sz = component.getSize();
        int       x  = ((containerDimension.width - sz.width) / 2);
        int       y  = ((containerDimension.height - sz.height) / 2);

        component.setLocation(x, y);
    }

    /**
     * Method description
     *
     *
     * @param position
     */
    public void removeCamera(int position) {
        cameras.remove(position);
    }

    /**
     * Shows a modal dialog containing the available cameras
     * and requests selection of one of them. Returns the selected one.
     */
    public void showCameraSelectionDialog() {
        CameraSelectionDialog dialog = new CameraSelectionDialog();

        dialog.setVisible(true);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public Camera getActiveCamera() {
        return cameras.get(activeCam);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public int getActiveCameraIndex() {
        return activeCam;
    }

    /**
     * Method description
     *
     *
     * @param camIndex
     *
     * @return
     */
    public Camera getCamera(int camIndex) {
        return cameras.get(camIndex);
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param camIndex
     */
    public void setActiveCamera(int camIndex) {
        if ((camIndex < 0) || (camIndex >= cameras.size())) {

            // System.out.println("There is no camera with such index");
            activeCam = camIndex;
        }
    }

    //~--- inner classes ------------------------------------------------------

    class CameraSelectionDialog extends JFrame {
        private static final long serialVersionUID = 1297138242418908591L;

        //~--- fields ---------------------------------------------------------

        private volatile int selectedIndex;

        //~--- constructors ---------------------------------------------------

        public CameraSelectionDialog() {
            super();
            setUndecorated(true);
            setResizable(false);

            // setLocation(CameraDirector.this.getActiveCamera().owner.getLocation());
            setTitle("Camera Selection");

            String[] cameraNames =
                new String[CameraDirector.this.cameras.size()];

            for (int i = 0; i < CameraDirector.this.cameras.size(); i++) {
                cameraNames[i] = CameraDirector.this.getCamera(i).toString();
            }

            final JList camerasList = new JList(cameraNames);

            camerasList.setSelectedIndex(
                CameraDirector.this.getActiveCameraIndex());
            camerasList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            camerasList.setSelectedIndex(0);

            JScrollPane scroller = new JScrollPane(camerasList);

            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(BorderLayout.NORTH,
                                 new JLabel("Select camera:"));
            getContentPane().add(BorderLayout.CENTER, scroller);

            JPanel okPanel = new JPanel();

            okPanel.setLayout(new BoxLayout(okPanel, BoxLayout.X_AXIS));
            okPanel.add(Box.createGlue());

            JButton ok = new JButton("OK");

            camerasList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    selectedIndex = camerasList.getSelectedIndex();
                    CameraDirector.this.setActiveCamera(selectedIndex);

                    Camera activeCamera =
                        CameraDirector.this.getCamera(selectedIndex);

                    activeCamera.update();
                }
            });
            ok.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectedIndex = camerasList.getSelectedIndex();
                    CameraDirector.this.setActiveCamera(selectedIndex);
                    setVisible(false);
                    dispose();
                }
            });
            okPanel.add(ok);
            okPanel.add(Box.createGlue());
            getContentPane().add(BorderLayout.SOUTH, okPanel);
            pack();
            center(this, Toolkit.getDefaultToolkit().getScreenSize());
        }
    }
}
