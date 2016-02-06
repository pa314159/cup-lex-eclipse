package pi.eclipse.cle.wizards;

import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.SimpleLog4JLogSystem;

import pi.eclipse.cle.ClePlugin;

/**
 */
class FileGen
implements RuntimeConstants
{
	/** */
	private static final Class	CLASS	= FileGen.class;

	static {
		try {
			Velocity.setProperty( RESOURCE_LOADER, "custom" ); 
			Velocity.setProperty( "custom.resource.loader.class", FileGenLoader.class.getName() ); 
			Velocity.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, ClePlugin.isLogEnabled()
				? SimpleLog4JLogSystem.class.getName() : "org.apache.velocity.runtime.log.NullLogSystem" ); 
			Velocity.init();
		}
		catch( final Exception e ) {
			throw new ExceptionInInitializerError( e );
		}
	}

	private final Template		template;

	FileGen( String template ) throws Exception
	{
		this.template = Velocity.getTemplate( template );
	}

	/**
	 * @param vc
	 * @param out
	 * @throws Exception
	 * @throws MethodInvocationException
	 * @throws ParseErrorException
	 * @throws ResourceNotFoundException
	 */
	void generate( VelocityContext vc, Writer out ) throws Exception
	{
		this.template.merge( vc, out );
	}

}
