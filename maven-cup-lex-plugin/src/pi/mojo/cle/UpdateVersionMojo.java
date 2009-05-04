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
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;

/**
 * @goal update-version
 * @phase process-resources
 * @author Pappy Răzvan STĂNESCU <a href="mailto:pappy&#64;clarmon.com">&lt;pappy&#64;clarmon.com&gt;</a>
 */
public class UpdateVersionMojo
extends AbstractMojo
{
	/**
	 * @parameter expression="${project.build.outputDirectory}"
	 * @required
	 */
	private File		outputDirectory;

	/**
	 * @parameter
	 * @required
	 */
	private String		replace;

	/**
	 * @parameter
	 * @required
	 */
	private String		with;

	/**
	 * @parameter default-value="UTF-8"
	 */
	private String		encoding;

	/**
	 * @parameter
	 */
	private String[]	includes;

	/**
	 * @parameter
	 */
	private String[]	excludes;

	public void execute() throws MojoExecutionException, MojoFailureException
	{
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
		final String rs = this.with.replace( "-SNAPSHOT", "" );

		String ln = null;

		while( (ln = br.readLine()) != null ) {
			ln = ln.replace( this.replace, rs );

			pw.println( ln );
		}

		br.close();
		pw.close();
	}
}
