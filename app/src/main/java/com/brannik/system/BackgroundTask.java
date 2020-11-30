package com.brannik.system;
import android.os.AsyncTask;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class BackgroundTask extends AsyncTask<String,Void,Void> {
    PrintWriter writer;
    Socket s;
    @Override
    protected Void doInBackground(String... voids) {
        try {
            String message = voids[0];
            s = new Socket("app-api.servehttp.com",6000);
            writer = new PrintWriter(s.getOutputStream());
            writer.write(message.replace(" ","_"));
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
