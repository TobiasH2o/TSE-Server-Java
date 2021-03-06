import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;


public class ConnectionManager implements Runnable, ActionListener {

    private ServerSocket serverSocket;
    private final boolean development;

    {
        try {
            serverSocket = new ServerSocket(420);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean running = false;
    ArrayList<User> currentUsers = new ArrayList<>();
    ArrayList<Integer> deadUsers = new ArrayList<>();
    Thread connector;

    ConnectionManager(boolean development) {
        this.development = development;
        Timer deathClock = new Timer(10, this);
        deathClock.setActionCommand("checkDead");
        deathClock.start();
    }

    public void flushDead(){
        connector.interrupt();
        connector = new Thread(this);
    }

    public void listConnections() {
        Log.logLine("Server open on address >" + serverSocket.getLocalPort());
        Log.logLine("Server open on IP      >" + serverSocket.getInetAddress().getHostAddress());
        Log.logLine("Stored devices         >" + currentUsers.size());
        Log.logLine("Living devices         >" + (currentUsers.size() - deadUsers.size()));
        Log.logLine("Exit/Show");
        if(Objects.requireNonNull(Log.readInput(true)).equalsIgnoreCase("Show"))
            for (User user : currentUsers) {
                user.printDetails();
            }
        Log.logLine("#############################################");
    }

    @Override
    public void run() {
        while (running)
            try {
                if(!serverSocket.isClosed() && (SQL.isRunning() || development)) {
                    removeDeadClients();
                    Socket clientSocket = serverSocket.accept();
                    boolean duplicate = false;
                    for (User currentUser : currentUsers) {
                        if (currentUser.getSocket().getInetAddress().toString().equals(clientSocket.getInetAddress().toString())) {
                          //  duplicate = true;
                        }
                    }
                    if(!duplicate)
                        addUser(new User(clientSocket, development));
                }
            } catch (IOException ignored) {}
    }

    public void removeDeadClients(){
        currentUsers.removeIf(User::isDead);
        deadUsers.clear();
    }

    public void addUser(User user) {
        currentUsers.add(user);
    }

    public void checkConnections() {
        for (int i = 0; i < currentUsers.size(); i++) {
            if (currentUsers.get(i).isDead() && !deadUsers.contains(i)) {
                deadUsers.add(i);
                currentUsers.get(i).closeConnection();
            }
        }

    }

    public void closeServer() {
        try {
            connector.interrupt();
            serverSocket.close();
            for(User user : currentUsers){
                user.closeConnection();
            }
        } catch (Exception e) {
            Log.logLine(e.getMessage());
        }
        running = false;
    }

    public boolean isRunning(){
        return running;
    }

    public void toggleServer() {
        if (running) {
            Log.logLine("Log-on is disabled");
            connector.interrupt();
        } else {
            Log.logLine("Log-on is enabled");
            connector = new Thread(this);
            connector.start();
        }
        running = !running;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String c = e.getActionCommand();
        if(c.equals("checkDead")){
            checkConnections();
        }
    }

    public void forceRemoveDead() {
        if (connector.isAlive()) {
            connector.interrupt();
        }
        removeDeadClients();
        connector = new Thread(this);
        connector.start();
    }
}
