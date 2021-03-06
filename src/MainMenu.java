import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.Objects;

public class MainMenu{

    private boolean development = true;
    private final ConnectionManager cm;

    MainMenu() {
        Log.logLine("Looking for localHost SQL");

        cm = new ConnectionManager(development);
        cm.toggleServer();

        Log.logLine("Starting server clock");

        boolean end = false;

    if(!new SQL().makeConnection() && !development){
            end = true;
            Log.logLine("Critical error occurred when connecting to the database");
        }

        while (!end) {

            Log.logLine("SERVER MENU - Running " + cm.running);
            Log.logLine("1 - Exit Server");
            Log.logLine("2 - Current Connections");
            Log.logLine("3 - Server Settings");
            Log.logLine("4 - Remove dead connections");
            String input = Log.readInput(true);
            int option = -1;
            try {
                if (input != null) option = Integer.parseInt(input);
            } catch (Exception ignored) {}

            switch (option) {
                case 1:
                    end = true;
                    break;
                case 2:
                    cm.listConnections();
                    break;
                case 3:
                    subMenu();
                    break;
                case 4:
                    cm.forceRemoveDead();
                    break;
            }
        }
        cm.closeServer();
        Log.logLine("Ending connections");
        System.exit(3);
    }

    public void subMenu() {
        boolean run = true;
        while(run){
            Log.logLine("####### Server Settings #######");
            if (cm.isRunning()) Log.logLine("1> Bring server offline");
            else Log.logLine("1> Bring server online");
            Log.logLine("2> back");
            switch (Objects.requireNonNull(Log.readInput(true))){
                case "1":
                    cm.toggleServer();
                    break;
                case "2":
                    run = false;
                    break;
            }
        }
    }
}
