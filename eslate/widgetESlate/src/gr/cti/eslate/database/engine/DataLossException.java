package gr.cti.eslate.database.engine;


public class DataLossException extends Exception {
    public String message;
    public DataLossException() {};
    public DataLossException(String msg) {
        super(msg);
        message = msg;
    }
}
