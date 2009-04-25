package pi.eclipse.cle.properties;

import static pi.eclipse.cle.preferences.ClePreferences.CUP_COMPACT_RED;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_DEBUG;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_EXPECTED_CONFLICTS;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_NON_TERMS;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_NO_POSITIONS;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_NO_SCANNER;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_OUTPUT;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_SYMBOLS_IF;

import org.eclipse.core.resources.IContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import pi.eclipse.cle.ClePlugin;
import pi.eclipse.cle.CleStrings;
import pi.eclipse.cle.util.AbstractWidget;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class CupPrefsWidget
extends AbstractWidget
{
	private CupPrefs	preferences		= null; // @jve:decl-index=0:

	private Label		lxJavaFolder	= null;

	private Text		txJavaFolder	= null;

	private Button		btJavaFolder	= null;

	private Label		lxCupClass		= null;

	private Text		txCupClass		= null;

	private Label		sepCupClass		= null;

	private Label		lxSymClass		= null;

	private Text		txSymClass		= null;

	private Label		sepSymClass		= null;

	private Button		ckSymIf			= null;

	private Button		ckDebug			= null;

	private Button		ckNonTerms		= null;

	private Button		ckCompactRed	= null;

	private Button		ckNoPositions	= null;

	private Button		ckNoScanner		= null;

	private Label		lxExpected		= null;

	private Spinner		ctExpected		= null;

	/**
	 * @param parent
	 * @param resource
	 */
	public CupPrefsWidget( Composite parent )
	{
		super( parent, SWT.NONE );

		initialize();
	}

	/**
	 * 
	 */
	protected void fillValues()
	{
		performDefaults();

		if( this.preferences != null ) {
			this.txJavaFolder.setText( this.preferences.getJavaFolder() );
			this.txCupClass.setText( this.preferences.getParserClass() );
			this.txSymClass.setText( this.preferences.getSymbolsClass() );
			this.ctExpected.setSelection( this.preferences.getExpectedConflicts() );
			this.ckSymIf.setSelection( this.preferences.getSymbolsIf() );
			this.ckDebug.setSelection( this.preferences.getDebug() );
			this.ckNonTerms.setSelection( this.preferences.getNonTerms() );
			this.ckCompactRed.setSelection( this.preferences.getCompactRed() );
			this.ckNoPositions.setSelection( this.preferences.getNoPositions() );
			this.ckNoScanner.setSelection( this.preferences.getNoScanner() );
		}
	}

	/**
	 * @return Returns the preferences.
	 */
	public CupPrefs getPreferences()
	{
		return this.preferences;
	}

	protected void initialize()
	{

		final GridLayout gridLayout = new GridLayout();

		gridLayout.numColumns = 3; // Generated

		// sink directory
		final GridData gdDestination = new GridData();
		gdDestination.horizontalAlignment = GridData.FILL; // Generated
		gdDestination.grabExcessHorizontalSpace = true; // Generated
		gdDestination.verticalAlignment = GridData.CENTER; // Generated

		this.lxJavaFolder = new Label( this, SWT.NONE );
		this.lxJavaFolder.setText( CUP_OUTPUT.toLabel() );
		this.txJavaFolder = new Text( this, SWT.BORDER );
		this.txJavaFolder.setLayoutData( gdDestination ); // Generated
		this.txJavaFolder.addModifyListener( new ModifyListener()
			{
				public void modifyText( ModifyEvent e )
				{
					updateWidgetContainer();
				}
			} );
		this.btJavaFolder = new Button( this, SWT.NONE );
		this.btJavaFolder.setText( "..." ); //$NON-NLS-1$
		this.btJavaFolder.addSelectionListener( new SelectionAdapter()
			{
				@Override
				public void widgetSelected( SelectionEvent e )
				{
					selectJavaFolder( e );
				}
			} );

		// parser class
		final GridData gdParserClass = new GridData();
		gdParserClass.horizontalAlignment = GridData.FILL;
		gdParserClass.grabExcessHorizontalSpace = true;
		gdParserClass.verticalAlignment = GridData.CENTER;

		this.lxCupClass = new Label( this, SWT.NONE );
		this.lxCupClass.setText( CleStrings.get( "preference-cup-parser-class" ) ); //$NON-NLS-1$
		this.txCupClass = new Text( this, SWT.BORDER );
		this.txCupClass.setLayoutData( gdParserClass );
		this.txCupClass.addModifyListener( new ModifyListener()
			{
				public void modifyText( ModifyEvent e )
				{
					updateWidgetContainer();
				}
			} );
		this.sepCupClass = new Label( this, SWT.NONE );

		// symbols class
		final GridData gdSymClass = new GridData();
		gdSymClass.horizontalAlignment = GridData.FILL;
		gdSymClass.grabExcessHorizontalSpace = true;
		gdSymClass.verticalAlignment = GridData.CENTER;

		this.lxSymClass = new Label( this, SWT.NONE );
		this.lxSymClass.setText( CleStrings.get( "preference-cup-symbols-class" ) ); //$NON-NLS-1$
		this.txSymClass = new Text( this, SWT.BORDER );
		this.txSymClass.setLayoutData( gdSymClass );
		this.txSymClass.addModifyListener( new ModifyListener()
			{
				public void modifyText( ModifyEvent e )
				{
					updateWidgetContainer();
				}
			} );
		this.sepSymClass = new Label( this, SWT.NONE );

		// expected conflicts
		final GridData gdExpected = new GridData();
		gdExpected.horizontalSpan = 2;

		this.lxExpected = new Label( this, SWT.NONE );
		this.lxExpected.setText( CUP_EXPECTED_CONFLICTS.toLabel() );

		this.ctExpected = new Spinner( this, SWT.BORDER );
		this.ctExpected.setMaximum( 65535 );
		this.ctExpected.setLayoutData( gdExpected );
		this.ctExpected.addModifyListener( new ModifyListener()
			{
				public void modifyText( ModifyEvent e )
				{
					updateWidgetContainer();
				}
			} );
		this.ctExpected.addSelectionListener( new SelectionListener()
			{
				public void widgetDefaultSelected( SelectionEvent e )
				{
					updateWidgetContainer();
				}

				public void widgetSelected( SelectionEvent e )
				{
					updateWidgetContainer();
				}
			} );

		// interface
		final GridData gdSymIf = new GridData();
		gdSymIf.horizontalSpan = 3;
		gdSymIf.verticalAlignment = GridData.CENTER;
		gdSymIf.grabExcessHorizontalSpace = true;
		gdSymIf.horizontalAlignment = GridData.FILL;

		this.ckSymIf = new Button( this, SWT.CHECK );
		this.ckSymIf.setText( CUP_SYMBOLS_IF.toLabel() );
		this.ckSymIf.setSelection( true );
		this.ckSymIf.setLayoutData( gdSymIf );

		// debug
		final GridData gdDebug = new GridData();
		gdDebug.horizontalSpan = 3;
		gdDebug.verticalAlignment = GridData.CENTER;
		gdDebug.grabExcessHorizontalSpace = true;
		gdDebug.horizontalAlignment = GridData.FILL;

		this.ckDebug = new Button( this, SWT.CHECK );
		this.ckDebug.setText( CUP_DEBUG.toLabel() );
		this.ckDebug.setSelection( true );
		this.ckDebug.setLayoutData( gdDebug );

		// non terminal
		final GridData gdNonTerms = new GridData();
		gdNonTerms.horizontalSpan = 3;
		gdNonTerms.horizontalAlignment = GridData.FILL;
		gdNonTerms.verticalAlignment = GridData.CENTER;
		gdNonTerms.grabExcessHorizontalSpace = true;

		this.ckNonTerms = new Button( this, SWT.CHECK );
		this.ckNonTerms.setText( CUP_NON_TERMS.toLabel() );
		this.ckNonTerms.setLayoutData( gdNonTerms );

		// compact reductions
		final GridData gdCompactRed = new GridData();
		gdCompactRed.horizontalSpan = 3;
		gdCompactRed.verticalAlignment = GridData.CENTER;
		gdCompactRed.grabExcessHorizontalSpace = true;
		gdCompactRed.horizontalAlignment = GridData.FILL;

		this.ckCompactRed = new Button( this, SWT.CHECK );
		this.ckCompactRed.setText( CUP_COMPACT_RED.toLabel() );
		this.ckCompactRed.setLayoutData( gdCompactRed );

		// no positions
		final GridData gdPosProp = new GridData();
		gdPosProp.horizontalSpan = 3;
		gdPosProp.verticalAlignment = GridData.CENTER;
		gdPosProp.grabExcessHorizontalSpace = true;
		gdPosProp.horizontalAlignment = GridData.FILL;

		this.ckNoPositions = new Button( this, SWT.CHECK );
		this.ckNoPositions.setText( CUP_NO_POSITIONS.toLabel() );
		this.ckNoPositions.setLayoutData( gdPosProp );

		// no scanner
		final GridData gdSkipScan = new GridData();
		gdSkipScan.horizontalAlignment = GridData.FILL;
		gdSkipScan.grabExcessHorizontalSpace = true;
		gdSkipScan.horizontalSpan = 3;
		gdSkipScan.verticalAlignment = GridData.CENTER;
		this.ckNoScanner = new Button( this, SWT.CHECK );
		this.ckNoScanner.setText( CUP_NO_SCANNER.toLabel() );
		this.ckNoScanner.setLayoutData( gdSkipScan );

		setSize( new Point( 341, 253 ) );
		setLayout( gridLayout );
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
			this.preferences.setParserClass( this.txCupClass.getText() );
			this.preferences.setSymbolsClass( this.txSymClass.getText() );
			this.preferences.setExpectedConflicts( this.ctExpected.getSelection() );
			this.preferences.setSymbolsIf( this.ckSymIf.getSelection() );
			this.preferences.setDebug( this.ckDebug.getSelection() );
			this.preferences.setNonTerms( this.ckNonTerms.getSelection() );
			this.preferences.setCompactRed( this.ckCompactRed.getSelection() );
			this.preferences.setNoPositions( this.ckNoPositions.getSelection() );
			this.preferences.setNoScanner( this.ckNoScanner.getSelection() );

			this.preferences.flush();
		}
	}

	/**
	 * 
	 */
	void performDefaults()
	{
		if( this.preferences != null ) {
			this.txJavaFolder.setText( this.preferences.getDefaultJavaFolder() );
			this.txCupClass.setText( this.preferences.getDefaultParserClass() );
			this.txSymClass.setText( this.preferences.getDefaultSymbolsClass() );
			this.ctExpected.setSelection( CUP_EXPECTED_CONFLICTS.getDefaultInt() );
			this.ckSymIf.setSelection( CUP_SYMBOLS_IF.getDefaultBoolean() );
			this.ckDebug.setSelection( CUP_DEBUG.getDefaultBoolean() );
			this.ckNonTerms.setSelection( CUP_NON_TERMS.getDefaultBoolean() );
			this.ckCompactRed.setSelection( CUP_COMPACT_RED.getDefaultBoolean() );
			this.ckNoPositions.setSelection( CUP_NO_POSITIONS.getDefaultBoolean() );
			this.ckNoScanner.setSelection( CUP_NO_SCANNER.getDefaultBoolean() );
		}
		else {
			this.txJavaFolder.setText( "" ); //$NON-NLS-1$
		}
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
	 * @param string
	 */
	public void setParserClass( String string )
	{
		this.txCupClass.setText( string );
	}

	/**
	 * @param preferences
	 *            The preferences to set.
	 */
	public void setPreferences( CupPrefs preferences )
	{
		this.preferences = preferences;

		this.btJavaFolder.setEnabled( this.preferences != null );

		fillValues();
	}

	/**
	 * @param string
	 */
	public void setSymClass( String string )
	{
		this.txSymClass.setText( string );
	}

	/**
	 * @see pi.eclipse.cle.util.AbstractWidget#updateWidgetContainer()
	 */
	@Override
	public void updateWidgetContainer()
	{
		if( isOutputVisible()
			&& !ClePlugin.isJavaFolder( this.preferences.getEclipseProject(), this.txJavaFolder.getText() ) ) {
			fireWidgetModified( CleStrings.get( "preference-cup-output-error" ) ); //$NON-NLS-1$

			return;
		}

		if( this.txCupClass.getText().length() == 0 ) {
			fireWidgetModified( CleStrings.get( "preference-cup-parser-error" ) ); //$NON-NLS-1$

			return;
		}

		if( this.txSymClass.getText().length() == 0 ) {
			fireWidgetModified( CleStrings.get( "preference-cup-symbol-error" ) ); //$NON-NLS-1$

			return;
		}

		fireWidgetModified( null );
	}

} // @jve:decl-index=0:visual-constraint="10,10"
