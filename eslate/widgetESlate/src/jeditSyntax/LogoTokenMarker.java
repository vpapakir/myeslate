package jeditSyntax;
/*
 * LogoTokenMarker.java - Turtle Tracks Logo token marker
 * Copyright (C) 1998, 1999 Slava Pestov
 * Copyright (C) 2006 Kriton Kyrimis
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.text.Segment;

/**
 * Turtle Tracks Logo token marker.
 *
 * @author Slava Pestov
 * @author Kriton Kyrimis
 */
public class LogoTokenMarker extends TokenMarker
{
  public LogoTokenMarker()
  {
    this.keywords = getKeywords();
  }

  public byte markTokensImpl(byte token, Segment line, int lineIndex)
  {
    char[] array = line.array;
    int offset = line.offset;
    lastOffset = offset;
    lastKeyword = offset;
    int length = line.count + offset;

    boolean vBar = false;

loop:
    for (int i = offset; i < length; i++) {
      int i1 = (i + 1);

      char c = array[i];

      switch (token) {
        case Token.NULL:
          switch (c) {
            case '"':
              if (i1 < length && array[i1] == '|') {
                doKeyword(line,i,c);
                addToken(i - lastOffset,token);
                token = Token.LITERAL2;
                lastOffset = lastKeyword = i;
                vBar = true;
              }else{
                doKeyword(line,i,c);
                addToken(i - lastOffset,token);
                token = Token.LITERAL1;
                lastOffset = lastKeyword = i;
              }
              break;
            case ';':
              if (doKeyword(line, i, c))
                break;
              addToken(i - lastOffset, token);
              addToken(length - i, Token.COMMENT1);
              lastOffset = lastKeyword = length;
              break loop;
            default:
              if (!Character.isLetterOrDigit(c)
                  && c != '_')
                doKeyword(line, i, c);
              break;
          }
          break;
        case Token.LITERAL1:
          if (c == ' ' || i1 == length) {
            addToken(i1 - lastOffset, token);
            token = Token.NULL;
            lastOffset = lastKeyword = i1;
          }
          break;
        case Token.LITERAL2:
          if (vBar) {
            vBar = false;
          }else{
            if (c == '|') {
              addToken(i1 - lastOffset, token);
              token = Token.NULL;
              lastOffset = lastKeyword = i1;
            }
          }
          break;
        default:
          throw new InternalError("Invalid state: " + token);
      }
    }

    if (token == Token.NULL)
      doKeyword(line, length, '\0');

    switch (token) {
      case Token.LITERAL1:
      case Token.LITERAL2:
        addToken(length - lastOffset, Token.INVALID);
        token = Token.NULL;
        break;
      case Token.KEYWORD2:
        addToken(length - lastOffset, token);
        token = Token.NULL;
      default:
        addToken(length - lastOffset, token);
        break;
    }

    return token;
  }

  private static ResourceBundle resources = null;
  private static ResourceBundle resources2 = null;

  private static ResourceBundle getResources()
  {
    if (resources == null) {
      resources = ResourceBundle.getBundle(
        "gr.cti.eslate.scripting.logo.PrimitivesBundle", Locale.getDefault()
      );
    }
    return resources;
  }

  private static ResourceBundle getResources2()
  {
    if (resources2 == null) {
      resources = ResourceBundle.getBundle(
        "gr.cti.eslate.scripting.logo.ComponentPrimitivesBundle",
        Locale.getDefault()
      );
    }
    return resources2;
  }

