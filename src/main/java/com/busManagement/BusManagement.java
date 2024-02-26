package com.busManagement;

import java.util.Scanner;

public class BusManagement {
    Scanner scanner=new Scanner(System.in);
    public BusManagement() {
        Bus bus=new Bus();
        while (true)
        {
            System.out.println("-----------------------");
            System.out.println("Bus Management");
            System.out.println("1. Add Bus");
            System.out.println("2. View Bus");
            System.out.println("3. Remove Bus");
            System.out.println("4. Go Back");
            System.out.println("5. Exit");
            int choice = scanner.nextInt();

            switch (choice)
            {
                case 1:
                    bus.addBus();
                    break;
                case 2:
                    bus.viewBuses();

                    break;

                case 3:
                    bus.deleteBus();

                    break;

                case 4:
                    return;
                case 5:
                    System.exit(0);
                    System.out.println("Exit the program");
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}
