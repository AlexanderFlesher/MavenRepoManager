package localapp.mavenrepomanager;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Collection;

public class MavenArgs {
    private final static String maven = resolveMvn("mvn");
    private final static String command = "org.apache.maven.plugins:maven-install-plugin:2.4:install-file";
    private final static String file = "-Dfile=%s";
    private final static String group = "-DgroupId=%s";
    private final static String artifact = "-DartifactId=%s";
    private final static String version = "-Dversion=%s";
    private final static String packaging = "-Dpackaging=jar";
    private final static String repoPath = "-DlocalRepositoryPath=%s";
    
    public static String[] from(Entry entry, RunSettings settings){
        String[] args = new String[9];
        args[0] = maven;
        args[1] = command;
        args[2] = packaging;
        args[3] = String.format(file, entry.getDependency().getPath().toString());
        args[4] = String.format(group, entry.getGroup());
        args[5] = String.format(artifact, entry.getArtifact());
        args[6] = String.format(version, entry.getVersion());
        args[7] = String.format(repoPath, 
            Path.of(settings.repoPath.toString(), settings.repoName).toString());
        args[8] = "-X";
        return args;
    }

    protected static String resolveMvn(String maven){
        Collection<String> strings = System.getenv().values();
        for (String env : strings){
            for (String path : env.split(File.pathSeparator)){
                try{
                    Path combined = Path.of(path, maven);
                    if (combined.toFile().exists())
                        return combined.toString();
                }
                catch (InvalidPathException ex){
                    ex.printStackTrace();
                }
            }
        }
        return maven;
    }
}
