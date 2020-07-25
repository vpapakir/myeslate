package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The class which stores metadata about a microworld. The main reason these data
 * are not part of the Microworld class itself, is the fact that microworlds have to
 * be searched based on criteria that have to do with their metadata. Therefore the
 * metadata is a different class, which is actually stored in a different archieve in
 * the microworld structured storage file, from the archieve of the actual microworld state.
 * Copyright:    Copyright (c) 2001
 * @author George Tsironis
 * @version 1.0
 */

public class MicroworldMetadata implements Externalizable {
    public static final int FORMAT_VERSION = 1;
    static final long serialVersionUID = 12;

    /** The title of the microworld.
     */
    private String title = null;
    /** The subject of the microworld.
     */
    private String subject = null;
    /** The company of the microworld.
     */
    private String company = null;
    /** The keywords of the microworld.
     */
    private String keywords = null;
    /** The authors of the microworld.
     */
    private MicroworldAuthor[] authors = null;
    /** The name of the category of the microworld.
     */
    private String categoryName = null;
    /** The category id of the microword.
     */
    private int category = Microworld.CUSTOM_CATEGORY;
    /** The comments for the microworld.
     */
    private String comments = null;

    public MicroworldMetadata() {
    }

    /** Sets the title of the microworld.
     */
    void setTitle(String t) {
        title = t;
    }
    /** Returns the title of the microworld.
     */
    public String getTitle() {
        return title;
    }

    /** Sets the subject of the microworld.
     */
    void setSubject(String subj) {
        this.subject = subj;
    }
    /** Returns the subject of the microworld.
     */
    public String getSubject() {
        return subject;
    }

    /** Sets the company that owns of the microworld.
     */
    void setCompany(String comp) {
        company = comp;
    }
    /** Returns the company that owns the microworld.
     */
    public String getCompany() {
        return company;
    }

    /** Sets the keyworrds for this microworld. The keywords are given in the form of
     *  a simple String, containings any number of keywords, separated in some way.
     */
    void setKeywords(String keys) {
        keywords = keys;
    }
    /** Returns the keywords.
     */
    public String getKeywords() {
        return keywords;
    }

    /** Sets the authors of the microworld.
     */
    void setAuthors(MicroworldAuthor[] authors) {
        this.authors = authors;
    }
    /** Returns the authors of the microworld.
     */
    public MicroworldAuthor[] getAuthors() {
        return authors;
    }

    /** Sets the category of the microworld. There are some predefined categories,
     *  like 'Geography', 'Mathematics',... but custom category names are allowed too.
     *  See category identifiers in Microworld class.
     */
    void setCategory(int categoryID) {
        category = categoryID;
    }
    /** Returns the category id of the microworld.
     */
    public int getCategory() {
        return category;
    }

    /** Sets the name of the category of the Microworld. If the category name is one of the
     *  predefined localized catefory names, then the 'category' of the microworld is also
     *  adjusted. If the category name is not among the known ones, then the category id
     *  of the microworld is set to CUSTOM_CATEGORY.
     */
    void setCategoryName(String name) {
        categoryName = name;
    }

    /** Returns the category name of the microworld.
     */
    public String getCategoryName() {
        return categoryName;
    }

    /** Sets the comments for the microworld.
     */
    void setComments(String c) {
        comments = c;
    }
    /** Returns the comments for the microworld.
     */
    public String getComments() {
        return comments;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("Title", title);
        fieldMap.put("Subject", subject);
        fieldMap.put("Company", company);
        fieldMap.put("Keywords", keywords);
        fieldMap.put("Authors", authors);
        fieldMap.put("Category name", categoryName);
        fieldMap.put("Category", category);
        fieldMap.put("Comments", comments);
        out.writeObject(fieldMap);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        title = (String) fieldMap.get("Title");
        subject = (String) fieldMap.get("Subject");
        company = (String) fieldMap.get("Company");
        keywords = (String) fieldMap.get("Keywords");
        authors = (MicroworldAuthor[]) fieldMap.get("Authors");
        categoryName = (String) fieldMap.get("Category name");
        category = fieldMap.get("Category", Microworld.CUSTOM_CATEGORY);
        comments = (String) fieldMap.get("Comments");
    }
}