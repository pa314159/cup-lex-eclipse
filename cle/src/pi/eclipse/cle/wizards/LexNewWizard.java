package pi.eclipse.cle.wizards;

import org.apache.velocity.VelocityContext;
import org.eclipse.core.resources.IContainer;

import pi.eclipse.cle.properties.AbstractPref;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class LexNewWizard
extends AbstractWizard
{
	private LexNewWizardPage1	page1;

	private LexNewWizardPage2	page2;

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages()
	{
		this.page1 = new LexNewWizardPage1( this.container );
		this.page2 = new LexNewWizardPage2( this.container );

		addPage( this.page1 );
		addPage( this.page2 );
	}

	/**
	 * @return
	 */
	@Override
	void fillContext( VelocityContext context )
	{
		final LexNewWizardWidget1 widget = this.page1.getControl();

		context.put( "PACKAGE", widget.txPackage.getText() ); //$NON-NLS-1$
		context.put( "CLASS", widget.txJavaName.getText() ); //$NON-NLS-1$

		context.put( "UNICODE", widget.ckUnicode.getSelection() ); //$NON-NLS-1$
		context.put( "LINE", widget.ckLines.getSelection() ); //$NON-NLS-1$
		context.put( "COLUMN", widget.ckColumns.getSelection() ); //$NON-NLS-1$

		context.put( "PUBLIC", widget.ckPublic.getSelection() ); //$NON-NLS-1$
		context.put( "ABSTRACT", widget.ckAbstract.getSelection() ); //$NON-NLS-1$
		context.put( "FINAL", widget.ckFinal.getSelection() ); //$NON-NLS-1$

		context.put( "CUP", widget.ckCup.getSelection() ); //$NON-NLS-1$
		context.put( "SYM_CLASS", widget.txSym.getText() ); //$NON-NLS-1$
		context.put( "CUP_DEBUG", widget.ckCupDebug.getSelection() ); //$NON-NLS-1$
	}

	/**
	 * @return
	 */
	@Override
	IContainer getFileContainer()
	{
		return this.page1.getControl().getFolder();
	}

	/**
	 * @see pi.eclipse.cle.wizards.AbstractWizard#getFileExtension()
	 */
	@Override
	String getFileExtension()
	{
		return "lex"; //$NON-NLS-1$
	}

	/**
	 * @see pi.eclipse.cle.wizards.AbstractWizard#getFileName()
	 */
	@Override
	String getFileName()
	{
		return this.page1.getControl().getFileName();
	}
}
