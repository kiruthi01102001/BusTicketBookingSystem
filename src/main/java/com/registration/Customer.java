package com.registration;

class Customer extends Account {
    private int customerId;
    private String firstName;
    private String lastName;
    private String dob;
    private String gender;
    private String country;

    public Customer(int userId, String username, String password, String email, String phoneNumber,
                    int customerId, String firstName, String lastName, String dob, String gender, String country) {
        super(userId, username, password, email, phoneNumber);
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.country = country;
        
        
    }
    
    
    public static Customer createCustomer(int userId, String username, String password, String email, String phoneNumber,
       int customerId, String firstName, String lastName, String dob, String gender, String country) 
    {
    	return new Customer(userId, username, password, email, phoneNumber, customerId, firstName, lastName, dob, gender, country);
    }
}