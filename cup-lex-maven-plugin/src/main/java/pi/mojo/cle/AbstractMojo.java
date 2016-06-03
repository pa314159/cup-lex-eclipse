
package pi.mojo.cle;

import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class AbstractMojo
extends org.apache.maven.plugin.AbstractMojo
{

	@Parameter( defaultValue = "${project}", readonly = true, required = true )
	protected MavenProject project;

}
