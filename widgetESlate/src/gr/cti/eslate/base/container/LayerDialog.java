package gr.cti.eslate.base.container;

import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;
import gr.cti.eslate.utils.ESlateOptionPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


class LayerDialog extends JFrame {
    ResourceBundle layerDialogBundle;
    JList layerList;
	LayerListModel layerListModel = null;
    ImageIcon downIcon = new ImageIcon(getClass().getResource("images/downArrow.gif"));
    ImageIcon upIcon = new ImageIcon(getClass().getResource("images/upArrow.gif"));
    ImageIcon deleteIcon = new ImageIcon(getClass().getResource("images/delete.gif"));
    ImageIcon addIcon = new ImageIcon(getClass().getResource("images/add.gif"));
    JButton downButton, upButton, deleteButton, addButton;
    JScrollPane scrollPane;
    ESlateContainer container;
    /* Keep track of the current layer-component mapping. After the layers are edited
     * and the user clicks OK, the new levels for the pre-existing layers are assigned
     * to the components which existed in those layers.
     */
    Hashtable layerComponents = new Hashtable();

    public LayerDialog(ESlateContainer cont) {
        super(); //new JFrame(), true);
        this.container = cont;
        /* This is an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        if (cont.microworld != null)
            cont.microworld.checkActionPriviledge(cont.microworld.mwdLayerMgmtAllowed, "mwdLayerMgmtAllowed");

        setIconImage(container.getAppIcon());

        layerDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.LayerDialogBundle", Locale.getDefault());
        String title = layerDialogBundle.getString("DialogTitle");
        setTitle(title);

        refreshLayerComponents();

        String[] layerNames = container.mwdLayers.getLayerNames();
		layerListModel = new LayerListModel();
		for (int i=0; i<layerNames.length; i++) {
			layerListModel.addElement(layerNames[i]);
		}
        layerList = new JList(layerListModel);

        scrollPane = new JScrollPane(layerList);
        Dimension scrollPaneSize = new Dimension(235, 4*25+50);
        scrollPane.setMaximumSize(scrollPaneSize);
        scrollPane.setPreferredSize(scrollPaneSize);
        scrollPane.setMinimumSize(scrollPaneSize);

        upButton = new JButton(upIcon);
        downButton = new JButton(downIcon);
        addButton = new JButton(addIcon);
        deleteButton = new JButton(deleteIcon);

        Dimension buttonSize = new Dimension(27, 22);
        upButton.setMaximumSize(buttonSize);
        upButton.setPreferredSize(buttonSize);
        upButton.setMinimumSize(buttonSize);
        downButton.setMaximumSize(buttonSize);
        downButton.setPreferredSize(buttonSize);
        downButton.setMinimumSize(buttonSize);
        addButton.setMaximumSize(buttonSize);
        addButton.setPreferredSize(buttonSize);
        addButton.setMinimumSize(buttonSize);
        deleteButton.setMaximumSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        deleteButton.setMinimumSize(buttonSize);
        upButton.setToolTipText(layerDialogBundle.getString("UpButtonTip"));
        downButton.setToolTipText(layerDialogBundle.getString("DownButtonTip"));
        addButton.setToolTipText(layerDialogBundle.getString("AddButtonTip"));
        deleteButton.setToolTipText(layerDialogBundle.getString("DeleteButtonTip"));

        JPanel listButtonPanel = new JPanel();
        listButtonPanel.setLayout(new BoxLayout(listButtonPanel, BoxLayout.Y_AXIS));

        listButtonPanel.add(Box.createGlue());
        listButtonPanel.add(upButton);
        listButtonPanel.add(Box.createVerticalStrut(3));
        listButtonPanel.add(downButton);
        listButtonPanel.add(Box.createVerticalStrut(3));
        listButtonPanel.add(addButton);
        listButtonPanel.add(Box.createVerticalStrut(3));
        listButtonPanel.add(deleteButton);
        listButtonPanel.add(Box.createGlue());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(scrollPane);
        topPanel.add(Box.createHorizontalStrut(5));
        topPanel.add(listButtonPanel);
        topPanel.add(Box.createGlue());
        topPanel.setBorder(new EmptyBorder(3,4,3,4));

        // The button panel (APPLY, OK, CANCEL)
        JButton okButton = new JButton(layerDialogBundle.getString("OK"));
        Color color128 = new Color(0, 0, 128);
        buttonSize = new Dimension(90, 25);
        okButton.setForeground(color128);
        buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0,0,0,0);
        okButton.setMargin(zeroInsets);

        final JButton cancelButton = new JButton(layerDialogBundle.getString("Cancel"));
        cancelButton.setForeground(color128);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setMargin(zeroInsets);

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createGlue());

        buttonPanel.setBorder(new EmptyBorder(5,5,5,5));

        // The main panel
        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buttonPanel);

        getContentPane().add(mainPanel);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] layerNames = new String[layerList.getModel().getSize()];
                for (int i=0; i<layerNames.length; i++)
                    layerNames[i] = (String) layerList.getModel().getElementAt(i);

                /* Check for layer name uniqueness */
                for (int i=0; i<layerNames.length; i++) {
                    String layerName = layerNames[i];
                    for (int k=i+1; k<layerNames.length; k++) {
                        if (layerNames[k].equals(layerName)) {
                            ESlateOptionPane.showMessageDialog(LayerDialog.this, layerDialogBundle.getString("SameLayerNamesError"), layerDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
                container.mwdLayers.setLayers(layerNames, container.mwdLayers.getDefaultLayerName());
                /* Layer order may change through this dialog. This means that existing
                 * layers may have to be re-arranged. When this layerDialog was constructed
                 * the component-layer mapping was recorded in the 'layerComponents'
                 * Hashtable. This mapping was updated dyring this Layerdialog's session,
                 * when layers were renamed or deleted. After setting the new layers,
                 * the components' levels are updated according to the new levels of the
                 * layers
                 */
                Enumeration layers = layerComponents.keys();
                while (layers.hasMoreElements()) {
                    String layerName = (String) layers.nextElement();
                    Integer layerLevel = new Integer(container.mwdLayers.getLayerLevel(layerName));
                    Vector compoVector = (Vector) layerComponents.get(layerName);
                    Enumeration compos = compoVector.elements();
                    while (compos.hasMoreElements()) {
                        ((ESlateInternalFrame) compos.nextElement()).setLayer(layerLevel);
                    }
                }

                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        upButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = layerList.getSelectedIndex();
                if (index == -1) return;
                if (index != 0) {
                    layerListModel.moveItem(index, index-1);
					layerList.setSelectedIndex(index-1);
                }else
                    upButton.setEnabled(false);
            }
        });
        downButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = layerList.getSelectedIndex();
                if (index == -1) return;
                if (index != layerList.getModel().getSize()-1) {
                    layerListModel.moveItem(index, index+1);
					layerList.setSelectedIndex(index+1);
                }else
                    downButton.setEnabled(false);
            }
        });
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				String newLayerName = (String) ESlateOptionPane.showInputDialog(LayerDialog.this,
									  layerDialogBundle.getString("NewLayerMsg"),
									  layerDialogBundle.getString("NewLayerTitle"),
									  JOptionPane.QUESTION_MESSAGE,
									  null,
									  null,
									  layerDialogBundle.getString("NewLayer"));

				if (newLayerName != null) {
					newLayerName = newLayerName.trim();
					if (newLayerName.length() == 0) return;
					if (layerListModel.contains(newLayerName)) {
						ESlateOptionPane.showMessageDialog(LayerDialog.this,
								layerDialogBundle.getString("NameExists"),
								layerDialogBundle.getString("Error"),
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					layerListModel.add(layerListModel.size(), newLayerName);
					int index = layerListModel.indexOf(newLayerName);
					layerList.setSelectedIndex(index);
				}
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				int[] selectedIndices = layerList.getSelectedIndices();
				for (int i=0; i<selectedIndices.length; i++) {
					moveComponentsToDefaultLayer((String) layerListModel.get(i));
				}
				layerListModel.removeItems(selectedIndices);
            }
        });

		layerList.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() != 2) return;
				if (isDefaultLayerSelected()) return;
				int selectedIndex = layerList.getSelectedIndex();
				if (selectedIndex == -1) return;
				String currentName = (String) layerListModel.get(selectedIndex);
				String newLayerName = (String) ESlateOptionPane.showInputDialog(LayerDialog.this,
									  layerDialogBundle.getString("RenameLayerMsg") + " \"" + currentName + '\"',
									  layerDialogBundle.getString("RenameLayerTitle"),
									  JOptionPane.QUESTION_MESSAGE,
									  null,
									  null,
									  currentName);
				if (newLayerName == null) return;
				newLayerName = newLayerName.trim();
				if (newLayerName.length() == 0) return;
				if (newLayerName.equals(currentName)) return;
				layerListModel.set(selectedIndex, newLayerName);
				// Update the layerComponents Hashtable
				if (layerComponents.containsKey(currentName)) {
					Vector v = (Vector) layerComponents.remove(currentName);
					layerComponents.put(newLayerName, v);
				}
			}
		});

        layerList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                JList list  = (JList) e.getSource();
				int selectedIndex = layerList.getSelectedIndex();
				if (selectedIndex == -1) {
					downButton.setEnabled(false);
					upButton.setEnabled(false);
					deleteButton.setEnabled(false);
					return;
				}
				boolean defaultLayerSelected = isDefaultLayerSelected();
                deleteButton.setEnabled(!defaultLayerSelected);// && !layerList.isEditing());

                if (selectedIndex == layerList.getModel().getSize()-1)
                    downButton.setEnabled(false);
                else
                    downButton.setEnabled(true);
                if (selectedIndex == 0)
                    upButton.setEnabled(false);
                else
                    upButton.setEnabled(true);
            }
        });
