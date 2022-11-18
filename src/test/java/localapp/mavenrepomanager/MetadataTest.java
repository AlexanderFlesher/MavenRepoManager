package localapp.mavenrepomanager;

import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.NodeList;

public class MetadataTest {
    @Test
    public void metadata_shouldExcept_whenFilenameGiven(){
        Entry entry = new Entry("x", "y", "z");
        Path invalidPath = Path.of("/path/to/file.xml");
        Assert.assertThrows(IllegalArgumentException.class, () -> new Metadata(entry, invalidPath));
    }

    @Test
    public void metadata_shouldExcept_whenBlankPathGiven(){
        Entry entry = new Entry("x", "y", "z");
        Path invalidPath = Path.of("");
        Assert.assertThrows(IllegalArgumentException.class, () -> new Metadata(entry, invalidPath));
    }

    @Test
    public void filename_shouldPopulateWithPath(){
        final String PATH = "./";
        Entry entry = new Entry("x", "y", "z");
        Path path = Path.of(PATH);
        Metadata metadata = new Metadata(entry, path);
        Assert.assertEquals(PATH + Metadata.DEFAULT_NAME, metadata.filename);
    }

    @Test
    public void getDocument_shouldHaveCorrectData() {
        final String ARTIFACT = "artifact";
        final String GROUP = "group";
        final String VERSION = "1.4";
        Entry entry = new Entry(ARTIFACT, GROUP, VERSION);
        Metadata metadata = new Metadata(entry, Path.of("./"));
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
}
