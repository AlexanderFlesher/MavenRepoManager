package localapp.mavenrepomanager;

import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

public class ArgParserTest {
    @Test
    public void parse_shouldExcept_missingArguments() {
        String[] input = new String[0];
        Assert.assertThrows(IllegalArgumentException.class, () -> ArgParser.parse(input));
    }

    @Test
    public void parse_shouldExcept_missingArgumentText() {
        final String TEST_TEXT = "test";
        String[] input = {"-i", TEST_TEXT, "-n", TEST_TEXT, "-p", TEST_TEXT, "-o"};
        Assert.assertThrows(IllegalArgumentException.class, () -> ArgParser.parse(input));
    }

    @Test
    public void parse_shouldExcept_missingArgumentTextMiddle() {
        final String TEST_TEXT = "test";
        String[] input = {"-i", TEST_TEXT, "-n", "-p", TEST_TEXT, "-o"};
        Assert.assertThrows(IllegalArgumentException.class, () -> ArgParser.parse(input));
    }

    @Test
    public void parse_shouldParseArgText_noOptionalArg() {
        final String INPUT = "input";
        final String REPO = "repo name";
        final String PATH = "repo path";
        String[] input = {"-i", INPUT, "-n", REPO, "-p", PATH};
        RunSettings settings = ArgParser.parse(input);
        Assert.assertEquals(Path.of(INPUT).toAbsolutePath().toString(), 
            settings.classpathFilePath.toString());
        Assert.assertEquals(REPO, settings.repoName);
        Assert.assertEquals(Path.of(PATH).toAbsolutePath().toString(), 
            settings.repoPath.toString());
        Assert.assertEquals(Path.of(RunSettings.DEFAULT_OUTPUT_NAME).toAbsolutePath().toString(), 
            settings.dependencyXmlName.toString());
    }

    @Test
    public void parse_shouldParseArgText_noOptionalArg_orderSwap() {
        final String INPUT = "input";
        final String REPO = "repo name";
        final String PATH = "repo path";
        String[] input = {"-n", REPO, "-i", INPUT, "-p", PATH};
        RunSettings settings = ArgParser.parse(input);
        Assert.assertEquals(Path.of(INPUT).toAbsolutePath().toString(), 
            settings.classpathFilePath.toString());
        Assert.assertEquals(REPO, settings.repoName);
        Assert.assertEquals(Path.of(PATH).toAbsolutePath().toString(), 
            settings.repoPath.toString());
        Assert.assertEquals(Path.of(RunSettings.DEFAULT_OUTPUT_NAME).toAbsolutePath().toString(), 
            settings.dependencyXmlName.toString());
    }

    @Test
    public void parse_shouldParseArgText_optionalArg() {
        final String INPUT = "input";
        final String REPO = "repo name";
        final String PATH = "repo path";
        final String OUTPUT = "out";
        String[] input = {"-i", INPUT, "-n", REPO, "-p", PATH, "-o", OUTPUT};
        RunSettings settings = ArgParser.parse(input);
        Assert.assertEquals(Path.of(INPUT).toAbsolutePath().toString(), 
            settings.classpathFilePath.toString());
        Assert.assertEquals(REPO, settings.repoName);
        Assert.assertEquals(Path.of(PATH).toAbsolutePath().toString(), 
            settings.repoPath.toString());
        Assert.assertEquals(Path.of(OUTPUT).toAbsolutePath().toString(), 
            settings.dependencyXmlName.toString());
    }
}