/*        layerList.addJEditListListener(new gr.cti.eslate.jeditlist.event.JEditListAdapter() {
            public void itemEdited(gr.cti.eslate.jeditlist.event.ItemEditedEvent e) {
                JEditList list = (JEditList) e.getSource();
                String layerName = (String) e.getPreviousValue();
                if (layerName.equals(container.mwdLayers.getDefaultLayerName())) {
                    container.mwdLayers.setDefaultLayerName((String) e.getNewValue());
                    list.setDeleteEnabled(false);
                    list.setCutEnabled(false);
                    deleteButton.setEnabled(false);
                }else
                    deleteButton.setEnabled(true);

                // Update the layerComponents Hashtable
                if (layerComponents.containsKey(layerName)) {
                    Vector v = (Vector) layerComponents.remove(layerName);
                    layerComponents.put(e.getNewValue(), v);
                }
            }
            public void editStarted(java.util.EventObject e) {
                deleteButton.setEnabled(false);
            }
            public void itemDeleted(gr.cti.eslate.jeditlist.event.ItemEvent e) {
            }
        });
*/
        //Intitialization
        layerList.requestFocus();
        layerList.setSelectedValue(container.mwdLayers.getDefaultLayerName(), true);

        // ESCAPE HANDLER
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = cancelButton.getModel();
                bm.setArmed(true);
                bm.setPressed(true);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = cancelButton.getModel();
                bm.setPressed(false);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().setDefaultButton(okButton);

        setResizable(false);
        ESlateContainerUtils.showDialog(this, container, true);
    }

    public void refreshLayerComponents() {
        String[] layerNames = container.mwdLayers.getLayerNames();
        for (int i=0;i<layerNames.length; i++) {
            int level = container.mwdLayers.getLayerLevel(layerNames[i]);
            Vector comps = container.getComponentFrames(level);
            if (comps.size() != 0)
                layerComponents.put(layerNames[i], comps);
        }
    }

	void moveComponentsToDefaultLayer(String fromLayer) {
		/* Check if any there exists any component at this layer. Move any component
		 * of this layer to the defaultLayer.
		 */
		String layerName = fromLayer; //(String) e.getValue();
		int level = container.mwdLayers.getLayerLevel(layerName);
//0                for (int i=0; i<container.componentFrames.size(); i++) {
//0                    ESlateInternalFrame fr = (ESlateInternalFrame) container.componentFrames.at(i);
		for (int i=0; i<container.mwdComponents.size(); i++) {
			if (!container.mwdComponents.components.get(i).visualBean)
				continue;
			ESlateComponent ecomponent = container.mwdComponents.components.get(i);
			ESlateInternalFrame fr = ecomponent.frame;
			if (fr != null && fr.getLayer() == level) {
				ESlateOptionPane.showMessageDialog(LayerDialog.this,
						layerDialogBundle.getString("LayersMoved0") + layerName +
						layerDialogBundle.getString("LayersMoved1") + ecomponent.handle.getComponentName() +
						layerDialogBundle.getString("LayersMoved2"),
					layerDialogBundle.getString("Warning"), JOptionPane.WARNING_MESSAGE);
//                        System.out.println("Moving " +  fr.eSlateHandle.getComponentName() + " to default layer");
				fr.setLayer(LayerInfo.DEFAULT_LAYER_Z_ORDER);
			}
		}

		// Update the layerComponents Hashtable
		if (layerComponents.containsKey(layerName)) {
			layerComponents.remove(layerName);
		}
	}

	boolean isDefaultLayerSelected() {
		String defaultLayerName = container.mwdLayers.getDefaultLayerName();
		Object[] values = layerList.getSelectedValues();
		for (int i=0; i<values.length; i++) {
			if (values[i].equals(defaultLayerName)) {
				return true;
			}
		}
		return false;
	}

	class LayerListModel extends DefaultListModel {
		public void moveItem(int from, int to) {
			Object movedItem = get(from);
			set(from, get(to));
			set(to, movedItem);
		}

		public void removeItems(int[] indices) {
			java.util.Arrays.sort(indices);
			for (int i=indices.length-1; i>=0; i--) {
				remove(indices[i]);
			}
		}
	}
}

