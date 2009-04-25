package pi.eclipse.cle.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import pi.eclipse.cle.ClePlugin;
import pi.eclipse.cle.CleStrings;

/**
 * Constant definitions for plug-in preferences.
 * 
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public enum ClePreferences
{
	/** */
	LEX_OUTPUT,

	/** */
	LEX_SKIP_MIN,

	/** */
	LEX_COMPLY_JLEX,

	/** */
	LEX_CODE_METHOD,

	/** */
	LEX_DUMP,

	/** */
	LEX_QUIET,

	/** */
	CUP_OUTPUT,

	/** */
	CUP_DEBUG,

	/** */
	CUP_DUMP_ALL,

	/** */
	CUP_DUMP_GRAMMAR,

	/** */
	CUP_DUMP_STATES,

	/** */
	CUP_DUMP_TABLES,

	/** */
	CUP_PARSER_CLASS,

	/** */
	CUP_SYMBOLS_CLASS,

	/** */
	CUP_SYMBOLS_IF,

	/** */
	CUP_EXPECTED_CONFLICTS,

	/** */
	CUP_NON_TERMS,

	/** */
	CUP_COMPACT_RED,

	/** */
	CUP_NO_POSITIONS,

	/** */
	CUP_NO_SCANNER,

	/** */
	;

	/** */
	static final IPreferenceStore	STORE	= ClePlugin.getDefault().getPreferenceStore();

	/** */
	private final String			string;

	/** */
	private final String			label;

	/** */
	private ClePreferences()
	{
		String x = name().toLowerCase();

		x = x.replace( '_', '-' );

		this.string = x;

		this.label = "preference-" + x; //$NON-NLS-1$
	}

	/**
	 * @return
	 */
	public boolean getBoolean()
	{
		return STORE.getBoolean( toString() );
	}

	/**
	 * @return
	 */
	public boolean getDefaultBoolean()
	{
		return STORE.getDefaultBoolean( toString() );
	}

	/**
	 * @return
	 */
	public int getDefaultInt()
	{
		return STORE.getDefaultInt( toString() );
	}

	/**
	 * @return
	 */
	public String getDefaultString()
	{
		return STORE.getDefaultString( toString() );
	}

	/**
	 * @return
	 */
	public String getString()
	{
		return STORE.getString( toString() );
	}

	/**
	 * @param b
	 */
	public void set( boolean value )
	{
		STORE.setValue( toString(), value );
	}

	/**
	 * @param value
	 */
	public void set( String value )
	{
		STORE.setValue( toString(), value );
	}

	/**
	 * @param b
	 */
	public void setDefault( boolean value )
	{
		STORE.setDefault( toString(), value );
	}

	/**
	 * @param value
	 */
	public void setDefault( String value )
	{
		STORE.setDefault( toString(), value );
	}

	/**
	 * @return
	 */
	public String toLabel()
	{
		return CleStrings.get( this.label );
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.string;
	}
}
