package pi.eclipse.cle.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.widgets.Composite;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
class CupNewWizardPage1
extends AbstractWizardPage
{

	/**
	 */
	public CupNewWizardPage1( IContainer container )
	{
		super( container, "page-1" ); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#getControl()
	 */
	@Override
	public CupNewWizardWidget1 getControl()
	{
		return (CupNewWizardWidget1) super.getControl();
	}

	/**
	 * @see pi.eclipse.cle.wizards.AbstractWizardPage#createWidget(IContainer, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Composite createWidget( IContainer container, Composite parent )
	{
		return new CupNewWizardWidget1( parent, container );
	}

}
