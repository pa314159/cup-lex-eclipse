package pi.eclipse.cle.editors;

import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.IDocumentProvider;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
class LexOutlinePage
extends AbstractOutlinePage
{

	/**
	 * @param documentProvider
	 * @param editor
	 */
	LexOutlinePage( IDocumentProvider documentProvider, LexEditor editor )
	{
		super( documentProvider, editor );
	}

	/**
	 * @see pi.eclipse.cle.editors.AbstractOutlinePage#parse(org.eclipse.jface.text.IDocument)
	 */
	@Override
	protected List<Segment> parse( IDocument document )
	{
		return super.parse( document );
	}

}
