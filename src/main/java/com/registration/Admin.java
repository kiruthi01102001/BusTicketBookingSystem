package com.registration;

public class Admin extends Account {
    private int adminId;
    private String fullName;
    private String role;
    private String workEmail;

    public Admin(int userId, String username, String password, String email, String phoneNumber,
                 int adminId, String fullName, String role, String workEmail) {
        super(userId, username, password, email, phoneNumber);
        this.adminId = adminId;
        this.fullName = fullName;
        this.role = role;
        this.workEmail = workEmail;


    }

}
