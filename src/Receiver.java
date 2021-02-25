import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Receiver implements Runnable {

    BufferedReader in;

    ArrayList<String> receivedText = new ArrayList<>();

    boolean receiving = false;

    Thread receiverThread;

    Receiver(BufferedReader in) {
        this.in = in;
    }

    public String[] getText() {
        String[] temp = receivedText.toArray(String[]::new);
        receivedText.clear();
        return temp;
    }

    public void stopReceiving(){
        receiving = false;
        if(receiverThread.isAlive()){
            receiverThread.interrupt();
        }
    }

    public void start(){
        receiving = true;
        receiverThread = new Thread(this);
        receiverThread.start();
    }

    @Override
    public void run() {
        while(receiving) {
            try {
                String text = in.readLine();
                while (text != null) {
                    receivedText.add(text);
                    text = in.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
