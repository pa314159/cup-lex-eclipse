package pi.eclipse.cle.builders;

import static pi.eclipse.cle.CleVariablesInitializer.VARS.CUP_JAR;
import static pi.eclipse.cle.CleVariablesInitializer.VARS.CUP_RUNTIME_JAR;
import static pi.eclipse.cle.CleVariablesInitializer.VARS.LEX_JAR;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;

import pi.eclipse.cle.CleLog;
import pi.eclipse.cle.ClePlugin;
import pi.eclipse.cle.CleVariablesInitializer.VARS;
import pi.eclipse.cle.properties.AbstractPref;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
abstract class AbstractBuilder
extends IncrementalProjectBuilder
implements IJavaLaunchConfigurationConstants, IDebugEventSetListener
{

	/**
	 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
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
			if( resourceMatches( resource ) ) {
				cleanResource( this.progressMonitor, resource );
				buildResource( this.progressMonitor, resource );
			}

			return true;
		}
	}

	/**
	 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
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
			if( resourceMatches( resource ) ) {
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
					if( resourceMatches( delta.getResource() ) ) {
						cleanResource( this.progressMonitor, delta.getResource() );
						buildResource( this.progressMonitor, delta.getResource() );
					}
			}

			return true;
		}

	}

	/**
	 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
	 */
	final class ProcessData
	implements IStreamListener
	{
		final AbstractPref		preferences;

		final CharArrayWriter	out	= new CharArrayWriter();

		ProcessData( AbstractPref preferences )
		{
			this.preferences = preferences;
		}

		public void streamAppended( String text, IStreamMonitor monitor )
		{
			// L.trace("PROCESS: %s", text.replace("\n", "\\n")); //$NON-NLS-1$
			// //$NON-NLS-2$ //$NON-NLS-3$

			System.out.print( text );
			System.out.flush();

			this.out.append( text );
		}
	}

	final CleLog	L			= new CleLog( getClass() );

	/**  */
	final Map		processData	= new HashMap();

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
			final ProcessData d = (ProcessData) this.processData.get( n );

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
				int index = line.indexOf( "package" ); //$NON-NLS-1$
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
				int index = line.indexOf( "%class" ); //$NON-NLS-1$
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
		return ClePlugin.ID + "." + getClass().getSimpleName(); //$NON-NLS-1$
	}

	/**
	 * @param resource
	 * @return
	 */
	protected abstract boolean resourceMatches( IResource resource );

	void buildResource( IProgressMonitor progressMonitor, IResource resource )
	{
		this.L.debug( "BUILDING %s", resource.getFullPath() ); //$NON-NLS-1$

		try {
			createJavaSource( progressMonitor, resource );
		}
		catch( final Exception e ) {
			addError( resource, e.getLocalizedMessage(), 0 );
		}
	}

	void cleanResource( IProgressMonitor progressMonitor, IResource resource )
	{
		this.L.debug( "CLEANING %s", resource.getFullPath() ); //$NON-NLS-1$

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
		final ProcessData rpd = new ProcessData( preferences );

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

		this.L.debug( "LAUNCH %s %s", className, arguments ); //$NON-NLS-1$

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

}
