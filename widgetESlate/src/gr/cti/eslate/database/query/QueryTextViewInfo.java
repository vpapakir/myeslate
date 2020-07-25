package gr.cti.eslate.database.query;

import javax.swing.DefaultListModel;

public class QueryTextViewInfo {

    //This class should carry the model.
    int fieldListSelectedIndex;
    String queryAreaText;
    DefaultListModel myListModel;

    public QueryTextViewInfo(DefaultListModel listModel) {
        myListModel=listModel;
        if (listModel.getSize() > 0)
            fieldListSelectedIndex = 0;
        else
            fieldListSelectedIndex = -1;
        queryAreaText="";
    }
    public void saveInfo(int index,String text) {
        fieldListSelectedIndex=index;
        queryAreaText=text;
    }

    
}