package pi.eclipse.cle.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import pi.eclipse.cle.CleStrings;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
abstract class AbstractWizard
extends Wizard
implements INewWizard
{

	protected IContainer	container;

	protected AbstractWizard()
	{
		setNeedsProgressMonitor( true );
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init( IWorkbench workbench, IStructuredSelection selection )
	{
		this.container = null;

		if( (selection != null) && !selection.isEmpty() ) {
			final IStructuredSelection ssel = selection;
			final Object obj = ssel.getFirstElement();

			if( obj instanceof IResource ) {
				if( obj instanceof IContainer ) {
					this.container = (IContainer) obj;
				}
				else {
					this.container = ((IResource) obj).getParent();
				}
			}
		}

	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We will create an operation and run it using
	 * wizard as execution context.
	 */
	@Override
	public final boolean performFinish()
	{
		for( final IWizardPage p : getPages() ) {
			((AbstractWizardPage) p).finishWizard();
		}

		final VelocityContext context = new VelocityContext();
		final IContainer folder = getFileContainer();
		final IFile file = folder.getFile( getFilePath() );

		fillContext( context );

		final IRunnableWithProgress op = new IRunnableWithProgress()
			{
				public void run( IProgressMonitor monitor ) throws InvocationTargetException
				{
					try {
						doFinish( file, context, monitor );
					}
					catch( final CoreException e ) {
						throw new InvocationTargetException( e );
					}
					finally {
						monitor.done();
					}
				}
			};

		try {
			getContainer().run( true, false, op );
		}
		catch( final InterruptedException e ) {
			return false;
		}
		catch( final InvocationTargetException e ) {
			final Throwable realException = e.getTargetException();

			MessageDialog.openError( getShell(), CleStrings.get( "error-title" ), realException.getMessage() ); //$NON-NLS-1$

			return false;
		}

		return true;
	}

	/**
	 * We will initialize file contents with a sample text.
	 * 
	 * @param context
	 * @throws Exception
	 * @throws ParseErrorException
	 * @throws ResourceNotFoundException
	 */
	private InputStream openContentStream( VelocityContext context )
	throws ResourceNotFoundException, ParseErrorException, Exception
	{
		final FileGen velocity = new FileGen( getFileExtension() + ".template" ); //$NON-NLS-1$
		final StringWriter output = new StringWriter();

		velocity.generate( context, output );

		return new ByteArrayInputStream( output.toString().getBytes() );
	}

	/**
	 * The worker method. It will find the container, create the file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 * 
	 * @param file
	 * @param context
	 */
	protected void doFinish( final IFile file, VelocityContext context, IProgressMonitor monitor ) throws CoreException
	{
		monitor.beginTask( CleStrings.get( "creating-file", file ), 2 ); //$NON-NLS-1$

		try {
			final InputStream stream = openContentStream( context );

			if( file.exists() ) {
				file.setContents( stream, true, true, monitor );
			}
			else {
				file.create( stream, true, monitor );
			}

			stream.close();
		}
		catch( final Exception e ) {
			e.printStackTrace();
		}

		monitor.worked( 1 );

		monitor.setTaskName( CleStrings.get( "opening-file", file ) ); //$NON-NLS-1$

		getShell().getDisplay().asyncExec( new Runnable()
			{
				public void run()
				{
					final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

					try {
						IDE.openEditor( page, file, true );
					}
					catch( final PartInitException e ) {
					}
				}
			} );

		monitor.worked( 1 );
	}

	/**
	 * @return
	 */
	abstract void fillContext( VelocityContext context );

	/**
	 * @return
	 */
	abstract IContainer getFileContainer();

	/**
	 * @return
	 */
	abstract String getFileExtension();

	/**
	 * @return
	 */
	abstract String getFileName();

	/**
	 * @return
	 */
	final IPath getFilePath()
	{
		return new Path( getFileName() + "." + getFileExtension() ); //$NON-NLS-1$
	}
}
