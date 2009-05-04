package pi.mojo.cle;

import java.util.List;

import pi.cle.exec.LexConfig;

/**
 * @goal lex
 * @phase generate-sources
 * @author Pappy Răzvan STĂNESCU <a href="mailto:pappy&#64;clarmon.com">&lt;pappy&#64;clarmon.com&gt;</a>
 */
public class LexMojo
extends AbstractLexMojo
{

	/**
	 * @parameter name="testSourceDirectory" default-value="${basedir}/src/main/etc"
	 */
	private String		sourceDirectory;

	/**
	 * @parameter name="testOutputDirectory" default-value="${project.build.directory}/generated-sources/etc"
	 */
	private String		outputDirectory;

	/**
	 * @parameter name="lex"
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
		return this.project.getCompileSourceRoots();
	}

}
