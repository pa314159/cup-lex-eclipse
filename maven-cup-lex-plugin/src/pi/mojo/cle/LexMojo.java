
package pi.mojo.cle;

import java.util.List;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import pi.cle.exec.LexConfig;

@Mojo( name = "lex", threadSafe = true, defaultPhase = LifecyclePhase.GENERATE_SOURCES )
public class LexMojo
extends AbstractLexMojo
{

	@Parameter
	private LexConfig lex;

	@Parameter( defaultValue = "${basedir}/src/main/cup-lex" )
	private String sourceDirectory;

	@Parameter( defaultValue = "${project.build.directory}/generated-sources/cup-lex" )
	private String outputDirectory;

	@Override
	synchronized LexConfig config()
	{
		if( this.lex == null ) {
			this.lex = new LexConfig();
		}

		return this.lex;
	}

	@Override
	String outputDirectory()
	{
		return this.outputDirectory;
	}

	@Override
	String sourceDirectory()
	{
		return this.sourceDirectory;
	}

	@Override
	List sourceRoots()
	{
		return this.project.getCompileSourceRoots();
	}

}
