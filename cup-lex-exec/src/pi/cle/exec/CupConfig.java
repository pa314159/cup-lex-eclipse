package pi.cle.exec;

import java.io.File;

import java_cup.anttask.CUPTask;

import org.apache.tools.ant.Target;

public class CupConfig
extends AbstractConfig
{
	private final CUPTask	task	= new CUPTask();

	private String			parserSuffix;

	private String			symbolsSuffix;

	private String			parserClass;

	private String			symbolsClass;

	public CupConfig()
	{
		setParserSuffix( "" );
		setSymbolsSuffix( "Symbols" );
		setInterface( true );
	}

	@Override
	public CUPTask createTask( Target target, File source, String destDir ) throws CloneNotSupportedException
	{
		final CUPTask t = (CUPTask) this.task.clone();

		t.setProject( target.getProject() );
		t.setOwningTarget( target );
		t.setSrcfile( source.getAbsolutePath() );
		t.setDestdir( destDir );

		String base = source.getName();

		base = base.substring( 0, base.lastIndexOf( ".cup" ) );

		base = CupLexUtil.toJavaName( base, true );

		if( this.parserClass == null ) {
			t.setParser( base + this.parserSuffix );
		}
		else {
			t.setSymbols( this.parserClass );
		}

		if( this.symbolsClass == null ) {
			t.setSymbols( base + this.symbolsSuffix );
		}
		else {
			t.setSymbols( this.symbolsClass );
		}

		return t;
	}

	public void setCompact_red( boolean argCompact_red )
	{
		this.task.setCompact_red( argCompact_red );
	}

	public void setDebug( boolean argDebug )
	{
		this.task.setDebug( argDebug );
	}

	public void setDump( boolean argDump )
	{
		this.task.setDump( argDump );
	}

	public void setDump_grammar( boolean argDump_grammar )
	{
		this.task.setDump_grammar( argDump_grammar );
	}

	public void setDump_states( boolean argDump_states )
	{
		this.task.setDump_states( argDump_states );
	}

	public void setDump_tables( boolean argDump_tables )
	{
		this.task.setDump_tables( argDump_tables );
	}

	public void setExpect( int expect )
	{
		if( expect > 0 ) {
			this.task.setExpect( Integer.toString( expect ) );
		}
	}

	public void setForce( boolean argforce )
	{
		this.task.setForce( argforce );
	}

	public void setInterface( boolean arg_interface )
	{
		this.task.setInterface( arg_interface );
	}

	public void setNonterms( boolean argNonterms )
	{
		this.task.setNonterms( argNonterms );
	}

	public void setNopositions( boolean argNopositions )
	{
		this.task.setNopositions( argNopositions );
	}

	public void setNoscanner( boolean argNoscanner )
	{
		this.task.setNoscanner( argNoscanner );
	}

	public void setNosummary( boolean argNosummary )
	{
		this.task.setNosummary( argNosummary );
	}

	public void setNowarn( boolean argNowarn )
	{
		this.task.setNowarn( argNowarn );
	}

	/**
	 * @param parserClass
	 *            the parserClass to set
	 */
	public void setParserClass( String parserClass )
	{
		this.parserClass = parserClass;
	}

	public void setParserSuffix( String parserSuffix )
	{
		this.parserSuffix = parserSuffix;
	}

	public void setProgress( boolean argProgress )
	{
		this.task.setProgress( argProgress );
	}

	public void setQuiet( boolean argquiet )
	{
		this.task.setQuiet( argquiet );
	}

	/**
	 * @param symbolsClass
	 *            the symbolsClass to set
	 */
	public void setSymbolsClass( String symbolsClass )
	{
		this.symbolsClass = symbolsClass;
	}

	public void setSymbolsSuffix( String symbolsSuffix )
	{
		this.symbolsSuffix = symbolsSuffix;
	}

	public void setTime( boolean argTime )
	{
		this.task.setTime( argTime );
	}

}
