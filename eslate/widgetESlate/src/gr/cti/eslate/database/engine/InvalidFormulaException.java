package gr.cti.eslate.database.engine;


public class InvalidFormulaException extends Exception {
    public String message;
    public InvalidFormulaException() {};
    public InvalidFormulaException(String msg) {
        super(msg);
        Thread.currentThread().dumpStack();
        message = msg;
    }
}
