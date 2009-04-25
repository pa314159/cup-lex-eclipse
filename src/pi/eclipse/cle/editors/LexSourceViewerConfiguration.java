package pi.eclipse.cle.editors;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
class LexSourceViewerConfiguration
extends SourceViewerConfiguration
{

	/**
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredDocumentPartitioning(org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public String getConfiguredDocumentPartitioning( ISourceViewer sourceViewer )
	{
		return LexPartitionScanner.LEX_PARTITIONING;
	}

}
