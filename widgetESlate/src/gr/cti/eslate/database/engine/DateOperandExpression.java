package gr.cti.eslate.database.engine;

import gr.cti.eslate.utils.*;
import com.objectspace.jgl.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParsePosition;
import java.util.StringTokenizer;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;
import javax.swing.JFrame;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;


/**
 * @version	2.0, May 01
 */
class DateOperandExpression extends OperandExpression implements Serializable {
    /** The version of the storage format of the DateOperandExpression class
     */
//    public static final String STR_FORMAT_VERSION = "1.1";
    public static final int FORMAT_VERSION = 2;

    Stack postfix = new Stack();
    Stack operatorStack = new Stack();
    SimpleDateFormat sdf; //= new SimpleDateFormat("dd/MM/yyyy:HH");
    SimpleDateFormat stf; // = new SimpleDateFormat("HH:mm");
    boolean isDate;
    boolean invalidOperandExpression = false;
    boolean moreThan1DateFields;
    static JFrame frame = null;
    String textualFormula;
    private static Calendar calendar = new GregorianCalendar();
    static final long serialVersionUID = 12;

    protected DateOperandExpression(String formula, String exprString, boolean date, Table tab, int numOfDateFields)
    throws InvalidOperandExpressionException, InvalidDataFormatException,
    IllegalDateOperandExpression {

//        System.out.println("exprString12---------: " + exprString);
        sdf = tab.getDateFormat(); //new SimpleDateFormat(tab.getDateFormat().toPattern()+":HH");
        stf = tab.getTimeFormat();
        textualFormula = formula;
        isDate = date;
        table = tab;
        if (numOfDateFields > 1)
            moreThan1DateFields = true;
        else
            moreThan1DateFields = false;
        Class fieldType;

        sdf.setLenient(false);
        stf.setLenient(false);

        if (date)
//t            fieldType = java.util.Date.class; //sdf.parse("1/1/1990"+":02", new ParsePosition(0)).getClass();
            fieldType = DateTableField.DATA_TYPE; //sdf.parse("1/1/1990"+":02", new ParsePosition(0)).getClass();
        else
//t            fieldType = java.util.Date.class; //stf.parse("00:00", new ParsePosition(0)).getClass();
            fieldType = TimeTableField.DATA_TYPE; //stf.parse("00:00", new ParsePosition(0)).getClass();


        //Initializing "fieldType" for test purposes
//        fieldType = sdf.parse("1/1/1990:11", new ParsePosition(0)).getClass();
        //The following stay in this method
//        fldType = fieldType;
//        if (fieldType.getName().equals("java.lang.Integer"))
//            fldType = new Double("5").getClass();

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
            throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + textualFormula + DBase.resources.getString("CBooleanOperandExpressionMsg2"));

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
                        exprStringBuff.setCharAt(i, ' ');
                        i++;
                        while (exprStringBuff.charAt(i) != '"') i++;
                        exprStringBuff.setCharAt(i, ' ');
                    }catch (StringIndexOutOfBoundsException e) {throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg7") + textualFormula + DBase.resources.getString("CBooleanOperandExpressionMsg8"));}
                    i++;
                    break;
                default:
                    i++;
            }
        }

        /* Extract the quotes from the string.
         */
        String qs = exprStringBuff.toString();
        if (qs.indexOf(' ') != -1) {
//            System.out.println("Extracting quotes...");
            int blankIndex = 0;
            while((blankIndex = qs.indexOf(' ', blankIndex)) != -1) {
                if (blankIndex != qs.length()-1)
                    qs = qs.substring(0, blankIndex) + qs.substring(blankIndex+1);
                else;
//                System.out.println("qs: " + qs);
            }
//            System.out.println("qs: " + qs);
            exprStringBuff = new StringBuffer(qs);
        }


        String op;
        StringTokenizer st = new StringTokenizer(exprStringBuff.toString(), "|", false);
        int numOfTokens = st.countTokens();
//        System.out.println("exprStringBuff:" + exprStringBuff +".");

        /* The numOfTokens cannot be 0 or 1. Also when two adjacent operators are found
         * delimiting above has as a result two '|' delimiters to be adjacent. We cannot
         * trace this illegal situation through the StringTokenizer, because when it
         * encounters an empty token, it automatically fetches the next one. So we can't
         * trace this condition. Therefore we have introduced the "delimCount" variable
         * and the second condition in the if-statement below.
         */
        if (numOfTokens == 2 || numOfTokens == 0 || (numOfTokens != (delimCount+1)))
           throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + textualFormula + "\"");

        /* Check if the order of operators, parenthesis and operands is proper.
         */
        count = 0;
        while (st.hasMoreTokens()) {
            op = st.nextToken();
//            System.out.println("-----  " + op);

            if (op.equals(""))
                throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + textualFormula + "\"");

            if (op.equals("+") || op.equals("-"))
              count--;
            else if (op.equals("(")) {
//                System.out.println(count);
                if (count != 0)
                    throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + textualFormula + "\"");
            }
            else if (op.equals(")")) {
//                System.out.println(count);
                if (count != 1)
                    throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + textualFormula + "\"");
            }else
                count++;

