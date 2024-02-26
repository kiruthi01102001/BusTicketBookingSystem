package com.registration;


class Account {
    protected int userId;
    protected String username;
    protected String password;
    protected String email;
    protected String phoneNumber;

    public Account(int userId, String username, String password, String email, String phoneNumber) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}




