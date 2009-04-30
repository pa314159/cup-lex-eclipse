package pi.eclipse.cle.builders;

import static pi.eclipse.cle.preferences.ClePreferences.CUP_DEBUG;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_DUMP_ALL;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_DUMP_GRAMMAR;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_DUMP_STATES;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_DUMP_TABLES;

import java.io.CharArrayReader;
import java.io.File;
import java.io.IOException;

import java_cup.Main;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import pi.eclipse.cle.ClePlugin;
import pi.eclipse.cle.properties.CupPrefs;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class CupBuilder
extends AbstractBuilder
{

	public static final String	ID	= ClePlugin.ID + "." + "CupBuilder";	//$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * @param resource
	 * @param string
	 */
	private void addCupError( IResource resource, String string )
	{
		final String ln = string.substring( 1, string.indexOf( '/' ) );
		final String tx = string.substring( string.indexOf( ':' ) + 1 );

		addError( resource, tx.trim(), Integer.parseInt( ln ) );
	}

	/**
	 * @param resource
	 * @param string
	 */
	private void addCupWarning( IResource resource, String string )
	{
		final String ln = string.substring( 0, string.indexOf( '(' ) );
		final String tx = string.substring( string.indexOf( ':' ) + 1 );

		addWarning( resource, tx.trim(), Integer.parseInt( ln ) );
	}

	/**
	 * @see pi.eclipse.cle.builders.AbstractBuilder#cleanJavaSource(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IResource)
	 */
	@Override
	protected void cleanJavaSource( IProgressMonitor progressMonitor, IResource resource )
	{
	}

	/**
	 * @param progressMonitor
	 * @throws CoreException
	 * @see pi.eclipse.cle.builders.AbstractBuilder#createJavaSource(IProgressMonitor,
	 *      org.eclipse.core.resources.IResource)
	 */
	@Override
	protected void createJavaSource( IProgressMonitor progressMonitor, IResource resource )
	throws IOException, CoreException
	{
		final CupPrefs pref = new CupPrefs( resource );
		final File iFile = resource.getLocation().toFile();
		final IResource dest = resource.getProject().findMember( pref.getJavaFolder() );
		File fDest = dest.getLocation().toFile();
		final StringBuilder args = new StringBuilder();
		final String packageName = findPackageName( iFile );

		if( packageName != null ) {
			fDest = new File( fDest, packageName.replace( '.', File.separatorChar ) );
		}

		args.append( " -parser" ); //$NON-NLS-1$
		args.append( " " + pref.getParserClass() ); //$NON-NLS-1$

		args.append( " -symbols" ); //$NON-NLS-1$
		args.append( " " + pref.getSymbolsClass() ); //$NON-NLS-1$

		args.append( " -destdir" ); //$NON-NLS-1$
		args.append( " \"" + fDest.getAbsolutePath() + "\"" ); //$NON-NLS-1$ //$NON-NLS-2$

		final int ec = pref.getExpectedConflicts();

		if( ec > 0 ) {
			args.append( " -expect " );
			args.append( ec );
		}
		if( pref.getSymbolsIf() ) {
			args.append( " -interface" ); //$NON-NLS-1$
		}
		if( pref.getNonTerms() ) {
			args.append( " -nonterms" ); //$NON-NLS-1$
		}
		if( pref.getCompactRed() ) {
			args.append( " -compact_red" ); //$NON-NLS-1$
		}
		if( pref.getNoPositions() ) {
			args.append( " -nopositions" ); //$NON-NLS-1$
		}
		if( pref.getNoScanner() ) {
			args.append( " -noscanner" ); //$NON-NLS-1$
		}
		if( CUP_DEBUG.getBoolean() ) {
			args.append( " -debug" ); //$NON-NLS-1$
		}
		if( CUP_DUMP_ALL.getBoolean() ) {
			args.append( " -dump" ); //$NON-NLS-1$
		}
		else {
			if( CUP_DUMP_GRAMMAR.getBoolean() ) {
				args.append( " -dump_grammar" ); //$NON-NLS-1$
			}
			if( CUP_DUMP_STATES.getBoolean() ) {
				args.append( " -dump_states" ); //$NON-NLS-1$
			}
			if( CUP_DUMP_TABLES.getBoolean() ) {
				args.append( " -dump_tables" ); //$NON-NLS-1$
			}
		}
		args.append( " \"" + iFile.getAbsolutePath() + "\"" ); //$NON-NLS-1$ //$NON-NLS-2$

		fDest.mkdirs();

		launchJava( progressMonitor, pref, Main.class, args.toString() );
	}

	/**
	 * @see pi.eclipse.cle.builders.AbstractBuilder#resourceMatches(org.eclipse.core.resources.IResource)
	 */
	@Override
	protected boolean resourceMatches( IResource resource )
	{
		return "cup".equalsIgnoreCase( resource.getFileExtension() ); //$NON-NLS-1$
	}

	/**
	 * @param chars
	 * @throws CoreException
	 * @throws IOException
	 */
	@Override
	void collectErrors( IResource resource, char[] chars )
	{
		// BufferedReader br = new BufferedReader();
		// String ln = null;
		//
		// while( (ln = br.readLine()) != null ) {
		// // System.out.println(ln);
		//
		// L.trace("CUP: %s", ln); //$NON-NLS-1$
		//
		// try {
		// if( ln.startsWith("Error ") ) { //$NON-NLS-1$
		// addCupError(resource, ln.substring(6)); //$NON-NLS-1$
		// }
		// if( ln.startsWith("Fatal ") ) { //$NON-NLS-1$
		// addCupError(resource, ln.substring(6)); //$NON-NLS-1$
		// }
		// if( ln.startsWith("Warning ") ) { //$NON-NLS-1$
		// addCupWarning(resource, ln.substring(8)); //$NON-NLS-1$
		// }
		// }
		// catch( Throwable t ) {
		// addError(resource, t.getLocalizedMessage(), 0);
		// }
		// }
		final MarkerTool t = new MarkerTool( resource, markerType() );
		final CupOutScanner s = new CupOutScanner( new CharArrayReader( chars ) );
		final CupOutParser p = new CupOutParser( s, t );

		try {
			p.parse();
		}
		catch( final Exception e ) {
			t.addError( e.getLocalizedMessage() );
		}
	}
}
