package localapp.mavenrepomanager;

import org.junit.Assert;
import org.junit.Test;

public class EntryTest {
    @Test
    public void from_shouldParse_withoutVersion() {
        final String NAME = "test-library";
        final String PATH = "/path/to/library/" + NAME + ".jar";
        Entry entry = Entry.from(PATH);
        Assert.assertEquals(NAME, entry.getArtifact());
        Assert.assertEquals(NAME, entry.getGroup());
        Assert.assertEquals(Entry.DEFAULT_VERSION, entry.getVersion());
    }

    @Test
    public void from_shouldParse_withVersion() {
        final String NAME = "test-library";
        final String VERS = "2.3.5";
        final String PATH = "/path/to/library/" + NAME + "-" + VERS + ".jar";
        Entry entry = Entry.from(PATH);
        Assert.assertEquals(NAME, entry.getArtifact());
        Assert.assertEquals(NAME, entry.getGroup());
        Assert.assertEquals(VERS, entry.getVersion());
    }

    @Test
    public void toNode_hasCorrectStructure_simpleGroup(){
        final String ARTIFACT = "artifact";
        final String GROUP = "group";
        final String VERSION = "1.4";
        Entry entry = new Entry(ARTIFACT, GROUP, VERSION);
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
        Entry entry = new Entry(ARTIFACT, GROUP, VERSION);
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

    private DirNode next(DirNode node){
        return node.getChildren().get(0);
    }
}
