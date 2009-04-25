package pi.eclipse.cle.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
class LexPartitionScanner
extends RuleBasedPartitionScanner
{
	enum TYPES
	{
		__lex_begin,
		__lex_usercode,
		__lex_option,
		__lex_definition,
		__lex_rule;

		final IToken	token	= new Token( name() );
	}

	static final String[]				LEX_PARTITION_TYPES;

	static final LexPartitionScanner	INSTANCE			= new LexPartitionScanner();

	static final String					LEX_PARTITIONING	= "__lex_partitioning";		//$NON-NLS-1$

	static {
		final List<String> types = new ArrayList<String>();
		final Enum[] values = TYPES.values();

		for( final Enum element : values ) {
			types.add( element.name() );
		}

		LEX_PARTITION_TYPES = types.toArray( new String[0] );
	}

	private LexPartitionScanner()
	{
		final List<IPredicateRule> rules = new ArrayList();

		rules.add( new EndOfLineRule( "//", Token.UNDEFINED ) ); //$NON-NLS-1$
		rules.add( createOptionRule() );

		setPredicateRules( rules.toArray( new IPredicateRule[0] ) );
	}

	/**
	 * @return
	 */
	private IPredicateRule createOptionRule()
	{
		final EndOfLineRule rule = new EndOfLineRule( "%", TYPES.__lex_option.token ); //$NON-NLS-1$

		rule.setColumnConstraint( 0 );

		return rule;
	}
}
