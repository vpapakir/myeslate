package gr.cti.eslate.database.engine;


import com.objectspace.jgl.*;
import java.text.ParsePosition;
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
class BooleanOperandExpression extends OperandExpression implements Serializable {
    /** The version of the storage format of the BooleanOperandExpression class
     */
//    public static final String STR_FORMAT_VERSION = "1.1";
    public static final int FORMAT_VERSION = 2;

    Stack postfix = new Stack();
    Stack operatorStack = new Stack();
    boolean invalidOperandExpression = false;
    static final long serialVersionUID = 12;


    protected BooleanOperandExpression(String exprString, Table tab)
    throws InvalidOperandExpressionException, InvalidDataFormatException {
        try{
            throw new Exception();
        }catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("exprString: "+ exprString);
        }

        table = tab;
        Class fldType;
        fldType = java.lang.Boolean.class; //new Boolean("true").getClass();

        StringBuffer exprStringBuff = new StringBuffer();
        char[] c = exprString.toCharArray();
        int count = 0;
        for (int k=0; k<exprString.length(); k++) {
   //         if (c[k] != ' ')
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
            throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + exprString + DBase.resources.getString("CBooleanOperandExpressionMsg2"));

        /* Delimit the operators using '|' delimiter. Don't touch the parts of the
         * exprString that are inside quotes (").
         */
        int i=0;
        int delimCount = 0;
        boolean operFound = false;
        while (i<exprStringBuff.length()) {
//                System.out.println("i= " + i + " " + exprStringBuff.charAt(i));
            switch (exprStringBuff.charAt(i)) {
                case 'a':
                    if (i+2<exprStringBuff.length()) {
//                        System.out.println("i+1: " + exprStringBuff.charAt(i+1) +
//                        ", i+2: " + exprStringBuff.charAt(i+2) + ", i+3: " + exprStringBuff.charAt(i+3));
                        if ((exprStringBuff.charAt(i+1) == 'n' || exprStringBuff.charAt(i+1) == 'N') &&
                            (exprStringBuff.charAt(i+2) == 'd' || exprStringBuff.charAt(i+1) == 'D')) {
                            if (((i+3) == exprStringBuff.length()) || (exprStringBuff.charAt(i+3) == ' ')) {
                                exprStringBuff.insert(i, '|');
                                exprStringBuff.setCharAt(i+1, 'a');
                                exprStringBuff.setCharAt(i+2, 'n');
                                exprStringBuff.setCharAt(i+3, 'd');
                                exprStringBuff.insert(i+4, '|');
                                delimCount = delimCount + 2;
                                i = i+5;
                            }else
                                i++;
                        }else
                            i++;
                    }else
                        i++;
                    break;
                case 'A':
                    if (i+2<exprStringBuff.length()) {
                        if ((exprStringBuff.charAt(i+1) == 'n' || exprStringBuff.charAt(i+1) == 'N') &&
                            (exprStringBuff.charAt(i+2) == 'd' || exprStringBuff.charAt(i+1) == 'D')) {
                            if (((i+3) == exprStringBuff.length()) || (exprStringBuff.charAt(i+3) == ' ')) {
                                exprStringBuff.insert(i, '|');
                                exprStringBuff.setCharAt(i+1, 'a');
                                exprStringBuff.setCharAt(i+2, 'n');
                                exprStringBuff.setCharAt(i+3, 'd');
                                exprStringBuff.insert(i+4, '|');
                                delimCount = delimCount + 2;
                                i = i+5;
                            }else
                                i++;
                        }else
                            i++;
                    }else
                        i++;
                    break;
                case 'o':
                    if (i+1<exprStringBuff.length()) {
                        if ((exprStringBuff.charAt(i+1) == 'r' || exprStringBuff.charAt(i+1) == 'R')) {
                            if (((i+2) == exprStringBuff.length()) || (exprStringBuff.charAt(i+2) == ' ')) {
                                exprStringBuff.insert(i, '|');
                                exprStringBuff.setCharAt(i+1, 'o');
                                exprStringBuff.setCharAt(i+2, 'r');
                                exprStringBuff.insert(i+3, '|');
                                delimCount = delimCount + 2;
                                i = i+4;
                            }else
                                i++;
                        }else
                            i++;
                    }else
                        i++;
                    break;
                case 'O':
                    if (i+1<exprStringBuff.length()) {
                        if ((exprStringBuff.charAt(i+1) == 'r' || exprStringBuff.charAt(i+1) == 'R')) {
                            if (((i+2) == exprStringBuff.length()) || (exprStringBuff.charAt(i+2) == ' ')) {
                                exprStringBuff.insert(i, '|');
                                exprStringBuff.setCharAt(i+1, 'o');
                                exprStringBuff.setCharAt(i+2, 'r');
                                exprStringBuff.insert(i+3, '|');
                                delimCount = delimCount + 2;
                                i = i+4;
                            }else
                                i++;
                        }else
                            i++;
                    }else
                        i++;
                    break;
                case 'n':
                    if (i+2<exprStringBuff.length()) {
                        if ((exprStringBuff.charAt(i+1) == 'o' || exprStringBuff.charAt(i+1) == 'O') &&
                            (exprStringBuff.charAt(i+2) == 't' || exprStringBuff.charAt(i+1) == 'T')) {
                            if (((i+3) == exprStringBuff.length()) || (exprStringBuff.charAt(i+3) == ' ')) {
                                exprStringBuff.setCharAt(i, 'n');
                                exprStringBuff.setCharAt(i+1, 'o');
                                exprStringBuff.setCharAt(i+2, 't');
                                exprStringBuff.insert(i+3, '|');
                                delimCount = delimCount + 1;
                                i = i+4;
                            }else
                                i++;
                        }else
                            i++;
                    }else
                        i++;
                    break;
                case 'N':
                    if (i+2<exprStringBuff.length()) {
                        if ((exprStringBuff.charAt(i+1) == 'o' || exprStringBuff.charAt(i+1) == 't') &&
                            (exprStringBuff.charAt(i+2) == 't' || exprStringBuff.charAt(i+1) == 'T')) {
                            if (((i+3) == exprStringBuff.length()) || (exprStringBuff.charAt(i+3) == ' ')) {
//                                exprStringBuff.insert(i, '|');
                                exprStringBuff.setCharAt(i, 'n');
                                exprStringBuff.setCharAt(i+1, 'o');
                                exprStringBuff.setCharAt(i+2, 't');
                                exprStringBuff.insert(i+3, '|');
                                delimCount = delimCount + 1;
                                i = i+4;
                            }else
                                i++;
                        }else
                            i++;
                    }else
                        i++;
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
                    }catch (StringIndexOutOfBoundsException e) {throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg3"));}
                    i++;
                    break;
                default:
                    i++;
            }
        }

//        System.out.println("exprStringBuff: " + exprStringBuff);
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
           throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + exprString);

        /* Check if the order of operators, parenthesis and operands is proper.
         */
        count = 0;
        int numberOfNOT = 0;
        while (st.hasMoreTokens()) {
            op = st.nextToken().trim();
//            System.out.println("-----  " + op);

            if (op.equals("not"))
                numberOfNOT++;

            if (op.equals(""))
                throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + exprString);

            if (op.equals(DBase.resources.getString("and")) || op.equals(DBase.resources.getString("or")) || op.equals(DBase.resources.getString("not")) || op.equals("και") || op.equals("ή") || op.equals("όχι"))
              count--;
            else if (op.equals("(")) {
//                System.out.println(count);
                if (count != (0-numberOfNOT))
                    throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + exprString);
            }
            else if (op.equals(")")) {
//                System.out.println(count);
                if (count != (1-numberOfNOT))
                    throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + exprString);
            }else
                count++;

            if (count< (0-numberOfNOT) || count> (1-numberOfNOT))
                    throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + exprString);
        }

        if (count != (1-numberOfNOT))
            throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + exprString);

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
            op = st2.nextToken().trim();

            if (op.equals(DBase.resources.getString("and")) || op.equals(DBase.resources.getString("or")) || op.equals(DBase.resources.getString("not")) || op.equals("και") || op.equals("ή") || op.equals("όχι")
                || op.equals("(") || op.equals(")") ) {

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
  /*                  try {
                        con = fldType.getConstructor(cl);
                        val[0] = op;
                        try {
                            operand = con.newInstance(val);
                        }catch (InvocationTargetException e) {throw new InvalidDataFormatException("Unable to evaluate expression: "+ exprString2 + ". \"" + op + "\" cannot be cast to Number");}
                         catch (InstantiationException e) {throw new InvalidDataFormatException("Unable to evaluate expression: "+ exprString2 + ". \"" + op + "\" cannot be cast to Number");}
                         catch (IllegalAccessException e) {throw new InvalidDataFormatException("Unable to evaluate expression: "+ exprString2 + ". \"" + op + "\" cannot be cast to Number");}
                    }catch (NoSuchMethodException e) {throw new InvalidDataFormatException("Unable to evaluate expression: "+ exprString2 + ". \"" + op + "\" cannot be cast to Number");}
//                System.out.println(operand + " " + operand.getClass().getName());
*/
                    if (op.equals("true") || op.equals("false"))
                        postfix.push(new StackElement(new Boolean(op)));
                    else
                        throw new InvalidDataFormatException(DBase.resources.getString("CBooleanOperandExpressionMsg4")+ exprString2 + ". \"" + op + DBase.resources.getString("CBooleanOperandExpressionMsg5"));                }
            }
        }//while

        while (!operatorStack.isEmpty()) {
            try{
                postfix.push(new StackElement(operatorStack.pop()));
            }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in NumericOperandExpression NumericOperandExpression(): (3)"); return;}
        }

        Reversing.reverse(postfix);

    }


    private boolean precedence(String op1, String op2) { //throws InvalidOperandExpressionException {

        if (op1.equals("(") && op2.equals(")"))
            return false;

        if (op1.equals(op2) && !op1.equals("(")) return true;

        if (op1.equals(DBase.resources.getString("and")) || op1.equals("και")) {
            if (op2.equals(DBase.resources.getString("or")) || op2.equals(")") || op2.equals("ή"))
                return true;
            else
                return false;
        }
        if (op1.equals(DBase.resources.getString("or")) || op1.equals("ή")) {
            if (op2.equals(DBase.resources.getString("and")) || op2.equals(")") || op2.equals("και"))
                return true;
            else
                return false;
        }
        if (op1.equals(DBase.resources.getString("not")) || op1.equals("όχι")) {
            if (op2.equals(DBase.resources.getString("and")) || op2.equals(DBase.resources.getString("or")) || op2.equals(")") || op2.equals("και") || op2.equals("ή"))
                return true;
            else
                return false;
        }

        return false;
    }


    protected Object execute(int rowIndex, boolean inDifferent) { // throws InvalidOperandExpressionException {

        if (invalidOperandExpression)
            return null;

        Stack operandStack = new Stack();
        Boolean operand1;
        Boolean operand2;
        boolean result;

        StackElement popped;
        Stack postfixClone = (Stack) postfix.clone();
        Object obj;

        while (!postfixClone.isEmpty()) {
            try {
                popped = (StackElement) postfixClone.pop();
            }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in NumericOperandExpression NumericOperandExpression(): (4)"); return new Boolean("false");}

            if (popped.isFieldIndex) {
                obj = table.riskyGetCell(new Integer(((String) popped.element)).intValue(), rowIndex);
                if (obj == null)
                    return null;
                operandStack.push(obj);
                continue;
            }

            if (!popped.element.getClass().getName().equals("java.lang.String")) {
                operandStack.push(popped.element);
            }else if (popped.element.equals(DBase.resources.getString("not")) || popped.element.equals("όχι")) {
                try{
                    operand1 = (Boolean) operandStack.pop();
                }catch (Exception e) {
                    //This must be presented to the user in a message box
                    System.out.println("Invalid expression");
                    invalidOperandExpression = true;
                    return null;}

                result = !(operand1.booleanValue());
                operandStack.push(new Boolean(result));
            }else{
                operand1 = (Boolean) operandStack.pop();
                operand2 = (Boolean) operandStack.pop();

                if (popped.element.equals(DBase.resources.getString("and")) || popped.element.equals("και"))
                    result = operand2.booleanValue() && operand1.booleanValue();
                else if (popped.element.equals(DBase.resources.getString("or")) || popped.element.equals("ή"))
                    result = operand2.booleanValue() || operand1.booleanValue();
                else
                    result = false;  //In case an invalid comparator finds its way down here, which seems impossible

//                System.out.println(operand2 + " " + popped + " " + operand1 + "= " + result);
                operandStack.push(new Boolean(result));
            }
        }//while

        Boolean finalResult;
        try{
           finalResult = (Boolean) operandStack.pop();
        }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in NumericOperandExpression NumericOperandExpression(): (4)"); return new Boolean("false");}

        return finalResult;
    }



    protected Object execute() throws InvalidOperandExpressionException {
        Stack operandStack = new Stack();
        Boolean operand1;
        Boolean operand2;
        boolean result;

        StackElement popped;

        while (!postfix.isEmpty()) {
            try {
                popped = (StackElement) postfix.pop();
            }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in NumericOperandExpression NumericOperandExpression(): (4)"); return new Boolean("false");}

            if (!popped.element.getClass().getName().equals("java.lang.String"))
                operandStack.push(popped.element);
            else if (popped.element.equals(DBase.resources.getString("not")) || popped.element.equals("όχι")) {
                operand1 = (Boolean) operandStack.pop();

                result = !(operand1.booleanValue());
                operandStack.push(new Boolean(result));
            }else{
                operand1 = (Boolean) operandStack.pop();
                operand2 = (Boolean) operandStack.pop();

                if (popped.element.equals(DBase.resources.getString("and")) || popped.element.equals("και"))
                    result = operand2.booleanValue() && operand1.booleanValue();
                else if (popped.element.equals(DBase.resources.getString("or")) || popped.element.equals("ή"))
                    result = operand2.booleanValue() || operand1.booleanValue();
                else
                    throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg6") + popped + "\"");

//                System.out.println(operand2 + " " + popped + " " + operand1 + "= " + result);
                operandStack.push(new Boolean(result));
            }
        }//while

        Boolean finalResult;
        try{
           finalResult = (Boolean) operandStack.pop();
        }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in NumericOperandExpression NumericOperandExpression(): (4)"); return new Boolean("false");}

        return finalResult;
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
        fieldMap.put("Invalid operand expression", invalidOperandExpression);

        out.writeObject(fieldMap);

/*        out.writeObject(postfix);
        out.writeObject(operatorStack);
        out.writeObject(table);
        out.writeObject(new Boolean(invalidOperandExpression));
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
//            table = (CTable) fieldMap.get("Table");
            invalidOperandExpression = fieldMap.get("Invalid operand expression", false);
        }
    }

    private void oldReadObject(ObjectInputStream in, Object firstObj) throws IOException {
        System.out.println("BooleanOperandExpression oldReadObject()");
        try{
            postfix = (Stack) firstObj; //in.readObject();
            operatorStack = (Stack) in.readObject();
//t            table = (CTable) in.readObject();
            Object tableStructure = in.readObject();
            if (CTable.class.isAssignableFrom(tableStructure.getClass()))
                table = new Table((CTable) tableStructure);
            else
                table = (Table) tableStructure;
            invalidOperandExpression = ((Boolean) in.readObject()).booleanValue();
        }catch (Exception e) {
//            System.out.println(e.getClass() + ", " + e.getMessage());
            throw new IOException(e.getMessage());
        }
    }

}