import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MainMenu implements ActionListener {

    final int masterHeartBeat = 30;
    Timer clock = new Timer(100, this);
    int heartBeat = 0;
    SQL sql = new SQL();
    ConnectionManager cm;

    MainMenu() {
        Log.logLine("Looking for localHost SQL");

        cm = new ConnectionManager();
        cm.toggleServer();

        Log.logLine("Starting server clock");

        clock.setActionCommand("tick");
        clock.start();

        boolean end = false;

        while (!end) {

            Log.logLine("SERVER MENU - Running " + cm.running);
            Log.logLine("1 - Exit Server");
            Log.logLine("2 - Current Connections");
            Log.logLine("3 - Server Settings");
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
            Log.logLine("2> exit");
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

    @Override
    public void actionPerformed(ActionEvent e) {
        heartBeat++;
        if (heartBeat == masterHeartBeat) {
            cm.checkConnections();
            heartBeat = 0;
        }
    }
}
