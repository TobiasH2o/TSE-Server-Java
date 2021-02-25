import java.io.PrintWriter;

public class Sender implements Runnable{

    private final PrintWriter out;

    private String senderText = "";

    private Thread senderThread;

    private boolean started = false;

    Sender(PrintWriter out){
        this.out = out;
    }

    public synchronized void addText(String text){
        senderText += text + "\n";
    }

    public void send(){
        if(started) {
            if (senderThread.isAlive()) {
                senderThread.interrupt();
            }
            senderThread = new Thread(this);
            senderThread.start();
        }else start();
    }

    private void start(){
        started = true;
        senderThread = new Thread(this);
        senderThread.start();
    }

    @Override
    public void run() {
        out.print(senderText);
        out.flush();
        senderText = "";
    }
}
