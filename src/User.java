import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.Timer;

public class User implements ActionListener {

    Socket userSocket;
    Timer selfUpdater = new Timer(100, this);
    int userID;
    String userName = "";
    String password = "";
    Thread sendThread;
    Sender sender;
    Receiver receiver;
    Commands com = new Commands();
    boolean loggedOn = false;
    boolean dead = false;

    public User(Socket userSocket) {
        try {
            this.userSocket = userSocket;
            sender = new Sender(new PrintWriter(userSocket.getOutputStream()));
            receiver = new Receiver(new BufferedReader(new InputStreamReader(userSocket.getInputStream())));
            sender.addText("logon");
            sender.start();
            receiver.start();
            selfUpdater.setActionCommand("selfUpdate");
            selfUpdater.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isDead() {
        return dead;
    }

    private void reload(){
        if (!userName.isBlank() && !password.isBlank()) {
            userID = com.checkUser(userName, password);
            loggedOn = true;
            com.userOnline(userID);
        }
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

    public void process(String[] text){
        for(String data : text){
            if(!loggedOn) {
                if (data.startsWith("U")) {
                    Log.logLine("New userName " + data);
                    userName = data.replaceFirst("U", "");
                    reload();
                } else if (data.startsWith("P")) {
                    Log.logLine("New password " + data);
                    password = data.replaceFirst("P", "");
                    reload();
                }
            }else{

            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String c = e.getActionCommand();
        if(c.equals("selfUpdate")){
            receiver.stopReceiving();
            process(receiver.getText());
            receiver.start();
        }
    }
}
