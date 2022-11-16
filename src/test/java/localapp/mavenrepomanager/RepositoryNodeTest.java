package localapp.mavenrepomanager;

import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

public class RepositoryNodeTest {
    @Test
    public void node_shouldExcept_blankName(){
        Assert.assertThrows(IllegalArgumentException.class, () -> new Repository.Node(""));
    }

    @Test
    public void addChild_shouldAddChild(){
        Repository.Node parent = new Repository.Node("parent");
        Repository.Node child = new Repository.Node("child");
        parent.addChild(child);
        Assert.assertEquals(parent.getChildren().get(0), child);
    }

    @Test
    public void addChild_shouldAddParent(){
        Repository.Node parent = new Repository.Node("parent");
        Repository.Node child = new Repository.Node("child");
        parent.addChild(child);
        Assert.assertEquals(parent, child.getParent());
    }

    @Test
    public void removeChild_shouldRemoveChild(){
        Repository.Node parent = new Repository.Node("parent");
        Repository.Node child = new Repository.Node("child");
        parent.addChild(child);
        parent.removeChild(child);
        Assert.assertTrue(parent.getChildren().isEmpty());
    }

    @Test
    public void removeChild_shoulRemoveParent(){
        Repository.Node parent = new Repository.Node("parent");
        Repository.Node child = new Repository.Node("child");
        parent.addChild(child);
        parent.removeChild(child);
        Assert.assertEquals(child.getParent(), null);
    }

    @Test
    public void setName_shouldExcept_blankName(){
        Repository.Node parent = new Repository.Node("parent");
        Assert.assertThrows(IllegalArgumentException.class, () -> parent.setName(""));
    }

    @Test
    public void setName_shouldSetName_validName(){
        final String NEW_NAME = "child";
        Repository.Node parent = new Repository.Node("parent");
        parent.setName(NEW_NAME);
        Assert.assertEquals(NEW_NAME, parent.getName());
    }

    @Test
    public void toPath_shouldOrderCorrectly(){
        final String PARENT = "parent";
        final String CHILD = "child";
        final Path EXPECTED = Path.of(PARENT, CHILD);
        Repository.Node parent = new Repository.Node("parent");
        Repository.Node child = new Repository.Node("child");
        parent.addChild(child);
        Assert.assertEquals(EXPECTED, child.toPath());
    }
}
