package com.reservationManagement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
public class ReservationService {
    Scanner scanner = new Scanner(System.in);

    static int reserveId = 1;
    private List<Reservation> reservations = new ArrayList<>();
    private int nextId = 1;

    public ReservationService() {
        boolean exitLoop = false;

        while (!exitLoop) {
            try {
                System.out.println("1. Make a reservation");
                System.out.println("2. View all reservations");
                System.out.println("3. Cancel a reservation");
                System.out.println("4. Exit");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        makeReservation();
                        break;
                    case 2:

                        ReservationDb rdb = new ReservationDb();
                        rdb.viewAllReservations();
                        break;
                    case 3:
                        ReservationDb rdb2 = new ReservationDb();
                        rdb2.cancelReservation();
                        break;
                    case 4:
                        exitLoop = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

        public void makeReservation(){


            BusSearchMenu bsm=new BusSearchMenu();


            System.out.println("Enter Route ID:");
            int routeId = 0;
            try {
                routeId = scanner.nextInt();

            } catch (InputMismatchException e) {
                System.out.println("Input mismatch. Please enter a valid integer.");
            }

            System.out.println("Enter Date of Travel (YYYY-MM-DD):");
            String dateOfTravelString = scanner.next();

            boolean validInput = false;
            LocalDate dateOfTravel = null;
            while (!validInput) {


                try {

                    dateOfTravel = LocalDate.parse(dateOfTravelString);
                    validInput = true;

                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
                }
            }

            scanner.nextLine();
            System.out.println("Enter User name:");
            String username = " ";
            try {
                username = scanner.nextLine();

            } catch (InputMismatchException e) {
                System.out.println("Input mismatch. Please enter a valid username .");
            }

            System.out.println("Enter Number of Seats:");
            int numberOfSeats = 0;
            try {
                numberOfSeats = scanner.nextInt();

            } catch (InputMismatchException e) {
                System.out.println("Input mismatch. Please enter a valid integer for number of seats.");
            }


            System.out.println("Enter Payment ID:");
            int paymentId = scanner.nextInt();

            System.out.println("Enter Bus ID:");
            int busId = scanner.nextInt();


            double totalFare = 0;


            Reservation reservation = Reservation.createReservation(reserveId++, routeId, dateOfTravel, username, numberOfSeats, paymentId, busId, totalFare);
            ReservationDb obj = new ReservationDb();
            obj.addReservation(reservation);
            reservations.add(reservation);
          //  System.out.println("Reservation made successfully! Reservation ID: " + reservation.getReservationId());
        }

        private void viewAllReservations () {

            System.out.println("All Reservations:");
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }

        private void cancelReservation () {

            System.out.println("Enter Reservation ID to cancel:");
            int reservationId = scanner.nextInt();


            boolean canceled = cancelReservation(reservationId);

            if (canceled) {
                System.out.println("Reservation canceled successfully!");
            } else {
                System.out.println("Reservation not found. Please enter a valid Reservation ID.");
            }
        }



            private List<Reservation> getReservations () {
                return reservations;
            }

            private Reservation getReservationById ( int id){
                for (Reservation reservation : reservations) {
                    if (reservation.getReservationId() == id) {
                        return reservation;
                    }
                }
                return null;
            }

            private boolean cancelReservation ( int id){
                Reservation reservation = getReservationById(id);
                if (reservation != null) {
                    reservations.remove(reservation);
                    return true;
                }
                return false;
            }
        }
