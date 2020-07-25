package gr.cti.eslate.database.engine;

import com.objectspace.jgl.*;
import java.text.ParsePosition;
import java.text.ParseException;
import java.util.StringTokenizer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;


/**
 * @version	2.0, May 01
 */
class NumericOperandExpression extends OperandExpression implements Serializable {
    /** The version of the storage format of the NumericOperandExpression class
     */
//    public static final String STR_FORMAT_VERSION = "1.1";
    public static final int FORMAT_VERSION = 2;

    Stack postfix = new Stack();
    Stack operatorStack = new Stack();
    boolean containsDoubleOperand = false;
    static final long serialVersionUID = 12;

    protected NumericOperandExpression(String exprString, Table tab)
    throws InvalidOperandExpressionException, InvalidDataFormatException {

        table = tab;
        Class fldType;

        //Initializing "fieldType" for test purposes
/*        fieldType = new Double("5").getClass();
        //The following stay in this method
        fldType = fieldType;
        if (fieldType.getName().equals("java.lang.Integer"))
*/
        fldType = new Double("5").getClass();

        StringBuffer exprStringBuff = new StringBuffer();
        char[] c = exprString.toCharArray();
        int count = 0;
        for (int k=0; k<exprString.length(); k++) {
            if (c[k] != ' ')
                exprStringBuff.append(c[k]);
            if (c[k] == '(')
                count++;
            if (c[k] == ')')
                count--;
            if (c[k] == '"') {
                k++;
//                System.out.println("Length: " + exprString.length());
                while (k<exprString.length() && c[k] != '"') {
//                    System.out.println(k + " " + c[k]);
                    exprStringBuff.append(c[k]);
                    k++;
                }
                if (k<exprString.length())
                    exprStringBuff.append(c[k]);
            }
        }

        if (count != 0)
            throw new InvalidOperandExpressionException(CDatabase.resources.getString("CBooleanOperandExpressionMsg1") + exprString + DBase.resources.getString("CBooleanOperandExpressionMsg2"));

        /* Delimit the operators using '|' delimiter. Don't touch the parts of the
         * exprString that are inside quotes (").
         */
        int i=0;
        int delimCount = 0;
        boolean operFound = false;
        while (i<exprStringBuff.length()) {
            switch (exprStringBuff.charAt(i)) {
                case '+':
                    exprStringBuff.insert(i, '|');
                    exprStringBuff.insert(i+2, '|');
                    delimCount = delimCount + 2;
                    i = i+3;
                    break;
                case '-':
                    if (i==0 || exprStringBuff.charAt(i-1)=='|')  // A negative number
                        i++;
                    else{
                        exprStringBuff.insert(i, '|');
                        exprStringBuff.insert(i+2, '|');
                        delimCount = delimCount + 2;
                        i = i+3;
                    }
                    break;
                case '*':
                    exprStringBuff.insert(i, '|');
                    exprStringBuff.insert(i+2, '|');
                    delimCount = delimCount + 2;
                    i = i+3;
                    break;
                case '/':
                    exprStringBuff.insert(i, '|');
                    exprStringBuff.insert(i+2, '|');
                    delimCount = delimCount + 2;
                    i = i+3;
                    break;
                case '%':
                    exprStringBuff.insert(i, '|');
                    exprStringBuff.insert(i+2, '|');
                    delimCount = delimCount + 2;
                    i = i+3;
                    break;
                case '$':
                    exprStringBuff.insert(i, '|');
                    exprStringBuff.insert(i+2, '|');
                    delimCount = delimCount + 2;
                    i = i+3;
                    break;
                case '(':
                    exprStringBuff.insert(i+1, '|');
                    delimCount = delimCount + 1;
                    i = i+2;
                    break;
                case ')':
                    exprStringBuff.insert(i, '|');
                    delimCount = delimCount + 1;
                    i = i+2;
                    break;
                case '"':
                    try{
//                        exprStringBuff.setCharAt(i, ' ');
                        i++;
                        while (exprStringBuff.charAt(i) != '"') i++;
//                        exprStringBuff.setCharAt(i, ' ');
                    }catch (StringIndexOutOfBoundsException e) {throw new InvalidOperandExpressionException(DBase.resources.getString("LogicalExpressionMsg2"));}
                    i++;
                    break;
                default:
                    i++;
            }
        }

        String op;
        StringTokenizer st = new StringTokenizer(exprStringBuff.toString(), "|", false);
        int numOfTokens = st.countTokens();

        /* The numOfTokens cannot be 0 or 1. Also when two adjacent operators are found
         * delimiting above has as a result two '|' delimiters to be adjacent. We cannot
         * trace this illegal situation through the StringTokenizer, because when it
         * encounters an empty token, it automatically fetches the next one. So we can't
         * trace this condition. Therefore we have introduced the "delimCount" variable
         * and the second condition in the if-statement below.
         */
        if (numOfTokens < 2 || (numOfTokens != (delimCount+1)))
           throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + exprString + "\"");

