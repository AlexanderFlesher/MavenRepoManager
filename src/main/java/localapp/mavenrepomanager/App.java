package localapp.mavenrepomanager;

import java.io.IOException;
import java.nio.file.Path;

public class App 
{
    public static void main( String[] args )
    {
        RunSettings settings = ArgParser.parse(args);
        ClasspathFile file = new ClasspathFile(settings.classpathFilePath);
        Path absoluteName = Path.of(settings.repoPath, settings.repoName);
        Repository repository = new Repository(absoluteName.toString(), file);
        try {
            repository.write();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
