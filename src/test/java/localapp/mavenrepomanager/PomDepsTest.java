package localapp.mavenrepomanager;

import java.io.File;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

import localapp.mavenrepomanager.DebugOptions.Option;

public class PomDepsTest {
    @Test
    public void write_shouldExcept_whenInvalidPathGiven(){
        Entry entry = new Entry("x", "y", "z", "." + File.separatorChar);
        Path invalidPath = Path.of("/path/to/file.xml");
        RunSettings settings = new RunSettings(invalidPath, invalidPath, "name", invalidPath);
        PomDeps depsXml = new PomDeps(settings, entry);
        Assert.assertThrows(IllegalArgumentException.class, () -> depsXml.write(Option.DEBUG));
    }
}
