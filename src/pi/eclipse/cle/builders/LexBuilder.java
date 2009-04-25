package pi.eclipse.cle.builders;

import static pi.eclipse.cle.preferences.ClePreferences.LEX_DUMP;
import static pi.eclipse.cle.preferences.ClePreferences.LEX_QUIET;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import pi.eclipse.cle.ClePlugin;
import pi.eclipse.cle.properties.LexPrefs;
import JFlex.Main;
import JFlex.Options;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class LexBuilder
extends AbstractBuilder
{
	public static final String	ID	= ClePlugin.ID + "." + "LexBuilder";	//$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * @param ln
	 * @param br
	 * @param resource
	 * @throws IOException
	 */
	private void addLexError( IResource resource, String ln, BufferedReader br ) throws IOException
	{
		int lineValue = 0;
		String string = ln;

		if( ln.endsWith( ": " ) ) { //$NON-NLS-1$
			final int lineIndex = ln.indexOf( "(line " ); //$NON-NLS-1$

			if( lineIndex > 0 ) {
				final int endIndex = ln.indexOf( ')', lineIndex );
				final String lineString = ln.substring( lineIndex + 6, endIndex );

				lineValue = Integer.parseInt( lineString );

				string = br.readLine();
			}
		}

		addError( resource, string, lineValue );
	}

	/**
	 * @throws IOException
	 * @see pi.eclipse.cle.builders.AbstractBuilder#cleanJavaSource(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IResource)
	 */
	@Override
	protected void cleanJavaSource( IProgressMonitor progressMonitor, IResource resource ) throws IOException
	{
		final LexPrefs pref = new LexPrefs( resource );
		final File iFile = resource.getLocation().toFile();
		final IResource dest = resource.getProject().findMember( pref.getJavaFolder() );
		File fDest = dest.getLocation().toFile();
		final String packageName = findPackageName( iFile );

		if( packageName != null ) {
			fDest = new File( fDest, packageName.replace( '.', File.separatorChar ) );
		}

		fDest.delete();
	}

	/**
	 * @param bs
	 * @throws IOException
	 */
	@Override
	void collectErrors( IResource resource, char[] chars ) throws IOException
	{
		final MarkerTool t = new MarkerTool( resource, markerType() );
		final LexOutLex s = new LexOutLex( new CharArrayReader( chars ) );
		final LexOutCup p = new LexOutCup( s, t );

		try {
			p.parse();
		}
		catch( final Exception e ) {
			t.addError( e.getLocalizedMessage() );
		}
	}

	/**
	 * @throws CoreException
	 * @see pi.eclipse.cle.builders.AbstractBuilder#createJavaSource(IProgressMonitor,
	 *      org.eclipse.core.resources.IResource)
	 */
	@Override
	protected void createJavaSource( IProgressMonitor progressMonitor, IResource resource )
	throws IOException, CoreException
	{
		final LexPrefs pref = new LexPrefs( resource );
		final File iFile = resource.getLocation().toFile();
		final IResource dest = resource.getProject().findMember( pref.getJavaFolder() );
		File fDest = dest.getLocation().toFile();
		final StringBuilder args = new StringBuilder();
		final String packageName = findPackageName( iFile );

		if( packageName != null ) {
			fDest = new File( fDest, packageName.replace( '.', File.separatorChar ) );
		}

		args.append( " -d" ); //$NON-NLS-1$
		args.append( " \"" + fDest.getAbsolutePath() + "\"" ); //$NON-NLS-1$ //$NON-NLS-2$
		args.append( " --nobak" ); //$NON-NLS-1$

		if( pref.getSkipMin() ) {
			args.append( " --nomin" ); //$NON-NLS-1$
		}
		if( pref.getComply() ) {
			args.append( " --jlex" ); //$NON-NLS-1$
		}
		switch( pref.getCodeMethod() ) {
			case Options.PACK:
				args.append( " --pack" ); //$NON-NLS-1$
				break;
			case Options.TABLE:
				args.append( " --table" ); //$NON-NLS-1$
				break;
			case Options.SWITCH:
				args.append( " --switch" ); //$NON-NLS-1$
				break;
		}
		if( LEX_DUMP.getBoolean() ) {
			args.append( " --dump" ); //$NON-NLS-1$
		}
		if( LEX_QUIET.getBoolean() ) {
			args.append( " --quiet" ); //$NON-NLS-1$
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
		return "lex".equalsIgnoreCase( resource.getFileExtension() ); //$NON-NLS-1$
	}
}
