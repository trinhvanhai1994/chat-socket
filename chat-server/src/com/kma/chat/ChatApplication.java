/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kma.chat;

import com.kma.chat.service.ThreadChatRoom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Dieu Huong
 */
public class ChatApplication {
    public static Hashtable<String, ThreadChatRoom> listUser;

    public void execute() throws IOException {
        ServerSocket server = new ServerSocket(3333); // Mở kết nối socket với port 3333
        System.out.println("doi client ....");
        listUser = new Hashtable<>();
        while (true) {
            Socket socket = server.accept(); //Tạo socket cho kết nối mới
            ThreadChatRoom login = new ThreadChatRoom(socket);
            login.start(); // Bắt đầu
        }
    }

    public static void main(String[] args) {
        ChatApplication application = new ChatApplication();
        try {
            application.execute();
        } catch (IOException ex) {
            Logger.getLogger(ChatApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
