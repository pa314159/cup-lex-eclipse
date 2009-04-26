package pi.eclipse.cle.wizards;

import org.apache.velocity.VelocityContext;
import org.eclipse.core.resources.IContainer;
import org.eclipse.ui.INewWizard;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class CupNewWizard
extends AbstractWizard
implements INewWizard
{

	private CupNewWizardPage1	page1;

	private CupNewWizardPage2	page2;

	/**
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	@Override
	public void addPages()
	{
		this.page1 = new CupNewWizardPage1( this.container );
		this.page2 = new CupNewWizardPage2( this.container );

		addPage( this.page1 );
		addPage( this.page2 );
	}

	/**
	 * @return
	 */
	@Override
	public String getFileName()
	{
		return this.page1.getControl().getFileName();
	}

	/**
	 * @see pi.eclipse.cle.wizards.AbstractWizard#createContext()
	 */
	@Override
	void fillContext( VelocityContext context )
	{
		final CupNewWizardWidget1 widget = this.page1.getControl();

		context.put( "PACKAGE", widget.txPackage.getText() ); //$NON-NLS-1$
	}

	/**
	 * @return
	 */
	@Override
	IContainer getFileContainer()
	{
		return this.page1.getControl().getFolder();
	}

	/**
	 * @see pi.eclipse.cle.wizards.AbstractWizard#getFileExtension()
	 */
	@Override
	String getFileExtension()
	{
		return "cup"; //$NON-NLS-1$
	}

}
