package jeditSyntax;
/*
 * JavaScriptTokenMarker.java - JavaScript token marker
 * Copyright (C) 1999 Slava Pestov
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

/**
 * JavaScript token marker.
 *
 * @author Slava Pestov
 * @author Kriton Kyrimis
 */
public class JavaScriptTokenMarker extends CTokenMarker
{
	public JavaScriptTokenMarker()
	{
		super(false,getKeywords());
	}

	public static KeywordMap getKeywords()
	{
		if(javaScriptKeywords == null)
		{
			javaScriptKeywords = new KeywordMap(false);
			javaScriptKeywords.add("abstract",Token.KEYWORD1);
			javaScriptKeywords.add("boolean",Token.KEYWORD1);
			javaScriptKeywords.add("break",Token.KEYWORD1);
			javaScriptKeywords.add("byte",Token.KEYWORD1);
			javaScriptKeywords.add("case",Token.KEYWORD1);
			javaScriptKeywords.add("catch",Token.KEYWORD1);
			javaScriptKeywords.add("char",Token.KEYWORD1);
			javaScriptKeywords.add("class",Token.KEYWORD1);
			javaScriptKeywords.add("const",Token.KEYWORD1);
			javaScriptKeywords.add("continue",Token.KEYWORD1);
			javaScriptKeywords.add("debugger",Token.KEYWORD1);
			javaScriptKeywords.add("default",Token.KEYWORD1);
			javaScriptKeywords.add("delete",Token.KEYWORD1);
			javaScriptKeywords.add("do",Token.KEYWORD1);
			javaScriptKeywords.add("double",Token.KEYWORD1);
			javaScriptKeywords.add("else",Token.KEYWORD1);
			javaScriptKeywords.add("enum",Token.KEYWORD1);
			javaScriptKeywords.add("export",Token.KEYWORD1);
			javaScriptKeywords.add("extends",Token.KEYWORD1);
			javaScriptKeywords.add("false",Token.LABEL);
			javaScriptKeywords.add("final",Token.LABEL);
			javaScriptKeywords.add("finally",Token.LABEL);
			javaScriptKeywords.add("float",Token.LABEL);
			javaScriptKeywords.add("for",Token.KEYWORD1);
			javaScriptKeywords.add("function",Token.KEYWORD3);
			javaScriptKeywords.add("goto",Token.KEYWORD3);
			javaScriptKeywords.add("if",Token.KEYWORD1);
			javaScriptKeywords.add("implements",Token.KEYWORD1);
			javaScriptKeywords.add("import",Token.KEYWORD1);
			javaScriptKeywords.add("in",Token.KEYWORD1);
			javaScriptKeywords.add("instanceof",Token.KEYWORD1);
			javaScriptKeywords.add("int",Token.KEYWORD1);
			javaScriptKeywords.add("interface",Token.KEYWORD1);
			javaScriptKeywords.add("long",Token.KEYWORD1);
			javaScriptKeywords.add("native",Token.KEYWORD1);
			javaScriptKeywords.add("new",Token.KEYWORD1);
			javaScriptKeywords.add("null",Token.KEYWORD1);
			javaScriptKeywords.add("package",Token.KEYWORD1);
			javaScriptKeywords.add("private",Token.KEYWORD1);
			javaScriptKeywords.add("protected",Token.KEYWORD1);
			javaScriptKeywords.add("public",Token.KEYWORD1);
			javaScriptKeywords.add("return",Token.KEYWORD1);
			javaScriptKeywords.add("short",Token.KEYWORD1);
			javaScriptKeywords.add("static",Token.KEYWORD1);
			javaScriptKeywords.add("super",Token.KEYWORD1);
			javaScriptKeywords.add("synchronized",Token.KEYWORD1);
			javaScriptKeywords.add("this",Token.LABEL);
			javaScriptKeywords.add("throw",Token.LABEL);
			javaScriptKeywords.add("throws",Token.LABEL);
			javaScriptKeywords.add("transient",Token.LABEL);
			javaScriptKeywords.add("true",Token.LABEL);
			javaScriptKeywords.add("try",Token.LABEL);
			javaScriptKeywords.add("typeof",Token.LABEL);
			javaScriptKeywords.add("var",Token.KEYWORD3);
			javaScriptKeywords.add("void",Token.KEYWORD3);
			javaScriptKeywords.add("volatile",Token.KEYWORD3);
			javaScriptKeywords.add("while",Token.KEYWORD1);
			javaScriptKeywords.add("with",Token.KEYWORD1);

		}
		return javaScriptKeywords;
	}

	// private members
	private static KeywordMap javaScriptKeywords;
}
