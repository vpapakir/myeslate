package gr.cti.eslate.database;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.objectspace.jgl.Array;


public class BooleanValues extends javax.swing.JComboBox {
    ResourceBundle valuesBundle = ResourceBundle.getBundle("gr.cti.eslate.database.BooleanValueList", Locale.getDefault());
    String emptyItem = " ";
    String trueValue = valuesBundle.getString("true");
    String falseValue = valuesBundle.getString("false");
    Array itemListenerArray;
    ActionListener actionListener;

    public BooleanValues() {
        super();
    	  addItem(trueValue);
        addItem(falseValue);
	      addItem(emptyItem);
	  }

    public void addItemListener(ItemListener aListener) {
        if (itemListenerArray == null)
            itemListenerArray = new Array();

        itemListenerArray.add(aListener);
        super.addItemListener(aListener);
    }

    public void addActionListener(ActionListener aListener) {
        actionListener = aListener;
        super.addActionListener(aListener);
    }

    public void setSelectedItem(Object item) {
        if (item == " " || item == null) {
            super.setSelectedItem(emptyItem);
            return;
        }

        if (item.getClass().equals(java.lang.Boolean.class)) {
            Boolean val = (Boolean) item;

            if (val == null)
                super.setSelectedItem(emptyItem);
            else if (val.booleanValue())
                super.setSelectedItem(trueValue);
            else
                super.setSelectedItem(falseValue);
        }else{
            if (item.equals("true") || item.equals(valuesBundle.getString("true")))
                super.setSelectedItem(trueValue);
            else
                super.setSelectedItem(falseValue);
        }
    }

    public void selectNext() {
        if (getSelectedItem() == null)
            setSelectedItem(Boolean.TRUE);
        else if (getSelectedItem().equals(trueValue))
            setSelectedItem(Boolean.FALSE);
        else
            setSelectedItem(null);
    }

    public void selectPrevious() {
        if (getSelectedItem() == null)
            setSelectedItem(Boolean.FALSE);
        else if (getSelectedItem().equals(falseValue))
            setSelectedItem(Boolean.TRUE);
        else
            setSelectedItem(null);
    }


	  public Object getSelectedItem() {
        Object selItem = super.getSelectedItem();
        if (selItem.equals(emptyItem))
            return null;
        else
            return selItem;
	  }
}



