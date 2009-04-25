package pi.eclipse.cle;

import java.io.File;
import java.net.URL;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class ClePlugin
extends AbstractUIPlugin
{

	/**  */
	private static final Class		CLASS	= ClePlugin.class;

	/**	 */
	public static final String		ID		= "pi.eclipse.cle";	//$NON-NLS-1$

	/**	 */
	private static ClePlugin		PLUGIN;

	/**	 */
	private static BundleContext	CONTEXT;

	/**	 */
	private static final CleLog		L		= new CleLog( CLASS );

	/**
	 * @param path
	 * @return
	 */
	public static URL findEntry( IPath path )
	{
		return getDefault().getBundle().getEntry( path.toPortableString() );
	}

	/**
	 * @param fileName
	 * @return
	 */
	public static File getDataFile( String fileName )
	{
		return CONTEXT.getDataFile( fileName );
	}

	/**
	 * Returns the shared instance.
	 */
	public static ClePlugin getDefault()
	{
		return PLUGIN;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor( String image )
	{
		return AbstractUIPlugin.imageDescriptorFromPlugin( ID, "/icons/" + image + ".gif" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @param project
	 * @param folder
	 * @return
	 */
	public static boolean isJavaFolder( IProject project, String folder )
	{
		final IResource resource = project.findMember( folder );
		final IJavaElement element = JavaCore.create( resource );

		if( element instanceof IPackageFragmentRoot ) {
			final IPackageFragmentRoot fragment = (IPackageFragmentRoot) element;

			try {
				if( fragment.getKind() == IPackageFragmentRoot.K_SOURCE ) {
					if( resource instanceof IContainer ) {
						return true;
					}
				}
			}
			catch( final JavaModelException e ) {
				;
			}
		}

		return false;
	}

	/**
	 * @return
	 */
	public static boolean isLogEnabled()
	{
		return PLUGIN == null || PLUGIN != null && PLUGIN.LOG4J;
	}

	/**
	 * @param shell
	 * @return
	 */
	public static IPath selectFolder( Shell shell )
	{
		IPath value = null;
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final ContainerSelectionDialog dialog = new ContainerSelectionDialog( shell, root, true, CleStrings
			.get( "select-folder" ) ); //$NON-NLS-1$

		if( dialog.open() == Window.OK ) {
			final Object[] result = dialog.getResult();
			if( result.length == 1 ) {
				value = (IPath) result[0];
			}
		}

		return value;
	}

	/**
	 * @param shell
	 * @param project
	 * @return
	 */
	public static IContainer selectJavaFolder( Shell shell, IProject project )
	{
		final StandardJavaElementContentProvider provider = new StandardJavaElementContentProvider();
		final ILabelProvider labelProvider = new JavaElementLabelProvider( JavaElementLabelProvider.SHOW_DEFAULT );
		final ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog( shell, labelProvider, provider );

		final ViewerFilter filter = new ViewerFilter()
			{

				@Override
				public boolean select( Viewer viewer, Object parentElement, Object element )
				{
					if( element instanceof IPackageFragmentRoot ) {
						try {
							return ((IPackageFragmentRoot) element).getKind() == IPackageFragmentRoot.K_SOURCE;
						}
						catch( JavaModelException e ) {
						}
					}

					return false;
				}
			};

		final IJavaProject jproject = JavaCore.create( project );
		dialog.addFilter( filter );
		dialog.setInput( jproject );

		if( dialog.open() == Window.OK ) {
			final Object element = dialog.getFirstResult();
			if( element instanceof IPackageFragmentRoot ) {
				final IPackageFragmentRoot root = (IPackageFragmentRoot) element;

				return (IContainer) root.getResource();
			}
		}

		return null;
	}

	/**
	 * @param shell
	 * @param source
	 * @return
	 */
	public static IPackageFragment selectJavaPackage( Shell shell, IResource source )
	{
		final IPackageFragmentRoot element = (IPackageFragmentRoot) JavaCore.create( source );
		IJavaElement[] packages = null;

		try {
			if( element != null && element.exists() ) {
				packages = element.getChildren();
			}
		}
		catch( final JavaModelException e ) {
			JavaPlugin.log( e );
		}

		if( packages == null ) {
			packages = new IJavaElement[0];
		}

		final LabelProvider jelp = new JavaElementLabelProvider( JavaElementLabelProvider.SHOW_DEFAULT );
		final ElementListSelectionDialog dialog = new ElementListSelectionDialog( shell, jelp );

		dialog.setIgnoreCase( false );
		dialog.setTitle( NewWizardMessages.NewTypeWizardPage_ChoosePackageDialog_title );
		dialog.setMessage( NewWizardMessages.NewTypeWizardPage_ChoosePackageDialog_description );
		dialog.setEmptyListMessage( NewWizardMessages.NewTypeWizardPage_ChoosePackageDialog_empty );
		dialog.setElements( packages );

		if( dialog.open() == Window.OK ) {
			return (IPackageFragment) dialog.getFirstResult();
		}

		return null;
	}

	/**
	 * Converts the given xml name to a Java name.
	 * 
	 * @param name
	 *            the name to convert to a Java Name
	 * @param upperFirst
	 *            a flag to indicate whether or not the the first character should be converted to uppercase.
	 */
	public static String toJavaName( String name, boolean upperFirst )
	{
		final int size = name.length();
		final char[] ncChars = name.toCharArray();
		int next = 0;
		boolean uppercase = upperFirst;
		boolean lowercase = !uppercase;

		if( size > 1 && lowercase ) {
			if( Character.isUpperCase( ncChars[0] ) && Character.isUpperCase( ncChars[1] ) ) {
				lowercase = false;
			}
		}

		for( int i = 0; i < size; i++ ) {
			final char ch = ncChars[i];

			switch( ch ) {
				case '.':
				case ' ':
					ncChars[next++] = '_';
					break;
				case ':':
				case '-':
					uppercase = true;
					break;
				case '_':
					uppercase = true;
					ncChars[next] = ch;
					++next;
					break;
				default:
					if( uppercase ) {
						ncChars[next] = Character.toUpperCase( ch );
						uppercase = false;
					}
					else if( lowercase ) {
						ncChars[next] = Character.toLowerCase( ch );
						lowercase = false;
					}
					else {
						ncChars[next] = ch;
					}
					++next;
					break;
			}
		}

		return new String( ncChars, 0, next );
	}

	/**	 */
	private boolean	LOG4J;

	/**
	 * This method is called upon plug-in activation
	 */
	@Override
	public void start( BundleContext context ) throws Exception
	{
		this.LOG4J = Boolean.valueOf( Platform.getDebugOption( ID + "/LOG4J" ) ); //$NON-NLS-1$

		// if( LOG4J ) {
		// URL u = CLASS.getResource("/log4j.xml"); //$NON-NLS-1$
		// if( u != null ) {
		// DOMConfigurator.configure(u);
		// }
		// else {
		// System.err.println("cannot find LOG4J configuration file"); //$NON-NLS-1$
		// LOG4J = false;
		// }
		// }

		super.start( context );

		ClePlugin.PLUGIN = this;
		ClePlugin.CONTEXT = context;

		L.debug( "started plugin %s", ID ); //$NON-NLS-1$

		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IPathVariableManager pathMan = workspace.getPathVariableManager();

		final String[] a = pathMan.getPathVariableNames();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
	public void stop( BundleContext context ) throws Exception
	{
		super.stop( context );

		ClePlugin.PLUGIN = null;
		ClePlugin.CONTEXT = null;

		L.debug( "stopped plugin %s", ID ); //$NON-NLS-1$
	}

}
