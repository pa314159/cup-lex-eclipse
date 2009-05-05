package pi.eclipse.cle.builders;

import java.io.CharArrayWriter;

import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IStreamMonitor;

import pi.eclipse.cle.properties.AbstractPref;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
final class JavaProcessData
implements IStreamListener
{
	final AbstractPref		preferences;

	final CharArrayWriter	out	= new CharArrayWriter();

	JavaProcessData( AbstractPref preferences )
	{
		this.preferences = preferences;
	}

	public void streamAppended( String text, IStreamMonitor monitor )
	{
		System.out.print( text );
		System.out.flush();

		this.out.append( text );
	}
}
