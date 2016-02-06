package pi.eclipse.cle.preferences;

import static pi.eclipse.cle.preferences.ClePreferences.CUP_OUTPUT;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_SYMBOLS_IF;
import static pi.eclipse.cle.preferences.ClePreferences.LEX_OUTPUT;
import static pi.eclipse.cle.preferences.ClePreferences.LEX_QUIET;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 * 
 */
public class PreferenceInitializer
extends AbstractPreferenceInitializer
{

	/**
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences()
	{
		final IPreferenceStore store = JavaPlugin.getDefault().getPreferenceStore();

		if( store.getBoolean( PreferenceConstants.SRCBIN_FOLDERS_IN_NEWPROJ ) ) {
			LEX_OUTPUT.setDefault( "" ); 
			CUP_OUTPUT.setDefault( "" ); 
		}
		else {
			final String src = store.getString( PreferenceConstants.SRCBIN_SRCNAME );

			LEX_OUTPUT.setDefault( src );
			CUP_OUTPUT.setDefault( src );
		}

		CUP_SYMBOLS_IF.setDefault( true );
		LEX_QUIET.setDefault( true );
	}

}
