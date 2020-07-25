package gr.cti.eslate.base.container;

import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.util.Hashtable;


/* 'ExtendedEventSetDescriptor' class is declared for the sole purpose of defining end-user
 * names for the event methods described in an EventSetDescriptor.
 */
public class ExtendedEventSetDescriptor extends EventSetDescriptor {
    Hashtable endUserMethodNames = new Hashtable();


    public ExtendedEventSetDescriptor(Class sourceClass,
                               String eventSetName,
                               Class listenerType,
                               String listenerMethodName,
                               String endUserMethodName) throws IntrospectionException {
        super(sourceClass, eventSetName, listenerType, listenerMethodName);
        if (endUserMethodName == null || listenerMethodName == null)
            throw new NullPointerException();
        if (endUserMethodName != null && listenerMethodName != null)
            endUserMethodNames.put(listenerMethodName, endUserMethodName);
    }

    public ExtendedEventSetDescriptor(Class sourceClass,
                               String eventSetName,
                               Class listenerType,
                               String[] listenerMethodNames,
                               String[] methodUserNames,
                               String addListenerMethodName,
                               String removeListenerMethodName) throws IntrospectionException {

        super(sourceClass, eventSetName, listenerType, listenerMethodNames,
              addListenerMethodName, removeListenerMethodName);
        if (listenerMethodNames == null || methodUserNames == null)
            throw new NullPointerException();
        if (listenerMethodNames.length != methodUserNames.length)
            throw new IllegalArgumentException("The number of end-user method names should be the same as the number of methods");
        for (int i=0; i<listenerMethodNames.length; i++)
            endUserMethodNames.put(listenerMethodNames[i], methodUserNames[i]);
    }

    public String getEndUserMethodName(String methodName) {
        return (String) endUserMethodNames.get(methodName);
    }
}
