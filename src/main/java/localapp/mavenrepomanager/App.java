package localapp.mavenrepomanager;

public class App 
{
    public static void main( String[] args )
    {
        IRunnable main;
        RunSettings settings;
        try{
            settings = ArgParser.parse(args);
            main = new RepoManager();
        }
        catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
            settings = RunSettings.BLANK_SETTINGS;
            main = new Help();
        }

        main.run(settings);
    }
}
