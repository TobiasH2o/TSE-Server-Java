import java.io.PrintWriter;

public class Sender implements Runnable{

    PrintWriter out;

    String senderText = "";

    Sender(PrintWriter out){
        this.out = out;
    }

    public synchronized void addText(String text){
        senderText += text + "#~#~#~#~#";
    }

    @Override
    public void run() {
        out.print(senderText);
        out.flush();
        senderText = "";
    }
}
