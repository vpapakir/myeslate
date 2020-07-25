package gr.cti.eslate.database.engine;


public interface ReferenceToExternalFile {

    public abstract boolean isReferenceToExternalFile();

    public abstract Object getReference();

    public abstract void setReferenceToExternalFile(Table table, AbstractTableField f, boolean isReference);

}