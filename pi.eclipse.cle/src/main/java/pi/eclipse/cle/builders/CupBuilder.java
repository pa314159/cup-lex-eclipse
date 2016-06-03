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
 */
public class CupBuilder
extends AbstractBuilder
{

	public static final String	ID	= ClePlugin.ID + "." + "CupBuilder";	

	public CupBuilder()
	{
		super( "cup" );
	}

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
	 * @throws IOException
	 * @see pi.eclipse.cle.builders.AbstractBuilder#cleanJavaSource(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IResource)
	 */
	@Override
	protected void cleanJavaSource( IProgressMonitor progressMonitor, IResource resource ) throws IOException
	{
		final CupPrefs pref = new CupPrefs( resource );
		final File iFile = resource.getLocation().toFile();
		final IResource dest = resource.getProject().findMember( pref.getJavaFolder() );

		if( dest == null ) {
			return;
		}

		File fDest = dest.getLocation().toFile();
		final String packageName = findPackageAndClass( iFile )[0];

		if( packageName != null ) {
			fDest = new File( fDest, packageName.replace( '.', File.separatorChar ) );

			final File fCup = new File( fDest, pref.getParserClass() + ".java" );
			final File fSym = new File( fDest, pref.getSymbolsClass() + ".java" );

			fCup.delete();
			fSym.delete();
		}
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

		if( dest == null ) {
			return;
		}

		File fDest = dest.getLocation().toFile();
		final StringBuilder args = new StringBuilder();
		final String packageName = findPackageAndClass( iFile )[0];

		if( packageName != null ) {
			fDest = new File( fDest, packageName.replace( '.', File.separatorChar ) );
		}

		args.append( " -parser" ); 
		args.append( " " + pref.getParserClass() ); 

		args.append( " -symbols" ); 
		args.append( " " + pref.getSymbolsClass() ); 

		args.append( " -destdir" ); 
		args.append( " \"" + fDest.getAbsolutePath() + "\"" ); 

		final int ec = pref.getExpectedConflicts();

		if( ec > 0 ) {
			args.append( " -expect " );
			args.append( ec );
		}
		if( pref.getSymbolsIf() ) {
			args.append( " -interface" ); 
		}
		if( pref.getNonTerms() ) {
			args.append( " -nonterms" ); 
		}
		if( pref.getCompactRed() ) {
			args.append( " -compact_red" ); 
		}
		if( pref.getNoPositions() ) {
			args.append( " -nopositions" ); 
		}
		if( pref.getNoScanner() ) {
			args.append( " -noscanner" ); 
		}
		if( CUP_DEBUG.getBoolean() ) {
			args.append( " -debug" ); 
		}
		if( CUP_DUMP_ALL.getBoolean() ) {
			args.append( " -dump" ); 
		}
		else {
			if( CUP_DUMP_GRAMMAR.getBoolean() ) {
				args.append( " -dump_grammar" ); 
			}
			if( CUP_DUMP_STATES.getBoolean() ) {
				args.append( " -dump_states" ); 
			}
			if( CUP_DUMP_TABLES.getBoolean() ) {
				args.append( " -dump_tables" ); 
			}
		}
		args.append( " \"" + iFile.getAbsolutePath() + "\"" ); 

		fDest.mkdirs();

		launchJava( progressMonitor, pref, Main.class, args.toString() );
	}

	/**
	 * @param chars
	 * @throws CoreException
	 * @throws IOException
	 */
	@Override
	void collectErrors( IResource resource, char[] chars )
	{
		final MarkerTool t = new MarkerTool( resource, markerType() );
		final CupOutLex s = new CupOutLex( new CharArrayReader( chars ) );
		final CupOut p = new CupOut( s, t );

		try {
			p.parse();
		}
		catch( final Exception e ) {
			t.addError( e.getLocalizedMessage() );
		}
	}
}
