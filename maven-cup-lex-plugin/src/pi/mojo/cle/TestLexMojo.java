package pi.mojo.cle;

import java.util.List;

import pi.cle.exec.LexConfig;

/**
 * @goal test-lex
 * @phase generate-sources
 * @author Pappy Răzvan STĂNESCU <a href="mailto:pappy&#64;clarmon.com">&lt;pappy&#64;clarmon.com&gt;</a>
 */
public class TestLexMojo
extends AbstractLexMojo
{
	/**
	 * @parameter name="sourceDirectory" default-value="${basedir}/src/test/etc"
	 */
	private String		sourceDirectory;

	/**
	 * @parameter name="outputDirectory" default-value="${project.build.directory}/test-generated-sources/etc"
	 */
	private String		outputDirectory;

	/**
	 * @parameter name="testLex"
	 */
	private LexConfig	lex;

	@Override
	LexConfig config()
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
		return this.project.getTestCompileSourceRoots();
	}

}
