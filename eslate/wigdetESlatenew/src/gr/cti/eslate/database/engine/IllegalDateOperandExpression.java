package gr.cti.eslate.database.engine;


public class IllegalDateOperandExpression extends Exception {
    public String message;
    public IllegalDateOperandExpression() {};
    public IllegalDateOperandExpression(String msg) {
        super(msg);
        message = msg;
    }
}
