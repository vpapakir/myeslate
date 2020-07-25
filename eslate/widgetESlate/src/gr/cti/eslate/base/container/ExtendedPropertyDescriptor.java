package gr.cti.eslate.base.container;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;


/* 'ExtendedPropertyDescriptor' class is declared for the sole purpose of defining custom
 * property categories. Also is provides early-access support for the 'preferred' properties,
 * which are defined in JDK 1.2.
 */
public class ExtendedPropertyDescriptor extends PropertyDescriptor {
    private boolean preferred = false;
    private String category = null;

    public ExtendedPropertyDescriptor(String propertyName, Class beanClass) throws IntrospectionException {
        super(propertyName, beanClass);
    }

    public ExtendedPropertyDescriptor(String propertyName,
                                      Class beanClass,
                                      String getterName,
                                      String setterName) throws IntrospectionException {
        super(propertyName, beanClass, getterName, setterName);
    }

    public ExtendedPropertyDescriptor(String propertyName,
                                      Method getter,
                                      Method setter) throws IntrospectionException {
        super(propertyName, getter, setter);
    }

    public boolean isPreferred() {
        return preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

