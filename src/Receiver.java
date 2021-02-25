import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Receiver implements Runnable{

    BufferedReader in;

    String receivedText = "";

     Receiver(BufferedReader in){
        this.in = in;
    }

    public String[] getText(){
         String[] temp = receivedText.split("#~#~#~#~#");
         receivedText = "";
         return temp;
    }

    @Override
    public void run() {
        try {
            String text = in.readLine();
            while(text != null){
                receivedText += text + "#~#~#~#~#";
                text = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
