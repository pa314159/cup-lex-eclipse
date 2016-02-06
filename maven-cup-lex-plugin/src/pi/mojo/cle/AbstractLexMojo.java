
package pi.mojo.cle;

import org.apache.maven.plugins.annotations.Parameter;

public abstract class AbstractLexMojo
extends AbstractCupLexMojo
{

	@Parameter( defaultValue = "lex" )
	private String lexSuffix;

	@Override
	String suffix()
	{
		return this.lexSuffix;
	}

}
