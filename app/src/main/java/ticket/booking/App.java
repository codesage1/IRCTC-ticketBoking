package ticket.booking;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.entities.Ticket;
import ticket.booking.services.UserBookingService;
import ticket.booking.util.UserServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class App {

    public static void main(String[] args) {
        System.out.println("Running Train Booking System");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;
        Train trainSelectedForBooking = null;  // Initialize as null
        
        try {
            userBookingService = new UserBookingService();
        } catch (IOException ex) {
            System.out.println("There is something wrong: " + ex.getMessage());
            ex.printStackTrace();
            return;
        }

        while(option!=7) {
            try {
                System.out.println("\nChoose option");
                System.out.println("1. Sign up");
                System.out.println("2. Login");
                System.out.println("3. Fetch Bookings");
                System.out.println("4. Search Trains");
                System.out.println("5. Book a Seat");
                System.out.println("6. Cancel my Booking");
                System.out.println("7. Exit the App");
                option = scanner.nextInt();
                
                switch (option) {
                    case 1:
                        System.out.println("Enter the username to signup");
                        String nameToSignUp = scanner.next();
                        System.out.println("Enter the password to signup");
                        String passwordToSignUp = scanner.next();
                        User userToSignup = new User(nameToSignUp, passwordToSignUp, UserServiceUtil.hashPassword(passwordToSignUp), new ArrayList<>(), UUID.randomUUID().toString());
                        userBookingService.signUPUser(userToSignup);
                        break;
                    case 2:
                        System.out.println("Enter the username to Login");
                        String nameToLogin = scanner.next();
                        System.out.println("Enter the password to login");
                        String passwordToLogin = scanner.next();
                        User userToLogin = new User(nameToLogin, passwordToLogin, UserServiceUtil.hashPassword(passwordToLogin), new ArrayList<>(), UUID.randomUUID().toString());
                        try {
                            userBookingService = new UserBookingService(userToLogin);
                            System.out.println("Login successful!");
                        } catch (IOException ex) {
                            System.out.println("Login failed: Invalid username or password");
                            continue;
                        }
                        break;
                    case 3:
                        System.out.println("Fetching your bookings");
                        userBookingService.fetchBooking();
                        break;
                    case 4:
                        System.out.println("Type your source station");
                        String source = scanner.next();
                        System.out.println("Type your destination station");
                        String dest = scanner.next();
                        List<Train> trains = userBookingService.getTrains(source, dest);
                        if (trains.isEmpty()) {
                            System.out.println("No trains found for this route");
                            continue;
                        }
                        for (int i = 0; i < trains.size(); i++) {
                            Train t = trains.get(i);
                            System.out.println("\nTrain " + (i + 1) + ":");
                            System.out.println("Train ID: " + t.getTrainID());
                            System.out.println("Schedule:");
                            for (Map.Entry<String, String> entry : t.getStationTimes().entrySet()) {
                                System.out.println("  " + entry.getKey() + " at " + entry.getValue());
                            }
                        }
                        System.out.println("\nSelect a train by typing 1,2,3...");
                        int selectedIndex = scanner.nextInt() - 1;  // Convert to 0-based index
                        if (selectedIndex >= 0 && selectedIndex < trains.size()) {
                            trainSelectedForBooking = new Train(trains.get(selectedIndex));  // Use copy constructor
                            System.out.println("Train selected successfully!");
                        } else {
                            System.out.println("Invalid train selection");
                            continue;
                        }
                        break;
                    case 5:
                        if (trainSelectedForBooking.getTrainID() == null) {
                            System.out.println("Please search and select a train first (Option 4)");
                            continue;
                        }
                        System.out.println("Selected Train: " + trainSelectedForBooking.getTrainID());
                        System.out.println("\nSeat Map (0: Available, 1: Booked):");
                        List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);
                        for (int i = 0; i < seats.size(); i++) {
                            System.out.print("Row " + i + ": ");
                            for (Integer val : seats.get(i)) {
                                System.out.print(val + " ");
                            }
                            System.out.println();
                        }
                        System.out.println("\nSelect the seat by typing the row and column");
                        System.out.println("Enter the row (0-" + (seats.size()-1) + "):");
                        int row = scanner.nextInt();
                        if (row < 0 || row >= seats.size()) {
                            System.out.println("Invalid row number");
                            continue;
                        }
                        
                        System.out.println("Enter the column (0-" + (seats.get(0).size()-1) + "):");
                        int col = scanner.nextInt();
                        if (col < 0 || col >= seats.get(0).size()) {
                            System.out.println("Invalid column number");
                            continue;
                        }

                        System.out.println("Booking your seat....");
                        Boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row, col);
                        if (booked.equals(Boolean.TRUE)) {
                            System.out.println("Booked! Enjoy your journey");
                            // Create and save ticket
                            userBookingService.createTicket(trainSelectedForBooking, row, col);
                        } else {
                            System.out.println("Can't book this seat - it may already be taken");
                        }
                        break;
                    case 6:
                        if (!userBookingService.getLoginStatus()) {
                            System.out.println("Please login first");
                            continue;
                        }
                        System.out.println("\nYour current bookings:");
                        List<Ticket> tickets = userBookingService.getUser().getTicketBooked();
                        if (tickets.isEmpty()) {
                            System.out.println("You have no bookings to cancel");
                            continue;
                        }
                        
                        // Display all tickets
                        for (int i = 0; i < tickets.size(); i++) {
                            System.out.println((i + 1) + ". " + tickets.get(i).getTicketInfo());
                        }
                        
                        System.out.println("\nEnter the number of the ticket to cancel (1-" + tickets.size() + "):");
                        int ticketIndex = scanner.nextInt() - 1;
                        
                        if (ticketIndex >= 0 && ticketIndex < tickets.size()) {
                            String ticketId = tickets.get(ticketIndex).getTicketId();
                            try {
                                if (userBookingService.cancelBooking(ticketId)) {
                                    System.out.println("Ticket cancelled successfully!");
                                } else {
                                    System.out.println("Failed to cancel ticket");
                                }
                            } catch (IOException ex) {
                                System.out.println("Error cancelling ticket: " + ex.getMessage());
                            }
                        } else {
                            System.out.println("Invalid ticket number");
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception ex) {
                System.out.println("Invalid input. Please try again.");
                continue;
            }
        }
        scanner.close();
    }

    public Object getGreeting() {
        return "Hello World";
    }
}