package pi.eclipse.cle.wizards;

import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

/**
 */
public class FileGenLoader
extends ResourceLoader
{
	/** */
	private static final Class	CLASS	= FileGenLoader.class;

	/**
	 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#getLastModified(org.apache.velocity.runtime.resource.Resource)
	 */
	@Override
	public long getLastModified( Resource resource )
	{
		return 0;
	}

	/**
	 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#getResourceStream(java.lang.String)
	 */
	@Override
	public InputStream getResourceStream( String source ) throws ResourceNotFoundException
	{
		return CLASS.getResourceAsStream( source );
	}

	/**
	 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#init(org.apache.commons.collections.ExtendedProperties)
	 */
	@Override
	public void init( ExtendedProperties configuration )
	{
	}

	/**
	 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#isSourceModified(org.apache.velocity.runtime.resource.Resource)
	 */
	@Override
	public boolean isSourceModified( Resource resource )
	{
		return false;
	}
}
