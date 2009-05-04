package pi.eclipse.cle.preferences;

import static pi.eclipse.cle.preferences.ClePreferences.LEX_CODE_METHOD;
import static pi.eclipse.cle.preferences.ClePreferences.LEX_COMPLY_JLEX;
import static pi.eclipse.cle.preferences.ClePreferences.LEX_DUMP;
import static pi.eclipse.cle.preferences.ClePreferences.LEX_OUTPUT;
import static pi.eclipse.cle.preferences.ClePreferences.LEX_QUIET;
import static pi.eclipse.cle.preferences.ClePreferences.LEX_SKIP_MIN;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import pi.eclipse.cle.ClePlugin;
import pi.eclipse.cle.CleStrings;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class LexPreferencePage
extends FieldEditorPreferencePage
implements IWorkbenchPreferencePage
{

	/**
	 * 
	 */
	public LexPreferencePage()
	{
		super( GRID );

		setPreferenceStore( ClePlugin.getDefault().getPreferenceStore() );
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init( IWorkbench workbench )
	{
	}

	/**
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	protected void createFieldEditors()
	{
		final Composite fep = getFieldEditorParent();
		final String[][] val = new String[][] {
		//
			{ CleStrings.get( "preference-lex-pack" ), "preference-lex-pack" }, //$NON-NLS-1$ //$NON-NLS-2$
			//
			{ CleStrings.get( "preference-lex-table" ), "preference-lex-table" }, //$NON-NLS-1$ //$NON-NLS-2$
			//
			{ CleStrings.get( "preference-lex-switch" ), "preference-lex-switch" }, //$NON-NLS-1$ //$NON-NLS-2$
		//
		};

		addField( new StringFieldEditor( LEX_OUTPUT.toString(), LEX_OUTPUT.toLabel(), fep ) );
		addField( new BooleanFieldEditor( LEX_SKIP_MIN.toString(), LEX_SKIP_MIN.toLabel(), fep ) );
		addField( new BooleanFieldEditor( LEX_COMPLY_JLEX.toString(), LEX_COMPLY_JLEX.toLabel(), fep ) );
		addField( new RadioGroupFieldEditor( LEX_CODE_METHOD.toString(), LEX_CODE_METHOD.toLabel(), 1, val, fep ) );
		addField( new BooleanFieldEditor( LEX_DUMP.toString(), LEX_DUMP.toLabel(), fep ) );
		addField( new BooleanFieldEditor( LEX_QUIET.toString(), LEX_QUIET.toLabel(), fep ) );
	}
}
