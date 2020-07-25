package gr.cti.eslate.database.engine;


public class JoinNoCommonFieldsException extends Exception {
    public String message;
    public JoinNoCommonFieldsException() {};
    public JoinNoCommonFieldsException(String msg) {
        super(msg);
        message = msg;
    }
}
