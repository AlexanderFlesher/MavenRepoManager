package localapp.mavenrepomanager;

import java.io.IOException;
import java.nio.file.Path;
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
     * Also writes the required metadata files to the repository.
     * @throws IOException
     */
    public void write() throws IOException{
        DirNode root = new DirNode(name);
        Metadata[] metadata = getEntryDataAndPopulateNodes(root);
        writeDirectories(root);
        writeMetadata(metadata);
    }

    private Metadata[] getEntryDataAndPopulateNodes(DirNode root) {
        Metadata[] metadata = new Metadata[this.classpathEntries.size()];
        for (int i = 0; i < this.classpathEntries.size(); i++){
            Entry entry = this.classpathEntries.get(i);
            root.addChild(entry.toNode());
            metadata[i] 
                = new Metadata(entry, Path.of(name, entry.getMetadataLocation().toString()));
        }
        return metadata;
    }

    private void writeDirectories(DirNode root) {
        for (DirNode entry : root.getLeafNodes()){
            entry.tryWriteDirectory();
        }
    }

    private void writeMetadata(Metadata[] metadata) throws IOException {
        for (Metadata data : metadata){
            data.write();
        }
    }
}
