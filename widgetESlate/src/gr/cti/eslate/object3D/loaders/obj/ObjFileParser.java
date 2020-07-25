package gr.cti.eslate.object3D.loaders.obj;

import java.io.StreamTokenizer;
import java.io.IOException;
import java.io.Reader;

class ObjFileParser extends StreamTokenizer {

    private static final char BACKSLASH = '\\';

    // setup
    //
    // Sets up StreamTokenizer for reading ViewPoint .obj file format.
    //
    void setup() {
        resetSyntax();
        eolIsSignificant(true);
        lowerCaseMode(true);

        // All printable ascii characters
        wordChars('!', '~');

        // Comment from ! to end of line
        commentChar('!');

        whitespaceChars(' ', ' ');
        whitespaceChars('\n', '\n');
        whitespaceChars('\r', '\r');
        whitespaceChars('\t', '\t');

        // These characters returned as tokens
        ordinaryChar('#');
        ordinaryChar('/');
        ordinaryChar(BACKSLASH);
    } // End of setup

    // getToken
    //
    // Gets the next token from the stream. Puts one of the four
    // constants (TT_WORD, TT_NUMBER, TT_EOL, or TT_EOF) or the token value
    // for single character tokens into ttype. Handles backslash
    // continuation of lines.
    void getToken() {
        int t;
        boolean done = false;

        try {
            do {
                t = nextToken();
                if (t==BACKSLASH) {
                    t = nextToken();
                    if (ttype!=TT_EOL)
                        done = true;
                } else
                    done = true;
            } while (! done);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // End of getToken

    void printToken() {
        switch (ttype) {
        case TT_EOL:
            System.out.println("Token EOL");
            break;
        case TT_EOF:
            System.out.println("Token EOF");
            break;
        case TT_WORD:
            System.out.println("Token TT_WORD: "+sval);
            break;
        case '/':
            System.out.println("Token /");
            break;
        case BACKSLASH:
            System.out.println("Token "+BACKSLASH);
            break;
        case '#':
            System.out.println("Token #");
            break;
        }
    } // end of printToken

    // skipToNextLine
    //
    // Skips all tokens on the rest of this line. Doesn't do anything if
    // We're already at the end of a line
    //
    void skipToNextLine() {
        while (ttype!=TT_EOL) {
            getToken();
        }
    } // end of skipToNextLine

    // getNumber
    //
    // Gets a number from the stream. Note that we don't recognize
    // numbers in the tokenizer automatically because numbers might be in
    // scientific notation, which isn't processed correctly by
    // StreamTokenizer. The number is returned in nval.
    //
    void getNumber() {
        int t;

        try {
            getToken();
            // if (ttype != TT_WORD)
            // throw new ParsingErrorException("Expected number on line " +
            // lineno());
            nval = (Double.valueOf(sval)).doubleValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    } // end of getNumber

    // ObjectFileParser constructor
    //
    ObjFileParser(Reader r) {
        super(r);
        setup();
    } // end of ObjectFileParser

} // End of file ObjectFileParser.java
