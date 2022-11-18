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
            addEntry(Entry.from(path));
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
}
