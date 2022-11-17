package localapp.mavenrepomanager;

import java.util.ArrayList;
import java.util.List;

/**
 * An object representing the Repository directory structure constructed from the
 * classpath entries.
 */
public final class Repository {
    private String name;
    private List<Entry> classpathEntries;

    private Repository(){
        classpathEntries = new ArrayList<>();
    }

    public Repository(String name){
        this();
        this.setName(name);
    }

    public Repository(String name, ClasspathFile file){
        this(name);
        for (String path : file.getClasspathEntries().get("lib")){
            addEntry(EntryParser.parse(path));
        }
    }
    
    public void addEntry(Entry entry){
        classpathEntries.add(entry);
    }

    public void addEntry(String artifact, String group, String version){
        addEntry(new Entry(artifact, group, version));
    }

    public String getName(){
        return this.name;
    }

    public void removeEntry(Entry entry){
        classpathEntries.remove(entry);
    }

    public void removeEntry(String artifact, String group, String version){
        removeEntry(new Entry(artifact, group, version));
    }

    /**
     * Sets this Repository's name. The provided name cannot be blank or null.
     * @param name
     */
    public void setName(String name){
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name may not be blank.");
        this.name = name;
    }

    /**
     * Writes this Repository directory structure to filesystem.
     */
    public void write(){
        DirNode root = new DirNode(name);
        for (Entry entry : classpathEntries){
            root.addChild(entry.toNode());
        }
        for (DirNode entry : root.getLeafNodes()){
            entry.tryWriteDirectory();
        }
    }

    /**
     * Abstraction of an entry in a Maven Repository. It must have an artifact,
     * a groupId, and a version number.
     */
    public final static class Entry{
        private String artifact;
        private String group;
        private String version;

        public Entry(String artifact, String group, String version){
            this.artifact = artifact;
            this.group = group;
            this.version = version;
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
            for (String s : group.split(".")){
                if (node == null)
                    node = new DirNode(s);
                else {
                    next = new DirNode(s, node);
                    node = next;
                }
            }

            next = new DirNode(artifact, node);
            node = next;

            next = new DirNode(version, node);
            node = next;

            while (node.hasParent())
                node = node.getParent();

            return node;
        }
   }
}
