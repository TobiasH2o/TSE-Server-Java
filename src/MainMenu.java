public class MainMenu {

    MainMenu() {
        Log.logLine("Looking for localHost SQL");
        SQL sql = new SQL();

        Log.logLine("Accepting connections");
        ConnectionManager cm = new ConnectionManager();
        cm.toggleConnection();
        boolean end = false;

        while (!end) {

            Log.readInput(false);
            Log.logLine("SERVER MENU");
            Log.logLine("1 - Exit Server");
            Log.logLine("2 - Current Connections");
            Log.logLine("3 - Connection Details");
            Log.logLine("4 - Disable/Enable log-on");
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
                case 3:
                case 4:
                    cm.toggleConnection();
            }
        }
        cm.endConnection();
        Log.logLine("Ending connections");
        System.exit(3);
    }


}
