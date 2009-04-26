package pi.eclipse.cle.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Composite;

import pi.eclipse.cle.ClePlugin;
import pi.eclipse.cle.properties.CupPrefs;
import pi.eclipse.cle.properties.CupPrefsWidget;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class CupNewWizardPage2
extends AbstractWizardPage
{

	/**
	 * @param pageName
	 */
	protected CupNewWizardPage2( IContainer container )
	{
		super( container, "page-2" ); //$NON-NLS-1$

		setPageComplete( false );
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#getControl()
	 */
	@Override
	public CupPrefsWidget getControl()
	{
		return (CupPrefsWidget) super.getControl();
	}

	/**
	 * @see org.eclipse.jface.wizard.IWizardPage#getWizard()
	 */
	@Override
	public CupNewWizard getWizard()
	{
		return (CupNewWizard) super.getWizard();
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

				getControl().setPreferences( new CupPrefs( folder.getProject(), resource ) );

				final String javaName = ClePlugin.toJavaName( getWizard().getFileName(), true );

				getControl().setParserClass( javaName + "Cup" ); //$NON-NLS-1$
				getControl().setSymClass( javaName + "Sym" ); //$NON-NLS-1$
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
		final CupPrefsWidget widget = new CupPrefsWidget( parent );

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
