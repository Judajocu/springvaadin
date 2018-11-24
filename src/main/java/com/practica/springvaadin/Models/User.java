package com.practica.springvaadin.Models;

public class User {

    String email;
    String firstname;
    String lastname;
    String password;
    boolean looged;

    public User(String email, String firstname, String lastname, String password, boolean looged) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.looged = looged;
    }
    public User() {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.looged = looged;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLooged() {
        return looged;
    }

    public void setLooged(boolean looged) {
        this.looged = looged;
    }
}
