package pi.cle.exec;

import java.io.File;

import org.apache.tools.ant.Target;

import JFlex.anttask.JFlexTask;

public class LexConfig
extends AbstractConfig
{
	private final JFlexTask	task	= new JFlexTask();

	public LexConfig()
	{
	}

	@Override
	public JFlexTask createTask( Target target, File source, String destDir ) throws CloneNotSupportedException
	{
		final JFlexTask t = (JFlexTask) this.task.clone();

		t.setProject( target.getProject() );
		t.setOwningTarget( target );
		t.setFile( source );
		t.setDestdir( new File( destDir ) );

		return t;
	}

	public void setDot( boolean b )
	{
		this.task.setDot( b );
	}

	public void setDump( boolean b )
	{
		this.task.setDump( b );
	}

	public void setGenerateDot( boolean genDot )
	{
		this.task.setGenerateDot( genDot );
	}

	public void setJLex( boolean b )
	{
		this.task.setJLex( b );
	}

	public void setNobak( boolean b )
	{
		this.task.setNobak( b );
	}

	public void setNomin( boolean b )
	{
		this.task.setNomin( b );
	}

	public void setPack( boolean b )
	{
		this.task.setPack( b );
	}

	public void setSkel( File skeleton )
	{
		this.task.setSkel( skeleton );
	}

	public void setSkeleton( File skeleton )
	{
		this.task.setSkeleton( skeleton );
	}

	public void setSkipMinimization( boolean skipMin )
	{
		this.task.setSkipMinimization( skipMin );
	}

	public void setSwitch( boolean b )
	{
		this.task.setSwitch( b );
	}

	public void setTable( boolean b )
	{
		this.task.setTable( b );
	}

	public void setTime( boolean displayTime )
	{
		this.task.setTime( displayTime );
	}

	public void setTimeStatistics( boolean displayTime )
	{
		this.task.setTimeStatistics( displayTime );
	}

	public void setVerbose( boolean verbose )
	{
		this.task.setVerbose( verbose );
	}

}
