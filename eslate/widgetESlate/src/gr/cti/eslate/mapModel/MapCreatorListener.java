package gr.cti.eslate.mapModel;

interface MapCreatorListener {
    public void databaseDefinition();
    public void tableDefinition(gr.cti.eslate.database.engine.Table table);
    public void mapCreatorClosing();
    public void mapCreatorClosed();
}