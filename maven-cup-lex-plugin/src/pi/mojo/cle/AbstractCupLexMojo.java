package pi.mojo.cle;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.codehaus.plexus.util.DirectoryScanner;

import pi.cle.exec.AbstractConfig;

public abstract class AbstractCupLexMojo
extends AbstractMojo
{

	public void execute() throws MojoExecutionException, MojoFailureException
	{
		final AbstractConfig lex = config();

		if( lex == null ) {
			throw new MojoFailureException( "No configuration" );
		}

		final DirectoryScanner ds = new DirectoryScanner();
		final String s = sourceDirectory();

		ds.addDefaultExcludes();
		ds.setBasedir( s );

		ds.setIncludes( new String[] { "**/*." + suffix() } );

		ds.scan();

		final String[] files = ds.getIncludedFiles();

		if( files.length == 0 ) {
			return;
		}

		final List r = sourceRoots();
		final String o = outputDirectory();

		if( !r.contains( o ) ) {
			r.add( o );
		}

		final Project p = new Project();
		final Target t = new Target();

		p.setBaseDir( this.project.getBasedir() );

		t.setProject( p );

		for( final String f : files ) {
			try {
				final Task task = lex.createTask( t, new File( s, f ), o );

				t.addTask( task );
			}
			catch( final CloneNotSupportedException e ) {
				throw new MojoFailureException( "Cannot create the ANT task", e );
			}

		}

		t.execute();
	}

	abstract AbstractConfig config();

	abstract String outputDirectory();

	abstract String sourceDirectory();

	abstract List<String> sourceRoots();

	abstract String suffix();

}
