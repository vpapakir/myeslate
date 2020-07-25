package gr.cti.eslate.tableInspector;

import java.util.ListResourceBundle;

public class MessagesBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"componame",       "Record Browser"},
        {"object",          "Object "},
        {"objects",         "objects"},
        {"of",              " of "},
        {"couldntquery",    "Couldn't perform query!"},
        {"cantsee",         "You can't see the data!"},
        {"error",           "Error!"},
        {"cannotpaste",     "Cannot paste data!"},
        {"notrecord",       "Data are not a database record."},
        {"malformed",       "Malformed record data."},
        {"confirm",         "Confirmation"},
        {"overwrite",       "Overwrite existing"},
        {"questionmark",    "?"},
        {"cannotcopy",      "Cannot copy this record. Data types not compatible!"},
        {"incompatible",    "Nothing was pasted. Completely incompatible records!"},
        {"showall",         "Show all records"},
        {"showselected",    "Show only selected records"},
        {"gotomessage",     "Type object number:"},
        {"gototitle",       "Goto object"},
        {"invalidnumber",   "The number you gave is not valid! Please, try again!"},
        {"notinrange",      "The number you gave is not in the valid range!"},
        {"AND",             "and"},
        {"and",             "and"},
        {"OR",              "or"},
        {"or",              "or"},
        {"NOT",             "not"},
        {"not",             "not"},
        {"CONTAINS",        "contains"},
        {"Contains",        "contains"},
        {"contains",        "contains"},
        {"CONTAINED",       "contained"},
        {"Contained",       "contained"},
        {"contained",       "contained"},
        {"book",            "Book"},
        {"true",            "True"},
        {"false",           "False"},
        //This key contains the accepted lowercase strings that are considered true in the bundle language
        {"acceptedtrue",    "true"},
        //This key contains the accepted lowercase strings that are considered false in the bundle language
        {"acceptedfalse",   "false"},
        {"notlocked",       "Field not locked. Wrong table number, field name or field value."},
        {"notunlocked",     "Field not unlocked. Wrong table number, field name or field value."},
	};
}
