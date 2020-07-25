package gr.cti.eslate.database.engine;


import gr.cti.typeArray.IntBaseArray;
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

/**
 * @version	2.0, May 01
 */
public class FieldELE extends ELE {
    private AbstractTableField firstField;
    private ObjectComparator operator;
    private int firstFieldIndex;
    private AbstractTableField secondField;
    private int secondFieldIndex;
    private LogicalExpression logicExpr;
    private boolean singleFieldOperand = false;
    private OperandExpression oe = null;

    protected FieldELE(LogicalExpression le, String fieldName, String operatorStr, String operand, IntBaseArray fieldIndices)
    throws InvalidFieldNameException, InvalidOperatorException, InvalidFieldOperandException {

        if (operand == null)
            throw new InvalidFieldOperandException(DBase.resources.getString("ElementaryLogicalExceptionMsg1") + operand);

        int fldIndex = 0;
        try {
            fldIndex = le.table.getFieldIndex(fieldName);
        }catch (InvalidFieldNameException e) {throw new InvalidFieldNameException(DBase.resources.getString("CDatabaseMsg74") + fieldName + DBase.resources.getString("CImageIconMsg2"));}

        AbstractTableField f = (AbstractTableField) le.table.tableFields.get(fldIndex);

        /* If the operand contains just one field name in brackets.
         */
        AbstractTableField f1 = null;
        int secFldIndex = -1;
        if (operand.charAt(0) == '[' && operand.charAt(operand.length()-1) == ']') {
            try {
                secFldIndex = new Integer(operand.substring(1, operand.length()-1)).intValue();
            }catch (NumberFormatException e) {secFldIndex=-1;}
        }

        if (secFldIndex != -1) { // Operand contains a single field number

            singleFieldOperand = true;
            f1 = (AbstractTableField) le.table.tableFields.get(secFldIndex);

            //Check if the field types are the same
            if (!f.getDataType().getName().equals(f1.getDataType().getName())) {
                if (!f.getDataType().getName().equals(URLTableField.DATA_TYPE))
                    throw new InvalidFieldOperandException(DBase.resources.getString("CDatabaseMsg84") + fieldName + DBase.resources.getString("CDatabaseMsg60") + f1.getName() + DBase.resources.getString("CDatabaseMsg85"));
                else{
                    if ((!f1.getDataType().equals(StringTableField.DATA_TYPE)) &&
                            (!f1.getDataType().equals(URLTableField.DATA_TYPE)))
                        throw new InvalidFieldOperandException(DBase.resources.getString("FieldELEMsg1") + fieldName + DBase.resources.getString("FieldELEMsg2"));
                }
/*t            }else{
                if (f.getFieldType().getName().equals("java.util.Date")) {
                    if (!f.isDate() == f1.isDate())
                        throw new InvalidFieldOperandException(DBase.resources.getString("CDatabaseMsg84") + fieldName + DBase.resources.getString("CDatabaseMsg60") + f1.getName() + DBase.resources.getString("CDatabaseMsg85"));
                }
t*/
            }

            secondField = f1;
            secondFieldIndex = secFldIndex;
        }else{
            /* "operand" contains an expression on one or more fields.
             */

            /* Check the data types of the fields.
             */
            // Integer or Double or Float
            if (f.getDataType().equals(IntegerTableField.DATA_TYPE) || f.getDataType().equals(DoubleTableField.DATA_TYPE) ||
                    f.getDataType().equals(FloatTableField.DATA_TYPE)) {
                String dataType;
                for (int i=0; i<fieldIndices.size(); i++) {
                    dataType = ((AbstractTableField) le.table.tableFields.get(fieldIndices.get(i))).getDataType().getName();
                    if (!dataType.equals(IntegerTableField.DATA_TYPE) && !dataType.equals(DoubleTableField.DATA_TYPE) &&
                            !dataType.equals(FloatTableField.DATA_TYPE))
                        throw new InvalidFieldOperandException(DBase.resources.getString("FieldELEMsg3") + fieldName + "\". " + DBase.resources.getString("CDatabaseMsg74") + ((AbstractTableField) le.table.tableFields.get(fieldIndices.get(i))).getName() + DBase.resources.getString("CTableMsg14") + f.localizedNameForDataType((AbstractTableField) le.table.tableFields.get(fieldIndices.get(i))) + DBase.resources.getString("CTableMsg15"));
                }
                try{
                    oe = new NumericOperandExpression(operand, le.table);
//                    System.out.println("Creating NumericOperandExpression....");
                }catch (InvalidDataFormatException e) {throw new InvalidFieldOperandException(e.message);}
                 catch (InvalidOperandExpressionException e) {throw new InvalidFieldOperandException(e.message);}
            // Boolean
            }else if (f.getDataType().equals(BooleanTableField.DATA_TYPE)) {
                throw new InvalidFieldOperandException(DBase.resources.getString("CTableMsg23") + f.getLocalizedDataTypeName() + DBase.resources.getString("FieldELEMsg4"));

/*                String dataType;
                for (int i=0; i<fieldIndices.size(); i++) {
                    dataType = ((TableField) le.table.getFields().at(((Integer) fieldIndices.at(i)).intValue())).getFieldType().getName();
                    if (!dataType.equals("java.lang.Boolean"))
                        throw new InvalidFieldOperandException("Only Fields of type \"Boolean\" can be used to query field \"" + fieldName + "\". Field \"" + ((TableField) le.table.getFields().at(((Integer) fieldIndices.at(i)).intValue())).getName() + "\" has \"" + dataType.substring(dataType.lastIndexOf('.')+1) + "\" type");
                }
*/
            // String
            }else if (f.getDataType().getName().equals(StringTableField.DATA_TYPE)) {
                String dataType;
                for (int i=0; i<fieldIndices.size(); i++) {
                    dataType = ((AbstractTableField) le.table.tableFields.get(fieldIndices.get(i))).getDataType().getName();
                    if (dataType.equals(ImageTableField.DATA_TYPE) || dataType.equals("Sound data type ???"))
                        throw new InvalidFieldOperandException(DBase.resources.getString("FieldELEMsg5") + fieldName + "\"." + DBase.resources.getString("CDatabaseMsg74") + ((AbstractTableField) le.table.tableFields.get(fieldIndices.get(i))).getName() + DBase.resources.getString("CTableMsg14") + f.localizedNameForDataType((AbstractTableField) le.table.tableFields.get(fieldIndices.get(i))) + DBase.resources.getString("CTableMsg15"));
                }
                try {
                    oe = new StringOperandExpression(operand, le.table);
                }catch (InvalidDataFormatException e) {throw new InvalidFieldOperandException(e.message);}
                 catch (InvalidOperandExpressionException e) {throw new InvalidFieldOperandException(e.message);}
            // Date or Time
//            }else if (f.getFieldType().getName().equals("java.util.Date")) {
            }else if (f.getDataType().equals(DateTableField.DATA_TYPE) || f.getDataType().equals(TimeTableField.DATA_TYPE)) {
                String dataType;
                for (int i=0; i<fieldIndices.size(); i++) {
                    AbstractTableField fld1 = (AbstractTableField) le.table.tableFields.get(fieldIndices.get(i));
                    dataType = fld1.getDataType().getName();
                    if (!dataType.equals(IntegerTableField.DATA_TYPE) && !dataType.equals(DoubleTableField.DATA_TYPE) &&
                            !dataType.equals(DateTableField.DATA_TYPE) && !dataType.equals(FloatTableField.DATA_TYPE)) {
/*/                        String dataType2 = dataType.substring(dataType.lastIndexOf('.')+1);
                        if (dataType2.equals("Date")) {
                            if (!fld1.isDate())
                                dataType2 = "Time";
                        }
*/
//                        System.out.println(fld1.getFieldType() + ", " + fld1.getName());
//t                        if (f.isDate())
                        if (f.getDataType().equals(DateTableField.DATA_TYPE))
                            throw new InvalidFieldOperandException(DBase.resources.getString("FieldELEMsg6") + fieldName + "\"." + DBase.resources.getString("CDatabaseMsg74") + ((AbstractTableField) le.table.tableFields.get(fieldIndices.get(i))).getName() + DBase.resources.getString("CTableMsg14") + fld1.localizedNameForDataType(fld1) + DBase.resources.getString("CTableMsg15"));
                        else
                            throw new InvalidFieldOperandException(DBase.resources.getString("FieldELEMsg7") + fieldName + "\"." +  DBase.resources.getString("CDatabaseMsg74") + ((AbstractTableField) le.table.tableFields.get(fieldIndices.get(i))).getName() + DBase.resources.getString("CTableMsg14") + fld1.localizedNameForDataType(fld1) + DBase.resources.getString("CTableMsg15"));
                    }
                }
                try{
//t                    if (f.isDate())
                    if (f.getDataType().equals(DateTableField.DATA_TYPE))
                        oe = new DateOperandExpression(operand, operand, true, le.table, 1);
                    else
                        oe = new DateOperandExpression(operand, operand, false, le.table, 1);
                }catch (InvalidDataFormatException e) {throw new InvalidFieldOperandException(e.message);}
                 catch (IllegalDateOperandExpression e) {throw new InvalidFieldOperandException(e.message);}
                 catch (InvalidOperandExpressionException e) {throw new InvalidFieldOperandException(e.message);}

            // URL
            }else if (f.getDataType().equals(URLTableField.DATA_TYPE)) {
                throw new InvalidFieldOperandException(DBase.resources.getString("CTableMsg23") + f.getLocalizedDataTypeName() + DBase.resources.getString("FieldELEMsg4"));

/*                String dataType;
                for (int i=0; i<fieldIndices.size(); i++) {
                    dataType = ((TableField) le.table.getFields().at(((Integer) fieldIndices.at(i)).intValue())).getFieldType().getName();
                    if (!dataType.equals("java.net.URL") && !dataType.equals("java.lang.String"))
                            throw new InvalidFieldOperandException("Only Fields of type \"URL\" or \"String\" can be used to query field \"" + fieldName + "\". Field \"" + ((TableField) le.table.getFields().at(((Integer) fieldIndices.at(i)).intValue())).getName() + "\" has \"" + dataType.substring(dataType.lastIndexOf('.')+1) + "\" type");
                }
*/
            // any other
            }else{
                throw new InvalidFieldOperandException(DBase.resources.getString("CTableMsg23") + f.getLocalizedDataTypeName() + DBase.resources.getString("FieldELEMsg4"));
            }
        }

/*        HashMapIterator operatorsForThisDataType;
        operatorsForThisDataType = DBase.operators.find(f.getDataType().getName());
        HashMap validOperators = (HashMap) operatorsForThisDataType.value();

        if ((validOperators.count(operatorStr)) == 0) {
            String className = f.getDataType().getName();
            if (className.equals("CDate"))
                className = "Date";
            else if (className.equals("CTime"))
                className = "Time";
//            className = className.substring(className.lastIndexOf('.') + 1);
            throw new InvalidOperatorException(DBase.resources.getString("CDatabaseMsg79") + operatorStr + DBase.resources.getString("CDatabaseMsg80") + f.localizedNameForDataType(f) + DBase.resources.getString("CDatabaseMsg81"));
        }
*/
//        comparator = (BinaryPredicate) validOperators.find(operatorStr).value();
        operator = f.getComparatorFor(operatorStr);
        if (operator == null)
            throw new InvalidOperatorException(DBase.resources.getString("CDatabaseMsg79") + operatorStr + DBase.resources.getString("CDatabaseMsg80") + f.getLocalizedDataTypeName() + DBase.resources.getString("CDatabaseMsg81"));
        firstField = f;
        firstFieldIndex = fldIndex;
        logicExpr = le;
    }



