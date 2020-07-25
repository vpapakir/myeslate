package gr.cti.eslate.database.engine;


public class JoinIncompatibleFieldTypesException extends Exception {
    public String message;
    public JoinIncompatibleFieldTypesException() {};
    public JoinIncompatibleFieldTypesException(String msg) {
        super(msg);
        message = msg;
    }
}
