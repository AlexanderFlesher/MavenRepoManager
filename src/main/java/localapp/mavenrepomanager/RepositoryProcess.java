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
            String[] args = MavenArgs.from(entry, App.getSettings());
            System.out.println(concat(args));
            Process process = Runtime.getRuntime().exec(args);
            process.getInputStream().transferTo(System.out);
        }
    }

    private String concat(String[] strings){
        String result = "";
        for (String string : strings){
            result += string + " ";
        }
        return result.trim();
    }
}