//            System.out.println(count);
            if (count<0 || count>1)
                    throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + textualFormula + "\"");

        }

        if (count!=1)
            throw new InvalidOperandExpressionException(DBase.resources.getString("CBooleanOperandExpressionMsg1") + textualFormula + "\"");

        String exprString2 = exprString;
        exprString = exprStringBuff.toString();


        StringTokenizer st2 = new StringTokenizer(exprString, "|", false);
        String stackTop;
        Object operand = null;
        int convertedDateOperands = 0;
        int dateFieldCount = 0;
        /* Algorithm for converion of exprString from infix to postfix notation.
         * The algorithm was taken from "Aaron Tenenbaum && Mosce Augenstein:
         * Data Structures Using Pascal" pages 97-99.
         */
        while (st2.hasMoreTokens()) {
            op = st2.nextToken();
//            System.out.println("op: " + op);

            if (op.equals("+") || op.equals("-") || op.equals("(") || op.equals(")")) {

                try{
                    stackTop = (String) operatorStack.top();
                }catch (InvalidOperationException e) {stackTop=null;}

                while (stackTop!=null && precedence(stackTop,op)) {
                    try{
                        stackTop = (String) operatorStack.pop();
                        postfix.push(new StackElement(stackTop));
                    }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in DateOperandExpression DateOperandExpression(): (1)"); return;}

                    try{
                        stackTop = (String) operatorStack.top();
                    }catch (InvalidOperationException e) {stackTop=null;}
                }

                if (operatorStack.isEmpty() || !(op.equals(")")))
                    operatorStack.push(op);
                else{
                    try{
                        stackTop = (String) operatorStack.pop();
                    }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in DateOperandExpression DateOperandExpression(): (2)"); return;}
                }

            }else{
                /* Cast "op" to the proper data type, before pushing it in the "
                 * postfix" stack. However if "op" is a field index included in
                 * square brackets and followed by a Date field specifier, then don't
                 * cast it, but simply insert it in the stack.
                 */

                //Date or Time field index
                if (op.charAt(0) == '[' && op.charAt(op.length()-1) == ']') {
//                    System.out.println("op in data/time if: " + op);
//                    op = op.substring(1, op.length()-1));
                    postfix.push(new StackElement(op, true));
                    String fieldIndex = op.substring(1, op.length()-1);
                    AbstractTableField f;
                    try{
                        f = table.getTableField(new Integer(fieldIndex).intValue());
//t                        if (f.getFieldType().getName().equals("java.util.Date"))
                        if (f.getDataType().equals(DateTableField.DATA_TYPE) ||
                            f.getDataType().equals(TimeTableField.DATA_TYPE))
                            dateFieldCount++;
                        else{
                            if (date)
                                throw new InvalidDataFormatException(DBase.resources.getString("CDateOperandExpressionMsg1") + textualFormula + DBase.resources.getString("CDateOperandExpressionMsg2"));
                            else
                                throw new InvalidDataFormatException(DBase.resources.getString("CDateOperandExpressionMsg1") + textualFormula + DBase.resources.getString("CDateOperandExpressionMsg3"));
                        }
                    }catch (InvalidFieldIndexException e) {System.out.println(DBase.resources.getString("CDatabaseMsg74") + fieldIndex + DBase.resources.getString("DatabaseMsg18"));}
                     catch (NumberFormatException e) {System.out.println("Serious inconsistency error in DateOperandExpression DateOperandExpression() : (4)");}

                //Integer field index
                }else if (op.charAt(0) == '[' && op.charAt(op.length()-2) == ']') {
//                    System.out.println("op in integer if: " + op);
                    char ch = op.charAt(op.length()-1);
                    if (date) {
                        if (ch != 'd' && ch != 'm' && ch != 'y' && ch != 'D' && ch != 'M' && ch != 'Y')
                            throw new InvalidDataFormatException(DBase.resources.getString("CDateOperandExpressionMsg1") + textualFormula + DBase.resources.getString("CDateOperandExpressionMsg2"));
                    }else{
                        if (ch != 'h' && ch != 'm' && ch != 'H' && ch != 'M')
                            throw new InvalidDataFormatException(DBase.resources.getString("CDateOperandExpressionMsg1") + textualFormula + DBase.resources.getString("CDateOperandExpressionMsg3"));
                    }
//                    op = op.substring(1, op.length()-2)+op.substring(op.length()-1);
                    postfix.push(new StackElement(op, true));
                //Date or Time operand (a date, a time or an Integer with a Time field specifier)
                }else{

                    if (date) {
//                        System.out.println("op ---: " + op);
                        try{
                            operand = sdf.parse(((String) op), new ParsePosition(0));
                            calendar.setTime((Date) operand);
                            calendar.add(Calendar.HOUR, 10);
                            operand = calendar.getTime();
//                            System.out.println(sdf.toPattern() + ", operand: " + operand +", oper: " + ((String) op)+":10");
                        }catch (Exception e) {} //throw new InvalidDataFormatException("4" + DBase.resources.getString("CBooleanOperandExpressionMsg4") + "\"" + textualFormula + "\". \"" + op + "\"" + DBase.resources.getString("CTableMsg40"));}

                        if (operand == null) {
                            char ch = ((String) op).charAt(((String) op).length()-1);
                            if (ch != 'd' && ch != 'm' && ch != 'y' && ch != 'D' && ch != 'M' && ch != 'Y')
                                throw new InvalidDataFormatException(DBase.resources.getString("CDateOperandExpressionMsg1") + textualFormula + DBase.resources.getString("CDateOperandExpressionMsg4"));
                            String s = ((String) op).substring(0, ((String) op).length()-1);
                            try{
                                Integer value = new Integer(s);
                            }catch (NumberFormatException e) {
                                throw new InvalidDataFormatException(DBase.resources.getString("CDateOperandExpressionMsg1") + textualFormula + DBase.resources.getString("CDateOperandExpressionMsg4"));
                            }
                            operand = op;
                        }else{
                            operand = new CDate((Date) operand);
                            convertedDateOperands++;
                        }
                    }

                    if (!date) {
                        try{
                            operand = stf.parse(((String) op), new ParsePosition(0));
                        }catch (Exception e) {throw new InvalidDataFormatException(DBase.resources.getString("CDateOperandExpressionMsg5") + textualFormula + "\". \"" + op + DBase.resources.getString("CDateOperandExpressionMsg6"));}

                        if (operand == null) {
                            char ch = ((String) op).charAt(((String) op).length()-1);
                            if (ch != 'h' && ch != 'm' && ch != 'H' && ch != 'M')
                                throw new InvalidDataFormatException(DBase.resources.getString("CDateOperandExpressionMsg1") + textualFormula + DBase.resources.getString("CDateOperandExpressionMsg7"));
                            String s = ((String) op).substring(0, ((String) op).length()-1);
                            try{
                                Integer value = new Integer(s);
                            }catch (NumberFormatException e) {
                                throw new InvalidDataFormatException(DBase.resources.getString("CDateOperandExpressionMsg1") + textualFormula + DBase.resources.getString("CDateOperandExpressionMsg7"));
                            }
                            operand = op;
                        }else{
                            operand = new CTime((Date) operand);
                            convertedDateOperands++;
                        }
                    }

//                System.out.println(operand + " " + operand.getClass().getName());
                    postfix.push(new StackElement(operand));
                }
            }
        }//while

        while (!operatorStack.isEmpty()) {
            try{
                postfix.push(new StackElement(operatorStack.pop()));
            }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in DateOperandExpression DateOperandExpression(): (3)"); return;}
        }

        /* Check if the number of date fields is right. This check is performed only for
         * DateOperandExpressions which are used to create calculated fields. In this case in
         * CTable's "addCalculatedField()" the number of Date/Time fields in the expression have
         * been counted. The constructor of the DateOperandExpression is not strict. When in such
         * an expression there is a division, for example, of date fields then this constructor will
         * consider the expression legal. The check below will not allow this for expressions used
         * when calculated fields are added to the CTable.
         */
        if (table.creatingCalculatedField) {
//            System.out.println("numOfDateFields: " + numOfDateFields + ", " + "dateFieldCount: " + dateFieldCount);
            if (numOfDateFields != dateFieldCount)
                throw new IllegalDateOperandExpression(DBase.resources.getString("CDateOperandExpressionMsg8") + textualFormula + "\"");
        }

//        System.out.println(postfix);
        if (convertedDateOperands > 1)
            moreThan1DateFields = true;

        Reversing.reverse(postfix);

