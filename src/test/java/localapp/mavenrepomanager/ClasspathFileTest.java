package localapp.mavenrepomanager;

import java.io.File;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClasspathFileTest{
    private InputStream file;
    @Before
    public void setupFile(){
        file = this.getClass().getResourceAsStream("example.classpath");
        // file = new File("example.classpath");
    }

    @Test
    public void getPaths(){
        ClasspathFile test = new ClasspathFile(file);
        Assert.assertEquals(3, test.getLibPaths().length);
        // Assert.assertTrue(true);
    }    
}
