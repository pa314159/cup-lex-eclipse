package pi.eclipse.cle.properties;

import static pi.eclipse.cle.preferences.ClePreferences.LEX_CODE_METHOD;
import static pi.eclipse.cle.preferences.ClePreferences.LEX_COMPLY_JLEX;
import static pi.eclipse.cle.preferences.ClePreferences.LEX_OUTPUT;
import static pi.eclipse.cle.preferences.ClePreferences.LEX_SKIP_MIN;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import JFlex.Options;

import pi.eclipse.cle.ClePlugin;
import pi.eclipse.cle.CleStrings;
import pi.eclipse.cle.util.AbstractWidget;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class LexPrefsWidget
extends AbstractWidget
{

	private LexPrefs	preferences;

	private Label		lxJavaFolder	= null;

	private Text		txJavaFolder	= null;

	private Button		btJavaFolder	= null;

	private Group		groupOptions	= null;

	private Button		ckSkipMin		= null;

	private Button		ckComply		= null;

	private Button		brPack			= null;

	private Button		brTable			= null;

	private Button		brSwitch		= null;

	private Group		groupCode		= null;

	/**
	 * @param parent
	 * @param resource
	 */
	public LexPrefsWidget( Composite parent )
	{
		super( parent, SWT.NONE );

		initialize();
	}

	/**
	 * @return Returns the preferences.
	 */
	public LexPrefs getPreferences()
	{
		return this.preferences;
	}

	/**
	 * @return
	 */
	public boolean isOutputVisible()
	{
		return this.txJavaFolder.isVisible();
	}

	/**
	 * 
	 */
	public void performApply()
	{
		if( this.preferences != null ) {
			this.preferences.setJavaFolder( this.txJavaFolder.getText() );

			this.preferences.setSkipMin( this.ckSkipMin.getSelection() );
			this.preferences.setComply( this.ckComply.getSelection() );

			this.preferences.setCodeMethod( Options.PACK );

			if( this.brTable.getSelection() ) {
				this.preferences.setCodeMethod( Options.TABLE );
			}
			if( this.brSwitch.getSelection() ) {
				this.preferences.setCodeMethod( Options.SWITCH );
			}

			this.preferences.flush();
		}
	}

	/**
	 * @param visible
	 */
	public void setOutputVisible( boolean visible )
	{
		this.lxJavaFolder.setVisible( visible );
		this.txJavaFolder.setVisible( visible );
		this.btJavaFolder.setVisible( visible );

		layout( true, true );
	}

	/**
	 * @param preferences
	 *            The preferences to set.
	 */
	public void setPreferences( LexPrefs preferences )
	{
		this.preferences = preferences;

		this.btJavaFolder.setEnabled( this.preferences != null );

		fillValues();
	}

	/**
	 * @see pi.eclipse.cle.util.AbstractWidget#updateWidgetContainer()
	 */
	@Override
	public void updateWidgetContainer()
	{
		if( isOutputVisible()
			&& !ClePlugin.isJavaFolder( this.preferences.getEclipseProject(), this.txJavaFolder.getText() ) ) {
			fireWidgetModified( CleStrings.get( "java-folder-required" ) ); //$NON-NLS-1$

			return;
		}

		fireWidgetModified( null );
	}

	/**
	 * This method initializes group1
	 */
	protected void createGroupCode()
	{
		final RowLayout rowLayout = new RowLayout();

		rowLayout.fill = true; // Generated
		rowLayout.spacing = 5; // Generated
		rowLayout.marginTop = 5; // Generated
		rowLayout.marginRight = 5; // Generated
		rowLayout.marginLeft = 5; // Generated
		rowLayout.marginBottom = 5; // Generated
		rowLayout.type = org.eclipse.swt.SWT.VERTICAL; // Generated

		final GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL; // Generated
		gridData.grabExcessHorizontalSpace = true; // Generated
		gridData.horizontalSpan = 3; // Generated
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER; // Generated

		this.groupCode = new Group( this, SWT.NONE );

		this.groupCode.setText( LEX_CODE_METHOD.toLabel() );
		this.groupCode.setLayout( rowLayout );
		this.groupCode.setLayoutData( gridData );

		this.brPack = new Button( this.groupCode, SWT.RADIO );
		this.brPack.setText( CleStrings.get( "preference-lex-pack" ) ); // Generated //$NON-NLS-1$
		// //$NON-NLS-1$
		this.brTable = new Button( this.groupCode, SWT.RADIO );
		this.brTable.setText( CleStrings.get( "preference-lex-table" ) ); // Generated //$NON-NLS-1$
		// //$NON-NLS-1$
		this.brSwitch = new Button( this.groupCode, SWT.RADIO );
		this.brSwitch.setText( CleStrings.get( "preference-lex-switch" ) ); // Generated //$NON-NLS-1$
		// //$NON-NLS-1$
	}

	/**
	 * This method initializes group
	 */
	protected void createGroupOptions()
	{
		final RowLayout rowLayout = new RowLayout();

		rowLayout.fill = true; // Generated
		rowLayout.spacing = 5; // Generated
		rowLayout.marginTop = 5; // Generated
		rowLayout.marginRight = 5; // Generated
		rowLayout.marginLeft = 5; // Generated
		rowLayout.marginBottom = 5; // Generated
		rowLayout.type = org.eclipse.swt.SWT.VERTICAL; // Generated

		final GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL; // Generated
		gridData.grabExcessHorizontalSpace = true; // Generated
		gridData.horizontalSpan = 3; // Generated
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER; // Generated

		this.groupOptions = new Group( this, SWT.NONE );

		this.groupOptions.setText( CleStrings.get( "preference-lex-general-options" ) ); // Generated //$NON-NLS-1$
		// //$NON-NLS-1$
		this.groupOptions.setLayout( rowLayout ); // Generated
		this.groupOptions.setLayoutData( gridData ); // Generated

		this.ckSkipMin = new Button( this.groupOptions, SWT.CHECK );
		this.ckSkipMin.setText( LEX_SKIP_MIN.toLabel() ); // Generated
		this.ckComply = new Button( this.groupOptions, SWT.CHECK );
		this.ckComply.setText( LEX_COMPLY_JLEX.toLabel() ); // Generated
		// //$NON-NLS-1$
	}

	/**
	 * 
	 */
	protected void fillValues()
	{
		performDefaults();

		if( this.preferences != null ) {
			this.txJavaFolder.setText( this.preferences.getJavaFolder() );

			this.ckSkipMin.setSelection( this.preferences.getSkipMin() );
			this.ckComply.setSelection( this.preferences.getComply() );

			switch( this.preferences.getCodeMethod() ) {
				default:
					this.brPack.setSelection( true );
					break;

				case Options.TABLE:
					this.brTable.setSelection( true );
					break;

				case Options.SWITCH:
					this.brSwitch.setSelection( true );
			}
		}
	}

	protected void initialize()
	{
		final GridLayout gridLayout = new GridLayout();

		gridLayout.numColumns = 3; // Generated

		final GridData gridData = new GridData();

		gridData.horizontalAlignment = GridData.FILL; // Generated
		gridData.grabExcessHorizontalSpace = true; // Generated
		gridData.verticalAlignment = GridData.CENTER; // Generated

		this.lxJavaFolder = new Label( this, SWT.NONE );
		this.lxJavaFolder.setText( LEX_OUTPUT.toLabel() ); // Generated

		this.txJavaFolder = new Text( this, SWT.BORDER );
		this.txJavaFolder.setLayoutData( gridData ); // Generated
		this.txJavaFolder.addModifyListener( new org.eclipse.swt.events.ModifyListener()
			{
				public void modifyText( org.eclipse.swt.events.ModifyEvent e )
				{
					updateWidgetContainer();
				}
			} );

		this.btJavaFolder = new Button( this, SWT.NONE );
		this.btJavaFolder.setText( "..." ); // Generated //$NON-NLS-1$
		this.btJavaFolder.addSelectionListener( new SelectionAdapter()
			{
				@Override
				public void widgetSelected( SelectionEvent e )
				{
					selectJavaFolder( e );
				}
			} );

		createGroupOptions();
		createGroupCode();

		setSize( new org.eclipse.swt.graphics.Point( 433, 233 ) );
		setLayout( gridLayout ); // Generated
	}

	/**
	 * @param e
	 */
	protected void selectJavaFolder( SelectionEvent e )
	{
		final IContainer selected = ClePlugin.selectJavaFolder( getShell(), this.preferences.getEclipseProject() );

		if( selected != null ) {
			this.txJavaFolder.setText( selected.getProjectRelativePath().toPortableString() );
		}
		else {
			this.txJavaFolder.setText( "" ); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 */
	void performDefaults()
	{
		if( this.preferences != null ) {
			this.txJavaFolder.setText( this.preferences.getDefaultJavaFolder() );

			this.ckSkipMin.setSelection( LEX_SKIP_MIN.getDefaultBoolean() );
			this.ckComply.setSelection( LEX_COMPLY_JLEX.getDefaultBoolean() );

			this.brPack.setSelection( false );
			this.brTable.setSelection( false );
			this.brSwitch.setSelection( false );

			switch( LEX_CODE_METHOD.getDefaultInt() ) {

				case 0:
					this.brPack.setSelection( true );
					break;

				case 1:
					this.brTable.setSelection( true );
					break;

				case 2:
					this.brSwitch.setSelection( true );
					break;
			}
		}
		else {
			this.txJavaFolder.setText( "" ); //$NON-NLS-1$
		}
	}
} // @jve:decl-index=0:visual-constraint="10,10"
