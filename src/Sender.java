import java.io.PrintWriter;

public class Sender implements Runnable{

    PrintWriter out;

    String senderText = "";

    Thread senderThread;

    Sender(PrintWriter out){
        this.out = out;
    }

    public synchronized void addText(String text){
        senderText += text + "\n";
    }

    public void send(){
        if(senderThread.isAlive()){
            senderThread.interrupt();
        }
        senderThread = new Thread(this);
        senderThread.start();
    }

    public void start(){
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
