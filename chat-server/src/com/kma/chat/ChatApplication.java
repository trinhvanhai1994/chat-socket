/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kma.chat;

import com.kma.chat.service.ThreadLogin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Nguyen Son
 */
public class ChatApplication {
    private int port;
    public static Hashtable<String, ThreadLogin> listUser;

    public ChatApplication(int port) {
        this.port = port;
    }

    public void execute() throws IOException {
        ServerSocket server = new ServerSocket(port);
        System.out.println("doi client ....");
        listUser = new Hashtable<>();
        while (true) {
            Socket socket = server.accept();
            ThreadLogin login = new ThreadLogin(this, socket);
            login.start();
        }
    }

    public static void main(String[] args) {
        ChatApplication sv = new ChatApplication(3333);
        try {
            sv.execute();
        } catch (IOException ex) {
            Logger.getLogger(ChatApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
