package gr.cti.eslate.database.engine;

import com.objectspace.jgl.*;
import java.util.StringTokenizer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import java.util.Date;
import java.text.ParsePosition;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

/**
 * @version	2.0, May 01
 */
class StringOperandExpression extends OperandExpression implements Serializable {
    /** The version of the storage format of the StringOperandExpression class
     */
//    public static final String STR_FORMAT_VERSION = "1.1";
    public static final int FORMAT_VERSION = 2;

    Stack postfix = new Stack();
    Stack operatorStack = new Stack();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
    static final long serialVersionUID = 12;

    protected StringOperandExpression(String exprString, Table tab)
    throws InvalidOperandExpressionException, InvalidDataFormatException {
//        System.out.println("Creating string operand expression");
        table = tab;
        sdf.setLenient(false);
        stf.setLenient(false);

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
            throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + exprString + "\"" + DBase.resources.getString("CBooleanOperandExpressionMsg2"));

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
                        exprStringBuff = new StringBuffer(exprStringBuff.toString().substring(0, i) + exprStringBuff.toString().substring(i+1, exprStringBuff.length()));
//                        System.out.println(exprStringBuff);
//                        exprStringBuff.setCharAt(i, ' ');
//                        i++;
                        int tmp=i;
                        while (exprStringBuff.charAt(i) != '"') i++;
                        if ((i-tmp) == 1 && (exprStringBuff.charAt(tmp) == '+' || exprStringBuff.charAt(tmp) == '(' || exprStringBuff.charAt(tmp) == ')'))
                            i++;
                        else
                            exprStringBuff = new StringBuffer(exprStringBuff.toString().substring(0, i) + exprStringBuff.toString().substring(i+1, exprStringBuff.length()));
//                        System.out.println(exprStringBuff);
//                        exprStringBuff.setCharAt(i, ' ');
                    }catch (StringIndexOutOfBoundsException e) {throw new InvalidOperandExpressionException(DBase.resources.getString("ParenthesizedELEMsg1"));}
//                    i++;
                    break;
                default:
                    i++;
            }
        }

        String op;
        StringTokenizer st = new StringTokenizer(exprStringBuff.toString(), "|", false);
        int numOfTokens = st.countTokens();
//        System.out.println("numOfTokens: " + numOfTokens);
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

            if (op.equals("+"))
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
        /* Algorithm for converion of exprString from infix to postfix notation.
         * The algorithm was taken from "Aaron Tenenbaum && Mosce Augenstein:
         * Data Structures Using Pascal" pages 97-99.
         */
        while (st2.hasMoreTokens()) {
            op = st2.nextToken();

            if (op.equals("+") || op.equals("(") || op.equals(")")) {

                try{
                    stackTop = (String) operatorStack.top();
                }catch (InvalidOperationException e) {stackTop=null;}

                while (stackTop!=null && precedence(stackTop,op)) {
                    try{
                        stackTop = (String) operatorStack.pop();
                        postfix.push(new StackElement(stackTop));
                    }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in StringOperandExpression NumericOperandExpression(): (1)"); return;}

                    try{
                        stackTop = (String) operatorStack.top();
                    }catch (InvalidOperationException e) {stackTop=null;}
                }

                if (operatorStack.isEmpty() || !(op.equals(")")))
                    operatorStack.push(op);
                else{
                    try{
                        stackTop = (String) operatorStack.pop();
                    }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in StringOperandExpression StringOperandExpression(): (2)"); return;}
                }

            }else{
                /* Field index encountered
                 */
                if (op.charAt(0) == '[' && op.charAt(op.length()-1) == ']') {
                    op = op.substring(1, op.length()-1);
                    postfix.push(new StackElement(op, true));
                }else{
                    /* String operand encountered
                     */
                    if (op.equals("(\"") || op.equals(")\""))
                        op=op.substring(0,1);
                    if (op.equals("+\""))
                        op=op.replace('"', ' ');
                    postfix.push(new StackElement(op));
                }
            }
        }//while

        while (!operatorStack.isEmpty()) {
            try{
                postfix.push(new StackElement(operatorStack.pop()));
            }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in StringOperandExpression StringOperandExpression(): (3)"); return;}
        }

        Reversing.reverse(postfix);
//        System.out.println("Size: " + postfix.size());
//        System.out.println(postfix);

