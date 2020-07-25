package gr.cti.eslate.database.engine;


import com.objectspace.jgl.*;
//import java.io.Serializable;

/**
 * @version	2.0, May 01
 */
abstract public class OperandExpression {//  implements Serializable {
//    Stack postfix = new Stack();
//    Stack operatorStack = new Stack();
    Table table;

    public OperandExpression() {
    }

    protected abstract Object execute(int rowIndex, boolean calculatedFieldExpression);  //throws InvalidOperandExpressionException
//     {return null;};

    protected abstract Object execute() throws InvalidOperandExpressionException;
//     {return null;};

}