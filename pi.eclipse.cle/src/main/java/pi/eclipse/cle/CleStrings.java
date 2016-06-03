package pi.eclipse.cle;

import java.util.Formatter;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 */
public class CleStrings
{
	private static final CleLog		L				= new CleLog( CleStrings.class );

	private static final String		BUNDLE_NAME		= "pi.eclipse.cle.translations";	

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
			L.warn( e, "%s", key ); 
		}
		catch( final IllegalArgumentException e ) {
			L.warn( e, "%s", key ); 
		}

		return '!' + key + '!';
	}

	private CleStrings()
	{
	}
}
