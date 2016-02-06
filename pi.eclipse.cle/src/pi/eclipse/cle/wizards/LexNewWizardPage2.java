package pi.eclipse.cle.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Composite;

import pi.eclipse.cle.properties.LexPrefs;
import pi.eclipse.cle.properties.LexPrefsWidget;

/**
 */
class LexNewWizardPage2
extends AbstractWizardPage
{

	/**
	 * @param container
	 */
	public LexNewWizardPage2( IContainer container )
	{
		super( container, "page-2" ); 

		setPageComplete( false );
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#getControl()
	 */
	@Override
	public LexPrefsWidget getControl()
	{
		return (LexPrefsWidget) super.getControl();
	}

	/**
	 * @see org.eclipse.jface.wizard.IWizardPage#getWizard()
	 */
	@Override
	public LexNewWizard getWizard()
	{
		return (LexNewWizard) super.getWizard();
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible( boolean visible )
	{
		super.setVisible( visible );

		if( visible ) {
			final IContainer folder = getWizard().getFileContainer();

			if( folder != null ) {
				final IFile target = folder.getFile( getWizard().getFilePath() );
				final IPath resource = target.getProjectRelativePath();

				getControl().setPreferences( new LexPrefs( folder.getProject(), resource ) );
			}
		}
	}

	/**
	 * @see pi.eclipse.cle.wizards.AbstractWizardPage#createWidget(org.eclipse.core.resources.IContainer,
	 *      org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Composite createWidget( IContainer container, Composite parent )
	{
		final LexPrefsWidget widget = new LexPrefsWidget( parent );

		widget.setOutputVisible( false );

		return widget;
	}

	@Override
	protected void finishWizard()
	{
		getControl().performApply();

		super.finishWizard();
	}

}
