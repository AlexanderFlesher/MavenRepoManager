package localapp.mavenrepomanager;

import java.io.File;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

import localapp.mavenrepomanager.MavenArgs.OperatingSystem;

public class MavenArgsTest {
    @Test
    public void from_shouldParseData() {
        final String ARTIFACT = "artifact";
        final String GROUP = "group";
        final String VERSION = "1.4";
        final String INPUT = "input";
        final String REPO = "repo name";
        final String PATH = "repo path";
        final String OUTPUT = "." + File.separatorChar;
        final RunSettings settings = new RunSettings(Path.of(INPUT), Path.of(OUTPUT), REPO, Path.of(PATH));
        final Entry entry = new Entry(ARTIFACT, GROUP, VERSION, OUTPUT);
        final String[] args = MavenArgs.from(entry, settings);
        final String command = String.format(
            "%s org.apache.maven.plugins:maven-install-plugin:2.4:install-file -Dpackaging=jar -Dfile=%s -DgroupId=%s -DartifactId=%s -Dversion=%s -DlocalRepositoryPath=%s -X",
            MavenArgs.resolveMvn("mvn"),
            Path.of(OUTPUT).toString(),
            GROUP,
            ARTIFACT,
            VERSION,
            Path.of(PATH, REPO).toString());
        String argString = "";
        for(String arg : args){
            argString += arg + " ";
        }
        argString = argString.trim();
        Assert.assertEquals(command, argString);
    }

    @Test
    public void prependOsArgs_shouldMatchOs(){
        final String UNIX_ARGS = "";
        final String WIN_ARGS = "cmd /C ";
        Assert.assertEquals(UNIX_ARGS, MavenArgs.prependOsArgs(OperatingSystem.UNIX));
        Assert.assertEquals(WIN_ARGS, MavenArgs.prependOsArgs(OperatingSystem.WINDOWS));
    }
}
