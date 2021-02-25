import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

public class Receiver implements Runnable {

    private final BufferedReader in;

    private ArrayList<String> receivedText = new ArrayList<>();

    private boolean receiving = false;
    private boolean alive = true;

    private Thread receiverThread;

    Receiver(BufferedReader in) {
        this.in = in;
    }

    public String[] getText() {
        return receivedText.toArray(String[]::new);
    }

    public void stopReceiving() {
        receiving = false;
        if (receiverThread.isAlive()) {
            receiverThread.interrupt();
        }
    }

    public void start() {
        receiving = true;
        receiverThread = new Thread(this);
        receiverThread.start();
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public void run() {
        receivedText = new ArrayList<>(0);
        while (receiving && alive) {
            try {
                String text = in.readLine();
                while (text != null) {
                    receivedText.add(text);
                    text = in.readLine();
                }
            } catch (SocketException e) {
                alive = false;
            } catch (IOException e) {
                Log.logLine("Generic Error");
            }
        }
    }

}
