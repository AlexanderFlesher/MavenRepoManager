package localapp.mavenrepomanager;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class containing the static methods for parsing the command-line arguments.
 */
public final class ArgParser {
    public static final String OPTIONAL_OUTPUT_NAME_SWITCH = "-o";
    public static final String REQUIRED_CLASSPATH_PATH_SWITCH = "-i";
    public static final String REQUIRED_REPO_NAME_SWITCH = "-n";
    public static final String REQUIRED_REPO_PATH_SWITCH = "-p";

    /**
     * Parses the command-line arguments and generates a {@code RunSettings} from them. 
     * @param args the command-line arguments.
     * @return a {@code RunSettings} constructed from the user arguments.
     * @throws IllegalArgumentException when missing required arguments {@code -i, -n, -p},
     * or when any argument text is invalid.
     */
    public static RunSettings parse(List<String> args) throws IllegalArgumentException{
        String repoName;
        Path classpathPath, outputFile, repoPath;
        validateArguments(args);        
        outputFile = Path.of(fillOutputName(args)).toAbsolutePath();
        classpathPath = Path.of(getArgumentText(args, REQUIRED_CLASSPATH_PATH_SWITCH)).toAbsolutePath();
        repoName = getArgumentText(args, REQUIRED_REPO_NAME_SWITCH);
        repoPath = Path.of(getArgumentText(args, REQUIRED_REPO_PATH_SWITCH)).toAbsolutePath();
        if (!isBlank(outputFile))
            return new RunSettings(classpathPath, outputFile, repoName, repoPath);
        else 
            return new RunSettings(classpathPath, repoName, repoPath);
    }

    /**
     * Parses the command-line arguments and generates a {@code RunSettings} from them. 
     * @param args the command-line arguments.
     * @return a {@code RunSettings} constructed from the user arguments.
     * @throws IllegalArgumentException when missing required arguments {@code -i, -n, -p},
     * or when any argument text is invalid.
     */
    public static RunSettings parse(String[] args) throws IllegalArgumentException{
        return parse(Arrays.asList(args));
    }

    private static void checkArgument(List<String> args, String arg){
        final int outputIndex = args.indexOf(arg) + 1;
        final String outputArg; 
        File out;
        if (outputIndex >= args.size())
            throwEmptyArgument(arg);
        outputArg = args.get(outputIndex);
        if (isSwitchCharacter(outputArg))
            throwEmptyArgument(outputArg);
        out = new File(outputArg);
        if (out.isDirectory()){
            if (!out.exists())
                throwDirectoryDoesNotExist();
        }
    }

    private static void checkOptionalArgs(List<String> args){
        checkArgument(args, OPTIONAL_OUTPUT_NAME_SWITCH);
    }

    private static void checkRequiredArgs(List<String> args){
        if (!getMissingArgs(args).isEmpty()){
            String argumentList = "";
            for (String arg : args){
                argumentList += arg + ", ";
            }
            throw new IllegalArgumentException(
                String.format("Missing required arguments: %s", argumentList));
        }
        checkArgument(args, REQUIRED_CLASSPATH_PATH_SWITCH);
        checkArgument(args, REQUIRED_REPO_NAME_SWITCH);
        checkArgument(args, REQUIRED_REPO_PATH_SWITCH);
    }

    private static String fillOutputName(List<String> args){
        File out;
        String name = "";
        String path = "";
        if (!hasOptionalArgs(args))
            return "";
        out = new File(getArgumentText(args, OPTIONAL_OUTPUT_NAME_SWITCH));
        if (out.isDirectory()){
            name = RunSettings.DEFAULT_OUTPUT_NAME;
            path = out.toPath().toString();
        }
        else {
            name = out.getPath();
        }
        return path + name;
    }

    private static String getArgumentText(List<String> args, String arg){
        return args.get(args.indexOf(arg) + 1);
    }

    private static List<String> getMissingArgs(List<String> args){
        List<String> missing = new ArrayList<>();
        if (!args.contains(REQUIRED_CLASSPATH_PATH_SWITCH))
            missing.add(REQUIRED_CLASSPATH_PATH_SWITCH);
        if (!args.contains(REQUIRED_REPO_NAME_SWITCH)) 
            missing.add(REQUIRED_REPO_NAME_SWITCH);
        if (!args.contains(REQUIRED_REPO_PATH_SWITCH))
            missing.add(REQUIRED_REPO_PATH_SWITCH);
        return missing;
    }

    private static boolean hasOptionalArgs(List<String> args){
        return args.contains(OPTIONAL_OUTPUT_NAME_SWITCH);
    }

    private static boolean isBlank(Path outputFile) {
        return outputFile.toString().equals(Path.of("").toAbsolutePath().toString());
    }

    private static boolean isSwitchCharacter(String s){
        return s.equals(OPTIONAL_OUTPUT_NAME_SWITCH) ||
            s.equals(REQUIRED_CLASSPATH_PATH_SWITCH) || 
            s.equals(REQUIRED_REPO_NAME_SWITCH) || 
            s.equals(REQUIRED_REPO_PATH_SWITCH);
    }

    private static void throwDirectoryDoesNotExist(){
        throw new IllegalArgumentException("Output directory does not exist.");
    }

    private static void throwEmptyArgument(String switchChar){
        throw new IllegalArgumentException(String.format("Argument %s is blank.", switchChar));
    }

    private static void validateArguments(List<String> args){
        checkRequiredArgs(args);
        if (hasOptionalArgs(args))
            checkOptionalArgs(args);
    }
}
