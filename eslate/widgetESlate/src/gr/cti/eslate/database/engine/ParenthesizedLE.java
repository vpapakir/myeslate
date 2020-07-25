package gr.cti.eslate.database.engine;


import com.objectspace.jgl.*;
import java.util.StringTokenizer;

import gr.cti.typeArray.IntBaseArray;

/**
 * @version	2.0, May 01
 */
class ParenthesizedLE extends ELE {
//    Array selectedSubset, unselectedSubset;
    private int nestLevel;
    private String queryString;
    private Array nestedPLEs = new Array();
    private LogicalExpression logExpr;
    private Array logicalOperators = new Array();
    private Array ELEArray = new Array();
    private Array NOTArray = new Array();
    protected IntBaseArrayDesc unselectedSubset = new IntBaseArrayDesc();
    protected IntBaseArrayDesc selectedSubset = new IntBaseArrayDesc();

    protected ParenthesizedLE(LogicalExpression le, String query, int level) throws InvalidLogicalExpressionException {
//        selectedSubset = selSubset;
//        unselectedSubset = unselSubset;
        queryString = query;
        nestLevel = level;
        logExpr = le;

//        System.out.println();
//        System.out.println("PLE got queryString: " + queryString);
        int firstOpenParenIndex = -1;
        int prevOpenParenIndex;
        while (true) {
            prevOpenParenIndex = firstOpenParenIndex;
//            System.out.println("firstOpenParenIndex+1: " + (firstOpenParenIndex+1) + ", queryString: " + queryString + ", queryString.indexOf('(', firstOpenParenIndex+1): " + queryString.indexOf('(', firstOpenParenIndex+1));
            if ((firstOpenParenIndex = queryString.indexOf('(', firstOpenParenIndex+1)) != -1) {
//                System.out.println("While loop... firstOpenParenIndex: " + firstOpenParenIndex);
                /* Check if this open parenthesis is contained within quotes. If it is,
                 * then look for the next one and don't form a new PLE.
                 */
                int numOfQuotes = 0;
                for (int i = prevOpenParenIndex+1; i<firstOpenParenIndex; i++) {
                    if (queryString.charAt(i) == '"')
                        numOfQuotes++;
                }

//                System.out.println("Number of quotes between: " + (prevOpenParenIndex+1) +
//                " and " + (firstOpenParenIndex-1) + " = " + numOfQuotes + ". numOfQuotes%2= " + numOfQuotes%2);
                if (numOfQuotes%2 == 1) { // '(' in double quotes
                    // Move on to the next double quote. Don't check for an '(' between the two double quotes
                    while (queryString.charAt(firstOpenParenIndex) != '"')
                        firstOpenParenIndex++;
                }else{ //if ((numOfQuotes%2) == 0) {  // '(' not in double quotes
                    int parLevel = 1;
                    for (int i=firstOpenParenIndex+1; i<queryString.length(); i++) {
//                        System.out.println("Loop start: " + i + " " + queryString.charAt(i));
                        if (queryString.charAt(i)=='(') {
                            parLevel++;
//                            System.out.println("Found '(' at: " + i + " parLevel= " + parLevel);
                        }
                        /* Omit any part of the queryString which is quoted.
                         */
                        if (queryString.charAt(i) == '"') {
//                            System.out.println("Found double quotes at: " + i);
                            try{
                                i++;
                                while (queryString.charAt(i) != '"') {
//                                    System.out.print(i + ": " + queryString.charAt(i));
                                    i++;
                                }
//                                System.out.println(" --->  " + i + ": " + queryString.charAt(i));
                                i++;
                            }catch (StringIndexOutOfBoundsException e) {throw new InvalidLogicalExpressionException("ff" + CDatabase.resources.getString("ParenthesizedELEMsg1"));}
                        }

                        if (queryString.charAt(i) == ')') {
                            parLevel--;
//                            System.out.println("Found ')' at: " + i + " parLevel= " + parLevel);
                            if (parLevel==0) {
                                /* Check if there is an '|' character within the parenthesized
                                 * portion of the queryString. If there exists at least one '|'
                                 * then this portion is qualified to become a PLE. However if no
                                 * '|' character is found within this substring, then it probably
                                 * is an operand expression which includes parenthesis. In this case
                                 * don't form a PLE, but go on looking for another pair of parenthesis.
                                 */
//                                System.out.println(i);
                                String parenthesizedString = queryString.substring(firstOpenParenIndex+1, i);
//                                System.out.println(i + " " + parenthesizedString);
                                if (parenthesizedString.indexOf('|') != -1) {
//                                  System.out.println("New PLE on: " + queryString.substring(firstOpenParenIndex+1, i));
                                    ParenthesizedLE nestedPLE = new ParenthesizedLE(logExpr, parenthesizedString, level+1);
                                    nestedPLEs.add(nestedPLE);
//                                    System.out.println("Got back with: " + i +"  " + queryString + " firstOpenParenIndex: " + firstOpenParenIndex);
                                    String s1 = "";
                                    if (i+1 < queryString.length())
                                        s1 = queryString.substring(i+1);
                                    String s2 = "";
                                    if (firstOpenParenIndex != 0)
                                        s2 = queryString.substring(0, firstOpenParenIndex-1);
                                    queryString = s2 + " PLE " + s1;
//                                    System.out.println("New query string: " + queryString);
                                    break;
                                }else{
                                    break;
                                }
                            }
                        }
                    }//for
                }// if (numOfQuotes....
            }else
                break;
        }//while

//        System.out.println("Exiting: " + queryString + "   nest level: " + nestLevel);


        int numOfNestedPLEs = nestedPLEs.size();
        int currNestedPLE = 0;
        String operand, Operator, fieldName;
        StringTokenizer st = new StringTokenizer(queryString, "|", false);
//System.out.println("PLE queryString: " + queryString);
        int numOfTokens = st.countTokens(), count = 0;
        while (st.hasMoreTokens()) {
            fieldName = st.nextToken().trim();
            if (fieldName.equals(CDatabase.resources.getString("not")) || fieldName.equals("όχι")) {
                NOTArray.add(new Integer(count));
                if (st.hasMoreTokens())
                    fieldName = st.nextToken().trim();
                else
                    throw new InvalidLogicalExpressionException(DBase.resources.getString("ParenthesizedELEMsg2"));
            }
            if (fieldName.equals("PLE")) {
                ELEArray.add(nestedPLEs.at(currNestedPLE));
                currNestedPLE++;
                if (st.hasMoreTokens()) {
                    String s = st.nextToken();
                    if (s.equals(DBase.resources.getString("not")) || s.equals("όχι"))
                        throw new InvalidLogicalExpressionException(DBase.resources.getString("ParenthesizedELEMsg2"));
                    else
                        logicalOperators.add(s);
                }
//                System.out.println("Found PLE");
                count++;
                continue;
            }

            if (st.hasMoreTokens())
                Operator = st.nextToken().trim();
            else
                throw new InvalidLogicalExpressionException(DBase.resources.getString("ParenthesizedELEMsg2"));

            if (st.hasMoreTokens()) {
                operand = st.nextToken().trim();
                if (operand.length() == 0)
                    throw new InvalidLogicalExpressionException(DBase.resources.getString("ParenthesizedELEMsg3"));
            }else
                throw new InvalidLogicalExpressionException(DBase.resources.getString("ParenthesizedELEMsg2"));

//            System.out.println(fieldName + " " + Operator + " " + operand);
            /* Check if the "operand" contains field names, which by convention are
             * included in square brackets. If yes, then a FieldELE will be constructed.
             * If not an ElementaryLogicalExpression will be constructed.
             * In case the "operand" contains field names, these names are replaced in the
             * "operand" string with the fields' indices, which are still included by
             * square brackets.
             */
            try {
                int firstBracket=0;
                int prevBracket = firstBracket;
                int closingBracket = 0, prevClosingBracket=0;
                int numOfQuotes;
                boolean closingBracketFound;
                String newOperand = "";
                IntBaseArray fieldIndices = new IntBaseArray();

                while ((firstBracket<operand.length()-1) && (firstBracket = operand.indexOf('[', firstBracket)) != -1) {
                    /* Check whether the found '[' is contained between quotes.
                     */
                     numOfQuotes = 0;
                     for (int i=0; i<firstBracket; i++) {
                        if (operand.charAt(i) == '"')
                            numOfQuotes++;
                     }

                     if ((numOfQuotes%2) != 0) {
                        firstBracket++;
                        continue;
                     }else{
                        /* Look for a closing square bracket(']'), not included in quotes.
                         */
                        closingBracketFound = false;
                        int lastFoundClosingBracket = firstBracket+1;
                        while (!closingBracketFound) {
                            try{
                                closingBracket = operand.indexOf(']', lastFoundClosingBracket);
                            }catch (StringIndexOutOfBoundsException e) {break;}
                            if (closingBracket == -1)
                                break;
                            else{
                                /* Check if the closing bracket is included in quotes.
                                 */
                                numOfQuotes = 0;
                                for (int i=firstBracket+1; i<closingBracket; i++) {
                                    if (operand.charAt(i) == '"')
                                    numOfQuotes++;
                                }

                                if ((numOfQuotes%2) != 0) { // If ']' is included in quotes
                                    lastFoundClosingBracket = closingBracket+1;
                                    continue;
                                }else{ //if ']' not included in quotes
                                    closingBracketFound = true;
                                    /* Get the name of the field within the square brackets. If this name
                                     * exists the replce it in the "newOperand" String with the index of
                                     * this field. Otherwise throw an exception.
                                     */
                                    String operandFieldName = operand.substring(firstBracket+1, closingBracket);
                                    try{
                                        int operandFieldIndex = le.table.getFieldIndex(operandFieldName);
                                        fieldIndices.add(operandFieldIndex);
                                        newOperand = newOperand + operand.substring(prevClosingBracket, firstBracket+1) + operandFieldIndex + "]";

                                    }catch (InvalidFieldNameException e) {throw new InvalidLogicalExpressionException(e.message);}
                                }
                            }
                        } //while

                        if (!closingBracketFound)
                            throw new InvalidLogicalExpressionException(DBase.resources.getString("CTableMsg9"));
                        else{
                            prevBracket = firstBracket;
                            firstBracket = closingBracket+1;
                            prevClosingBracket = closingBracket+1;
                        }
                     }
                }

                if (!newOperand.equals("")) { //field names were found in "operand"
                    newOperand = newOperand + operand.substring(prevClosingBracket);
                    System.out.println(newOperand);
                    FieldELE ele = new FieldELE(this.logExpr, fieldName, Operator, newOperand, fieldIndices);
                    ELEArray.add(ele);
                }else{
                    ElementaryLogicalExpression ele = new ElementaryLogicalExpression(this.logExpr, fieldName, Operator, operand);
                    ELEArray.add(ele);
                }
            }catch (InvalidFieldNameException e) {throw new InvalidLogicalExpressionException(e.message);}
             catch (InvalidOperatorException e) {throw new InvalidLogicalExpressionException(e.message);}
             catch (InvalidOperandException e) {e.printStackTrace(); throw new InvalidLogicalExpressionException(e.message);}
             catch (InvalidFieldOperandException e) {throw new InvalidLogicalExpressionException(e.message);}

            if (st.hasMoreTokens()) {
                String s = st.nextToken();
                if (!s.equals(DBase.resources.getString("and")) && !s.equals(DBase.resources.getString("or")) && !s.equals("και") && !s.equals("ή"))
                    throw new InvalidLogicalExpressionException(DBase.resources.getString("ParenthesizedELEMsg2"));
                else
                    logicalOperators.add(s);
            }
            count++;
        }
//        System.out.println(logicalOperators);
    }


