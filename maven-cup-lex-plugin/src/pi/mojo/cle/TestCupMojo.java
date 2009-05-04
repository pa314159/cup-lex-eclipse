package pi.mojo.cle;

import java.util.List;

import pi.cle.exec.CupConfig;

/**
 * @goal test-cup
 * @phase generate-sources
 * @author Pappy Răzvan STĂNESCU <a href="mailto:pappy&#64;clarmon.com">&lt;pappy&#64;clarmon.com&gt;</a>
 */
public class TestCupMojo
extends AbstractCupMojo
{

	/**
	 * @parameter name="testCup"
	 */
	private CupConfig	cup;

	/**
	 * @parameter name="testSourceDirectory" default-value="${basedir}/src/test/etc"
	 */
	private String		sourceDirectory;

	/**
	 * @parameter name="testOutputDirectory" default-value="${project.build.directory}/test-generated-sources/etc"
	 */
	private String		outputDirectory;

	@Override
	CupConfig config()
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
