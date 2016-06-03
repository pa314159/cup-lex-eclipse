package pi.cle.exec;

import java.io.File;

import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

public abstract class AbstractConfig
{
	public abstract Task createTask( Target t, File source, String destDir ) throws CloneNotSupportedException;
}
