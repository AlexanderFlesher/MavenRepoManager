package localapp.mavenrepomanager;

import java.io.IOException;
import java.nio.file.Path;

public final class RepoManager implements IRunnable {
    @Override
    public void run(RunSettings settings) {
        ClasspathFile file = new ClasspathFile(settings.classpathFilePath.toFile());
        Path absoluteName = Path.of(settings.repoPath.toString(), settings.repoName);
        Repository repository = new RepositoryProcess(absoluteName.toString(), file);
        PomDeps depXml = new PomDeps(settings, repository.getClasspathEntries());
        try {
            repository.write();
            depXml.write();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
