package gr.cti.eslate.database.engine;


public class InsufficientPrivilegesException extends Exception {
    public InsufficientPrivilegesException() {};
    public InsufficientPrivilegesException(String msg) {
        super(msg);
    }
}

