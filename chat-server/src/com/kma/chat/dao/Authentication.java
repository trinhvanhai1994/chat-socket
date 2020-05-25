/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kma.chat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Dieu Huong
 */
public class Authentication {
    public int checkLogin(String username, String password) {
        Connection connection = ConnectDatabase.connect();
        String checkLoginQuery = "select count(*) as SoTV from user where username = ? and password = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(checkLoginQuery);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("SoTV");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -4;
    }
}
