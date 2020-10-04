package info.infosite.functions.messaging;

import org.springframework.stereotype.Service;

import java.io.IOException;
//Don't work net send
@Service
public class Messaging {
    public void SendMessage(String message, String destination) {
        try {
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec("net send 192.168.10.9 msg");
            pr.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
