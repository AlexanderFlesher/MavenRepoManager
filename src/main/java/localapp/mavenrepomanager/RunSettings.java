package localapp.mavenrepomanager;

/**
 * Object to track the file paths and names used to generate a maven repository.
 */
public final class RunSettings {
    /**The default name of the pom dependencies xml */
    public static final String DEFAULT_OUTPUT_NAME = "pomdeps.xml";
    
    /**.classpath file location */
    public final String classpathFilePath;
    /**Output pom dependencies xml to be generated from .classpath */
    public final String dependencyXmlName;    
    /**Name of the output repository constructed from .classpath */
    public final String repoName;
    /**Location of the output repository constructed from .classpath */
    public final String repoPath;

    public RunSettings(
        String classpathPath, 
        String outputXmlName, 
        String repoName, 
        String repoPath) throws IllegalArgumentException
    {
        this.classpathFilePath = classpathPath;
        this.dependencyXmlName = outputXmlName;
        this.repoPath = repoPath;
        this.repoName = repoName;
        if (this.hasNullArgs())
            throw new IllegalArgumentException("Input strings may not be null.");
    }

    public RunSettings(
        String classpathPath, 
        String repoName, 
        String repoPath) throws IllegalArgumentException
    {
        this(classpathPath, DEFAULT_OUTPUT_NAME, repoName, repoPath);
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
