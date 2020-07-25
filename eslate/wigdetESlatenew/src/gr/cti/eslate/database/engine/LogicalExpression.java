package gr.cti.eslate.database.engine;


import gr.cti.eslate.tableModel.event.*;
import gr.cti.typeArray.IntBaseArray;
import com.objectspace.jgl.*;
import java.lang.StringBuffer;
import java.util.StringTokenizer;


/**
 * @version	2.0, May 01
 */
public class LogicalExpression {
//    protected HashMap operators = new HashMap();
    protected Table table;
    public final static int NEW_SELECTION = 0;
    public final static int ADD_TO_SELECTION = 1;
    public final static int REMOVE_FROM_SELECTION = 2;
    public final static int SELECT_FROM_SELECTION = 3;
    protected IntBaseArrayDesc queryResults;

    public LogicalExpression(Table tab, String queryString, int selectionMode, boolean alterTableSelection) throws InvalidLogicalExpressionException {
        if (selectionMode < 0 || selectionMode > 3)
            throw new InvalidLogicalExpressionException(DBase.resources.getString("LogicalExpressionMsg1"));

        table = tab;
        String queryStringBackup = queryString;
//        System.out.println("Table: " + table.getTitle() + ", selectionMode: " + selectionMode + ", alterTableSelection : " + alterTableSelection + ", queryStringBackup: " + queryStringBackup);
/*
        HashMap h = new HashMap();
//Integer
        try{
            h.add("<", new LessNumber(java.lang.Class.forName("java.lang.Integer")));
            h.add("<=", new LessEqualNumber(java.lang.Class.forName("java.lang.Integer")));
            h.add(">", new GreaterNumber(java.lang.Class.forName("java.lang.Integer")));
            h.add(">=", new GreaterEqualNumber(java.lang.Class.forName("java.lang.Integer")));
            h.add("!=", new NotEqualNumber(java.lang.Class.forName("java.lang.Integer")));
            h.add("=", new EqualNumber(java.lang.Class.forName("java.lang.Integer")));
        }catch (ClassNotFoundException e) {}
        operators.add("java.lang.Integer", h);
//Double
        h = new HashMap();
        try{
            h.add("<", new LessNumber(java.lang.Class.forName("java.lang.Double")));
            h.add("<=", new LessEqualNumber(java.lang.Class.forName("java.lang.Double")));
            h.add(">", new GreaterNumber(java.lang.Class.forName("java.lang.Double")));
            h.add(">=", new GreaterEqualNumber(java.lang.Class.forName("java.lang.Double")));
            h.add("!=", new NotEqualNumber(java.lang.Class.forName("java.lang.Double")));
            h.add("=", new EqualNumber(java.lang.Class.forName("java.lang.Double")));
        }catch (ClassNotFoundException e) {}
        operators.add("java.lang.Double", h);
//String
        h = new HashMap();
        h.add("=", new EqualString());
        h.add("<=", new LessEqualString());
        h.add("<", new LessString());
        h.add(">", new GreaterString());
        h.add(">=", new GreaterEqualString());
        h.add("!=", new NotEqualString());
        h.add("contained", new ContainedInString());
        h.add("contains", new ContainsString());
        operators.add("java.lang.String", h);
//Boolean
        h = new HashMap();
        h.add("=", new EqualString());
        h.add("!=", new NotEqualString());
        operators.add("java.lang.Boolean", h);
//URL
        h = new HashMap();
        h.add("contains", new ContainsString());
        h.add("contained", new ContainedInString());
        operators.add("java.net.URL", h);
//Date & Time
        h = new HashMap();
        h.add("=", new EqualDate());
        h.add("!=", new NotEqualDate());
        h.add("<", new LessDate());
        h.add("<=", new LessEqualDate());
        h.add(">", new GreaterDate());
        h.add(">=", new GreaterEqualDate());
        operators.add("java.util.Date", h);
*/
        IntBaseArrayDesc unselectedSubsetBck = null;
        IntBaseArrayDesc selectedSubsetBck = null;
        selectedSubsetBck = (IntBaseArrayDesc) table.selectedSubset.clone();
        if (!alterTableSelection) {
            unselectedSubsetBck = (IntBaseArrayDesc) table.unselectedSubset.clone();
        }

        IntBaseArrayDesc unselectedSubsetBefore = null;
        if (selectionMode == NEW_SELECTION) {
            table.selectedSubset.clear();
            table.unselectedSubset.clear();
            for (int k=0; k<table.getRecordCount(); k++)
                table.unselectedSubset.add(k); //new Integer(k));
        }else if (selectionMode == ADD_TO_SELECTION) {
        }else if (selectionMode == REMOVE_FROM_SELECTION || selectionMode == SELECT_FROM_SELECTION) {
            unselectedSubsetBefore = table.unselectedSubset;
            table.unselectedSubset = (IntBaseArrayDesc) table.selectedSubset.clone();
        }

        queryString = formatQueryString(queryString);

        ParenthesizedLE ple = new ParenthesizedLE(this, queryString, 0);
//        System.out.println("Logical expression 0: " + System.currentTimeMillis());
        IntBaseArrayDesc selectedSet = ple.executePLE(table.unselectedSubset);

        if (selectionMode == NEW_SELECTION) {
            queryResults = selectedSet;
            if (alterTableSelection) {
                table.selectedSubset = selectedSet;
                table.selectedSubset.sort(true);
//                System.out.println("Altering table selection 1 selected: " + table.selectedSubset);
                table.unselectedSubset.clear();
                int count = 0;
                int k = 0;
                int recCount = table.getRecordCount();
                for (k=0; k<recCount; k++) {
                    if ((k-count) >= table.selectedSubset.size()) {
                        break;
                    }
                    if  (table.selectedSubset.get(k-count) != k) {
//                        System.out.println("k: " + k);
                        table.unselectedSubset.add(k);
                        count++;
                    }
                }
                if (k != recCount) {
                    for (int j=k; j<recCount; j++) {
                        table.unselectedSubset.add(j);
//                        System.out.println("j: " + j);
                    }
                }
//                System.out.println("Altering table selection 1 unselected: " + table.unselectedSubset);

//                for (int k=0; k<table.selectedSubset.size(); k++)
//                    table.unselectedSubset.removeElements(table.selectedSubset.get(k));
//                ParenthesizedLE.clearArray(table.unselectedSubset, selectedSet);
            }else{
                table.selectedSubset = selectedSubsetBck;
                table.unselectedSubset = unselectedSubsetBck;
//                System.out.println("table.selectedSubset: " + table.selectedSubset.size());
//                System.out.println("table.unselectedSubset: " + table.unselectedSubset.size());
            }
        }else if (selectionMode == ADD_TO_SELECTION) {
            if (alterTableSelection) {
//--                for (int k=0; k<selectedSet.size(); k++)
//--                    table.unselectedSubset.removeElements(selectedSet.get(k));
                ParenthesizedLE.clearArray(table.unselectedSubset, selectedSet);
                for (int k=0; k<selectedSet.size(); k++)
                    table.selectedSubset.add(selectedSet.get(k));
                queryResults = table.selectedSubset;
            }else{
                queryResults = selectedSet;
                for (int k=0; k<table.selectedSubset.size(); k++)
                    queryResults.add(table.selectedSubset.get(k));

                table.selectedSubset = selectedSubsetBck;
                table.unselectedSubset = unselectedSubsetBck;
//                System.out.println("table.selectedSubset: " + table.selectedSubset.size());
//                System.out.println("table.unselectedSubset: " + table.unselectedSubset.size());
            }
        }else if (selectionMode == REMOVE_FROM_SELECTION) {
            if (alterTableSelection) {
                table.unselectedSubset = unselectedSubsetBefore;
                for (int k=0; k<selectedSet.size(); k++) {
//--                    table.selectedSubset.removeElements(selectedSet.get(k));
                    table.unselectedSubset.add(selectedSet.get(k));
                }
                ParenthesizedLE.clearArray(table.selectedSubset, selectedSet);
                queryResults = table.selectedSubset;
            }else{
                queryResults = (IntBaseArrayDesc) table.selectedSubset.clone();
//--                for (int k=0; k<selectedSet.size(); k++) {
//--                    queryResults.removeElements(selectedSet.get(k));
//--                }
                ParenthesizedLE.clearArray(queryResults, selectedSet);
                table.selectedSubset = selectedSubsetBck;
                table.unselectedSubset = unselectedSubsetBck;
//                System.out.println("table.selectedSubset: " + table.selectedSubset.size());
//                System.out.println("table.unselectedSubset: " + table.unselectedSubset.size());
            }
        }else if (selectionMode == SELECT_FROM_SELECTION) {
            if (alterTableSelection) {
                table.unselectedSubset = unselectedSubsetBefore;
                for (int k=0; k<selectedSet.size(); k++) {
                    if (table.selectedSubset.indexOf(selectedSet.get(k)) == -1) {
                        table.unselectedSubset.add(table.selectedSubset.get(k));
//                        table.selectedSubset.remove(k);
                    }
                }
                table.selectedSubset = selectedSet;
                queryResults = table.selectedSubset;
            }else{
                queryResults = selectedSet;
                table.selectedSubset = selectedSubsetBck;
                table.unselectedSubset = unselectedSubsetBck;
//                System.out.println("table.selectedSubset: " + table.selectedSubset.size());
//                System.out.println("table.unselectedSubset: " + table.unselectedSubset.size());
            }
        }

//        System.out.println("Selected: "  + table.selectedSubset);
//        System.out.println("Unselected: "  + table.unselectedSubset);

//        System.out.println("Logical expression 2: " + System.currentTimeMillis());

        if (alterTableSelection) {
            for (int m=0; m<table.recordSelection.size(); m++)
                table.recordSelection.set(m, false); //Boolean.FALSE);
            for (int m=0; m<table.selectedSubset.size(); m++)
                table.recordSelection.set(table.selectedSubset.get(m), true); //(((Integer) table.selectedSubset.at(m)).intValue(), true); //Boolean.TRUE);
            table.setModified();
        }
//        System.out.println("Logical expression 3: " + System.currentTimeMillis());
//            System.out.println("Altering table selection 2");
        if (alterTableSelection) {
            table.fireSelectedRecordSetChanged(SelectedRecordSetChangedEvent.RECORD_SELECTION_CHANGED,
                                               queryStringBackup,
                                               selectedSubsetBck,
                                               null,
                                               null);
        }
//        System.out.println("Logical expression 4: " + System.currentTimeMillis());

        return;


/*        StringBuffer queryStringBuff = new StringBuffer(queryString);
        int i=0;
        boolean operFound = false;
        while (i<queryStringBuff.length()) {
            switch (queryStringBuff.charAt(i)) {
                case '<':
                    if (operFound) {
                        queryStringBuff.insert(i+1, '|');
                        operFound = false;
                        i = i+2;
                    }else{
                        queryStringBuff.insert(i, '|');
                        operFound = true;
                        i = i+2;
                    }break;
                case '>':
                    if (operFound) {
                        queryStringBuff.insert(i+1, '|');
                        operFound = false;
                        i = i+2;
                    }else{
                        queryStringBuff.insert(i, '|');
                        operFound = true;
                        i = i+2;
                    }break;
                case '=':
                    if (operFound) {
                        queryStringBuff.insert(i+1, '|');
                        operFound = false;
                        i = i+2;
                    }else{
                        queryStringBuff.insert(i, '|');
                        operFound = true;
                        i = i+2;
                    }break;
                case '!':
                    if (operFound) {
                        queryStringBuff.insert(i+1, '|');
                        operFound = false;
                        i = i+2;
                    }else{
                        queryStringBuff.insert(i, '|');
                        operFound = true;
                        i = i+2;
                    }break;
                default:
                    if (operFound)
                        queryStringBuff.insert(i, '|');
                    operFound = false;
                    i++;
            }
        }
        queryString = queryStringBuff.toString();

        int index;
        int lastIndex;
        String[] logOperators = {"and", "or", "not", "contains", "contained"};
        for (int k=0; k<5; k++) {
            index = lastIndex = 0;
            queryStringBuff = new StringBuffer(queryString.length());
            while ((index = queryString.toLowerCase().indexOf(logOperators[k], lastIndex)) != -1) {
//                System.out.println("k: " + k + " index: " + index + " length: " + queryString.length());
                if ((index + logOperators[k].length()) < queryString.length()) {
                    if ((queryString.charAt(index + logOperators[k].length())) != ' ') {
                        queryStringBuff.append(queryString.substring(lastIndex, index +logOperators[k].length()));
                        lastIndex = index+logOperators[k].length();
//                        System.out.println("In if: "+ queryStringBuff);
                        continue;
                    }
                }else
                    break;

                queryStringBuff.append(queryString.substring(lastIndex, index));
                //Don't put an "|" in front of the not comparator, because the preceding
                //logical comparator (AND or OR) has done so.
                if (!logOperators[k].equals("not"))
                    queryStringBuff.append('|');
                queryStringBuff.append(logOperators[k]);
                queryStringBuff.append('|');
                lastIndex = index + logOperators[k].length();
            }
            queryStringBuff.append(queryString.substring(lastIndex));
            queryString = queryStringBuff.toString();
        }
        queryString = queryStringBuff.toString();
        System.out.println(queryString);

        Array logicalOperators = new Array();
        Array ELEArray = new Array();
        Array NOTArray = new Array();
        String operand, Operator, fieldName;
        StringTokenizer st = new StringTokenizer(queryString, "|", false);
        int numOfTokens = st.countTokens(), count = 0;
        while (st.hasMoreTokens()) {
            fieldName = st.nextToken().trim();
            if (fieldName.equals("not")) {
                NOTArray.add(new Integer(count));
                if (st.hasMoreTokens())
                    fieldName = st.nextToken().trim();
                else
                    throw new InvalidLogicalExpressionException("Invalid query expression");
            }

            if (st.hasMoreTokens())
                Operator = st.nextToken().trim();
            else
                throw new InvalidLogicalExpressionException("Invalid query expression");

            if (st.hasMoreTokens())
                operand = st.nextToken().trim();
            else
                throw new InvalidLogicalExpressionException("Invalid query expression");

            System.out.println(fieldName + " " + Operator + " " + operand);
            try {
                if (operand.charAt(0) == '[' && operand.charAt(operand.length()-1) == ']') {
                    FieldELE ele = new FieldELE(this, fieldName, Operator, operand.substring(1, operand.length()-1));
                    ELEArray.add(ele);
                }else{
                    ElementaryLogicalExpression ele = new ElementaryLogicalExpression(this, fieldName, Operator, operand);
                    ELEArray.add(ele);
                }
            }catch (InvalidFieldNameException e) {throw new InvalidLogicalExpressionException(e.message);}
             catch (InvalidOperatorException e) {throw new InvalidLogicalExpressionException(e.message);}
             catch (InvalidOperandException e) {throw new InvalidLogicalExpressionException(e.message);}
             catch (InvalidFieldOperandException e) {throw new InvalidLogicalExpressionException(e.message);}

            if (st.hasMoreTokens()) {
                String s = st.nextToken();
                if (s.equals("not"))
                    throw new InvalidLogicalExpressionException("Invalid query expression");
                else
                    logicalOperators.add(s);
            }
            count++;
        }
        System.out.println(logicalOperators);

        //Execute the logical expression
        for (int k = 0; k<ELEArray.size(); k++) {
            if (k==0) {
                ((ELE) ELEArray.at(k)).executeELE((Array) table.unselectedSubset.clone(), false, false);
                if (NOTArray.contains(new Integer(k))) {
                    table.selectedSubset.swap(table.unselectedSubset);
                }
            }else if (((String) logicalOperators.at(k-1)).equals("and"))
                ((ELE) ELEArray.at(k)).executeELE((Array) table.selectedSubset.clone(), true, NOTArray.contains(new Integer(k)));
            else if (((String) logicalOperators.at(k-1)).equals("or"))
                ((ELE) ELEArray.at(k)).executeELE((Array) table.unselectedSubset.clone(), false, NOTArray.contains(new Integer(k)));
            System.out.println("Selected subset: " + table.selectedSubset);
            System.out.println("UnSelected subset: " + table.unselectedSubset);
        }
*/    }


