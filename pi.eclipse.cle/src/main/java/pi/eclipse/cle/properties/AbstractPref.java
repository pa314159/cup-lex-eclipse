package pi.eclipse.cle.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.osgi.service.prefs.BackingStoreException;

import pi.eclipse.cle.ClePlugin;
import pi.eclipse.cle.builders.CupLexNature;
import pi.eclipse.cle.preferences.ClePreferences;

/**
 */
public abstract class AbstractPref
{
	private final IPath					resource;

	private final IProject				eproject;

	private final IJavaProject			jproject;

	private final IEclipsePreferences	preferences;

	/**
	 * @param eproject
	 * @param resource
	 */
	public AbstractPref( IProject eproject, IPath resource )
	{
		this.resource = resource;
		this.eproject = eproject;
		this.jproject = JavaCore.create( eproject );

		final IScopeContext scopeContext = new ProjectScope( eproject );

		CupLexNature.updateProject( this.eproject );

		this.preferences = scopeContext.getNode( ClePlugin.ID );
	}

	/**
	 * @param resource
	 */
	public AbstractPref( IResource resource )
	{
		this( resource.getProject(), resource.getProjectRelativePath() );
	}

	/**
	 * @return
	 */
	public IResource findResource()
	{
		return this.eproject.findMember( this.resource );
	}

	/**
	 * 
	 */
	public void flush()
	{
		try {
			this.preferences.flush();
		}
		catch( final BackingStoreException e ) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	public String getDefaultJavaFolder()
	{
		try {
			final IPackageFragmentRoot[] roots = this.jproject.getAllPackageFragmentRoots();
			for( final IPackageFragmentRoot element : roots ) {
				if( element.getKind() == IPackageFragmentRoot.K_SOURCE ) {
					return element.getResource().getProjectRelativePath().toPortableString();
				}
			}
		}
		catch( final JavaModelException e ) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @return
	 */
	public IProject getEclipseProject()
	{
		return this.eproject;
	}

	/**
	 * @return
	 */
	public abstract String getJavaFolder();

	/**
	 * @return
	 */
	public String getJavaName()
	{
		final IPath p = this.resource.removeFileExtension();

		return ClePlugin.toJavaName( p.lastSegment(), true );
	}

	/**
	 * @return
	 */
	public IJavaProject getJavaProject()
	{
		return this.jproject;
	}

	/**
	 * @return
	 */
	public IPath getResource()
	{
		return this.resource;
	}

	/**
	 * @param v
	 */
	public abstract void setJavaFolder( String v );

	/**
	 * @param k
	 * @param d
	 * @return
	 */
	protected boolean getBooleanPref( ClePreferences e )
	{
		return this.preferences.getBoolean( this.resource + "/" + e.toString(), e.getDefaultBoolean() ); 
	}

	/**
	 * @param k
	 * @return
	 */
	protected int getIntPref( ClePreferences e )
	{
		return this.preferences.getInt( this.resource + "/" + e, e.getDefaultInt() ); 
	}

	/**
	 * @param k
	 * @param v
	 * @return
	 */
	protected String getStringPref( ClePreferences e )
	{
		return this.preferences.get( this.resource + "/" + e, e.getDefaultString() ); 
	}

	/**
	 * @param e
	 * @param v
	 * @return
	 */
	protected String getStringPref( ClePreferences e, String v )
	{
		return this.preferences.get( this.resource + "/" + e, v ); 
	}

	/**
	 * @param string
	 * @param selection
	 */
	protected void setPref( ClePreferences e, boolean v )
	{
		this.preferences.putBoolean( this.resource + "/" + e, v ); 
	}

	/**
	 * @param string
	 * @param selection
	 */
	protected void setPref( ClePreferences e, int v )
	{
		this.preferences.putInt( this.resource + "/" + e, v ); 
	}

	/**
	 * @param k
	 * @param v
	 */
	protected void setPref( ClePreferences e, String v )
	{
		this.preferences.put( this.resource + "/" + e, v ); 
	}
}
