package localapp.mavenrepomanager;

import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;

public class EntryTest extends XmlWriter{
    @Test
    public void from_shouldParse_withoutVersion() {
        final String NAME = "test-library";
        final String PATH = "/path/to/library/" + NAME + ".jar";
        Entry entry = Entry.from(PATH, Option.DEBUG);
        Assert.assertEquals(NAME, entry.getArtifact());
        Assert.assertEquals(NAME, entry.getGroup());
        Assert.assertEquals(Entry.DEFAULT_VERSION, entry.getVersion());
    }

    @Test
    public void from_shouldParse_withVersion() {
        final String NAME = "test-library";
        final String VERS = "2.3.5";
        final String PATH = "/path/to/library/" + NAME + "-" + VERS + ".jar";
        Entry entry = Entry.from(PATH, Option.DEBUG);
        Assert.assertEquals(NAME, entry.getArtifact());
        Assert.assertEquals(NAME, entry.getGroup());
        Assert.assertEquals(VERS, entry.getVersion());
    }
 
    @Test
    public void getDependencyLocation_hasCorrectStructure_simpleGroup(){
        final String ARTIFACT = "artifact";
        final String GROUP = "group";
        final String VERSION = "1.4";
        Entry entry = new Entry(ARTIFACT, GROUP, VERSION, "./");
        Path path = entry.getDependencyLocation();
        Assert.assertEquals(Path.of(GROUP, ARTIFACT, VERSION), path);
    }

    @Test
    public void getDependencyLocation_hasCorrectStructure_complicatedGroup(){
        final String ARTIFACT = "artifact";
        final String GROUP = "com.complicated.group";
        final String VERSION = "1.4";
        final String[] SPLIT = GROUP.split("\\.");
        Entry entry = new Entry(ARTIFACT, GROUP, VERSION, "./");
        Path path = entry.getDependencyLocation();
        Assert.assertEquals(Path.of(SPLIT[0], SPLIT[1], SPLIT[2], ARTIFACT, VERSION), path);
    }

    @Test
    public void getMetadataLocation_hasCorrectStructure_simpleGroup(){
        final String ARTIFACT = "artifact";
        final String GROUP = "group";
        Entry entry = new Entry(ARTIFACT, GROUP, "1.0", "./");
        Path path = entry.getMetadataLocation();
        Assert.assertEquals(Path.of(GROUP, ARTIFACT), path);
    }

    @Test
    public void getMetadataLocation_hasCorrectStructure_complicatedGroup(){
        final String ARTIFACT = "artifact";
        final String GROUP = "com.complicated.group";
        final String[] SPLIT = GROUP.split("\\.");
        Entry entry = new Entry(ARTIFACT, GROUP, "1.0", "./");
        Path path = entry.getMetadataLocation();
        Assert.assertEquals(Path.of(SPLIT[0], SPLIT[1], SPLIT[2], ARTIFACT), path);
    }

    @Test
    public void toNode_hasCorrectStructure_simpleGroup(){
        final String ARTIFACT = "artifact";
        final String GROUP = "group";
        final String VERSION = "1.4";
        Entry entry = new Entry(ARTIFACT, GROUP, VERSION, "./");
        DirNode node = entry.toNode();
        Assert.assertEquals(GROUP, node.getName());
        Assert.assertEquals(ARTIFACT, node.getChildren().get(0).getName());
        Assert.assertEquals(VERSION, node.getChildren().get(0).getChildren().get(0).getName());
    }

    @Test
    public void toNode_hasCorrectStructure_complicatedGroup(){
        final String ARTIFACT = "artifact";
        final String GROUP = "com.complicated.group";
        final String VERSION = "1.4";
        final String[] SPLIT = GROUP.split("\\.");
        Entry entry = new Entry(ARTIFACT, GROUP, VERSION, "./");
        DirNode node = entry.toNode();
        Assert.assertEquals(SPLIT[0], node.getName());
        node = next(node);
        Assert.assertEquals(SPLIT[1], node.getName());
        node = next(node);
        Assert.assertEquals(SPLIT[2], node.getName());
        node = next(node);
        Assert.assertEquals(ARTIFACT, node.getName());
        node = next(node);
        Assert.assertEquals(VERSION, node.getName());
    }

    @Test
    public void toXml_shouldReturnFormattedData() {
        final String ARTIFACT = "artifact";
        final String GROUP = "group";
        final String VERSION = "1.4";
        Entry entry = new Entry(ARTIFACT, GROUP, VERSION, "./");
        Element xml = entry.toXml(getDocumentBuilder().newDocument());
        Assert.assertEquals("dependency", xml.getNodeName());
        Assert.assertEquals(GROUP, xml.getElementsByTagName("groupId").item(0).getTextContent());
        Assert.assertEquals(ARTIFACT, xml.getElementsByTagName("artifactId").item(0).getTextContent());
        Assert.assertEquals(VERSION, xml.getElementsByTagName("version").item(0).getTextContent());
    }

    private DirNode next(DirNode node){
        return node.getChildren().get(0);
    }
}
