
package pi.eclipse.cle.builders;

import java_cup.runtime.*;

import java.io.*;

import pi.eclipse.cle.CleLog;
import static pi.eclipse.cle.builders.CupOutSym.*;

%%

%class CupOutLex

%unicode
%line
%column

// %public
%final
// %abstract

%cupsym CupOutSym
%cup
// %cupdebug

%{
	static final CleLog L = new CleLog(CupOutLex.class);

	private Symbol sym(int type)
	{
		return sym(type, yytext());
	}

	private Symbol sym(int type, Object value)
	{
//		L.trace("type = %d, line = %d, column = %d, text = '%s'", type, yyline, yycolumn, value.toString().replace("\n", "\\n"));

		return new Symbol(type, yyline, yycolumn, value);
	}

	private void error()
	throws IOException
	{
		throw new IOException("illegal text at line = "+yyline+", column = "+yycolumn+", text = '"+yytext()+"'");
	}
%}

WARNING			=	"Warning" " "? ": "
WARNING_AT		=	"Warning at "
ERROR			=	"Error" " "? ": "
ERROR_AT		=	"Error at "
FATAL			=	"Fatal" " "? ": "
FATAL_AT		=	"Fatal at "
NUM				=	-? [:digit:]+

%state			XSTR
%state			XSYM

%%

{WARNING}		{	yybegin(XSTR); return sym(WARNING); }
{WARNING_AT}	{	yybegin(XSTR); return sym(WARNING_AT); }
{ERROR}			{	yybegin(XSTR); return sym(ERROR); }
{ERROR_AT}		{	yybegin(XSTR); return sym(ERROR_AT); }
{FATAL}			{	yybegin(XSTR); return sym(FATAL); }
{FATAL_AT}		{	yybegin(XSTR); return sym(FATAL_AT); }

<XSTR>
{
	"("			{	}
	{NUM}		{	return sym(NUM, Integer.valueOf(yytext())); }
	"/"			{	}
	")"			{	yybegin(XSYM); }
}

<XSYM>
{
	.			{	}
	": "		{	yybegin(YYINITIAL); }
}

.				{	return sym(ANY); }
[\r\n]+			{	yybegin(YYINITIAL); return sym(EOL); }
