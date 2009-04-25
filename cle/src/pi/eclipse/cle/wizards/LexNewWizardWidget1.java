package pi.eclipse.cle.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import pi.eclipse.cle.ClePlugin;
import pi.eclipse.cle.CleStrings;
import pi.eclipse.cle.util.AbstractWidget;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
class LexNewWizardWidget1
extends AbstractWidget
{

	private final SelectionAdapter	btChanged		= new SelectionAdapter()
														{
															@Override
															public void widgetSelected( SelectionEvent e )
															{
																updateWidgetContainer();
															}
														};

	private final ModifyListener	txChanged		= new ModifyListener()
														{
															public void modifyText( ModifyEvent e )
															{
																updateWidgetContainer();

															}
														};

	Button							ckPublic		= null;

	Button							ckAbstract		= null;

	Button							ckFinal			= null;

	Button							ckCupDebug		= null;

	Button							ckUnicode		= null;

	Button							ckLines			= null;

	Button							ckColumns		= null;

	Button							ckCup			= null;

	private Label					lxFolder		= null;

	private Text					txFolder		= null;

	private Button					btFolder		= null;

	private Label					lxJavaName		= null;

	Text							txJavaName		= null;

	private Label					sepJavaName		= null;

	private Label					lxPackage		= null;

	Text							txPackage		= null;

	private Button					btPackage		= null;

	private Label					lxSym			= null;

	Text							txSym			= null;

	private Label					sepSym			= null;

	private Label					lxJavaFolder	= null;

	private Text					txJavaFolder	= null;

	private Button					btJavaFolder	= null;

	private IContainer				resFolder		= null;

	private IFolder					resJavaFolder	= null;

	private Label					lxFileName		= null;

	private Text					txFileName		= null;

	private Label					sepFileName		= null;

	/**
	 * @param parent
	 * @param style
	 */
	public LexNewWizardWidget1( Composite parent, IContainer container )
	{
		super( parent, SWT.NONE );

		initialize();

		if( container != null ) {
			this.txFolder.setText( container.getFullPath().toPortableString() );
		}

		final Control[] children = getChildren();

		for( final Control element : children ) {
			if( element instanceof Button ) {
				((Button) element).addSelectionListener( this.btChanged );
			}
			if( element instanceof Text ) {
				((Text) element).addModifyListener( this.txChanged );
			}
		}
	}

	/**
	 * @return
	 */
	String getFileName()
	{
		return this.txFileName.getText().replaceAll( "\\.lex$", "" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @return
	 */
	IContainer getFolder()
	{
		return this.resFolder;
	}

	protected void initialize()
	{
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4; // Generated

		// source folder
		final GridData gdFolder = new GridData();

		gdFolder.horizontalAlignment = GridData.FILL;
		gdFolder.grabExcessHorizontalSpace = true;
		gdFolder.horizontalSpan = 2;
		gdFolder.verticalAlignment = GridData.CENTER;

		this.lxFolder = new Label( this, SWT.NONE );
		this.lxFolder.setText( CleStrings.get( "source-folder" ) ); //$NON-NLS-1$

		this.txFolder = new Text( this, SWT.BORDER );
		this.txFolder.setLayoutData( gdFolder );
		this.txFolder.addModifyListener( new ModifyListener()
			{
				public void modifyText( ModifyEvent e )
				{
					updateResFolder();
				}
			} );

		this.btFolder = new Button( this, SWT.NONE );
		this.btFolder.setText( "..." ); //$NON-NLS-1$
		this.btFolder.addSelectionListener( new org.eclipse.swt.events.SelectionAdapter()
			{
				@Override
				public void widgetSelected( org.eclipse.swt.events.SelectionEvent e )
				{
					selectFolder();
				}
			} );

		// file name
		final GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.verticalAlignment = GridData.CENTER;
		gridData.horizontalAlignment = GridData.FILL;

		this.lxFileName = new Label( this, SWT.NONE );

		this.lxFileName.setText( CleStrings.get( "file-name" ) ); //$NON-NLS-1$

		this.txFileName = new Text( this, SWT.BORDER );

		this.txFileName.setLayoutData( gridData );
		this.txFileName.addModifyListener( new ModifyListener()
			{
				public void modifyText( ModifyEvent e )
				{
					updateJavaClass();
				}
			} );
		this.sepFileName = new Label( this, SWT.NONE );

		// java folder
		final GridData gdJavaFolder = new GridData();

		gdJavaFolder.horizontalSpan = 2;
		gdJavaFolder.verticalAlignment = GridData.CENTER;
		gdJavaFolder.grabExcessHorizontalSpace = true;
		gdJavaFolder.horizontalAlignment = GridData.FILL;

		this.lxJavaFolder = new Label( this, SWT.NONE );

		this.lxJavaFolder.setText( CleStrings.get( "java-folder" ) ); //$NON-NLS-1$

		this.txJavaFolder = new Text( this, SWT.BORDER );

		this.txJavaFolder.setLayoutData( gdJavaFolder );
		this.txJavaFolder.addModifyListener( new ModifyListener()
			{
				public void modifyText( ModifyEvent e )
				{
					updateJavaFolder();
				}
			} );

		this.btJavaFolder = new Button( this, SWT.NONE );

		this.btJavaFolder.setText( "..." ); //$NON-NLS-1$
		this.btJavaFolder.addSelectionListener( new SelectionAdapter()
			{
				@Override
				public void widgetSelected( SelectionEvent e )
				{
					selectJavaFolder();
				}
			} );

		// java package
		final GridData gdPackage = new GridData();

		gdPackage.horizontalAlignment = GridData.FILL;
		gdPackage.grabExcessHorizontalSpace = true;
		gdPackage.horizontalSpan = 2;
		gdPackage.verticalAlignment = GridData.CENTER;

		this.lxPackage = new Label( this, SWT.NONE );

		this.lxPackage.setText( CleStrings.get( "package-name" ) ); //$NON-NLS-1$

		this.txPackage = new Text( this, SWT.BORDER );

		this.txPackage.setLayoutData( gdPackage );

		this.btPackage = new Button( this, SWT.NONE );

		this.btPackage.setText( "..." ); //$NON-NLS-1$

		this.btPackage.addSelectionListener( new org.eclipse.swt.events.SelectionAdapter()
			{
				@Override
				public void widgetSelected( org.eclipse.swt.events.SelectionEvent e )
				{
					selectPackage();
				}
			} );

		// java class name
		final GridData gdClass = new GridData();

		gdClass.horizontalAlignment = GridData.FILL;
		gdClass.grabExcessHorizontalSpace = true;
		gdClass.horizontalSpan = 2;
		gdClass.verticalAlignment = GridData.CENTER;

		this.lxJavaName = new Label( this, SWT.NONE );

		this.lxJavaName.setText( CleStrings.get( "class-name" ) ); //$NON-NLS-1$

		this.txJavaName = new Text( this, SWT.BORDER );

		this.txJavaName.setLayoutData( gdClass );
		this.txJavaName.addModifyListener( new ModifyListener()
			{
				public void modifyText( ModifyEvent e )
				{
					updateSymClass();
				}
			} );

		this.sepJavaName = new Label( this, SWT.NONE );

		//
		final GridData gdUnicode = new GridData();

		gdUnicode.horizontalSpan = 1;

		final GridData gdColumns = new GridData();

		gdColumns.horizontalSpan = 2;

		this.ckUnicode = new Button( this, SWT.CHECK );

		this.ckUnicode.setText( CleStrings.get( "use-unicode" ) ); //$NON-NLS-1$
		this.ckUnicode.setSelection( true );
		this.ckUnicode.setLayoutData( gdUnicode );

		this.ckLines = new Button( this, SWT.CHECK );

		this.ckLines.setText( CleStrings.get( "line-counting" ) ); //$NON-NLS-1$

		this.ckLines.setSelection( true );
		this.ckColumns = new Button( this, SWT.CHECK );

		this.ckColumns.setText( CleStrings.get( "column-counting" ) ); //$NON-NLS-1$
		this.ckColumns.setSelection( true );
		this.ckColumns.setLayoutData( gdColumns );

		//
		final GridData gdFinal = new GridData();

		gdFinal.horizontalSpan = 2;

		this.ckPublic = new Button( this, SWT.CHECK );

		this.ckPublic.setText( CleStrings.get( "decl-public" ) ); //$NON-NLS-1$

		this.ckAbstract = new Button( this, SWT.RADIO );

		this.ckAbstract.setText( CleStrings.get( "decl-abstract" ) ); //$NON-NLS-1$

		this.ckFinal = new Button( this, SWT.RADIO );

		this.ckFinal.setText( CleStrings.get( "decl-final" ) ); //$NON-NLS-1$
		this.ckFinal.setSelection( true );
		this.ckFinal.setLayoutData( gdFinal );

		//
		final GridData gdCup = new GridData();

		gdCup.horizontalSpan = 4;

		this.ckCup = new Button( this, SWT.CHECK );

		this.ckCup.setText( CleStrings.get( "cup-compat" ) ); //$NON-NLS-1$
		this.ckCup.setLayoutData( gdCup );
		this.ckCup.setSelection( true );
		this.ckCup.addSelectionListener( new org.eclipse.swt.events.SelectionAdapter()
			{
				@Override
				public void widgetSelected( org.eclipse.swt.events.SelectionEvent e )
				{
					LexNewWizardWidget1.this.txSym.setEnabled( LexNewWizardWidget1.this.ckCup.getSelection() );
					LexNewWizardWidget1.this.ckCupDebug.setEnabled( LexNewWizardWidget1.this.ckCup.getEnabled() );
				}
			} );

		//
		final GridData gdLxSym = new GridData();

		gdLxSym.horizontalIndent = 20;

		final GridData gdTxSym = new GridData();

		gdTxSym.horizontalAlignment = GridData.FILL;
		gdTxSym.grabExcessHorizontalSpace = true;
		gdTxSym.horizontalSpan = 2;
		gdTxSym.verticalAlignment = GridData.CENTER;

		this.lxSym = new Label( this, SWT.NONE );

		this.lxSym.setText( CleStrings.get( "symbols-class-name" ) ); //$NON-NLS-1$
		this.lxSym.setLayoutData( gdLxSym );

		this.txSym = new Text( this, SWT.BORDER );

		this.txSym.setLayoutData( gdTxSym );

		this.sepSym = new Label( this, SWT.NONE );

		//
		final GridData gdCupDebug = new GridData();

		gdCupDebug.horizontalIndent = 20;

		this.ckCupDebug = new Button( this, SWT.CHECK );

		this.ckCupDebug.setText( CleStrings.get( "cup-debug" ) ); //$NON-NLS-1$
		this.ckCupDebug.setLayoutData( gdCupDebug );

		setLayout( gridLayout ); // Generated
		setSize( new org.eclipse.swt.graphics.Point( 368, 305 ) ); // Generated
	}

	/**
	 * 
	 */
	protected void selectFolder()
	{
		final IPath value = ClePlugin.selectFolder( getShell() );

		if( value != null ) {
			this.txFolder.setText( value.toPortableString() );
		}
		else {
			this.txFolder.setText( "" ); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 */
	protected void selectJavaFolder()
	{
		if( this.resFolder != null ) {
			final IResource resource = ClePlugin.selectJavaFolder( getShell(), this.resFolder.getProject() );

			if( resource != null ) {
				this.txJavaFolder.setText( resource.getProjectRelativePath().toPortableString() );

				return;
			}
		}

		this.txJavaFolder.setText( "" ); //$NON-NLS-1$
	}

	/**
	 * 
	 */
	protected void selectPackage()
	{
		if( this.resJavaFolder != null ) {
			final IPackageFragment resource = ClePlugin.selectJavaPackage( getShell(), this.resJavaFolder );

			if( resource != null ) {
				this.txPackage.setText( resource.getElementName() );

				return;
			}
		}

		this.txPackage.setText( "" ); //$NON-NLS-1$
	}

	/**
	 * 
	 */
	protected void updateJavaClass()
	{
		String fileName = this.txFileName.getText();

		fileName = fileName.replaceAll( "\\.lex$", "" ); //$NON-NLS-1$ //$NON-NLS-2$

		this.txJavaName.setText( ClePlugin.toJavaName( fileName, true ) + "Lex" ); //$NON-NLS-1$
	}

	/**
	 * 
	 */
	protected void updateJavaFolder()
	{
		this.resJavaFolder = null;

		if( this.resFolder != null ) {
			final IProject project = this.resFolder.getProject();
			final IResource resource = project.findMember( this.txJavaFolder.getText() );

			final IJavaElement element = JavaCore.create( resource );

			if( element instanceof IPackageFragmentRoot ) {
				final IPackageFragmentRoot fragment = (IPackageFragmentRoot) element;

				try {
					if( fragment.getKind() == IPackageFragmentRoot.K_SOURCE ) {
						if( resource instanceof IFolder ) {
							this.resJavaFolder = (IFolder) resource;
						}
					}
				}
				catch( final JavaModelException e ) {
					;
				}
			}
		}

		this.txPackage.setEnabled( this.resJavaFolder != null );
		this.btPackage.setEnabled( this.resJavaFolder != null );
	}

	/**
	 * 
	 */
	protected void updateResFolder()
	{
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final IResource resource = root.findMember( this.txFolder.getText() );

		if( resource instanceof IContainer ) {
			this.resFolder = (IContainer) resource;
		}

		if( this.resFolder == root ) {
			this.resFolder = null;
		}

		this.txJavaFolder.setEnabled( this.resFolder != null );
		this.btJavaFolder.setEnabled( this.resFolder != null );

		this.txJavaFolder.setText( "" ); //$NON-NLS-1$
	}

	/**
	 * 
	 */
	protected void updateSymClass()
	{
		String javaName = this.txJavaName.getText();

		javaName = javaName.replaceAll( "Lex$", "" ); //$NON-NLS-1$ //$NON-NLS-2$

		this.txSym.setText( javaName + "Sym" ); //$NON-NLS-1$
	}

	@Override
	public void updateWidgetContainer()
	{
		if( this.txFolder.getText().length() == 0 ) {
			fireWidgetModified( CleStrings.get( "folder-name-required" ) ); //$NON-NLS-1$

			return;
		}

		final String fileName = this.txFileName.getText();

		if( fileName.length() == 0 ) {
			fireWidgetModified( CleStrings.get( "file-name-required" ) ); //$NON-NLS-1$

			return;
		}

		if( fileName.lastIndexOf( '.' ) >= 0 && !fileName.endsWith( ".lex" ) ) { //$NON-NLS-1$
			fireWidgetModified( CleStrings.get( "lex-ext-required" ) ); //$NON-NLS-1$

			return;
		}

		final IContainer folder = getFolder();
		final IResource resource = folder != null ? folder.findMember( getFileName() + ".lex" ) : null; //$NON-NLS-1$

		if( resource != null && resource.exists() ) {
			fireWidgetModified( CleStrings.get( "resource-already-exists", getFileName() ) ); //$NON-NLS-1$

			return;
		}

		if( this.txJavaName.getText().length() == 0 ) {
			fireWidgetModified( CleStrings.get( "class-name-required" ) ); //$NON-NLS-1$

			return;
		}
		if( this.txJavaFolder.getText().length() == 0 ) {
			fireWidgetModified( CleStrings.get( "java-folder-required" ) ); //$NON-NLS-1$

			return;
		}
		if( this.ckCup.getSelection() && this.txSym.getText().length() == 0 ) {
			fireWidgetModified( CleStrings.get( "symbols-class-required" ) ); //$NON-NLS-1$

			return;
		}

		fireWidgetModified( null );
	}
} // @jve:decl-index=0:visual-constraint="10,10"
