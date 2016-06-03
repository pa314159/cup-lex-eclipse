package pi.eclipse.cle.properties;

import static pi.eclipse.cle.preferences.ClePreferences.LEX_CODE_METHOD;
import static pi.eclipse.cle.preferences.ClePreferences.LEX_COMPLY_JLEX;
import static pi.eclipse.cle.preferences.ClePreferences.LEX_OUTPUT;
import static pi.eclipse.cle.preferences.ClePreferences.LEX_SKIP_MIN;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

/**
 */
public class LexPrefs
extends AbstractPref
{
	/**
	 * @param eproject
	 * @param resource
	 */
	public LexPrefs( IProject eproject, IPath resource )
	{
		super( eproject, resource );
	}

	/**
	 * @param resource
	 */
	public LexPrefs( IResource resource )
	{
		super( resource );
	}

	/**
	 * @return
	 */
	public int getCodeMethod()
	{
		return getIntPref( LEX_CODE_METHOD );
	}

	/**
	 * @return
	 */
	public boolean getComply()
	{
		return getBooleanPref( LEX_COMPLY_JLEX );
	}

	/**
	 * @see pi.eclipse.cle.properties.AbstractPref#getJavaFolder()
	 */
	@Override
	public String getJavaFolder()
	{
		return getStringPref( LEX_OUTPUT, getDefaultJavaFolder() );
	}

	/**
	 * @return
	 */
	public boolean getSkipMin()
	{
		return getBooleanPref( LEX_SKIP_MIN );
	}

	/**
	 * @param selection
	 */
	public void setCodeMethod( int codeMethod )
	{
		setPref( LEX_CODE_METHOD, codeMethod );
	}

	/**
	 * @param v
	 */
	public void setComply( boolean v )
	{
		setPref( LEX_COMPLY_JLEX, v );
	}

	/**
	 * @see pi.eclipse.cle.properties.AbstractPref#setJavaFolder(java.lang.String)
	 */
	@Override
	public void setJavaFolder( String v )
	{
		setPref( LEX_OUTPUT, v );
	}

	/**
	 * @param v
	 */
	public void setSkipMin( boolean v )
	{
		setPref( LEX_SKIP_MIN, v );
	}
}
