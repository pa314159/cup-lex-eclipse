
package pi.mojo.cle;

import org.apache.maven.plugins.annotations.Parameter;

public abstract class AbstractCupMojo
extends AbstractCupLexMojo
{

	@Parameter( defaultValue = "cup" )
	private String cupSuffix;

	@Override
	String suffix()
	{
		return this.cupSuffix;
	}

}
