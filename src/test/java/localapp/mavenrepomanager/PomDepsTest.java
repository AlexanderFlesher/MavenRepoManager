package localapp.mavenrepomanager;

import java.io.File;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;

import localapp.mavenrepomanager.DebugOptions.Option;

public class PomDepsTest {
    @Test
    public void getDocument_shouldHaveCorrectRepositoryData() {
        final String name = "name";
        final String here = "." + File.separatorChar;
        final String repoUrl = "file://" + Path.of(here, name).toString();
        Entry entry = new Entry("x", "y", "z", here);
        Path validPath = Path.of(here);
        RunSettings settings = new RunSettings(validPath, validPath, name, validPath);
        PomDeps depsXml = new PomDeps(settings, entry);
        Node repository = depsXml.getDocument().getElementsByTagName("repository").item(0);
        Assert.assertEquals("id", repository.getChildNodes().item(0).getNodeName());
        Assert.assertEquals(name, repository.getChildNodes().item(0).getTextContent());
        Assert.assertEquals("name", repository.getChildNodes().item(1).getNodeName());
        Assert.assertEquals(name, repository.getChildNodes().item(1).getTextContent());
        Assert.assertEquals("url", repository.getChildNodes().item(2).getNodeName());
        Assert.assertEquals(repoUrl, repository.getChildNodes().item(2).getTextContent());
    }

    @Test
    public void write_shouldExcept_whenInvalidPathGiven(){
        Entry entry = new Entry("x", "y", "z", "." + File.separatorChar);
        Path invalidPath = Path.of("/path/to/file.xml");
        RunSettings settings = new RunSettings(invalidPath, invalidPath, "name", invalidPath);
        PomDeps depsXml = new PomDeps(settings, entry);
        Assert.assertThrows(IllegalArgumentException.class, () -> depsXml.write(Option.DEBUG));
    }
}
