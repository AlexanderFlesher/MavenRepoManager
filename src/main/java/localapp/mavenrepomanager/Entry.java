package localapp.mavenrepomanager;

import java.nio.file.Path;

/**
 * Abstraction of an entry in a Maven Repository. It must have an artifact,
 * a groupId, and a version number.
 */
public final class Entry{
    public static final String DEFAULT_VERSION = "1.0";

    private String artifact;
    private String group;
    private String version;

    public Entry(String artifact, String group, String version){
        this.artifact = artifact;
        this.group = group;
        this.version = version;
    }

    /**
     * Creates a Entry based on the filename from the classpath entry. Will
     * attempt to get the version from the filename. 
     * @param filename
     * @return an Entry based on the filename from the classpath entry.
     */
    public static Entry from(String filename){
        String artifact, group, version;
        Path file = Path.of(filename).getFileName();
        version = parseVersion(file.toString());
        artifact = parseArtifact(file.toString(), version);
        group = artifact;
        return new Entry(artifact, group, version);
    }

    @Override
    public boolean equals(Object r) {
        return r.hashCode() == this.hashCode();
    }

    public String getArtifact(){
        return this.artifact;
    }

    public String getGroup(){
        return this.group;
    }

    public String getVersion(){
        return this.version;
    }

    @Override
    public int hashCode() {
        return (17 * artifact.hashCode()) 
            + (17 * group.hashCode())
            + (17 * version.hashCode());
    }

    /**
     * Creates a DirNode corresponding to the Maven Repository directory structure for 
     * an entry.
     * @return the DirNode corresponding to the directory structure of this entry in a
     * Maven repository.
     */
    public DirNode toNode(){
        DirNode node = null, next;
        String[] split = this.group.split("\\.");
        split = split.length == 0 ? new String[]{this.group} : split;
        for (String s : split){
            if (node == null)
                node = new DirNode(s);
            else {
                next = new DirNode(s, node);
                node = next;
            }
        }

        next = new DirNode(this.artifact, node);
        node = next;

        next = new DirNode(this.version, node);
        node = next;

        while (node.hasParent())
            node = node.getParent();

        return node;
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