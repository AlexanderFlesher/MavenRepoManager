package localapp.mavenrepomanager;

public class App 
{
    private static RunSettings settings = RunSettings.BLANK_SETTINGS;
    public static RunSettings getSettings(){
        return App.settings;    
    }

    public static void main( String[] args )
    {
        System.out.println(MavenArgs.getOperatingSystem());
        IRunnable main;
        try{
            App.settings = ArgParser.parse(args);
            main = new RepoManager();
        }
        catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
            App.settings = RunSettings.BLANK_SETTINGS;
            main = new Help();
        }

        main.run(settings);
    }
}
