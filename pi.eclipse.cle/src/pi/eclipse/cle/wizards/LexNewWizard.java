package pi.eclipse.cle.wizards;

import org.apache.velocity.VelocityContext;
import org.eclipse.core.resources.IContainer;

/**
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

		context.put( "PACKAGE", widget.txPackage.getText() ); 
		context.put( "CLASS", widget.txJavaName.getText() ); 

		context.put( "UNICODE", widget.ckUnicode.getSelection() ); 
		context.put( "LINE", widget.ckLines.getSelection() ); 
		context.put( "COLUMN", widget.ckColumns.getSelection() ); 

		context.put( "PUBLIC", widget.ckPublic.getSelection() ); 
		context.put( "ABSTRACT", widget.ckAbstract.getSelection() ); 
		context.put( "FINAL", widget.ckFinal.getSelection() ); 

		context.put( "CUP", widget.ckCup.getSelection() ); 
		context.put( "SYM_CLASS", widget.txSym.getText() ); 
		context.put( "CUP_DEBUG", widget.ckCupDebug.getSelection() ); 
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
		return "lex"; 
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
