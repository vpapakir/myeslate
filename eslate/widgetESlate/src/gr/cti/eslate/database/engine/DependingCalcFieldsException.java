package gr.cti.eslate.database.engine;


public class DependingCalcFieldsException extends Exception {
    public String message;
    public DependingCalcFieldsException() {};
    public DependingCalcFieldsException(String msg) {
        super(msg);
        message = msg;
    }
}
