/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kma.chat.entity;

import java.util.Date;

/**
 *
 * @author Dieu Huong
 */
public class User {
    private String username;
    private String password;
    private String name;
    private String gender;
    private int phone;
    private Date dayOfBirth;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String password, String name, String gender, int phone, Date dayOfBirth) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.dayOfBirth = dayOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getPhone() {
        return phone;
    }

    public Date getDayOfBirth() {
        return dayOfBirth;
    }
}
