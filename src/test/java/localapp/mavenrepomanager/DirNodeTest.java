package localapp.mavenrepomanager;

import java.nio.file.Path;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class DirNodeTest {
    @Test
    public void node_shouldExcept_blankName(){
        Assert.assertThrows(IllegalArgumentException.class, () -> new DirNode(""));
    }

    @Test
    public void addChild_shouldAddChild(){
        DirNode parent = new DirNode("parent");
        DirNode child = new DirNode("child");
        parent.addChild(child);
        Assert.assertEquals(parent.getChildren().get(0), child);
    }

    @Test
    public void addChild_shouldAddParent(){
        DirNode parent = new DirNode("parent");
        DirNode child = new DirNode("child");
        parent.addChild(child);
        Assert.assertEquals(parent, child.getParent());
    }

    @Test
    public void getLeafNodes_shouldReturnChildlessNodes(){
        final int EXPECTED_LENGTH = 2;
        DirNode parent = new DirNode("parent");
        DirNode child1 = new DirNode("child1");
        DirNode child2 = new DirNode("child2");
        DirNode child3 = new DirNode("child3");
        parent.addChild(child1);
        parent.addChild(child2);
        child2.addChild(child3);
        List<DirNode> leaves = parent.getLeafNodes();
        Assert.assertEquals(EXPECTED_LENGTH, leaves.size());
        Assert.assertTrue(leaves.contains(child1));
        Assert.assertTrue(leaves.contains(child3));
        Assert.assertFalse(leaves.contains(child2));
        Assert.assertFalse(leaves.contains(parent));
    }

    @Test
    public void removeChild_shouldRemoveChild(){
        DirNode parent = new DirNode("parent");
        DirNode child = new DirNode("child");
        parent.addChild(child);
        parent.removeChild(child);
        Assert.assertTrue(parent.getChildren().isEmpty());
    }

    @Test
    public void removeChild_shoulRemoveParent(){
        DirNode parent = new DirNode("parent");
        DirNode child = new DirNode("child");
        parent.addChild(child);
        parent.removeChild(child);
        Assert.assertEquals(child.getParent(), null);
    }

    @Test
    public void setName_shouldExcept_blankName(){
        DirNode parent = new DirNode("parent");
        Assert.assertThrows(IllegalArgumentException.class, () -> parent.setName(""));
    }

    @Test
    public void setName_shouldSetName_validName(){
        final String NEW_NAME = "child";
        DirNode parent = new DirNode("parent");
        parent.setName(NEW_NAME);
        Assert.assertEquals(NEW_NAME, parent.getName());
    }

    @Test
    public void toPath_shouldOrderCorrectly(){
        final String PARENT = "parent";
        final String CHILD = "child";
        final Path EXPECTED = Path.of(PARENT, CHILD);
        DirNode parent = new DirNode("parent");
        DirNode child = new DirNode("child");
        parent.addChild(child);
        Assert.assertEquals(EXPECTED, child.toPath());
    }
}
