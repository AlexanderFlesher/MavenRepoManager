package localapp.mavenrepomanager;

import java.nio.file.Path;

/**
 * Object to track the file paths and names used to generate a maven repository.
 */
public final class RunSettings {
    /**Blank RunSettings */
    public static final RunSettings BLANK_SETTINGS = new RunSettings(Path.of(""), "", Path.of(""));
    /**The default name of the pom dependencies xml */
    public static final String DEFAULT_OUTPUT_NAME = "pomdeps.xml";
    
    /**.classpath file location */
    public final Path classpathFilePath;
    /**Output pom dependencies xml to be generated from .classpath */
    public final Path dependencyXmlName;    
    /**Name of the output repository constructed from .classpath */
    public final String repoName;
    /**Location of the output repository constructed from .classpath */
    public final Path repoPath;

    public RunSettings(
        Path classpathPath, 
        Path outputXmlName, 
        String repoName, 
        Path repoPath) throws IllegalArgumentException
    {
        this.classpathFilePath = classpathPath;
        this.dependencyXmlName = outputXmlName;
        this.repoPath = repoPath;
        this.repoName = repoName;
        if (this.hasNullArgs())
            throw new IllegalArgumentException("Input strings may not be null.");
    }

    public RunSettings(
        Path classpathPath, 
        String repoName, 
        Path repoPath) throws IllegalArgumentException
    {
        this(classpathPath, Path.of(DEFAULT_OUTPUT_NAME).toAbsolutePath(), repoName, repoPath);
    }

    private boolean hasNullArgs(){
        if (this.classpathFilePath == null ||
            this.dependencyXmlName == null ||
            this.repoName == null ||
            this.repoPath == null)
            return true;
        return false;
    }
}
