package pi.eclipse.cle.preferences;

import static pi.eclipse.cle.preferences.ClePreferences.CUP_COMPACT_RED;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_DEBUG;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_DUMP_ALL;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_DUMP_GRAMMAR;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_DUMP_STATES;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_DUMP_TABLES;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_EXPECTED_CONFLICTS;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_NON_TERMS;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_NO_POSITIONS;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_NO_SCANNER;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_OUTPUT;
import static pi.eclipse.cle.preferences.ClePreferences.CUP_SYMBOLS_IF;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import pi.eclipse.cle.ClePlugin;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By subclassing
 * <samp>FieldEditorPreferencePage</samp>, we can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the main
 * plug-in class. That way, preferences can be accessed directly via the preference store.
 * 
 */
public class CupPreferencePage
extends FieldEditorPreferencePage
implements IWorkbenchPreferencePage
{

	/**
	 * 
	 */
	public CupPreferencePage()
	{
		super( GRID );

		setPreferenceStore( ClePlugin.getDefault().getPreferenceStore() );
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various
	 * types of preferences. Each field editor knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors()
	{
		final Composite fep = getFieldEditorParent();

		addField( new StringFieldEditor( CUP_OUTPUT.toString(), CUP_OUTPUT.toLabel(), fep ) );
		addField( new IntegerFieldEditor( CUP_EXPECTED_CONFLICTS.toString(), CUP_EXPECTED_CONFLICTS.toLabel(), fep ) );
		addField( new BooleanFieldEditor( CUP_SYMBOLS_IF.toString(), CUP_SYMBOLS_IF.toLabel(), fep ) );
		addField( new BooleanFieldEditor( CUP_NON_TERMS.toString(), CUP_NON_TERMS.toLabel(), fep ) );
		addField( new BooleanFieldEditor( CUP_COMPACT_RED.toString(), CUP_COMPACT_RED.toLabel(), fep ) );
		addField( new BooleanFieldEditor( CUP_NO_POSITIONS.toString(), CUP_NO_POSITIONS.toLabel(), fep ) );
		addField( new BooleanFieldEditor( CUP_NO_SCANNER.toString(), CUP_NO_SCANNER.toLabel(), fep ) );
		addField( new BooleanFieldEditor( CUP_DEBUG.toString(), CUP_DEBUG.toLabel(), fep ) );
		addField( new BooleanFieldEditor( CUP_DUMP_ALL.toString(), CUP_DUMP_ALL.toLabel(), fep ) );
		addField( new BooleanFieldEditor( CUP_DUMP_GRAMMAR.toString(), CUP_DUMP_GRAMMAR.toLabel(), fep ) );
		addField( new BooleanFieldEditor( CUP_DUMP_STATES.toString(), CUP_DUMP_STATES.toLabel(), fep ) );
		addField( new BooleanFieldEditor( CUP_DUMP_TABLES.toString(), CUP_DUMP_TABLES.toLabel(), fep ) );
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init( IWorkbench workbench )
	{
	}

}
