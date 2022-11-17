package localapp.mavenrepomanager;

import java.nio.file.Path;

/**
 * Contains a static parse method used to create a Repository.Entry from the filename of
 * a classpath entry.
 */
public class EntryParser {
    public static final String DEFAULT_VERSION = "1.0";

    /**
     * Creates a Repository.Entry based on the filename from the classpath entry. Will
     * attempt to get the version from the filename. 
     * @param filename
     * @return a Repository.Entry based on the filename from the classpath entry.
     */
    public static Repository.Entry parse(String filename){
        String artifact, group, version;
        Path file = Path.of(filename).getFileName();
        version = parseVersion(file.toString());
        artifact = parseArtifact(file.toString(), version);
        group = artifact;
        return new Repository.Entry(artifact, group, version);
    }

    private static boolean beginsWithNumber(String s){
        return s.charAt(0) >= '0' && s.charAt(0) <= '9';
    }

    private static String parseArtifact(String name, String version){
        return name.replaceAll("-" + version, "").replace(".jar", "");
    }

    private static String parseVersion(String name){
        if (name.contains("-")){
            String[] split = name.split("-");
            if (beginsWithNumber(split[split.length - 1]))
                return split[split.length - 1].replace(".jar", "");
        }
        return DEFAULT_VERSION;
    }
}
