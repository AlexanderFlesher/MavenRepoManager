package localapp.mavenrepomanager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Abstraction of the actual dependency file listed under the "path" attribute of a 
 * "lib" classpathentry kind.
 */
public class Dependency {
    private File dependencyFile;

    public Dependency(File file) throws IllegalArgumentException{
        this.dependencyFile = file; 
        if (!validate(this.dependencyFile))
            throw new IllegalArgumentException(
                String.format("Listed classpath dependency \'%s\' does not exist.", file.toPath().toString()));
    }

    public Dependency(Path file) throws IllegalArgumentException{
        this(file != null ? file.toFile() : null);
    }

    public Dependency(String file) throws IllegalArgumentException{
        this(new File(file != null ? file : null));
    }

    /**
     * Writes the dependency file associated with this object to the specified destination.
     * @param destination the path to the copy of this dependency
     * @throws IOException
     */
    public void copyTo(Path destination) throws IOException{
        Path newfile = Path.of(destination.toString(), 
            this.dependencyFile.toPath().getFileName().toString());
        try (FileReader reader = new FileReader(this.dependencyFile)){
            try (FileWriter writer = new FileWriter(newfile.toFile())){
                reader.transferTo(writer);
            }
        }
    }

    public Path getPath(){
        return dependencyFile.toPath();
    }

    @Override
    public int hashCode() {
        return this.dependencyFile.toString().hashCode();
    }

    private boolean validate(File f) {
        return f != null && f.exists();
    }
}
