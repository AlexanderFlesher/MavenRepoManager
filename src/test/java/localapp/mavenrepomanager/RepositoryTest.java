package localapp.mavenrepomanager;

import org.junit.Assert;
import org.junit.Test;

public class RepositoryTest {
    @Test
    public void repository_shouldExcept_blankName(){
        Assert.assertThrows(IllegalArgumentException.class, () -> new Repository(""));
    }

    @Test
    public void setName_shouldExcept_blankName(){
        Repository parent = new Repository("parent");
        Assert.assertThrows(IllegalArgumentException.class, () -> parent.setName(""));
    }

    @Test
    public void setName_shouldSetName_validName(){
        final String NEW_NAME = "child";
        Repository parent = new Repository("parent");
        parent.setName(NEW_NAME);
        Assert.assertEquals(NEW_NAME, parent.getName());
    }
}
