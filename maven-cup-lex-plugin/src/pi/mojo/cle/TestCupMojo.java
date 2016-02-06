
package pi.mojo.cle;

import java.util.List;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import pi.cle.exec.CupConfig;

@Mojo( name = "test-cup", threadSafe = true, defaultPhase = LifecyclePhase.GENERATE_SOURCES )
public class TestCupMojo
extends AbstractCupMojo
{

	@Parameter
	private CupConfig cup;

	@Parameter( defaultValue = "${basedir}/src/test/cup-lex" )
	private String sourceDirectory;

	@Parameter( defaultValue = "${project.build.directory}/generated-test-sources/cup-lex" )
	private String outputDirectory;

	@Override
	synchronized CupConfig config()
	{
		if( this.cup == null ) {
			this.cup = new CupConfig();
		}

		return this.cup;
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
		return this.project.getTestCompileSourceRoots();
	}
}
