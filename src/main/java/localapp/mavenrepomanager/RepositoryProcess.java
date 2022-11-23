package localapp.mavenrepomanager;

import java.io.IOException;

public final class RepositoryProcess extends Repository {
    public RepositoryProcess(String name){
        super(name);
    }

    public RepositoryProcess(String name, ClasspathFile file) {
        super(name, file);
    }

    @Override
    public void write() throws IOException {
        for (Entry entry : getClasspathEntries()){
            Process process = Runtime.getRuntime().exec(MavenArgs.from(entry, App.getSettings()));
            process.getInputStream().transferTo(System.out);
        }
    }
}
