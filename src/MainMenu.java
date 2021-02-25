import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainMenu implements ActionListener {

    Timer clock = new Timer(100, this);
    int heartBeat = 0;
    final int masterHeartBeat = 30;
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
            Log.logLine("3 - Force remove dead connections");
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
                case 3:
                    cm.flushDead();
                    break;
                case 2:
                    cm.listConnections();
                    break;
                case 4:
                    break;
            }
        }
        cm.closeServer();
        Log.logLine("Ending connections");
        System.exit(3);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        heartBeat++;
        if(heartBeat == masterHeartBeat) {
            cm.checkConnections();
            heartBeat = 0;
        }
    }
}
