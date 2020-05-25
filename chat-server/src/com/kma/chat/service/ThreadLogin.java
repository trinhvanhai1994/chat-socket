/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kma.chat.service;

import com.kma.chat.ChatApplication;
import com.kma.chat.dao.Authentication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Nguyen Son
 */
public class ThreadLogin extends Thread {

    private Socket socket;
    private boolean flag = true;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;

    public ThreadLogin(ChatApplication server, Socket socket) {
        this.socket = socket;
    }

    public void sendMessage(String mess) {
        Enumeration e = ChatApplication.listUser.keys();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            ChatApplication.listUser.get(name).send(mess);
        }
    }

    public void send(String mess) {
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(mess);

        } catch (IOException ex) {
            Logger.getLogger(ThreadLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //hien thi các user 
    public String getAllName() {
        Enumeration e = ChatApplication.listUser.keys();
        String name = "";

        while (e.hasMoreElements()) {
            name += e.nextElement() + "|";
        }
        return name;
    }

    public void sendAll() {
        Enumeration e = ChatApplication.listUser.keys();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            ChatApplication.listUser.get(name).send("Online_User|" + getAllName());
        }
    }

    public void ExitChat() {
        try {
            dis.close();
            dos.close();
            socket.close();
            System.out.println("da dong");
        } catch (IOException ex) {
            Logger.getLogger(ThreadLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                String message = dis.readUTF();
                StringTokenizer tokenizer = new StringTokenizer(message, "|");
                String cmd = tokenizer.nextToken();
                switch (cmd) {
                    case "Ten":
                        String name = tokenizer.nextToken();
                        ChatApplication.listUser.put(name, this);

                        break;
                    case "KT_Login":
                        do {
                            String username = tokenizer.nextToken();
                            String password = tokenizer.nextToken();
                            int results = new Authentication().checkLogin(username, password);
                            if (results == 1) {
                                dos.writeInt(1);
                                ExitChat();
                                flag = false;
                            } else if (results == -1) {
                                dos.writeInt(-1);
                                flag = false;
                            } else if (results == -2) {
                                dos.writeInt(-2);
                                flag = false;
                            }
                        } while (flag);
                        break;
                    case "TinNhan":
                        String str = tokenizer.nextToken();
                        sendMessage("TinNhan|" + str);

                        break;
                    case "Online_User":
                        sendAll();
                        break;
                    case "Thoat":
                        String nameT = tokenizer.nextToken();
                        System.out.println(" Tai Khoản Thoát : " + nameT);
                        sendMessage("Thoat|-" + nameT + "- Đã Thoát");
                        ChatApplication.listUser.remove(nameT, this);
                        sendAll();
                        break;
                }

            } catch (Exception e) {
                try {
                    dis.close();
                    dos.close();
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(ThreadLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
