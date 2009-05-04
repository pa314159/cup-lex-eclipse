package pi.mojo.cle;

public abstract class AbstractCupMojo
extends AbstractCupLexMojo
{

	/**
	 * @parameter default-value="cup"
	 */
	private String	cupSuffix;

	@Override
	String suffix()
	{
		return this.cupSuffix;
	}

}
