package pi.eclipse.cle.editors;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

/**
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