    protected IntBaseArrayDesc executePLE(IntBaseArrayDesc workingSubset)  {
        for (int k = 0; k<ELEArray.size(); k++) {

            if (k==0) {
//                System.out.println("Got in with workingSubset: " + workingSubset + ", unselectedSubset: " + unselectedSubset);
                unselectedSubset.copy(workingSubset);
//                System.out.println("Got in with selected: " + selectedSubset);
//                System.out.println("unselectedSubset: " + unselectedSubset);
                ELE e = (ELE) ELEArray.at(k);
//                System.out.println("In PLE: " + e.getClass());
                if (e.getClass().isInstance(this)) { //Got a ParenthesizedLE
//                    System.out.println("Executing PLE");
                    IntBaseArrayDesc result = e.executePLE((IntBaseArrayDesc) workingSubset);

                    for (int i=0; i<result.size(); i++) {
                        selectedSubset.add(result.get(i));
//--                        unselectedSubset.removeElements(result.get(i));
                    }
                    clearArray(unselectedSubset, result);
                    if (NOTArray.contains(new Integer(k))) {
                        selectedSubset.swap(unselectedSubset);
                    }
//                    System.out.println("Got OUT with selected: " + selectedSubset + "  unselected: " + unselectedSubset);
                }else{
                    ((ELE) ELEArray.at(k)).executeELE(selectedSubset, unselectedSubset, (IntBaseArrayDesc) unselectedSubset.clone(), false, false);
                    if (NOTArray.contains(new Integer(k))) {
                        selectedSubset.swap(unselectedSubset);
                    }
                }
            }else if (((String) logicalOperators.at(k-1)).equals(DBase.resources.getString("and")) || ((String) logicalOperators.at(k-1)).equals("και")) {
                ELE e = (ELE) ELEArray.at(k);
                if (e.getClass().isInstance(this)) { //Got a ParenthesizedLE
                    IntBaseArrayDesc result = e.executePLE((IntBaseArrayDesc) selectedSubset.clone());
                    if (NOTArray.contains(new Integer(k))) {
//                      for (int i=0; i<selectedSubset.size(); i++)
//                            unselectedSubset.add(selectedSubset.at(i));
//                        selectedSubset.clear();
                        for (int i=0; i<result.size(); i++) {
//--                            selectedSubset.removeElements(result.get(i));
                            unselectedSubset.add(result.get(i));
                        }
                        clearArray(selectedSubset, result);
                    }else{
                      for (int i=0; i<selectedSubset.size(); i++)
                            unselectedSubset.add(selectedSubset.get(i));
                        selectedSubset.clear();
                        for (int i=0; i<result.size(); i++) {
                            selectedSubset.add(result.get(i));
//--                            unselectedSubset.removeElements(result.get(i));
                        }
                        clearArray(unselectedSubset, result);
                    }
//                    if (NOTArray.contains(new Integer(k)))
//                        selectedSubset.swap(unselectedSubset);
//                    System.out.println("AND! Got OUT with selected: " + selectedSubset + "  unselected: " + unselectedSubset);
                }else
                    ((ELE) ELEArray.at(k)).executeELE(selectedSubset, unselectedSubset, (IntBaseArrayDesc) selectedSubset.clone(), true, NOTArray.contains(new Integer(k)));
            }else if (((String) logicalOperators.at(k-1)).equals(DBase.resources.getString("or")) || ((String) logicalOperators.at(k-1)).equals("ή")) {
                ELE e = (ELE) ELEArray.at(k);
                if (e.getClass().isInstance(this)) { //Got a ParenthesizedLE
                    IntBaseArrayDesc result = e.executePLE((IntBaseArrayDesc) unselectedSubset.clone());
                    if (NOTArray.contains(new Integer(k))) {
                        for (int i=0; i<unselectedSubset.size(); i++)
                            selectedSubset.add(unselectedSubset.get(i));
                        unselectedSubset.clear();
                        for (int i=0; i<result.size(); i++) {
//--                            selectedSubset.removeElements(result.get(i));
                            unselectedSubset.add(result.get(i));
                        }
                        clearArray(selectedSubset, result);
                    }else{
                        for (int i=0; i<result.size(); i++) {
                            selectedSubset.add(result.get(i));
//--                            unselectedSubset.removeElements(result.get(i));
                        }
                        clearArray(unselectedSubset, result);
                    }
//                    if (NOTArray.contains(new Integer(k)))
//                        selectedSubset.swap(unselectedSubset);
//                    System.out.println("OR! Got OUT with selected: " + selectedSubset + "  unselected: " + unselectedSubset);
                }else
                    ((ELE) ELEArray.at(k)).executeELE(selectedSubset, unselectedSubset, (IntBaseArrayDesc) unselectedSubset.clone(), false, NOTArray.contains(new Integer(k)));
            }
        }
//            System.out.println("PLE Exiting....Selected subset: " + selectedSubset);
//            System.out.println("PLE Exiting....UnSelected subset: " + unselectedSubset);
        return selectedSubset;
    }

    /* This method accepts two IntBaseArrayDescs. It removes all the elements of
     * the second array from the first array, if they exist there.
     */
    public static void clearArray(IntBaseArrayDesc array, IntBaseArrayDesc elemArray) {
        //array.removeElements(elemArray.get(i));
        array.sort(true);
        elemArray.sort(true);
        int k = 0;
        int elemArraySize = elemArray.size();
        for (int i=0; i<array.size() && k<elemArraySize; i++) {
            if (array.get(i) < elemArray.get(k))
                continue;
            if (array.get(i) == elemArray.get(k)) {
                array.remove(i);
                k++;
                i--;
                continue;
            }
            break;
        }
    }
}