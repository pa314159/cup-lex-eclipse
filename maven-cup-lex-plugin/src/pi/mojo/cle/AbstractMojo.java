package pi.mojo.cle;

import java.util.List;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

public abstract class AbstractMojo
extends org.apache.maven.plugin.AbstractMojo
{
	/**
	 * The project's resources.
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject			project;

	/**
	 * The project's resources.
	 * 
	 * @parameter expression="${component.org.apache.maven.project.MavenProjectHelper}"
	 */
	protected MavenProjectHelper	projectHelper;

	/**
	 * Used to look up Artifacts in the remote repository.
	 * 
	 * @parameter expression="${component.org.apache.maven.artifact.factory.ArtifactFactory}"
	 * @required
	 * @readonly
	 */
	protected ArtifactFactory		artifactFactory;

	/**
	 * Used to look up Artifacts in the remote repository.
	 * 
	 * @parameter expression="${component.org.apache.maven.artifact.resolver.ArtifactResolver}"
	 * @required
	 * @readonly
	 */
	protected ArtifactResolver		artifactResolver;

	/**
	 * Location of the local repository.
	 * 
	 * @parameter expression="${localRepository}"
	 * @readonly
	 * @required
	 */
	protected ArtifactRepository	localRepos;

	/**
	 * List of Remote Repositories used by the resolver
	 * 
	 * @parameter expression="${project.remoteArtifactRepositories}"
	 * @readonly
	 * @required
	 */
	protected List					remoteRepos;

}
