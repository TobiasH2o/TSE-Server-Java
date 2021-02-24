import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionManager implements Runnable {

    ServerSocket serverSocket;

    {
        try {
            serverSocket = new ServerSocket(420);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean acceptConnection = false;
    CopyOnWriteArrayList<User> currentUsers = new CopyOnWriteArrayList<>();
    Thread connector;

    ConnectionManager() {

    }

    public void listConnections() {
        for (User user : currentUsers) {
            user.printDetails();
        }
    }

    @Override
    public void run() {
        while(true)
            try {
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                currentUsers.add(new User(clientSocket, out, in));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void checkConnections() {
        currentUsers.removeIf(User::checkOnline);
    }

    public void endConnection() {
        try {
            serverSocket.close();
        } catch (Exception e) {
            Log.logLine(e.getMessage());
        }
        acceptConnection = false;
    }

    public void toggleConnection() {
        if (acceptConnection) {
            Log.logLine("Log-on is disabled");
        } else {
            Log.logLine("Log-on is enabled");
            connector = new Thread(this);
            connector.start();
        }
        acceptConnection = !acceptConnection;
    }
}
