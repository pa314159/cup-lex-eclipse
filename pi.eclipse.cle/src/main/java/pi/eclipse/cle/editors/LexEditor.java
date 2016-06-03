package pi.eclipse.cle.editors;

import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import pi.eclipse.cle.ClePlugin;

/**
 */
public class LexEditor
extends AbstractEditor
{
	public static final String	ID	= ClePlugin.ID + "." + "LexEditor"; 

	private AbstractOutlinePage	outlinePage;

	@Override
	public Object getAdapter( Class required )
	{
		if( IContentOutlinePage.class == required ) {
			if( this.outlinePage == null ) {
				this.outlinePage = new LexOutlinePage( getDocumentProvider(), this );
				if( getEditorInput() != null ) {
					this.outlinePage.setInput( getEditorInput() );
				}
			}
			return this.outlinePage;
		}

		return super.getAdapter( required );
	}

	@Override
	protected void initializeEditor()
	{
		super.initializeEditor();

		setSourceViewerConfiguration( new LexSourceViewerConfiguration() );
	}
}
