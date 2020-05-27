/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kma.chat.dao;

import com.kma.chat.entity.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dieu Huong
 */
public class UserDAO {
    private final Connection con = ConnectDatabase.connect();

    public void Registration(User user) {
        String str = "insert into user(username, password, name, gender, phone, dayofbirth) values(?,?,?,?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(str);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getGender());
            ps.setInt(5, user.getPhone());
            ps.setDate(6, new Date(user.getDayOfBirth().getTime()));

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUserByUsername(String username) {
        String query = "select * from user where username = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new User(rs.getString(1), rs.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String str = "select * from user";
        try {
            PreparedStatement ps = con.prepareStatement(str);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getString(1));
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
