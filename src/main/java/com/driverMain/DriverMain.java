package com.driverMain;

import com.registration.CustomerServices;
import com.registration.ShowAdmin;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DriverMain {

    Scanner scanner = new Scanner(System.in);

    DriverMain() {
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            System.out.println("-----------------------------------------------");
            System.out.println("- BUS RESERVATION AND TICKETING SYSTEM        -");
            System.out.println("-----------------------------------------------");
            System.out.println("- Login As                                    -");
            System.out.println("- [1] Customer                                -");
            System.out.println("- [2] Admin                                   -");
            System.out.println("- [3] Exit                                    -");
            System.out.println("-----------------------------------------------");
            System.out.println("-----------------------------------------------\n");

            boolean checkInput = false;
            int choice = 0;
            while (!checkInput) {
                try {
                    System.out.println("Enter your choice:");
                    choice = scanner.nextInt();
                    checkInput = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer ");
                    scanner.next();
                }
            }

            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter 0 to Login or 1 to Registering  ");
                    int c=scanner.nextInt();
                    CustomerServices customer=new CustomerServices();
                    if (c == 0) {
                       customer.showCustomerMenu();
                    } else {
                        customer.registerCustomer();
                    }
                    break;

                case 2:
                    System.out.println("Note : Admin is registered in this System");
                    ShowAdmin admin = new ShowAdmin();
                    admin.displayAdminMenu();
                    break;

                case 3:
                    System.out.println("Exiting program...");
                    return;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    public static void main(String[] args) {
        DriverMain dm = new DriverMain();
    }
}
