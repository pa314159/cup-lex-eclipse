package pi.eclipse.cle.builders;

import static pi.eclipse.cle.CleVariablesInitializer.VARS.CUP_JAR;
import static pi.eclipse.cle.CleVariablesInitializer.VARS.CUP_RUNTIME_JAR;
import static pi.eclipse.cle.CleVariablesInitializer.VARS.LEX_JAR;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;

import pi.eclipse.cle.CleLog;
import pi.eclipse.cle.ClePlugin;
import pi.eclipse.cle.CleVariablesInitializer.VARS;
import pi.eclipse.cle.properties.AbstractPref;

/**
 */
abstract class AbstractBuilder
extends IncrementalProjectBuilder
implements IJavaLaunchConfigurationConstants, IDebugEventSetListener
{

	/**
	 */
	final class FullBuildVisitor
	implements IResourceVisitor
	{
		final IProgressMonitor	progressMonitor;

		protected FullBuildVisitor( IProgressMonitor progressMonitor )
		{
			this.progressMonitor = progressMonitor;
		}

		/**
		 * @see org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core.resources.IResource)
		 */
		public boolean visit( IResource resource )
		{
			if( resourceAccept( resource ) ) {
				cleanResource( this.progressMonitor, resource );
				buildResource( this.progressMonitor, resource );
			}

			return true;
		}
	}

	/**
	 */
	final class FullCleanVisitor
	implements IResourceVisitor
	{
		final IProgressMonitor	progressMonitor;

		protected FullCleanVisitor( IProgressMonitor progressMonitor )
		{
			this.progressMonitor = progressMonitor;
		}

		/**
		 * @see org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core.resources.IResource)
		 */
		public boolean visit( IResource resource )
		{
			if( resourceAccept( resource ) ) {
				cleanResource( this.progressMonitor, resource );
			}

			return true;
		}
	}

	final class IncrBuildVisitor
	implements IResourceDeltaVisitor
	{
		final IProgressMonitor	progressMonitor;

		protected IncrBuildVisitor( IProgressMonitor progressMonitor )
		{
			this.progressMonitor = progressMonitor;
		}

		/**
		 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
		 */
		public boolean visit( IResourceDelta delta )
		{
			switch( delta.getKind() ) {
				case IResourceDelta.ADDED:
				case IResourceDelta.CHANGED:
					if( resourceAccept( delta.getResource() ) ) {
						cleanResource( this.progressMonitor, delta.getResource() );
						buildResource( this.progressMonitor, delta.getResource() );
					}
			}

			return true;
		}

	}

	final CleLog	L			= new CleLog( getClass() );

	/**  */
	final Map		processData	= new HashMap();

	IJavaProject	javaProject;

	final String	extension;

	public AbstractBuilder( String extension )
	{
		this.extension = extension;
	}

	/**
	 * @see org.eclipse.debug.core.IDebugEventSetListener#handleDebugEvents(org.eclipse.debug.core.DebugEvent[])
	 */
	public void handleDebugEvents( DebugEvent[] events )
	{
		for( int k = 0; k < events.length; k++ ) {
			if( events[k].getKind() != DebugEvent.TERMINATE ) {
				continue;
			}

			if( !(events[k].getSource() instanceof IProcess) ) {
				continue;
			}

			final IProcess p = (IProcess) events[k].getSource();
			final String n = p.getLaunch().getLaunchConfiguration().getName();
			final JavaProcessData d = (JavaProcessData) this.processData.get( n );

			if( d == null ) {
				continue;
			}

			try {
				collectErrors( d.preferences.findResource(), d.out.toCharArray() );
			}
			catch( final IOException e ) {
				addError( d.preferences.findResource(), e.getLocalizedMessage(), 0 );
			}
			finally {
				this.processData.remove( n );

				try {
					d.preferences.getEclipseProject().refreshLocal( IResource.DEPTH_INFINITE, null );
				}
				catch( final CoreException e ) {
				}
			}
		}
	}

	/**
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement,
	 *      java.lang.String, java.lang.Object)
	 */
	@Override
	public void setInitializationData( IConfigurationElement config, String propertyName, Object data )
	throws CoreException
	{
		super.setInitializationData( config, propertyName, data );

		DebugPlugin.getDefault().addDebugEventListener( this );
	}

	/**
	 * @param resource
	 * @param string
	 * @param line
	 */
	protected void addError( IResource resource, String string, int line )
	{
		addMarker( IMarker.SEVERITY_ERROR, resource, string, line );
	}

	/**
	 * @param severity
	 * @param resource
	 * @param string
	 * @param line
	 */
	protected void addMarker( int severity, IResource resource, String string, int line )
	{
		try {
			final IMarker marker = resource.createMarker( markerType() );

			marker.setAttribute( IMarker.MESSAGE, string );
			marker.setAttribute( IMarker.SEVERITY, severity );

			if( line <= 0 ) {
				line = 1;
			}

			marker.setAttribute( IMarker.LINE_NUMBER, line );
		}
		catch( final CoreException e ) {
		}
	}

	/**
	 * @param resource
	 * @param string
	 * @param line
	 */
	protected void addWarning( IResource resource, String string, int line )
	{
		addMarker( IMarker.SEVERITY_WARNING, resource, string, line );
	}

	/**
	 * @see IncrementalProjectBuilder#build(int, java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IProject[] build( int kind, Map args, IProgressMonitor monitor ) throws CoreException
	{
		updateExclusions();

		if( kind == FULL_BUILD ) {
			fullBuild( monitor );
		}
		else {
			final IResourceDelta delta = getDelta( getProject() );
			if( delta == null ) {
				fullBuild( monitor );
			}
			else {
				incrBuild( delta, monitor );
			}
		}

		getProject().refreshLocal( IResource.DEPTH_INFINITE, monitor );

		return null;
	}

	/**
	 * @see org.eclipse.core.internal.events.InternalBuilder#clean(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void clean( IProgressMonitor progressMonitor ) throws CoreException
	{
		getProject().accept( new FullCleanVisitor( progressMonitor ) );
	}

	/**
	 * @param progressMonitor
	 * @param resource
	 * @throws IOException
	 */
	protected abstract void cleanJavaSource( IProgressMonitor progressMonitor, IResource resource ) throws IOException;

	/**
	 * @param progressMonitor
	 *            TODO
	 * @param resource
	 * @throws IOException
	 *             TODO
	 * @throws CoreException
	 */
	protected abstract void createJavaSource( IProgressMonitor progressMonitor, IResource resource )
	throws IOException, CoreException;

	/**
	 * Peek into .cup/.lex file to get the package and class name
	 * 
	 * @return
	 * @throws IOException
	 *             if there is a problem reading the file
	 */
	protected String[] findPackageAndClass( File file ) throws IOException
	{
		final String[] result = new String[2];

		final BufferedReader reader = new BufferedReader( new FileReader( file ) );

		while( (result[0] == null) || (result[1] == null) ) {
			final String line = reader.readLine();
			if( line == null ) {
				break;
			}

			if( result[0] == null ) {
				int index = line.indexOf( "package" ); 
				if( index >= 0 ) {
					index += 7;

					final int end = line.indexOf( ';', index );
					if( end >= index ) {
						result[0] = line.substring( index, end );
						result[0] = result[0].trim();
					}
				}
			}

			if( result[1] == null ) {
				int index = line.indexOf( "%class" ); 
				if( index >= 0 ) {
					index += 6;

					result[1] = line.substring( index );
					result[1] = result[1].trim();
				}
			}
		}

		return result;
	}

	/**
	 * @return
	 */
	protected String markerType()
	{
		return ClePlugin.ID + "." + getClass().getSimpleName(); 
	}

	/**
	 * @see org.eclipse.core.resources.IncrementalProjectBuilder#startupOnInitialize()
	 */
	@Override
	protected void startupOnInitialize()
	{
		super.startupOnInitialize();

		this.javaProject = JavaCore.create( getProject() );

	}

	void buildResource( IProgressMonitor progressMonitor, IResource resource )
	{
		this.L.debug( "BUILDING %s", resource.getFullPath() ); 

		try {
			createJavaSource( progressMonitor, resource );
		}
		catch( final Exception e ) {
			addError( resource, e.getLocalizedMessage(), 0 );
		}
	}

	void cleanResource( IProgressMonitor progressMonitor, IResource resource )
	{
		this.L.debug( "CLEANING %s", resource.getFullPath() ); 

		try {
			resource.deleteMarkers( markerType(), true, IResource.DEPTH_INFINITE );
		}
		catch( final CoreException e ) {
		}

		try {
			cleanJavaSource( progressMonitor, resource );
		}
		catch( final Exception e ) {
			addError( resource, e.getLocalizedMessage(), 0 );
		}
	}

	abstract void collectErrors( IResource resource, char[] output ) throws IOException;

	/**
	 * @param string
	 * @param project
	 * @return
	 */
	IRuntimeClasspathEntry findJar( VARS variable )
	{
		final IRuntimeClasspathEntry vcpe = JavaRuntime.newVariableRuntimeClasspathEntry( variable.path );

		vcpe.setClasspathProperty( IRuntimeClasspathEntry.VARIABLE );

		return vcpe;
	}

	/**
	 * @param monitor
	 * @throws CoreException
	 */
	void fullBuild( IProgressMonitor progressMonitor ) throws CoreException
	{
		getProject().accept( new FullBuildVisitor( progressMonitor ) );
	}

	/**
	 * @param delta
	 * @param monitor
	 * @throws CoreException
	 */
	void incrBuild( IResourceDelta delta, IProgressMonitor progressMonitor ) throws CoreException
	{
		delta.accept( new IncrBuildVisitor( progressMonitor ) );
	}

	void launchJava( IProgressMonitor progressMonitor, AbstractPref preferences, Class className, String arguments )
	throws CoreException
	{
		final ILaunchManager man = DebugPlugin.getDefault().getLaunchManager();
		final ILaunchConfigurationType typ = man.getLaunchConfigurationType( ID_JAVA_APPLICATION );
		final IPath lwp = preferences.getEclipseProject().getFullPath().append( preferences.getResource() );
		final String lwn = lwp.toPortableString();
		final ILaunchConfigurationWorkingCopy lwc = typ.newInstance( preferences.getEclipseProject(), lwn );
		final IVMInstall jre = JavaRuntime.getVMInstall( preferences.getJavaProject() );
		final JavaProcessData rpd = new JavaProcessData( preferences );

		updateClassPath( preferences.getJavaProject() );

		final IRuntimeClasspathEntry cup = findJar( CUP_JAR );
		final IRuntimeClasspathEntry lex = findJar( LEX_JAR );

		final List<String> jcp = new ArrayList<String>();

		jcp.add( cup.getMemento() );
		jcp.add( lex.getMemento() );

		lwc.setAttribute( ATTR_ALLOW_TERMINATE, false );
		lwc.setAttribute( ATTR_VM_INSTALL_NAME, jre.getName() );
		lwc.setAttribute( ATTR_VM_INSTALL_TYPE, jre.getVMInstallType().getId() );
		lwc.setAttribute( ATTR_MAIN_TYPE_NAME, className.getName() );
		lwc.setAttribute( ATTR_DEFAULT_CLASSPATH, false );
		lwc.setAttribute( ATTR_CLASSPATH, jcp );
		lwc.setAttribute( ATTR_PROGRAM_ARGUMENTS, arguments );

		this.processData.put( lwn, rpd );

		this.L.debug( "LAUNCH %s %s", className, arguments ); 

		try {
			final ILaunch lan = lwc.launch( ILaunchManager.RUN_MODE, progressMonitor );
			final IProcess[] run = lan.getProcesses();
			final IStreamsProxy pxy = run[0].getStreamsProxy();
			IStreamMonitor mon = null;

			mon = pxy.getOutputStreamMonitor();

			if( mon != null ) {
				mon.addListener( rpd );
			}

			mon = pxy.getErrorStreamMonitor();

			if( mon != null ) {
				mon.addListener( rpd );
			}
		}
		finally {
			lwc.delete();
		}
	}

	final boolean resourceAccept( IResource resource )
	{
		if( !this.extension.equalsIgnoreCase( resource.getFileExtension() ) ) {
			return false;
		}

		try {
			for( final IClasspathEntry e : this.javaProject.getResolvedClasspath( true ) ) {
				if( e.getEntryKind() != IClasspathEntry.CPE_SOURCE ) {
					continue;
				}

				System.out.println( e.getExclusionPatterns() );

				final IPath outputLocation = e.getOutputLocation();

				if( outputLocation == null ) {
					continue;
				}

				if( outputLocation.isPrefixOf( resource.getFullPath() ) ) {
					return false;
				}
			}
		}
		catch( final JavaModelException e ) {
			e.printStackTrace();
		}

		try {
			if( this.javaProject.getOutputLocation().isPrefixOf( resource.getFullPath() ) ) {
				return false;
			}
		}
		catch( final JavaModelException e ) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param javaProject
	 * @throws CoreException
	 */
	void updateClassPath( IJavaProject project ) throws CoreException
	{
		final boolean needCup = project.findType( java_cup.runtime.Symbol.class.getName() ) == null;

		if( needCup ) {
			final IClasspathEntry[] cpes = project.getRawClasspath();
			IClasspathEntry[] newCpes = null;
			final int count = cpes.length;

			newCpes = new IClasspathEntry[count + 1];

			System.arraycopy( cpes, 0, newCpes, 0, count );

			newCpes[count] = JavaCore.newVariableEntry( CUP_RUNTIME_JAR.path, null, null );

			project.setRawClasspath( newCpes, null );
		}
	}

	void updateExclusions() throws JavaModelException
	{
		final IClasspathEntry[] oldCp = this.javaProject.getRawClasspath();
		final IClasspathEntry[] newCp = new IClasspathEntry[oldCp.length];

		for( int k = 0; k < oldCp.length; k++ ) {
			final IClasspathEntry oldEnt = oldCp[k];

			if( oldEnt.getEntryKind() != IClasspathEntry.CPE_SOURCE ) {
				newCp[k] = oldEnt;

				continue;
			}

			final IPath cupEx = new Path( "**/*.cup" );
			final IPath lexEx = new Path( "**/*.lex" );

			final Set<IPath> ex = new HashSet<IPath>( Arrays.asList( oldEnt.getExclusionPatterns() ) );

			if( ex.contains( cupEx ) && ex.contains( lexEx ) ) {
				newCp[k] = oldEnt;

				continue;
			}

			ex.add( cupEx );
			ex.add( lexEx );

			final IClasspathEntry newEnt = JavaCore.newSourceEntry( oldEnt.getPath(), oldEnt.getInclusionPatterns(),
				ex.toArray( new IPath[0] ), oldEnt.getOutputLocation(), oldEnt.getExtraAttributes() );

			newCp[k] = newEnt;
		}

		this.javaProject.setRawClasspath( newCp, true, null );
	}

}
