package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This class contains information about an author of a microworld.
 * Copyright:    Copyright (c) 2001
 * @author George Tsironis
 * @version 1.0
 */

public class MicroworldAuthor implements Externalizable {
    public static final int FORMAT_VERSION = 1;
    static final long serialVersionUID = 12;
    String name, surname, email;

    /** For use by the Externalization mechanism only.
     */
    public MicroworldAuthor() {
    }

    public MicroworldAuthor(String name, String surname) {
        if (surname == null || surname.trim().length() == 0)
            throw new NullPointerException("A microworld author's surname cannot be null");
        this.name = name;
        this.surname = surname;
    }

    /** Sets the author's first name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Returns the author's first name.
     */
    public String getName() {
        return name;
    }

    /** Returns the author's surname.
     */
    public String getSurname() {
        return surname;
    }

    /** Sets the author's e-mail address.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /** Returns the author's e-mail address.
     */
    public String getEmail() {
        return email;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("Name", name);
        fieldMap.put("Surname", surname);
        fieldMap.put("E-mail", email);
        out.writeObject(fieldMap);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        name = (String) fieldMap.get("Name");
        surname = (String) fieldMap.get("Surname");
        email = (String) fieldMap.get("E-mail");
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        if (name != null) {
            buff.append(name);
            buff.append(' ');
        }
        buff.append(surname);
        if (email != null) {
            buff.append(' ');
            buff.append(email);
        }
        return buff.toString();
    }
}