import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainMenu implements ActionListener {

    Timer clock = new Timer(100, this);
    SQL sql = new SQL();
    ConnectionManager cm;

    MainMenu() {
        Log.logLine("Looking for localHost SQL");

        cm = new ConnectionManager();
        cm.toggleConnection();

        Log.logLine("Starting server clock");

        clock.setActionCommand("tick");
        clock.start();

        boolean end = false;

        while (!end) {

            Log.readInput(false);
            Log.logLine("SERVER MENU");
            Log.logLine("1 - Exit Server");
            Log.logLine("2 - Current Connections");
            Log.logLine("3 - Connection Details");
            Log.logLine("4 - Server Settings");
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
                case 4:
                    cm.toggleConnection();
                    break;
            }
        }
        cm.endConnection();
        Log.logLine("Ending connections");
        System.exit(3);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        cm.checkConnections();
    }
}
