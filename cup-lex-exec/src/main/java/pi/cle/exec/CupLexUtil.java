package pi.cle.exec;

public final class CupLexUtil
{
	public static String toJavaName( String name, boolean upperFirst )
	{
		final int size = name.length();
		final char[] ncChars = name.toCharArray();
		int next = 0;
		boolean uppercase = upperFirst;

		for( int i = 0; i < size; i++ ) {
			final char ch = ncChars[i];

			switch( ch ) {
				case '_':
				case '-':
					uppercase = true;

					break;

				default:
					if( uppercase == true ) {
						ncChars[next] = Character.toUpperCase( ch );
						uppercase = false;
					}
					else {
						ncChars[next] = ch;
					}

					++next;

					break;
			}
		}

		return new String( ncChars, 0, next );
	}

	private CupLexUtil()
	{
	}

}
