/*
===============================================================================

        FILE:  GuiConsole.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Graphical console
        
        PROGRAMMERS:
        
                Daniel Azuma (DA)  <dazuma@kagi.com>
        
        COPYRIGHT:
        
                Copyright (C) 1997-1999  Daniel Azuma  (dazuma@kagi.com)
                
                This program is free software; you can redistribute it and/or
                modify it under the terms of the GNU General Public License
                as published by the Free Software Foundation; either version 2
                of the License, or (at your option) any later version.
                
                This program is distributed in the hope that it will be useful,
                but WITHOUT ANY WARRANTY; without even the implied warranty of
                MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
                GNU General Public License for more details.
                
                You should have received a copy of the GNU General Public
                License along with this program. If not, you can obtain a copy
                by writing to:
                        Free Software Foundation, Inc.
                        59 Temple Place - Suite 330,
                        Boston, MA  02111-1307, USA.
        
        VERSION:
        
                Turtle Tracks 1.0  (13 November 1999)
        
        CHANGE HISTORY:
        
                13 November 1999 -- DA -- Released under GNU General Public License

===============================================================================
*/


package virtuoso.logo.app;

import virtuoso.logo.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.util.Vector;
import java.io.*;


/**
 * Console interface
 */
@SuppressWarnings(value={"unchecked"})
public class GuiConsole
extends InteractiveConsole
{

        static final int BUFFER_SIZE = 4096;
        static final int OUTPUT_LENGTH = 4096;
        static final int HISTORY_SIZE = 20;


        GuiConsoleWind _wind;
        MenuActor _menuActor;
        Vector _buf;
        Vector _editors;
        String _curPrompt;
        boolean _copyPrompt;
        Clipboard _clipboard;
        Font _monofont;


        /**
         * Create a GuiConsole
         */
        public GuiConsole()
        {
        }


        /**
         * Set up console
         */
        protected void setup()
        throws
                SetupException
        {
                try
                {
                        _monofont = Font.decode("Monospaced");
                        _clipboard = new Clipboard("private");
                        _menuActor = new MenuActor();
                        _wind = new GuiConsoleWind();
                }
                catch (Throwable e)
                {
                        throw new SetupException("Unable to set up graphical console: " + e.toString());
                }
                _buf = new Vector();
                _editors = new Vector();
                _curPrompt = new String();
                _copyPrompt = false;
                
                putStatusMessage("Turtle Tracks graphical console and editor v1.0");
        }


        /**
         * Gets next line in buffer, or null if no lines
         *
         * @return the string
         */
        synchronized String popBuffer()
        {
                if (_buf.size() == 0)
                {
                        return null;
                }
                String str = (String)(_buf.firstElement());
                _buf.removeElementAt(0);
                return str;
        }


        /**
         * Pushes entry into the buffer
         */
        synchronized void pushBuffer(
                String str)
        {
                _buf.addElement(str);
                notifyAll();
        }


        /**
         * Has the console encountered EOF?
         *
         * @return true iff eof encountered
         */
        public synchronized boolean eof()
        {
                return false;
        }


        /**
         * Get a line from the console. Blocks until a line is available.
         *
         * @return the string
         */
        public synchronized String getLine()
        {
                String str = popBuffer();
                
                while (str == null)
                {
                        try
                        {
                                wait();
                        }
                        catch (InterruptedException e) {}
                        str = popBuffer();
                }
                putLine(str);
                if (str.length() > 0)
                {
                        _wind.insertHistory(str);
                }
                return str;
        }


        /**
         * Prompt and read a command
         *
         * @param prompt the prompt
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public synchronized String promptGetLine(
                char prompt)
        {
                put(prompt);
                put(' ');
                
                char[] cs = new char[2];
                cs[0] = prompt;
                cs[1] = ' ';
                _curPrompt = new String(cs);
                _copyPrompt = true;
                
                String str = popBuffer();
                while (str == null)
                {
                        try
                        {
                                wait();
                        }
                        catch (InterruptedException e) {}
                        str = popBuffer();
                }
                _curPrompt = new String();
                putLine(str);
                if (str.length() > 0)
                {
                        _wind.insertHistory(str);
                }
                return str;
        }


        /**
         * Get all available data from stream
         *
         * @param buf buffer to read into
         *
         * @return how much data was actually read
         */
        public synchronized int getAvailable(
                char[] buf)
        {
                String str = null;
                
                if (_buf.size() != 0)
                {
                        str = (String)(_buf.firstElement());
                        if (str.length() > buf.length-1)
                        {
                                _buf.setElementAt(str.substring(buf.length-1),0);
                                str = str.substring(0, buf.length-1).concat(Machine.LINE_SEPARATOR);
                        }
                        else
                        {
                                _buf.removeElementAt(0);
                                str = str.concat(Machine.LINE_SEPARATOR);
                        }
                }
                
                if (str == null)
                {
                        return 0;
                }
                else
                {
                        str.getChars(0, str.length(), buf, 0);
                        return str.length();
                }
        }


        /**
         * Write a char to the stream
         *
         * @param c the char
         */
        public synchronized void put(
                char c)
        {
                _wind.put(String.valueOf(c));
        }


        /**
         * Write a string to the console, without an EOL
         *
         * @param str the string
         */
        public synchronized void put(
                String str)
        {
                _wind.put(str);
        }


        /**
         * Write a char array to the stream
         *
         * @param buf the buffer
         * @param num number of characters
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public synchronized void put(
                char[] buf,
                int num)
        {
                _wind.put(new String(buf, 0, num));
        }


        /**
         * Write a string to the stream, terminated with a newline
         *
         * @param str the string
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public synchronized void putLine(
                String str)
        {
                _wind.put(str+'\n');
        }


        /**
         * Get a character from the console
         *
         * @return char the character
         */
        public synchronized char getChar()
        {
                return _wind.getChar();
        }


        /**
         * Character available on console?
         *
         * @return true iff available
         */
        public synchronized boolean charAvail()
        {
                return _wind.charAvail();
        }


        /**
         * Request a new editor
         *
         * @param data initial data for editor window
         *
         * @exception virtuoso.logo.LanguageException failed to open editor
         */
        public synchronized void createEditor(
                String data)
        throws
                LanguageException
        {
                _editors.addElement(new GuiEditorWind(data));
        }


        /**
         * Close down the console
         */
        protected void exiting()
        {
                int i;
                for (i=0; i<_editors.size(); i++)
                {
                        ((GuiEditorWind)(_editors.elementAt(i))).dispose();
                }
                _wind.dispose();
        }


        /**
         * Menu command inner class
         */
        class MenuActor
        implements
                ActionListener
        {

                /**
                 * Called on action
                 *
                 * @param event the event
                 */
                public void actionPerformed(
                        ActionEvent event)
                {
                        String cmd = event.getActionCommand();
                        if (cmd.equals("NEW "))
                        {
                                try
                                {
                                        createEditor("");
                                }
                                catch (LanguageException e) {}
                        }
                        else if (cmd.equals("OPEN"))
                        {
                                FileDialog dlog = new FileDialog(_wind, "Open File");
                                dlog.setVisible(true);
                                String name = dlog.getFile();
                                if (name == null)
                                {
                                        return;
                                }
                                try
                                {
                                        File file = new File(dlog.getDirectory(), name);
                                        FileReader read = new FileReader(file);
                                        char[] buf = new char[BUFFER_SIZE];
                                        StringBuffer sb = new StringBuffer();
                                        while (true)
                                        {
                                                int num = read.read(buf);
                                                if (num == -1)
                                                        break;
                                                sb.append(buf, 0, num);
                                        }
                                        try
                                        {
                                                createEditor(sb.toString());
                                        }
                                        catch (LanguageException e) {}
                                }
                                catch (IOException e)
                                {
                                        putLine("Couldn't read file: " + e.toString());
                                }
                        }
                        else if (cmd.equals("LODM"))
                        {
                                FileDialog dlog = new FileDialog(_wind, "Load Contents");
                                dlog.setVisible(true);
                                String name = dlog.getFile();
                                if (name == null)
                                {
                                        return;
                                }
                                try
                                {
                                        File file = new File(dlog.getDirectory(), name);
                                        InterpreterThread thread = new InterpreterThread(mach(),
                                                new IOStream(new BufferedReader(new FileReader(file))));
                                        thread.setPriority(_priority);
                                        thread.start();
                                }
                                catch (FileNotFoundException e)
                                {
                                        putLine("File \"" + name + "\" not found.");
                                }
                        }
                        else if (cmd.equals("SAVM"))
                        {
                                FileDialog dlog = new FileDialog(_wind, "Save Contents", FileDialog.SAVE);
                                dlog.setVisible(true);
                                String name = dlog.getFile();
                                if (name == null)
                                {
                                        return;
                                }
                                try
                                {
                                        IOWriteFile file = new IOWriteFile(new File(dlog.getDirectory(), name)); 
                                        mach().printout(file, null, null, null);
                                        file.close();
                                }
                                catch (LanguageException e)
                                {
                                        putLine("Error saving state: " + e.generateMessage());
                                }
                        }
                        else if (cmd.equals("RSET"))
                        {
                                mach().terminateAllThreads();
                                mach().eraseAll();
                        }
                        else if (cmd.equals("INTR"))
                        {
                                mach().terminateAllThreads();
                        }
                        else if (cmd.equals("PAUS"))
                        {
                        }
                }

        }


        /**
         * Console window inner class
         */
        class GuiConsoleWind
        extends Frame
        implements
                ActionListener,
                FocusListener,
                KeyListener
        {
                /**
                 * Serialization version.
                 */
                final static long serialVersionUID = 1L;
                
                TextArea _entryArea;
                TextArea _historyArea;
                int _historyLength;
                Vector _history;
                int _historyPos;
                
                MenuItem _cutItem;
                MenuItem _pasteItem;
                MenuItem _clearItem;


                /**
                 * Create a GuiConsoleWind
                 */
                GuiConsoleWind()
                {
                        GridBagLayout layout = new GridBagLayout();
                        setLayout(layout);
                        setTitle("Turtle Tracks Console");
                        setBackground(Color.lightGray);
                        GridBagConstraints constraints = new GridBagConstraints();
                        
                        constraints.weightx = 1.0;
                        constraints.gridwidth = GridBagConstraints.REMAINDER;
                        
                        constraints.weighty = 0.0;
                        constraints.fill = GridBagConstraints.NONE;
                        constraints.anchor = GridBagConstraints.SOUTHWEST;
                        Label label = new Label("  History:");
                        layout.setConstraints(label, constraints);
                        add(label);
                        _historyArea = new TextArea(24, 80);
                        _historyArea.setEditable(false);
                        _historyArea.setBackground(Color.white);
                        _historyArea.setFont(_monofont);
                        constraints.weighty = 1.0;
                        constraints.fill = GridBagConstraints.BOTH;
                        constraints.anchor = GridBagConstraints.CENTER;
                        layout.setConstraints(_historyArea, constraints);
                        add(_historyArea);
                        _historyLength = 0;
                        
                        constraints.weighty = 0.0;
                        constraints.fill = GridBagConstraints.NONE;
                        constraints.anchor = GridBagConstraints.SOUTHWEST;
                        constraints.insets = new Insets(5, 0, 0, 0);
                        label = new Label("  Enter command:");
                        layout.setConstraints(label, constraints);
                        add(label);
                        _entryArea = new TextArea(4, 80);
                        _entryArea.setBackground(Color.white);
                        _entryArea.addFocusListener(this);
                        _entryArea.addKeyListener(this);
                        _entryArea.setFont(_monofont);
                        constraints.insets = new Insets(0, 0, 0, 0);
                        constraints.anchor = GridBagConstraints.CENTER;
                        constraints.fill = GridBagConstraints.HORIZONTAL;
                        layout.setConstraints(_entryArea, constraints);
                        add(_entryArea);
                        
                        MenuBar mb = new MenuBar();
                        Menu m;
                        MenuItem mi;
                        
                        m = new Menu("File");
                        mi = new MenuItem("New Editor");
                        mi.addActionListener(_menuActor);
                        mi.setActionCommand("NEW ");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_N));
                        m.add(mi);
                        mi = new MenuItem("Open Into Editor...");
                        mi.addActionListener(_menuActor);
                        mi.setActionCommand("OPEN");
                        m.add(mi);
                        m.addSeparator();
                        mi = new MenuItem("Load State...");
                        mi.addActionListener(_menuActor);
                        mi.setActionCommand("LODM");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_L));
                        m.add(mi);
                        mi = new MenuItem("Save State...");
                        mi.addActionListener(_menuActor);
                        mi.setActionCommand("SAVM");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_S));
                        m.add(mi);
                        m.addSeparator();
                        mi = new MenuItem("Quit");
                        mi.addActionListener(this);
                        mi.setActionCommand("QUIT");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_Q));
                        m.add(mi);
                        mb.add(m);
                        
                        m = new Menu("Edit");
                        mi = new MenuItem("Undo");
                        mi.setEnabled(false);
                        mi.addActionListener(this);
                        mi.setActionCommand("UNDO");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_Z));
                        m.add(mi);
                        m.addSeparator();
                        _cutItem = new MenuItem("Cut");
                        _cutItem.addActionListener(this);
                        _cutItem.setActionCommand("CUT ");
                        _cutItem.setShortcut(new MenuShortcut(KeyEvent.VK_X));
                        m.add(_cutItem);
                        mi = new MenuItem("Copy");
                        mi.addActionListener(this);
                        mi.setActionCommand("COPY");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_C));
                        m.add(mi);
                        _pasteItem = new MenuItem("Paste");
                        _pasteItem.addActionListener(this);
                        _pasteItem.setActionCommand("PAST");
                        _pasteItem.setShortcut(new MenuShortcut(KeyEvent.VK_V));
                        m.add(_pasteItem);
                        _clearItem = new MenuItem("Clear");
                        _clearItem.addActionListener(this);
                        _clearItem.setActionCommand("CLER");
                        _clearItem.setShortcut(new MenuShortcut(KeyEvent.VK_B));
                        m.add(_clearItem);
                        m.addSeparator();
                        mi = new MenuItem("Clear History");
                        mi.addActionListener(this);
                        mi.setActionCommand("CLRH");
                        m.add(mi);
                        mb.add(m);
                        
                        m = new Menu("Environment");
                        mi = new MenuItem("Reset Environment");
                        mi.addActionListener(_menuActor);
                        mi.setActionCommand("RSET");
                        m.add(mi);
                        m.addSeparator();
                        mi = new MenuItem("Interrupt");
                        mi.addActionListener(_menuActor);
                        mi.setActionCommand("INTR");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_I));
                        m.add(mi);
                /*      mi = new MenuItem("Pause");
                        mi.addActionListener(_menuActor);
                        mi.setActionCommand("PAUS");
                        m.add(mi);      */
                        mb.add(m);
                        
                        setMenuBar(mb);
                        
                        _history = new Vector(HISTORY_SIZE+2);
                        _historyPos = -1;
                        
                        addWindowListener(new WindowHandler());
                        
                        pack();
                        setVisible(true);
                        _entryArea.requestFocus();
                }


                /**
                 * Crops history to prespecified length by killing lines at top
                 */
                private void cropHistory()
                {
                        String str = _historyArea.getText();
                        int startPos = 0;
                        while (_historyLength > OUTPUT_LENGTH)
                        {
                                int pos = str.indexOf('\n', startPos);
                                if (pos >= _historyLength-10 || pos == -1)
                                {
                                        return;
                                }
                                _historyArea.replaceRange(new String(), 0, pos-startPos+1);
                                startPos = pos+1;
                                _historyLength -= pos+1;
                        }
                }


                /**
                 * Insert a line into the input history
                 *
                 * @param str the string to insert
                 */
                void insertHistory(
                        String str)
                {
                        _history.insertElementAt(str, 0);
                        if (_history.size() > HISTORY_SIZE)
                        {
                                _history.removeElementAt(_history.size() - 1);
                        }
                }


                /**
                 * Write a string to the console
                 *
                 * @param str the string
                 */
                void put(
                        String str)
                {
                        if (_curPrompt.length() > 0)
                        {
                                if (_copyPrompt)
                                {
                                        _historyArea.append("\n"+str+_curPrompt);
                                        _copyPrompt = false;
                                        _historyLength += _curPrompt.length()+1;
                                }
                                else
                                {
                                        _historyArea.insert(str, _historyLength-_curPrompt.length());
                                }
                        }
                        else
                        {
                                _historyArea.append(str);
                        }
                        _historyLength += str.length();
                        _historyArea.setCaretPosition(_historyLength+1);
                        cropHistory();
                }


                /**
                 * Get a character from the console
                 *
                 * @return char the character
                 */
                char getChar()
                {
                        String str = _entryArea.getText();
                        
                        if (str.length() == 0)
                        {
                                return '\0';
                        }
                        else
                        {
                                _entryArea.setText(str.substring(1));
                                return str.charAt(0);
                        }
                }


                /**
                 * Character available on console?
                 *
                 * @return true iff available
                 */
                boolean charAvail()
                {
                        return (_entryArea.getText().length() > 0);
                }


                /**
                 * Called on action
                 *
                 * @param event the event
                 */
                public void actionPerformed(
                        ActionEvent event)
                {
                        String cmd = event.getActionCommand();
                        
                        if (cmd.equals("QUIT"))
                        {
                                int result = MessageWind.execute(
                                        MessageWind.genStrings("Are you sure you want to quit Turtle Tracks?"),
                                        MessageWind.genStrings("Quit", "Cancel"), this);
                                if (result == 0)
                                {
                                        goodbye();
                                        mach().terminateAllThreads();
                                        pushBuffer(new String());
                                }
                        }
                        else if (cmd.equals("CLRH"))
                        {
                                _historyArea.setText(_curPrompt);
                                _historyLength = _curPrompt.length();
                        }
                        
                        Component rawfoc = getFocusOwner();
                        if (!(rawfoc instanceof TextArea))
                        {
                                return;
                        }
                        TextArea foc = (TextArea)rawfoc;
                        if (foc == null)
                        {
                                return;
                        }
                        
                        if (cmd.equals("UNDO"))
                        {
                        }
                        else if (cmd.equals("CUT "))
                        {
                                if (foc == _entryArea)
                                {
                                        int selStart = foc.getSelectionStart();
                                        int selEnd = foc.getSelectionEnd();
                                        StringSelection sel = new StringSelection(
                                                foc.getText().substring(selStart, selEnd));
                                        try
                                        {
                                                getToolkit().getSystemClipboard().setContents(sel, sel);
                                        }
                                        catch (SecurityException e)
                                        {
                                                _clipboard.setContents(sel, sel);
                                        }
                                        foc.replaceRange(new String(), selStart, selEnd);
                                        foc.setCaretPosition(selStart);
                                        foc.select(selStart, selStart);
                                }
                        }
                        else if (cmd.equals("COPY"))
                        {
                                StringSelection sel = new StringSelection(foc.getSelectedText());
                                try
                                {
                                        getToolkit().getSystemClipboard().setContents(sel, sel);
                                }
                                catch (SecurityException e)
                                {
                                        _clipboard.setContents(sel, sel);
                                }
                        }
                        else if (cmd.equals("PAST"))
                        {
                                if (foc == _entryArea)
                                {
                                        try
                                        {
                                                String toPaste;
                                                try
                                                {
                                                        Transferable trans = getToolkit().getSystemClipboard().getContents(this);
                                                        toPaste = (String)(trans.getTransferData(DataFlavor.stringFlavor));
                                                }
                                                catch (SecurityException e)
                                                {
                                                        Transferable trans = _clipboard.getContents(this);
                                                        toPaste = (String)(trans.getTransferData(DataFlavor.stringFlavor));
                                                }
                                                int selStart = foc.getSelectionStart();
                                                int afterPos = selStart + toPaste.length();
                                                foc.replaceRange(toPaste, selStart, foc.getSelectionEnd());
                                                foc.setCaretPosition(afterPos);
                                                foc.select(afterPos, afterPos);
                                        }
                                        catch (UnsupportedFlavorException e) {}
                                        catch (IOException e) {}
                                }
                        }
                        else if (cmd.equals("CLER"))
                        {
                                if (foc == _entryArea)
                                {
                                        int pos = foc.getSelectionStart();
                                        foc.replaceRange(new String(), pos, foc.getSelectionEnd());
                                        foc.setCaretPosition(pos);
                                        foc.select(pos, pos);
                                }
                        }
                }


                /**
                 * Handle focus gained events
                 *
                 * @param event the event
                 */
                public void focusGained(
                        FocusEvent event)
                {
                        _cutItem.setEnabled(true);
                        _pasteItem.setEnabled(true);
                        _clearItem.setEnabled(true);
                }


                /**
                 * Handle focus lost events
                 *
                 * @param event the event
                 */
                public void focusLost(
                        FocusEvent event)
                {
                        _cutItem.setEnabled(false);
                        _pasteItem.setEnabled(false);
                        _clearItem.setEnabled(false);
                }


                /**
                 * Handle keyboard events
                 *
                 * @param event the event
                 */
                public void keyTyped(
                        KeyEvent event)
                {
                        char c = event.getKeyChar();
                        if (c == '\n' || c == '\r')
                        {
                                event.consume();
                        }
                }


                /**
                 * Handle keyboard events
                 *
                 * @param event the event
                 */
                public void keyPressed(
                        KeyEvent event)
                {
                        if (event.getKeyCode() == KeyEvent.VK_ENTER)
                        {
                                pushBuffer(_entryArea.getText());
                                _entryArea.setText("");
                                _historyPos = -1;
                                event.consume();
                        }
                        else if (event.getKeyCode() == KeyEvent.VK_UP)
                        {
                                if (_historyPos < _history.size() - 1)
                                {
                                        _historyPos++;
                                        String str = (String)(_history.elementAt(_historyPos));
                                        int pos = str.length();
                                        _entryArea.setText(str);
                                        _entryArea.setCaretPosition(pos);
                                        _entryArea.select(pos, pos);
                                }
                                event.consume();
                        }
                        else if (event.getKeyCode() == KeyEvent.VK_DOWN)
                        {
                                if (_historyPos > -1)
                                {
                                        _historyPos--;
                                        if (_historyPos > -1)
                                        {
                                                String str = (String)(_history.elementAt(_historyPos));
                                                int pos = str.length();
                                                _entryArea.setText(str);
                                                _entryArea.setCaretPosition(pos);
                                                _entryArea.select(pos, pos);
                                        }
                                        else
                                        {
                                                _entryArea.setText("");
                                        }
                                }
                                event.consume();
                        }
                }


                /**
                 * Handle keyboard events
                 *
                 * @param event the event
                 */
                public void keyReleased(
                        KeyEvent event)
                {
                }

        }


        /**
         * Graphical editor window inner class
         */
        class GuiEditorWind
        extends Frame
        implements
                ActionListener
        {
                /**
                 * Serialization version.
                 */
                final static long serialVersionUID = 1L;
                
                TextArea _textArea;


                /**
                 * Create a GuiEditorWind
                 */
                GuiEditorWind(
                        String data)
                {
                        GridBagLayout layout = new GridBagLayout();
                        setLayout(layout);
                        setTitle("Turtle Tracks Editor");
                        setBackground(Color.lightGray);
                        
                        _textArea = new TextArea(data, 30, 80);
                        GridBagConstraints constraints = new GridBagConstraints();
                        constraints.fill = GridBagConstraints.BOTH;
                        constraints.weightx = 1.0;
                        constraints.weighty = 1.0;
                        layout.setConstraints(_textArea, constraints);
                        _textArea.setBackground(Color.white);
                        _textArea.setFont(_monofont);
                        add(_textArea);
                        
                        MenuBar mb = new MenuBar();
                        Menu m;
                        MenuItem mi;
                        
                        m = new Menu("File");
                        mi = new MenuItem("New Editor");
                        mi.addActionListener(_menuActor);
                        mi.setActionCommand("NEW ");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_N));
                        m.add(mi);
                        mi = new MenuItem("Open Into Editor...");
                        mi.addActionListener(_menuActor);
                        mi.setActionCommand("OPEN");
                        m.add(mi);
                        m.addSeparator();
                        mi = new MenuItem("Execute without closing");
                        mi.addActionListener(this);
                        mi.setActionCommand("ACCP");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_E));
                        m.add(mi);
                        mi = new MenuItem("Close And Execute");
                        mi.addActionListener(this);
                        mi.setActionCommand("CLAC");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_W));
                        m.add(mi);
                        mi = new MenuItem("Close And Discard");
                        mi.addActionListener(this);
                        mi.setActionCommand("CLDS");
                        m.add(mi);
                        mi = new MenuItem("Save As...");
                        mi.addActionListener(this);
                        mi.setActionCommand("SAVE");
                        m.add(mi);
                        m.addSeparator();
                        mi = new MenuItem("Quit");
                        mi.addActionListener(this);
                        mi.setActionCommand("QUIT");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_Q));
                        m.add(mi);
                        mb.add(m);
                        
                        m = new Menu("Edit");
                        mi = new MenuItem("Undo");
                        mi.setEnabled(false);
                        mi.addActionListener(this);
                        mi.setActionCommand("UNDO");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_Z));
                        m.add(mi);
                        m.addSeparator();
                        mi = new MenuItem("Cut");
                        mi.addActionListener(this);
                        mi.setActionCommand("CUT ");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_X));
                        m.add(mi);
                        mi = new MenuItem("Copy");
                        mi.addActionListener(this);
                        mi.setActionCommand("COPY");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_C));
                        m.add(mi);
                        mi = new MenuItem("Paste");
                        mi.addActionListener(this);
                        mi.setActionCommand("PAST");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_V));
                        m.add(mi);
                        mi = new MenuItem("Clear");
                        mi.addActionListener(this);
                        mi.setActionCommand("CLER");
                        mi.setShortcut(new MenuShortcut(KeyEvent.VK_B));
                        m.add(mi);
                        mb.add(m);
                        
                        setMenuBar(mb);
                        
                        addWindowListener(new WindowHandler());
                        
                        pack();
                        setVisible(true);
                        _textArea.requestFocus();
                }


                /**
                 * Accept the edits
                 */
                void acceptEdits()
                {
                        InterpreterThread thread = new InterpreterThread(mach(),
                                new IOStream(new BufferedReader(new StringReader(_textArea.getText()))));
                        thread.setPriority(_priority);
                        thread.start();
                }


                /**
                 * Save edits to a file
                 *
                 * @return true iff successful
                 */
                boolean saveToFile()
                {
                        FileDialog dlog = new FileDialog(GuiEditorWind.this,
                                "Save Edits", FileDialog.SAVE);
                        dlog.setVisible(true);
                        String name = dlog.getFile();
                        if (name == null)
                        {
                                return false;
                        }
                        try
                        {
                                BufferedWriter file = new BufferedWriter(new FileWriter(new File(dlog.getDirectory(), name)));
                                try
                                {
                                        BufferedReader text = new BufferedReader(new StringReader(_textArea.getText()));
                                        while (true)
                                        {
                                                String str = text.readLine();
                                                if (str == null)
                                                        break;
                                                file.write(str);
                                                file.newLine();
                                        }
                                        text.close();
                                }
                                finally
                                {
                                        file.close();
                                }
                        }
                        catch (IOException e)
                        {
                                putLine("Error saving file: " + e.toString());
                                return false;
                        }
                        return true;
                }


                /**
                 * Called on action
                 *
                 * @param event the event
                 */
                public void actionPerformed(
                        ActionEvent event)
                {
                        String cmd = event.getActionCommand();
                        if (cmd.equals("ACCP"))
                        {
                                acceptEdits();
                        }
                        else if (cmd.equals("CLAC"))
                        {
                                acceptEdits();
                                _editors.removeElement(this);
                                dispose();
                        }
                        else if (cmd.equals("CLDS"))
                        {
                                _editors.removeElement(this);
                                dispose();
                        }
                        else if (cmd.equals("SAVE"))
                        {
                                saveToFile();
                        }
                        else if (cmd.equals("QUIT"))
                        {
                                int result = MessageWind.execute(
                                        MessageWind.genStrings("Are you sure you want to quit Turtle Tracks?"),
                                        MessageWind.genStrings("Quit", "Cancel"), this);
                                if (result == 0)
                                {
                                        goodbye();
                                        mach().terminateAllThreads();
                                        pushBuffer(new String());
                                }
                        }
                        else if (cmd.equals("UNDO"))
                        {
                        }
                        else if (cmd.equals("CUT "))
                        {
                                int selStart = _textArea.getSelectionStart();
                                int selEnd = _textArea.getSelectionEnd();
                                StringSelection sel = new StringSelection(
                                        _textArea.getText().substring(selStart, selEnd));
                                try
                                {
                                        getToolkit().getSystemClipboard().setContents(sel, sel);
                                }
                                catch (SecurityException e)
                                {
                                        _clipboard.setContents(sel, sel);
                                }
                                _textArea.replaceRange(new String(), selStart, selEnd);
                                _textArea.setCaretPosition(selStart);
                                _textArea.select(selStart, selStart);
                        }
                        else if (cmd.equals("COPY"))
                        {
                                StringSelection sel = new StringSelection(_textArea.getSelectedText());
                                try
                                {
                                        getToolkit().getSystemClipboard().setContents(sel, sel);
                                }
                                catch (SecurityException e)
                                {
                                        _clipboard.setContents(sel, sel);
                                }
                        }
                        else if (cmd.equals("PAST"))
                        {
                                try
                                {
                                        String toPaste;
                                        try
                                        {
                                                Transferable trans = getToolkit().getSystemClipboard().getContents(this);
                                                toPaste = (String)(trans.getTransferData(DataFlavor.stringFlavor));
                                        }
                                        catch (SecurityException e)
                                        {
                                                Transferable trans = _clipboard.getContents(this);
                                                toPaste = (String)(trans.getTransferData(DataFlavor.stringFlavor));
                                        }
                                        int selStart = _textArea.getSelectionStart();
                                        int afterPos = selStart + toPaste.length();
                                        _textArea.replaceRange(toPaste, selStart, _textArea.getSelectionEnd());
                                        _textArea.setCaretPosition(afterPos);
                                        _textArea.select(afterPos, afterPos);
                                }
                                catch (UnsupportedFlavorException e) {}
                                catch (IOException e) {}
                        }
                        else if (cmd.equals("CLER"))
                        {
                                int pos = _textArea.getSelectionStart();
                                _textArea.replaceRange(new String(), pos, _textArea.getSelectionEnd());
                                _textArea.setCaretPosition(pos);
                                _textArea.select(pos, pos);
                        }
                }


                /**
                 * Determine if another object is equal to this one
                 *
                 * @param obj what to compare with
                 *
                 * @return true iff equal
                 */
                public boolean equals(
                        Object obj)
                {
                        if (obj instanceof GuiEditorWind)
                        {
                                return ((GuiEditorWind)obj == this);
                        }
                        else
                        {
                                return false;
                        }
                }

        }


        /**
         * Inner class for handling window events
         */
        class WindowHandler
        extends WindowAdapter
        {

                /**
                 * Handle window closing events
                 *
                 * @param event the event
                 */
                public void windowClosing(
                        WindowEvent event)
                {
                        Window w = event.getWindow();
                        if (w instanceof GuiConsoleWind)
                        {
                                int result = MessageWind.execute(
                                        MessageWind.genStrings("Are you sure you want to quit Turtle Tracks?"),
                                        MessageWind.genStrings("Quit", "Cancel"), (GuiConsoleWind)w);
                                if (result == 0)
                                {
                                        goodbye();
                                        mach().terminateAllThreads();
                                        pushBuffer(new String());
                                }
                        }
                        else if (w instanceof GuiEditorWind)
                        {
                                GuiEditorWind wind = (GuiEditorWind)w;
                                int result = MessageWind.execute(
                                        MessageWind.genStrings("Do you want to execute or save your edits before closing?"),
                                        MessageWind.genStrings("Execute", "Save To File", "Discard", "Cancel"), wind);
                                if (result == 0)
                                {
                                        wind.acceptEdits();
                                }
                                else if (result == 1)
                                {
                                        if (!wind.saveToFile())
                                                return;
                                }
                                if (result != 3)
                                {
                                        _editors.removeElement(wind);
                                        wind.dispose();
                                }
                        }
                }

        }

}



