package pi.eclipse.cle;

import java.util.Formatter;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class CleStrings
{
	private static final CleLog		L				= new CleLog( CleStrings.class );

	private static final String		BUNDLE_NAME		= "pi.eclipse.cle.translations";	//$NON-NLS-1$

	private static ResourceBundle	RESOURCE_BUNDLE	= null;

	public static String get( String key, Object... objects )
	{
		try {
			if( RESOURCE_BUNDLE == null ) {
				RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );
			}

			final Formatter f = new Formatter();
			final String x = RESOURCE_BUNDLE.getString( key );

			if( x != null ) {
				return f.format( x, objects ).toString();
			}
		}
		catch( final MissingResourceException e ) {
			L.warn( e, "%s", key ); //$NON-NLS-1$
		}
		catch( final IllegalArgumentException e ) {
			L.warn( e, "%s", key ); //$NON-NLS-1$
		}

		return '!' + key + '!';
	}

	private CleStrings()
	{
	}
}
