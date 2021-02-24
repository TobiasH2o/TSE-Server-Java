import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.Timer;

public class User implements Runnable, ActionListener {

    Timer deathTimer = new Timer(100, this);
    Socket  userSocket;
    PrintWriter out;
    BufferedReader in;
    boolean dead = false;

    public User(Socket userSocket, PrintWriter out, BufferedReader in){
        this.userSocket = userSocket;
        this.out = out;
        this.in = in;
    }

    public void printDetails(){
        Log.logLine(userSocket.getLocalAddress() + ", " + userSocket.getLocalPort());
    }

    public boolean checkOnline() {
        try {
            userSocket.getInputStream().read();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void run() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(deathTimer))
            dead = true;
    }
}
