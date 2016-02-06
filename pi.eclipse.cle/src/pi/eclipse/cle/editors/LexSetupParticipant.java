package pi.eclipse.cle.editors;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;

/**
 */
public class LexSetupParticipant
implements IDocumentSetupParticipant
{

	public void setup( IDocument document )
	{
		if( document instanceof IDocumentExtension3 ) {
			final IDocumentExtension3 extension3 = (IDocumentExtension3) document;
			final IDocumentPartitioner partitioner = new FastPartitioner( LexPartitionScanner.INSTANCE,
				LexPartitionScanner.LEX_PARTITION_TYPES );

			extension3.setDocumentPartitioner( LexPartitionScanner.LEX_PARTITIONING, partitioner );

			partitioner.connect( document );
		}
	}

}
