package pi.mojo.cle;

public abstract class AbstractLexMojo
extends AbstractCupLexMojo
{

	/**
	 * @parameter default-value="lex"
	 */
	private String	lexSuffix;

	@Override
	String suffix()
	{
		return this.lexSuffix;
	}

}
