package pi.eclipse.cle.properties;

import static pi.eclipse.cle.preferences.ClePreferences.CUP_COMPACT_RED;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_DEBUG;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_EXPECTED_CONFLICTS;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_NON_TERMS;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_NO_POSITIONS;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_NO_SCANNER;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_OUTPUT;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_PARSER_CLASS;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_SYMBOLS_CLASS;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_SYMBOLS_IF;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class CupPrefs
extends AbstractPref
{

	/**
	 * @param eproject
	 * @param resource
	 */
	public CupPrefs( IProject eproject, IPath resource )
	{
		super( eproject, resource );
	}

	/**
	 * @param resource
	 */
	public CupPrefs( IResource resource )
	{
		super( resource );
	}

	/**
	 * @return
	 */
	public boolean getCompactRed()
	{
		return getBooleanPref( CUP_COMPACT_RED );
	}

	/**
	 * @return
	 */
	public boolean getDebug()
	{
		return getBooleanPref( CUP_DEBUG );
	}

	/**
	 * @return
	 */
	public String getDefaultParserClass()
	{
		return getJavaName() + "Cup"; //$NON-NLS-1$
	}

	/**
	 * @return
	 */
	public String getDefaultSymbolsClass()
	{
		return getJavaName() + "Sym"; //$NON-NLS-1$
	}

	public int getExpectedConflicts()
	{
		return getIntPref( CUP_EXPECTED_CONFLICTS );
	}

	/**
	 * @see pi.eclipse.cle.properties.AbstractPref#getJavaFolder()
	 */
	@Override
	public String getJavaFolder()
	{
		return getStringPref( CUP_OUTPUT, getDefaultJavaFolder() );
	}

	/**
	 * @return
	 */
	public boolean getNonTerms()
	{
		return getBooleanPref( CUP_NON_TERMS );
	}

	/**
	 * @return
	 */
	public boolean getNoPositions()
	{
		return getBooleanPref( CUP_NO_POSITIONS );
	}

	/**
	 * @return
	 */
	public boolean getNoScanner()
	{
		return getBooleanPref( CUP_NO_SCANNER );
	}

	/**
	 * @return
	 */
	public String getParserClass()
	{
		return getStringPref( CUP_PARSER_CLASS, getDefaultParserClass() );
	}

	/**
	 * @return
	 */
	public String getSymbolsClass()
	{
		return getStringPref( CUP_SYMBOLS_CLASS, getDefaultSymbolsClass() );
	}

	/**
	 * @return
	 */
	public boolean getSymbolsIf()
	{
		return getBooleanPref( CUP_SYMBOLS_IF );
	}

	/**
	 * @param b
	 */
	public void setCompactRed( boolean b )
	{
		setPref( CUP_COMPACT_RED, b );
	}

	/**
	 * @param b
	 */
	public void setDebug( boolean b )
	{
		setPref( CUP_DEBUG, b );
	}

	/**
	 * @param selection
	 */
	public void setExpectedConflicts( int v )
	{
		setPref( CUP_EXPECTED_CONFLICTS, v );
	}

	/**
	 * @see pi.eclipse.cle.properties.AbstractPref#setJavaFolder(java.lang.String)
	 */
	@Override
	public void setJavaFolder( String v )
	{
		setPref( CUP_OUTPUT, v );
	}

	/**
	 * @param b
	 */
	public void setNonTerms( boolean b )
	{
		setPref( CUP_NON_TERMS, b );
	}

	/**
	 * @param b
	 */
	public void setNoPositions( boolean b )
	{
		setPref( CUP_NO_POSITIONS, b );
	}

	/**
	 * @param b
	 */
	public void setNoScanner( boolean b )
	{
		setPref( CUP_NO_SCANNER, b );
	}

	/**
	 * @param v
	 */
	public void setParserClass( String v )
	{
		setPref( CUP_PARSER_CLASS, v );
	}

	/**
	 * @param v
	 */
	public void setSymbolsClass( String v )
	{
		setPref( CUP_SYMBOLS_CLASS, v );
	}

	/**
	 * @param b
	 */
	public void setSymbolsIf( boolean b )
	{
		setPref( CUP_SYMBOLS_IF, b );
	}
}
