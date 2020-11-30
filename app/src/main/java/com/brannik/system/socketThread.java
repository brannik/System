package com.brannik.system;

import android.app.UiAutomation;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static android.content.Context.WIFI_SERVICE;

public class socketThread extends Thread {
    Context context = MainActivity.getAppContext();
    ServerSocket serverSocket;
    Thread Thread1 = null;
    PrintWriter output;
    public static final int SERVER_PORT = 6001;

    @Override
    public void run() {
        //Log.d("DEBUG","THREAD start");
        Thread1 = new Thread(new Thread1());
        Thread1.start();
    }

    private BufferedReader input;
    class Thread1 implements Runnable {
        @Override
        public void run() {
            Socket socket;
            try {
                serverSocket = new ServerSocket(SERVER_PORT);
                try {
                    socket = serverSocket.accept();
                    output = new PrintWriter(socket.getOutputStream());
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    new Thread(new Thread2()).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class Thread2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    String message = input.readLine();
                    if (message != null) {
                        Log.d("DEBUG","RECIEVED -> " + message); // store recieved data globaly to process it !!!
                        if(message.equals("true")){
                            MainActivity.setUserExs(true);
                            MainActivity.setUserRank(2);
                        }else{
                            MainActivity.setUserExs(false);
                        }
                    } else {
                        Thread1 = new Thread(new Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