    public LogicalExpression(Table tab, String queryString, IntBaseArrayDesc recordsToSearch) throws InvalidLogicalExpressionException {
        table = tab;
        queryString = formatQueryString(queryString);

        ParenthesizedLE ple = new ParenthesizedLE(this, queryString, 0);
//        System.out.println("Logical expression 0: " + System.currentTimeMillis());
        queryResults = ple.executePLE(recordsToSearch);
    }

    public IntBaseArrayDesc getQueryResults() {
        return queryResults;
    }

    private String formatQueryString(String queryString) throws InvalidLogicalExpressionException {
        StringBuffer tmp = new StringBuffer(queryString);
        char[] c = queryString.toCharArray();
        int countOpenParen = 0, countCloseParen = 0;
        int l = 0, n=0;
        while (l<queryString.length()) {
            if (c[l] == '(')
                countOpenParen++;
            if (c[l] == ')')
                countCloseParen++;
            if (c[l] == '"') {
                tmp.insert(l+n, ' ');
                n++;
//                System.out.println(l + "   tmp: " + tmp);
                try{
                    l++;
                    while (c[l] != '"') l++;
                }catch (ArrayIndexOutOfBoundsException e) {throw new InvalidLogicalExpressionException(DBase.resources.getString("LogicalExpressionMsg2"));}
            }
            l++;
        }
        if (countOpenParen != countCloseParen)
            throw new InvalidLogicalExpressionException(DBase.resources.getString("LogicalExpressionMsg3"));

        queryString = tmp.toString();


        StringBuffer queryStringBuff = new StringBuffer(queryString);
        int i=0;
        boolean operFound = false;
        while (i<queryStringBuff.length()) {
            switch (queryStringBuff.charAt(i)) {
                case '<':
                    if (operFound) {
                        queryStringBuff.insert(i+1, '|');
                        operFound = false;
                        i = i+2;
                    }else{
                        queryStringBuff.insert(i, '|');
                        operFound = true;
                        i = i+2;
                    }break;
                case '>':
                    if (operFound) {
                        queryStringBuff.insert(i+1, '|');
                        operFound = false;
                        i = i+2;
                    }else{
                        queryStringBuff.insert(i, '|');
                        operFound = true;
                        i = i+2;
                    }break;
                case '=':
                    if (operFound) {
                        queryStringBuff.insert(i+1, '|');
                        operFound = false;
                        i = i+2;
                    }else{
                        queryStringBuff.insert(i, '|');
                        operFound = true;
                        i = i+2;
                    }break;
                case '!':
                    if (operFound) {
                        queryStringBuff.insert(i+1, '|');
                        operFound = false;
                        i = i+2;
                    }else{
                        queryStringBuff.insert(i, '|');
                        operFound = true;
                        i = i+2;
                    }break;
                case '"':
                    try{
//                        queryStringBuff.setCharAt(i, ' ');
                        i++;
                        while (queryStringBuff.charAt(i) != '"') i++;
//                        queryStringBuff.setCharAt(i, ' ');
                    }catch (StringIndexOutOfBoundsException e) {throw new InvalidLogicalExpressionException(DBase.resources.getString("LogicalExpressionMsg2"));}
                    i++;
                default:
                    if (operFound)
                        queryStringBuff.insert(i, '|');
                    operFound = false;
                    i++;
            }
        }
        queryString = queryStringBuff.toString();
//System.out.println("1. queryString: " + queryString);


        /* Next we have to delimit the logical operators  and the multi-character operators.
         * we search for those operators only in the parts of the "queryString" which is not included
         * in quotes. To do it we split the "queryString" in as many sub-strings as are the parts
         * which are not included in quotes. Then we parse each of these parts, searching for the
         * above operators. In the end these parts are concatenated again.
         */
        Array querySubstrings = new Array();
        int lastIndex = 0;
        int quoteIndex = -1;
        if ((quoteIndex = queryString.indexOf('"', lastIndex)) != -1) {
            querySubstrings.add(queryString.substring(lastIndex, quoteIndex));
//            System.out.println("querySubstrings.at(" + (querySubstrings.size()-1) + "): " + querySubstrings.at(querySubstrings.size()-1));
            lastIndex = queryString.indexOf('"', quoteIndex+1) + 1;
            querySubstrings.add(queryString.substring(quoteIndex, lastIndex));
//            System.out.println("querySubstrings.at(" + (querySubstrings.size()-1) + "): " + querySubstrings.at(querySubstrings.size()-1));
//            System.out.println("lastIndex: " + lastIndex + ", queryString: " + queryString);
        }
//        System.out.println("queryString.substring(lastIndex): " + queryString.substring(lastIndex));
        querySubstrings.add(queryString.substring(lastIndex));
//        System.out.println("querySubstrings: " + querySubstrings);

        int index;
        String[] logOperators = {
            DBase.resources.getString("and"),
            "και",
            DBase.resources.getString("or"),
            "ή",
            DBase.resources.getString("not"),
            "όχι",
            DBase.resources.getString("contains"),
            "περιέχει",
            DBase.resources.getString("contained"),
            "περιέχεται"
            };

//        System.out.println("querySubstrings.size(): " + querySubstrings.size());
        for (int m=0; m<querySubstrings.size(); m++) {
            /* Check only the strings which have an odd position in the Array. The strings
             * in the even positions, are the substrings which are contained within quotes.
             */
            if (m%2 == 1)
                continue;
            queryString = (String) querySubstrings.at(m);
//            System.out.println("Processing: " + queryString + ", m: " + m);
            for (int k=0; k<logOperators.length; k++) {
                index = lastIndex = 0;
                queryStringBuff = new StringBuffer(queryString.length());
//                System.out.println("logOperators[k]: " + logOperators[k] + ", lastIndex: " + lastIndex + ", queryString.toLowerCase().indexOf(logOperators[k], lastIndex)): " + queryString.toLowerCase().indexOf(logOperators[k], lastIndex));
//                if (index > 0)
//                    System.out.println("Char before (at " + (index-1) +"): " + queryString.charAt(index-1));
                while ((index = queryString.toLowerCase().indexOf(logOperators[k], lastIndex)) != -1) { // && (index > 0 && queryString.charAt(index-1) == ' ')) {
//                    System.out.println("logOperators[k]: " + logOperators[k] + ", k: " + k + " index: " + index + " length: " + queryString.length());
                    if (index > 0 && queryString.charAt(index-1) != ' ') {
                        queryStringBuff.append(queryString.substring(lastIndex, index +logOperators[k].length()));
                        lastIndex = index + logOperators[k].length();
                        continue;
                    }
                    if ((index + logOperators[k].length()) < queryString.length()) {
                        if ((queryString.charAt(index + logOperators[k].length())) != ' ') {
                            queryStringBuff.append(queryString.substring(lastIndex, index +logOperators[k].length()));
                            lastIndex = index+logOperators[k].length();
    //                        System.out.println("In if: "+ queryStringBuff);
                            continue;
                        }
                    }else
                        break;

                    queryStringBuff.append(queryString.substring(lastIndex, index));
                    //Don't put an "|" in front of the not comparator, because the preceding
                    //logical comparator (AND or OR) has done so.
                    if (!logOperators[k].equals(DBase.resources.getString("not")))
                        queryStringBuff.append('|');
                    queryStringBuff.append(logOperators[k]);
                    queryStringBuff.append('|');
                    lastIndex = index + logOperators[k].length();
                }
                queryStringBuff.append(queryString.substring(lastIndex));
                queryString = queryStringBuff.toString();
            }
//            System.out.println("queryString turned to: " + queryStringBuff.toString());
            querySubstrings.put(m, queryStringBuff.toString());
        }

        /* Compose the "queryString" again from its parts
         */
         queryString = "";
        for (int m=0; m<querySubstrings.size(); m++)
            queryString = queryString + querySubstrings.at(m);

//System.out.println("2. queryString: " + queryString);
        querySubstrings.clear();
        return queryString;
    }
}