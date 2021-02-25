import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.Timer;

public class User implements Runnable, ActionListener {

    Thread listener = new Thread(this);
    Timer deathTimer = new Timer(100, this);
    String dataIn = "";
    Socket userSocket;
    String userName;
    String password;
    Sender sender;
    Receiver receiver;
    boolean dead = false;

    public User(Socket userSocket) {
        try {
            this.userSocket = userSocket;
            sender = new Sender(new PrintWriter(userSocket.getOutputStream()));
            receiver = new Receiver(new BufferedReader(new InputStreamReader(userSocket.getInputStream())));
            sender.addText("logon-");
            sender.run();
            Thread.sleep(10);
            receiver.run();
            receiver.getText();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isDead() {
        return dead;
    }

    public void printDetails() {
        if (dead)
            Log.logLine("##################Dead#######################");
        else
            Log.logLine("##################Live#######################");
        Log.logLine("User address " + userSocket.getLocalAddress());
        Log.logLine("User Name    " + userName);
    }

    public Socket getSocket() {
        return userSocket;
    }

    public void closeConnection() {
        try {
            userSocket.close();
            dead = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkOnline() {
        try {
            return userSocket.getInetAddress().isReachable(100);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void run() {
        while (true){

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
