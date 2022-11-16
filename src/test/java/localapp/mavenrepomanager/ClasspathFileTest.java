package localapp.mavenrepomanager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClasspathFileTest{
    private InputStream file;

    @Before
    public void setupFile(){
        file = this.getClass().getResourceAsStream("example.classpath");
    }

    @Test
    public void getPaths_returnCorrectLibEntries(){
        final int LIB_ENTRIES = 3;
        final String LIB_PATH = "path";
        final String KIND = "lib";
        ClasspathFile classpath = new ClasspathFile(file);
        assertCorrectEntries(LIB_ENTRIES, LIB_PATH, KIND, classpath);
    }

    @Test
    public void getPaths_returnCorrectSrcEntries(){
        final int LIB_ENTRIES = 1;
        final String LIB_PATH = "src";
        final String KIND = "src";
        ClasspathFile classpath = new ClasspathFile(file);
        assertCorrectEntries(LIB_ENTRIES, LIB_PATH, KIND, classpath);
    }    

    @Test
    public void getPaths_returnCorrectOutputEntries(){
        final int LIB_ENTRIES = 1;
        final String LIB_PATH = "bin";
        final String KIND = "output";
        ClasspathFile classpath = new ClasspathFile(file);
        assertCorrectEntries(LIB_ENTRIES, LIB_PATH, KIND, classpath);
    }    
    
    @Test
    @SuppressWarnings("unused")
    public void getPaths_shouldInvalidArgExcept_invalidData(){
        Assert.assertThrows(IllegalArgumentException.class, 
            () -> {
                ClasspathFile classpath = new ClasspathFile(new InputStream() {
                    @Override
                    public int read() throws IOException {
                        return 0;
                    }
                });
            });
    }

    @Test
    @SuppressWarnings("unused")
    public void getPaths_shouldInvalidArgExcept_nullFile(){
        Assert.assertThrows(IllegalArgumentException.class, 
            () -> {
                ClasspathFile classpath = new ClasspathFile((File)null);
            });
    }

    @Test
    @SuppressWarnings("unused")
    public void getPaths_shouldInvalidArgExcept_nullInput(){
        Assert.assertThrows(IllegalArgumentException.class, 
            () -> {
                ClasspathFile classpath = new ClasspathFile((InputStream)null);
            });
    }

    @Test
    @SuppressWarnings("unused")
    public void getPaths_shouldInvalidArgExcept_nullPath(){
        Assert.assertThrows(IllegalArgumentException.class, 
            () -> {
                ClasspathFile classpath = new ClasspathFile((String)null);
            });
    }

    private void assertCorrectEntries(final int LIB_ENTRIES, final String LIB_PATH, final String KIND, ClasspathFile classpath) {
        Assert.assertEquals(LIB_ENTRIES, classpath.getClasspathEntries().get(KIND).size());
        for (String path : classpath.getClasspathEntries().get(KIND)){
            Assert.assertEquals(LIB_PATH, path);
        }
    }    
}
