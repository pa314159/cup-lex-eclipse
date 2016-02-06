package pi.eclipse.cle.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.widgets.Composite;

/**
 */
class LexNewWizardPage1
extends AbstractWizardPage
{

	LexNewWizardPage1( IContainer container )
	{
		super( container, "page-1" ); 
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#getControl()
	 */
	@Override
	public LexNewWizardWidget1 getControl()
	{
		return (LexNewWizardWidget1) super.getControl();
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Composite createWidget( IContainer container, Composite parent )
	{
		return new LexNewWizardWidget1( parent, container );

	}

}
