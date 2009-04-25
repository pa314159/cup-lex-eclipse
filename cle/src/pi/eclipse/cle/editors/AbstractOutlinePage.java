package pi.eclipse.cle.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
abstract class AbstractOutlinePage
extends ContentOutlinePage
{

	/**
	 * Divides the editor's document into ten segments and provides elements for them.
	 */
	protected class ContentProvider
	implements ITreeContentProvider
	{

		protected IPositionUpdater	fPositionUpdater	= new DefaultPositionUpdater( SEGMENTS );

		protected List<Segment>		content				= new ArrayList<Segment>( 10 );

		/*
		 * @see IContentProvider#dispose
		 */
		public void dispose()
		{
			if( this.content != null ) {
				this.content.clear();
				this.content = null;
			}
		}

		/*
		 * @see ITreeContentProvider#getChildren(Object)
		 */
		public Object[] getChildren( Object element )
		{
			if( element == AbstractOutlinePage.this.input ) {
				return this.content.toArray();
			}
			return new Object[0];
		}

		/*
		 * @see IStructuredContentProvider#getElements(Object)
		 */
		public Object[] getElements( Object element )
		{
			return this.content.toArray();
		}

		/*
		 * @see ITreeContentProvider#getParent(Object)
		 */
		public Object getParent( Object element )
		{
			if( element instanceof Segment ) {
				return AbstractOutlinePage.this.input;
			}
			return null;
		}

		/*
		 * @see ITreeContentProvider#hasChildren(Object)
		 */
		public boolean hasChildren( Object element )
		{
			return element == AbstractOutlinePage.this.input;
		}

		/*
		 * @see IContentProvider#inputChanged(Viewer, Object, Object)
		 */
		public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
		{
			if( oldInput != null ) {
				final IDocument document = AbstractOutlinePage.this.documentProvider.getDocument( oldInput );
				if( document != null ) {
					try {
						document.removePositionCategory( SEGMENTS );
					}
					catch( final BadPositionCategoryException x ) {
					}
					document.removePositionUpdater( this.fPositionUpdater );
				}
			}

			if( newInput != null ) {
				final IDocument document = AbstractOutlinePage.this.documentProvider.getDocument( newInput );
				if( document != null ) {
					document.addPositionCategory( SEGMENTS );
					document.addPositionUpdater( this.fPositionUpdater );

					this.content = parse( document );
				}
			}
		}

		/*
		 * @see IContentProvider#isDeleted(Object)
		 */
		public boolean isDeleted( Object element )
		{
			return false;
		}
	}

	/**
	 * A segment element.
	 */
	protected static class Segment
	{
		public String	name;

		public Position	position;

		public Segment( String name, Position position )
		{
			this.name = name;
			this.position = position;
		}

		@Override
		public String toString()
		{
			return this.name;
		}
	}

	protected final static String	SEGMENTS	= "__cle_segments"; //$NON-NLS-1$

	private final IDocumentProvider	documentProvider;

	private final AbstractEditor	editor;

	private Object					input;

	/**
	 * @param documentProvider
	 * @param editor
	 */
	AbstractOutlinePage( IDocumentProvider documentProvider, AbstractEditor editor )
	{
		this.documentProvider = documentProvider;
		this.editor = editor;
	}

	@Override
	public void createControl( Composite parent )
	{
		super.createControl( parent );

		final TreeViewer viewer = getTreeViewer();

		viewer.setContentProvider( new ContentProvider() );
		viewer.setLabelProvider( new LabelProvider() );
		viewer.addSelectionChangedListener( this );

		if( this.input != null ) {
			viewer.setInput( this.input );
		}
	}

	protected List<Segment> parse( IDocument document )
	{
		final List<Segment> content = new ArrayList<Segment>();
		final int lines = document.getNumberOfLines();
		final int increment = Math.max( Math.round( lines / 10 ), 10 );

		for( int line = 0; line < lines; line += increment ) {

			int length = increment;
			if( line + increment > lines ) {
				length = lines - line;
			}

			try {

				final int offset = document.getLineOffset( line );
				final int end = document.getLineOffset( line + length );
				length = end - offset;
				final Position p = new Position( offset, length );
				document.addPosition( SEGMENTS, p );

				content.add( new Segment( "offset", p ) ); //$NON-NLS-1$

			}
			catch( final BadPositionCategoryException x ) {
			}
			catch( final BadLocationException x ) {
			}
		}

		return content;
	}

	/**
	 * Sets the input of the outline page
	 * 
	 * @param input
	 *            the input of this outline page
	 */
	void setInput( Object input )
	{
		this.input = input;

		update();
	}

	/**
	 * Updates the outline page.
	 */
	void update()
	{
		final TreeViewer viewer = getTreeViewer();

		if( viewer != null ) {
			final Control control = viewer.getControl();
			if( control != null && !control.isDisposed() ) {
				control.setRedraw( false );
				viewer.setInput( this.input );
				viewer.expandAll();
				control.setRedraw( true );
			}
		}
	}

}