//        try{
//            System.out.println("Result: " + execute());
//        }catch (InvalidOperandExpressionException e) {throw new InvalidOperandExpressionException(e.message);}

    }


    private boolean precedence(String op1, String op2) { //throws InvalidOperandExpressionException {

        if (op1.equals("(") && op2.equals(")"))
            return false;

        if (op1.equals(op2) && !op1.equals("(")) return true;

        if (op1.equals("+")) {
            if (op2.equals(")"))
                return true;
            else
                return false;
        }

        return false;
    }


    protected Object execute() throws InvalidOperandExpressionException {
        Stack operandStack = new Stack();
        String operand1;
        String operand2;
        String result;

        StackElement popped;

        while (!postfix.isEmpty()) {
            try {
                popped = (StackElement) postfix.pop();
            }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in StringOperandExpression StringOperandExpression(): (4)"); return "";}

            if (!popped.element.equals("+"))
                operandStack.push(popped.element);
            else{
                operand1 = (String) operandStack.pop();
                operand2 = (String) operandStack.pop();

                result = operand2.concat(operand1);
//                System.out.println("result of concatenation: " + result);
                operandStack.push(result);
            }
        }//while

        try{
           result = (String) operandStack.pop();
        }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in StringOperandExpression StringOperandExpression(): (4)"); return "";}

        return result;
    }


    protected Object execute(int rowIndex, boolean inDifferent) {
        Stack operandStack = new Stack();
        String operand1;
        String operand2;
        String result;

        Stack postfixClone = (Stack) postfix.clone();
        StackElement popped;
        Object obj;

        while (!postfixClone.isEmpty()) {
            try {
                popped = (StackElement) postfixClone.pop();
            }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in StringOperandExpression StringOperandExpression(): (4)"); return "";}

            if (popped.isFieldIndex) {
                obj = table.riskyGetCell(new Integer(((String) popped.element)).intValue(), rowIndex);
                if (obj == null)
                    obj = "";
/*                if (obj.getClass().getName().equals("java.util.Date")) {
                    TableField f = (TableField) table._getFields().at((new Integer(((String) popped.element)).intValue()));
                    if (f.isDate())
                        obj = sdf.format((Date) obj);
                    else
                        obj = stf.format((Date) obj);
                }else
*/
                    obj = obj.toString();

                operandStack.push(obj);
                continue;
            }

            if (!popped.element.equals("+"))
                operandStack.push(popped.element);
            else{
                operand1 = (String) operandStack.pop();
                operand2 = (String) operandStack.pop();

                result = operand2.concat(operand1);
//                System.out.println("result of concatenation2: " + result);
                operandStack.push(result);
            }
        }//while

        try{
           result = (String) operandStack.pop();
        }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in StringOperandExpression StringOperandExpression(): (4)"); return "";}

        return result;
    }


    private void writeObject(ObjectOutputStream out) throws IOException {
        /* In 1.0 version of the storage format the 'table' field was also
         * stored. This stopped in version 1.1. Instead the Table which
         * restores every TableField also assigns its OperandExpression's
         * 'table' field.
         */
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("Postfix", postfix);
        fieldMap.put("Operator stack", operatorStack);
//1.1        fieldMap.put("Table", table);
        fieldMap.put("Date format", sdf);
        fieldMap.put("Time format", stf);
        out.writeObject(fieldMap);

/*        out.writeObject(postfix);
        out.writeObject(operatorStack);
        out.writeObject(table);
        out.writeObject(sdf);
        out.writeObject(stf);
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
//1.1            table = (Table) fieldMap.get("Table");
            sdf = (SimpleDateFormat) fieldMap.get("Date format");
            stf = (SimpleDateFormat) fieldMap.get("Time format");
        }
    }

    private void oldReadObject(ObjectInputStream in, Object firstObj) throws IOException {
        System.out.println("StringOperandExpression oldReadObject()");
        try{
            postfix = (Stack) firstObj; //in.readObject();
            operatorStack = (Stack) in.readObject();
//t            table = (Table) in.readObject();
            Object tableStructure = in.readObject();
            if (CTable.class.isAssignableFrom(tableStructure.getClass()))
                table = new Table((CTable) tableStructure);
            else
                table = (Table) tableStructure;
            sdf = (SimpleDateFormat) in.readObject();
            stf = (SimpleDateFormat) in.readObject();
        }catch (Exception e) {
//            System.out.println(e.getClass() + ", " + e.getMessage());
            throw new IOException(e.getMessage());
        }
    }
}
