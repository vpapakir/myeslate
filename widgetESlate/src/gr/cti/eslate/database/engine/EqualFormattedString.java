package gr.cti.eslate.database.engine;


import java.util.ArrayList;

/**
 * EqualFormattedString is a binary predicate that
 * compares a string(first) to a formatted String(second). A formatted
 * string contains one or more wildcards, typicaly characters '*' and
 * '^', which denote zero or more characters and exactly one character
 * respectively. EqualFormattedString returns true if the first string
 * can be an instance of the recond formatted string.
 */

public final class EqualFormattedString extends StringComparator {
    ArrayList anyCharIndex;
    ArrayList anyString;

    public EqualFormattedString(ArrayList a1, ArrayList a2) {
        anyCharIndex = a1;
        anyString = a2;
    }

    /**
     * Return true if the first string is an instance of the second formatted string.
     * @param first The first string.
     * @param second The second formatted string.
     */
    public boolean execute(String first, String second) {
        int i;
//        first = (String) first;
//        second = (String) second;

        if (anyString.isEmpty()) {
            if (first.length() != second.length())
                return false;
            //Eliminating characters at the position of '^' characters in the variant string operand
            char[] c = first.toCharArray();
            for (i=0; i<anyCharIndex.size(); i++)
                c[((Integer) anyCharIndex.get(i)).intValue()] = '^';
            first = new String(c);
            if (first.equals(second))
                return true;
            return false;
        }

        int k, l = first.length()-1, searchIndex=0, lastSearchIndexValue;
        String s, s1, s2;
        StringBuffer sb;
        Integer index;
        int lastIndex = 0;
        int foundAtIndex = 0;
        for (i=0; i<anyString.size(); i++) {


            if (((String) anyString.get(i)).length() == 0) {
                if (i == (anyString.size()-1))
                    return true;
                continue;
            }

            if (searchIndex>l) {
                return false;
            }

            s1 = (String) anyString.get(i);

            lastSearchIndexValue = searchIndex;
            if (i == 0 && s1.indexOf('^')==-1) {
                if (!first.startsWith(s1))
                    return false;
                else
                    searchIndex = searchIndex + s1.length();
            }else if (i == (anyString.size()-1) && s1.indexOf('^')==-1) {
                int n = first.length() - s1.length();
                if (n < searchIndex)
                    return false;
                String s4 = first.substring(n);
                if (!s4.equals(s1))
                    return false;
                else
                    return true;
            }else{
                searchIndex = first.indexOf(s1, searchIndex);
//                System.out.println("searchIndex: " +searchIndex);
                if (searchIndex == -1) {
                    if (s1.indexOf('^') == -1)
                        return false;
                    else{
                        int numOfLeadingCarets=0, n;
                        while (numOfLeadingCarets < s1.length() && s1.charAt(numOfLeadingCarets) == '^')
                            numOfLeadingCarets++;

                        int g = numOfLeadingCarets;
                        while (g < s1.length() && s1.charAt(g) != '^')
                            g++;

                        String lookForString = s1.substring(numOfLeadingCarets, g);

                        //Find all the carets'(^) positions in string s1
                        ArrayList s1Carets = new ArrayList();
                        for (int m=0; m<s1.length(); m++) {
                            if (s1.charAt(m) == '^')
                                s1Carets.add(new Integer(m));
                        }

                        int numOfTrailingCarets = s1Carets.size()-numOfLeadingCarets;

//                        System.out.println("first: " + ((String) first) + "  lookForString: " + lookForString + ", length: " + lookForString.length());
                        if (lookForString.length() == 0) {
//                            System.out.println(((String) first).length() + ", " + numOfLeadingCarets + ", " + foundAtIndex);
                            if ((first.length()-1) >= numOfLeadingCarets + foundAtIndex) {
                                if (foundAtIndex==0)
                                    foundAtIndex = numOfLeadingCarets + foundAtIndex-1;
                                else
                                    foundAtIndex = numOfLeadingCarets + foundAtIndex;
                                searchIndex = foundAtIndex+1;
                                continue;
                            }else
                                return false;
                        }

                        searchIndex = foundAtIndex;
                        if (searchIndex !=0) searchIndex++;
//                        System.out.println("searchIndex: " + searchIndex);
                        boolean found = false;
                        while ((searchIndex = first.indexOf(lookForString, searchIndex)) != -1) {
//                            System.out.println("====================");
                            if (i==0 && searchIndex > numOfLeadingCarets) {
//                                System.out.println("Breaking here" + searchIndex + ", " + numOfLeadingCarets);
                                break;
                            }

                            if (i == (anyString.size()-1)) {
                                if (searchIndex + lookForString.length()+numOfTrailingCarets != (first.length())) {
//                                    System.out.println("Breaking out...." + searchIndex + ", " + lookForString.length() + ", " + numOfTrailingCarets + ", " + (((String) first).length()) + ", " + (String) first);
                                    searchIndex++;
                                    if (searchIndex == first.length()) break;
                                    continue;
                                }
                            }

                            if (searchIndex < numOfLeadingCarets) {
                                searchIndex++;
                                if (searchIndex == first.length()) break;
//                                System.out.println("Continuing 1");
                                continue;
                            }
                            if (searchIndex-numOfLeadingCarets + s1.length() > first.length()) {
                                searchIndex++;
                                if (searchIndex == first.length()) break;
//                                System.out.println("Continuing 2");
                                continue;
                            }
//                            System.out.println("s1: " + s1 + "    " + searchIndex + ", " + numOfLeadingCarets + ", " + s1.length());
                            String partialString = first.substring(searchIndex-numOfLeadingCarets, searchIndex-numOfLeadingCarets+s1.length());
//                            System.out.println("s1: " + s1 + ",  partialString: " + partialString);

                            if ((foundAtIndex!=0) && (searchIndex-numOfLeadingCarets <= foundAtIndex)) {
                                searchIndex++;
//                                System.out.println("Continuing 3");
                                if (searchIndex == first.length()) break;
                                continue;
                            }

                            EqualFormattedString eqfs = new EqualFormattedString(s1Carets, new ArrayList());
                            if (eqfs.execute(partialString, s1)) {
                                foundAtIndex = searchIndex;
                                found = true;
                                break;
                            }else{
                                searchIndex++;
//                                System.out.println("Continuing 4");
                                if (searchIndex == first.length()) break;
                            }
                        }
                        if (found){
//                            System.out.println("Continuing...");
                            searchIndex++;
                            continue;
                        }else
                            return false;
                    }
                }

                s = first.substring(searchIndex, searchIndex+((String) anyString.get(i)).length());
//                System.out.println("searchIndex: " + searchIndex + "  first: " + s + "   anyString: " + anyString.at(i));

                if (s.equals(s1)) {
//                    System.out.println("Continuing... "  + searchIndex);
                    foundAtIndex = searchIndex;
                    searchIndex = searchIndex + ((String) anyString.get(i)).length();
//                    System.out.println("Continuing... "  + searchIndex);
                    continue;
                }
//                System.out.println("Exiting...");
                return false;
            }
        }
        return true;
    }

}

