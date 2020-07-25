package gr.cti.eslate.base.container;

import virtuoso.logo.Console;
import virtuoso.logo.LanguageException;

public class LogoConsole extends Console {

    public void put(char c) throws LanguageException {};
    public void put(char buf[], int num) throws LanguageException {};
    public void putLine(String str) {};
    public void put(String str) throws LanguageException {};
    public String promptGetLine(char prompt) {return "";};
    public boolean charAvail() throws LanguageException {return false;};
    public char getChar() throws LanguageException {return ' ';};
    public int getAvailable(char buf[]) throws LanguageException {return 0;};
    public String getLine() throws LanguageException {return "";};
    public boolean eof() throws LanguageException {return true;};
    public void createEditor(String data) throws LanguageException {};
}
