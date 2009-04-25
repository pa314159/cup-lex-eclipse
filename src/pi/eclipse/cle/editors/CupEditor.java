package pi.eclipse.cle.editors;

import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import pi.eclipse.cle.ClePlugin;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class CupEditor
extends AbstractEditor
{
	public static final String	ID	= ClePlugin.ID + "." + "CupEditor"; //$NON-NLS-1$ //$NON-NLS-2$

	private AbstractOutlinePage	outlinePage;

	/**
	 * @see org.eclipse.ui.editors.text.TextEditor#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter( Class required )
	{
		if( IContentOutlinePage.class == required ) {
			if( this.outlinePage == null ) {
				this.outlinePage = new CupOutlinePage( getDocumentProvider(), this );
				if( getEditorInput() != null ) {
					this.outlinePage.setInput( getEditorInput() );
				}
			}
			return this.outlinePage;
		}

		return super.getAdapter( required );
	}

	/**
	 * @see org.eclipse.ui.editors.text.TextEditor#initializeEditor()
	 */
	@Override
	protected void initializeEditor()
	{
		super.initializeEditor();

		setSourceViewerConfiguration( new CupSourceViewerConfiguration() );
	}
}
