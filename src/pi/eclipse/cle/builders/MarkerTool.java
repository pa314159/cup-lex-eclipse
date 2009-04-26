package pi.eclipse.cle.builders;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
class MarkerTool
{
	final IResource	resource;

	final String	markerId;

	MarkerTool( IResource resource, String markerId )
	{
		this.resource = resource;
		this.markerId = markerId;
	}

	void addError( Object string, Integer... values )
	{
		addMarker( IMarker.SEVERITY_ERROR, string, values );
	}

	void addInfo( Object string, Integer... values )
	{
		addMarker( IMarker.SEVERITY_INFO, string, values );
	}

	/**
	 * @param severity
	 * @param resource
	 * @param string
	 * @param line
	 * @throws CoreException
	 */
	void addMarker( Integer severity, Object string, Integer... values )
	{
		try {
			final IMarker marker = this.resource.createMarker( this.markerId );
			Integer value = 0;

			marker.setAttribute( IMarker.MESSAGE, string.toString() );
			marker.setAttribute( IMarker.SEVERITY, severity );

			if( (values != null) && (values.length > 0) ) {
				value = values[0];
			}

			if( value < 1 ) {
				value = 1;
			}

			marker.setAttribute( IMarker.LINE_NUMBER, value );
		}
		catch( final CoreException e ) {
			e.printStackTrace();
		}
	}

	void addWarning( Object string, Integer... values )
	{
		addMarker( IMarker.SEVERITY_WARNING, string, values );
	}
}
