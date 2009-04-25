package pi.eclipse.cle.builders;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import pi.eclipse.cle.ClePlugin;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
public class CupLexNature
implements IProjectNature
{
	public static final String	ID	= ClePlugin.ID + "." + "CupLexNature";	//$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * Toggles sample nature on a project
	 * 
	 * @param project
	 *            to have sample nature added or removed
	 */
	static public void addNature( IProject project )
	{
		try {
			project.refreshLocal( IResource.DEPTH_INFINITE, null );

			final IProjectDescription description = project.getDescription();
			final String[] natures = description.getNatureIds();

			for( int i = 0; i < natures.length; ++i ) {
				if( ID.equals( natures[i] ) ) {
					return;
				}
			}

			// Add the nature
			final String[] newNatures = new String[natures.length + 1];
			System.arraycopy( natures, 0, newNatures, 1, natures.length );
			newNatures[0] = ID;
			description.setNatureIds( newNatures );
			project.setDescription( description, null );
		}
		catch( final CoreException e ) {
			e.printStackTrace();
		}
	}

	private IProject	project;

	/**
	 * @see org.eclipse.core.resources.IProjectNature#configure()
	 */
	public void configure() throws CoreException
	{
		final IProjectDescription desc = this.project.getDescription();
		final ICommand[] commands = desc.getBuildSpec();

		for( int i = 0; i < commands.length; ++i ) {
			if( commands[i].getBuilderName().equals( CupBuilder.ID ) ) {
				return;
			}
		}

		final ICommand[] newCommands = new ICommand[commands.length + 2];

		System.arraycopy( commands, 0, newCommands, 2, commands.length );

		newCommands[0] = desc.newCommand();
		newCommands[0].setBuilderName( CupBuilder.ID );
		newCommands[1] = desc.newCommand();
		newCommands[1].setBuilderName( LexBuilder.ID );

		desc.setBuildSpec( newCommands );

		this.project.setDescription( desc, null );
		this.project.refreshLocal( IResource.DEPTH_INFINITE, null );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#deconfigure()
	 */
	public void deconfigure() throws CoreException
	{
		final ICommand[] commands = getProject().getDescription().getBuildSpec();
		int cupIx = -1;
		int lexIx = -1;

		for( int i = 0; i < commands.length; ++i ) {
			if( commands[i].getBuilderName().equals( CupBuilder.ID ) ) {
				cupIx = i;
			}
			if( commands[i].getBuilderName().equals( LexBuilder.ID ) ) {
				lexIx = i;
			}
		}

		if( cupIx >= 0 ) {
			removeBuilder( cupIx );
		}
		if( lexIx >= 0 ) {
			removeBuilder( lexIx );
		}

		this.project.refreshLocal( IResource.DEPTH_INFINITE, null );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#getProject()
	 */
	public IProject getProject()
	{
		return this.project;
	}

	/**
	 * @param cupIx
	 * @throws CoreException
	 */
	private void removeBuilder( int index ) throws CoreException
	{
		final IProjectDescription description = getProject().getDescription();
		final ICommand[] commands = description.getBuildSpec();
		final ICommand[] newCommands = new ICommand[commands.length - 1];

		System.arraycopy( commands, 0, newCommands, 0, index );
		System.arraycopy( commands, index + 1, newCommands, index, commands.length - 1 - index );

		description.setBuildSpec( newCommands );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject)
	 */
	public void setProject( IProject project )
	{
		this.project = project;
	}

}