        /* Check if the order of operators, parenthesis and operands is proper.
         */
        count = 0;
        while (st.hasMoreTokens()) {
            op = st.nextToken();
//            System.out.println("-----  " + op);

            if (op.equals(""))
                throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + exprString + "\"");

            if (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") ||
              op.equals("%") || op.equals("$") )
              count--;
            else if (op.equals("(")) {
//                System.out.println(count);
                if (count != 0)
                    throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + exprString + "\"");
            }
            else if (op.equals(")")) {
//                System.out.println(count);
                if (count != 1)
                    throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + exprString + "\"");
            }else
                count++;

//            System.out.println(count);
            if (count<0 || count>1)
                    throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + exprString + "\"");

        }

        if (count!=1)
            throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + exprString + "\"");

        String exprString2 = exprString;
        exprString = exprStringBuff.toString();
//        System.out.println("exprString: " + exprString);


        StringTokenizer st2 = new StringTokenizer(exprString, "|", false);
        String stackTop;
        Class[] cl = new Class[1];
        Object[] val = new Object[1];
        Constructor con;
        cl[0] = exprString.getClass();
        Object operand;
        /* Algorithm for converion of exprString from infix to postfix notation.
         * The algorithm was taken from "Aaron Tenenbaum && Mosce Augenstein:
         * Data Structures Using Pascal" pages 97-99.
         */
        while (st2.hasMoreTokens()) {
            op = st2.nextToken();

            if (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") ||
              op.equals("%") || op.equals("$") || op.equals("(") || op.equals(")")) {

                try{
                    stackTop = (String) operatorStack.top();
                }catch (InvalidOperationException e) {stackTop=null;}

                while (stackTop!=null && precedence(stackTop,op)) {
                    try{
                        stackTop = (String) operatorStack.pop();
                        postfix.push(new StackElement(stackTop));
                    }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in NumericOperandExpression NumericOperandExpression(): (1)"); return;}

                    try{
                        stackTop = (String) operatorStack.top();
                    }catch (InvalidOperationException e) {stackTop=null;}
                }

                if (operatorStack.isEmpty() || !(op.equals(")")))
                    operatorStack.push(op);
                else{
                    try{
                        stackTop = (String) operatorStack.pop();
                    }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in NumericOperandExpression NumericOperandExpression(): (2)"); return;}
                }

            }else{
                /* Cast "op" to the proper data type, before pushing it in the "
                 * postfix" stack. However if "op" is a field index included in
                 * square brackets, don't cast it, but simply insert it in the stack.
                 */
                if (op.charAt(0) == '[' && op.charAt(op.length()-1) == ']') {
                    op = op.substring(1, op.length()-1);
                    postfix.push(new StackElement(op, true));
                }else{
                    if (!containsDoubleOperand && op.indexOf('.') != -1)
                        containsDoubleOperand = true;

                    /* Double numbers are produced through the DoubleNumberFormat's parse() method.
                     */
                    if (fldType.equals(java.lang.Double.class)) {
                        try{
                            operand = table.getNumberFormat().parse((String) op);
                        }catch (ParseException e) {
//                        }catch (DoubleNumberFormatParseException e) {
                            throw new InvalidDataFormatException(DBase.resources.getString("CBooleanOperandExpressionMsg4") + exprString2 + ". \"" + op + DBase.resources.getString("CDoubleNumberFormatMsg1"));
                        }
                    }else{
                        try {
                            con = fldType.getConstructor(cl);
                            val[0] = op;
                            try {
                                operand = con.newInstance(val);
                            }catch (InvocationTargetException e) {throw new InvalidDataFormatException(DBase.resources.getString("CBooleanOperandExpressionMsg4") + exprString2 + ". \"" + op + DBase.resources.getString("CDoubleNumberFormatMsg1"));}
                             catch (InstantiationException e) {throw new InvalidDataFormatException(DBase.resources.getString("CBooleanOperandExpressionMsg4") + exprString2 + ". \"" + op + DBase.resources.getString("CDoubleNumberFormatMsg1"));}
                             catch (IllegalAccessException e) {throw new InvalidDataFormatException(DBase.resources.getString("CBooleanOperandExpressionMsg4") + exprString2 + ". \"" + op + DBase.resources.getString("CDoubleNumberFormatMsg1"));}
                        }catch (NoSuchMethodException e) {throw new InvalidDataFormatException(DBase.resources.getString("CBooleanOperandExpressionMsg4") + exprString2 + ". \"" + op + DBase.resources.getString("CDoubleNumberFormatMsg1"));}
//                    System.out.println(operand + " " + operand.getClass().getName());
                    }
                    postfix.push(new StackElement(operand));
                }
            }
        }//while

        while (!operatorStack.isEmpty()) {
            try{
                postfix.push(new StackElement(operatorStack.pop()));
            }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in NumericOperandExpression NumericOperandExpression(): (3)"); return;}
        }

