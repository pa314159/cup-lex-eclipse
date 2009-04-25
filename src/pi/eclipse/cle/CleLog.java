package pi.eclipse.cle;

import java.util.Formatter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class CleLog
{
	/**  */
	static final Class	CLASS	= CleLog.class;

	/**  */
	static final String	FQCN	= CLASS.getName();

	/**  */
	final Logger		L;

	/**
	 * Creates a new CleLog object.
	 * 
	 * @param category
	 */
	public CleLog( Class category )
	{
		this.L = Logger.getLogger( category );
	}

	/**
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void debug( String s, Object... o )
	{
		log( Level.DEBUG, s, o );
	}

	/**
	 * @param t
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void debug( Throwable t, String s, Object... o )
	{
		log( Level.DEBUG, t, s, o );
	}

	/**
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void error( String s, Object... o )
	{
		log( Level.ERROR, s, o );
	}

	/**
	 * @param t
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void error( Throwable t, String s, Object... o )
	{
		log( Level.ERROR, t, s, o );
	}

	/**
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void fatal( String s, Object... o )
	{
		log( Level.FATAL, s, o );
	}

	/**
	 * @param t
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void fatal( Throwable t, String s, Object... o )
	{
		log( Level.FATAL, t, s, o );
	}

	/**
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void info( String s, Object... o )
	{
		log( Level.INFO, s, o );
	}

	/**
	 * @param t
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void info( Throwable t, String s, Object... o )
	{
		log( Level.INFO, t, s, o );
	}

	/**
	 * @param l
	 * @param s
	 * @param o
	 *            TODO
	 */
	void log( Level l, String s, Object[] o )
	{
		log( l, null, s, o );
	}

	/**
	 * @param l
	 * @param t
	 * @param s
	 * @param o
	 *            TODO
	 */
	void log( Level l, Throwable t, String s, Object[] o )
	{
		if( ClePlugin.isLogEnabled() && this.L.isEnabledFor( l ) ) {
			final Formatter f = new Formatter();

			f.format( s, o );

			this.L.log( FQCN, l, f, t );
		}
	}

	/**
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void trace( String s, Object... o )
	{
		log( Level.TRACE, s, o );
	}

	/**
	 * @param t
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void trace( Throwable t, String s, Object... o )
	{
		log( Level.TRACE, t, s, o );
	}

	/**
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void warn( String s, Object... o )
	{
		log( Level.WARN, s, o );
	}

	/**
	 * @param t
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void warn( Throwable t, String s, Object... o )
	{
		log( Level.WARN, t, s, o );
	}
}
