
package pi.eclipse.cle.builders;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import static pi.eclipse.cle.preferences.ClePreferences.LEX_DUMP;
import static pi.eclipse.cle.preferences.ClePreferences.LEX_QUIET;

import jflex.Main;
import pi.eclipse.cle.ClePlugin;
import pi.eclipse.cle.properties.LexPrefs;

/**
 */
public class LexBuilder
extends AbstractBuilder
{

	public static final String ID = ClePlugin.ID + "." + "LexBuilder"; 

	public LexBuilder()
	{
		super( "lex" );
	}

	private void addLexError( IResource resource, String ln, BufferedReader br ) throws IOException
	{
		int lineValue = 0;
		String string = ln;

		if( ln.endsWith( ": " ) ) { 
			final int lineIndex = ln.indexOf( "(line " ); 

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

		if( dest == null ) {
			return;
		}

		File fDest = dest.getLocation().toFile();
		final String[] packageAndClass = findPackageAndClass( iFile );

		if( ( packageAndClass[0] != null ) && ( packageAndClass[1] != null ) ) {
			fDest = new File( fDest, packageAndClass[0].replace( '.', File.separatorChar ) );

			fDest = new File( fDest, packageAndClass[1] + ".java" );

			fDest.delete();
		}
	}

	/**
	 * @throws CoreException
	 * @see pi.eclipse.cle.builders.AbstractBuilder#createJavaSource(IProgressMonitor,
	 *      org.eclipse.core.resources.IResource)
	 */
	@Override
	protected void createJavaSource( IProgressMonitor progressMonitor, IResource resource ) throws IOException, CoreException
	{
		final LexPrefs pref = new LexPrefs( resource );
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

		args.append( " -d" ); 
		args.append( " \"" + fDest.getAbsolutePath() + "\"" ); 
		args.append( " --nobak" ); 

		if( pref.getSkipMin() ) {
			args.append( " --nomin" ); 
		}
		if( pref.getComply() ) {
			args.append( " --jlex" ); 
		}
		//		switch( pref.getCodeMethod() ) {
		//			case Options.PACK:
		//				args.append( " --pack" ); 
		//				break;
		//			case Options.TABLE:
		//				args.append( " --table" ); 
		//				break;
		//			case Options.SWITCH:
		//				args.append( " --switch" ); 
		//				break;
		//		}
		if( LEX_DUMP.getBoolean() ) {
			args.append( " --dump" ); 
		}
		if( LEX_QUIET.getBoolean() ) {
			args.append( " --quiet" ); 
		}

		args.append( " \"" + iFile.getAbsolutePath() + "\"" ); 

		fDest.mkdirs();

		launchJava( progressMonitor, pref, Main.class, args.toString() );
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
		final LexOut p = new LexOut( s, t );

		try {
			p.parse();
		}
		catch( final Exception e ) {
			t.addError( e.getLocalizedMessage() );
		}
	}
}
