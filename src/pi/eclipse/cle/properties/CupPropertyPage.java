package pi.eclipse.cle.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;

import pi.eclipse.cle.CleStrings;
import pi.eclipse.cle.util.WidgetListener;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class CupPropertyPage
extends PropertyPage
implements WidgetListener
{
	private CupPrefsWidget	widget;

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	@Override
	protected Control createContents( Composite parent )
	{
		final IResource resource = (IResource) getElement();

		try {
			if( resource.getProject().isNatureEnabled( JavaCore.NATURE_ID ) ) {
				this.widget = new CupPrefsWidget( parent );

				this.widget.addWidgetListener( this );
				this.widget.setPreferences( new CupPrefs( resource ) );

				return this.widget;
			}
		}
		catch( final CoreException e ) {
			e.printStackTrace();
		}

		this.widget = null;

		final Label label = new Label( parent, SWT.NONE );

		label.setText( CleStrings.get( "java-nature-required" ) ); //$NON-NLS-1$

		return label;
	}

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#performApply()
	 */
	@Override
	protected void performApply()
	{
		if( this.widget != null ) {
			final IResource resource = (IResource) getElement();

			try {
				resource.getProject().build( IncrementalProjectBuilder.CLEAN_BUILD, null );
			}
			catch( final CoreException e ) {
				e.printStackTrace();
			}

			this.widget.performApply();

			try {
				resource.getProject().build( IncrementalProjectBuilder.AUTO_BUILD, null );
			}
			catch( final CoreException e ) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults()
	{
		if( this.widget != null ) {
			this.widget.performDefaults();
		}
	}

	/**
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	@Override
	public boolean performOk()
	{
		performApply();

		return super.performOk();
	}

	/**
	 * @see pi.eclipse.cle.util.WidgetListener#updateContainer(java.lang.String)
	 */
	public void updateContainer( String status )
	{
		setErrorMessage( status );
		setValid( status == null );
	}
}
