package pi.eclipse.cle;

import java.util.logging.Logger;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class CleLog
{
	static class Level
	extends java.util.logging.Level
	{
		private static final long				serialVersionUID	= 1L;

		static final java.util.logging.Level	FATAL				= new Level( "FATAL", 1100 );

		protected Level( String name, int value )
		{
			super( name, value );
		}
	}

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
		this.L = Logger.getLogger( category.getName() );
	}

	/**
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void debug( String s, Object... o )
	{
		log( java.util.logging.Level.FINE, s, o );
	}

	/**
	 * @param t
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void debug( Throwable t, String s, Object... o )
	{
		log( java.util.logging.Level.FINE, t, s, o );
	}

	/**
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void error( String s, Object... o )
	{
		log( java.util.logging.Level.SEVERE, s, o );
	}

	/**
	 * @param t
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void error( Throwable t, String s, Object... o )
	{
		log( java.util.logging.Level.SEVERE, t, s, o );
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
		log( java.util.logging.Level.INFO, s, o );
	}

	/**
	 * @param t
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void info( Throwable t, String s, Object... o )
	{
		log( java.util.logging.Level.INFO, t, s, o );
	}

	/**
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void trace( String s, Object... o )
	{
		log( java.util.logging.Level.FINEST, s, o );
	}

	/**
	 * @param t
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void trace( Throwable t, String s, Object... o )
	{
		log( java.util.logging.Level.FINEST, t, s, o );
	}

	/**
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void warn( String s, Object... o )
	{
		log( java.util.logging.Level.WARNING, s, o );
	}

	/**
	 * @param t
	 * @param s
	 * @param o
	 *            TODO
	 */
	public void warn( Throwable t, String s, Object... o )
	{
		log( java.util.logging.Level.WARNING, t, s, o );
	}

	/**
	 * @param l
	 * @param s
	 * @param o
	 *            TODO
	 */
	void log( java.util.logging.Level l, String s, Object[] o )
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
	void log( java.util.logging.Level l, Throwable t, String s, Object[] o )
	{
		if( ClePlugin.isLogEnabled() && this.L.isLoggable( l ) ) {
			final Throwable tt = new Throwable();
			final StackTraceElement st[] = tt.getStackTrace();

			// Caller will be the fourth element
			String cn = "unknown";
			String mn = "unknown";

			final int DEPTH = 3;

			if( (st != null) && (st.length > DEPTH) ) {
				cn = st[DEPTH].getClassName();
				mn = st[DEPTH].getMethodName();
			}

			if( t == null ) {
				this.L.logp( l, cn, mn, String.format( s, o ) );
			}
			else {
				this.L.logp( l, cn, mn, String.format( s, o ), t );
			}
		}
	}
}
