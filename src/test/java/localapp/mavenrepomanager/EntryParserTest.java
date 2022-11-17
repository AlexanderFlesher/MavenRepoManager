package localapp.mavenrepomanager;

import org.junit.Assert;
import org.junit.Test;

public class EntryParserTest {
    @Test
    public void parse_shouldParse_withoutVersion() {
        final String NAME = "test-library";
        final String PATH = "/path/to/library/" + NAME + ".jar";
        Repository.Entry entry = EntryParser.parse(PATH);
        Assert.assertEquals(NAME, entry.getArtifact());
        Assert.assertEquals(NAME, entry.getGroup());
        Assert.assertEquals(EntryParser.DEFAULT_VERSION, entry.getVersion());
    }

    @Test
    public void parse_shouldParse_withVersion() {
        final String NAME = "test-library";
        final String VERS = "2.3.5";
        final String PATH = "/path/to/library/" + NAME + "-" + VERS + ".jar";
        Repository.Entry entry = EntryParser.parse(PATH);
        Assert.assertEquals(NAME, entry.getArtifact());
        Assert.assertEquals(NAME, entry.getGroup());
        Assert.assertEquals(VERS, entry.getVersion());
    }

}
