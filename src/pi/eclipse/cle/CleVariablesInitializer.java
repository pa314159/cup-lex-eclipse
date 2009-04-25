package pi.eclipse.cle;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
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
		CUP_RUNTIME_JAR( "java-cup-11-runtime" ), //$NON-NLS-1$
		CUP_JAR( "java-cup-11" ), //$NON-NLS-1$
		LEX_JAR( "jflex-1.4.1" ), //$NON-NLS-1$
		;

		public final Path	path	= new Path( name() );

		public final Path	value;

		VARS( String value )
		{
			this.value = new Path( value + ".jar" );
		}
	}

	@Override
	public void initialize( String variable )
	{
		try {
			final VARS v = VARS.valueOf( variable );
			IPath p = new Path( "lib" );
			URL u = null;

			p = p.append( v.value );
			u = ClePlugin.findEntry( p );
			u = Platform.asLocalURL( u );
			p = new Path( u.getPath() );

			JavaCore.setClasspathVariable( variable, p, null );
		}
		catch( final JavaModelException e ) {
			e.printStackTrace();
		}
		catch( final IOException e ) {
			e.printStackTrace();
		}
	}

}