    protected void executeELE(IntBaseArrayDesc selectedSubset,
                              IntBaseArrayDesc unselectedSubset,
                              IntBaseArrayDesc subset,
                              boolean onSelectedSubset,
                              boolean NOTfound) {

//        ArrayList tableData = logicExpr.table.getTableData();
//        ArrayList firstFieldData = (ArrayList) tableData.get(firstFieldIndex);
        Object o1, o2;

        if (singleFieldOperand) {
//            System.out.println("secondF: " + secondFieldIndex);
//            ArrayList secondFieldData = (ArrayList) tableData.get(secondFieldIndex);
//            tableData=null;

            System.out.println("ELE Queried subset: " + subset + " onSelectedSubset?: " + onSelectedSubset);
            int countRemoved = 0;
            if (!onSelectedSubset && !NOTfound) {   //OR without NOT
                IntBaseArrayDesc res = new IntBaseArrayDesc();
                for (int i=0; i<subset.size(); i++) {
                    int recIndex = subset.get(i);
                    if (firstField.compareTo(recIndex, operator, secondField, recIndex)) {
                        selectedSubset.add(recIndex);
                        res.add(recIndex);
                    }
//                System.out.println(firstFieldData.at(((Integer) subset.at(i)).intValue()) + "  " + secondFieldData.at(((Integer) subset.at(i)).intValue()));
/*                    o1 = firstFieldData.get(subset.get(i)); //(((Integer) subset.at(i)).intValue());
                    o2 = secondFieldData.get(subset.get(i)); //(((Integer) subset.at(i)).intValue());
                    if (o1 == null || o2 == null) {
                        if (o1==o2) {
                            selectedSubset.add(subset.get(i));
//-                            unselectedSubset.removeElements(subset.get(i)); //?
                            res.add(subset.get(i));
                        }
                    }else if (comparator.execute(o1, o2)) {
                        selectedSubset.add(subset.get(i));
//--                        unselectedSubset.removeElements(subset.get(i)); //?
                        res.add(subset.get(i));
                    }
*/
                }
                ParenthesizedLE.clearArray(unselectedSubset, res);
            }else if (!onSelectedSubset && NOTfound) { //NOT OR
                for (int l=0; l<unselectedSubset.size(); l++)
                    selectedSubset.add(unselectedSubset.get(l));
                unselectedSubset.clear();
//                System.out.println("Selected: " + selectedSubset);
                IntBaseArrayDesc res = new IntBaseArrayDesc();
                for (int i=0; i<subset.size(); i++) {
                    int recIndex = subset.get(i);
                    if (firstField.compareTo(recIndex, operator, secondField, recIndex)) {
                        unselectedSubset.add(recIndex);
                        res.add(recIndex);
                    }
/*                    o1 = firstFieldData.get(subset.get(i)); //((Integer) subset.at(i)).intValue());
                    o2 = secondFieldData.get(subset.get(i)); //((Integer) subset.get(i)).intValue());
                    if (o1 == null || o2 == null) {
                        if (o1==o2) {
                            unselectedSubset.add(subset.get(i));
//--                            selectedSubset.removeElements(subset.get(i)); //?
                            res.add(subset.get(i));
                        }
                    }else if (comparator.execute(o1, o2)) {
                        unselectedSubset.add(subset.get(i));
//--                        selectedSubset.removeElements(subset.get(i)); //?
                        res.add(subset.get(i));
                    }
*/
                }
                ParenthesizedLE.clearArray(selectedSubset, res);
            }else if (onSelectedSubset && !NOTfound) { //AND without NOT
                int lastIndex = 0;
                selectedSubset.clear();
                for (int i=0; i<subset.size(); i++) {
                    int recIndex = subset.get(i);
                    if (firstField.compareTo(recIndex, operator, secondField, recIndex)) {
                        selectedSubset.add(recIndex);
                        for (int k = lastIndex; k<i; k++)
                            unselectedSubset.add(subset.get(k));
                        lastIndex = i+1;
                    }
/*                    o1 = firstFieldData.get(subset.get(i)); //((Integer) subset.at(i)).intValue());
                    o2 = secondFieldData.get(subset.get(i));
                    if (o1 == null || o2 == null) {
                        if (o1==o2) {
                            selectedSubset.add(subset.get(i));
                            for (int k = lastIndex; k<i; k++)
                                unselectedSubset.add(subset.get(k));
                            lastIndex = i+1;
                        }
                    }else if (comparator.execute(o1, o2)) {
                        selectedSubset.add(subset.get(i));
                        for (int k = lastIndex; k<i; k++)
                            unselectedSubset.add(subset.get(k));
                        lastIndex = i+1;
                    }
*/
                }
                for (int k = lastIndex; k<subset.size(); k++)
                    unselectedSubset.add(subset.get(k));
            }else if (onSelectedSubset && NOTfound) {   //NOT AND
                IntBaseArrayDesc res = new IntBaseArrayDesc();
                for (int i=0; i<subset.size(); i++) {
                    int recIndex = subset.get(i);
                    if (firstField.compareTo(recIndex, operator, secondField, recIndex)) {
                        unselectedSubset.add(recIndex);
                        res.add(recIndex);
                    }
/*                    o1 = firstFieldData.get(subset.get(i)); //((Integer) subset.at(i)).intValue());
                    o2 = secondFieldData.get(subset.get(i)); //((Integer) subset.at(i)).intValue());
                    if (o1 == null || o2 == null) {
                        if (o1==o2) {
//--                            selectedSubset.removeElements(subset.get(i)); //?
                            res.add(subset.get(i));
                            unselectedSubset.add(subset.get(i));
                        }
                    }else if (comparator.execute(o1, o2)) {
//--                        selectedSubset.removeElements(subset.get(i)); //?
                        res.add(subset.get(i));
                        unselectedSubset.add(subset.get(i));
                    }
*/
                }
                ParenthesizedLE.clearArray(selectedSubset, res);
            }
        }else{
//            tableData=null;
//            System.out.println("Queried subset: " + subset + " onSelectedSubset?: " + onSelectedSubset);
            int countRemoved = 0;
            if (!onSelectedSubset && !NOTfound) {   //OR without NOT
                IntBaseArrayDesc res = new IntBaseArrayDesc();
                for (int i=0; i<subset.size(); i++) {
//                System.out.println(firstFieldData.at(((Integer) subset.at(i)).intValue()) + "  " + secondFieldData.at(((Integer) subset.at(i)).intValue()));
///                    o1 = firstFieldData.get(subset.get(i)); //((Integer) subset.at(i)).intValue());
//                    System.out.println(oe);
                    int recIndex = subset.get(i);
                    o2 = oe.execute(recIndex, false); //((Integer) subset.at(i)).intValue(), false);
                    if (firstField.compareTo(recIndex, operator, o2)) {
                        selectedSubset.add(recIndex);
                        res.add(recIndex);
                    }
/*                    if (o1!=null && o2!=null && comparator.execute(o1, o2)) { //    secondFieldData.at(((Integer) subset.at(i)).intValue()))) {
                        selectedSubset.add(subset.get(i));
//--                        unselectedSubset.removeElements(subset.get(i)); //?
                        res.add(subset.get(i));
                     }
*/
                }
                ParenthesizedLE.clearArray(unselectedSubset, res);
            }else if (!onSelectedSubset && NOTfound) { //NOT OR
                for (int l=0; l<unselectedSubset.size(); l++)
                    selectedSubset.add(unselectedSubset.get(l));
                unselectedSubset.clear();
//                System.out.println("Selected: " + selectedSubset);
                IntBaseArrayDesc res = new IntBaseArrayDesc();
                for (int i=0; i<subset.size(); i++) {
///                    o1 = firstFieldData.get(subset.get(i)); //((Integer) subset.at(i)).intValue());
                    int recIndex = subset.get(i);
                    o2 = oe.execute(recIndex, false); //((Integer) subset.at(i)).intValue(), false);
                    if (firstField.compareTo(recIndex, operator, o2)) {
                        unselectedSubset.add(recIndex);
                        res.add(recIndex);
                    }
/*                    if (o1!=null && o2!=null && comparator.execute(o1, o2)) { //    secondFieldData.at(((Integer) subset.at(i)).intValue()))) {
                        unselectedSubset.add(subset.get(i));
//--                        selectedSubset.removeElements(subset.get(i)); //?
                        res.add(subset.get(i));
                    }
*/
                }
                ParenthesizedLE.clearArray(selectedSubset, res);
            }else if (onSelectedSubset && !NOTfound) { //AND without NOT
                int lastIndex = 0;
                selectedSubset.clear();
                for (int i=0; i<subset.size(); i++) {
///                    o1 = firstFieldData.get(subset.get(i)); //((Integer) subset.at(i)).intValue());
                    int recIndex = subset.get(i);
                    o2 = oe.execute(recIndex, false); //((Integer) subset.at(i)).intValue(), false);
                    if (firstField.compareTo(recIndex, operator, o2)) {
                        selectedSubset.add(recIndex);
                        for (int k = lastIndex; k<i; k++)
                            unselectedSubset.add(subset.get(k));
                        lastIndex = i+1;
                    }
/*                    if (o1!=null && o2!=null && comparator.execute(o1, o2)) { //    secondFieldData.at(((Integer) subset.at(i)).intValue()))) {
                        selectedSubset.add(subset.get(i));
                        for (int k = lastIndex; k<i; k++)
                            unselectedSubset.add(subset.get(k));
                        lastIndex = i+1;
                    }
*/
                }
                for (int k = lastIndex; k<subset.size(); k++)
                    unselectedSubset.add(subset.get(k));
            }else if (onSelectedSubset && NOTfound) {   //NOT AND
                IntBaseArrayDesc res = new IntBaseArrayDesc();
                for (int i=0; i<subset.size(); i++) {
///                    o1 = firstFieldData.get(subset.get(i)); //((Integer) subset.at(i)).intValue());
                    int recIndex = subset.get(i);
                    o2 = oe.execute(recIndex, false); //((Integer) subset.at(i)).intValue(), false);
                    if (firstField.compareTo(recIndex, operator, o2)) {
                        unselectedSubset.add(recIndex);
                        res.add(recIndex);
                    }
/*                    if (o1!=null && o2!=null && comparator.execute(o1, o2)) { //    secondFieldData.at(((Integer) subset.at(i)).intValue()))) {
//--                        selectedSubset.removeElements(subset.get(i)); //?
                        res.add(subset.get(i));
                        unselectedSubset.add(subset.get(i));
                    }
*/
                }
                ParenthesizedLE.clearArray(selectedSubset, res);
            }
        }
    }
}