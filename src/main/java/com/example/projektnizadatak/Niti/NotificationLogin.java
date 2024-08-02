package com.example.projektnizadatak.Niti;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NotificationLogin {
    private static final BlockingQueue<String> loginQueue = new LinkedBlockingQueue<>();

    public static void notifyLogin(String username){
        try{
            loginQueue.put(username);
        }catch (InterruptedException ex){
            Thread.currentThread().interrupt();
        }
    }

    public static String waitForLogin() throws InterruptedException{
        return loginQueue.take();
    }
}
