package localapp.mavenrepomanager;

public final class Help implements IRunnable{
    @Override
    public void run(RunSettings settings) {
        System.out.println("The MavenRepositoryManager is a tool used to convert eclipse projects to "
                         + "Maven projects more easily. This tool is only useful if the eclipse project included local "
                         + "jar files in the .classpath file that cannot be located on a Maven repository.");
        System.out.println("Usage: java -jar MavenRepoManager.jar [arguments]");
        System.out.println(""); 
        System.out.println("Arguments:");
        System.out.printf( "    %s  [path to .classpath]  Specifies the path to the input .classpath file to read from. (required)%s", 
            ArgParser.REQUIRED_CLASSPATH_PATH_SWITCH, 
            System.lineSeparator());
        System.out.printf( "    %s  [path to repo]        Specifies the path to write the resulting local Maven repository to. (required)%s",
            ArgParser.REQUIRED_REPO_PATH_SWITCH, 
            System.lineSeparator());
        System.out.printf( "    %s  [repository name]     Specifies the name of the new Maven repository. (required)%s",
            ArgParser.REQUIRED_REPO_NAME_SWITCH, 
            System.lineSeparator());
        System.out.printf( "    %s  [output filename]     Specifies the output filename. (optional)%s", 
            ArgParser.OPTIONAL_OUTPUT_NAME_SWITCH, 
            System.lineSeparator());
        System.out.printf( "                              Defaults to \"./pomdeps.xml\"%s", 
            System.lineSeparator());
    }
}
