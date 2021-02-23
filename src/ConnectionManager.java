import java.util.ArrayList;

public class ConnectionManager implements Runnable {

    boolean acceptConnection = false;
    ArrayList currentUsers = new ArrayList<String>();
    Thread connector;

    ConnectionManager() {

    }

    @Override
    public void run() {
        while (acceptConnection) {

        }
    }

    public void endConnection(){
        connector.interrupt();
        acceptConnection = false;
    }

    public boolean toggleConnection() {
        if(acceptConnection){
            Log.logLine("Log-on is disabled");
        }else{
            Log.logLine("Log-on is enabled");
            connector = new Thread(this);
            connector.start();
        }
        acceptConnection = !acceptConnection;
        return  acceptConnection;
    }
}
