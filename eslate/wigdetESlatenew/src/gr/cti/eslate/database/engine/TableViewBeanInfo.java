/**
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: Nov 28, 2002
 * Time: 7:38:17 PM
 * To change this template use Options | File Templates.
 */
package gr.cti.eslate.database.engine;

import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;

public class TableViewBeanInfo extends TableBeanInfo {
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] descriptors = super.getPropertyDescriptors();
        try{
            PropertyDescriptor pd1 = new PropertyDescriptor(
                                            "synchronizedActiveRecord",
                                            TableView.class,
                                            "isSynchronizedActiveRecord",
                                            "setSynchronizedActiveRecord");
            pd1.setDisplayName(bundle.getString("SynchronizedActiveRecord"));
            pd1.setShortDescription(bundle.getString("SynchronizedActiveRecordTip"));

            PropertyDescriptor[] tmp = descriptors;
            descriptors = new PropertyDescriptor[tmp.length+1];
            int i = 0;
            for (i=0; i<tmp.length; i++)
                descriptors[i] = tmp[i];
            descriptors[i] = pd1;

            return descriptors;
        }catch (IntrospectionException exc) {
            System.out.println("IntrospectionException: " + exc.getMessage());
            return null;
        }
    }
}
