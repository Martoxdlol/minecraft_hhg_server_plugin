package commands;

public class CommandsUtil {
    public static boolean hasOption(String[] args, String _arg){
        for(String arg : args){
            if (arg.equals("-"+_arg) || arg.equals("--"+_arg)) {
                return true;
            }
        }
        return false;
    }
}
