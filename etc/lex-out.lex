
package pi.eclipse.cle.builders;

import java_cup.runtime.*;

import java.io.*;

import pi.eclipse.cle.CleLog;
import static pi.eclipse.cle.builders.LexOutSymbols.*;

%%

%class LexOutScanner

%unicode
%line
%column

// %public
%final
// %abstract

%cupsym LexOutSymbols
%cup
%cupdebug

%{
	static final CleLog L = new CleLog(LexOutScanner.class);

	private Symbol sym(int type)
	{
		return sym(type, yytext());
	}

	private Symbol sym(int type, Object value)
	{
		L.trace("type = %s, line = %d, column = %d, text = '%s'", getTokenName(type), yyline, yycolumn, value.toString().replace("\n", "\\n"));

		return new Symbol(type, yyline, yycolumn, value);
	}

	private Symbol any()
	{
		return new Symbol(ANY, yyline, yycolumn, yytext());
	}

	private void error()
	throws IOException
	{
		throw new IOException("illegal text at line = "+yyline+", column = "+yycolumn+", text = '"+yytext()+"'");
	}
%}

EOL				=	\r|\n
WARNING			=	"Warning" " "? ": "
WARNING_IN		=	"Warning in "
ERROR			=	"Error" " "? ": "
ERROR_IN		=	"Error in "
NUM				=	"line " [:digit:]+

%state XIN

%%

{WARNING}		{	return sym(WARNING); }
{WARNING_IN}	{	yybegin(XIN); return sym(WARNING_IN); }
{ERROR}			{	return sym(ERROR); }
{ERROR_IN}		{	yybegin(XIN); return sym(ERROR_IN); }

<XIN>
{
	{NUM}		{	return sym(NUM, Integer.valueOf(yytext().substring(5))); }
	
	": "		{	yybegin(YYINITIAL); }
	
	.			{	}
}

{EOL}			{	return sym(EOL); }

.				{	return any(); }
