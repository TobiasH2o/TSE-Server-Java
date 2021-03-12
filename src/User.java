import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class User implements ActionListener {

    private Socket userSocket;
    private final Timer selfUpdater = new Timer(100, this);
    private final Timer deathTimer = new Timer(1000, this);
    private String userName = "";
    private String password = "";
    private Sender sender;
    private Receiver receiver;
    private final Commands com = new Commands();
    private boolean loggedOn = false;
    private boolean dead = false;
    private boolean development;

    public User(Socket userSocket, boolean development) {
        try {
            this.userSocket = userSocket;
            sender = new Sender(new PrintWriter(userSocket.getOutputStream()));
            receiver = new Receiver(new BufferedReader(new InputStreamReader(userSocket.getInputStream())));
            sender.addText("logon");
            sender.send();
            receiver.start();
            selfUpdater.setActionCommand("selfUpdate");
            selfUpdater.start();
            deathTimer.setActionCommand("death");
            deathTimer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isDead() {
        return dead;
    }

    private void reload() {
        if (!userName.isBlank() && !password.isBlank() && !development) {
            if (com.checkUser(userName, password)) {
                loggedOn = true;
                com.userOnline();
                sender.addText("goodLog");
            }else{
                sender.addText("badLog");
            }
            sender.send();
        }
    }

    public void printDetails() {
        if (dead)
            Log.logLine("##################Dead#######################");
        else
            Log.logLine("##################Live#######################");
        Log.logLine("User address " + userSocket.getInetAddress());
        Log.logLine("User Name    " + userName);
    }

    public Socket getSocket() {
        return userSocket;
    }

    public void closeConnection() {
        if (!dead) {
            try {
                dead = true;
                selfUpdater.stop();
                deathTimer.stop();
                com.userOffline();
                receiver.stopReceiving();
                userSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void process(String[] text) {
        String key;
        String data;
        if (text.length > 0) {
            deathTimer.restart();
        }
        for (String info : text) {
            key = info.split("")[0];
            data = info.replaceFirst(key, "");
            Log.logLine(data);
            if (!loggedOn) {
                if (data.startsWith("U")) {
                    userName = data.replaceFirst("U", "");
                    reload();
                } else if (data.startsWith("P")) {
                    password = data.replaceFirst("P", "");
                    reload();
                }
            } else {

            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String c = e.getActionCommand();
        if (c.equals("selfUpdate")) {
            if (!receiver.isAlive()) {
                closeConnection();
            }
            receiver.stopReceiving();
            String[] text = receiver.getText();
            if (development) {
                for (String s : text) sender.addText("Echo: " + s);
                sender.send();
            }
            process(receiver.getText());
            receiver.start();
        } else if (c.equals("death")) {
            closeConnection();
        }
    }
}