//            try{
//                System.out.println("Result: " + execute());
//            }catch (InvalidOperandExpressionException e) {throw new InvalidOperandExpressionException(e.message);}

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
        }
        if (op1.equals("-")) {
            if (op2.equals("+") || op2.equals(")"))
                return true;
            else
                return false;
        }

        return false;
    }


    protected Object execute() throws InvalidOperandExpressionException {
        Stack operandStack = new Stack();
        Object operand1;
        Object operand2;
        Date result = null;
        char fieldSpecifier1 = 'm', fieldSpecifier2 = 'm';
        Integer oper1=null, oper2=null;
        String possibleDateFieldSpecifiers = "YyMmDd";
        String possibleTimeFieldSpecifiers = "HhMm";

        StackElement popped;

        while (!postfix.isEmpty()) {
            try {
                popped = (StackElement) postfix.pop();
            }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in DateOperandExpression execute(): (1)"); return new Date();}

            if (popped.element.getClass().getName().equals("java.lang.String") && (((String) popped.element).equals("+") || ((String) popped.element).equals("-"))) {
                operand1 = operandStack.pop();
                operand2 = operandStack.pop();
//                System.out.println(operand1 + ", " + operand2 + ", "+ popped);

//Because of CDate if (!operand1.getClass().getName().equals("java.util.Date")) {
                if (!(operand1.getClass().getName().equals("gr.cti.eslate.database.engine.CDate") ||
                      operand1.getClass().getName().equals("gr.cti.eslate.database.engine.CTime"))) {
                    fieldSpecifier1 = ((String) operand1).charAt(((String) operand1).length()-1);

                    if (isDate) {
                        if (possibleDateFieldSpecifiers.indexOf(fieldSpecifier1) == -1)
                            throw new InvalidOperandExpressionException(DBase.resources.getString("CDateOperandExpressionMsg9") + operand1 + "\"");
                    }else{
                        if (possibleTimeFieldSpecifiers.indexOf(fieldSpecifier1) == -1)
                            throw new InvalidOperandExpressionException(DBase.resources.getString("CDateOperandExpressionMsg9") + operand1 + "\"");
                    }

                    operand1 = ((String)operand1).substring(0, ((String)operand1).length()-1);

                    try{
                        oper1 = new Integer(new Double((String)operand1).intValue());
                    }catch (ClassCastException e) {throw new InvalidOperandExpressionException(DBase.resources.getString("CDateOperandExpressionMsg9") + operand1 + fieldSpecifier1 + "\"");}
//                     catch (DoubleNumberFormatParseException e) {throw new InvalidOperandExpressionException("5" + DBase.resources.getString("CDateOperandExpressionMsg9") + operand1 + fieldSpecifier1 + "\"");}
                     catch (NumberFormatException e) {throw new InvalidOperandExpressionException(DBase.resources.getString("CDateOperandExpressionMsg9") + operand1 + fieldSpecifier1 + "\"");}
                }
//Because of CDate                if (!operand2.getClass().getName().equals("java.util.Date")) {
                if (!(operand2.getClass().getName().equals("gr.cti.eslate.database.engine.CDate") ||
                     operand2.getClass().getName().equals("gr.cti.eslate.database.engine.CTime"))) {
                     fieldSpecifier2 = ((String) operand2).charAt(((String) operand2).length()-1);

                    if (isDate) {
                        if (possibleDateFieldSpecifiers.indexOf(fieldSpecifier2) == -1)
                            throw new InvalidOperandExpressionException(DBase.resources.getString("CDateOperandExpressionMsg9") + operand2 + "\"");
                    }else{
                        if (possibleTimeFieldSpecifiers.indexOf(fieldSpecifier2) == -1)
                            throw new InvalidOperandExpressionException(DBase.resources.getString("CDateOperandExpressionMsg9") + operand2 + "\"");
                    }

                    operand2 = ((String) operand2).substring(0, ((String) operand2).length()-1);

                    try{
                        oper2 = new Integer(new Double((String)operand2).intValue());
                    }catch (ClassCastException e) {throw new InvalidOperandExpressionException(DBase.resources.getString("CDateOperandExpressionMsg9") + operand2 + fieldSpecifier2 + "\"");}
//                     catch (DoubleNumberFormatParseException e) {throw new InvalidOperandExpressionException("9" + DBase.resources.getString("CDateOperandExpressionMsg9") + operand2 + fieldSpecifier2 + "\"");}
                     catch (NumberFormatException e) {throw new InvalidOperandExpressionException(DBase.resources.getString("CDateOperandExpressionMsg9") + operand2 + fieldSpecifier2 + "\"");}
                }

                if (((String)popped.element).equals("+")) {
//System.out.println("In here with " + oper1 + "," + oper2);
                     if (oper1==null || oper2==null) {
                         if (oper1==oper2)
                             throw new InvalidOperandExpressionException(DBase.resources.getString("CDateOperandExpressionMsg1") + textualFormula + DBase.resources.getString("CDateOperandExpressionMsg10"));

                         Date dateOperand = (oper1 == null)? (Date) operand1: (Date) operand2;
                         Integer oper = (oper1 != null)? oper1 : oper2;
                         char fieldSpec = (oper1 != null)? fieldSpecifier1 : fieldSpecifier2;

                         Calendar c = new GregorianCalendar();
                         c.setTime(dateOperand);
                         if (isDate) {
                             if (fieldSpec == 'Y' || fieldSpec == 'y')
                                 c.add(Calendar.YEAR, oper.intValue());
                             else if (fieldSpec == 'M' || fieldSpec == 'm')
                                 c.add(Calendar.MONTH, oper.intValue());
                             else if (fieldSpec == 'D' || fieldSpec == 'd')
                                 c.add(Calendar.DATE, oper.intValue());
                         }else{
                             if (fieldSpec == 'H' || fieldSpec == 'h')
                                 c.add(Calendar.HOUR, oper.intValue());
                             else if (fieldSpec == 'M' || fieldSpec == 'm')
                                 c.add(Calendar.MINUTE, oper.intValue());
                         }

                         result = c.getTime();
                     }else{ // if (oper1==null || oper2==null)
                        if (isDate)
                            throw new InvalidOperandExpressionException(DBase.resources.getString("CDateOperandExpressionMsg1") + textualFormula + DBase.resources.getString("CDateOperandExpressionMsg11") + operand1.toString() + fieldSpecifier1 + DBase.resources.getString("CDateOperandExpressionMsg12") + operand2.toString() + fieldSpecifier2 + "\"");
                        else{
                            if (fieldSpecifier1 == 'H' || fieldSpecifier1 == 'h')
                                oper1 = new Integer((oper1.intValue()*60));
                            if (fieldSpecifier2 == 'H' || fieldSpecifier2 == 'h')
                                oper2 = new Integer((oper2.intValue()*60));
                            operandStack.push((oper1.intValue()+oper2.intValue())+"m");
                            oper1=oper2=null;
                            continue;
                        }
                     }

                }else{
                     if (oper1==null || oper2==null) {
                         if (oper1==oper2) {
                            if (isDate) {
                                long diff = ((Date) operand2).getTime() - ((Date) operand1).getTime();
                                int dayDiff = (int) (diff/86400000);
                                operandStack.push(dayDiff+"d");
                                continue;
                            }else{
                                long diff = ((Date) operand2).getTime() - ((Date) operand1).getTime();
                                int minuteDiff = (int) (diff/60000);
                                operandStack.push(minuteDiff+"m");
                                continue;
                            }
                         }else{
                             Date dateOperand = (oper1 == null)? (Date) operand1: (Date) operand2;
                             Integer oper = (oper1 != null)? new Integer(0-oper1.intValue()) : new Integer(0-oper2.intValue());
                             char fieldSpec = (oper1 != null)? fieldSpecifier1 : fieldSpecifier2;

                             Calendar c = new GregorianCalendar();
                             c.setTime(dateOperand);
                             if (isDate) {
                                 if (fieldSpec == 'Y' || fieldSpec == 'y')
                                     c.add(Calendar.YEAR, oper.intValue());
                                 else if (fieldSpec == 'M' || fieldSpec == 'm')
                                     c.add(Calendar.MONTH, oper.intValue());
                                 else if (fieldSpec == 'D' || fieldSpec == 'd')
                                     c.add(Calendar.DATE, oper.intValue());
                             }else{
                                 if (fieldSpec == 'H' || fieldSpec == 'h')
                                     c.add(Calendar.HOUR, oper.intValue());
                                 else if (fieldSpec == 'M' || fieldSpec == 'm')
                                     c.add(Calendar.MINUTE, oper.intValue());
                             }

                             result = c.getTime();
                         }
                     }else{
                        if (isDate)
                            throw new InvalidOperandExpressionException(DBase.resources.getString("CDateOperandExpressionMsg1") + textualFormula + DBase.resources.getString("CDateOperandExpressionMsg11") + operand1.toString() + fieldSpecifier1 + DBase.resources.getString("CDateOperandExpressionMsg12") + operand2.toString() + fieldSpecifier2 + "\"");
                        else{
                            if (fieldSpecifier1 == 'H' || fieldSpecifier1 == 'h')
                                oper1 = new Integer((oper1.intValue()*60));
                            if (fieldSpecifier2 == 'H' || fieldSpecifier2 == 'h')
                                oper2 = new Integer((oper2.intValue()*60));
                            operandStack.push((oper2.intValue()-oper1.intValue())+"m");
                            oper1=oper2=null;
                            continue;
                        }
                     }
                }

                oper1=oper2=null;
                if (isDate)
                    operandStack.push(new CDate(result));
                else
                    operandStack.push(new CTime(result));
            }else{
                operandStack.push(popped.element);
            }
        }//while

        try{
           result = ((Date) operandStack.pop());
        }catch (ClassCastException e) {throw new InvalidOperandExpressionException(DBase.resources.getString("CDateOperandExpressionMsg13"));}

        /* For date expressions on Time fields, switch back to the base date: 1/1/1970.
         * Calculations on Time fields with minutes and hours may yield a time in a
         * diffrent day. To recover this problem, we set the date of the "result" to the
         * base date.
         */
        if (!isDate) {
            Calendar c = new GregorianCalendar();
            c.setTime(result);
            c.set(1970, 0, 1);
            return c.getTime();
        }
        if (isDate)
            return new CDate(result);
        else
            return new CTime(result);
    }


    protected Object execute(int rowIndex, boolean calculatedFieldExpression) {

//???        calculatedFieldExpression = moreThan1DateFields;

        if (invalidOperandExpression)
            return null;

        Stack operandStack = new Stack();
        Object operand1;
        Object operand2;
        Date result = null;
        char fieldSpecifier1 = 'm', fieldSpecifier2 = 'm';
        Integer oper1=null, oper2=null;
        String possibleDateFieldSpecifiers = "YyMmDd";
        String possibleTimeFieldSpecifiers = "HhMm";

        StackElement popped;
        Stack postfixClone = (Stack) postfix.clone();
        Object obj;

        while (!postfixClone.isEmpty()) {
            try {
                popped = (StackElement) postfixClone.pop();
            }catch (InvalidOperationException e) {System.out.println("Serious inconsistency error in DateOperandExpression execute(int, boolean): (1)"); return new Date();}

//            System.out.println(popped + ", " + popped.element + ", " + popped.isFieldIndex);
            if (popped.isFieldIndex) {
                String elem = (String) popped.element;
                if (elem.charAt(elem.length()-1) == ']') {
                    elem = elem.substring(1, elem.length()-1);
                    try{
                        obj = table.riskyGetCell(new Integer(elem).intValue(), rowIndex);
                    }catch (NumberFormatException e) {
                        invalidOperandExpression = true;
                        if (frame == null) frame = new JFrame();
                        ESlateOptionPane.showMessageDialog(frame, DBase.resources.getString("CDateOperandExpressionMsg1") + textualFormula + "\"", DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return null;
                    }

                    if (obj == null)
                        return null;
                }else{
                    String fldSpec = elem.substring(elem.length()-1);
                    elem = elem.substring(1, elem.length()-2);
                    try{
                        obj = table.riskyGetCell(new Integer(elem).intValue(), rowIndex);
                        if (obj==null)
                            return null;
                    }catch (NumberFormatException e) {
                        invalidOperandExpression = true;
                        if (frame == null) frame = new JFrame();
                        ESlateOptionPane.showMessageDialog(frame,  DBase.resources.getString("CDateOperandExpressionMsg1") + textualFormula + "\"", DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                    obj = obj.toString()+fldSpec;
                }

                operandStack.push(obj);
                continue;
            }

            if (popped.element.getClass().getName().equals("java.lang.String") && (((String) popped.element).equals("+") || ((String) popped.element).equals("-"))) {
                operand1 = operandStack.pop();
                operand2 = operandStack.pop();

//                System.out.println("operand1: "+ operand1 + " " + operand1.getClass() + ", operand2: " + operand2);
//                if (!operand1.getClass().getName().equals("java.util.Date")) {
                if (!(operand1.getClass().getName().equals("gr.cti.eslate.database.engine.CDate") ||
                      operand1.getClass().getName().equals("gr.cti.eslate.database.engine.CTime"))) {
                    fieldSpecifier1 = operand1.toString().charAt(operand1.toString().length()-1);

//                  System.out.println("fieldSpecifier1: "+ fieldSpecifier1);

                    if (isDate) {
                        if (possibleDateFieldSpecifiers.indexOf(fieldSpecifier1) == -1) {
                            invalidOperandExpression = true;
                            if (frame == null) frame = new JFrame();
                            ESlateOptionPane.showMessageDialog(frame, DBase.resources.getString("CDateOperandExpressionMsg9") + operand1 + "\"", DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                            return null;
                        }
                    }else{
                        if (possibleTimeFieldSpecifiers.indexOf(fieldSpecifier1) == -1) {
                            invalidOperandExpression = true;
                            if (frame == null) frame = new JFrame();
                            ESlateOptionPane.showMessageDialog(frame, DBase.resources.getString("CDateOperandExpressionMsg9") + operand1 + "\"", DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                            return null;
                        }
                    }

                    operand1 = ((String)operand1).substring(0, ((String)operand1).length()-1);

                    try{
                        oper1 = new Integer(new Double((String) operand1).intValue());
                    }catch (ClassCastException e) {
                        invalidOperandExpression = true;
                        if (frame == null) frame = new JFrame();
                        ESlateOptionPane.showMessageDialog(frame, DBase.resources.getString("CDateOperandExpressionMsg9") + operand1 + fieldSpecifier1 + "\"", DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                     catch (NumberFormatException e) {
                        invalidOperandExpression = true;
                        if (frame == null) frame = new JFrame();
                        ESlateOptionPane.showMessageDialog(frame, DBase.resources.getString("CDateOperandExpressionMsg9") + operand1 + fieldSpecifier1 + "\"", DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return null;
                   }
/*                     catch (DoubleNumberFormatParseException e) {
                        invalidOperandExpression = true;
                        if (frame == null) frame = new JFrame();
                        ESlateOptionPane.showMessageDialog(frame, "14" + DBase.resources.getString("CDateOperandExpressionMsg9") + operand1 + fieldSpecifier1 + "\"", DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return null;
                   }
*/                }
//Because of CDate if (!operand2.getClass().getName().equals("java.util.Date")) {
                if (!(operand2.getClass().getName().equals("gr.cti.eslate.database.engine.CDate") ||
                      operand2.getClass().getName().equals("gr.cti.eslate.database.engine.CTime"))) {
                    fieldSpecifier2 = operand2.toString().charAt(operand2.toString().length()-1);

                    if (isDate) {
                        if (possibleDateFieldSpecifiers.indexOf(fieldSpecifier2) == -1) {
                            invalidOperandExpression = true;
                            if (frame == null) frame = new JFrame();
                            ESlateOptionPane.showMessageDialog(frame, DBase.resources.getString("CDateOperandExpressionMsg9") + operand2 + "\"", DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                            return null;
                        }
                    }else{
                        if (possibleTimeFieldSpecifiers.indexOf(fieldSpecifier2) == -1) {
                            invalidOperandExpression = true;
                            if (frame == null) frame = new JFrame();
                            ESlateOptionPane.showMessageDialog(frame, DBase.resources.getString("CDateOperandExpressionMsg9") + operand2 + "\"", DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                            return null;
                        }
                    }

                    operand2 = ((String) operand2).substring(0, ((String) operand2).length()-1);

                    try{
                        oper2 = new Integer(new Double((String) operand2).intValue());
                    }catch (ClassCastException e) {
                        invalidOperandExpression = true;
                        if (frame == null) frame = new JFrame();
                        ESlateOptionPane.showMessageDialog(frame, DBase.resources.getString("CDateOperandExpressionMsg9") + operand2 + fieldSpecifier2 + "\"", DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                     catch (NumberFormatException e) {
                        invalidOperandExpression = true;
                        if (frame == null) frame = new JFrame();
                        ESlateOptionPane.showMessageDialog(frame, DBase.resources.getString("CDateOperandExpressionMsg9") + operand2 + fieldSpecifier2 + "\"", DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return null;
                     }
/*                     catch (DoubleNumberFormatParseException e) {
                        invalidOperandExpression = true;
                        if (frame == null) frame = new JFrame();
                        ESlateOptionPane.showMessageDialog(frame, "14" + DBase.resources.getString("CDateOperandExpressionMsg9") + operand2 + fieldSpecifier2 + "\"", DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return null;
                   }
*/                }

                if (((String)popped.element).equals("+")) {
                     if (oper1==null || oper2==null) {
                         if (oper1==oper2) {
                            invalidOperandExpression = true;
                            if (frame == null) frame = new JFrame();
                            ESlateOptionPane.showMessageDialog(frame, DBase.resources.getString("CDateOperandExpressionMsg14"), DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                             return null;
                         }

                         Date dateOperand = (oper1 == null)? (Date) operand1: (Date) operand2;
                         Integer oper = (oper1 != null)? oper1 : oper2;
                         char fieldSpec = (oper1 != null)? fieldSpecifier1 : fieldSpecifier2;

                         Calendar c = new GregorianCalendar();
                         c.setTime(dateOperand);
                         if (isDate) {
                             if (fieldSpec == 'Y' || fieldSpec == 'y')
                                 c.add(Calendar.YEAR, oper.intValue());
                             else if (fieldSpec == 'M' || fieldSpec == 'm')
                                 c.add(Calendar.MONTH, oper.intValue());
                             else if (fieldSpec == 'D' || fieldSpec == 'd')
                                 c.add(Calendar.DATE, oper.intValue());
                         }else{
                             if (fieldSpec == 'H' || fieldSpec == 'h')
                                 c.add(Calendar.HOUR, oper.intValue());
                             else if (fieldSpec == 'M' || fieldSpec == 'm')
                                 c.add(Calendar.MINUTE, oper.intValue());
                         }

                         result = c.getTime();
                     }else{
                        if (isDate) {
/*                            invalidOperandExpression = true;
                            //This System.out.println(...) has to be diplayed in a message box
                            System.out.println("Cannot add operands1 \"" + operand1.toString() + fieldSpecifier1 + "\" add \"" + operand2.toString() + fieldSpecifier2 + "\"");
                            return null;
*/
                            if (fieldSpecifier1 == 'Y' || fieldSpecifier1 == 'y')
                                oper1 = new Integer((oper1.intValue()*365));
                            if (fieldSpecifier1 == 'M' || fieldSpecifier1 == 'm')
                                oper1 = new Integer((oper1.intValue()*30));
                            if (fieldSpecifier2 == 'Y' || fieldSpecifier2 == 'Y')
                                oper2 = new Integer((oper2.intValue()*365));
                            if (fieldSpecifier2 == 'M' || fieldSpecifier2 == 'm')
                                oper2 = new Integer((oper2.intValue()*30));
                            operandStack.push((oper1.intValue()+oper2.intValue())+"d");
                            oper1=oper2=null;
                            continue;
                        }else{
                            if (fieldSpecifier1 == 'H' || fieldSpecifier1 == 'h')
                                oper1 = new Integer((oper1.intValue()*60));
                            if (fieldSpecifier2 == 'H' || fieldSpecifier2 == 'h')
                                oper2 = new Integer((oper2.intValue()*60));
                            operandStack.push((oper1.intValue()+oper2.intValue())+"m");
                            oper1=oper2=null;
                            continue;
                        }
                     }

                }else{ // if (((String)popped.element).equals("+"))
                     if (oper1==null || oper2==null) {
                         if (oper1==oper2) {
                            if (isDate) {
                                long diff = ((Date) operand2).getTime() - ((Date) operand1).getTime();
                                int dayDiff = (int) (diff/86400000);
                                operandStack.push(dayDiff+"d");
                                continue;
                            }else{
                                long diff = ((Date) operand2).getTime() - ((Date) operand1).getTime();
                                int minuteDiff = (int) (diff/60000);
                                operandStack.push(minuteDiff+"m");
                                continue;
                            }
                         }else{

                             Date dateOperand = (oper1 == null)? (Date) operand1: (Date) operand2;
                             Integer oper = (oper1 != null)? new Integer(0-oper1.intValue()) : new Integer(0-oper2.intValue());
                             char fieldSpec = (oper1 != null)? fieldSpecifier1 : fieldSpecifier2;

                             Calendar c = new GregorianCalendar();
                             c.setTime(dateOperand);
                             if (isDate) {
                                 if (fieldSpec == 'Y' || fieldSpec == 'y')
                                     c.add(Calendar.YEAR, oper.intValue());
                                 else if (fieldSpec == 'M' || fieldSpec == 'm')
                                     c.add(Calendar.MONTH, oper.intValue());
                                 else if (fieldSpec == 'D' || fieldSpec == 'd')
                                     c.add(Calendar.DATE, oper.intValue());
                             }else{
                                 if (fieldSpec == 'H' || fieldSpec == 'h')
                                     c.add(Calendar.HOUR, oper.intValue());
                                 else if (fieldSpec == 'M' || fieldSpec == 'm')
                                     c.add(Calendar.MINUTE, oper.intValue());
                             }

                             result = c.getTime();
                         }
                     }else{
                        if (isDate) {
/*                            invalidOperandExpression = true;
                            //This System.out.println(...) has to be diplayed in a message box
                            System.out.println("Cannot add operands2 \"" + operand1.toString() + fieldSpecifier1 + "\" add \"" + operand2.toString() + fieldSpecifier2 + "\"");
                            return null;
*/
                            if (fieldSpecifier1 == 'Y' || fieldSpecifier1 == 'y')
                                oper1 = new Integer((oper1.intValue()*365));
                            if (fieldSpecifier1 == 'M' || fieldSpecifier1 == 'm')
                                oper1 = new Integer((oper1.intValue()*30));
                            if (fieldSpecifier2 == 'Y' || fieldSpecifier2 == 'Y')
                                oper2 = new Integer((oper2.intValue()*365));
                            if (fieldSpecifier2 == 'M' || fieldSpecifier2 == 'm')
                                oper2 = new Integer((oper2.intValue()*30));
                            operandStack.push((oper2.intValue()-oper1.intValue())+"d");
                            oper1=oper2=null;
                            continue;
                        }else{
                            if (fieldSpecifier1 == 'H' || fieldSpecifier1 == 'h')
                                oper1 = new Integer((oper1.intValue()*60));
                            if (fieldSpecifier2 == 'H' || fieldSpecifier2 == 'h')
                                oper2 = new Integer((oper2.intValue()*60));
                            operandStack.push((oper2.intValue()-oper1.intValue())+"m");
                            oper1=oper2=null;
                            continue;
                        }
                     }
                }

                oper1=oper2=null;
                if (isDate)
                    operandStack.push(new CDate(result));
                 else
                    operandStack.push(new CTime(result));
            }else{
                operandStack.push(popped.element);
            }
        }//while

        if (!calculatedFieldExpression) {
            try{
                result = ((Date) operandStack.pop());
            }catch (ClassCastException e) {
                invalidOperandExpression = true;
                if (frame == null) frame = new JFrame();
                ESlateOptionPane.showMessageDialog(frame, DBase.resources.getString("CDateOperandExpressionMsg1") + textualFormula + "\"", DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return null;
            }

            /* For date expressions on Time fields, switch back to the base date: 1/1/1970.
            * Calculations on Time fields with minutes and hours may yield a time in a
             * diffrent day. To recover this problem, we set the date of the "result" to the
             * base date.
             */
            if (!isDate) {
                Calendar c = new GregorianCalendar();
                c.setTime(result);
                c.set(1970, 0, 1);
                return new CTime(c.getTime());
            }
        /* In queries which involve Date or Time fields it is imparative that the "result"
         * of the calculation of the operand expression is a valid Date or a Time. However
         * when the formula of a calculated field is evaluated, the outcome may also be
         * an Integer (which expresses Date or Time operand differences) apart from Dates or
         * Time instances. When this method is used on a calculated field expression the
         * "calculatedFieldExpression" is set. This does not happen for all calculated field
         * expressions. Only in those where more than one dates/time instances or Date/Time
         * fields are present. In this case the result of the evaluation of the operand
         * expression is not cast to Date, but is returned as is.
         */
        }else{
            String s = "";
            Object res = operandStack.pop();
            if (s.getClass().isInstance(res)) { // res is a String
                s = (String) res;
//              System.out.print("s: " + s);
                s = s.substring(0, s.length()-1);
//              System.out.print(", s: " + s);
                Integer a;
                try{
                    a = new Integer(s);
                }catch (NumberFormatException e) {return null;}
//                System.out.println(", a: " + a);
                return (a);
            }else{  //res is Date
                if (isDate)
                    return new CDate((Date) res);
                else
                    return new CTime((Date) res);
            }
        }

        if (isDate)
            return new CDate(result);
        else
            return new CTime(result);
    }


    protected Class getCalcFieldType(boolean calculatedFieldExpression) {

        calculatedFieldExpression = moreThan1DateFields;

        if (invalidOperandExpression)
            return null;

        Stack operandStack = new Stack();
        Object operand1;
        Object operand2;
        Date result = null;
        char fieldSpecifier1 = 'm', fieldSpecifier2 = 'm';
        Integer oper1=null, oper2=null;
        String possibleDateFieldSpecifiers = "YyMmDd";
        String possibleTimeFieldSpecifiers = "HhMm";
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy:HH");
        SimpleDateFormat stf2 = new SimpleDateFormat("HH:mm");

        StackElement popped;
        Stack postfixClone = (Stack) postfix.clone();
        Object obj;

        while (!postfixClone.isEmpty()) {
            try {
                popped = (StackElement) postfixClone.pop();
            }catch (InvalidOperationException e) {
                System.out.println("Serious inconsistency error in DateOperandExpression execute(int, boolean): (1)");
//t                return java.util.Date.class;
                return gr.cti.eslate.database.engine.CDate.class;
            }

//            System.out.println(popped + ", " + popped.element + ", " + popped.isFieldIndex);
            if (popped.isFieldIndex) {
                String elem = (String) popped.element;
                if (elem.charAt(elem.length()-1) == ']') {
                    if (isDate)
                        obj = new CDate(sdf2.parse("1/1/1990"+":02", new ParsePosition(0)));
                    else
                        obj = new CTime(stf2.parse("00:00", new ParsePosition(0)));
                }else{
                    obj = "1" + 'm';
                }

                operandStack.push(obj);
                continue;
            }

            if (popped.element.getClass().getName().equals("java.lang.String") && (((String) popped.element).equals("+") || ((String) popped.element).equals("-"))) {
                operand1 = operandStack.pop();
                operand2 = operandStack.pop();

//                System.out.println("operand1: "+ operand1 + " " + operand1.getClass() + ", operand2: " + operand2);
//                if (!operand1.getClass().getName().equals("java.util.Date")) {
                if (!(operand1.getClass().getName().equals("gr.cti.eslate.database.engine.CDate") ||
                      operand1.getClass().getName().equals("gr.cti.eslate.database.engine.CTime"))) {
                    fieldSpecifier1 = operand1.toString().charAt(operand1.toString().length()-1);

//                  System.out.println("fieldSpecifier1: "+ fieldSpecifier1);

                    if (isDate) {
                        if (possibleDateFieldSpecifiers.indexOf(fieldSpecifier1) == -1) {
                            invalidOperandExpression = true;
//                            if (frame == null) frame = new JFrame();
//                            JOptionPane.showMessageDialog(frame, "Invalid operand: \"" + operand1 + "\"", "Error", JOptionPane.ERROR_MESSAGE);
                            //This System.out.println(...) has to be diplayed in a message box
//                            System.out.println("Invalid operand9: \"" + operand1 + "\"");
                            return null;
                        }
                    }else{
                        if (possibleTimeFieldSpecifiers.indexOf(fieldSpecifier1) == -1) {
                            invalidOperandExpression = true;
//                            if (frame == null) frame = new JFrame();
//                            JOptionPane.showMessageDialog(frame, "Invalid operand: \"" + operand1 + "\"", "Error", JOptionPane.ERROR_MESSAGE);
                            //This System.out.println(...) has to be diplayed in a message box
//                            System.out.println("Invalid operand10: \"" + operand1 + "\"");
                            return null;
                        }
                    }

                    operand1 = ((String)operand1).substring(0, ((String)operand1).length()-1);

                    try{
                        oper1 = new Integer(new Double((String)operand1).intValue());
                    }catch (ClassCastException e) {
                        invalidOperandExpression = true;
//                        if (frame == null) frame = new JFrame();
//                        JOptionPane.showMessageDialog(frame, "Invalid operand \"" + operand1 + fieldSpecifier1 + "\"", "Error", JOptionPane.ERROR_MESSAGE);
                        //This System.out.println(...) has to be diplayed in a message box
//                        System.out.println("Invalid operand11: \"" + operand1 + fieldSpecifier1 + "\"");
                        return null;
                    }
                     catch (NumberFormatException e) {
                        invalidOperandExpression = true;
//                        if (frame == null) frame = new JFrame();
//                        JOptionPane.showMessageDialog(frame, "Invalid operand \"" + operand1 + fieldSpecifier1 + "\"", "Error", JOptionPane.ERROR_MESSAGE);
                        //This System.out.println(...) has to be diplayed in a message box
//                        System.out.println("Invalid operand12: \"" + operand1 + fieldSpecifier1 + "\"");
                        return null;
                   }
/*                     catch (DoubleNumberFormatParseException e) {
                        invalidOperandExpression = true;
//                        if (frame == null) frame = new JFrame();
//                        ESlateOptionPane.showMessageDialog(frame, "24" + DBase.resources.getString("CDateOperandExpressionMsg9") + operand1 + fieldSpecifier1 + "\"", DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return null;
                   }
*/                }
//Because of CDate if (!operand2.getClass().getName().equals("java.util.Date")) {
                if (!(operand2.getClass().getName().equals("gr.cti.eslate.database.engine.CDate") ||
                      operand2.getClass().getName().equals("gr.cti.eslate.database.engine.CTime"))) {
                    fieldSpecifier2 = operand2.toString().charAt(operand2.toString().length()-1);

                    if (isDate) {
                        if (possibleDateFieldSpecifiers.indexOf(fieldSpecifier2) == -1) {
                            invalidOperandExpression = true;
//                            if (frame == null) frame = new JFrame();
//                            JOptionPane.showMessageDialog(frame, "Invalid operand: \"" + operand2 + "\"", "Error", JOptionPane.ERROR_MESSAGE);
                            //This System.out.println(...) has to be diplayed in a message box
//                            System.out.println("Invalid operand13: \"" + operand2 + "\"");
                            return null;
                        }
                    }else{
                        if (possibleTimeFieldSpecifiers.indexOf(fieldSpecifier2) == -1) {
                            invalidOperandExpression = true;
//                            if (frame == null) frame = new JFrame();
//                            JOptionPane.showMessageDialog(frame, "Invalid operand: \"" + operand2 + "\"", "Error", JOptionPane.ERROR_MESSAGE);
                            //This System.out.println(...) has to be diplayed in a message box
//                            System.out.println("Invalid operand14: \"" + operand2 + "\"");
                            return null;
                        }
                    }

                    operand2 = ((String) operand2).substring(0, ((String) operand2).length()-1);

                    try{
                        oper2 = new Integer(new Double((String)operand2).intValue());
                    }catch (ClassCastException e) {
                        invalidOperandExpression = true;
//                        if (frame == null) frame = new JFrame();
//                        JOptionPane.showMessageDialog(frame, "Invalid operand \"" + operand2 + fieldSpecifier2 + "\"", "Error", JOptionPane.ERROR_MESSAGE);
                        //This System.out.println(...) has to be diplayed in a message box
//                        System.out.println("Invalid operand15: \"" + operand2 + fieldSpecifier2 + "\"");
                        return null;
                    }
                     catch (NumberFormatException e) {
                        invalidOperandExpression = true;
//                        if (frame == null) frame = new JFrame();
//                        JOptionPane.showMessageDialog(frame, "Invalid operand \"" + operand2 + fieldSpecifier2 + "\"", "Error", JOptionPane.ERROR_MESSAGE);
                        //This System.out.println(...) has to be diplayed in a message box
//                        System.out.println("Invalid operand16: \"" + operand2 + fieldSpecifier2 + "\"");
                        return null;
                     }
/*                     catch (DoubleNumberFormatParseException e) {
                        invalidOperandExpression = true;
//                        if (frame == null) frame = new JFrame();
//                        ESlateOptionPane.showMessageDialog(frame, "14" + DBase.resources.getString("CDateOperandExpressionMsg9") + operand1 + fieldSpecifier1 + "\"", DBase.resources.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return null;
                   }
*/                }

                if (((String)popped.element).equals("+")) {
                     if (oper1==null || oper2==null) {
                         if (oper1==oper2) {
                            invalidOperandExpression = true;
//                            if (frame == null) frame = new JFrame();
//                            JOptionPane.showMessageDialog(frame, "Cannot add two Date/Time operands", "Error", JOptionPane.ERROR_MESSAGE);
                            //This System.out.println(...) has to be diplayed in a message box
//                             System.out.println("Cannot add two Date/Time operands");
                             return null;
                         }

                         Date dateOperand = (oper1 == null)? (Date) operand1: (Date) operand2;
                         Integer oper = (oper1 != null)? oper1 : oper2;
                         char fieldSpec = (oper1 != null)? fieldSpecifier1 : fieldSpecifier2;

                         Calendar c = new GregorianCalendar();
                         c.setTime(dateOperand);
                         if (isDate) {
                             if (fieldSpec == 'Y' || fieldSpec == 'y')
                                 c.add(Calendar.YEAR, oper.intValue());
                             else if (fieldSpec == 'M' || fieldSpec == 'm')
                                 c.add(Calendar.MONTH, oper.intValue());
                             else if (fieldSpec == 'D' || fieldSpec == 'd')
                                 c.add(Calendar.DATE, oper.intValue());
                         }else{
                             if (fieldSpec == 'H' || fieldSpec == 'h')
                                 c.add(Calendar.HOUR, oper.intValue());
                             else if (fieldSpec == 'M' || fieldSpec == 'm')
                                 c.add(Calendar.MINUTE, oper.intValue());
                         }

                         result = c.getTime();
                     }else{
                        if (isDate) {
/*                            invalidOperandExpression = true;
                            //This System.out.println(...) has to be diplayed in a message box
                            System.out.println("Cannot add operands1 \"" + operand1.toString() + fieldSpecifier1 + "\" add \"" + operand2.toString() + fieldSpecifier2 + "\"");
                            return null;
*/
                            if (fieldSpecifier1 == 'Y' || fieldSpecifier1 == 'y')
                                oper1 = new Integer((oper1.intValue()*365));
                            if (fieldSpecifier1 == 'M' || fieldSpecifier1 == 'm')
                                oper1 = new Integer((oper1.intValue()*30));
                            if (fieldSpecifier2 == 'Y' || fieldSpecifier2 == 'Y')
                                oper2 = new Integer((oper2.intValue()*365));
                            if (fieldSpecifier2 == 'M' || fieldSpecifier2 == 'm')
                                oper2 = new Integer((oper2.intValue()*30));
                            operandStack.push((oper1.intValue()+oper2.intValue())+"d");
                            oper1=oper2=null;
                            continue;
                        }else{
                            if (fieldSpecifier1 == 'H' || fieldSpecifier1 == 'h')
                                oper1 = new Integer((oper1.intValue()*60));
                            if (fieldSpecifier2 == 'H' || fieldSpecifier2 == 'h')
                                oper2 = new Integer((oper2.intValue()*60));
                            operandStack.push((oper1.intValue()+oper2.intValue())+"m");
                            oper1=oper2=null;
                            continue;
                        }
                     }

                }else{ // if (((String)popped.element).equals("+"))
                     if (oper1==null || oper2==null) {
                         if (oper1==oper2) {
                            if (isDate) {
                                long diff = ((Date) operand2).getTime() - ((Date) operand1).getTime();
                                int dayDiff = (int) (diff/86400000);
                                operandStack.push(dayDiff+"d");
                                continue;
                            }else{
                                long diff = ((Date) operand2).getTime() - ((Date) operand1).getTime();
                                int minuteDiff = (int) (diff/60000);
                                operandStack.push(minuteDiff+"m");
                                continue;
                            }
                         }else{

                             Date dateOperand = (oper1 == null)? (Date) operand1: (Date) operand2;
                             Integer oper = (oper1 != null)? new Integer(0-oper1.intValue()) : new Integer(0-oper2.intValue());
                             char fieldSpec = (oper1 != null)? fieldSpecifier1 : fieldSpecifier2;

                             Calendar c = new GregorianCalendar();
                             c.setTime(dateOperand);
                             if (isDate) {
                                 if (fieldSpec == 'Y' || fieldSpec == 'y')
                                     c.add(Calendar.YEAR, oper.intValue());
                                 else if (fieldSpec == 'M' || fieldSpec == 'm')
                                     c.add(Calendar.MONTH, oper.intValue());
                                 else if (fieldSpec == 'D' || fieldSpec == 'd')
                                     c.add(Calendar.DATE, oper.intValue());
                             }else{
                                 if (fieldSpec == 'H' || fieldSpec == 'h')
                                     c.add(Calendar.HOUR, oper.intValue());
                                 else if (fieldSpec == 'M' || fieldSpec == 'm')
                                     c.add(Calendar.MINUTE, oper.intValue());
                             }

                             result = c.getTime();
                         }
                     }else{
                        if (isDate) {
/*                            invalidOperandExpression = true;
                            //This System.out.println(...) has to be diplayed in a message box
                            System.out.println("Cannot add operands2 \"" + operand1.toString() + fieldSpecifier1 + "\" add \"" + operand2.toString() + fieldSpecifier2 + "\"");
                            return null;
*/
                            if (fieldSpecifier1 == 'Y' || fieldSpecifier1 == 'y')
                                oper1 = new Integer((oper1.intValue()*365));
                            if (fieldSpecifier1 == 'M' || fieldSpecifier1 == 'm')
                                oper1 = new Integer((oper1.intValue()*30));
                            if (fieldSpecifier2 == 'Y' || fieldSpecifier2 == 'Y')
                                oper2 = new Integer((oper2.intValue()*365));
                            if (fieldSpecifier2 == 'M' || fieldSpecifier2 == 'm')
                                oper2 = new Integer((oper2.intValue()*30));
                            operandStack.push((oper2.intValue()-oper1.intValue())+"d");
                            oper1=oper2=null;
                            continue;
                        }else{
                            if (fieldSpecifier1 == 'H' || fieldSpecifier1 == 'h')
                                oper1 = new Integer((oper1.intValue()*60));
                            if (fieldSpecifier2 == 'H' || fieldSpecifier2 == 'h')
                                oper2 = new Integer((oper2.intValue()*60));
                            operandStack.push((oper2.intValue()-oper1.intValue())+"m");
                            oper1=oper2=null;
                            continue;
                        }
                     }
                }

                oper1=oper2=null;
                if (isDate)
                    operandStack.push(new CDate(result));
                 else
                    operandStack.push(new CTime(result));
            }else{
                operandStack.push(popped.element);
            }
        }//while

        Object result1 = operandStack.pop();
//        System.out.println("result1: " + result1);
        if (result1.getClass().getName().equals("java.lang.String"))
            return java.lang.Integer.class;
        else
            return result1.getClass();

    }


    private void writeObject(ObjectOutputStream out) throws IOException {
        /* In 1.0 version of the storage format the 'table' field was also
         * stored. This stopped in version 1.1. Instead the CTable which
         * restores every AbstractTableField also assigns its OperandExpression's
         * 'table' field.
         */
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("Postfix", postfix);
        fieldMap.put("Operator stack", operatorStack);
        fieldMap.put("Date format", sdf);
        fieldMap.put("Time format", stf);
        fieldMap.put("Date", isDate);
//1.1        fieldMap.put("Table", table);
        fieldMap.put("Invalid operand expression", invalidOperandExpression);
        fieldMap.put("More than one fields", moreThan1DateFields);
        fieldMap.put("Textual formula", textualFormula);

        out.writeObject(fieldMap);

/*        out.writeObject(postfix);
        out.writeObject(operatorStack);
        out.writeObject(sdf);
        out.writeObject(stf);
        out.writeObject(new Boolean(isDate));
        out.writeObject(table);
        out.writeObject(new Boolean(invalidOperandExpression));
        out.writeObject(new Boolean(moreThan1DateFields));
        out.writeObject(textualFormula);
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
            sdf = (SimpleDateFormat) fieldMap.get("Date format");
            stf = (SimpleDateFormat) fieldMap.get("Time format");
            isDate = fieldMap.get("Date", true);
//1.1            table = (CTable) fieldMap.get("Table");
            invalidOperandExpression = fieldMap.get("Invalid operand expression", false);
            moreThan1DateFields = fieldMap.get("More than one fields", false);
            textualFormula = (String) fieldMap.get("Textual formula");
        }
    }

    private void oldReadObject(ObjectInputStream in, Object firstObj) throws IOException {
        System.out.println("DateOperandExpression oldReadObject()");
        try{
            postfix = (Stack) firstObj; //in.readObject();
            operatorStack = (Stack) in.readObject();
            sdf = (SimpleDateFormat) in.readObject();
            stf = (SimpleDateFormat) in.readObject();
            isDate = ((Boolean) in.readObject()).booleanValue();
            Object tableStructure = in.readObject();
            if (CTable.class.isAssignableFrom(tableStructure.getClass()))
                table = new Table((CTable) tableStructure);
            else
                table = (Table) tableStructure;
            invalidOperandExpression = ((Boolean) in.readObject()).booleanValue();
            moreThan1DateFields = ((Boolean) in.readObject()).booleanValue();
            textualFormula = (String) in.readObject();
        }catch (Exception e) {
//            System.out.println(e.getClass() + ", " + e.getMessage());
            throw new IOException(e.getMessage());
        }
    }

}
