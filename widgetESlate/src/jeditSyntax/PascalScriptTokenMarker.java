package jeditSyntax;
/*
 * CTokenMarker.java - Pascal Script token marker
 * Copyright (C) 1998, 1999 Slava Pestov
 * Copyright (C) 2006 Kriton Kyrimis
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

import javax.swing.text.Segment;

/**
 * Pascal Script token marker.
 *
 * @author Slava Pestov
 * @author Kriton Kyrimis
 */
public class PascalScriptTokenMarker extends TokenMarker
{
	public PascalScriptTokenMarker()
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

loop:		for(int i = offset; i < length; i++)
		{
			int i1 = (i+1);

			char c = array[i];

			switch(token)
			{
			case Token.NULL:
				switch(c)
				{
				case '\'':
					doKeyword(line,i,c);
                                        addToken(i - lastOffset,token);
                                        token = Token.LITERAL1;
                                        lastOffset = lastKeyword = i;
					break;
				case ':':
					if(lastKeyword == offset)
					{
						if(doKeyword(line,i,c))
							break;
						addToken(i1 - lastOffset,Token.LABEL);
						lastOffset = lastKeyword = i1;
					}
					else if(doKeyword(line,i,c))
						break;
					break;
				case '/':
					doKeyword(line,i,c);
					if(length - i > 1)
					{
                                                switch (array[i1]) {
						case '/':
							addToken(i - lastOffset,token);
							addToken(length - i,Token.COMMENT1);
							lastOffset = lastKeyword = length;
							break loop;
						}
					}
					break;
				case '{':
					doKeyword(line,i,c);
                                        addToken(i - lastOffset,token);
                                        lastOffset = lastKeyword = i;
                                        token = Token.COMMENT2;
					break;
				default:
					if(!Character.isLetterOrDigit(c) && c != '_')
						doKeyword(line,i,c);
					break;
				}
				break;
			case Token.COMMENT2:
				if(c == '}')
				{
						addToken(i+1 - lastOffset,token);
						token = Token.NULL;
						lastOffset = lastKeyword = i+1;
				}
				break;
			case Token.LITERAL1:
				if(c == '\'')
				{
					addToken(i1 - lastOffset,token);
					token = Token.NULL;
					lastOffset = lastKeyword = i1;
				}
				break;
			default:
				throw new InternalError("Invalid state: "
					+ token);
			}
		}

		if(token == Token.NULL)
			doKeyword(line,length,'\0');

		switch(token)
		{
		case Token.LITERAL1:
			addToken(length - lastOffset,Token.INVALID);
			token = Token.NULL;
			break;
		case Token.KEYWORD2:
			addToken(length - lastOffset,token);
                        token = Token.NULL;
		default:
			addToken(length - lastOffset,token);
			break;
		}

		return token;
	}

	public static KeywordMap getKeywords()
	{
		if(psKeywords == null)
		{
			psKeywords = new KeywordMap(true);

			psKeywords.add("#include",Token.KEYWORD1);
			psKeywords.add("begin",Token.KEYWORD1);
			psKeywords.add("break",Token.KEYWORD1);
			psKeywords.add("case",Token.KEYWORD1);
			psKeywords.add("catch",Token.KEYWORD1);
			psKeywords.add("continue",Token.KEYWORD1);
			psKeywords.add("do",Token.KEYWORD1);
			psKeywords.add("downto",Token.KEYWORD1);
			psKeywords.add("else",Token.KEYWORD1);
			psKeywords.add("end",Token.KEYWORD1);
			psKeywords.add("exit",Token.KEYWORD1);
			psKeywords.add("finally",Token.KEYWORD1);
			psKeywords.add("for",Token.KEYWORD1);
			psKeywords.add("function",Token.KEYWORD1);
			psKeywords.add("if",Token.KEYWORD1);
			psKeywords.add("of",Token.KEYWORD1);
			psKeywords.add("repeat",Token.KEYWORD1);
			psKeywords.add("then",Token.KEYWORD1);
			psKeywords.add("to",Token.KEYWORD1);
			psKeywords.add("try",Token.KEYWORD1);
			psKeywords.add("until",Token.KEYWORD1);
			psKeywords.add("uses",Token.KEYWORD1);
			psKeywords.add("while",Token.KEYWORD1);

			psKeywords.add("array",Token.KEYWORD3);
			psKeywords.add("boolean",Token.KEYWORD3);
			psKeywords.add("byte",Token.KEYWORD3);
			psKeywords.add("cardinal",Token.KEYWORD3);
			psKeywords.add("char",Token.KEYWORD3);
			psKeywords.add("double",Token.KEYWORD3);
			psKeywords.add("enumerations",Token.KEYWORD3);
			psKeywords.add("extended",Token.KEYWORD3);
			psKeywords.add("integer",Token.KEYWORD3);
			psKeywords.add("longint",Token.KEYWORD3);
			psKeywords.add("real",Token.KEYWORD3);
			psKeywords.add("record",Token.KEYWORD3);
			psKeywords.add("shortint",Token.KEYWORD3);
			psKeywords.add("single",Token.KEYWORD3);
			psKeywords.add("smallint",Token.KEYWORD3);
			psKeywords.add("string",Token.KEYWORD3);
			psKeywords.add("var",Token.KEYWORD3);
			psKeywords.add("variant",Token.KEYWORD3);
			psKeywords.add("word",Token.KEYWORD3);

			psKeywords.add("idispatch",Token.KEYWORD2);
			psKeywords.add("iunknown",Token.KEYWORD2);

			psKeywords.add("false",Token.LITERAL2);
			psKeywords.add("nil",Token.LITERAL2);
			psKeywords.add("true",Token.LITERAL2);

		}
		return psKeywords;
	}

	// private members
	private static KeywordMap psKeywords;

	private KeywordMap keywords;
	private int lastOffset;
	private int lastKeyword;

	private boolean doKeyword(Segment line, int i, char c)
	{
		int i1 = i+1;

		int len = i - lastKeyword;
		byte id = keywords.lookup(line,lastKeyword,len);
		if(id != Token.NULL)
		{
			if(lastKeyword != lastOffset)
				addToken(lastKeyword - lastOffset,Token.NULL);
			addToken(len,id);
			lastOffset = i;
		}
		lastKeyword = i1;
		return false;
	}
}