  public static KeywordMap getKeywords()
  {
    if (lKeywords == null) {
      lKeywords = new KeywordMap(true);
      addKeyword(lKeywords, "abs", Token.KEYWORD1);
      addKeyword(lKeywords, "allof", Token.KEYWORD1);
      addKeyword(lKeywords, "allopen", Token.KEYWORD1);
      addKeyword(lKeywords, "and", Token.KEYWORD1);
      addKeyword(lKeywords, "anyof", Token.KEYWORD1);
      addKeyword(lKeywords, "apply", Token.KEYWORD1);
      addKeyword(lKeywords, "applyresult", Token.KEYWORD1);
      addKeyword(lKeywords, "arccos", Token.KEYWORD1);
      addKeyword(lKeywords, "arcsin", Token.KEYWORD1);
      addKeyword(lKeywords, "arctan", Token.KEYWORD1);
      addKeyword(lKeywords, "arctan2", Token.KEYWORD1);
      addKeyword(lKeywords, "ascii", Token.KEYWORD1);
      addKeyword(lKeywords, "ashift", Token.KEYWORD1);
      addKeyword(lKeywords, "ask", Token.KEYWORD1);
      addKeyword(lKeywords, "barrier", Token.KEYWORD1);
      addKeyword(lKeywords, "before?", Token.KEYWORD1);
      addKeyword(lKeywords, "beforep", Token.KEYWORD1);
      addKeyword(lKeywords, "bf", Token.KEYWORD1);
      addKeyword(lKeywords, "bitand", Token.KEYWORD1);
      addKeyword(lKeywords, "bitnot", Token.KEYWORD1);
      addKeyword(lKeywords, "bitor", Token.KEYWORD1);
      addKeyword(lKeywords, "bitxor", Token.KEYWORD1);
      addKeyword(lKeywords, "bl", Token.KEYWORD1);
      addKeyword(lKeywords, "butfirst", Token.KEYWORD1);
      addKeyword(lKeywords, "butlast", Token.KEYWORD1);
      addKeyword(lKeywords, "bye", Token.KEYWORD1);
      addKeyword(lKeywords, "catch", Token.KEYWORD1);
      addKeyword(lKeywords, "char", Token.KEYWORD1);
      addKeyword(lKeywords, "close", Token.KEYWORD1);
      addKeyword(lKeywords, "closeall", Token.KEYWORD1);
      addKeyword(lKeywords, "contents", Token.KEYWORD1);
      addKeyword(lKeywords, "cos", Token.KEYWORD1);
      addKeyword(lKeywords, "critical", Token.KEYWORD1);
      addKeyword(lKeywords, "currentthread", Token.KEYWORD1);
      addKeyword(lKeywords, "cwd", Token.KEYWORD1);
      addKeyword(lKeywords, "cwdup", Token.KEYWORD1);
      addKeyword(lKeywords, "define", Token.KEYWORD1);
      addKeyword(lKeywords, "defined?", Token.KEYWORD1);
      addKeyword(lKeywords, "definedp", Token.KEYWORD1);
      addKeyword(lKeywords, "difference", Token.KEYWORD1);
      addKeyword(lKeywords, "do.until", Token.KEYWORD1);
      addKeyword(lKeywords, "do.while", Token.KEYWORD1);
      addKeyword(lKeywords, "each", Token.KEYWORD1);
      addKeyword(lKeywords, "edall", Token.KEYWORD1);
      addKeyword(lKeywords, "edit", Token.KEYWORD1);
      addKeyword(lKeywords, "empty?", Token.KEYWORD1);
      addKeyword(lKeywords, "emptyp", Token.KEYWORD1);
      addKeyword(lKeywords, "end", Token.KEYWORD1);
      addKeyword(lKeywords, "eof?", Token.KEYWORD1);
      addKeyword(lKeywords, "eofp", Token.KEYWORD1);
      addKeyword(lKeywords, "equal?", Token.KEYWORD1);
      addKeyword(lKeywords, "equalp", Token.KEYWORD1);
      addKeyword(lKeywords, "erall", Token.KEYWORD1);
      addKeyword(lKeywords, "erase", Token.KEYWORD1);
      addKeyword(lKeywords, "erasename", Token.KEYWORD1);
      addKeyword(lKeywords, "eraseplist", Token.KEYWORD1);
      addKeyword(lKeywords, "eraseprocedure", Token.KEYWORD1);
      addKeyword(lKeywords, "ern", Token.KEYWORD1);
      addKeyword(lKeywords, "erp", Token.KEYWORD1);
      addKeyword(lKeywords, "erpl", Token.KEYWORD1);
      addKeyword(lKeywords, "exec", Token.KEYWORD1);
      addKeyword(lKeywords, "execasync", Token.KEYWORD1);
      addKeyword(lKeywords, "exp", Token.KEYWORD1);
      addKeyword(lKeywords, "false", Token.KEYWORD1);
      addKeyword(lKeywords, "first", Token.KEYWORD1);
      addKeyword(lKeywords, "for", Token.KEYWORD1);
      addKeyword(lKeywords, "foreach", Token.KEYWORD1);
      addKeyword(lKeywords, "forward", Token.KEYWORD1);
      addKeyword(lKeywords, "fput", Token.KEYWORD1);
      addKeyword(lKeywords, "gc", Token.KEYWORD1);
      addKeyword(lKeywords, "gensym", Token.KEYWORD1);
      addKeyword(lKeywords, "goodbye", Token.KEYWORD1);
      addKeyword(lKeywords, "gprop", Token.KEYWORD1);
      addKeyword(lKeywords, "greater?", Token.KEYWORD1);
      addKeyword(lKeywords, "greaterequal?", Token.KEYWORD1);
      addKeyword(lKeywords, "greaterequalp", Token.KEYWORD1);
      addKeyword(lKeywords, "greaterp", Token.KEYWORD1);
      addKeyword(lKeywords, "if", Token.KEYWORD1);
      addKeyword(lKeywords, "ifelse", Token.KEYWORD1);
      addKeyword(lKeywords, "iff", Token.KEYWORD1);
      addKeyword(lKeywords, "iffalse", Token.KEYWORD1);
      addKeyword(lKeywords, "ift", Token.KEYWORD1);
      addKeyword(lKeywords, "iftrue", Token.KEYWORD1);
      addKeyword(lKeywords, "ignore", Token.KEYWORD1);
      addKeyword(lKeywords, "integer", Token.KEYWORD1);
      addKeyword(lKeywords, "invoke", Token.KEYWORD1);
      addKeyword(lKeywords, "invokeresult", Token.KEYWORD1);
      addKeyword(lKeywords, "item", Token.KEYWORD1);
      addKeyword(lKeywords, "key?", Token.KEYWORD1);
      addKeyword(lKeywords, "keyp", Token.KEYWORD1);
      addKeyword(lKeywords, "last", Token.KEYWORD1);
      addKeyword(lKeywords, "length", Token.KEYWORD1);
      addKeyword(lKeywords, "less", Token.KEYWORD1);
      addKeyword(lKeywords, "lessequal?", Token.KEYWORD1);
      addKeyword(lKeywords, "lessequalp", Token.KEYWORD1);
      addKeyword(lKeywords, "lessp", Token.KEYWORD1);
      addKeyword(lKeywords, "list", Token.KEYWORD1);
      addKeyword(lKeywords, "list?", Token.KEYWORD1);
      addKeyword(lKeywords, "listp", Token.KEYWORD1);
      addKeyword(lKeywords, "load", Token.KEYWORD1);
      addKeyword(lKeywords, "loadprimitives", Token.KEYWORD1);
      addKeyword(lKeywords, "loadurl", Token.KEYWORD1);
      addKeyword(lKeywords, "local", Token.KEYWORD1);
      addKeyword(lKeywords, "localmake", Token.KEYWORD1);
      addKeyword(lKeywords, "log", Token.KEYWORD1);
      addKeyword(lKeywords, "lowercase", Token.KEYWORD1);
      addKeyword(lKeywords, "lput", Token.KEYWORD1);
      addKeyword(lKeywords, "lshift", Token.KEYWORD1);
      addKeyword(lKeywords, "macro?", Token.KEYWORD1);
      addKeyword(lKeywords, "macrop", Token.KEYWORD1);
      addKeyword(lKeywords, "make", Token.KEYWORD1);
      addKeyword(lKeywords, "map", Token.KEYWORD1);
      addKeyword(lKeywords, "member?", Token.KEYWORD1);
      addKeyword(lKeywords, "memberp", Token.KEYWORD1);
      addKeyword(lKeywords, "minus", Token.KEYWORD1);
      addKeyword(lKeywords, "name", Token.KEYWORD1);
      addKeyword(lKeywords, "name?", Token.KEYWORD1);
      addKeyword(lKeywords, "namep", Token.KEYWORD1);
      addKeyword(lKeywords, "names", Token.KEYWORD1);
      addKeyword(lKeywords, "not", Token.KEYWORD1);
      addKeyword(lKeywords, "number?", Token.KEYWORD1);
      addKeyword(lKeywords, "numberp", Token.KEYWORD1);
      addKeyword(lKeywords, "op", Token.KEYWORD1);
      addKeyword(lKeywords, "openrandom", Token.KEYWORD1);
      addKeyword(lKeywords, "openread", Token.KEYWORD1);
      addKeyword(lKeywords, "opensocket", Token.KEYWORD1);
      addKeyword(lKeywords, "openurl", Token.KEYWORD1);
      addKeyword(lKeywords, "openwrite", Token.KEYWORD1);
      addKeyword(lKeywords, "or", Token.KEYWORD1);
      addKeyword(lKeywords, "output", Token.KEYWORD1);
      addKeyword(lKeywords, "parse", Token.KEYWORD1);
      addKeyword(lKeywords, "pi", Token.KEYWORD1);
      addKeyword(lKeywords, "pick", Token.KEYWORD1);
      addKeyword(lKeywords, "plists", Token.KEYWORD1);
      addKeyword(lKeywords, "po", Token.KEYWORD1);
      addKeyword(lKeywords, "poall", Token.KEYWORD1);
      addKeyword(lKeywords, "pon", Token.KEYWORD1);
      addKeyword(lKeywords, "pop", Token.KEYWORD1);
      addKeyword(lKeywords, "popl", Token.KEYWORD1);
      addKeyword(lKeywords, "power", Token.KEYWORD1);
      addKeyword(lKeywords, "pprop", Token.KEYWORD1);
      addKeyword(lKeywords, "pr", Token.KEYWORD1);
      addKeyword(lKeywords, "primitive?", Token.KEYWORD1);
      addKeyword(lKeywords, "primitivep", Token.KEYWORD1);
      addKeyword(lKeywords, "print", Token.KEYWORD1);
      addKeyword(lKeywords, "print1", Token.KEYWORD1);
      addKeyword(lKeywords, "printout", Token.KEYWORD1);
      addKeyword(lKeywords, "printoutname", Token.KEYWORD1);
      addKeyword(lKeywords, "printoutplist", Token.KEYWORD1);
      addKeyword(lKeywords, "printoutprocedure", Token.KEYWORD1);
      addKeyword(lKeywords, "procedure?", Token.KEYWORD1);
      addKeyword(lKeywords, "procedurep", Token.KEYWORD1);
      addKeyword(lKeywords, "procedures", Token.KEYWORD1);
      addKeyword(lKeywords, "product", Token.KEYWORD1);
      addKeyword(lKeywords, "pwd", Token.KEYWORD1);
      addKeyword(lKeywords, "qsort", Token.KEYWORD1);
      addKeyword(lKeywords, "quotient", Token.KEYWORD1);
      addKeyword(lKeywords, "radarccos", Token.KEYWORD1);
      addKeyword(lKeywords, "radarcsin", Token.KEYWORD1);
      addKeyword(lKeywords, "radarctan", Token.KEYWORD1);
      addKeyword(lKeywords, "radarctan2", Token.KEYWORD1);
      addKeyword(lKeywords, "radcos", Token.KEYWORD1);
      addKeyword(lKeywords, "radsin", Token.KEYWORD1);
      addKeyword(lKeywords, "radtan", Token.KEYWORD1);
      addKeyword(lKeywords, "random", Token.KEYWORD1);
      addKeyword(lKeywords, "randomize", Token.KEYWORD1);
      addKeyword(lKeywords, "rc", Token.KEYWORD1);
      addKeyword(lKeywords, "readcharacter", Token.KEYWORD1);
      addKeyword(lKeywords, "reader", Token.KEYWORD1);
      addKeyword(lKeywords, "readlist", Token.KEYWORD1);
      addKeyword(lKeywords, "readword", Token.KEYWORD1);
      addKeyword(lKeywords, "remainder", Token.KEYWORD1);
      addKeyword(lKeywords, "remprop", Token.KEYWORD1);
      addKeyword(lKeywords, "repcount", Token.KEYWORD1);
      addKeyword(lKeywords, "repeat", Token.KEYWORD1);
      addKeyword(lKeywords, "rerandom", Token.KEYWORD1);
      addKeyword(lKeywords, "rl", Token.KEYWORD1);
      addKeyword(lKeywords, "round", Token.KEYWORD1);
      addKeyword(lKeywords, "run", Token.KEYWORD1);
      addKeyword(lKeywords, "runresult", Token.KEYWORD1);
      addKeyword(lKeywords, "rw", Token.KEYWORD1);
      addKeyword(lKeywords, "save", Token.KEYWORD1);
      addKeyword(lKeywords, "se", Token.KEYWORD1);
      addKeyword(lKeywords, "sentence", Token.KEYWORD1);
      addKeyword(lKeywords, "serversocket", Token.KEYWORD1);
      addKeyword(lKeywords, "setname", Token.KEYWORD1);
      addKeyword(lKeywords, "setread", Token.KEYWORD1);
      addKeyword(lKeywords, "setstreampos", Token.KEYWORD1);
      addKeyword(lKeywords, "setwrite", Token.KEYWORD1);
      addKeyword(lKeywords, "shell", Token.KEYWORD1);
      addKeyword(lKeywords, "show", Token.KEYWORD1);
      addKeyword(lKeywords, "show1", Token.KEYWORD1);
      addKeyword(lKeywords, "sin", Token.KEYWORD1);
      addKeyword(lKeywords, "sqrt", Token.KEYWORD1);
      addKeyword(lKeywords, "stop", Token.KEYWORD1);
      addKeyword(lKeywords, "stopthread", Token.KEYWORD1);
      addKeyword(lKeywords, "streamkind", Token.KEYWORD1);
      addKeyword(lKeywords, "streamlength", Token.KEYWORD1);
      addKeyword(lKeywords, "streamname", Token.KEYWORD1);
      addKeyword(lKeywords, "streampos", Token.KEYWORD1);
      addKeyword(lKeywords, "streamrandom?", Token.KEYWORD1);
      addKeyword(lKeywords, "streamrandomp", Token.KEYWORD1);
      addKeyword(lKeywords, "sum", Token.KEYWORD1);
      addKeyword(lKeywords, "tan", Token.KEYWORD1);
      addKeyword(lKeywords, "tell", Token.KEYWORD1);
      addKeyword(lKeywords, "tellall", Token.KEYWORD1);
      addKeyword(lKeywords, "test", Token.KEYWORD1);
      addKeyword(lKeywords, "text", Token.KEYWORD1);
      addKeyword(lKeywords, "thing", Token.KEYWORD1);
      addKeyword(lKeywords, "thread", Token.KEYWORD1);
      addKeyword(lKeywords, "threadapply", Token.KEYWORD1);
      addKeyword(lKeywords, "threadapplyid", Token.KEYWORD1);
      addKeyword(lKeywords, "threadrun", Token.KEYWORD1);
      addKeyword(lKeywords, "threadrunid", Token.KEYWORD1);
      addKeyword(lKeywords, "threadterminate", Token.KEYWORD1);
      addKeyword(lKeywords, "throw", Token.KEYWORD1);
      addKeyword(lKeywords, "to", Token.KEYWORD1);
      addKeyword(lKeywords, "tomacro", Token.KEYWORD1);
      addKeyword(lKeywords, "toplevel", Token.KEYWORD1);
      addKeyword(lKeywords, "true", Token.KEYWORD1);
      addKeyword(lKeywords, "try", Token.KEYWORD1);
      addKeyword(lKeywords, "type", Token.KEYWORD1);
      addKeyword(lKeywords, "unloadprimitives", Token.KEYWORD1);
      addKeyword(lKeywords, "until", Token.KEYWORD1);
      addKeyword(lKeywords, "uppercase", Token.KEYWORD1);
      addKeyword(lKeywords, "wait", Token.KEYWORD1);
      addKeyword(lKeywords, "while", Token.KEYWORD1);
      addKeyword(lKeywords, "who", Token.KEYWORD1);
      addKeyword(lKeywords, "word", Token.KEYWORD1);
      addKeyword(lKeywords, "word?", Token.KEYWORD1);
      addKeyword(lKeywords, "wrap", Token.KEYWORD1);
      addKeyword(lKeywords, "writer", Token.KEYWORD1);
    }
    return lKeywords;
  }

  private static void addKeyword(KeywordMap map, String keyword, byte type)
  {
    map.add(keyword, type);
    String kw;
    keyword = StringUtils.toUpperCase(keyword);
    try {
      kw = getResources().getString(keyword);
    } catch (Exception e) {
      kw = null;
    }
    if (kw == null) {
      try {
        kw = getResources2().getString(keyword);
      } catch (Exception e) {
        kw = null;
      }
    }
    if (kw != null && !StringUtils.toUpperCase(kw).equals(keyword)) {
      map.add(kw, type);
    }
  }

  // private members
  private static KeywordMap lKeywords;

  protected KeywordMap keywords;
  private int lastOffset;
  private int lastKeyword;

  private boolean doKeyword(Segment line, int i, char c)
  {
    int i1 = i + 1;

    int len = i - lastKeyword;
    byte id = keywords.lookup(line, lastKeyword, len);
    if (id != Token.NULL) {
      if (lastKeyword != lastOffset)
        addToken(lastKeyword - lastOffset, Token.NULL);
      addToken(len, id);
      lastOffset = i;
    }
    lastKeyword = i1;
    return false;
  }

}
