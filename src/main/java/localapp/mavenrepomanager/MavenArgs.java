package localapp.mavenrepomanager;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class MavenArgs {
    protected enum OperatingSystem{
        UNIX,
        WINDOWS
    }

    private final static String maven = resolveMvn("mvn");
    private final static String command = "org.apache.maven.plugins:maven-install-plugin:2.4:install-file";
    private final static String file = "-Dfile=%s";
    private final static String group = "-DgroupId=%s";
    private final static String artifact = "-DartifactId=%s";
    private final static String version = "-Dversion=%s";
    private final static String packaging = "-Dpackaging=jar";
    private final static String repoPath = "-DlocalRepositoryPath=%s";
    
    public static String[] from(Entry entry, RunSettings settings){
        List<String> args = new ArrayList<>();
        if (prependOsArgs(getOperatingSystem()) != "") 
            args.add(prependOsArgs(getOperatingSystem()));
        args.add(maven);
        args.add(command);
        args.add(packaging);
        args.add(String.format(file, entry.getDependency().getPath().toString()));
        args.add(String.format(group, entry.getGroup()));
        args.add(String.format(artifact, entry.getArtifact()));
        args.add(String.format(version, entry.getVersion()));
        args.add(String.format(repoPath, 
            Path.of(settings.repoPath.toString(), settings.repoName).toString()));
        args.add("-X");
        return args.toArray(new String[args.size()]);
    }

    protected static OperatingSystem getOperatingSystem(){
        return File.pathSeparatorChar == ';' ? OperatingSystem.WINDOWS : OperatingSystem.UNIX;
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
                }
            }
        }
        return maven;
    }

    protected static String prependOsArgs(OperatingSystem system){
        String result;
        switch(system){
            case UNIX:
                result = "";
                break;
            case WINDOWS:
                result = "cmd /C";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }
}
