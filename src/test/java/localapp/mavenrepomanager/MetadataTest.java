package localapp.mavenrepomanager;

import java.io.File;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.NodeList;

import localapp.mavenrepomanager.DebugOptions.Option;

public class MetadataTest {
    @Test
    public void filename_shouldPopulateWithPath(){
        final String PATH = "." + File.separatorChar;
        Entry entry = new Entry("x", "y", "z", "." + File.separatorChar);
        Path path = Path.of(PATH);
        Metadata metadata = new Metadata(entry, path);
        Assert.assertEquals(PATH + Metadata.DEFAULT_NAME, metadata.filename);
    }

    @Test
    public void getDocument_shouldHaveCorrectData() {
        final String ARTIFACT = "artifact";
        final String GROUP = "group";
        final String VERSION = "1.4";
        Entry entry = new Entry(ARTIFACT, GROUP, VERSION, "." + File.separatorChar);
        Metadata metadata = new Metadata(entry, Path.of("." + File.separatorChar));
        NodeList children = metadata.getDocument().getDocumentElement().getChildNodes();
        NodeList versionChildren = children.item(2).getChildNodes();
        Assert.assertEquals("metadata", metadata.getDocument().getDocumentElement().getTagName());
        Assert.assertEquals("groupId", children.item(0).getNodeName());
        Assert.assertEquals(GROUP, children.item(0).getTextContent());
        Assert.assertEquals("artifactId", children.item(1).getNodeName());
        Assert.assertEquals(ARTIFACT, children.item(1).getTextContent());
        Assert.assertEquals("versioning", children.item(2).getNodeName());
        Assert.assertEquals("release", versionChildren.item(0).getNodeName());
        Assert.assertEquals(VERSION, versionChildren.item(0).getTextContent());
        Assert.assertEquals("versions", versionChildren.item(1).getNodeName());
        Assert.assertEquals("version", versionChildren.item(1).getChildNodes().item(0).getNodeName());
        Assert.assertEquals(VERSION, versionChildren.item(1).getChildNodes().item(0).getTextContent());
        Assert.assertEquals("lastUpdated", versionChildren.item(2).getNodeName());
    }

    @Test
    public void write_shouldExcept_whenFilenameGiven(){
        Entry entry = new Entry("x", "y", "z", "." + File.separatorChar);
        Path invalidPath = Path.of("/path/to/file.xml");
        Metadata metadata = new Metadata(entry, invalidPath);
        Assert.assertThrows(IllegalArgumentException.class, () -> metadata.write(Option.DEBUG));
    }

    @Test
    public void write_shouldExcept_whenBlankPathGiven(){
        Entry entry = new Entry("x", "y", "z", "." + File.separatorChar);
        Path invalidPath = Path.of("");
        Metadata metadata = new Metadata(entry, invalidPath);
        Assert.assertThrows(IllegalArgumentException.class, () -> metadata.write(Option.DEBUG));
    }
}
