
package pi.mojo.cle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;

@Mojo( name = "update-version", defaultPhase = LifecyclePhase.PROCESS_RESOURCES )
public class UpdateVersionMojo
extends AbstractMojo
{

	@Parameter( property = "project.build.outputDirectory", required = true )
	private File outputDirectory;

	@Parameter( required = true )
	private String replace;

	@Parameter( required = true )
	private String with;

	@Parameter( required = true, defaultValue = "UTF-8" )
	private String encoding;

	@Parameter
	private String[] includes;

	@Parameter
	private String[] excludes;

	@Parameter
	private String property;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		this.with = this.with.replace( "-SNAPSHOT", "" );
		this.project.getProperties().setProperty( this.property, this.with );

		final DirectoryScanner ds = new DirectoryScanner();

		ds.setBasedir( this.outputDirectory );
		ds.addDefaultExcludes();

		if( this.includes != null ) {
			ds.setIncludes( this.includes );
		}
		if( this.excludes != null ) {
			ds.setExcludes( this.excludes );
		}

		ds.scan();

		for( final String f : ds.getIncludedFiles() ) {
			try {
				updateVersion( f );
			}
			catch( final IOException e ) {
				throw new MojoExecutionException( f, e );
			}
		}
	}

	void updateVersion( String f ) throws IOException
	{
		final File file = new File( this.outputDirectory, f );
		final File temp = File.createTempFile( "ver", ".tmp" );

		FileUtils.copyFile( file, temp );

		final OutputStream os = new FileOutputStream( file );
		final Writer wr = new OutputStreamWriter( os, this.encoding );
		final PrintWriter pw = new PrintWriter( wr );
		final InputStream is = new FileInputStream( temp );
		final Reader isr = new InputStreamReader( is, this.encoding );
		final BufferedReader br = new BufferedReader( isr );

		String ln = null;

		while( ( ln = br.readLine() ) != null ) {
			ln = ln.replace( this.replace, this.with );

			pw.println( ln );
		}

		br.close();
		pw.close();
	}
}
