package gr.cti.eslate.database.query;

import java.util.ListResourceBundle;

public class QueryComponentBeanInfoBundle extends ListResourceBundle {

    public Object [][] getContents() {
        return contents;
    }
    static final Object[][] contents={
        // Property descriptors
        {"ToolBarButtons"                       ,   "Toolbar buttons"},
        {"ExecuteButtonVisible"                 ,   "\'Execute Query\' button visible"},
        {"ExecuteButtonVisibleTip"              ,   "Set the visibility of the execution button"},
        {"ClearQueryButtonVisible"              ,   "\'Clear Query\' button visible"},
        {"ClearQueryButtonVisibleTip"           ,   "Set the visibility of the button that clears the query"},
        {"addRowButtonVisible"                  ,   "\'Add Row\' button visible"},
        {"addRowButtonVisibleTip"               ,   "Set the visibility of the the button that adds rows to the table"},
        {"removeRowButtonVisible"               ,   "\'Remove Row\' button visible"},
        {"removeRowButtonVisibleTip"            ,   "Set the visibility of the the button that removes columns from the table"},
        {"headerStatusButtonVisible"            ,   "\'Header Showing\' button visible"},
        {"headerStatusButtonVisibleTip"         ,   "Set the visibility of the button that shows or hides the header of the columns"},
        {"queryPaneButtonVisible"               ,   "\'Change View\' button visible"},
        {"queryPaneButtonVisibleTip"            ,   "Set the visibility of the button that switches the view of the Query Component"},
        {"newSelectionButtonVisible"            ,   "\'New Selection Set\' button visible"},
        {"newSelectionButtonVisibleTip"         ,   "Set the visibility of the button which forms a new selection's set"},
        {"addToSelectionButtonVisible"          ,   "\'Add to Selected Set\' button visible"},
        {"addToSelectionButtonVisibleTip"       ,   "Set the visibility of the button which adds the selections to the existing set"},
        {"removeFromSelectionVisible"           ,   "\'Remove from Selected Set\' button visible"},
        {"removeFromSelectionVisibleTip"        ,   "Set the visibility of the button which removes the selections from the existing set "},
        {"selectFromSelectionButtonVisible"     ,   "\'Select from the Selected Set\' button visible"},
        {"selectFromSelectionButtonVisibleTip"  ,   "Set the visibility of the button which selects elements from the existing set"},
        // Event descriptors
        {"propertyChange"                       ,   "Property Change"},
        {"vetoableChange"                       ,   "Vetoable Change"},
        {"mouseEntered"                         ,   "Mouse Entered"},
        {"mouseExited"                          ,   "Mouse Exited"},
        {"mouseMoved"                           ,   "Mouse Moved"},
   };
}