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
class CupNewWizardWidget1
extends AbstractWidget
{

	private final SelectionAdapter	btChanged	= new SelectionAdapter()
													{
														@Override
														public void widgetSelected( SelectionEvent e )
														{
															updateWidgetContainer();
														}
													};

	private final ModifyListener	txChanged	= new ModifyListener()
													{
														public void modifyText( ModifyEvent e )
														{
															updateWidgetContainer();

														}
													};

	private Label					lxFolder;

	private Text					txFolder;

	private Button					btFolder;

	private Label					lxFileName;

	private Text					txFileName;

	private Label					sepFileName;

	private Label					lxJavaFolder;

	private Text					txJavaFolder;

	private Button					btJavaFolder;

	private Label					lxPackage;

	Text							txPackage;

	private Button					btPackage;

	private IContainer				resFolder;

	private IContainer				resJavaFolder;

	/**
	 * @param container
	 * @param parent
	 */
	public CupNewWizardWidget1( Composite parent, IContainer container )
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
	 * @see pi.eclipse.cle.util.AbstractWidget#updateWidgetContainer()
	 */
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

		if( (fileName.lastIndexOf( '.' ) >= 0) && !fileName.endsWith( ".cup" ) ) { //$NON-NLS-1$
			fireWidgetModified( CleStrings.get( "cup-ext-required" ) ); //$NON-NLS-1$

			return;
		}

		final IContainer folder = getFolder();
		final IResource resource = folder != null ? folder.findMember( getFileName() + ".cup" ) : null; //$NON-NLS-1$

		if( (resource != null) && resource.exists() ) {
			fireWidgetModified( CleStrings.get( "resource-exists", getFileName() ) ); //$NON-NLS-1$

			return;
		}

		if( this.txJavaFolder.getText().length() == 0 ) {
			fireWidgetModified( CleStrings.get( "java-folder-required" ) ); //$NON-NLS-1$

			return;
		}

		fireWidgetModified( null );
	}

	/**
	 * 
	 */
	private void initialize()
	{
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;

		setLayout( gridLayout );
		setSize( new org.eclipse.swt.graphics.Point( 390, 234 ) );

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
	 * @return
	 */
	String getFileName()
	{
		return this.txFileName.getText().replaceAll( "\\.cup$", "" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @return
	 */
	IContainer getFolder()
	{
		return this.resFolder;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
