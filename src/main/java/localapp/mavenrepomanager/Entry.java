package localapp.mavenrepomanager;

import java.nio.file.Path;

/**
 * Abstraction of an entry in a Maven Repository. It must have an artifact,
 * a groupId, and a version number.
 */
public final class Entry extends DebugOptions{
    public static enum Option{
        /**No options selected. Method will function normally. */
        NONE,
        /**Debug selected, which will ignore IllegalArgumentException when trying
         * to parse files that do not exist. Use this for testing purposes.
         */
        DEBUG
    }

    public static final String DEFAULT_VERSION = "1.0";

    private String artifact;
    private String group;
    private String version;
    private Dependency dependency;

    public Entry(String artifact, String group, String version, String filename){
        this.artifact = artifact;
        this.group = group;
        this.version = version;
        this.dependency = new Dependency(filename);
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
        return new Entry(artifact, group, version, filename);
    }

    /**
     * Creates a Entry based on the filename from the classpath entry. Will
     * attempt to get the version from the filename. 
     * @param filename
     * @return an Entry based on the filename from the classpath entry.
     */
    public static Entry from(String filename, Entry.Option option){
        if (option == Option.DEBUG){
            try {
                return from(filename);
            }
            catch (IllegalArgumentException ex){
                return debugFrom(filename, "./");        
            }
        }
        else 
            return from(filename);
    }

    @Override
    public boolean equals(Object r) {
        return r.hashCode() == this.hashCode();
    }

    public String getArtifact(){
        return this.artifact;
    }

    public Dependency getDependency(){
        return this.dependency;
    }

    public Path getDependencyLocation(){
        return Path.of(getMetadataLocation().toString(), this.version);
    }

    public String getGroup(){
        return this.group;
    }

    /**
     * @return the relative path to where the metadata corresponding to this entry 
     * should be written.
     */
    public Path getMetadataLocation(){
        String first;
        String[] split = splitGroup();
        String[] next = new String[split.length];
        first = split[0];
        for (int i = 0; i < split.length - 1; i++){
            next[i] = split[i + 1];
        }
        next[next.length - 1] = this.artifact;
        return Path.of(first, next);
    }

    public String getVersion(){
        return this.version;
    }

    @Override
    public int hashCode() {
        return (17 * this.artifact.hashCode()) 
            + (17 * this.group.hashCode())
            + (17 * this.version.hashCode())
            + (17 * this.dependency.hashCode());
    }

    /**
     * Creates a DirNode corresponding to the Maven Repository directory structure for 
     * an entry.
     * @return the DirNode corresponding to the directory structure of this entry in a
     * Maven repository.
     */
    public DirNode toNode(){
        DirNode node = null;
        String[] split = splitGroup();
        for (String s : split){
            if (node == null)
                node = new DirNode(s);
            else {
                node = new DirNode(s, node);
            }
        }
        node = new DirNode(this.artifact, node);
        node = new DirNode(this.version, node);
        while (node.hasParent())
            node = node.getParent();

        return node;
    }

    private static boolean beginsWithNumber(String s){
        return s.charAt(0) >= '0' && s.charAt(0) <= '9';
    }

    private static Entry debugFrom(String filename, String defaultFilename){
        String artifact, group, version;
        Path file = Path.of(filename).getFileName();
        version = parseVersion(file.toString());
        artifact = parseArtifact(file.toString(), version);
        group = artifact;
        return new Entry(artifact, group, version, defaultFilename);
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

    private String[] splitGroup() {
        String[] split = this.group.split("\\.");
        return split.length == 0 ? new String[]{this.group} : split;
    }
}