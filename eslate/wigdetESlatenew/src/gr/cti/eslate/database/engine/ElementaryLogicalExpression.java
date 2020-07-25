package gr.cti.eslate.database.engine;

import java.util.ArrayList;
//import com.objectspace.jgl.*;
//import com.objectspace.jgl.BinaryPredicate;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.net.URL;


/**
 * @version	2.0, May 01
 */
public class ElementaryLogicalExpression extends ELE {
    private AbstractTableField field;
    private SecondPredicateComparator predicate;
    private int fieldIndex;
    private LogicalExpression logicExpr;
    private static Calendar calendar = new GregorianCalendar();


    protected ElementaryLogicalExpression(LogicalExpression le, String fieldName, String Operator, Object operand)
    throws InvalidFieldNameException, InvalidOperatorException, InvalidOperandException {

//System.out.println("Operand: " + operand + ", operand class: " + operand.getClass());
        if (operand == null)
            throw new InvalidOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg1") + operand);

        int fldIndex = 0;
        try {
            fldIndex = le.table.getFieldIndex(fieldName);
        }catch (InvalidFieldNameException e) {throw new InvalidFieldNameException(DBase.resources.getString("CDatabaseMsg74") + fieldName + DBase.resources.getString("CImageIconMsg2"));}

        AbstractTableField f = (AbstractTableField) le.table.tableFields.get(fldIndex);

        /* Eliminate possibly existing '"' characters at the beginning and end of
         * "operand", if "operand" is of String data type.
         */
        boolean quotedOperand = false;
//        System.out.println("Oper: " + operand);
        if (f.getDataType().equals(StringTableField.DATA_TYPE) &&
           operand.getClass().getName().equals("java.lang.String") &&
           ((String) operand).charAt(0) == '"' &&
           ((String) operand).charAt(((String) operand).length()-1)== '"' &&
           ((String) operand).substring(1, ((String) operand).length()-1).indexOf('"') == -1 ) {
//System.out.println("Altering operand 1");
            operand = ((String) operand).substring(1, ((String) operand).length()-1);
//            operand = " " + ((String) operand) + " ";
//            System.out.println("1" + ((String) operand) +"2");
            quotedOperand = true;
        }

        //Formatted string variables
        int asteriskIndex = 0;
        boolean formattedOperand = false;
        boolean formattedDay = false;
        boolean formattedMonth = false;
        boolean formattedYear = false;
        boolean formattedMinute = false;
        boolean formattedHour = false;

        //URLs are queried as strings through the "contains" and "contained" operators
//System.out.println("f.getName(): " + f.getName() +", f.getFieldType().getName(): " + f.getFieldType().getName());
//System.out.println("operand.getClass().getName(): " + operand.getClass().getName());
        if ((!(f.getDataType().getName().equals(operand.getClass().getName()))) && (!(f.getDataType().equals(URLTableField.DATA_TYPE)))) {
//            System.out.println("Should not be here");
            Class[] cl = new Class[1];
            Object[] val = new Object[1];
            Constructor con;
//t            if (f.fieldType.getName().equals("java.util.Date")) {
//System.out.println("Here 1");
            if (f.getDataType().equals(DateTableField.DATA_TYPE)) {
//System.out.println("Here 2");
                Date d = new Date();
//t                if (f.isDate()) {
                    //Date query format
  //              SimpleDateFormat sdf = le.table.getDateFormat(); //new SimpleDateFormat("dd/MM/yyyy:HH");
//                    System.out.println("Current pattern: " + le.table.getDateFormat().toPattern());
//                sdf.setLenient(false);
                boolean operandExpression = false;

                //FORMATTED DATE operands
                if (operand.getClass().isInstance((Object) "A string")) {  //formatted date operands can only be in string data type
                    String operandStr = (String) operand;

                    /* Date Operand expression -start
                     */
                    if (operandStr.indexOf('+')!=-1 || operandStr.indexOf('-')!=-1
                       || operandStr.indexOf('(')!=-1 || operandStr.indexOf(')')!=-1) {
                        operandExpression = true;
                        DateOperandExpression doe;
                        try{
                            doe = new DateOperandExpression(operandStr, operandStr, true, le.table, 1);
                        }catch (InvalidOperandExpressionException e) {throw new InvalidOperandException(e.message);}
                         catch (InvalidDataFormatException e) {throw new InvalidOperandException(e.message);}
                         catch (IllegalDateOperandExpression e) {throw new InvalidOperandException(e.message);}
                        try{
//                                System.out.println("Executing doe");
                            operand = (Date) doe.execute();
                        }catch (InvalidOperandExpressionException e) {throw new InvalidOperandException(e.message);}
                    /* Date Operand expression -end
                     */
                    }else{
                        while ((asteriskIndex = operandStr.indexOf('*', asteriskIndex)) != -1) {
                            formattedOperand = true;
                            if (asteriskIndex !=0) {
                                if (operandStr.charAt(asteriskIndex-1) != '/' && (operandStr.charAt(asteriskIndex-1) >='0' && operandStr.charAt(asteriskIndex-1) <='9'))
                                    throw new InvalidOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg2"));
                            }
                            if (asteriskIndex != operandStr.length()-1) {
                                if (operandStr.charAt(asteriskIndex+1) != '/' && (operandStr.charAt(asteriskIndex+1) >='0' && operandStr.charAt(asteriskIndex+1) <='9'))
                                    throw new InvalidOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg2"));
                            }

                            //Calculate the date field which has the '*'
                            int slashCount=0;
                            for (int i=0; i<asteriskIndex; i++) {
                                if (operandStr.charAt(i) == '/')
                                    slashCount++;
                            }
                            if (slashCount==0) formattedDay = true;
                            else if (slashCount==1) formattedMonth = true;
                            else if (slashCount==2) formattedYear = true;

                            StringBuffer temp = new StringBuffer(operandStr);
                            temp.setCharAt(asteriskIndex, '1');
                            if (slashCount ==2)
                                temp.append("970");  //Forming 1970 year
                            operandStr = temp.toString();

                            if (asteriskIndex==operandStr.length()-1)
                                break;
                            else
                                asteriskIndex++;
                        }
                        operand = (Object) operandStr;
                    }// if (operandStr.indexOf('+')!=-1 ...
                }//if (...isInstance()..)

                if (!operandExpression) {
                    try{
//                            System.out.println("ELE operand: " + operand);
                        if (((String) operand).charAt(0) == '\"' && ((String) operand).charAt(((String) operand).length()-1) == '\"')
                            operand = ((String) operand).substring(1, ((String) operand).length()-1);
//                            System.out.println("ELE operand: " + operand);
//                            System.out.println("sdf: " + sdf.toPattern());
                        d = le.table.dateFormat.parse(((String) operand), new ParsePosition(0));
//                            System.out.println("d: " + d);
                        calendar.setTime(d);
                        calendar.add(Calendar.HOUR, 10);
                        d = new CDate(calendar.getTime());
//                            System.out.println("Operand: " + d);
                    }catch (Exception e) {e.printStackTrace(); throw new InvalidOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg3") + operand + DBase.resources.getString("CTableMsg43") + f.localizedNameForDataType(f) + ".");}
                    if (d == null)
                       throw new InvalidOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg3") + operand + DBase.resources.getString("CTableMsg40"));
                    operand = d;
                }
            }else if (f.getDataType().equals(TimeTableField.DATA_TYPE)) {
                Date d = new Date();
//t                }else{
                //Time query format
//                SimpleDateFormat stf = le.table.getTimeFormat(); //new SimpleDateFormat("H:mm");
//                stf.setLenient(false);
//                stf.getTimeZone().setRawOffset(0); This cause the time to go forward 9 hours
                boolean operandExpression = false;

                //FORMATTED TIME operands
                if (operand.getClass().isInstance((Object) "A string")) {  //formatted time operands can only be in string data type
                    String operandStr = (String) operand;

                    /* Time Operand expression -start
                     */
                    if (operandStr.indexOf('+')!=-1 || operandStr.indexOf('-')!=-1
                       || operandStr.indexOf('(')!=-1 || operandStr.indexOf(')')!=-1) {
                        operandExpression = true;
                        DateOperandExpression doe;
                        try{
                            doe = new DateOperandExpression(operandStr, operandStr, false, le.table, 1);
                        }catch (InvalidOperandExpressionException e) {throw new InvalidOperandException(e.message);}
                         catch (InvalidDataFormatException e) {throw new InvalidOperandException(e.message);}
                         catch (IllegalDateOperandExpression e) {throw new InvalidOperandException(e.message);}
                        try{
                            operand = doe.execute();
                        }catch (InvalidOperandExpressionException e) {throw new InvalidOperandException(e.message);}
                    /* Time Operand expression -end
                     */
                    }else{
                        while ((asteriskIndex = operandStr.indexOf('*', asteriskIndex)) != -1) {
                            formattedOperand = true;
                            if (asteriskIndex !=0) {
                                formattedMinute = true;
                                if (operandStr.charAt(asteriskIndex-1) != ':' && (operandStr.charAt(asteriskIndex-1) >='0' && operandStr.charAt(asteriskIndex-1) <='9'))
                                    throw new InvalidOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg4"));
                            }
                            if (asteriskIndex != operandStr.length()-1) {
                                formattedHour = true;
                                if (operandStr.charAt(asteriskIndex+1) != ':' && (operandStr.charAt(asteriskIndex+1)>='0' && operandStr.charAt(asteriskIndex+1) <='9'))
                                    throw new InvalidOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg4"));
                            }

                            StringBuffer temp = new StringBuffer(operandStr);
                            temp.setCharAt(asteriskIndex, '1');
                            operandStr = temp.toString();

                            if (asteriskIndex==operandStr.length()-1)
                                break;
                            else
                                asteriskIndex++;
                        }
                        operand = (Object) operandStr;
                    }// if (operandStr.indexOf('+') ....
                }// if (...isInstance()...
                if (!operandExpression) {
                    try{
                        d = le.table.timeFormat.parse((String) operand, new ParsePosition(0));
                    }catch (Exception e) {e.printStackTrace(); throw new InvalidOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg3") + operand + DBase.resources.getString("CTableMsg40") + f.localizedNameForDataType(f) + ".");}
                    if (d == null)
                        throw new InvalidOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg3") + operand + DBase.resources.getString("CDateOperandExpressionMsg6"));
                    calendar.setTime(d);
                    d = new CTime(calendar.getTime());
                    operand = d;
                }
            }else{
//                System.out.println("In here...1");
                boolean operandExpression = false;
                /* Numeric operand expression -start
                 */
                Class fieldType = f.getDataType();
                if (fieldType.equals(IntegerTableField.DATA_TYPE) || fieldType.equals(DoubleTableField.DATA_TYPE) || fieldType.equals(FloatTableField.DATA_TYPE)) {
                    if (operand.getClass().getName().equals("java.lang.String")) {
                        String operandStr = (String) operand;

                        if (operandStr.indexOf('+') != -1 || operandStr.indexOf('-') != -1 ||
                         operandStr.indexOf('*') != -1 || operandStr.indexOf('/') != -1 ||
                         operandStr.indexOf('%') != -1 || operandStr.indexOf('$') != -1 ||
                         operandStr.indexOf('(') != -1 || operandStr.indexOf(')') != -1) {
                            /* Check if the operandStr is a single negative number.
                             */
//                            System.out.println("oper: " + operandStr);
                            if (operandStr.trim().indexOf('-') == 0 && (
                               (operandStr.indexOf('+') == -1 &&
                                operandStr.indexOf('*') == -1 && operandStr.indexOf('/') == -1 &&
                                operandStr.indexOf('%') == -1 && operandStr.indexOf('$') == -1 &&
                                operandStr.indexOf('(') == -1 && operandStr.indexOf(')') == -1)))
                                operandExpression = false;
                            else{
                                NumericOperandExpression noe;
                                operandExpression = true;

                                try{
                                    noe = new NumericOperandExpression((String) operandStr, le.table);
                                }catch (InvalidOperandExpressionException e) {throw new InvalidOperandException(e.message);}
                                 catch (InvalidDataFormatException e) {throw new InvalidOperandException(e.message);}
                                if (fieldType.equals(IntegerTableField.DATA_TYPE)) {
                                    try{
                                        operand = new Integer(((Double)noe.execute()).intValue());
                                    }catch (InvalidOperandExpressionException e) {throw new InvalidOperandException(e.message);}
                                }else if (fieldType.equals(FloatTableField.DATA_TYPE)) {
                                        try{
                                            operand = new Float(((Double)noe.execute()).floatValue());
                                        }catch (InvalidOperandExpressionException e) {throw new InvalidOperandException(e.message);}
                                }else{
                                    try{
                                        operand =  (Double) noe.execute();
                                    }catch (InvalidOperandExpressionException e) {throw new InvalidOperandException(e.message);}
                                }
                            }
                        }
                     }
                /* Numeric operand expression -end
                 */
                }
                if (!operandExpression) {
//                    System.out.println("In here...2");
                    /* Double operands are produced separately from the other type of operands,
                     * though the DoubleNumberFormat parse() method.
                     */
                    if (f.getDataType().equals(DoubleTableField.DATA_TYPE)) {
                        try{
//                            System.out.println("operand: \"" + operand + "\"");
                            operand = new Double(le.table.getNumberFormat().parse((String) operand).doubleValue());
//                            System.out.println("operand: " + operand);
                        }catch (ParseException e) {
//                        }catch (DoubleNumberFormatParseException e) {
                            e.printStackTrace();
                            throw new InvalidOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg1") + operand);
                        }
                    }else if (f.getDataType().equals(FloatTableField.DATA_TYPE)) {
                            try{
//                            System.out.println("operand: " + operand);
                                operand = new Float(le.table.getNumberFormat().parse((String) operand).floatValue());
//                            System.out.println("operand: " + operand);
                            }catch (ParseException e) {
//                            }catch (DoubleNumberFormatParseException e) {
                                throw new InvalidOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg1") + operand);
                            }
                    }else{
                        cl[0] = operand.getClass();
                        try {
                            con = f.getDataType().getConstructor(cl);
//                            System.out.println("operand: " + operand + ", operand type: " + operand.getClass() + ", fieldType: " + f.getFieldType());
                            /* Because Boolean operands are localized, they have to be mapped back to
                             * "true" and "false" string values, so that they can be processed correctly.
                             */
                            if (f.getDataType().equals(BooleanTableField.DATA_TYPE) && !operand.equals("true") && !operand.equals("false")) {
                                if (operand.equals(DBase.resources.getString("true")))
                                    operand = "true";
                                else if (operand.equals(DBase.resources.getString("false")))
                                    operand = "false";
                            }
                            val[0] = operand;
//System.out.println("con: " + con + ", con.getName(): " + con.getName());
//System.out.println("val: " + val);
                            try {
                                operand = con.newInstance(val);
//                                System.out.println(operand + " --- " + operand.getClass().getName());
                            }catch (InvocationTargetException e) {
                                String className = f.getDataType().getName();
                                className = className.substring(className.lastIndexOf('.') + 1);
                                e.printStackTrace();
                                throw new InvalidOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg3") + operand + DBase.resources.getString("CTableMsg43") + f.localizedNameForDataType(f));
                            }
                             catch (InstantiationException e) {
                                String className = f.getDataType().getName();
                                className = className.substring(className.lastIndexOf('.') + 1);
                                e.printStackTrace();
                                throw new InvalidOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg3") + operand + DBase.resources.getString("CTableMsg43") + f.localizedNameForDataType(f));
                             }
                             catch (IllegalAccessException e) {
                                String className = f.getDataType().getName();
                                className = className.substring(className.lastIndexOf('.') + 1);
                                e.printStackTrace();
                                throw new InvalidOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg3") + operand + DBase.resources.getString("CTableMsg43") + f.localizedNameForDataType(f));
                             }
                        }catch (NoSuchMethodException e) {
                            String className = f.getDataType().getName();
                            className = className.substring(className.lastIndexOf('.') + 1);
                            e.printStackTrace();
                            throw new InvalidOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg3") + operand + DBase.resources.getString("CTableMsg43") + f.localizedNameForDataType(f));
                        }
                    }
                }
            }
        }

        //FORMATTED STRING operands
        //variables used for formatted strings
        ArrayList anyCharIndex = new ArrayList();
//        Array anyCharStringIndex = new Array();
        ArrayList anyString = new ArrayList();
//        Array anyStringIndex = new Array();

//        System.out.println("Should get in here");
        if ((f.getDataType().getName().equals(operand.getClass().getName())) && (operand.getClass().getName().equals("java.lang.String"))) {
//            System.out.println("Got in");

            String operandStr = (String) operand;
            /* String operand expression -start
             */
            if (!quotedOperand) {
//                System.out.println("Got in !quotedOperand");
                boolean operandExpression = false;

                char[] c = operandStr.toCharArray();
                int counter = 0;
                while (counter<operandStr.length()) {
                    if (c[counter] == '(' || c[counter] == ')' || c[counter] == '+') {
                        operandExpression = true;
                        break;
                    }
                    if (c[counter++] == '"') {
                        while (c[counter] != '"') counter++;
                        counter++;
                    }
                }
                if (operandExpression) {
//                    System.out.println("Got in operandExpression");
                    StringOperandExpression soe;
                    try{
//                        System.out.println("Creating String operand expression");
                        soe = new StringOperandExpression(operandStr, le.table);
                    }catch (InvalidOperandExpressionException e) {throw new InvalidOperandException(e.message);}
                     catch (InvalidDataFormatException e) {throw new InvalidOperandException(e.message);}

                    try{
//                        System.out.println("Executing String operand expression");
                        operand = soe.execute();
                    }catch (InvalidOperandExpressionException e) {throw new InvalidOperandException(e.message);}
                }
            }
            /* String operand expression -end
             */
            operandStr = (String) operand;
//            System.out.println("operandStr: " + operandStr);
            if (!quotedOperand && ((operandStr.indexOf('^') != -1) || (operandStr.indexOf('*') != -1))) {
//                System.out.println("Got in !quotedOperand &&");

                formattedOperand = true;

                int index=0;
                int lastIndexValue = 0;
                while ((index = operandStr.indexOf('*', index)) != -1) {
//                    if (!(lastIndexValue==0))
//                        anyString.add(operandStr.substring(lastIndexValue, index));
//                    else
                        anyString.add(operandStr.substring(lastIndexValue, index));
//                    anyStringIndex.add(new Integer(lastIndexValue));
                    lastIndexValue = index+1;
                    if (!(index == operandStr.length()-1))
                        index++;
                    else
                        break;
                }
                if (lastIndexValue != 0) {
                    anyString.add(operandStr.substring(lastIndexValue, operandStr.length()));
//                    anyStringIndex.add(new Integer(lastIndexValue));
                }

                index = 0;
                if (anyString.size() == 0) {
                    while ((index = operandStr.indexOf('^', index)) != -1) {
                        anyCharIndex.add(new Integer(index));
                        if (!(index == operandStr.length()-1))
                            index++;
                        else
                            break;
                    }
                }else{
                    for (int i=0; i<anyString.size(); i++) {
                        while ((index = ((String) anyString.get(i)).indexOf('^', index)) != -1) {
                              anyCharIndex.add(new Integer(index));
//                            anyCharStringIndex.add(new Integer(i));
                            if (!(index == operandStr.length()-1))
                                index++;
                            else
                                break;
                        }
                    }
                }
//                System.out.println("Formatted string: " + anyCharIndex + " " + anyString);
//                operand = operandStr;
            }
        }

        if (formattedOperand) {
            if (!Operator.equals("="))
                throw new InvalidOperatorException(DBase.resources.getString("ElementaryLogicalExceptionMsg5") + Operator + DBase.resources.getString("ElementaryLogicalExceptionMsg6"));

//t            if (f.getFieldType().getName().equals("java.util.Date")) {
//t                if (f.isDate()) {
            if (f.getDataType().equals(DateTableField.DATA_TYPE)) {
                predicate = new DateSecondPredicateComparator(new EqualFormattedDate(formattedDay, formattedMonth, formattedYear), (CDate) operand);
            }else{
                predicate = new TimeSecondPredicateComparator(new EqualFormattedTime(formattedHour, formattedMinute), (CTime) operand);
            }

            if (f.getDataType().equals(StringTableField.DATA_TYPE))
                predicate = new StringSecondPredicateComparator(new EqualFormattedString(anyCharIndex, anyString), (String) operand);

            field = f;
            fieldIndex = fldIndex;
            logicExpr = le;
            return;
        }

/*        HashMapIterator validOperatorsIter;
        validOperatorsIter = DBase.operators.find(f.getDataType().getName());
        HashMap validOperators = (HashMap) validOperatorsIter.value();

        if ((validOperators.count(Operator)) == 0) {
            String className = f.getDataType().getName();
            className = className.substring(className.lastIndexOf('.') + 1);
            if (className.equals("CDate"))
                className = "Date";
            else if (className.equals("CTime"))
                className = "Time";
            throw new InvalidOperatorException(DBase.resources.getString("ElementaryLogicalExceptionMsg7") + Operator + DBase.resources.getString("ElementaryLogicalExceptionMsg8") + f.localizedNameForDataType(f) + DBase.resources.getString("ElementaryLogicalExceptionMsg9"));
        }
*/
//        System.out.println("Operand: " + operand + " type: " + operand.getClass());
//        predicate = new BindSecondPredicate((BinaryPredicate) validOperators.find(Operator).value(), operand);

        predicate = AbstractTableField.createSecondOperandOperator(f, Operator, operand);
        if (predicate == null)
            throw new InvalidOperatorException(DBase.resources.getString("ElementaryLogicalExceptionMsg7") + Operator + DBase.resources.getString("ElementaryLogicalExceptionMsg8") + f.localizedNameForDataType(f) + DBase.resources.getString("ElementaryLogicalExceptionMsg9"));

        field = f;
        fieldIndex = fldIndex;
        logicExpr = le;
        if (f.getDataType() == TimeTableField.DATA_TYPE)
            ((TimeTableField) f).printData();
    }

    protected void executeELE(IntBaseArrayDesc selectedSubset,
                              IntBaseArrayDesc unselectedSubset,
                              IntBaseArrayDesc subset,
                              boolean onSelectedSubset,
                              boolean NOTfound) {
//        ArrayList tableData = logicExpr.table.getTableData();
//        ArrayIter i = new ArrayIter(subset, 0, (ArrayList) tableData.get(fieldIndex));
        FieldDataIterator iterator = field.iterator(subset, 0);

//       System.out.println("executeELE Queried subset: " + subset + " onSelectedSubset?: " + onSelectedSubset);
        int countRemoved = 0;
        if (!onSelectedSubset && !NOTfound) {   //OR without NOT
//            System.out.println("!onSelectedSubset && !NOTfound");
//            System.out.println("subset ELE: " + subset + " predicate: " + predicate);
            while ( true ) {
                field.findNext(iterator, predicate);
//System.out.println("iterator: " + iterator.getIndex() + " atEnd: " + iterator.atEnd());
//                i = (ArrayIter) MyFinding.findIf( i, subset.size(), predicate);
                if (iterator.atEnd())
                    break;
//                System.out.println("Index: " + i.index());
//                System.out.println("unselectedSubset: " + logicExpr.table.unselectedSubset);
                selectedSubset.add(subset.get(iterator.getIndex()));
                unselectedSubset.remove(iterator.getIndex()-countRemoved);
                countRemoved++;
                iterator.advance();
            }
//            System.out.println("ELE selectedSubset: " + selectedSubset);
        }else if (!onSelectedSubset && NOTfound) { //NOT OR
            for (int l=0; l<unselectedSubset.size(); l++)
                selectedSubset.add(unselectedSubset.get(l));
            unselectedSubset.clear();
//            System.out.println("Selected: " + selectedSubset);
            IntBaseArrayDesc res = new IntBaseArrayDesc();
            while ( true ) {
///                iterator = field.iterator(iterator.recIndexArray, subset.size());
                field.findNext(iterator, predicate);
//                i = (ArrayIter) MyFinding.findIf( i, subset.size(), predicate);
                if (iterator.atEnd())
                    break;
//                System.out.println("Index: " + i.index());
//                System.out.println("unselectedSubset: " + logicExpr.table.unselectedSubset);
                unselectedSubset.add(subset.get(iterator.getIndex()));
                res.add(subset.get(iterator.getIndex()));
//--                selectedSubset.removeElements(subset.get(i.index())); //remove(subset.at(i.index()));
                iterator.advance();
            }
            ParenthesizedLE.clearArray(selectedSubset, res);
        }else if (onSelectedSubset && !NOTfound) { //AND without NOT
//            System.out.println("onSelectedSubset && !NOTfound");
            int lastIndex = 0;
            selectedSubset.clear();
            while ( true ) {
///                iterator = field.iterator(iterator.recIndexArray, subset.size());
                field.findNext(iterator, predicate);
//                i = (ArrayIter) MyFinding.findIf( i, subset.size(), predicate);
                if (iterator.atEnd())
                    break;
//                System.out.println("Index: " + i.index());
//                System.out.println("unselectedSubset: " + logicExpr.table.unselectedSubset);
                selectedSubset.add(subset.get(iterator.getIndex()));
                for (int k=lastIndex; k<iterator.getIndex(); k++)
                    unselectedSubset.add(subset.get(k));
                lastIndex = iterator.getIndex() +1;
                iterator.advance();
            }
            for (int k = lastIndex; k<subset.size(); k++)
                unselectedSubset.add(subset.get(k));
        }else if (onSelectedSubset && NOTfound) {   //NOT AND
//            int lastIndex = 0;
//            logicExpr.table.selectedSubset.clear();
            IntBaseArrayDesc res = new IntBaseArrayDesc();
            while ( true ) {
///                iterator = field.iterator(iterator.recIndexArray, subset.size());
                field.findNext(iterator, predicate);
//                i = (ArrayIter) MyFinding.findIf( i, subset.size(), predicate);
                if (iterator.atEnd())
                    break;
//                System.out.println("Index: " + i.index());
//                System.out.println("unselectedSubset: " + logicExpr.table.unselectedSubset);
//--                selectedSubset.removeElements(subset.get(i.index()));
                res.add(subset.get(iterator.recIndex));
                unselectedSubset.add(subset.get(iterator.recIndex));
//                for (int k = lastIndex; k<i.index(); k++)
//                    logicExpr.table.unselectedSubset.add(subset.at(k));
//                lastIndex = i.index() +1;
                iterator.advance();
            }
            ParenthesizedLE.clearArray(selectedSubset, res);
//            for (int k = lastIndex; k<subset.size(); k++)
//                logicExpr.table.unselectedSubset.add(subset.at(k));
        }
    }
}