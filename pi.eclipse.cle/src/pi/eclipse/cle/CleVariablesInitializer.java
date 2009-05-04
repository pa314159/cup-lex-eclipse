package pi.eclipse.cle;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathVariableInitializer;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class CleVariablesInitializer
extends ClasspathVariableInitializer
{
	public enum VARS
	{
		CUP_RUNTIME_JAR( "java-cup-0.11a-beta-20060608-runtime" ), //$NON-NLS-1$
		CUP_JAR( "java-cup-0.11a-beta-20060608" ), //$NON-NLS-1$
		LEX_JAR( "jflex-1.4.3" ), //$NON-NLS-1$
		;

		public final Path	path	= new Path( name() );

		public final Path	value;

		VARS( String value )
		{
			this.value = new Path( value + ".jar" );
		}
	}

	static final CleLog	L	= new CleLog( CleVariablesInitializer.class );

	@Override
	public void initialize( String variable )
	{
		try {
			final VARS v = VARS.valueOf( variable );
			IPath p = new Path( "lib" );
			URL u = null;

			p = p.append( v.value );
			u = ClePlugin.findEntry( p );

			if( u == null ) {
				final String x = String.format( "Cannot find entry %s for variable %s", p.toOSString(), variable );

				L.fatal( "%s", x );

				throw new RuntimeException( x );
			}

			u = FileLocator.toFileURL( u );
			p = new Path( u.getPath() );

			JavaCore.setClasspathVariable( variable, p, null );
		}
		catch( final JavaModelException e ) {
			L.fatal( e, "Cannot initialize variable %s", variable );
			throw new RuntimeException( e );
		}
		catch( final IOException e ) {
			L.fatal( e, "Cannot initialize variable %s", variable );
			throw new RuntimeException( e );
		}
	}
}