//        System.out.println(postfix);

        Reversing.reverse(postfix);

    }


    private boolean precedence(String op1, String op2) { //throws InvalidOperandExpressionException {

        if (op1.equals("(") && op2.equals(")"))
            return false;

        if (op1.equals(op2) && !op1.equals("(")) return true;

        if (op1.equals("+")) {
            if (op2.equals("-") || op2.equals(")"))
                return true;
            else
                return false;
/*            if (op2.equals("*") || op2.equals("/") || op2.equals("%") || op2.equals("$"))
                return false;
*/      }
        if (op1.equals("-")) {
            if (op2.equals("+") || op2.equals(")"))
                return true;
            else
                return false;
/*            if (op2.equals("*") || op2.equals("/") || op2.equals("%") || op2.equals("$"))
                return false;
*/      }
        if (op1.equals("*")) {
            if (op2.equals("+") || op2.equals("-") || op2.equals(")") || op2.equals("*") || op2.equals("/") || op2.equals("%"))
                return true;
            else
                return false;
/*            if (op2.equals("/") || op2.equals("%") || op2.equals("$"))
                return false;
*/      }
        if (op1.equals("/")) {
            if (op2.equals("+") || op2.equals("-") || op2.equals(")") || op2.equals("*") || op2.equals("/") || op2.equals("%"))
                return true;
            else
                return false;
/*            if (op2.equals("*") || op2.equals("%") || op2.equals("$"))
                return false;
*/      }
        if (op1.equals("%")) {
            if (op2.equals("+") || op2.equals("-") || op2.equals(")") || op2.equals("*") || op2.equals("/") || op2.equals("%"))
                return true;
            else
                return false;
        }
        if (op1.equals("$")) {
            if (op2.equals("("))
                return false;
            return true;
        }

/*        if (op1.equals(")") && op2.equals("("))
            throw new InvalidOperandExpressionException();
*/
        return false;
    }


    protected Object execute(int rowIndex, boolean inDifferent) { // throws InvalidOperandExpressionException {
        Stack operandStack = new Stack();
        Number operand1;
        Number operand2;
        double result;

        StackElement popped;
        Stack postfixClone = (Stack) postfix.clone();
        Object obj;
        Integer anInteger = new Integer(5);

        while (!postfixClone.isEmpty()) {
            try {
                popped = (StackElement) postfixClone.pop();
            }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in NumericOperandExpression NumericOperandExpression(): (4)"); return new Double(0);}

//            System.out.println("Popped: " + popped.element + " " + popped.isFieldIndex);

            if (popped.isFieldIndex) {
//                System.out.println("field: " + Integer.valueOf((String) popped.element) + ", row: " + rowIndex + ", table: " + table);
                obj = table.riskyGetCell(new Integer(((String) popped.element)).intValue(), rowIndex);
                if (obj == null)
                    return null;
                if (obj.getClass().isInstance(anInteger))
                    obj = new Double(((Integer)obj).doubleValue());
                operandStack.push(obj);
                continue;
            }

            if (!popped.element.getClass().getName().equals("java.lang.String")) {
                operandStack.push(popped.element);
            }else{
                operand1 = (Number) operandStack.pop();
                operand2 = (Number) operandStack.pop();

                if (popped.element.equals("+"))
                    result = operand2.doubleValue() + operand1.doubleValue();
                else if (popped.element.equals("-"))
                    result = operand2.doubleValue() - operand1.doubleValue();
                else if (popped.element.equals("*"))
                    result = operand2.doubleValue() * operand1.doubleValue();
                else if (popped.element.equals("/")) {
                    if (operand1.equals(new Double(0.0)))
                        return null;
                    else
                        result = operand2.doubleValue() / operand1.doubleValue();
                }else if (popped.element.equals("%"))
                    result = operand2.doubleValue() % operand1.doubleValue();
                else if (popped.element.equals("$")) {
                    result = 1;
                    for (int i=0; i<operand1.intValue(); i++)
                        result = result * operand2.doubleValue();
                }else{
                    result = 0;  //In case an invalid comparator finds its way down here, which seems impossible
                }
//                System.out.println(operand2 + " " + popped + " " + operand1 + "= " + result);
                operandStack.push(new Double(result));
            }
        }//while

        try{
           result = ((Double) operandStack.pop()).doubleValue();
        }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in NumericOperandExpression NumericOperandExpression(): (5)"); return new Double(0);}

        return new Double(result);
    }



    protected Object execute() throws InvalidOperandExpressionException {
        Stack operandStack = new Stack();
        Double operand1;
        Double operand2;
        double result;

        StackElement popped;

        while (!postfix.isEmpty()) {
            try {
                popped = (StackElement) postfix.pop();
            }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in NumericOperandExpression NumericOperandExpression(): (6)"); return new Double(0);}

            if (!popped.element.getClass().getName().equals("java.lang.String"))
                operandStack.push(popped.element);
            else{
                operand1 = (Double) operandStack.pop();
                operand2 = (Double) operandStack.pop();

                if (popped.element.equals("+"))
                    result = operand2.doubleValue() + operand1.doubleValue();
                else if (popped.element.equals("-"))
                    result = operand2.doubleValue() - operand1.doubleValue();
                else if (popped.element.equals("*"))
                    result = operand2.doubleValue() * operand1.doubleValue();
                else if (popped.element.equals("/")) {
                    if (operand1.equals(new Double(0.0)))
                        return null;
                    else
                        result = operand2.doubleValue() / operand1.doubleValue();
                }else if (popped.element.equals("%"))
                    result = operand2.doubleValue() % operand1.doubleValue();
                else if (popped.element.equals("$")) {
                    result = 1;
                    for (int i=0; i<operand1.intValue(); i++)
                        result = result * operand2.doubleValue();
                }else
                    throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg6") + popped + "\"");

//                System.out.println(operand2 + " " + popped + " " + operand1 + "= " + result);
                operandStack.push(new Double(result));
            }
        }//while

        try{
           result = ((Double) operandStack.pop()).doubleValue();
        }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in NumericOperandExpression NumericOperandExpression(): (7)"); return new Double(0);}

        return new Double(result);
    }


    private void writeObject(ObjectOutputStream out) throws IOException {
        /* In 1.0 version of the storage format the 'table' field was also
         * stored. This stopped in version 1.1. Instead the CTable which
         * restores every TableField also assigns its OperandExpression's
         * 'table' field.
         */
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("Postfix", postfix);
        fieldMap.put("Operator stack", operatorStack);
//1.1        fieldMap.put("Table", table);
        fieldMap.put("Contains double operand", containsDoubleOperand);

        out.writeObject(fieldMap);

/*        out.writeObject(postfix);
        out.writeObject(operatorStack);
        out.writeObject(table);
        out.writeObject(new Boolean(containsDoubleOperand));
*/
    }


    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object firstObj = in.readObject();
        if (!StorageStructure.class.isAssignableFrom(firstObj.getClass())) {
            // Old time readExtermal()
            oldReadObject(in, firstObj);
        }else{
            StorageStructure fieldMap = (StorageStructure) firstObj;
            postfix = (Stack) fieldMap.get("Postfix");
            operatorStack = (Stack) fieldMap.get("Operator stack");
//1.1            table = (CTable) fieldMap.get("Table");
            containsDoubleOperand = fieldMap.get("Contains double operand", false);
        }
    }

    private void oldReadObject(ObjectInputStream in, Object firstObj) throws IOException {
        System.out.println("NumericOperandExpression oldReadObject()");
        try{
            postfix = (Stack) firstObj; //in.readObject();
            operatorStack = (Stack) in.readObject();
//t            table = (CTable) in.readObject();
            Object tableStructure = in.readObject();
            if (CTable.class.isAssignableFrom(tableStructure.getClass()))
                table = new Table((CTable) tableStructure);
            else
                table = (Table) tableStructure;
            containsDoubleOperand = ((Boolean) in.readObject()).booleanValue();
        }catch (Exception e) {
//            System.out.println(e.getClass() + ", " + e.getMessage());
            throw new IOException(e.getMessage());
        }
    }

}

