/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kma.chat.service;

import com.kma.chat.ChatApplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Dieu Huong
 */
public class ThreadChatRoom extends Thread {

    private Socket socket;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;

    public ThreadChatRoom(Socket socket) {
        this.socket = socket;
    }

    public void sendMessage(String mess) { // gửi tin nhắn cho tất cả users đang online
        Enumeration listUserOnline = ChatApplication.listUser.keys(); // Lấy ra danh sách tất cả các user
        while (listUserOnline.hasMoreElements()) { // Kiểm tra xem còn user nào không(còn thì gửi messege tiếp)
            String name = (String) listUserOnline.nextElement(); // Lấy ra tên user được gửi
            ChatApplication.listUser.get(name).send(mess); // Gửi tin nhắn cho user đang online
        }
    }

    public void send(String mess) { // Hàm gửi tin nhắn
        try {
            dos = new DataOutputStream(socket.getOutputStream()); // Mở cổng gửi dữ liệu
            dos.writeUTF(mess); //Thực hiện gửi tin nhắn
        } catch (IOException ex) {
            Logger.getLogger(ThreadChatRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //hien thi các user 
    public String getAllName() {
        Enumeration listUserOnlines = ChatApplication.listUser.keys(); // Lấy ra danh sách các user đang online
        String name = "";
        while (listUserOnlines.hasMoreElements()) {
            name += listUserOnlines.nextElement() + "|";
        }
        return name;
    }

    public void getAllUserOnlines() { // Hàm lấy danh sách các member đang onlines để hiển thị ra màn hình Online_User
        Enumeration listUserOnlines = ChatApplication.listUser.keys(); // Lấy ra danh sách các user đang online
        while (listUserOnlines.hasMoreElements()) { // Lặp lại để gửi thông tin các đang Online_User cho tất cả các user
            String name = (String) listUserOnlines.nextElement(); // Tên user được gửi
            ChatApplication.listUser.get(name).send("Online_User|" + getAllName()); // Gửi danh sách các users online
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                dis = new DataInputStream(socket.getInputStream()); // Mở cổng đọc dữ liệu
                dos = new DataOutputStream(socket.getOutputStream()); // Mở cổng ghi(gửi) dữ liệu
                String message = dis.readUTF(); // đọc dữ liệu
                StringTokenizer tokenizer = new StringTokenizer(message, "|"); // Cắt chuỗi String ngăn cách bởi "|"
                String action = tokenizer.nextToken(); //Lấy ra được 1 trong các case(Tên | TinNhan...)
                String name = "";
                switch (action) {
                    case "Ten": //action = Ten sẽ vào đây(Khi user đăng nhập sẽ vào case này)
                        name = tokenizer.nextToken(); // Lấy ra tên user đăng nhập
                        ChatApplication.listUser.put(name, this); // Thêm vào danh sách các users online
                        break;
                    case "TinNhan": // Trường hợp gửi tin nhắn
                        name = tokenizer.nextToken(); // Lấy ra tên user gửi tin nhắn
                        sendMessage("TinNhan|" + name); // Gửi tin nhắn cho tất cả các users đang online
                        break;
                    case "Online_User":
                        getAllUserOnlines(); // Hiển thị các user đang online ra màn hình
                        break;
                    case "Thoat":
                        name = tokenizer.nextToken(); // Tên user nhấn nút thoát
                        System.out.println(" Tai Khoản Thoát : " + name);
                        sendMessage("Thoat|-" + name + "- Đã Thoát");
                        ChatApplication.listUser.remove(name, this); // Xóa khỏi danh sách users online
                        getAllUserOnlines();
                        break;
                }
            } catch (Exception e) {
                try {
                    dis.close();
                    dos.close();
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(ThreadChatRoom.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
