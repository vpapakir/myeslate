package gr.cti.eslate.utils;

import java.util.EventListener;
import java.awt.datatransfer.Transferable;

/**
 * TransferHandlerListener.
 *
 * @author      George Tsironis
 * @version     2.0.0, 18-May-2006
 */
public interface TransferHandlerListener extends EventListener {
    public Transferable transferableCreated(TransferableCreatedEvent cie);
    public boolean dataImportAllowed(DataImportAllowedEvent cie);
    public boolean dataImported(DataImportedEvent e);
    public void dataExported(DataExportedEvent e);
}
